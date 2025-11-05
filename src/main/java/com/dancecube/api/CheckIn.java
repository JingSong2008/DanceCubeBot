package com.dancecube.api;

import com.dancecube.token.Token;
import com.mirai.MiraiBot;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
// 修改后的CheckIn.java（仅展示变更部分）
public class CheckIn {
    // 移除原有的静态变量定义和loadDatabaseConfig方法

    public static boolean recordCheckIn(Token token) {
        String sql = "INSERT INTO check_in_records (user_id, check_in_time) VALUES (?, NOW())";

        try {
            // 使用MiraiBot中加载的数据库配置
            Class.forName(MiraiBot.JDBC_DRIVER);

            try (Connection conn = DriverManager.getConnection(
                    MiraiBot.DB_URL,
                    MiraiBot.USER,
                    MiraiBot.PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, token.getUserId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}