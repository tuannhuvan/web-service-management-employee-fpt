package com.enterprise.hrm.dao;

import com.enterprise.hrm.entity.Employee;
import com.enterprise.hrm.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object Implementation for Employee Management
 * Tuân thủ 100% nguyên tắc:
 * 1. Sử dụng PreparedStatement với tham số '?' - Tuyệt đối không cộng chuỗi SQL.
 * 2. Đóng tự động tài nguyên Connection, PreparedStatement, ResultSet qua try-with-resources.
 * 3. Ghi vết kiểm toán biến động lương vào bảng audit_logs theo Nghị định 13/2023/NĐ-CP.
 */
public class EmployeeDAOImpl implements EmployeeDAO {

    private static final Logger LOGGER = Logger.getLogger(EmployeeDAOImpl.class.getName());

    private static final String SQL_SELECT_ALL = "SELECT id, name, salary FROM employees ORDER BY id ASC";
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, salary FROM employees WHERE id = ?";
    private static final String SQL_CHECK_EXISTS = "SELECT COUNT(1) FROM employees WHERE id = ?";
    private static final String SQL_INSERT_EMPLOYEE = "INSERT INTO employees (id, name, salary) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_EMPLOYEE = "UPDATE employees SET name = ?, salary = ? WHERE id = ?";
    private static final String SQL_INSERT_AUDIT = "INSERT INTO audit_logs (employee_id, action_type, old_salary, new_salary, changed_by) VALUES (?, ?, ?, ?, ?)";

    @Override
    public List<Employee> getEmployees() throws SQLException {
        List<Employee> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double salary = rs.getDouble("salary");
                list.add(new Employee(id, name, salary));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi truy vấn danh sách nhân viên: " + e.getMessage(), e);
            throw e;
        }
        return list;
    }

    @Override
    public boolean addEmployees(Employee emp) throws SQLException {
        if (emp == null || emp.getId() == null || emp.getId().trim().isEmpty() || emp.getSalary() < 0) {
            LOGGER.warning("Từ chối thêm nhân viên: Dữ liệu không hợp lệ hoặc lương âm.");
            return false;
        }

        String cleanId = emp.getId().trim();
        String cleanName = emp.getName() != null ? emp.getName().trim() : "";

        if (cleanName.length() < 2) {
            LOGGER.warning("Từ chối thêm nhân viên: Tên nhân viên phải có ít nhất 2 ký tự.");
            return false;
        }

        // Bắt đầu Transaction để bảo đảm tính nguyên tử (Atomicity)
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Kiểm tra khóa chính trùng lặp
                if (checkExistsInternal(conn, cleanId)) {
                    LOGGER.warning("Từ chối thêm mới: Mã nhân viên đã tồn tại (id=" + cleanId + ")");
                    conn.rollback();
                    return false;
                }

                // 2. Chèn bản ghi nhân sự mới qua PreparedStatement
                try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_EMPLOYEE)) {
                    ps.setString(1, cleanId);
                    ps.setString(2, cleanName);
                    ps.setDouble(3, emp.getSalary());
                    int affectedRows = ps.executeUpdate();
                    if (affectedRows <= 0) {
                        conn.rollback();
                        return false;
                    }
                }

                // 3. Ghi vết kiểm toán (Audit Trail) tuân thủ Nghị định 13/2023/NĐ-CP
                try (PreparedStatement psAudit = conn.prepareStatement(SQL_INSERT_AUDIT)) {
                    psAudit.setString(1, cleanId);
                    psAudit.setString(2, "INSERT");
                    psAudit.setNull(3, java.sql.Types.DOUBLE);
                    psAudit.setDouble(4, emp.getSalary());
                    psAudit.setString(5, "SOAP_WEB_SERVICE");
                    psAudit.executeUpdate();
                } catch (SQLException auditEx) {
                    // Nếu bảng audit_logs chưa tạo hoặc lỗi, ghi log cảnh báo nhưng không làm hỏng giao dịch chính nếu không bắt buộc
                    LOGGER.log(Level.WARNING, "Không thể ghi audit_logs cho thao tác INSERT: " + auditEx.getMessage());
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Lỗi trong quá trình thêm nhân viên: " + e.getMessage(), e);
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public boolean updateEmployee(Employee emp) throws SQLException {
        if (emp == null || emp.getId() == null || emp.getId().trim().isEmpty() || emp.getSalary() < 0) {
            LOGGER.warning("Từ chối cập nhật: Dữ liệu không hợp lệ hoặc lương âm.");
            return false;
        }

        String cleanId = emp.getId().trim();
        String cleanName = emp.getName() != null ? emp.getName().trim() : "";

        if (cleanName.length() < 2) {
            LOGGER.warning("Từ chối cập nhật: Tên nhân viên phải có ít nhất 2 ký tự.");
            return false;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Lấy thông tin mức lương cũ phục vụ kiểm toán
                Double oldSalary = null;
                try (PreparedStatement psSelect = conn.prepareStatement(SQL_SELECT_BY_ID)) {
                    psSelect.setString(1, cleanId);
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) {
                            oldSalary = rs.getDouble("salary");
                        } else {
                            // Không tìm thấy nhân viên
                            LOGGER.warning("Cập nhật thất bại: Không tìm thấy nhân viên có ID = " + cleanId);
                            conn.rollback();
                            return false;
                        }
                    }
                }

                // 2. Thực thi lệnh cập nhật qua PreparedStatement
                int affectedRows;
                try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_EMPLOYEE)) {
                    ps.setString(1, cleanName);
                    ps.setDouble(2, emp.getSalary());
                    ps.setString(3, cleanId);
                    affectedRows = ps.executeUpdate();
                }

                if (affectedRows <= 0) {
                    conn.rollback();
                    return false;
                }

                // 3. Ghi vết kiểm toán biến động lương
                try (PreparedStatement psAudit = conn.prepareStatement(SQL_INSERT_AUDIT)) {
                    psAudit.setString(1, cleanId);
                    psAudit.setString(2, "UPDATE");
                    if (oldSalary != null) {
                        psAudit.setDouble(3, oldSalary);
                    } else {
                        psAudit.setNull(3, java.sql.Types.DOUBLE);
                    }
                    psAudit.setDouble(4, emp.getSalary());
                    psAudit.setString(5, "SOAP_WEB_SERVICE");
                    psAudit.executeUpdate();
                } catch (SQLException auditEx) {
                    LOGGER.log(Level.WARNING, "Không thể ghi audit_logs cho thao tác UPDATE: " + auditEx.getMessage());
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Lỗi trong quá trình cập nhật nhân viên: " + e.getMessage(), e);
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public boolean existsById(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            return checkExistsInternal(conn, id.trim());
        }
    }

    @Override
    public Employee getEmployeeById(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setString(1, id.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getDouble("salary")
                    );
                }
            }
        }
        return null;
    }

    private boolean checkExistsInternal(Connection conn, String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_CHECK_EXISTS)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
