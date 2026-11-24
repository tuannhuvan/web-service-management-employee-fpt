-- =============================================================================
-- HỆ THỐNG QUẢN LÝ NHÂN SỰ DOANH NGHIỆP (ENTERPRISE HRM WEB SERVICE SYSTEM)
-- Script khởi tạo Cơ sở Dữ liệu & Bảng ghi mẫu theo chuẩn database-schema.md
-- Tuân thủ Nghị định 13/2023/NĐ-CP về Bảo vệ dữ liệu cá nhân & Ghi vết kiểm toán
-- =============================================================================

-- 1. Tạo Cơ sở Dữ liệu (Nếu chưa tồn tại)
CREATE DATABASE IF NOT EXISTS hrm_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE hrm_db;

-- 2. Khởi tạo Bảng Thực thể Chính (employees)
-- Theo yêu cầu DDL: id VARCHAR(20) PRIMARY KEY, name VARCHAR(100) NOT NULL, salary DOUBLE NOT NULL
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
    id VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    salary DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_employees PRIMARY KEY (id),
    CONSTRAINT chk_employee_salary CHECK (salary >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Tạo Index Tối ưu Hiệu năng Truy vấn
CREATE INDEX idx_employees_name ON employees (name);
CREATE INDEX idx_employees_salary ON employees (salary);

-- 4. Khởi tạo Bảng Phụ trợ Ghi vết Kiểm toán (audit_logs)
-- Ghi lại lịch sử biến động thu nhập & thao tác theo Nghị định 13/2023/NĐ-CP
CREATE TABLE audit_logs (
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

-- 5. Nạp Bản ghi Mẫu Ban đầu (Seed Data - Tối thiểu 3 bản ghi với lương hợp lệ > 0)
INSERT INTO employees (id, name, salary) VALUES
('EMP001', 'Nguyen Van An', 25000000.0),
('EMP002', 'Tran Thi Binh', 32000000.0),
('EMP003', 'Le Van Cuong', 18500000.0),
('EMP004', 'Pham Minh Duc', 45000000.0),
('EMP005', 'Hoang Mai Linh', 22000000.0);

-- 6. Ghi vết Bản ghi Mẫu vào audit_logs
INSERT INTO audit_logs (employee_id, action_type, old_salary, new_salary, changed_by) VALUES
('EMP001', 'INSERT', NULL, 25000000.0, 'SYSTEM_INIT'),
('EMP002', 'INSERT', NULL, 32000000.0, 'SYSTEM_INIT'),
('EMP003', 'INSERT', NULL, 18500000.0, 'SYSTEM_INIT'),
('EMP004', 'INSERT', NULL, 45000000.0, 'SYSTEM_INIT'),
('EMP005', 'INSERT', NULL, 22000000.0, 'SYSTEM_INIT');

-- Kiểm tra tính sẵn sàng của dữ liệu
SELECT * FROM employees;
SELECT * FROM audit_logs;
