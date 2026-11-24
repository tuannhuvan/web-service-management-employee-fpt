# ĐẶC TẢ YÊU CẦU PHẦN MỀM (SOFTWARE REQUIREMENTS SPECIFICATION - SRS)
## Hệ Thống Quản Lý Nhân Sự Cấp Doanh Nghiệp Dựa Trên Công Nghệ Web Service
### Tiêu chuẩn áp dụng: ISO/IEC/IEEE 29148:2018 & IEEE 830-1998 | Phương pháp luận: BABOK v3

---

## 1. Mở Đầu và Khung Pháp Lý Tham Chiếu

Tài liệu Đặc tả Yêu cầu Phần mềm (SRS) này thiết lập đường cơ sở kỹ thuật (technical baseline) cho việc phát triển, kiểm thử, tích hợp và nghiệm thu Hệ thống Dịch vụ Web Quản lý Cán bộ Nhân viên tại Tập đoàn Công nghệ Thông tin FPT – một đơn vị quy mô lớn với quy mô nhân sự vượt trên 10.000 cán bộ công nhân viên. Tài liệu đóng vai trò là hợp đồng kỹ thuật và cam kết nghiệp vụ giữa các bên liên quan, đảm bảo tính liên kết chặt chẽ từ mục tiêu chiến lược của Ban Giám đốc đến từng phương thức dịch vụ và bảng dữ liệu vật lý.

### 1.1. Căn Cứ Pháp Lý và Tiêu Chuẩn Kỹ Thuật Áp Dụng

Hệ thống được thiết kế và triển khai tuân thủ các quy định pháp luật hiện hành của Nhà nước, các tiêu chuẩn an toàn thông tin và phương pháp luận kỹ nghệ yêu cầu quốc tế:

1. **Nghị định số 13/2023/NĐ-CP ngày 17/04/2023 của Chính phủ về bảo vệ dữ liệu cá nhân**: Hệ thống quản lý các trường thông tin nhận diện cá nhân (`name`, `id`) và thông tin tài chính/thu nhập (`salary`). Đây là các dữ liệu cá nhân thuộc diện điều chỉnh nghiêm ngặt. Hệ thống phải đảm bảo các nguyên tắc hợp pháp, minh bạch, giới hạn mục đích, bảo mật kỹ thuật bằng mã hóa đường truyền, kiểm soát truy cập và lưu vết nhật ký xử lý dữ liệu theo Điều 9, Điều 13 và Điều 26 của Nghị định.
2. **Nghị định số 73/2019/NĐ-CP ngày 05/09/2019 của Chính phủ quy định quản lý đầu tư ứng dụng công nghệ thông tin sử dụng nguồn vốn ngân sách nhà nước**: Áp dụng các nguyên tắc thiết kế hệ thống chuẩn hóa, khả năng kết nối liên thông, kiến trúc mở hướng dịch vụ (SOA), quản lý chất lượng phần mềm thông qua các bước kiểm thử chức năng và phi chức năng độc lập.
3. **Tiêu chuẩn Quốc gia TCVN 11930:2017 và ISO/IEC 27001:2022**: Quy định về xác định cấp độ an toàn hệ thống thông tin, kiểm soát truy cập định danh, mã hóa kênh truyền (Transport Layer Security - TLS) đối với các giao tiếp dịch vụ web.
4. **Tiêu chuẩn ISO/IEC/IEEE 29148:2018 và IEEE Std 830-1998**: Khung cấu trúc quy chuẩn kỹ nghệ phần mềm cho việc phân rã và đặc tả yêu cầu người dùng, yêu cầu hệ thống và điều kiện nghiệm thu.
5. **Hướng dẫn Nghiệp vụ Phân tích BABOK v3 (A Guide to the Business Analysis Body of Knowledge)**: Phương pháp phân tích yêu cầu từ tầng Yêu cầu Nghiệp vụ (Business Requirements), Yêu cầu Các bên liên quan (Stakeholder Requirements), Yêu cầu Giải pháp (Solution Requirements - Chức năng & Phi chức năng) và Yêu cầu Chuyển giao (Transition Requirements).

### 1.2. Thuật Ngữ và Từ Viết Tắt

Bảng dưới đây chuẩn hóa các thuật ngữ và khái niệm chuyên ngành được sử dụng xuyên suốt tài liệu nhằm đảm bảo sự thống nhất giữa đội ngũ phân tích nghiệp vụ, kiến trúc sư phần mềm, lập trình viên và kiểm thử viên.

| Thuật ngữ / Viết tắt | Tên tiếng Anh đầy đủ | Định nghĩa và Ý nghĩa Nghiệp vụ |
| :--- | :--- | :--- |
| **SRS** | Software Requirements Specification | Tài liệu Đặc tả Yêu cầu Phần mềm mô tả đầy đủ hành vi, chức năng và ràng buộc của hệ thống. |
| **SOA** | Service-Oriented Architecture | Kiến trúc hướng dịch vụ, cho phép tích hợp các ứng dụng độc lập qua mạng thông qua các giao tiếp chuẩn hóa. |
| **Web Service** | Web Service | Giao diện phần mềm cung cấp dịch vụ tính toán và dữ liệu qua mạng qua các giao thức web (SOAP/WSDL hoặc RESTful/OpenAPI). |
| **RDBMS** | Relational Database Management System | Hệ quản trị cơ sở dữ liệu quan hệ đảm bảo lưu trữ bền vững và tính toàn vẹn giao dịch (ACID). |
| **POJO** | Plain Old Java Object | Đối tượng Java thuần túy dùng để biểu diễn cấu trúc dữ liệu thực thể thực tế trong ứng dụng (`Employee`). |
| **CRUD** | Create, Read, Update, Delete | Bốn thao tác dữ liệu cơ bản tương ứng với việc thêm mới, truy xuất, cập nhật và xóa bản ghi. |
| **PDPA** | Personal Data Protection Act | Khái niệm pháp lý tham chiếu tới các nghĩa vụ bảo vệ dữ liệu cá nhân theo Nghị định 13/2023/NĐ-CP. |

---

## 2. Mô Tả Tổng Quan Hệ Thống (Overall System Description)

Phần này phác thảo bức tranh tổng thể về bối cảnh vận hành, mô hình kiến trúc logic, các đối tượng sử dụng và các giới hạn kỹ thuật của giải pháp quản lý nhân sự.

### 2.1. Bối Cảnh Nghiệp Vụ và Mục Tiêu Đầu Tư

Tập đoàn FPT là tổ chức công nghệ thông tin và truyền thông với quy mô nhân lực hiện hữu trên 10.000 nhân viên. Sự gia tăng quy mô đòi hỏi công tác quản trị dữ liệu nhân sự phải chuyển dịch từ các ứng dụng đơn khối (monolithic) hoặc cục bộ sang kiến trúc phân tán dựa trên dịch vụ web (web service technologies). 

Mục tiêu chiến lược của Giám đốc (Director) bao gồm:
- **Tập trung hóa dữ liệu nhân sự**: Đảm bảo toàn bộ thông tin mã định danh, họ tên và mức lương được quản lý tập trung trên cơ sở dữ liệu quan hệ chuẩn hóa.
- **Tách biệt tầng dịch vụ và tầng giao diện (Decoupling)**: Tầng nghiệp vụ xử lý dữ liệu được đóng gói thành các dịch vụ web độc lập, cho phép nhiều ứng dụng client khác nhau (ứng dụng web, ứng dụng kiểm thử nội bộ, cổng thông tin tập đoàn) cùng tiêu thụ dịch vụ một cách nhất quán.
- **Tạo nền tảng kiểm thử và tích hợp nhanh**: Xây dựng ngay một ứng dụng web (Web Application) đóng vai trò là giao diện người dùng trực quan để vận hành, kiểm thử và xác thực toàn diện các phương thức dịch vụ web trước khi tích hợp vào hệ thống tổng thể.

### 2.2. Kiến Trúc Hệ Thống Phân Tầng (Multi-Tier Architecture)

Kiến trúc giải pháp được thiết kế theo mô hình 3 tầng phân tán độc lập, đáp ứng các nguyên tắc quản lý đầu tư phần mềm theo Nghị định 73/2019/NĐ-CP:

1. **Tầng Giao Diện / Kiểm Thử (Web Presentation Layer - Test Client Application)**:
   - Giao diện web được thiết kế thân thiện, tương tác trực tiếp với người dùng và quản trị viên.
   - Chịu trách nhiệm khởi tạo các yêu cầu HTTP/SOAP, đóng gói dữ liệu đầu vào và hiển thị dữ liệu phản hồi từ Web Service (dạng bảng biểu danh sách nhân viên, form nhập thêm mới và cập nhật).
2. **Tầng Dịch Vụ Web và Xử Lý Nghiệp Vụ (Web Service & Business Logic Layer)**:
   - Hiện thực hóa các phương thức dịch vụ được chỉ định: `getEmployees`, `addEmployees(Employee e)` và `updateEmployee(Employee e)`.
   - Đảm nhiệm việc xác thực tính hợp lệ của dữ liệu đầu vào (Data Validation), xử lý ngoại lệ nghiệp vụ và điều phối giao dịch.
   - Cung cấp hợp đồng dịch vụ chuẩn hóa, tách bạch hoàn toàn với công nghệ lưu trữ phía dưới.
3. **Tầng Truy Xuất và Lưu Trữ Dữ Liệu (Data Access & Persistence Layer)**:
   - Cơ sở dữ liệu quan hệ (RDBMS) lưu trữ bảng dữ liệu nhân viên.
   - Đảm bảo tính nhất quán (Consistency), toàn vẹn khóa chính và lưu trữ bền vững (Durability) của thông tin cán bộ công nhân viên.

### 2.3. Tác Nhân Hệ Thống (System Actors) và Ma Trận Phân Quyền

Hệ thống phục vụ hai nhóm đối tượng tác nhân chính tương ứng với quyền hạn vận hành và khai thác thông tin:

| Tác nhân (Actor) | Vai trò và Trách nhiệm | Quyền hạn trên Hệ thống |
| :--- | :--- | :--- |
| **Quản trị Nhân sự / Giám đốc (HR Officer / Director)** | Chịu trách nhiệm quản lý biến động nhân sự, quản lý chính sách lương, bổ sung nhân viên mới và điều chỉnh thông tin cán bộ. | Toàn quyền gọi các phương thức dịch vụ: xem toàn bộ danh sách nhân viên (`getEmployees`), thêm nhân viên (`addEmployees`), sửa thông tin nhân viên (`updateEmployee`). |
| **Kỹ sư Phát triển / QA (Developer / Tester)** | Sử dụng ứng dụng web kiểm thử để thực hiện kiểm thử chức năng, kiểm thử luồng dịch vụ, phát hiện lỗi và nghiệm thu kỹ thuật. | Truy cập ứng dụng web kiểm thử, kích hoạt tất cả các ca kiểm thử tương ứng với 3 phương thức dịch vụ và kiểm tra phản hồi từ cơ sở dữ liệu. |

### 2.4. Giả Định và Ràng Buộc Kỹ Thuật (Assumptions and Constraints)

- **Ràng buộc ngôn ngữ và nền tảng**: Toàn bộ mô hình đối tượng dịch vụ và lớp thực thể `Employee` phải được hiện thực bằng ngôn ngữ lập trình Java theo yêu cầu kiến trúc.
- **Ràng buộc lưu trữ**: Dữ liệu bắt buộc phải được duy trì trong cơ sở dữ liệu (Database table), không sử dụng lưu trữ tạm trong bộ nhớ (in-memory mock) cho môi trường chính thức.
- **Ràng buộc hiệu năng**: Doanh nghiệp có quy mô trên 10.000 cán bộ, phương thức truy xuất mảng nhân viên `getEmployees` phải được tối ưu hóa truy vấn và phân bổ bộ nhớ hợp lý để tránh lỗi tràn bộ nhớ (Out-Of-Memory).
- **Ràng buộc bảo mật dữ liệu lương**: Thuộc tính `salary` gắn liền với quyền riêng tư cá nhân của người lao động theo Nghị định 13/2023/NĐ-CP; chỉ những cá nhân và dịch vụ có thẩm quyền mới được phép xem và thay đổi.

---

## 3. Đặc Tả Yêu Cầu Chức Năng Chi Tiết (Functional Requirements)

Phần này quy định chi tiết từng chức năng nghiệp vụ của hệ thống dưới góc độ ca sử dụng (Use Case), bao gồm điều kiện tiên quyết, luồng sự kiện chính, các luồng ngoại lệ và quy tắc nghiệp vụ tương ứng với 3 phương thức Web Service lõi và ứng dụng Web kiểm thử.

### 3.1. Phân Rã Ca Sử Dụng Tổng Thể (Use Case Decomposition)

Các chức năng cốt lõi được cấu trúc mạch lạc thành bốn nhóm ca sử dụng nghiệp vụ:

- **UC-01**: Truy xuất danh bạ toàn bộ nhân viên (`getEmployees`).
- **UC-02**: Thêm mới hồ sơ cán bộ nhân sự (`addEmployees`).
- **UC-03**: Cập nhật thông tin hồ sơ nhân sự (`updateEmployee`).
- **UC-04**: Giao diện Web Client kiểm thử và xác thực dịch vụ.

---

### 3.2. Chi Tiết Từng Ca Sử Dụng

#### UC-01: Truy xuất danh bạ toàn bộ nhân sự (`getEmployees`)

- **Mã định danh**: REQ-F-01 / UC-01
- **Phương thức Web Service**: `public Employee[] getEmployees()`
- **Mục tiêu**: Cung cấp dữ liệu toàn bộ cán bộ nhân viên trong tổ chức phục vụ công tác thống kê, báo cáo và hiển thị trên ứng dụng quản trị.
- **Tác nhân kích hoạt**: Ứng dụng Web Client kiểm thử, Cán bộ quản lý nhân sự, Giám đốc.
- **Điều kiện tiên quyết**: Hệ thống dịch vụ web đang hoạt động bình thường và kết nối cơ sở dữ liệu được thiết lập thành công.
- **Dữ liệu đầu vào**: Không có tham số đầu vào.
- **Dữ liệu đầu ra**: Một mảng (array) chứa các đối tượng `Employee`, mỗi đối tượng bao gồm `id`, `name`, `salary`. Trong trường hợp chưa có dữ liệu trong bảng, trả về mảng rỗng (empty array), không được trả về `null`.
- **Luồng sự kiện chính (Normal Flow)**:
  1. Ứng dụng Web Client gửi thông điệp yêu cầu thực thi phương thức `getEmployees` tới Web Service.
  2. Web Service tiếp nhận yêu cầu, thiết lập phiên kết nối tới RDBMS.
  3. Thực thi truy vấn dữ liệu từ bảng lưu trữ nhân viên: `SELECT id, name, salary FROM employee`.
  4. Hệ thống đọc từng bản ghi từ kết quả truy vấn, khởi tạo đối tượng `Employee` tương ứng và điền đầy đủ các thuộc tính `id`, `name`, `salary`.
  5. Đóng gói danh sách thành một cấu trúc mảng Java (`Employee[]`).
  6. Web Service trả về mảng kết quả dưới dạng thông điệp phản hồi chuẩn hóa (SOAP Response / RESTful JSON Array).
  7. Ứng dụng Web Client nhận thông điệp, bóc tách dữ liệu và hiển thị danh sách nhân viên lên giao diện bảng biểu cho người dùng.
- **Luồng ngoại lệ (Alternative / Exception Flows)**:
  - *E1 - Lỗi kết nối cơ sở dữ liệu*: Nếu đường truyền tới RDBMS bị gián đoạn, Web Service bắt ngoại lệ, ghi log an toàn và trả về mã lỗi `SVC-ERR-500: Database Connection Failed`. Web Client hiển thị thông báo lỗi thân thiện tới người dùng.
  - *E2 - Quá tải dữ liệu*: Trường hợp số lượng bản ghi vượt ngưỡng 10.000 bản ghi, hệ thống kích hoạt cơ chế đệm bộ nhớ (streaming/buffering) để đảm bảo không gây sập ứng dụng.
- **Quy tắc nghiệp vụ liên quan**:
  - `BR-SEC-01`: Tuân thủ Điều 13 Nghị định 13/2023/NĐ-CP, kênh truyền danh sách nhân viên chứa mức lương phải được bảo vệ qua kết nối bảo mật HTTPS/TLS.

---

#### UC-02: Thêm mới hồ sơ cán bộ nhân sự (`addEmployees`)

- **Mã định danh**: REQ-F-02 / UC-02
- **Phương thức Web Service**: `public boolean addEmployees(Employee e)`
- **Mục tiêu**: Cho phép bộ phận nhân sự ghi nhận một cán bộ nhân viên mới vào hệ thống quản lý tập trung của tập đoàn.
- **Tác nhân kích hoạt**: Cán bộ nhân sự qua ứng dụng Web Client.
- **Điều kiện tiên quyết**: Người dùng có quyền thêm mới nhân sự; cơ sở dữ liệu sẵn sàng.
- **Dữ liệu đầu vào**: Đối tượng `Employee` chứa:
  - `id`: Mã định danh nhân viên (kiểu số nguyên dương hoặc chuỗi định danh không rỗng).
  - `name`: Họ và tên nhân viên (chuỗi ký tự, không được để trống).
  - `salary`: Mức lương nhân viên (kiểu số thực hoặc số nguyên lớn hơn hoặc bằng 0).
- **Dữ liệu đầu ra**: Giá trị boolean (`true` nếu thêm mới thành công, `false` hoặc ném ngoại lệ có kiểm soát nếu thất bại) cùng mã thông điệp phản hồi.
- **Luồng sự kiện chính (Normal Flow)**:
  1. Người dùng truy cập form thêm mới trên ứng dụng Web Client, điền đầy đủ các thông tin `id`, `name`, `salary` và nhấn nút "Thêm Nhân Viên" (Add Employee).
  2. Web Client kiểm tra hợp lệ sơ bộ tại giao diện (Client-side validation) và gửi yêu cầu gọi phương thức `addEmployees(Employee e)` tới Web Service.
  3. Web Service thực hiện kiểm tra tính hợp lệ dữ liệu phía máy chủ (Server-side validation) theo các quy tắc nghiệp vụ:
     - Kiểm tra `id` không được trùng lặp với bất kỳ bản ghi nào đã tồn tại trong cơ sở dữ liệu.
     - Kiểm tra `name` không được rỗng hoặc chỉ chứa khoảng trắng.
     - Kiểm tra `salary` phải là giá trị số không âm ($\ge 0$).
  4. Web Service mở giao dịch (transaction) và thực thi lệnh ghi dữ liệu vào bảng cơ sở dữ liệu: `INSERT INTO employee (id, name, salary) VALUES (?, ?, ?)`.
  5. Cơ sở dữ liệu xác nhận bản ghi đã được ghi thành công; giao dịch được xác nhận (commit).
  6. Web Service trả về phản hồi thành công (`true`) cho Web Client.
  7. Web Client hiển thị thông báo "Thêm mới nhân viên thành công" và tự động làm mới bảng danh sách nhân viên.
- **Luồng ngoại lệ (Alternative / Exception Flows)**:
  - *E1 - Trùng lặp khóa chính (Duplicate ID)*: Nếu `id` đã tồn tại trong cơ sở dữ liệu, thao tác INSERT bị hủy. Web Service trả về lỗi `ERR-DUP-01: Employee ID already exists`. Web Client hiển thị cảnh báo yêu cầu người dùng kiểm tra lại mã nhân viên.
  - *E2 - Sai định dạng dữ liệu (Invalid Input)*: Nếu `salary` âm hoặc `name` để trống, Web Service từ chối xử lý, trả về lỗi `ERR-VAL-02: Invalid Employee Attributes`.
  - *E3 - Lỗi hệ thống*: Nếu xảy ra lỗi hạ tầng, giao dịch tự động hoàn tác (rollback) để bảo toàn tính nhất quán dữ liệu.
- **Quy tắc nghiệp vụ liên quan**:
  - `BR-BUS-01`: Mã định danh nhân viên `id` là duy nhất trên toàn hệ thống FPT.
  - `BR-BUS-02`: Tiền lương của nhân viên phải là số dương hoặc bằng 0 (áp dụng cho trường hợp thử việc hoặc thực tập sinh không hưởng lương chính thức).

---

#### UC-03: Cập nhật thông tin cán bộ nhân sự (`updateEmployee`)

- **Mã định danh**: REQ-F-03 / UC-03
- **Phương thức Web Service**: `public boolean updateEmployee(Employee e)`
- **Mục tiêu**: Điều chỉnh thông tin hồ sơ nhân viên hiện hữu khi có sự thay đổi về họ tên hoặc mức lương sau các kỳ đánh giá hiệu quả công việc.
- **Tác nhân kích hoạt**: Cán bộ quản lý nhân sự qua ứng dụng Web Client.
- **Điều kiện tiên quyết**: Nhân viên cần chỉnh sửa đã tồn tại trong hệ thống.
- **Dữ liệu đầu vào**: Đối tượng `Employee` mang thông tin cập nhật gồm `id` (dùng để định vị bản ghi), `name` mới, `salary` mới.
- **Dữ liệu đầu ra**: Giá trị boolean (`true` nếu cập nhật thành công, `false` nếu không tìm thấy hoặc thất bại).
- **Luồng sự kiện chính (Normal Flow)**:
  1. Trên ứng dụng Web Client, người dùng chọn một nhân viên từ danh sách hoặc nhập `id` cần chỉnh sửa.
  2. Giao diện hiển thị thông tin hiện tại của nhân viên; người dùng thay đổi giá trị của `name` hoặc `salary` và nhấn nút "Cập Nhật" (Update Employee).
  3. Web Client đóng gói dữ liệu và gọi phương thức `updateEmployee(Employee e)` của Web Service.
  4. Web Service xác thực dữ liệu đầu vào và kiểm tra sự tồn tại của nhân viên qua câu truy vấn kiểm tra khóa chính.
  5. Nếu bản ghi tồn tại, Web Service thực thi câu lệnh cập nhật dữ liệu: `UPDATE employee SET name = ?, salary = ? WHERE id = ?`.
  6. Cơ sở dữ liệu cập nhật dữ liệu thành công; giao dịch được xác nhận (commit).
  7. Web Service trả về kết quả thành công (`true`) cho Web Client.
  8. Web Client hiển thị thông báo "Cập nhật thông tin nhân viên thành công" và cập nhật trực tiếp dòng dữ liệu trên màn hình.
- **Luồng ngoại lệ (Alternative / Exception Flows)**:
  - *E1 - Không tìm thấy bản ghi (Record Not Found)*: Nếu không tìm thấy nhân viên nào có `id` tương ứng, Web Service trả về `false` kèm mã lỗi `ERR-NOT-FOUND: Employee record does not exist`.
  - *E2 - Dữ liệu cập nhật không hợp lệ*: Nếu giá trị `salary` mới là số âm, hệ thống từ chối cập nhật và trả về thông báo lỗi kiểm chuẩn.
- **Quy tắc nghiệp vụ liên quan**:
  - `BR-SEC-02`: Theo quy định tại Nghị định 13/2023/NĐ-CP, việc điều chỉnh thông tin cá nhân và tiền lương của người lao động phải được ghi lại nhật ký kiểm toán (Audit Trail) gồm: thời gian thực hiện, tài khoản thực hiện, giá trị trước và sau khi thay đổi.

---

#### UC-04: Xây dựng ứng dụng Web kiểm thử dịch vụ (Test Client Web Application)

- **Mã định danh**: REQ-F-04 / UC-04
- **Mục tiêu**: Cung cấp một giao diện web trực quan, hoàn chỉnh để người dùng, kiểm thử viên và ban giám đốc có thể tương tác, kích hoạt và kiểm chứng toàn diện tất cả các phương thức Web Service đã xây dựng.
- **Thành phần giao diện bắt buộc**:
  1. **Khu vực hiển thị danh bạ (Employee List View)**:
     - Nút chức năng "Nạp danh sách" (Load All Employees) kích hoạt phương thức `getEmployees`.
     - Bảng dữ liệu hiển thị trực quan các cột: Mã nhân viên (`id`), Họ và tên (`name`), Mức lương (`salary`).
  2. **Khu vực thêm mới nhân viên (Add Employee Form)**:
     - Các trường nhập liệu: Mã nhân viên, Họ và tên, Mức lương.
     - Nút chức năng "Thêm mới" gửi dữ liệu tới phương thức `addEmployees`.
     - Vùng hiển thị thông báo trạng thái kết quả (Thành công / Thất bại / Nguyên nhân lỗi).
  3. **Khu vực cập nhật nhân viên (Update Employee Form)**:
     - Cho phép chọn trực tiếp bản ghi từ bảng hoặc nhập mã nhân viên để tự động điền dữ liệu.
     - Cho phép sửa đổi tên và mức lương, nút "Lưu thay đổi" gọi phương thức `updateEmployee`.
- **Yêu cầu tương tác**:
  - Phản hồi nhanh chóng, hiển thị rõ ràng thông báo lỗi khi nhập liệu sai.
  - Tự động làm mới danh sách hiển thị ngay sau khi thực hiện thao tác thêm mới hoặc cập nhật thành công mà không làm gián đoạn trải nghiệm người dùng.

---

## 4. Đặc Tả Mô Hình Dữ Liệu và Hợp Đồng Dịch Vụ (Data & Interface Specifications)

Mô hình dữ liệu và giao diện trao đổi dữ liệu được thiết kế đồng bộ nhằm đảm bảo sự chuẩn hóa cao nhất giữa tầng lưu trữ, tầng mã nguồn Java và tầng giao tiếp mạng.

### 4.1. Thiết Kế Cơ Sở Dữ Liệu Quan Hệ (Database Schema)

Căn cứ yêu cầu đề bài, cơ sở dữ liệu phải lưu trữ bền vững thông tin cán bộ nhân viên trong một bảng dữ liệu quan hệ chuyên biệt.

- **Tên bảng**: `employee` (hoặc `tbl_employee`)
- **Khóa chính**: `id`
- **Mô tả cấu trúc các trường**:

| Tên trường (Column) | Kiểu dữ liệu RDBMS | Ràng buộc kỹ thuật | Ý nghĩa nghiệp vụ |
| :--- | :--- | :--- | :--- |
| `id` | `VARCHAR(20)` / `INT` | `PRIMARY KEY, NOT NULL` | Mã số nhân viên duy nhất trong tập đoàn FPT. |
| `name` | `NVARCHAR(100)` / `VARCHAR(100)` | `NOT NULL` | Họ và tên đầy đủ của cán bộ nhân viên. |
| `salary` | `DOUBLE` / `DECIMAL(15,2)` | `NOT NULL, CHECK (salary >= 0)` | Mức lương của cán bộ nhân viên, không âm. |

- **Script khởi tạo bảng mẫu (DDL Script)**:
```sql
CREATE TABLE employee (
    id VARCHAR(20) NOT NULL,
    name NVARCHAR(100) NOT NULL,
    salary DECIMAL(15, 2) NOT NULL DEFAULT 0.0,
    CONSTRAINT pk_employee PRIMARY KEY (id),
    CONSTRAINT chk_employee_salary CHECK (salary >= 0)
);
```

### 4.2. Đặc Tả Lớp Thực Thể Java (`Employee` Class Model)

Lớp `Employee` được thiết kế theo đúng chuẩn mực Java Bean / POJO để đảm bảo khả năng tuần tự hóa (Serialization) khi đóng gói dữ liệu qua Web Service:

- **Tên gói (Package)**: `com.fpt.csw.entity` (hoặc `com.fpt.csw.model`)
- **Tên lớp**: `Employee`
- **Các thuộc tính private**:
  - `private String id;` (hoặc `private int id;`)
  - `private String name;`
  - `private double salary;`
- **Các phương thức bắt buộc**:
  - Constructor mặc định không tham số: `public Employee()`
  - Constructor đầy đủ tham số: `public Employee(String id, String name, double salary)`
  - Tập hợp đầy đủ các hàm Getter và Setter cho từng thuộc tính:
    - `public String getId()`, `public void setId(String id)`
    - `public String getName()`, `public void setName(String name)`
    - `public double getSalary()`, `public void setSalary(double salary)`
  - Phương thức bổ trợ: `public String toString()` để hỗ trợ việc ghi log kỹ thuật.

### 4.3. Đặc Tả Giao Diện Dịch Vụ Web (Web Service Interface / Contract)

Dịch vụ web được định nghĩa độc lập dưới dạng giao diện dịch vụ (Service Endpoint Interface), có thể được triển khai theo chuẩn SOAP/WSDL (JAX-WS) hoặc RESTful Web Services (JAX-RS / Spring Boot):

```java
package com.fpt.csw.service;

import com.fpt.csw.entity.Employee;

public interface EmployeeWebService {
    
    /**
     * Truy xuất toàn bộ danh sách nhân viên trong hệ thống.
     * @return Mảng các đối tượng Employee
     */
    Employee[] getEmployees();

    /**
     * Thêm mới một bản ghi nhân viên vào cơ sở dữ liệu.
     * @param e Đối tượng Employee cần thêm mới
     * @return true nếu thêm mới thành công, false nếu thất bại
     */
    boolean addEmployees(Employee e);

    /**
     * Cập nhật thông tin của nhân viên đã tồn tại trong cơ sở dữ liệu.
     * @param e Đối tượng Employee chứa thông tin cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi
     */
    boolean updateEmployee(Employee e);
}
```

---

## 5. Yêu Cầu Phi Chức Năng và Tuân Thủ Pháp Lý (Non-Functional Requirements)

Bên cạnh các yêu cầu chức năng, hệ thống phải đáp ứng các tiêu chuẩn khắt khe về kỹ thuật, hiệu năng và hành lang pháp lý an toàn thông tin của Việt Nam.

### 5.1. Yêu Cầu An Toàn Thông Tin và Tuân Thủ Nghị Định 13/2023/NĐ-CP

1. **Bảo mật dữ liệu cá nhân trong lưu trữ và truyền tải**:
   - Dữ liệu họ tên (`name`) và mức thu nhập (`salary`) là dữ liệu cá nhân theo Điều 2 và Điều 9 Nghị định 13/2023/NĐ-CP.
   - Toàn bộ lưu lượng mạng trao đổi giữa Web Client và Web Service bắt buộc phải được mã hóa thông qua giao thức TLS 1.3 (HTTPS).
   - Cơ sở dữ liệu phải cấu hình hạn chế truy cập trực tiếp từ Internet, chỉ cho phép máy chủ ứng dụng (Application Server) kết nối qua mạng nội bộ bảo mật (VPC/Intranet).
2. **Quyền của chủ thể dữ liệu (Data Subject Rights)**:
   - Hệ thống đảm bảo quyền được biết, quyền chỉnh sửa dữ liệu của nhân viên (thông qua phương thức `updateEmployee`) và quyền tiếp cận dữ liệu cá nhân đúng quy trình.
3. **Nhật ký kiểm toán an toàn (Audit Logging)**:
   - Mọi thao tác thêm mới (`addEmployees`) và thay đổi lương/tên (`updateEmployee`) phải được ghi nhật ký hệ thống: mã nhân viên được chỉnh sửa, thời điểm thực hiện, địa chỉ IP yêu cầu và định danh tài khoản thực hiện. Không lưu mật khẩu hoặc dữ liệu nhạy cảm dưới dạng bản rõ trong log.

### 5.2. Tuân Thủ Tiêu Chuẩn Đầu Tư và Kiến Trúc Theo Nghị Định 73/2019/NĐ-CP

1. **Tính độc lập và khả năng liên thông (Interoperability)**:
   - Các phương thức dịch vụ web phải tuân thủ chuẩn mở quốc tế (W3C SOAP/XML hoặc REST/JSON), cho phép bất kỳ hệ thống nào của Tập đoàn FPT (quản lý dự án, chấm công, tính thuế) đều có thể kết nối liên thông mà không bị phụ thuộc vào công nghệ riêng biệt.
2. **Quản lý mã nguồn và tài liệu kỹ thuật**:
   - Kiến trúc giải pháp, thiết kế dữ liệu, mã nguồn và kết quả kiểm thử phải được lập hồ sơ đầy đủ, phục vụ công tác thẩm định thiết kế thi công và nghiệm thu bàn giao theo đúng quy định tại Chương II Nghị định 73/2019/NĐ-CP.

### 5.3. Yêu Cầu Về Hiệu Năng và Khả Năng Mở Rộng (Performance & Scalability)

1. **Quy mô dữ liệu lớn (> 10.000 cán bộ nhân viên)**:
   - Doanh nghiệp có quy mô hơn 10.000 nhân sự. Thời gian đáp ứng của phương thức `getEmployees` khi nạp toàn bộ danh sách không được vượt quá 3,0 giây trong điều kiện tải bình thường.
   - Thao tác thêm mới (`addEmployees`) và cập nhật (`updateEmployee`) phải phản hồi trong thời gian dưới 500 mili-giây.
2. **Khả năng chịu tải đồng thời (Concurrency)**:
   - Hệ thống dịch vụ web phải chịu được tối thiểu 100 phiên yêu cầu đồng thời (concurrent requests) từ các client mà không làm tăng tỷ lệ lỗi quá 0,1%.
3. **Tính toàn vẹn giao dịch (ACID Transactions)**:
   - Các thao tác cập nhật dữ liệu phải được bọc trong các giao dịch cơ sở dữ liệu nghiêm ngặt. Nếu có bất kỳ sự cố kết nối nào xảy ra giữa chừng, hệ thống phải tự động hoàn tác để ngăn chặn tình trạng dữ liệu mâu thuẫn.

---

## 6. Tiêu Chuẩn Lập Trình và Kế Hoạch Nghiệm Thu (Acceptance & Evaluation Matrix)

Để phục vụ công tác nghiệm thu dự án cũng như đối soát với barem đánh giá chuyên môn kỹ thuật, bảng ma trận dưới đây phân rã chi tiết tiêu chí nghiệm thu theo thang điểm chuẩn.

### 6.1. Ma Trận Nghiệm Thu Kỹ Thuật (Technical Evaluation Rubric)

| Hạng mục nghiệm thu | Trọng số (Điểm) | Tiêu chí kỹ thuật đánh giá chi tiết | Điều kiện đạt (Acceptance Criteria) |
| :--- | :---: | :--- | :--- |
| **1. Cơ sở dữ liệu (Database Table)** | 1.0 | Thiết kế và hiện thực bảng dữ liệu nhân viên trong RDBMS. | Bảng được tạo với đúng các trường `id`, `name`, `salary`. Khóa chính được thiết lập đúng, có ràng buộc hợp lệ dữ liệu. |
| **2. Lớp thực thể (Employee Class)** | 1.0 | Xây dựng lớp đối tượng Java `Employee` đúng chuẩn OOP/POJO. | Lớp có đủ 3 thuộc tính `id`, `name`, `salary`; có Constructor mặc định, Constructor đủ tham số, và các hàm Getter/Setter hợp lệ. |
| **3. Phương thức `getEmployees`** | 2.0 | Cung cấp dịch vụ lấy toàn bộ nhân viên dạng mảng. | Phương thức kết nối DB thành công, truy xuất toàn bộ bản ghi và trả về chính xác một mảng Java `Employee[]`. |
| **4. Phương thức `addEmployees`** | 2.0 | Cung cấp dịch vụ thêm mới hồ sơ nhân viên. | Phương thức tiếp nhận đối tượng `Employee`, kiểm tra hợp lệ, thêm thành công bản ghi vào bảng DB và trả về kết quả thành công. |
| **5. Phương thức `updateEmployee`** | 2.0 | Cung cấp dịch vụ cập nhật hồ sơ nhân viên hiện có. | Phương thức tìm đúng nhân viên theo `id`, cập nhật chính xác các giá trị `name` và `salary` vào DB, trả về kết quả đúng. |
| **6. Ứng dụng Web kiểm thử (Web App Client)** | 5.0 | Giao diện web tương tác và kiểm thử toàn diện dịch vụ. | Giao diện web chạy độc lập, gọi thành công 3 phương thức dịch vụ, cho phép nhập liệu, hiển thị danh sách trực quan và thông báo kết quả rõ ràng. |
| **7. Phong cách lập trình (Programming Style)** | 2.0 | Chất lượng mã nguồn, quy chuẩn đặt tên và xử lý ngoại lệ. | Mã nguồn tuân thủ Clean Code, đặt tên chuẩn mực Java Convention, có đóng mở tài nguyên DB an toàn, xử lý ngoại lệ chặt chẽ. |
| **Tổng điểm nghiệm thu** | **15.0** | **Đạt chuẩn bàn giao khi đạt tối thiểu 80% tổng điểm và không có lỗi nghiêm trọng (Critical Bug).** | |

### 6.2. Quy Chuẩn Phong Cách Lập Trình (Clean Code & Quality Guidelines)

Nhằm đảm bảo điểm tối đa ở tiêu chí "Programming Style" và duy trì chất lượng hệ thống cấp doanh nghiệp:
1. **Quy chuẩn đặt tên (Naming Conventions)**:
   - Tên lớp theo dạng `PascalCase` (ví dụ: `Employee`, `EmployeeWebService`).
   - Tên phương thức, biến theo dạng `camelCase` (ví dụ: `getEmployees`, `addEmployees`, `updateEmployee`, `employeeList`).
   - Tên bảng và cột trong cơ sở dữ liệu dùng chữ thường nối dấu gạch dưới `snake_case` (ví dụ: `employee`, `salary`).
2. **Quản lý tài nguyên cơ sở dữ liệu**:
   - Sử dụng cơ chế `try-with-resources` hoặc bảo đảm đóng kết nối (`Connection.close()`), đối tượng phát biểu (`Statement.close()`) và tập kết quả (`ResultSet.close()`) trong khối `finally` để triệt tiêu nguy cơ rò rỉ kết nối (Connection Leak).
3. **Bắt và xử lý ngoại lệ (Exception Handling)**:
   - Tuyệt đối không để khối bắt ngoại lệ trống rỗng (`empty catch block`).
   - Phân biệt rõ ngoại lệ người dùng (dữ liệu sai) và ngoại lệ hệ thống (mất kết nối cơ sở dữ liệu), ghi log chi tiết phục vụ việc rà soát sự cố.

---

## 7. Yêu Cầu Chuyển Giao và Kế Hoạch Vận Hành (Transition Requirements)

1. **Đóng gói giải pháp**:
   - Tầng dịch vụ web được đóng gói thành tệp lưu trữ triển khai chuẩn (`.war` hoặc file thực thi `.jar` độc lập).
   - Ứng dụng Web Client kiểm thử được cấu hình trỏ tới Endpoint dịch vụ thông qua tệp cấu hình động (không cứng hóa URL trong mã nguồn).
2. **Kịch bản kiểm thử mẫu (Test Scenarios)**:
   - Kịch bản TC-01: Kiểm tra nạp danh sách ban đầu khi DB rỗng $\rightarrow$ trả về mảng 0 phần tử.
   - Kịch bản TC-02: Thêm liên tiếp 3 nhân viên với thông tin mẫu $\rightarrow$ kiểm tra DB và gọi lại `getEmployees` để đối soát số lượng và giá trị từng thuộc tính.
   - Kịch bản TC-03: Cập nhật tăng lương cho nhân viên có sẵn $\rightarrow$ kiểm tra giá trị mới đã được cập nhật chính xác trong DB.
   - Kịch bản TC-04: Thử thêm nhân viên với `id` trùng lặp hoặc lương âm $\rightarrow$ kiểm tra hệ thống phát hiện lỗi và từ chối xử lý an toàn.
