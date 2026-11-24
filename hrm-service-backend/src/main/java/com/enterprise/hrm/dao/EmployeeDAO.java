package com.enterprise.hrm.dao;

import com.enterprise.hrm.entity.Employee;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object Interface for Employee Management
 * Định nghĩa các thao tác dữ liệu chuẩn mực phục vụ 3 hàm Web Service cốt lõi.
 */
public interface EmployeeDAO {

    /**
     * Truy vấn toàn bộ danh sách nhân viên từ CSDL.
     *
     * @return List chứa các đối tượng Employee (không bao giờ trả về null)
     * @throws SQLException nếu xảy ra lỗi truy vấn CSDL
     */
    List<Employee> getEmployees() throws SQLException;

    /**
     * Thêm mới một nhân viên vào CSDL.
     * Kiểm tra tính duy nhất của mã 'id' và tính hợp lệ của 'salary >= 0'.
     *
     * @param emp Đối tượng Employee cần thêm mới
     * @return true nếu thêm thành công, false nếu id đã tồn tại hoặc dữ liệu không hợp lệ
     * @throws SQLException nếu phát sinh lỗi thao tác dữ liệu
     */
    boolean addEmployees(Employee emp) throws SQLException;

    /**
     * Cập nhật thông tin họ tên và mức lương của nhân viên theo mã 'id'.
     *
     * @param emp Đối tượng Employee chứa id cần cập nhật kèm tên và lương mới
     * @return true nếu cập nhật thành công (số bản ghi thay đổi > 0), ngược lại false
     * @throws SQLException nếu phát sinh lỗi thao tác dữ liệu
     */
    boolean updateEmployee(Employee emp) throws SQLException;

    /**
     * Kiểm tra sự tồn tại của nhân viên theo mã ID.
     *
     * @param id Mã định danh nhân viên
     * @return true nếu nhân viên đã tồn tại trong CSDL, ngược lại false
     * @throws SQLException nếu phát sinh lỗi thao tác dữ liệu
     */
    boolean existsById(String id) throws SQLException;

    /**
     * Tìm kiếm thông tin nhân viên theo mã ID.
     *
     * @param id Mã định danh nhân viên
     * @return Đối tượng Employee nếu tìm thấy, null nếu không tồn tại
     * @throws SQLException nếu phát sinh lỗi thao tác dữ liệu
     */
    Employee getEmployeeById(String id) throws SQLException;
}
