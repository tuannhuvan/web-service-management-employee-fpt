package com.enterprise.hrm.service;

import com.enterprise.hrm.dao.EmployeeDAO;
import com.enterprise.hrm.entity.Employee;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test Suite cho EmployeeServiceImpl (Tầng Web Service Endpoint)
 */
public class EmployeeServiceTest {

    private EmployeeService service;
    private MockEmployeeDAO mockDAO;

    @Before
    public void setUp() {
        mockDAO = new MockEmployeeDAO();
        service = new EmployeeServiceImpl(mockDAO);
    }

    @Test
    public void testGetEmployees_Success() {
        List<Employee> list = service.getEmployees();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("EMP001", list.get(0).getId());
    }

    @Test
    public void testAddEmployees_Valid_ReturnsTrue() {
        Employee emp = new Employee("EMP003", "Le Van Cuong", 18500000.0);
        boolean result = service.addEmployees(emp);
        assertTrue(result);
    }

    @Test
    public void testAddEmployees_Null_ReturnsFalse() {
        boolean result = service.addEmployees(null);
        assertFalse(result);
    }

    @Test
    public void testAddEmployees_NegativeSalary_ReturnsFalse() {
        Employee emp = new Employee("EMP003", "Le Van Cuong", -100.0);
        boolean result = service.addEmployees(emp);
        assertFalse(result);
    }

    @Test
    public void testUpdateEmployee_Valid_ReturnsTrue() {
        Employee emp = new Employee("EMP001", "Nguyen Van An", 30000000.0);
        boolean result = service.updateEmployee(emp);
        assertTrue(result);
    }

    @Test
    public void testUpdateEmployee_NullOrEmptyId_ReturnsFalse() {
        Employee emp = new Employee("", "Nguyen Van An", 30000000.0);
        boolean result = service.updateEmployee(emp);
        assertFalse(result);
    }

    // Mock DAO Class
    private static class MockEmployeeDAO implements EmployeeDAO {
        @Override
        public List<Employee> getEmployees() throws SQLException {
            return Arrays.asList(
                    new Employee("EMP001", "Nguyen Van An", 25000000.0),
                    new Employee("EMP002", "Tran Thi Binh", 32000000.0)
            );
        }

        @Override
        public boolean addEmployees(Employee emp) throws SQLException {
            return emp != null && !"EMP_EXISTS".equals(emp.getId()) && emp.getSalary() >= 0;
        }

        @Override
        public boolean updateEmployee(Employee emp) throws SQLException {
            return emp != null && "EMP001".equals(emp.getId()) && emp.getSalary() >= 0;
        }

        @Override
        public boolean existsById(String id) throws SQLException {
            return "EMP001".equals(id);
        }

        @Override
        public Employee getEmployeeById(String id) throws SQLException {
            if ("EMP001".equals(id)) {
                return new Employee("EMP001", "Nguyen Van An", 25000000.0);
            }
            return null;
        }
    }
}
