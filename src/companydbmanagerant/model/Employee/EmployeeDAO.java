package companydbmanagerant.model.Employee;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import companydbmanagerant.model.DatabaseUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PHC
 */
public class EmployeeDAO {


    public static List<String> findNotSubordinates(String query) {
        List<String> notSubordinates = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED");
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
            // 예외 처리

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
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED");
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
                String sex = rs.getString("Sex");
                double salary = rs.getDouble("Salary");
                String superSsn = rs.getString("Super_ssn");
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
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
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
                String sex = rs.getString("Sex");
                double salary = rs.getDouble("Salary");
                String superSsn = rs.getString("Super_ssn");
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
            }
        }

        return employees;
    }

    public static boolean insertEmployee(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED");
            String insertSql = "INSERT INTO EMPLOYEE VALUES (?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP())";
            pstmt = conn.prepareStatement(insertSql);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String bdate = dateFormat.format(employee.getBdate());

            String salaryString;
            if (employee.getSalary() == -1.0) {
                salaryString = null;
            } else {
                salaryString = String.valueOf(employee.getSalary());
            }
            pstmt.clearParameters();
            pstmt.setString(1, employee.getFname());
            pstmt.setString(2, employee.getMinit());
            pstmt.setString(3, employee.getLname());
            pstmt.setString(4, employee.getSsn());
            pstmt.setString(5, bdate);
            pstmt.setString(6, employee.getAddress());
            pstmt.setString(7, employee.getSex());
            pstmt.setString(8, salaryString);
            pstmt.setString(9, employee.getSuperSsn());
            pstmt.setString(10, String.valueOf(employee.getDno()));

            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static void changeSuperviseSsn(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED");
            String ssnOfTargetEmployee = employee.getSsn();
            String superSsnOfTargetEmployee = employee.getSuperSsn();

            // String findSuperviseSql = "SELECT * FROM EMPLOYEE WHERE Super_ssn=" + ssnOfTargetEmployee;
            String updateSql = "UPDATE EMPLOYEE SET Super_Ssn=" + superSsnOfTargetEmployee + " WHERE Super_Ssn=" + ssnOfTargetEmployee;
            pstmt = conn.prepareStatement(updateSql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
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
    }

    private static void updateManagerToDefault(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED");

            String sql = "UPDATE DEPARTMENT SET Mgr_Ssn=DEFAULT WHERE Mgr_Ssn=" + employee.getSsn();
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
    }

    private static void deleteDependents(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED");

            String sql = "DELETE FROM DEPENDENT WHERE Essn=" + employee.getSsn();
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
    }

    public static void deleteWorksOn(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED");

            String sql = "DELETE FROM WORKS_ON WHERE Essn=" + employee.getSsn();
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
    }

    public static boolean deleteEmployee(Employee employee) {
        // 고려사항
        // 1. 삭제하려는 직원이 만약 다른이의 supervisor라면? -> supervisee의 supervisor를 null로...?
        // 2. 삭제하려는 직원이 만약 부서장이라면? -> default로 변경
        // 3. 삭제하려는 직원의 dependent? -> 전원 삭제
        // 4. 삭제하려는 직원의 works_on? -> 전체 삭제

        // 삭제하려는 직원이 다른이의 supervisor -> 해당 직원을 참조하던 모든 supervisee의 ssn을 superssn이 null인 사람의 ssn으로
        changeSuperviseSsn(employee);
        // 삭제하려는 직원이 만약 부서장이라면? -> 해당 부서의 부서장은 default로 변경
        updateManagerToDefault(employee);
        // 삭제하려는 직원의 dependent 전부 삭제
        deleteDependents(employee);
        // 삭제하려는 직원의 works_on 전부 삭제
        deleteWorksOn(employee);

        // 해당 직원 삭제
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtils.connect();
            System.out.println("DB CONNECTED");

            String sql = "DELETE FROM EMPLOYEE WHERE Ssn=" + employee.getSsn();
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

}
