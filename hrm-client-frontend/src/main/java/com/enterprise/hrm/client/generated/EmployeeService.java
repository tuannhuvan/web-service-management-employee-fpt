package com.enterprise.hrm.client.generated;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * Client Service Endpoint Interface (SEI)
 * Được sinh tự động hoặc ánh xạ tương đương từ WSDL của EmployeeWebService.
 */
@WebService(name = "EmployeeService", targetNamespace = "urn:techfirm:hrm:service:endpoint")
public interface EmployeeService {

    /**
     * @return danh sách nhân viên
     */
    @WebMethod(operationName = "getEmployees")
    @WebResult(name = "employee", targetNamespace = "")
    @RequestWrapper(localName = "getEmployees", targetNamespace = "urn:techfirm:hrm:service:endpoint", className = "com.enterprise.hrm.client.generated.GetEmployees")
    @ResponseWrapper(localName = "getEmployeesResponse", targetNamespace = "urn:techfirm:hrm:service:endpoint", className = "com.enterprise.hrm.client.generated.GetEmployeesResponse")
    List<Employee> getEmployees();

    /**
     * @param employee thông tin nhân viên cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    @WebMethod(operationName = "addEmployees")
    @WebResult(name = "success", targetNamespace = "")
    @RequestWrapper(localName = "addEmployees", targetNamespace = "urn:techfirm:hrm:service:endpoint", className = "com.enterprise.hrm.client.generated.AddEmployees")
    @ResponseWrapper(localName = "addEmployeesResponse", targetNamespace = "urn:techfirm:hrm:service:endpoint", className = "com.enterprise.hrm.client.generated.AddEmployeesResponse")
    boolean addEmployees(
        @WebParam(name = "employee", targetNamespace = "")
        Employee employee
    );

    /**
     * @param employee thông tin nhân viên cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    @WebMethod(operationName = "updateEmployee")
    @WebResult(name = "success", targetNamespace = "")
    @RequestWrapper(localName = "updateEmployee", targetNamespace = "urn:techfirm:hrm:service:endpoint", className = "com.enterprise.hrm.client.generated.UpdateEmployee")
    @ResponseWrapper(localName = "updateEmployeeResponse", targetNamespace = "urn:techfirm:hrm:service:endpoint", className = "com.enterprise.hrm.client.generated.UpdateEmployeeResponse")
    boolean updateEmployee(
        @WebParam(name = "employee", targetNamespace = "")
        Employee employee
    );
}
