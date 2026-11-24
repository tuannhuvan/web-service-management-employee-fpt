package com.enterprise.hrm.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Enterprise Employee Entity Class
 * Tuân thủ chuẩn Java Bean (POJO) và JAXB XML Binding cho JAX-WS SOAP Web Services.
 * Gồm đúng 3 trường dữ liệu: id (String), name (String), salary (double).
 */
@XmlRootElement(name = "Employee")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Employee", propOrder = {
    "id",
    "name",
    "salary"
})
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(required = true)
    private String id;

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private double salary;

    /**
     * Default No-arg Constructor
     * BẮT BUỘC cho quá trình JAXB / SOAP XML Serialization & Deserialization.
     */
    public Employee() {
    }

    /**
     * Parameterized Constructor
     * Khởi tạo đối tượng nhân viên với đầy đủ 3 trường thông tin.
     *
     * @param id     Mã định danh duy nhất của nhân viên (String)
     * @param name   Họ và tên nhân viên (String)
     * @param salary Mức thu nhập cơ sở (double >= 0.0)
     */
    public Employee(String id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Double.compare(employee.salary, salary) == 0 &&
               Objects.equals(id, employee.id) &&
               Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary);
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
