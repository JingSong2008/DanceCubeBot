package com.dancecube.api;

import com.dancecube.token.Token;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class CheckIn {
    // Database connection details
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bot?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "Administrator";
    private static final String PASS = "Jingsong2008";

    public static boolean recordCheckIn(Token token) {
        String sql = "INSERT INTO check_in_records (user_id, check_in_time) VALUES (?, NOW())";

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Establish connection
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Set parameters
                pstmt.setInt(1, token.getUserId());

                // Execute the query
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
