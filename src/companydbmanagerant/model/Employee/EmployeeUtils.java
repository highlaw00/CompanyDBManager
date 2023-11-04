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
import java.util.Map;

public class EmployeeUtils {

    // Define date format
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format according to your needs

    public static Employee createEmployeeFromMap(Map<String, String> fieldTexts) throws ParseException {
        // Extract and convert fields from the map
        String fname = fieldTexts.get("FirstName");
        String minit = fieldTexts.get("Minit");
        String lname = fieldTexts.get("LastName");
        String ssn = fieldTexts.get("SSN");
        Date bdate = convertToDate(fieldTexts.get("Birth"));
        String address = fieldTexts.get("Address");
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
        // Assuming dno is a fixed value, as there is no key in the map provided.
        int dno = 1; // Replace with the actual department number if available
        // Assuming created and modified dates are set to current date for new employee
        Date created = new Date(); // Replace with actual created date if available
        Date modified = new Date(); // Replace with actual modified date if available
        String dname = fieldTexts.get("Dname");

        // Create the Employee object
        return new Employee(fname, minit, lname, ssn, bdate, address, sex, salary, superSsn, dno, created, modified, dname);
    }

    private static Date convertToDate(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }
}
