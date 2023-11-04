/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.Project;

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
public class ProjectDAO {

    public static List<Project> loadDataFittered(String condition) {
        List<Project> projects = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT * FROM project " + condition;
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {

                String pname = rs.getString("Pname");
                int pnumber = rs.getInt("Pnumber");
                String plocation = rs.getString("Plocation");
                int dnum = rs.getInt("Dnum");
                // Employee 객체 생성 및 값 설정
                Project project = new Project(pname, pnumber, plocation, dnum);


                // 리스트에 Employee 객체 추가
                projects.add(project);
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

        return projects;
    }

    public static List<Project> loadData() {
        List<Project> projects = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옵니다.
            System.out.println("DB CONNECTED");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT * FROM project ";
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {

                String pname = rs.getString("Pname");
                int pnumber = rs.getInt("Pnumber");
                String plocation = rs.getString("Plocation");
                int dnum = rs.getInt("Dnum");
                // Employee 객체 생성 및 값 설정
                Project project = new Project(pname, pnumber, plocation, dnum);

                // 리스트에 Employee 객체 추가
                projects.add(project);
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

        return projects;
    }
}
