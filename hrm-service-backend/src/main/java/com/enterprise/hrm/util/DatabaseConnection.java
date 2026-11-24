package com.enterprise.hrm.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Quản lý kết nối JDBC tập trung và an toàn.
 * Đọc cấu hình từ file db.properties (hoặc ghi đè bằng Biến Môi Trường/System Properties).
 * Tuyệt đối không hardcode mật khẩu hay chuỗi kết nối trong logic xử lý.
 */
public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static final Properties PROPERTIES = new Properties();

    private static String url;
    private static String username;
    private static String password;
    private static String driverClassName;

    static {
        loadConfiguration();
    }

    private static synchronized void loadConfiguration() {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                PROPERTIES.load(input);
            } else {
                LOGGER.warning("Không tìm thấy file db.properties trên Classpath. Sử dụng cấu hình mặc định/môi trường.");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đọc file cấu hình db.properties", ex);
        }

        // Ưu tiên System Properties -> Environment Variables -> db.properties -> Default Value
        url = getPropertyOrEnv("db.url", "DB_URL", 
                "jdbc:mysql://localhost:3306/hrm_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8");
        username = getPropertyOrEnv("db.user", "DB_USER", "root");
        password = getPropertyOrEnv("db.password", "DB_PASSWORD", "root");
        driverClassName = getPropertyOrEnv("db.driver", "DB_DRIVER", "com.mysql.cj.jdbc.Driver");

        try {
            Class.forName(driverClassName);
            LOGGER.info("JDBC Driver loaded successfully: " + driverClassName);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Không thể nạp JDBC Driver: " + driverClassName, e);
        }
    }

    private static String getPropertyOrEnv(String propKey, String envKey, String defaultValue) {
        String sysProp = System.getProperty(propKey);
        if (sysProp != null) {
            return sysProp.trim();
        }
        String envVal = System.getenv(envKey);
        if (envVal != null) {
            return envVal.trim();
        }
        String fileProp = PROPERTIES.getProperty(propKey);
        if (fileProp != null) {
            return fileProp.trim();
        }
        return defaultValue;
    }

    /**
     * Mở một kết nối mới tới cơ sở dữ liệu.
     * Người gọi có trách nhiệm đóng kết nối thông qua try-with-resources.
     *
     * @return Connection đối tượng kết nối JDBC
     * @throws SQLException khi không thể thiết lập kết nối
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Cho phép cấu hình động nguồn kết nối (sử dụng trong Unit Test như H2 In-Memory DB).
     */
    public static synchronized void configureCustomDataSource(String customUrl, String customUser, String customPass, String customDriver) {
        url = customUrl;
        username = customUser;
        password = customPass;
        driverClassName = customDriver;
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi nạp custom driver: " + customDriver, e);
        }
    }

    /**
     * Khôi phục cấu hình mặc định từ file properties.
     */
    public static synchronized void resetToDefaultConfiguration() {
        loadConfiguration();
    }
}
