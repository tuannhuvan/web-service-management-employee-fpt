# Kiến trúc Phân tích Nghiệp vụ (BA Architecture) & Ma trận Truy vết Yêu cầu

Hệ thống Quản lý Nhân sự Doanh nghiệp trên nền tảng Web Service (>10.000 Nhân sự)

---

## 1. Giới thiệu & Định vị Theo Chuẩn BABOK v3

Tài liệu này xác lập khung kiến trúc phân tích nghiệp vụ (Business Analysis Architecture) dựa trên chuẩn BABOK v3 (A Guide to the Business Analysis Body of Knowledge) của IIBA và ISO/IEC/IEEE 29148. Mục tiêu là định hình toàn bộ cấu trúc logic, ranh giới phạm vi, chu trình chuyển giao yêu cầu và cơ chế kiểm soát chất lượng từ góc nhìn của Phân tích nghiệp vụ (BA) và Kiến trúc giải pháp (Solution Architecture).

### 1.1. Căn cứ Pháp lý & Tiêu chuẩn Tuân thủ
- **BABOK v3 Knowledge Areas**:
  - Strategy Analysis: Định vị nhu cầu quản trị nguồn nhân lực tập trung.
  - Requirements Analysis and Design Definition (RADD): Mô hình hóa dịch vụ Web Service và luồng dữ liệu.
  - Requirements Life Cycle Management: Quản lý vòng đời yêu cầu và truy vết toàn diện.
- **Quy chuẩn Pháp lý Việt Nam**:
  - **Nghị định 13/2023/NĐ-CP**: Đảm bảo toàn vẹn, bảo mật dữ liệu nhân sự (Họ tên, mức lương, định danh), kiểm soát quyền xử lý dữ liệu cá nhân cơ bản.
  - **Nghị định 73/2019/NĐ-CP**: Quy định quản lý đầu tư ứng dụng công nghệ thông tin sử dụng nguồn vốn ngân sách nhà nước/doanh nghiệp, tuân thủ tiêu chuẩn kỹ thuật về kết nối, tích hợp và an toàn thông tin.

---

## 2. Ranh giới Ngữ cảnh Hệ thống (System Context Boundary)

Hệ thống đóng vai trò là Lõi Dịch vụ Quản lý Nhân sự (Core Staff Management Web Service), cung cấp các giao diện dịch vụ độc lập cho các ứng dụng nội bộ và đối tác.

```
+-------------------------------------------------------------------------+
|                              CONTEXT BOUNDARY                           |
|                                                                         |
|  [ Web Client Application ]                                             |
|        (JSP/Servlet/UI)                                                 |
|               |                                                         |
|               | (HTTP POST / SOAP / XML)                                |
|               v                                                         |
|  +-------------------------------------------------------------------+  |
|  |             HRM Core Enterprise Web Service System                |  |
|  |                                                                   |  |
|  |  +---------------------+  +--------------------+  +------------+  |  |
|  |  | Endpoint Controller |  | Business Logic Core|  | Data Access|  |  |
|  |  |  - getEmployees     |->|  - Salary Rules    |->| Layer (DAO)|  |  |
|  |  |  - addEmployees     |  |  - Identity Verif. |  | (JDBC/JPA) |  |  |
|  |  |  - updateEmployee   |  |  - Audit Log Event |  |            |  |  |
|  |  +---------------------+  +--------------------+  +------------+  |  |
|  +-------------------------------------------------------------------+  |
|                                       |                                 |
|                                       | (JDBC Connection Pool)          |
|                                       v                                 |
|  +-------------------------------------------------------------------+  |
|  |          Relational Database Management System (RDBMS)            |  |
|  |          - Table: employees (id, name, salary)                    |  |
|  |          - Table: audit_logs (event_id, action, timestamp)        |  |
|  +-------------------------------------------------------------------+  |
+-------------------------------------------------------------------------+
```

---

## 3. Cây Phân rã Chức năng (Functional Decomposition Diagram - FDD)

Cấu trúc phân rã chức năng từ mục tiêu nghiệp vụ mức cao đến các dịch vụ vi mô:

1. **Hệ thống Quản trị Nhân sự (HRMS Web Service)**
   - **1.1. Dịch vụ Truy xuất Dữ liệu (Query Services)**
     - 1.1.1. Truy vấn danh sách toàn bộ nhân sự (`getEmployees`)
     - 1.1.2. Phân trang và kiểm soát tải danh sách 10.000 bản ghi
     - 1.1.3. Lọc theo định danh hoặc khoảng lương
   - **1.2. Dịch vụ Đăng ký & Thêm mới (Command/Mutation Services)**
     - 1.2.1. Thêm mới nhân sự đơn lẻ / danh sách nhân sự (`addEmployees`)
     - 1.2.2. Kiểm tra tính duy nhất của Mã nhân viên (`id`)
     - 1.2.3. Kiểm tra tính hợp lệ dữ liệu họ tên (`name`) và lương (`salary >= 0`)
   - **1.3. Dịch vụ Hiệu chỉnh Thông tin (Update Services)**
     - 1.3.1. Cập nhật thông tin nhân viên theo mã (`updateEmployee`)
     - 1.3.2. Kiểm tra trạng thái tồn tại của bản ghi trước cập nhật
     - 1.3.3. Bảo toàn tính toàn vẹn của mã định danh không đổi
   - **1.4. Dịch vụ Kiểm thử Giao diện (Client Verification)**
     - 1.4.1. Khởi tạo Client Stub kết nối Web Service
     - 1.4.2. Giao diện trực quan hóa danh sách nhân sự
     - 1.4.3. Form nhập liệu thêm mới và cập nhật thời gian thực

---

## 4. Ma trận Phân tích Các bên Liên quan (Stakeholder Onion Diagram)

| Vai trò | Phân loại | Kỳ vọng Chính | Tiêu chí Chấp thuận (Acceptance Criteria) |
| :--- | :--- | :--- | :--- |
| **Giám đốc Nhân sự (HR Director)** | Primary Business Owner | Dữ liệu nhân viên chính xác, báo cáo lương minh bạch. | Hệ thống phản hồi tức thì, không sai sót dữ liệu lương. |
| **Chuyên viên Nhân sự (HR Specialist)** | End User (Operational) | Nhập mới nhanh chóng, cập nhật lương không bị lỗi kết nối. | Giao diện thân thiện, báo lỗi chi tiết khi trùng lặp ID. |
| **Kiến trúc sư Hệ thống (Solution Architect)** | Technical Authority | Thiết kế tuân thủ SOA, phân tách tầng rõ ràng, loose coupling. | Đúng chuẩn JAX-WS/WSDL hoặc RESTful, cấu trúc sạch. |
| **Chuyên viên Bảo mật & DPO** | Compliance / Security | Tuân thủ Nghị định 13/2023/NĐ-CP, ghi vết truy cập dữ liệu lương. | Mọi thao tác truy vấn và sửa đổi đều sinh bản ghi kiểm toán. |
| **Cán bộ Kiểm định Chất lượng (QA/Tester)** | Verification / Validation | Đạt 15/15 điểm theo tiêu chuẩn đề bài kiểm tra kỹ thuật. | Đáp ứng trọn vẹn 5 nhóm tiêu chí kiểm thử kỹ thuật. |

---

## 5. Ma trận Truy vết Yêu cầu Toàn diện (Traceability Matrix - RTM)

Ma trận này thiết lập chuỗi liên kết 2 chiều giữa Yêu cầu Nghiệp vụ (BR), Yêu cầu Chức năng (FR), Phương thức Kỹ thuật, Lớp Dữ liệu và Ca kiểm thử (Test Cases):

| Mã Yêu cầu Nghiệp vụ (BR) | Mã Yêu cầu Chức năng (FR) | Thành phần Kỹ thuật / Phương thức | Cấu trúc Bảng / Cột CSDL | Mã Ca Kiểm thử (TC) | Tỷ trọng Điểm Đánh giá |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **BR-01**: Lưu trữ dữ liệu nhân sự | **FR-01**: Khởi tạo cấu trúc bảng | `Database: employees`<br>`Class: Employee.java` | `employees`<br>(`id`, `name`, `salary`) | `TC_DB_01`<br>`TC_ENT_01` | 2.0 / 15.0 Điểm |
| **BR-02**: Xem danh sách nhân sự | **FR-02**: Truy vấn tất cả nhân viên | `Web Method: getEmployees()` | `SELECT * FROM employees` | `TC_WS_GET_01`<br>`TC_WS_GET_02` | 2.0 / 15.0 Điểm |
| **BR-03**: Thêm mới nhân sự | **FR-03**: Đăng ký nhân viên mới | `Web Method: addEmployees(...)` | `INSERT INTO employees(...)` | `TC_WS_ADD_01`<br>`TC_WS_ADD_02` | 2.0 / 15.0 Điểm |
| **BR-04**: Điều chỉnh thông tin nhân sự | **FR-04**: Cập nhật thông tin nhân viên | `Web Method: updateEmployee(...)` | `UPDATE employees SET...` | `TC_WS_UPD_01`<br>`TC_WS_UPD_02` | 2.0 / 15.0 Điểm |
| **BR-05**: Kiểm chứng dịch vụ trên Web | **FR-05**: Xây dựng Web Client tiêu thụ dịch vụ | `JSP/Servlet Client`<br>`Service Proxy / Stub` | N/A (Frontend Consumption) | `TC_CLI_01`<br>`TC_CLI_02` | 5.0 / 15.0 Điểm |
| **BR-06**: Quản trị chất lượng mã nguồn | **FR-06**: Clean Code, chuẩn lập trình | `Naming convention, SOLID, Exception Handling` | Codebase formatting | `TC_CODE_01` | 2.0 / 15.0 Điểm |

---

## 6. Chiến lược Quản lý Thay đổi & Vòng đời Yêu cầu

1. **Kiểm soát Phạm vi (Scope Baseline)**: Mọi sự thay đổi về chữ ký phương thức dịch vụ (Method Signature) như đổi kiểu dữ liệu của `salary` từ `double` sang `BigDecimal` hoặc bổ sung trường `department` đều phải qua phân tích tác động (Impact Analysis) đến WSDL contract.
2. **Nguyên tắc Tương thích Ngược (Backward Compatibility)**: Phiên bản hóa endpoint dịch vụ (`/ws/v1/EmployeeService`) để đảm bảo không làm gián đoạn Client đang hoạt động.
3. **Phê duyệt Nghiệp vụ**: Đảm bảo mọi luồng nghiệp vụ đều có sự đồng thuận giữa Lead BA, Solution Architect và Lead Developer trước khi đóng gói bàn giao.
