/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.Department;

/**
 *
 * @author PHC
 */
public class Department {
    private String dname;
    private String dnumber;
    private String mgr_ssn;
    private String mgr_start_date;

    public Department(String danme, String dnumber, String mgr_ssn, String mgr_start_date) {
        this.dname = danme;
        this.dnumber = dnumber;
        this.mgr_ssn = mgr_ssn;
        this.mgr_start_date = mgr_start_date;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String danme) {
        this.dname = danme;
    }

    public String getDnumber() {
        return dnumber;
    }

    public void setDnumber(String dnumber) {
        this.dnumber = dnumber;
    }

    public String getMgr_ssn() {
        return mgr_ssn;
    }

    public void setMgr_ssn(String mgr_ssn) {
        this.mgr_ssn = mgr_ssn;
    }

    public String getMgr_start_date() {
        return mgr_start_date;
    }

    public void setMgr_start_date(String mgr_start_date) {
        this.mgr_start_date = mgr_start_date;
    }

    @Override
    public String toString() {
        return "Department{" + "danme=" + dname + ", dnumber=" + dnumber + ", mgr_ssn=" + mgr_ssn + ", mgr_start_date=" + mgr_start_date + '}';
    }
    
    
    
}
