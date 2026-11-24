<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hệ Thống Quản Lý Nhân Sự - Enterprise HRM Web Service</title>
    <style>
        :root {
            --primary: #1e40af;
            --primary-hover: #1d4ed8;
            --success: #15803d;
            --success-hover: #166534;
            --warning: #b45309;
            --danger: #b91c1c;
            --gray-bg: #f8fafc;
            --card-bg: #ffffff;
            --border-color: #e2e8f0;
            --text-main: #0f172a;
            --text-muted: #64748b;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        }

        body {
            background-color: var(--gray-bg);
            color: var(--text-main);
            padding: 24px;
            line-height: 1.5;
        }

        .container {
            max-width: 1100px;
            margin: 0 auto;
        }

        header {
            background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
            color: #ffffff;
            padding: 24px 32px;
            border-radius: 12px;
            margin-bottom: 24px;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
        }

        header h1 {
            font-size: 24px;
            font-weight: 700;
            margin-bottom: 6px;
        }

        header p {
            font-size: 14px;
            opacity: 0.9;
        }

        .status-badge {
            display: inline-block;
            font-size: 12px;
            font-weight: 600;
            padding: 4px 10px;
            border-radius: 9999px;
            margin-top: 10px;
        }
        .status-online { background-color: #dcfce7; color: #15803d; }
        .status-offline { background-color: #fee2e2; color: #b91c1c; }

        /* Banners */
        .alert {
            padding: 14px 18px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .alert-success { background-color: #dcfce7; color: #166534; border: 1px solid #bbf7d0; }
        .alert-warning { background-color: #fef3c7; color: #92400e; border: 1px solid #fde68a; }
        .alert-danger  { background-color: #fee2e2; color: #991b1b; border: 1px solid #fecaca; }
        .alert-info    { background-color: #e0f2fe; color: #075985; border: 1px solid #bae6fd; }

        /* Card Section */
        .card {
            background-color: var(--card-bg);
            border-radius: 10px;
            padding: 24px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.05);
            border: 1px solid var(--border-color);
            margin-bottom: 24px;
        }

        .card-title {
            font-size: 18px;
            font-weight: 600;
            margin-bottom: 16px;
            color: var(--text-main);
            border-bottom: 2px solid var(--border-color);
            padding-bottom: 8px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        /* Form Layout */
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
            gap: 16px;
            margin-bottom: 20px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-group label {
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 6px;
            color: var(--text-main);
        }

        .form-group label .required {
            color: var(--danger);
        }

        .form-control {
            padding: 10px 14px;
            border: 1px solid #cbd5e1;
            border-radius: 6px;
            font-size: 14px;
            transition: border-color 0.15s ease-in-out;
            outline: none;
        }

        .form-control:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
        }

        .form-text {
            font-size: 12px;
            color: var(--text-muted);
            margin-top: 4px;
        }

        /* Button Group - Khu vực nút bấm thao tác */
        .btn-group {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 10px 20px;
            font-size: 14px;
            font-weight: 600;
            border-radius: 6px;
            border: none;
            cursor: pointer;
            transition: all 0.2s ease;
            text-decoration: none;
        }

        .btn-success {
            background-color: var(--success);
            color: #ffffff;
        }
        .btn-success:hover {
            background-color: var(--success-hover);
        }

        .btn-primary {
            background-color: var(--primary);
            color: #ffffff;
        }
        .btn-primary:hover {
            background-color: var(--primary-hover);
        }

        .btn-secondary {
            background-color: #64748b;
            color: #ffffff;
        }
        .btn-secondary:hover {
            background-color: #475569;
        }

        .btn-outline {
            background-color: transparent;
            border: 1px solid #cbd5e1;
            color: var(--text-main);
        }
        .btn-outline:hover {
            background-color: #f1f5f9;
        }

        .btn-sm {
            padding: 6px 12px;
            font-size: 12px;
        }

        /* Data Table - Khu vực bảng hiển thị Zebra Striping */
        .table-responsive {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
            text-align: left;
        }

        thead tr {
            background-color: #0f172a;
            color: #ffffff;
        }

        thead th {
            padding: 12px 16px;
            font-weight: 600;
        }

        tbody tr {
            border-bottom: 1px solid var(--border-color);
            transition: background-color 0.15s ease;
        }

        /* Zebra striping */
        tbody tr:nth-child(even) {
            background-color: #f8fafc;
        }
        tbody tr:nth-child(odd) {
            background-color: #ffffff;
        }

        tbody tr:hover {
            background-color: #e0f2fe !important;
        }

        tbody td {
            padding: 12px 16px;
            vertical-align: middle;
        }

        .salary-column {
            font-family: "Courier New", Courier, monospace;
            font-weight: 700;
            color: var(--success);
            text-align: right;
        }

        .empty-message {
            text-align: center;
            padding: 32px 16px;
            color: var(--text-muted);
            font-style: italic;
        }

        footer {
            text-align: center;
            font-size: 13px;
            color: var(--text-muted);
            margin-top: 32px;
        }
    </style>
</head>
<body>

<div class="container">
    <!-- Header -->
    <header>
        <h1>Quản Lý Nhân Sự Doanh Nghiệp (Enterprise HRM)</h1>
        <p>JAX-WS SOAP Web Service Client | Tuân thủ Nghị định 13/2023/NĐ-CP & Tiêu chuẩn SOA</p>
        <span class="status-badge ${serviceStatus == 'ONLINE' ? 'status-online' : 'status-offline'}">
            ● Dịch vụ Web Service: ${serviceStatus}
        </span>
    </header>

    <!-- Thông báo phản hồi (Alert Banner) -->
    <c:if test="${not empty alertMessage}">
        <div class="alert alert-${alertType}">
            <span><strong>Thông báo:</strong> <c:out value="${alertMessage}"/></span>
        </div>
    </c:if>

    <c:if test="${not empty serviceMessage}">
        <div class="alert alert-warning">
            <span><strong>Cảnh báo kết nối:</strong> <c:out value="${serviceMessage}"/></span>
        </div>
    </c:if>

    <!-- KHU VỰC 1 & 2: BẢNG ĐIỀU KHIỂN, NHẬP LIỆU & NÚT BẤM THAO TÁC -->
    <div class="card">
        <div class="card-title">
            <span>Khu Vực Nhập Liệu & Thao Tác Nghiệp Vụ</span>
            <small style="font-size: 12px; font-weight: normal; color: var(--text-muted);">
                Endpoint: <c:out value="${endpointUrl}"/>
            </small>
        </div>

        <form id="employeeForm" method="POST" action="employees" onsubmit="return validateForm()">
            <!-- Hidden action parameter: 'add' hoặc 'update' -->
            <input type="hidden" id="formAction" name="action" value="add"/>

            <div class="form-grid">
                <!-- Mã nhân viên (ID) -->
                <div class="form-group">
                    <label for="empId">Mã Nhân Viên (ID) <span class="required">*</span></label>
                    <input type="text" id="empId" name="id" class="form-control" 
                           placeholder="Ví dụ: EMP001, EMP002..." 
                           value="<c:out value='${formId}'/>" required />
                    <span class="form-text">Mã định danh duy nhất (không trùng lặp).</span>
                </div>

                <!-- Họ và tên (Name) -->
                <div class="form-group">
                    <label for="empName">Họ và Tên (Name) <span class="required">*</span></label>
                    <input type="text" id="empName" name="name" class="form-control" 
                           placeholder="Ví dụ: Nguyễn Văn An" 
                           value="<c:out value='${formName}'/>" required minlength="2" />
                    <span class="form-text">Tối thiểu 2 ký tự hợp lệ.</span>
                </div>

                <!-- Mức lương (Salary) -->
                <div class="form-group">
                    <label for="empSalary">Mức Lương (Salary - VND) <span class="required">*</span></label>
                    <input type="number" id="empSalary" name="salary" class="form-control" 
                           placeholder="Ví dụ: 25000000" step="1000" min="0" 
                           value="<c:out value='${formSalary}'/>" required />
                    <span class="form-text">Thu nhập cơ sở không âm (>= 0).</span>
                </div>
            </div>

            <!-- KHU VỰC 2: THANH CÔNG CỤ TÁC VỤ (BUTTONS) -->
            <div class="btn-group">
                <!-- Nút 1: Add Employee (Màu xanh lá) -->
                <button type="button" class="btn btn-success" onclick="submitAction('add')">
                    ＋ Thêm Mới (Add Employee)
                </button>

                <!-- Nút 2: Update Employee (Màu xanh dương) -->
                <button type="button" class="btn btn-primary" onclick="submitAction('update')">
                    ✎ Cập Nhật (Update Employee)
                </button>

                <!-- Nút 3: Refresh List (Màu xám bạc) -->
                <a href="employees" class="btn btn-secondary">
                    ⟳ Làm Mới (Refresh List)
                </a>

                <!-- Nút xóa trắng Form -->
                <button type="button" class="btn btn-outline" onclick="resetForm()">
                    ✕ Nhập lại
                </button>
            </div>
        </form>
    </div>

    <!-- KHU VỰC 3: BẢNG DỮ LIỆU HIỂN THỊ (DATA TABLE ZEBRA STRIPING) -->
    <div class="card">
        <div class="card-title">
            <span>Danh Sách Nhân Viên Hiện Hữu</span>
            <span style="font-size: 13px; font-weight: normal; color: var(--text-muted);">
                Tổng cộng: <strong>${employeeList != null ? employeeList.size() : 0}</strong> nhân sự
            </span>
        </div>

        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th style="width: 60px; text-align: center;">STT</th>
                        <th style="width: 140px;">Mã Nhân Viên</th>
                        <th>Họ và Tên</th>
                        <th style="width: 180px; text-align: right;">Mức Lương Cơ Sở</th>
                        <th style="width: 120px; text-align: center;">Thao Tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty employeeList}">
                            <c:forEach var="emp" items="${employeeList}" varStatus="status">
                                <tr>
                                    <td style="text-align: center; color: var(--text-muted);">${status.index + 1}</td>
                                    <td><strong><c:out value="${emp.id}"/></strong></td>
                                    <td><c:out value="${emp.name}"/></td>
                                    <td class="salary-column">
                                        <fmt:formatNumber value="${emp.salary}" type="currency" currencySymbol="VND" maxFractionDigits="0"/>
                                    </td>
                                    <td style="text-align: center;">
                                        <button type="button" class="btn btn-outline btn-sm"
                                                onclick="populateForm('<c:out value="${emp.id}"/>', '<c:out value="${emp.name}"/>', '${emp.salary}')">
                                            Chọn sửa
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="empty-message">
                                    Không có bản ghi nhân viên nào trong cơ sở dữ liệu hoặc chưa kết nối được dịch vụ.
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

    <footer>
        <p>Hệ thống Quản lý Nhân sự Doanh nghiệp &copy; 2026. Chuẩn kiến trúc JAX-WS SOAP 1.1 / WSDL 1.1.</p>
    </footer>
</div>

<script>
    // Client-side Validation theo đúng fe-rules.md
    function validateForm() {
        var id = document.getElementById("empId").value.trim();
        var name = document.getElementById("empName").value.trim();
        var salary = document.getElementById("empSalary").value;

        if (id === "") {
            alert("Lỗi xác thực: Mã nhân viên (ID) không được để trống!");
            document.getElementById("empId").focus();
            return false;
        }

        if (name.length < 2) {
            alert("Lỗi xác thực: Họ tên nhân viên phải có tối thiểu 2 ký tự!");
            document.getElementById("empName").focus();
            return false;
        }

        if (salary === "" || isNaN(salary) || Number(salary) < 0) {
            alert("Lỗi xác thực: Mức lương phải là một số không âm (>= 0)!");
            document.getElementById("empSalary").focus();
            return false;
        }

        return true;
    }

    function submitAction(action) {
        document.getElementById("formAction").value = action;
        if (validateForm()) {
            document.getElementById("employeeForm").submit();
        }
    }

    function populateForm(id, name, salary) {
        document.getElementById("empId").value = id;
        document.getElementById("empName").value = name;
        document.getElementById("empSalary").value = salary;
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    function resetForm() {
        document.getElementById("empId").value = "";
        document.getElementById("empName").value = "";
        document.getElementById("empSalary").value = "";
        document.getElementById("formAction").value = "add";
        document.getElementById("empId").focus();
    }
</script>

</body>
</html>
