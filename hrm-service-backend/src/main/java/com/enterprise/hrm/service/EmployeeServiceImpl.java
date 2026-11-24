package com.enterprise.hrm.service;

import com.enterprise.hrm.dao.EmployeeDAO;
import com.enterprise.hrm.dao.EmployeeDAOImpl;
import com.enterprise.hrm.entity.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;

/**
 * Service Implementation Bean (SIB) - Employee Web Service Implementation
 * Triển khai Service Endpoint Interface (SEI) com.enterprise.hrm.service.EmployeeService.
 * Phục vụ các yêu cầu SOAP từ Web Client hoặc các hệ thống bên thứ ba.
 */
@WebService(
    serviceName = "EmployeeWebService",
    portName = "EmployeeWebServicePort",
    endpointInterface = "com.enterprise.hrm.service.EmployeeService",
    targetNamespace = "urn:techfirm:hrm:service:endpoint"
)
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class.getName());
    private final EmployeeDAO employeeDAO;

    /**
     * Default Constructor cho JAX-WS Runtime.
     */
    public EmployeeServiceImpl() {
        this.employeeDAO = new EmployeeDAOImpl();
    }

    /**
     * Constructor hỗ trợ Dependency Injection (phục vụ Unit Testing với Mock DAO).
     *
     * @param employeeDAO DAO triển khai
     */
    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> getEmployees() {
        LOGGER.info("[WebMethod: getEmployees] Bắt đầu truy xuất danh sách nhân sự...");
        try {
            List<Employee> list = employeeDAO.getEmployees();
            LOGGER.info("[WebMethod: getEmployees] Truy xuất thành công: " + list.size() + " bản ghi.");
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[WebMethod: getEmployees] Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage(), e);
            // Theo hợp đồng: không để lỗi null pointer làm sập client, trả về danh sách rỗng an toàn
            return new ArrayList<>();
        }
    }

    @Override
    public boolean addEmployees(Employee emp) {
        if (emp == null) {
            LOGGER.warning("[WebMethod: addEmployees] Từ chối: Đối tượng nhân viên null.");
            return false;
        }

        String id = emp.getId();
        double salary = emp.getSalary();

        LOGGER.info("[WebMethod: addEmployees] Tiếp nhận thêm mới: ID=" + id + ", Name=" + emp.getName());

        // Kiểm tra điều kiện bất biến nghiệp vụ
        if (id == null || id.trim().isEmpty()) {
            LOGGER.warning("[WebMethod: addEmployees] Từ chối: ID rỗng.");
            return false;
        }

        if (salary < 0.0) {
            LOGGER.warning("[WebMethod: addEmployees] Từ chối: Mức lương âm (" + salary + " < 0).");
            return false;
        }

        try {
            boolean success = employeeDAO.addEmployees(emp);
            if (success) {
                LOGGER.info("[WebMethod: addEmployees] Thêm thành công nhân viên ID: " + id);
            } else {
                LOGGER.warning("[WebMethod: addEmployees] Thêm thất bại (có thể do trùng ID hoặc vi phạm quy tắc).");
            }
            return success;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[WebMethod: addEmployees] Lỗi CSDL khi thêm nhân viên: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateEmployee(Employee emp) {
        if (emp == null) {
            LOGGER.warning("[WebMethod: updateEmployee] Từ chối: Đối tượng nhân viên null.");
            return false;
        }

        String id = emp.getId();
        double salary = emp.getSalary();

        LOGGER.info("[WebMethod: updateEmployee] Tiếp nhận cập nhật: ID=" + id);

        if (id == null || id.trim().isEmpty()) {
            LOGGER.warning("[WebMethod: updateEmployee] Từ chối: ID rỗng.");
            return false;
        }

        if (salary < 0.0) {
            LOGGER.warning("[WebMethod: updateEmployee] Từ chối: Mức lương âm (" + salary + " < 0).");
            return false;
        }

        try {
            boolean success = employeeDAO.updateEmployee(emp);
            if (success) {
                LOGGER.info("[WebMethod: updateEmployee] Cập nhật thành công nhân viên ID: " + id);
            } else {
                LOGGER.warning("[WebMethod: updateEmployee] Cập nhật thất bại: Không tìm thấy ID=" + id);
            }
            return success;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[WebMethod: updateEmployee] Lỗi CSDL khi cập nhật nhân viên: " + e.getMessage(), e);
            return false;
        }
    }
}
