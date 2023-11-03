/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.Department;

import companydbmanagerant.model.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PHC
 */
public class DepartmentDAO {

    public static List<String> loadDepartmentsList() {
        List<String> departments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED 1");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT Dname FROM DEPARTMENT";
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String dname = rs.getString("Dname");

                // 리스트에 Employee 객체 추가
                departments.add(dname);
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

        return departments;
    }

    public static List<Department> loadDataFittered(String condition) {
        List<Department> departments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED 0");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT * FROM DEPARTMENT " + condition;
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String dname = rs.getString("Dname");
                String dnumber = rs.getString("Dnumber");
                String mgr_ssn = rs.getString("Mgr_ssn");
                String mgr_start_date = rs.getString("Mgr_start_date");

                // Employee 객체 생성 및 값 설정
                Department department = new Department(dname, dnumber, mgr_ssn, mgr_start_date);

                // 리스트에 Employee 객체 추가
                departments.add(department);
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

        return departments;
    }

    public static List<Department> loadData() {
        List<Department> departments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED 1");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT * FROM DEPARTMENT";
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String dname = rs.getString("Dname");
                String dnumber = rs.getString("Dnumber");
                String mgr_ssn = rs.getString("Mgr_ssn");
                String mgr_start_date = rs.getString("Mgr_start_date");

                // Employee 객체 생성 및 값 설정
                Department department = new Department(dname, dnumber, mgr_ssn, mgr_start_date);

                // 리스트에 Employee 객체 추가
                departments.add(department);
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

        return departments;
    }
}
