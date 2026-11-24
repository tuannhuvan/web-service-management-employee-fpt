package com.enterprise.hrm.client;

import com.enterprise.hrm.client.generated.Employee;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit Test kiểm tra lớp DTO và Client Stub phía Frontend
 */
public class EmployeeClientTest {

    @Test
    public void testClientEmployeeEntity() {
        Employee emp = new Employee("EMP001", "Nguyen Van An", 25000000.0);
        assertEquals("EMP001", emp.getId());
        assertEquals("Nguyen Van An", emp.getName());
        assertEquals(25000000.0, emp.getSalary(), 0.001);

        emp.setId("EMP002");
        emp.setName("Tran Thi Binh");
        emp.setSalary(30000000.0);

        assertEquals("EMP002", emp.getId());
        assertEquals("Tran Thi Binh", emp.getName());
        assertEquals(30000000.0, emp.getSalary(), 0.001);
    }

    @Test
    public void testToStringFormatting() {
        Employee emp = new Employee("EMP001", "Test", 1000.0);
        assertNotNull(emp.toString());
        assertTrue(emp.toString().contains("EMP001"));
    }
}
