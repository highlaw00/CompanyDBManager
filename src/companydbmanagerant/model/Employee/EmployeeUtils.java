/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.Employee;

/**
 *
 * @author PHC
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EmployeeUtils {

    // Define date format
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format according to your needs

    public static Map<String, String> findDiffEmployeeInfo(Employee prev, Employee changed) {
        Map<String, String> diff = new HashMap<>();

        if (!prev.getFname().equals(changed.getFname())) {
            diff.put("Fname", changed.getFname());
        }

        if (!prev.getMinit().equals(changed.getMinit())) {
            diff.put("Minit", changed.getMinit());
        }

        if (!prev.getLname().equals(changed.getLname())) {
            diff.put("Lname", changed.getLname());
        }

        if (!prev.getSsn().equals(changed.getSsn())) {
            diff.put("Ssn", changed.getSsn());
        }

        if (!prev.getBdate().equals(changed.getBdate())) {
            diff.put("Bdate", changed.getBdate().toString());
        }

        if (prev.getAddress() == null && changed.getAddress() != null
                || prev.getAddress() != null && !prev.getAddress().equals(changed.getAddress())) {
            diff.put("Address", changed.getAddress() == null ? null : changed.getAddress());
        }

        if (!prev.getSex().equals(changed.getSex())) {
            diff.put("Sex", changed.getSex());
        }

        if (prev.getSalary() == null && changed.getSalary() != null || prev.getSalary() != null && !prev.getSalary().equals(changed.getSalary())) {
            diff.put("Salary", changed.getSalary() == null ? null : changed.getSalary().toString());
        }
        if (prev.getSuperSsn() == null && changed.getSuperSsn() != null || prev.getSuperSsn() != null && !prev.getSuperSsn().equals(changed.getSuperSsn())) {
            diff.put("Super_Ssn", changed.getSuperSsn() == null ? null : changed.getSuperSsn());
        }

        if (prev.getDno() != changed.getDno()) {
            diff.put("Dno", Integer.toString(changed.getDno()));
        }

        return diff;
    }

    public static Employee createEmployeeFromMap(Map<String, String> fieldTexts) throws ParseException {
        // Extract and convert fields from the map
        String fname = fieldTexts.get("Fname");
        String minit = fieldTexts.get("Minit");
        String lname = fieldTexts.get("Lname");
        String ssn = fieldTexts.get("SSN");
        Date bdate = convertToDate(fieldTexts.get("Birth"));
        
        String address = fieldTexts.get("Address");
        if ("".equals(address)) {
            address = null;
        }

        String sex = fieldTexts.get("SEX");
        Double salary = null;
        String salaryString = fieldTexts.get("Salary");

        if (salaryString != null && !salaryString.isEmpty()) {
            try {
                salary = Double.parseDouble(salaryString);
            } catch (NumberFormatException e) {
                // 숫자 형식으로 변환할 수 없을 때의 예외 처리, salary는 이미 null로 초기화됨.
            }
        }
        String superSsn = fieldTexts.get("SuperSSN");
        if ("".equals(superSsn)) {
            superSsn = null;
        }
        
        int dno = 1; // 나중에 Employee.setDname을 통해서 받아야함 

        Date created = new Date(); 
        Date modified = new Date(); 
        String dname = fieldTexts.get("Dname");

        // 새 객체를 생성 
        return new Employee(fname, minit, lname, ssn, bdate, address, sex, salary, superSsn, dno, created, modified, dname);
    }

    private static Date convertToDate(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }
}
