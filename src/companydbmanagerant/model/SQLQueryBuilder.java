/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model;

import companydbmanagerant.model.Employee.Employee;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 *
 * @author PHC
 */
public class SQLQueryBuilder {

    public static String createFindSSNsQuery() {
        return "SELECT E.Ssn " +
            "FROM EMPLOYEE E " ;        
    }

    public static String createFindNotSubordinatesQuery(String Ssn) {
        return "WITH RECURSIVE Subordinates AS (" +
            "    SELECT E.Ssn " +
            "    FROM EMPLOYEE E " +
            "    WHERE E.Super_ssn = " + Ssn + " " +
            "    UNION " +
            "    SELECT E2.Ssn " +
            "    FROM EMPLOYEE E2 " +
            "    JOIN Subordinates S ON E2.Super_ssn = S.Ssn " +
            ") " +
            "SELECT E3.Ssn " +
            "FROM EMPLOYEE E3 " +
            "WHERE E3.Ssn <> " + Ssn + " AND E3.Ssn NOT IN (SELECT Ssn FROM Subordinates);";
    }
    public static String createWhereClauseIfNotEmpty(String condition){
        if("".equals(condition)){
            return "";
            
        }
        else
        {
            return "WHERE "+condition;
        }
    }

    public static String createEmpolyeeWhereClause(String condition1, String condition2, String searchCombo3Text, String searchText) {
        // 기본적으로 WHERE 절을 시작하여 빈 문자열이 반환되면 WHERE 절이 쿼리에서 제거해야함
        StringBuilder condition = new StringBuilder("WHERE ");

        String singleQuote = "'";

        // 첫 번째 조건 처리
        if (condition1 != null && !condition1.isEmpty()) {
            switch (condition1) {
                case "First Name":
                    condition.append("fname ");
                    break;
                case "Minit":
                    condition.append("minit ");
                    break;
                case "Last Name":
                    condition.append("lname ");
                    break;
                case "SSN":
                    condition.append("ssn ");
                    singleQuote = "";
                    break;
                case "Birth Date":
                    condition.append("bdate ");
                    break;
                case "Address":
                    condition.append("address ");
                    break;
                case "Sex":
                    condition.append("sex ");
                    searchText = searchCombo3Text;
                    break;
                case "Salary":
                    condition.append("salary ");
                    singleQuote = "";
                    break;
                case "Super SSN":
                    condition.append("super_Ssn ");
                    singleQuote = "";
                    break;
                case "Dname":
                    condition.append("Dname ");
                    searchText = searchCombo3Text;
                    break;
                default:
                    // '전체'나 알 수 없는 값이 들어오면, WHERE 절 없이 처리합니다.
                    return "";
            }
        }


        if (condition2 != null && !condition2.isEmpty()) {
            if (searchText.equals("")) {
                switch (condition2) {
                    case "=":
                        condition.append("IS NULL");
                        break;
                    case "!=":
                        condition.append("IS NOT NULL");
                        break;
                    default:
                        // 알 수 없는 연산자가 들어오면, 조건을 추가하지 않고 WHERE절없이 처리
                        condition = new StringBuilder("");
                        break;
                }
                return condition.toString();
            }
          
            switch (condition2) {
                case "=":
                    condition.append("= ").append(singleQuote).append(searchText).append(singleQuote);
                    break;
                case "!=":
                    condition.append("!= ").append(singleQuote).append(searchText).append(singleQuote);
                    break;
                case ">":
                    condition.append("> ").append(singleQuote).append(searchText).append(singleQuote);
                    break;
                case "<":
                    condition.append("< ").append(singleQuote).append(searchText).append(singleQuote);
                    break;
                case ">=":
                    condition.append(">= ").append(singleQuote).append(searchText).append(singleQuote);
                    break;
                case "<=":
                    condition.append("<= ").append(singleQuote).append(searchText).append(singleQuote);
                    break;
                default:
                    // 알 수 없는 연산자가 들어오면, 조건을 추가하지 않고 WHERE절없이 처리
                    condition = new StringBuilder("");
                    break;
            }
        }

        return condition.toString();
    }
}
