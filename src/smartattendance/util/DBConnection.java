package smartattendance.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();

            // Read from config.properties
            config.setJdbcUrl(ConfigUtil.getProperty("db.url"));
            config.setUsername(ConfigUtil.getProperty("db.username"));
            config.setPassword(ConfigUtil.getProperty("db.password"));

            // Hikari must set all configs BEFORE creating pool
            config.setAutoCommit(true);            // FIX ✔

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(4);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(600000);
            config.setConnectionTimeout(30000);

            // Create pool AFTER setting everything
            dataSource = new HikariDataSource(config);

            // Optional shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (dataSource != null && !dataSource.isClosed()) {
                    dataSource.close();
                    System.out.println("HikariCP pool closed on app shutdown");
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize HikariCP", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
