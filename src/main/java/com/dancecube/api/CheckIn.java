package com.dancecube.api;

import com.dancecube.token.Token;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import static com.mirai.config.AbstractConfig.configPath;
public class CheckIn {
    private static String JDBC_DRIVER;
    private static String DB_URL;
    private static String USER;
    private static String PASS;

    static {
        loadDatabaseConfig();
    }

    private static void loadDatabaseConfig() {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = CheckIn.class.getClassLoader()
                    .getResourceAsStream(configPath + "database.yml");

            if (inputStream == null) {
                throw new RuntimeException("Database configuration file not found");
            }

            Map<String, Object> config = yaml.load(inputStream);
            @SuppressWarnings("unchecked")
            Map<String, String> databaseConfig = (Map<String, String>) config.get("database");

            JDBC_DRIVER = databaseConfig.get("jdbc-driver");
            DB_URL = databaseConfig.get("url");
            USER = databaseConfig.get("username");
            PASS = databaseConfig.get("password");

            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

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