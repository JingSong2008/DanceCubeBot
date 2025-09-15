package com.mirai.event;

import com.dancecube.token.Token;
import com.dancecube.token.TokenBuilder;
import com.mirai.auth.AuthUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.friendgroup.FriendGroup;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;

import static com.mirai.config.AbstractConfig.configPath;
import static com.mirai.config.AbstractConfig.userTokensMap;

public class MainHandler {

    // 授权检查
    private static boolean isAuthorized(Contact contact) {
        if (contact instanceof Friend friend) {
            return AuthUtil.isUserAuthorized(friend.getId());
        }
        if (contact instanceof Group group) {
            return AuthUtil.isGroupAuthorized(group.getId());
        }
        return false;
    }

    @EventHandler
    public static void eventCenter(MessageEvent event) {
        Contact contact = event.getSubject();
        MessageChain messageChain = event.getMessage();
        String message = messageChain.contentToString().trim();

        // 私聊授权
        if ("授权到私聊".equals(message)) {
            if (contact instanceof Group group) { // 检查是否在群聊中发送
                long userId = event.getSender().getId(); // 获取发送者的QQ号
                boolean result = AuthUtil.authorizeUser(userId);
                if (result) {
                    contact.sendMessage("授权成功，您的QQ号已添加到授权数据库！");
                } else {
                    contact.sendMessage("授权失败，请稍后重试。");
                }
            } else {
                contact.sendMessage("请在特定群聊中发送此命令进行授权！");
            }
            return;
        }

// 修改后的群聊授权逻辑
        if (message.startsWith("授权到群聊 ")) {
            if (contact instanceof Friend friend) { // 检查是否在私聊中发送
                String[] parts = message.split(" ", 2);
                if (parts.length == 2) {
                    try {
                        long groupId = Long.parseLong(parts[1]);
                        boolean result = AuthUtil.authorizeGroup(groupId);
                        if (result) {
                            contact.sendMessage("授权成功，群聊号 %d 已添加到授权数据库！".formatted(groupId));
                        } else {
                            contact.sendMessage("授权失败，请稍后重试。");
                        }
                    } catch (NumberFormatException e) {
                        contact.sendMessage("群聊号格式不正确，请重新输入。");
                    }
                } else {
                    contact.sendMessage("请按格式发送：授权到群聊 [群聊号]");
                }
            } else {
                contact.sendMessage("请在私聊中发送此命令进行授权！");
            }
            return;
        }

        // 正常业务授权检查
        if (!isAuthorized(contact)) {
            contact.sendMessage("未授权，无法使用本功能。");
            return;
        }

        if (messageChain.size() - 1 == messageChain.stream()
                .filter(msg -> msg instanceof At | msg instanceof PlainText)
                .toList().size()) {
            PlainTextHandler.accept(event);
        } else return;

        long qq = event.getSender().getId(); // qq发送者id

        // 文本消息检测
        switch(message) {
            case "#save" -> saveTokens(contact);
            case "#load" -> loadTokens(contact);
            case "#logout" -> logoutToken(contact);
        }
    }

    @EventHandler
    public static void NudgeHandler(NudgeEvent event) {
        Contact contact = event.getSubject();
        if (!isAuthorized(contact)) {
            contact.sendMessage("未授权，无法使用本功能。");
            return;
        }
        if(event.getTarget() instanceof Bot) {
            event.getFrom().nudge().sendTo(event.getSubject());
        }
    }

    @EventHandler
    public static void addFriendHandler(NewFriendRequestEvent event) {
        event.accept();
        Friend friend = event.getBot().getFriend(event.getFromId());
        if(friend != null) {
            friend.sendMessage("🥰呐~ 现在我们是好朋友啦！\n请到主页查看功能哦！");
            FriendGroup friendGroup = event.getBot().getFriendGroups().get(0);
            if(friendGroup != null) {
                friendGroup.moveIn(friend);
            }
        }
    }

    /**
     * 保存Token到文件JSON
     */
    public static void saveTokens(Contact contact) {
        TokenBuilder.tokensToFile(userTokensMap, configPath + "UserTokens.json");
        contact.sendMessage("保存成功！共%d条".formatted(userTokensMap.size()));
    }

    /**
     * 从文件JSON中加载Token
     */
    public static void loadTokens(Contact contact) {
        String path = configPath + "UserTokens.json";
        userTokensMap = TokenBuilder.tokensFromFile(path, false);
        contact.sendMessage("不刷新加载成功！共%d条".formatted(userTokensMap.size()));
    }

    /**
     * 注销Token
     */
    public static void logoutToken(Contact contact) {
        long qq = contact.getId();
        Token token = userTokensMap.get(qq);
        if(token == null) {
            contact.sendMessage("当前账号未登录到舞小铃！");
            return;
        }
        userTokensMap.remove(qq);
        contact.sendMessage("id:%d 注销成功！".formatted(token.getUserId()));
    }

}