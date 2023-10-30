/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.controller;

/**
 *
 * @author PHC
 */
public class SQLQueryBuilder {

    public static String createEmpolyeeWhereClause(String condition1, String condition2, String searchCombo3Text, String searchText) {
        // 기본적으로 WHERE 절을 시작합니다. 빈 문자열이 반환되면 WHERE 절이 쿼리에서 제거되어야 합니다.
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
                    return ""; // 이 경우, 후속 처리를 중단하고 여기서 반환합니다.
            }
        }

        // 두 번째 조건을 기반으로 필터를 추가합니다. condition2 값에 따라 다른 연산자를 사용합니다.
        if (condition2 != null && !condition2.isEmpty()) {
//        if (!condition.toString().endsWith("WHERE ")) {
//            // 첫 번째 조건이 이미 설정되어 있는 경우, AND로 조건을 연결합니다.
//            condition.append(" AND ");
//        }
            if (searchText.equals("")) {
                switch (condition2) {
                    case "=":
                        condition.append("IS NULL");
                        break;
                    case "!=":
                        condition.append("IS NOT NULL");
                        break;
                    // 추가적인 조건들...
                    default:
                        // 알 수 없는 연산자가 들어오면, 조건을 추가하지 않고 WHERE절없이 처리
                        condition = new StringBuilder("");
                        break;
                }
                return condition.toString();
            }
            // 여기에서는 condition2의 값을 기반으로 다양한 SQL 연산자를 처리할 수 있습니다.
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
                // 추가적인 조건들...
                default:
                    // 알 수 없는 연산자가 들어오면, 조건을 추가하지 않고 WHERE절없이 처리
                    condition = new StringBuilder("");
                    break;
            }
        }

        return condition.toString();
    }
}
