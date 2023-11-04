/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.Employee;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author PHC
 */
public class Employee {

    private String fname;
    private String minit;
    private String lname;
    private String ssn;
    private Date bdate;
    private String address;
    private String sex;
    private Double salary;
    private String superSsn; // 상사의 SSN
    private int dno; // 부서 번호
    private Date created;
    private Date modified;
    private String dname; //DEPARTMENT에서 가져온 Dname
    private boolean isSelected;  // 체크 박스 상태를 저장하기 위한 필드

    // 생성자
    public Employee(String fname, String minit, String lname, String ssn, Date bdate, String address, String sex, Double salary, String superSsn, int dno, Date created, Date modified, String Dname) {
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
        this.ssn = ssn;
        this.bdate = bdate;
        this.address = address;
        this.sex = sex;
        this.salary = salary;
        this.superSsn = superSsn;
        this.dno = dno;
        this.created = created;
        this.modified = modified;
        this.dname = Dname;
    }

    
        public Employee(String fname, String minit, String lname, String ssn, Date bdate, String address, String sex, Double salary, String superSsn, int dno, String Dname) {
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
        this.ssn = ssn;
        this.bdate = bdate;
        this.address = address;
        this.sex = sex;
        this.salary = salary;
        this.superSsn = superSsn;
        this.dno = dno;
        this.created = null;
        this.modified = null;
        this.dname = Dname;
    }
    // isSelected 필드에 대한 getter와 setter도 추가해주세요.
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    // Getter 및 Setter 메서드
    // ... 각 필드에 대한 getter와 setter를 여기에 구현 ...
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMinit() {
        return minit;
    }

    public void setMinit(String minit) {
        this.minit = minit;
    }

    // ... 이하 생략 (다른 필드에 대한 getter와 setter) ...
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        this.bdate = bdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getSuperSsn() {
        return superSsn;
    }

    public void setSuperSsn(String superSsn) {
        this.superSsn = superSsn;
    }

    public int getDno() {
        return dno;
    }

    public void setDno(int dno) {
        this.dno = dno;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "Employee{" + ",isSelected=" + isSelected  + "fname=" + fname + ", minit=" + minit + ", lname=" + lname + ", ssn=" + ssn + ", bdate=" + bdate + ", address=" + address + ", sex=" + sex + ", salary=" + salary + ", superSsn=" + superSsn + ", dno=" + dno + ", created=" + created + ", modified=" + modified + ", dname=" + dname + '}';
    }



}
