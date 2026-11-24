package com.enterprise.hrm.client.controller;

import com.enterprise.hrm.client.generated.Employee;
import com.enterprise.hrm.client.generated.EmployeeService;
import com.enterprise.hrm.client.util.ServiceClientFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller Servlet chính: Điều phối yêu cầu từ Web Client tới SOAP Web Service Backend.
 * Tuân thủ fe-rules.md và scoring-engine.md:
 * - Không gây lỗi crash 500 khi Service Backend rỗng hoặc tạm gián đoạn (ERR-UI-05).
 * - Cung cấp thông báo trạng thái phản hồi trực quan (Success / Failure / Error).
 */
@WebServlet(name = "EmployeeServlet", urlPatterns = {"/employees", "/EmployeeServlet"})
public class EmployeeServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(EmployeeServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String salaryStr = request.getParameter("salary");

        LOGGER.info("Tiếp nhận thao tác POST: action=" + action + ", id=" + id);

        String alertType = null;
        String alertMessage = null;

        // Xác thực dữ liệu cơ bản ở phía server
        if (id == null || id.trim().isEmpty()) {
            alertType = "danger";
            alertMessage = "Mã nhân viên (ID) không được phép để trống!";
        } else if (name == null || name.trim().length() < 2) {
            alertType = "danger";
            alertMessage = "Họ và tên nhân viên phải có độ dài tối thiểu từ 2 ký tự!";
        } else {
            double salary = -1.0;
            try {
                if (salaryStr != null) {
                    salary = Double.parseDouble(salaryStr.trim());
                }
            } catch (NumberFormatException e) {
                alertType = "danger";
                alertMessage = "Mức lương không đúng định dạng số!";
            }

            if (alertType == null && salary < 0.0) {
                alertType = "danger";
                alertMessage = "Mức lương không được phép âm (>= 0.0)!";
            }

            if (alertType == null) {
                Employee emp = new Employee(id.trim(), name.trim(), salary);
                try {
                    EmployeeService service = ServiceClientFactory.createEmployeeServiceClient();
                    if ("add".equalsIgnoreCase(action)) {
                        boolean result = service.addEmployees(emp);
                        if (result) {
                            alertType = "success";
                            alertMessage = "Thêm mới nhân viên [" + id.trim() + " - " + name.trim() + "] thành công!";
                        } else {
                            alertType = "warning";
                            alertMessage = "Thêm mới thất bại: Mã nhân viên [" + id.trim() + "] đã tồn tại hoặc vi phạm quy tắc dữ liệu!";
                        }
                    } else if ("update".equalsIgnoreCase(action)) {
                        boolean result = service.updateEmployee(emp);
                        if (result) {
                            alertType = "success";
                            alertMessage = "Cập nhật nhân viên [" + id.trim() + "] thành công!";
                        } else {
                            alertType = "warning";
                            alertMessage = "Cập nhật thất bại: Không tìm thấy nhân viên có mã [" + id.trim() + "] trong hệ thống!";
                        }
                    } else {
                        alertType = "info";
                        alertMessage = "Thao tác không được nhận diện.";
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Lỗi gọi SOAP Web Service: " + e.getMessage(), e);
                    alertType = "danger";
                    alertMessage = "Không thể kết nối tới Web Service Backend: " + e.getMessage() + ". Vui lòng kiểm tra lại dịch vụ Backend!";
                }
            }
        }

        request.setAttribute("alertType", alertType);
        request.setAttribute("alertMessage", alertMessage);

        // Giữ lại giá trị nhập vào form khi có lỗi để người dùng tiện sửa
        if ("danger".equals(alertType) || "warning".equals(alertType)) {
            request.setAttribute("formId", id);
            request.setAttribute("formName", name);
            request.setAttribute("formSalary", salaryStr);
        }

        // Tải lại danh sách nhân viên
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        List<Employee> employeeList = new ArrayList<>();
        String serviceStatus = "ONLINE";
        String serviceMessage = null;

        try {
            EmployeeService service = ServiceClientFactory.createEmployeeServiceClient();
            List<Employee> result = service.getEmployees();
            if (result != null) {
                employeeList = result;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Không thể lấy danh sách nhân sự từ Web Service: " + e.getMessage());
            serviceStatus = "OFFLINE";
            serviceMessage = "Chưa thể kết nối tới Backend Web Service tại: " + ServiceClientFactory.getEndpointUrl() +
                    ". Vui lòng khởi động Backend Service trước khi thao tác.";
        }

        request.setAttribute("employeeList", employeeList);
        request.setAttribute("serviceStatus", serviceStatus);
        request.setAttribute("serviceMessage", serviceMessage);
        request.setAttribute("endpointUrl", ServiceClientFactory.getEndpointUrl());

        // Forward tới trang JSP hiển thị giao diện
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
