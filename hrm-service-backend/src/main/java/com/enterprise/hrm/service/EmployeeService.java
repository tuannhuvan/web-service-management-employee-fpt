package com.enterprise.hrm.service;

import com.enterprise.hrm.entity.Employee;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Service Endpoint Interface (SEI) - Employee Web Service
 * Định nghĩa hợp đồng Web Service JAX-WS chuẩn mực theo đúng api-contract.md.
 * 
 * Target Namespace: urn:techfirm:hrm:service:endpoint
 * Style: DOCUMENT / LITERAL / WRAPPED
 */
@WebService(
    name = "EmployeeService",
    targetNamespace = "urn:techfirm:hrm:service:endpoint"
)
@SOAPBinding(
    style = SOAPBinding.Style.DOCUMENT,
    use = SOAPBinding.Use.LITERAL,
    parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
)
public interface EmployeeService {

    /**
     * Phương thức 1: getEmployees
     * Truy xuất toàn bộ danh sách hồ sơ nhân viên trong hệ thống.
     *
     * @return Danh sách các đối tượng Employee
     */
    @WebMethod(operationName = "getEmployees")
    @WebResult(name = "employee")
    List<Employee> getEmployees();

    /**
     * Phương thức 2: addEmployees
     * Tiếp nhận và ghi nhận hồ sơ nhân viên mới vào cơ sở dữ liệu.
     * Kiểm tra trùng lặp khóa chính 'id' và điều kiện 'salary >= 0'.
     *
     * @param emp Đối tượng Employee cần thêm mới
     * @return true nếu thêm thành công, false nếu id đã tồn tại hoặc salary < 0
     */
    @WebMethod(operationName = "addEmployees")
    @WebResult(name = "success")
    boolean addEmployees(@WebParam(name = "employee") Employee emp);

    /**
     * Phương thức 3: updateEmployee
     * Điều chỉnh thông tin họ tên và mức lương của nhân sự dựa trên mã 'id'.
     *
     * @param emp Đối tượng Employee chứa id, tên mới và mức lương mới
     * @return true nếu cập nhật thành công (rows affected > 0), ngược lại false
     */
    @WebMethod(operationName = "updateEmployee")
    @WebResult(name = "success")
    boolean updateEmployee(@WebParam(name = "employee") Employee emp);
}
