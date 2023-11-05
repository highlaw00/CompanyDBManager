package companydbmanagerant.model.Employee;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import companydbmanagerant.model.DatabaseUtils;
import companydbmanagerant.model.SQLQueryBuilder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import raven.toast.Notifications;

/**
 *
 * @author PHC
 */
public class EmployeeDAO {

    public static List<String> findNotAllSubordinates(List<Employee> checkedEmployees) {
        List<String> notAllSubordinates = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            boolean isFirst = true;
            for (Employee employee : checkedEmployees) {
                List<String> notSubordinates = new ArrayList<>();

                conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옴.

                // 쿼리 준비
                String sql = SQLQueryBuilder.createFindNotSubordinatesQuery(employee.getSsn());
                pstmt = conn.prepareStatement(sql);

                // 쿼리 실행 및 결과 처리
                rs = pstmt.executeQuery();

                while (rs.next()) {

                    String ssn = rs.getString("Ssn");
                    if (isFirst) { //처음 직원의 경우 result에 결과를 담아야함 
                        notAllSubordinates.add(ssn);

                    } else {
                        notSubordinates.add(ssn);
                    }
                }
                if (isFirst) {
                    isFirst = false;
                } else {
                    // notAllSubordinates과 notSubordinates의 교집합을 찾아서
                    // notAllSubordinates에 저장
                    notAllSubordinates.retainAll(notSubordinates);
                }
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            System.err.print(e);

        } finally {
            // 사용한 자원 반환
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            }
        }

        return notAllSubordinates;
    }

    public static List<String> findNotSubordinates(String query) {
        List<String> notSubordinates = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옴
            System.out.println("DB CONNECTED E1");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = query;
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {

                String ssn = rs.getString("Ssn");
                // 리스트에 Employee 객체 추가
                notSubordinates.add(ssn);
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            System.err.print(e);

        } finally {
            // 사용한 자원 반환
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            }
        }

        return notSubordinates;
    }

    public static List<Employee> loadDataFittered(String condition) {
        List<Employee> employees = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옴.
            System.out.println("DB CONNECTED E2");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT * FROM EMPLOYEE JOIN DEPARTMENT on Dnumber=Dno " + condition;
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String fname = rs.getString("Fname");
                String minit = rs.getString("Minit");
                String lname = rs.getString("Lname");
                String ssn = rs.getString("Ssn");
                Date bdate = rs.getDate("Bdate");
                String address = rs.getString("Address");
                if (rs.wasNull()) {
                    address = null;
                }

                String sex = rs.getString("Sex");

                Double salary = null;
                try {
                    double value = rs.getDouble("Salary");
                    if (rs.wasNull()) {
                        salary = null;
                    } else {
                        salary = value;
                    }
                } catch (SQLException e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
                    e.printStackTrace();
                }

                String superSsn = rs.getString("Super_ssn");
                if (rs.wasNull()) {
                    superSsn = null;
                }
                int dno = rs.getInt("Dno");
                Timestamp created = rs.getTimestamp("created");
                Timestamp modified = rs.getTimestamp("modified");
                String Dname = rs.getString("Dname");
                // Employee 객체 생성 및 값 설정
                Employee employee = new Employee(fname, minit, lname, ssn, bdate, address, sex, salary, superSsn, dno, created, modified, Dname);

                // 리스트에 Employee 객체 추가
                employees.add(employee);
            }
        } catch (SQLException e) {
            // 예외 처리
            System.err.print(e);
        } finally {
            // 사용한 자원 반환
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            }
        }

        return employees;
    }

    public static List<Employee> loadData() {
        List<Employee> employees = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옴
            System.out.println("DB CONNECTED E3");
            // 쿼리 준비. 여기서는 모든 직원을 선택함.
            String sql = "SELECT * FROM EMPLOYEE JOIN DEPARTMENT on Dnumber=Dno ";
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String fname = rs.getString("Fname");
                String minit = rs.getString("Minit");
                String lname = rs.getString("Lname");
                String ssn = rs.getString("Ssn");
                Date bdate = rs.getDate("Bdate");
                String address = rs.getString("Address");
                if (rs.wasNull()) {
                    address = null;
                }

                String sex = rs.getString("Sex");

                Double salary = null;
                try {
                    double value = rs.getDouble("Salary");
                    if (rs.wasNull()) {
                        salary = null;
                    } else {
                        salary = value;
                    }
                } catch (SQLException e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
                    // 예외 처리
                    e.printStackTrace();
                }

                String superSsn = rs.getString("Super_ssn");
                if (rs.wasNull()) {
                    superSsn = null;
                }
                int dno = rs.getInt("Dno");
                Timestamp created = rs.getTimestamp("created");
                Timestamp modified = rs.getTimestamp("modified");
                String Dname = rs.getString("Dname");
                // Employee 객체 생성 및 값 설정
                Employee employee = new Employee(fname, minit, lname, ssn, bdate, address, sex, salary, superSsn, dno, created, modified, Dname);

                // 리스트에 Employee 객체 추가
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.print(e);
            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            System.err.println("General Exception: " + e.getMessage());
        } finally {
            // 사용한 자원 반환
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            }
        }

        return employees;
    }

    public static boolean insertEmployee(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        System.out.println(employee.toString());
        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED");
            String insertSql = "INSERT INTO EMPLOYEE VALUES (?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP())";
            pstmt = conn.prepareStatement(insertSql);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String bdate = dateFormat.format(employee.getBdate());

            pstmt.clearParameters();
            pstmt.setString(1, employee.getFname());
            pstmt.setString(2, employee.getMinit());
            pstmt.setString(3, employee.getLname());
            pstmt.setString(4, employee.getSsn());
            pstmt.setString(5, bdate);
            if (employee.getAddress() == null) {
                pstmt.setNull(6, Types.VARCHAR);
            } else {
                pstmt.setString(6, employee.getAddress());
            }

            pstmt.setString(7, employee.getSex());

            if (employee.getSalary() == null) {
                pstmt.setNull(8, Types.DOUBLE);
            } else {
                pstmt.setDouble(8, employee.getSalary());
            }

            if (employee.getSuperSsn() == null) {
                pstmt.setNull(9, Types.VARCHAR);
            } else {
                pstmt.setString(9, employee.getSuperSsn());
            }
            pstmt.setString(10, String.valueOf(employee.getDno()));

            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {

            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            e.printStackTrace();
            return false;
        } finally {
            // 사용한 자원 반환
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            }
        }
    }

    public static boolean isTargetRootEmployee(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean isRoot = false;
        try {
            conn = DatabaseUtils.connect();
            String sql = "SELECT * FROM EMPLOYEE WHERE Ssn=" + employee.getSsn();

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String SuperSsn = rs.getString("Super_ssn");
                if (SuperSsn == null) {
                    isRoot = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isRoot;
    }

    private static String getHeritorSsn(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String heritorSsn = "";
        try {
            conn = DatabaseUtils.connect();
            String sql = "SELECT * FROM EMPLOYEE WHERE Super_ssn=" + employee.getSsn();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            // 루트 직원을 직속으로 하는 부하가 있다면 삭제 가능
            if (rs.next()) {
                heritorSsn = rs.getString("Ssn");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return heritorSsn;
    }

    public static String deleteRootEmployee(Employee employee) {
        // 고려사항
        // 0. 삭제하려는 직원이 루트 직원이라면?
        // 1. 루트 직원의 직속 부하가 없는 경우 -> 삭제 불가
        // 2. 루트 직원의 직속 부하가 있는 경우 -> 직속 부하 중 한 명을 루트로 삼는다.
        //      이전 직속 부하들의 Super_ssn을 새로운 루트 직원의 Ssn으로 변경한다.
        //      새로운 루트 직원의 직속 상사는 Null로 변경한다.
        // 3. Department의 Mgr_ssn 필드 기본값을 새로운 루트 직원의 Ssn으로 변경한다.
        // 4. Department의 Mgr_ssn 중 기존 루트 직원의 Ssn을 가지는 필드 또한 새로운 루트 직원의 Ssn으로 변경한다.
        // 5. Works_on, Dependent를 모두 삭제한다.

        Connection conn = null;
        PreparedStatement pstmt = null;

        String originRootSsn = employee.getSsn();
        String heritorSsn = getHeritorSsn(employee);

        if (heritorSsn.isEmpty()) {
            return "";
        }

        try {
            conn = DatabaseUtils.connect();

            // 새로운 루트 직원의 직속 상사 Null로 변환
            String updateHeritorSupervisorNullSql = "UPDATE EMPLOYEE SET Super_ssn=NULL WHERE Ssn=" + heritorSsn;
            pstmt = conn.prepareStatement(updateHeritorSupervisorNullSql);
            pstmt.executeUpdate();

            // 기존 루트 직원의 부하들의 직속 상사를 새로운 루트 직원으로 변경
            String changePrevSupervisorToNewRoot = "UPDATE EMPLOYEE SET Super_ssn=" + heritorSsn + " WHERE Super_ssn=" + originRootSsn;
            pstmt = conn.prepareStatement(changePrevSupervisorToNewRoot);
            pstmt.executeUpdate();

            // 부서의 기본 매니저 Ssn을 새로운 루트 직원의 Ssn으로 변경
            String alterTableDefaultSql = "ALTER TABLE DEPARTMENT ALTER Mgr_ssn SET DEFAULT " + heritorSsn;
            pstmt = conn.prepareStatement(alterTableDefaultSql);
            pstmt.executeUpdate();

            // 기존 루트 직원이 매니저로 있던 부서의 매니저 변경
            String changeDepartmentMangerSql = "UPDATE DEPARTMENT SET Mgr_ssn=" + heritorSsn + " WHERE Mgr_ssn=" + originRootSsn;
            pstmt = conn.prepareStatement(changeDepartmentMangerSql);
            pstmt.executeUpdate();

            // 기존 루트 직원의 부양 가족 삭제
            deleteDependents(conn, employee);

            // 기존 루트 직원의 근무 기록 삭제
            deleteWorksOn(conn, employee);

            // 기존 루트 직원 레코드 삭제
            String deleteRootEmployeeSql = "DELETE FROM EMPLOYEE WHERE Ssn=" + originRootSsn;
            pstmt = conn.prepareStatement(deleteRootEmployeeSql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
           Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
        } finally {
            // 사용한 자원 반환
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            }
        }

        return heritorSsn;
    }

    private static void changeSuperviseSsn(Connection conn, Employee employee) {
        PreparedStatement pstmt = null;

        try {
            String ssnOfTargetEmployee = employee.getSsn();
            String superSsnOfTargetEmployee = employee.getSuperSsn();

            // String findSuperviseSql = "SELECT * FROM EMPLOYEE WHERE Super_ssn=" + ssnOfTargetEmployee;
            String updateSql = "UPDATE EMPLOYEE SET Super_Ssn=" + superSsnOfTargetEmployee + " WHERE Super_Ssn=" + ssnOfTargetEmployee;
            pstmt = conn.prepareStatement(updateSql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
        }
    }

     private static void updateManagerToDefault(Connection conn, Employee employee) {
        PreparedStatement pstmt = null;

        try {
            String sql = "UPDATE DEPARTMENT SET Mgr_Ssn=DEFAULT WHERE Mgr_Ssn=" + employee.getSsn();
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        } catch (SQLException e) {
           Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
        }
    }

    private static void deleteDependents(Connection conn, Employee employee) {
        PreparedStatement pstmt = null;

        try {
            String sql = "DELETE FROM DEPENDENT WHERE Essn=" + employee.getSsn();
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
        }
    }

    public static void deleteWorksOn(Connection conn, Employee employee) {
        PreparedStatement pstmt = null;

        try {
            String sql = "DELETE FROM WORKS_ON WHERE Essn=" + employee.getSsn();
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        } catch (SQLException e) {
          Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
        }
    }

    public static boolean deleteEmployee(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED");

            // 삭제하려는 직원이 다른이의 supervisor -> 해당 직원을 참조하던 모든 supervisee의 ssn을 삭제하려는 직원의 superssn으로
            changeSuperviseSsn(conn, employee);
            // 삭제하려는 직원이 만약 부서장이라면? -> 해당 부서의 부서장은 default로 변경
            updateManagerToDefault(conn, employee);
            // 삭제하려는 직원의 dependent 전부 삭제
            deleteDependents(conn, employee);
            // 삭제하려는 직원의 works_on 전부 삭제
            deleteWorksOn(conn, employee);

            String sql = "DELETE FROM EMPLOYEE WHERE Ssn=" + employee.getSsn();
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        } catch (SQLException e) {
           Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
        } finally {
            // 사용한 자원 반환
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return true;
    }

    public static boolean isDuplicatedSsn(String ssn) {
        boolean isDuplicated = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 직원이 이미 존재하는지 확인
        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED: FOR CHECK DUPLICATED EMPLOYEE.");
            String sql = "SELECT * FROM EMPLOYEE WHERE Ssn=" + ssn;
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            // 중복된 행이 존재하여 삽입 실패 처리
            if (rs.next()) {
                isDuplicated = true;
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
        } finally {
            // 사용한 자원 반환
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            }
        }

        return isDuplicated;

    }

    public static boolean updateEmployee(String ssn, Map<String, String> diff) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED");

            StringBuilder updateQuery = new StringBuilder("UPDATE EMPLOYEE SET ");
            int parameterIndex = 1;

            for (String column : diff.keySet()) {
                String value = diff.get(column);
                updateQuery.append(column).append(" = ?");

                if (parameterIndex < diff.size()) {
                    updateQuery.append(", ");
                }

                parameterIndex++;
            }

            updateQuery.append(" WHERE Ssn = ?");

            pstmt = conn.prepareStatement(updateQuery.toString());

            parameterIndex = 1;
            for (String column : diff.keySet()) {
                String value = diff.get(column);
                pstmt.setString(parameterIndex, value);
                parameterIndex++;
            }

            pstmt.setString(parameterIndex, ssn);

            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
                e.printStackTrace();
            }
        }
    }

    public static boolean editAllSelectedEmloyee(String columnName, Map<String, String> datas) {

        //columnName = 변경할 속성
        //Map<String, String> datas = <SSN, 일괄변경할값>에 대한 정보가 담긴 맵
        // datas에는 이미 String으로 연산이 수행된 후처리가 된 값들이 저장되어있음(
        // 다만 변경될 값이 null 일경우 SET NULL을 해야함
        // 또한 실제 필드의 도메인이 String이 아닐경우 parseOOOO등의 후처리 연산을 해서 pstmt.setOOOO 수행을 해야함)
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED.");

            // 시작
            conn.setAutoCommit(false);

            //맵을 순회합니다.
            for (Map.Entry<String, String> entry : datas.entrySet()) {
                String ssn = entry.getKey();
                String newValue = entry.getValue();

                String sql = "UPDATE EMPLOYEE SET " + columnName + " = ? WHERE Ssn = ?";
                pstmt = conn.prepareStatement(sql);

                // newValue가 =null -> NULL
                if (newValue == null) {
                    pstmt.setNull(1, Types.NULL);
                } else {
                    if ("Salary".equals(columnName)) {
                        double parsedValue = Double.parseDouble(newValue);
                        pstmt.setDouble(1, parsedValue);
                    } else {
                        // 기본적으로 String으로 처리
                        pstmt.setString(1, newValue);
                    }
                }

                pstmt.setString(2, ssn);

                pstmt.executeUpdate();
            }
            // 트랜잭션
            conn.commit();

            return true;
        } catch (SQLException e) {
            
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackException) {
                Notifications.getInstance().show(Notifications.Type.ERROR, rollbackException.toString());
            }

            Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // 자동 커밋을 다시 활성화
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.toString());
                return false;
            }
        }

    }

}
