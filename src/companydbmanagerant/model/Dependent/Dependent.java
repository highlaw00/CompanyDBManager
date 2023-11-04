/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.Dependent;

import java.util.Date;

/**
 *
 * @author PHC
 */
public class Dependent {

    private String essn;
    private String dependentname;
    private String sex;
    private Date bdate;
    private String relationship; 

    public Dependent(String essn, String dependentname, String sex, Date bdate, String relationship) {
        this.essn = essn;
        this.dependentname = dependentname;
        this.sex = sex;
        this.bdate = bdate;
        this.relationship = relationship;
    }

    public String getEssn() {
        return essn;
    }

    public void setEssn(String essn) {
        this.essn = essn;
    }

    public String getDependentname() {
        return dependentname;
    }

    public void setDependentname(String dependentname) {
        this.dependentname = dependentname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        this.bdate = bdate;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return "Dependent{" + "essn=" + essn + ", dependentname=" + dependentname + ", sex=" + sex + ", bdate=" + bdate + ", relationship=" + relationship + '}';
    }
    
}
