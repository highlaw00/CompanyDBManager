package companydbmanagerant.model.DeptLocations;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

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
public class DeptLocationsDAO {

    public static List<DeptLocations> loadDataFittered(String condition) {
        List<DeptLocations> deptlocationss = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT * FROM dept_locations " + condition;
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {
                 int dnumber = rs.getInt("Dnumber");
                String dlocation = rs.getString("Dlocation");
                // Employee 객체 생성 및 값 설정
                DeptLocations deptlocations = new DeptLocations(dnumber,dlocation);

                // 리스트에 Employee 객체 추가
                deptlocationss.add(deptlocations);
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

        return deptlocationss;
    }
                                      
    public static List<DeptLocations> loadData() {
        List<DeptLocations> deptlocationss = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT * FROM dept_locations ";
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {

                int dnumber = rs.getInt("Dnumber");
                String dlocation = rs.getString("Dlocation");

                // Employee 객체 생성 및 값 설정
                DeptLocations deptlocations = new DeptLocations(dnumber,dlocation);

                // 리스트에 Employee 객체 추가
                deptlocationss.add(deptlocations);
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

        return deptlocationss;
    }

}
