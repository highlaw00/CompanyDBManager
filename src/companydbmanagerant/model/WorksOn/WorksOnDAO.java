package companydbmanagerant.model.WorksOn;

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
public class WorksOnDAO {

    public static List<WorksOn> loadDataFittered(String condition) {
        List<WorksOn> worksons = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옴
            System.out.println("DB CONNECTED WORKS ON");
            // 쿼리 준비
            String sql = "SELECT * FROM Works_on " + condition;
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String essn = rs.getString("Essn");
                int pno = rs.getInt("Pno");
                double hours = rs.getDouble("Hours");
                // Employee 객체 생성 및 값 설정
                WorksOn workson = new WorksOn(essn,pno,hours);

                // 리스트에 Employee 객체 추가
                worksons.add(workson);
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

        return worksons;
    }

    public static List<WorksOn> loadData() {
        List<WorksOn> worksons = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옴.
            System.out.println("DB CONNECTED WORKS ON");
            // 쿼리 준비
            String sql = "SELECT * FROM Works_on ";
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String essn = rs.getString("Essn");
                int pno = rs.getInt("Pno");
                double hours = rs.getDouble("Hours");
                // Employee 객체 생성 및 값 설정
                WorksOn workson = new WorksOn(essn,pno,hours);

                // 리스트에 Employee 객체 추가
                worksons.add(workson);
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

        return worksons;
    }

}
