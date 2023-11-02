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
import java.util.ArrayList;
import java.util.List;

/**
 *
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
            System.out.println("DB CONNECTED E3");
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
            System.err.print(e);

        } catch (Exception e) {
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
            }
        }

        return employees;
    }

}
