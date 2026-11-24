package com.enterprise.hrm.dao;

import com.enterprise.hrm.entity.Employee;
import com.enterprise.hrm.util.DatabaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test Suite cho EmployeeDAOImpl
 * Sử dụng H2 In-Memory Database để cô lập môi trường và kiểm thử nhanh chóng.
 * Bao phủ đầy đủ các ca:
 * 1. Thêm mới thành công (Positive)
 * 2. Thêm trùng ID (Negative - DUPLICATE_KEY)
 * 3. Thêm lương âm (Negative - NEGATIVE_SALARY)
 * 4. Cập nhật thành công (Positive)
 * 5. Cập nhật nhân viên không tồn tại (Negative)
 * 6. Lấy danh sách đầy đủ (Query)
 */
public class EmployeeDAOTest {

    private EmployeeDAO employeeDAO;

    @Before
    public void setUp() throws Exception {
        // Cấu hình H2 In-Memory Database
        String h2Url = "jdbc:h2:mem:hrm_test_db;DB_CLOSE_DELAY=-1;MODE=MySQL";
        DatabaseConnection.configureCustomDataSource(h2Url, "sa", "", "org.h2.Driver");

        // Tạo bảng employees & audit_logs trong memory
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS audit_logs");
            stmt.execute("DROP TABLE IF EXISTS employees");

            stmt.execute("CREATE TABLE employees (" +
                    "id VARCHAR(20) PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "salary DOUBLE NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            stmt.execute("CREATE TABLE audit_logs (" +
                    "log_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "employee_id VARCHAR(20) NULL, " +
                    "action_type VARCHAR(20) NOT NULL, " +
                    "old_salary DOUBLE NULL, " +
                    "new_salary DOUBLE NULL, " +
                    "changed_by VARCHAR(50) DEFAULT 'TEST', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Nạp 3 bản ghi mẫu
            stmt.execute("INSERT INTO employees (id, name, salary) VALUES ('EMP001', 'Nguyen Van An', 25000000.0)");
            stmt.execute("INSERT INTO employees (id, name, salary) VALUES ('EMP002', 'Tran Thi Binh', 32000000.0)");
            stmt.execute("INSERT INTO employees (id, name, salary) VALUES ('EMP003', 'Le Van Cuong', 18500000.0)");
        }

        employeeDAO = new EmployeeDAOImpl();
    }

    @After
    public void tearDown() {
        DatabaseConnection.resetToDefaultConfiguration();
    }

    @Test
    public void testGetEmployees_ReturnsSampleData() throws SQLException {
        List<Employee> list = employeeDAO.getEmployees();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals("EMP001", list.get(0).getId());
        assertEquals("Nguyen Van An", list.get(0).getName());
        assertEquals(25000000.0, list.get(0).getSalary(), 0.001);
    }

    @Test
    public void testAddEmployee_Success() throws SQLException {
        Employee newEmp = new Employee("EMP004", "Pham Minh Duc", 45000000.0);
        boolean added = employeeDAO.addEmployees(newEmp);
        assertTrue(added);

        Employee found = employeeDAO.getEmployeeById("EMP004");
        assertNotNull(found);
        assertEquals("Pham Minh Duc", found.getName());
        assertEquals(45000000.0, found.getSalary(), 0.001);
    }

    @Test
    public void testAddEmployee_DuplicateId_Fails() throws SQLException {
        Employee dupEmp = new Employee("EMP001", "Trùng Mã", 30000000.0);
        boolean added = employeeDAO.addEmployees(dupEmp);
        assertFalse("Không được phép thêm nhân viên có ID đã tồn tại", added);
    }

    @Test
    public void testAddEmployee_NegativeSalary_Fails() throws SQLException {
        Employee invalidEmp = new Employee("EMP999", "Lương Âm", -5000.0);
        boolean added = employeeDAO.addEmployees(invalidEmp);
        assertFalse("Không được phép thêm nhân viên với mức lương âm", added);
    }

    @Test
    public void testAddEmployee_InvalidName_Fails() throws SQLException {
        Employee invalidEmp = new Employee("EMP999", "A", 10000000.0);
        boolean added = employeeDAO.addEmployees(invalidEmp);
        assertFalse("Tên nhân viên phải có ít nhất 2 ký tự", added);
    }

    @Test
    public void testUpdateEmployee_Success() throws SQLException {
        Employee updateInfo = new Employee("EMP001", "Nguyen Van An Updated", 28000000.0);
        boolean updated = employeeDAO.updateEmployee(updateInfo);
        assertTrue(updated);

        Employee found = employeeDAO.getEmployeeById("EMP001");
        assertNotNull(found);
        assertEquals("Nguyen Van An Updated", found.getName());
        assertEquals(28000000.0, found.getSalary(), 0.001);
    }

    @Test
    public void testUpdateEmployee_NonExistentId_Fails() throws SQLException {
        Employee notFound = new Employee("EMP_NOT_EXIST", "Không Tồn Tại", 50000000.0);
        boolean updated = employeeDAO.updateEmployee(notFound);
        assertFalse("Cập nhật ID không tồn tại phải trả về false", updated);
    }

    @Test
    public void testUpdateEmployee_NegativeSalary_Fails() throws SQLException {
        Employee invalidEmp = new Employee("EMP001", "Nguyen Van An", -1.0);
        boolean updated = employeeDAO.updateEmployee(invalidEmp);
        assertFalse("Không được phép cập nhật mức lương âm", updated);
    }
}
