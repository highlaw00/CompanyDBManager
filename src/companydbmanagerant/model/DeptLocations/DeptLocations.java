/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.DeptLocations;


/**
 *
 * @author PHC
 */
public class DeptLocations {
    private int dnumber;
    private String dlocation;

    public DeptLocations(int dnumber, String dlocation) {
        this.dnumber = dnumber;
        this.dlocation = dlocation;
    }

    public int getDnumber() {
        return dnumber;
    }

    public void setDnumber(int dnumber) {
        this.dnumber = dnumber;
    }

    public String getDlocation() {
        return dlocation;
    }

    public void setDlocation(String dlocation) {
        this.dlocation = dlocation;
    }

    @Override
    public String toString() {
        return "DeptLocations{" + "dnumber=" + dnumber + ", dlocation=" + dlocation + '}';
    }


  
}
