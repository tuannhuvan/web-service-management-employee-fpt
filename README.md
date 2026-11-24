# HỆ THỐNG QUẢN LÝ NHÂN SỰ DOANH NGHIỆP (ENTERPRISE HRM SYSTEM)
## Tài Liệu Kiến Trúc, Hướng Dẫn Vận Hành & Triển Khai Toàn Diện

> **Tiêu chuẩn công nghệ:** Java EE / JAX-WS 2.2 SOAP 1.1 / WSDL 1.1 over HTTP / JSP 2.3 / Servlet 3.1  

---

## 1. Tổng Quan Kiến Trúc Hệ Thống (System Architecture)

Hệ thống được xây dựng theo mô hình **Kiến trúc Hướng Dịch vụ (SOA - Service-Oriented Architecture)** phân tách hoàn toàn giữa Lõi Dịch vụ Dữ liệu (Backend SOAP Web Service) và Ứng dụng Quản trị (Frontend Web Client):

```
+--------------------------------------------------------------------------+
|                     PRESENTATION LAYER (Web Client)                      |
|           JSP 2.3 + Servlet 3.1 + HTML5 + CSS3 + Client Validation       |
|                  (Cổng kết nối: http://localhost:8081/)                  |
+--------------------------------------------------------------------------+
                                    |
                                    | (SOAP 1.1 / HTTP XML Messages)
                                    v
+--------------------------------------------------------------------------+
|                     SERVICE INTEGRATION LAYER (Backend)                  |
|          JAX-WS 2.2 SEI & SIB: EmployeeService (@WebService)             |
|                  (Cổng kết nối: http://localhost:8080/)                  |
+--------------------------------------------------------------------------+
                                    |
                                    | (DAO Pattern / 100% PreparedStatement)
                                    v
+--------------------------------------------------------------------------+
|                     PERSISTENCE LAYER (MySQL 8.0 / InnoDB)               |
|      - Bảng chính: employees (id VARCHAR(20) PK, name, salary)          |
|      - Bảng kiểm toán: audit_logs (Ghi vết biến động lương - NĐ 13/2023) |
+--------------------------------------------------------------------------+
```

---

## 2. Cấu Trúc Thư Mục Dự Án (Project Structure)

Dự án được chuẩn hóa theo mô hình đa module Maven (Multi-Module Maven Project):

```
web-service-management-employee-fpt/
├── pom.xml                                      <- Root POM điều phối build và quản lý dependencies
├── README.md                                    <- Tài liệu hướng dẫn kỹ thuật và vận hành (File này)
├── mvnw & mvnw.cmd                              <- Maven Wrapper tiêu chuẩn
├── docs/                                        <- Tài liệu đặc tả & quy chuẩn kiến trúc
├── scripts/
│   └── database-init.sql                        <- Script DDL tạo bảng và nạp 5 bản ghi mẫu ban đầu
│
├── hrm-service-backend/                         <- MODULE BACKEND: JAX-WS SOAP Web Service
│   ├── pom.xml                                  <- Build cấu hình WAR, Metro JAX-WS, MySQL, H2 In-Memory
│   └── src/
│       ├── main/
│       │   ├── java/com/enterprise/hrm/
│       │   │   ├── entity/
│       │   │   │   └── Employee.java            <- Entity JavaBean/JAXB (id, name, salary, Serializable)
│       │   │   ├── util/
│       │   │   │   ├── DatabaseConnection.java  <- Quản lý kết nối JDBC từ db.properties (hỗ trợ pass rỗng)
│       │   │   │   └── DatabaseInitializer.java <- Tiện ích tự động tạo CSDL và nạp dữ liệu mẫu
│       │   │   ├── dao/
│       │   │   │   ├── EmployeeDAO.java         <- Interface thao tác CSDL
│       │   │   │   └── EmployeeDAOImpl.java     <- 100% PreparedStatement, try-with-resources, Audit Logs
│       │   │   ├── service/
│       │   │   │   ├── EmployeeService.java     <- SEI: getEmployees, addEmployees, updateEmployee
│       │   │   │   └── EmployeeServiceImpl.java <- SIB triển khai Web Service
│       │   │   └── publisher/
│       │   │       └── WebServicePublisher.java <- Chạy Standalone SOAP Service trên cổng 8080
│       │   ├── resources/
│       │   │   └── db.properties                <- Cấu hình kết nối MySQL Driver & Credentials
│       │   └── webapp/WEB-INF/
│       │       ├── web.xml                      <- Metro JAX-WS Servlet Context Listener
│       │       └── sun-jaxws.xml                <- Định nghĩa Endpoint Metro SOAP
│       └── test/java/com/enterprise/hrm/
│           ├── dao/EmployeeDAOTest.java         <- 8 Unit Tests H2 In-Memory (Pass 100%)
│           └── service/EmployeeServiceTest.java <- 6 Unit Tests Mock DAO & Service Logic (Pass 100%)
│
└── hrm-client-frontend/                         <- MODULE FRONTEND: Web Client (JSP & Servlet)
    ├── pom.xml                                  <- Cấu hình WAR & Jetty Plugin chạy cổng 8081
    └── src/
        ├── main/
        │   ├── java/com/enterprise/hrm/client/
        │   │   ├── generated/                   <- Client Stub sinh tương thích hợp đồng WSDL
        │   │   │   ├── Employee.java
        │   │   │   ├── EmployeeService.java
        │   │   │   └── EmployeeWebService.java
        │   │   ├── util/
        │   │   │   └── ServiceClientFactory.java<- Factory cấu hình WSDL Endpoint động từ properties
        │   │   └── controller/
        │   │       └── EmployeeServlet.java     <- Servlet điều phối, xác thực & xử lý an toàn
        │   ├── resources/
        │   │   └── client.properties            <- Cấu hình trỏ tới URL Endpoint Backend
        │   └── webapp/
        │       ├── index.jsp                    <- Giao diện 3 phân khu, Zebra Striping, Inline Validation
        │       └── WEB-INF/web.xml              <- Cấu hình Servlet Mapping & Welcome File
        └── test/java/com/enterprise/hrm/client/
            └── EmployeeClientTest.java          <- 2 Unit Tests Client DTO
```

---

## 3. Khởi Tạo Cơ Sở Dữ Liệu (Database Setup)

### 3.1. Kịch bản DDL Chuẩn Hóa
File kịch bản: `scripts/database-init.sql`.

```sql
-- 1. Tạo Database
CREATE DATABASE IF NOT EXISTS hrm_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE hrm_db;

-- 2. Tạo bảng Thực thể Chính: employees
CREATE TABLE IF NOT EXISTS employees (
    id VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    salary DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_employees PRIMARY KEY (id),
    CONSTRAINT chk_employee_salary CHECK (salary >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tạo index tối ưu hóa tìm kiếm
CREATE INDEX idx_employees_name ON employees (name);
CREATE INDEX idx_employees_salary ON employees (salary);

-- 3. Tạo bảng Ghi vết Kiểm toán: audit_logs (Tuân thủ Nghị định 13/2023/NĐ-CP)
CREATE TABLE IF NOT EXISTS audit_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NULL,
    action_type VARCHAR(20) NOT NULL,
    old_salary DOUBLE NULL,
    new_salary DOUBLE NULL,
    changed_by VARCHAR(50) DEFAULT 'WEB_SERVICE_CLIENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_employee (employee_id),
    INDEX idx_audit_created (created_at),
    CONSTRAINT fk_audit_employee FOREIGN KEY (employee_id) 
        REFERENCES employees (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Nạp Dữ liệu Mẫu Ban đầu
INSERT INTO employees (id, name, salary) VALUES
('EMP001', 'Nguyen Van An', 25000000.0),
('EMP002', 'Tran Thi Binh', 32000000.0),
('EMP003', 'Le Van Cuong', 18500000.0),
('EMP004', 'Pham Minh Duc', 45000000.0),
('EMP005', 'Hoang Mai Linh', 22000000.0);
```

### 3.2. Cấu hình Tài khoản MySQL
File cấu hình: `hrm-service-backend/src/main/resources/db.properties`
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/hrm_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.user=root
db.password=
```
*(Hỗ trợ ghi đè linh hoạt qua biến môi trường `DB_URL`, `DB_USER`, `DB_PASSWORD` hoặc tham số dòng lệnh JVM).*

---

## 4. Đặc Tả 3 Hàm Dịch Vụ Web Service (API Contract)

Triển khai tại `EmployeeService.java` & `EmployeeServiceImpl.java`:

| Tên Phương Thức | Tham Số Đầu Vào | Kiểu Trả Về | Mô Tả Nghiệp Vụ & Quy Tắc An Toàn |
| :--- | :--- | :---: | :--- |
| `getEmployees()` | *(Không có)* | `List<Employee>` | Truy xuất toàn bộ danh sách nhân sự từ CSDL. Tuyệt đối không trả về null pointer gây crash client. |
| `addEmployees(Employee emp)` | `emp`: Đối tượng nhân viên | `boolean` | Thêm mới nhân viên. Kiểm tra `id` không rỗng, `name` &ge; 2 ký tự, `salary` &ge; 0. Chặn trùng khóa chính `id`; trả về `false` nếu vi phạm. Ghi vết `INSERT` vào `audit_logs`. |
| `updateEmployee(Employee emp)`| `emp`: Thông tin cập nhật | `boolean` | Cập nhật `name` và `salary` dựa trên `id`. Trả về `true` nếu cập nhật thành công (rows affected > 0), `false` nếu không tìm thấy `id`. Ghi vết `UPDATE` kèm mức lương cũ/mới vào `audit_logs`. |

---

## 5. Đặc Tả Giao Diện Web Client (Frontend Standards)

Giao diện tại `hrm-client-frontend/src/main/webapp/index.jsp`:

1. **Khu vực 1 - Form nhập liệu:**
   - 3 trường: Mã Nhân Viên (`id`), Họ và Tên (`name`), Mức Lương (`salary`).
   - Xác thực Client-side trực quan: cảnh báo trực tiếp trên form kèm viền đỏ nổi bật tại ô cần sửa (không dùng popup `alert()` gây phiền hà).
   - Nút **`⚡ Điền Mẫu Nhanh`**: Tự động sinh dữ liệu ngẫu nhiên hợp lệ giúp kiểm thử 1-click tức thì.
2. **Khu vực 2 - Thanh tác vụ:**
   - Nút **Thêm Mới** (Màu xanh lá - gọi `addEmployees`).
   - Nút **Cập Nhật** (Màu xanh dương - gọi `updateEmployee`).
   - Nút **Làm Mới** (Màu xám bạc - tải lại danh sách qua `getEmployees`).
3. **Khu vực 3 - Bảng danh sách nhân sự:**
   - Thiết kế kẻ sọc (Zebra striping) xen kẽ dòng trắng/xám giúp quan sát dễ dàng.
   - Định dạng tiền tệ VND phân cách hàng nghìn rõ ràng: ví dụ `25,000,000 VND`.
   - Nút **"Chọn sửa"** ở mỗi dòng: Tự động đưa thông tin nhân viên lên form để sửa nhanh.
   - Bắt lỗi an toàn (`ERR-UI-05`): Hiển thị banner thông báo khi service offline, không gây crash trang (HTTP 500).

---

## 6. Hướng Dẫn Khởi Chạy Hệ Thống

### Cách 1: Khởi Chạy Nhanh (Khuyến Nghị Kiểm Thử Nhanh)
Hệ thống đã tích hợp sẵn Standalone Publisher và Jetty Server:

```bash
# Bước 1: Khởi tạo CSDL (Chỉ cần chạy lần đầu)
.\mvnw.cmd compile exec:java "-Dexec.mainClass=com.enterprise.hrm.util.DatabaseInitializer" -pl hrm-service-backend

# Bước 2: Khởi động Backend SOAP Web Service (Lắng nghe tại cổng 8080)
.\mvnw.cmd compile exec:java "-Dexec.mainClass=com.enterprise.hrm.publisher.WebServicePublisher" -pl hrm-service-backend

# Bước 3: Mở một cửa sổ Terminal mới và khởi động Web Client (Lắng nghe tại cổng 8081)
.\mvnw.cmd jetty:run -pl hrm-client-frontend
```

**Các đường link truy cập ngay:**
- 🌐 **Giao diện Web Client:** [http://localhost:8081/hrm-client/employees](http://localhost:8081/hrm-client/employees)
- 📄 **WSDL Service Contract:** [http://localhost:8080/HRMWebService/EmployeeService?wsdl](http://localhost:8080/HRMWebService/EmployeeService?wsdl)

---

### Cách 2: Triển Khai Lên GlassFish Server (Chuẩn Apache NetBeans)
1. Đảm bảo driver JDBC MySQL đã có trong thư mục: `<GLASSFISH_HOME>/glassfish/domains/domain1/lib/ext/`.
2. Khởi động GlassFish Server từ NetBeans (**Services** &rarr; **Servers** &rarr; **GlassFish Server** &rarr; **Start**).
3. Mở dự án trong NetBeans:
   - Chuột phải `hrm-service-backend` &rarr; Chọn **Deploy**.
   - Chuột phải `hrm-client-frontend` &rarr; Chọn **Run**.
4. Truy cập giao diện tại: `http://localhost:8080/hrm-client-frontend/`.

---

### Cách 3: Triển Khai Lên Apache Tomcat 9.x / 10.x
1. Đóng gói 2 file WAR:
   ```bash
   .\mvnw.cmd package -DskipTests
   ```
2. Copy `hrm-service-backend/target/hrm-service-backend.war` và `hrm-client-frontend/target/hrm-client-frontend.war` vào thư mục `<TOMCAT_HOME>/webapps/`.
3. Khởi động Tomcat qua file `startup.bat` (Windows) hoặc `startup.sh` (Linux).

---

## 7. Sinh Lại Client Stub Bằng Công Cụ `wsimport`

Khi endpoint backend có sự thay đổi về WSDL, có thể tự động sinh lại Client Stub bằng lệnh JDK:
```bash
wsimport -keep -p com.enterprise.hrm.client.generated -d ./hrm-client-frontend/src/main/java http://localhost:8080/HRMWebService/EmployeeService?wsdl
```

---

## 8. Kết Quả Kiểm Thử Tự Động (Unit Test Suite)

Chạy toàn bộ 16 bài kiểm thử tự động độc lập:
```bash
.\mvnw.cmd test
```

**Kết quả nghiệm thu:**
- `EmployeeDAOTest` (H2 In-Memory DB): 8/8 tests PASS (Thêm mới, chặn trùng ID, chặn lương âm, cập nhật thành công, cập nhật nhân viên không tồn tại, lấy danh sách).
- `EmployeeServiceTest` (Mock DAO): 6/6 tests PASS (Kiểm tra nghiệp vụ SEI/SIB).
- `EmployeeClientTest`: 2/2 tests PASS (Kiểm tra DTO và Client Stub).
- **Tổng số: 16 Tests run | 0 Failures | 0 Errors | 0 Skipped.**