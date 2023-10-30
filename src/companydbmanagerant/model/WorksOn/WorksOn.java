/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.WorksOn;


/**
 *
 * @author PHC
 */
public class WorksOn {

    private String essn;
    private int pno;
    private double hours;

    public WorksOn(String essn, int pno, double hours) {
        this.essn = essn;
        this.pno = pno;
        this.hours = hours;
    }

    public String getEssn() {
        return essn;
    }

    public void setEssn(String essn) {
        this.essn = essn;
    }

    public int getPno() {
        return pno;
    }

    public void setPno(int pno) {
        this.pno = pno;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "WorksOn{" + "essn=" + essn + ", pno=" + pno + ", hours=" + hours + '}';
    }

  
}
