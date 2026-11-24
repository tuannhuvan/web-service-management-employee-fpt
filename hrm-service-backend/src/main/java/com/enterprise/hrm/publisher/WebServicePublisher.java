package com.enterprise.hrm.publisher;

import com.enterprise.hrm.service.EmployeeServiceImpl;
import javax.xml.ws.Endpoint;
import java.util.logging.Logger;

/**
 * Web Service Publisher - Chạy độc lập (Standalone Execution)
 * Cho phép khởi chạy SOAP Web Service ngay lập tức trên máy trạm mà không cần cài đặt hoặc khởi động GlassFish/Tomcat.
 * Phù hợp để kiểm thử nhanh, sinh Client Stub tự động qua wsimport và chạy demo.
 */
public class WebServicePublisher {

    private static final Logger LOGGER = Logger.getLogger(WebServicePublisher.class.getName());

    public static final String DEFAULT_URL = "http://localhost:8080/HRMWebService/EmployeeService";

    public static void main(String[] args) {
        String url = DEFAULT_URL;
        if (args.length > 0 && args[0] != null && !args[0].trim().isEmpty()) {
            url = args[0].trim();
        }

        LOGGER.info("=================================================================");
        LOGGER.info("   ĐANG KHỞI ĐỘNG ENTERPRISE EMPLOYEE SOAP WEB SERVICE...");
        LOGGER.info("=================================================================");
        LOGGER.info("Publishing Endpoint tại URL: " + url);
        LOGGER.info("WSDL URL: " + url + "?wsdl");

        try {
            Endpoint endpoint = Endpoint.publish(url, new EmployeeServiceImpl());
            LOGGER.info(">>> DỊCH VỤ WEB SERVICE ĐÃ SẴN SÀNG PHỤC VỤ! <<<");
            LOGGER.info("Trạng thái Endpoint: " + (endpoint.isPublished() ? "PUBLISHED (Hoạt động)" : "STOPPED"));
            LOGGER.info("Nhấn Ctrl+C để dừng dịch vụ.");
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi publish Web Service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
