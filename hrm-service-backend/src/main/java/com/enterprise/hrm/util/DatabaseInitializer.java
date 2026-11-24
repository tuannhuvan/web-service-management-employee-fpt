package com.enterprise.hrm.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Tiện ích khởi tạo CSDL tự động qua JDBC.
 * Đọc script database-init.sql và thực thi trên MySQL.
 */
public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    public static void main(String[] args) {
        String baseHost = "localhost:3306";
        String user = "root";

        // Thử các mật khẩu: rỗng "", "root", "123456", "password"
        String[] possiblePasswords = { "", "root", "123456", "password" };

        String foundPass = null;
        for (String p : possiblePasswords) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String rawUrl = "jdbc:mysql://" + baseHost + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                try (Connection testConn = DriverManager.getConnection(rawUrl, user, p)) {
                    LOGGER.info(">>> KẾT NỐI MYSQL THÀNH CÔNG VỚI TÀI KHOẢN: root / pass: '" + (p.isEmpty() ? "<rỗng>" : p) + "' <<<");
                    foundPass = p;
                    break;
                }
            } catch (Exception ignored) {
            }
        }

        if (foundPass == null) {
            LOGGER.severe("Không thể đăng nhập MySQL với tài khoản root.");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String rawUrl = "jdbc:mysql://" + baseHost + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            try (Connection conn = DriverManager.getConnection(rawUrl, user, foundPass);
                 Statement stmt = conn.createStatement()) {

                // 1. Tạo database và switch vào database
                stmt.execute("CREATE DATABASE IF NOT EXISTS hrm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                stmt.execute("USE hrm_db");

                // 2. Tạo bảng employees và audit_logs
                stmt.execute("DROP TABLE IF EXISTS audit_logs");
                stmt.execute("DROP TABLE IF EXISTS employees");

                stmt.execute("CREATE TABLE employees (" +
                        "id VARCHAR(20) NOT NULL PRIMARY KEY, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "salary DOUBLE NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                        "CONSTRAINT chk_employee_salary CHECK (salary >= 0)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

                stmt.execute("CREATE INDEX idx_employees_name ON employees (name)");
                stmt.execute("CREATE INDEX idx_employees_salary ON employees (salary)");

                stmt.execute("CREATE TABLE audit_logs (" +
                        "log_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "employee_id VARCHAR(20) NULL, " +
                        "action_type VARCHAR(20) NOT NULL, " +
                        "old_salary DOUBLE NULL, " +
                        "new_salary DOUBLE NULL, " +
                        "changed_by VARCHAR(50) DEFAULT 'WEB_SERVICE_CLIENT', " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "INDEX idx_audit_employee (employee_id), " +
                        "INDEX idx_audit_created (created_at)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

                // 3. Chèn 5 bản ghi mẫu
                stmt.execute("INSERT INTO employees (id, name, salary) VALUES " +
                        "('EMP001', 'Nguyen Van An', 25000000.0), " +
                        "('EMP002', 'Tran Thi Binh', 32000000.0), " +
                        "('EMP003', 'Le Van Cuong', 18500000.0), " +
                        "('EMP004', 'Pham Minh Duc', 45000000.0), " +
                        "('EMP005', 'Hoang Mai Linh', 22000000.0)");

                stmt.execute("INSERT INTO audit_logs (employee_id, action_type, old_salary, new_salary, changed_by) VALUES " +
                        "('EMP001', 'INSERT', NULL, 25000000.0, 'SYSTEM_INIT'), " +
                        "('EMP002', 'INSERT', NULL, 32000000.0, 'SYSTEM_INIT'), " +
                        "('EMP003', 'INSERT', NULL, 18500000.0, 'SYSTEM_INIT'), " +
                        "('EMP004', 'INSERT', NULL, 45000000.0, 'SYSTEM_INIT'), " +
                        "('EMP005', 'INSERT', NULL, 22000000.0, 'SYSTEM_INIT')");

                // 4. Kiểm tra lại dữ liệu
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(1) FROM employees")) {
                    if (rs.next()) {
                        LOGGER.info(">>> ĐÃ NẠP THÀNH CÔNG " + rs.getInt(1) + " BẢN GHI VÀO BẢNG employees TRONG DATABASE hrm_db! <<<");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Lỗi khởi tạo CSDL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
