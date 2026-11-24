package com.enterprise.hrm.client.util;

import com.enterprise.hrm.client.generated.EmployeeService;
import com.enterprise.hrm.client.generated.EmployeeWebService;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

/**
 * ServiceClientFactory
 * Quản lý khởi tạo và cấu hình Client Stub kết nối tới JAX-WS Web Service.
 * Đọc cấu hình linh hoạt từ file client.properties, System Properties hoặc Biến môi trường.
 */
public class ServiceClientFactory {

    private static final Logger LOGGER = Logger.getLogger(ServiceClientFactory.class.getName());
    private static final Properties PROPERTIES = new Properties();

    private static String endpointUrl;
    private static String wsdlUrl;

    static {
        loadConfiguration();
    }

    private static synchronized void loadConfiguration() {
        try (InputStream is = ServiceClientFactory.class.getClassLoader().getResourceAsStream("client.properties")) {
            if (is != null) {
                PROPERTIES.load(is);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Không thể đọc client.properties: " + ex.getMessage());
        }

        endpointUrl = getPropertyOrEnv("ws.endpoint.url", "WS_ENDPOINT_URL",
                "http://localhost:8080/hrm-service-backend/EmployeeWebService");
        wsdlUrl = getPropertyOrEnv("ws.wsdl.url", "WS_WSDL_URL",
                "http://localhost:8080/hrm-service-backend/EmployeeWebService?wsdl");
    }

    private static String getPropertyOrEnv(String propKey, String envKey, String defaultValue) {
        String sysProp = System.getProperty(propKey);
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            return sysProp.trim();
        }
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }
        String fileProp = PROPERTIES.getProperty(propKey);
        if (fileProp != null && !fileProp.trim().isEmpty()) {
            return fileProp.trim();
        }
        return defaultValue;
    }

    /**
     * Khởi tạo EmployeeService Port kết nối tới Backend.
     * Tự động điều chỉnh BindingProvider.ENDPOINT_ADDRESS_PROPERTY theo URL cấu hình.
     *
     * @return EmployeeService proxy instance
     */
    public static EmployeeService createEmployeeServiceClient() {
        try {
            QName serviceQName = new QName("urn:techfirm:hrm:service:endpoint", "EmployeeWebService");
            URL serviceWsdl = new URL(wsdlUrl);
            EmployeeWebService service = new EmployeeWebService(serviceWsdl, serviceQName);
            EmployeeService port = service.getEmployeeWebServicePort();

            // Thiết lập Endpoint Address động
            BindingProvider bp = (BindingProvider) port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
            
            return port;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Không thể tạo Service từ URL WSDL (" + wsdlUrl + "): " + e.getMessage() + ". Thử khởi tạo trực tiếp với port...");
            try {
                EmployeeWebService service = new EmployeeWebService();
                EmployeeService port = service.getEmployeeWebServicePort();
                BindingProvider bp = (BindingProvider) port;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
                return port;
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Lỗi nghiêm trọng khi khởi tạo Client Stub: " + ex.getMessage(), ex);
                throw new RuntimeException("Không thể kết nối tới Web Service Backend: " + ex.getMessage(), ex);
            }
        }
    }

    public static String getEndpointUrl() {
        return endpointUrl;
    }

    public static String getWsdlUrl() {
        return wsdlUrl;
    }
}
