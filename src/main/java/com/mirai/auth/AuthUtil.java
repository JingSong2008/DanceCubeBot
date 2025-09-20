package com.mirai.auth;

import java.sql.*;

public class AuthUtil {
    // MySQL 数据库连接信息
    private static final String DB_URL = "jdbc:mysql://183.66.27.19:24633/bot";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Jingsong2008";

    public static boolean isUserAuthorized(long userId) {
        String sql = "SELECT 1 FROM authorized_users WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isGroupAuthorized(long groupId) {
        String sql = "SELECT 1 FROM authorized_groups WHERE group_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, groupId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean authorizeUser(long userId) {
        String sql = "INSERT IGNORE INTO authorized_users(user_id) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean authorizeGroup(long groupId) {
        String sql = "INSERT IGNORE INTO authorized_groups(group_id) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, groupId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}