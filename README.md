# DanceCubeBot

这是一个基于**Mirai & Java**的舞立方机器人

*给个**star**或许我会很开心🥰*

## 功能介绍

用户可用功能：
- 手机号或扫码至登录机器人
- 查看个人信息（图片，战力，排行，金币，积分等等）
- 查看战力分析（图片，b15/r15，单曲详情等等）
- 查看战力截图（模仿舞立方秀绘制）
- 舞立方机台二维码登录（发送给机器人二维码）~~~~至少不用微信扫码了~~
- 查找地区在线/离线的舞立方（包括舞立方秀）
- 自动批量兑换自制谱兑换码
- Token每日自动更新
- **...更多请查看舞小铃主页使用文档**

---
管理员可用功能：

- 读取/写入 Tokens
- 设置默认 Token
- 查看个人 Token
- 强制刷新 Token

## 搭建指南

如果你只是插件使用者，只要配置好文件就行了


### 文件配置

***前情提要：不难的其实，就是第一次有一点点的麻烦了...***

***当然，后续~~可能~~会优化***

---

首先要在**与 mcl文件夹 并列**的目录下  
创建一个文件夹 `DcConfig`放入如下文件，使用如下文件结构（**注意`DcConfig`在`mcl`外面**）

```
*当然如果你没有主动配置文件夹，插件也会自己生成
- root
- mcl
 - mcl
 - plugins
 - ...
- DcConfig （见下一代码块）
  - Images
  - ...
```

---
这是DcConfig的结构（tree生成的，看不懂怪ms去）

```text
DcConfig
│  ApiKeys.yml
│  OfficialMusicIds.json
│  TokenIds.json
│  UserTokens.json
│
└─Images
    │
    ├─Cover
    │  │  default.png
    │  │
    │  ├─CustomImage
    │  │      1011.jpg
    │  │      1023.jpg
    │  │      1026.jpg ...
    │  │      default.png
    │  │
    │  └─OfficialImage
    │          101.jpg
    │          106.jpg
    │          115.jpg ...
    │          default.png
    │
    ├─UserRatioImage
    │      A.png
    │      B.png
    │      Background1.png
    │      C.png
    │      Card1.png
    │      Card2.png
    │      Card3.png
    │      D.png
    │      result.png
    │      S.png
    │      SS.png
    │      SSS.png
    │
    └─UserInfoImage
            Background1.png
            Background2.png
```

~~其实是我不会写Mirai配置文件，才把文件夹放在外面的~~

---

以下是相关文件作用

| 文件                           | 类型      | 功能           | 要求         |
|------------------------------|---------|--------------|------------|
| `Images`         | **文件夹** | 存放素材图片文件     | **手动配置**   |
| `UserTokens.json`            | 文件      | 用于保存用户令牌     | **无需手动配置** |
| `TokenIds.json` | 文件      | 用于获取二维码登录    | **手动配置**   |
| `ApiKeys.yml`    | 文件      | 用于API令牌      | **手动配置**   |
| `UserCommands.json`          | 文件      | 用于保存用户信息触发指令 | **无需手动配置** |

#### TokenIds

用于登录时获取二维码，需要在 [舞立方制谱网站](https://danceweb.shenghuayule.com/MusicMaker/#/) 上
抓包找到一个名为`Token`的POST请求，然后多复制几个负载中的`client_id`，写入`DcConfig`里面

类似于：`client_id: yyQ6*****N4WUUQ8`  的

以**json**格式写入文件如下（星号是我加的）

```json
[
  "yyQ6VxqMeIL2hceWzZ******81Ru8pIE",
  "yyQ6VxqMeILLsdi*****SnddhlyVGcNa",
  "yyQ6VxqMeILneEzfVyXPFVCZo****oH3",
  "yyQ6VxqMeIL2h**********xNf/hHSzH",
  "yyQ6Vxq******zVmQuHtNAU******xmR"
]
```

可能你会发现不管开几个标签都是一样的，可以尝试先**登录**一个二维码，再打开另一个标签页

#### ApiKeys

用于**二维码识别**和**地名转经纬度**

*本项目使用的是[**腾讯SDK**](https://cloud.tencent.com/)和[**高德地图**](https://lbs.amap.com/)
的API，每月限度充足且**免费**，所以请自行申请API令牌*

---

**当然，如果有别的需求或者使用其它第三方平台SDK，请自己修改源码**

```yaml
# 腾讯OCR SDK密钥
tencentScannerKeys:
  secretId: AKIDK****TBnFXeibIm*********
  secretKey: HLCrQoyzrZ8Z1************
# 高德地图定位 SDK密钥
gaodeMapKeys:
  apiKey: b1bbd99c****1172**************
```

#### Images

配置文件中已含有背景图片

如果想自定义模板，需要修改`Image`类的源码  
你也可以进入[即时设计](https://js.design/f/M5a8Zp)中获取本图片模板，自行设计

### 开发帮助

看不懂？翻翻源码就知道了！

### 关于构建

已实现自动化构建，可以去Action里面下载成品

## 一些提醒

如果真的有人需要搭建，以下是一些注意事项：

- 请不要高频http请求
- 请遵循开源协议
- 本项目和[**广州市胜骅动漫科技有限公司**](https://arccer.com/#/home)无关

## 鸣谢
- **感谢 艾鲁Bot 的API提供** ~~呜呜呜，我的寄鲁😭~~
- **感谢各个开发者的测试与帮助**

