/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.Project;

/**
 *
 * @author PHC
 */
public class Project {

    private String pname;
    private int pnumber;
    private String plocation;
    private int dnum;
    
    public Project(String pname, int pnumber, String plocation, int dnum) {
        this.pname = pname;
        this.pnumber = pnumber;
        this.plocation = plocation;
        this.dnum = dnum;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getPnumber() {
        return pnumber;
    }

    public void setPnumber(int pnumber) {
        this.pnumber = pnumber;
    }

    public String getPlocation() {
        return plocation;
    }

    public void setPlocation(String plocation) {
        this.plocation = plocation;
    }

    public int getDnum() {
        return dnum;
    }

    public void setDnum(int dnum) {
        this.dnum = dnum;
    }

    @Override
    public String toString() {
        return "Project{" + "pname=" + pname + ", pnumber=" + pnumber + ", plocation=" + plocation + ", dnum=" + dnum + '}';
    }


}
