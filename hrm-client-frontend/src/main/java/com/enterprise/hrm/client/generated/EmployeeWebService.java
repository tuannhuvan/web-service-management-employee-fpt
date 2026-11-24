package com.enterprise.hrm.client.generated;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/**
 * Client Service Proxy Stub
 * Kế thừa javax.xml.ws.Service để tạo proxy kết nối tới Web Service Endpoint.
 */
@WebServiceClient(
    name = "EmployeeWebService",
    targetNamespace = "urn:techfirm:hrm:service:endpoint"
)
public class EmployeeWebService extends Service {

    private static final URL EMPLOYEEWEBSERVICE_WSDL_LOCATION;
    private static final WebServiceException EMPLOYEEWEBSERVICE_EXCEPTION;
    private static final QName EMPLOYEEWEBSERVICE_QNAME = new QName("urn:techfirm:hrm:service:endpoint", "EmployeeWebService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/hrm-service-backend/EmployeeWebService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        EMPLOYEEWEBSERVICE_WSDL_LOCATION = url;
        EMPLOYEEWEBSERVICE_EXCEPTION = e;
    }

    public EmployeeWebService() {
        super(__getWsdlLocation(), EMPLOYEEWEBSERVICE_QNAME);
    }

    public EmployeeWebService(WebServiceFeature... features) {
        super(__getWsdlLocation(), EMPLOYEEWEBSERVICE_QNAME, features);
    }

    public EmployeeWebService(URL wsdlLocation) {
        super(wsdlLocation, EMPLOYEEWEBSERVICE_QNAME);
    }

    public EmployeeWebService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, EMPLOYEEWEBSERVICE_QNAME, features);
    }

    public EmployeeWebService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EmployeeWebService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    @WebEndpoint(name = "EmployeeWebServicePort")
    public EmployeeService getEmployeeWebServicePort() {
        return super.getPort(new QName("urn:techfirm:hrm:service:endpoint", "EmployeeWebServicePort"), EmployeeService.class);
    }

    @WebEndpoint(name = "EmployeeWebServicePort")
    public EmployeeService getEmployeeWebServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("urn:techfirm:hrm:service:endpoint", "EmployeeWebServicePort"), EmployeeService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (EMPLOYEEWEBSERVICE_EXCEPTION != null) {
            throw EMPLOYEEWEBSERVICE_EXCEPTION;
        }
        return EMPLOYEEWEBSERVICE_WSDL_LOCATION;
    }
}
