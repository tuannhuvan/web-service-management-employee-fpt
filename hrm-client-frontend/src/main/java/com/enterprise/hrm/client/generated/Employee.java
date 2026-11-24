package com.enterprise.hrm.client.generated;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Client-Side JAXB Bean: Employee
 * Tương thích 100% với cấu trúc schema WSDL sinh ra từ Service Endpoint.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Employee", namespace = "urn:techfirm:hrm:service:endpoint", propOrder = {
    "id",
    "name",
    "salary"
})
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(required = true)
    protected String id;

    @XmlElement(required = true)
    protected String name;

    @XmlElement(required = true)
    protected double salary;

    public Employee() {
    }

    public Employee(String id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double value) {
        this.salary = value;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
