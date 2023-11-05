/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model.Dependent;

/**
 *
 * @author PHC
 */


import companydbmanagerant.model.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DependentDAO {
     public static List<Dependent> loadDataFittered(String condition) {
        List<Dependent> dependents = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옴.
            System.out.println("DB CONNECTED");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT * FROM DEPENDENT " + condition;
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String essn = rs.getString("Essn");
                String dependentname = rs.getString("Dependent_name");
                String sex = rs.getString("Sex");
                java.sql.Date bdate = rs.getDate("Bdate");
                String relationship = rs.getString("Relationship");               
                
                // Dependent 객체 생성 및 값 설정
                Dependent dependent = new Dependent(essn, dependentname, sex, bdate, relationship);

                // 리스트에 Dependent 객체 추가
                dependents.add(dependent);
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

        return dependents;
    }

    public static List<Dependent> loadData() {
        List<Dependent> dependents = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtils.connect();  // 데이터베이스 연결을 가져옴
            System.out.println("DB CONNECTED");
            // 쿼리 준비. 여기서는 모든 직원을 선택합니다.
            String sql = "SELECT * FROM DEPENDENT ";
            pstmt = conn.prepareStatement(sql);

            // 쿼리 실행 및 결과 처리
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String essn = rs.getString("Essn");
                String dependentname = rs.getString("Dependent_name");
                String sex = rs.getString("Sex");
                java.sql.Date bdate = rs.getDate("Bdate");
                String relationship = rs.getString("Relationship");               
                
                // Dependent 객체 생성 및 값 설정
                Dependent dependent = new Dependent(essn, dependentname, sex, bdate, relationship);
                // 리스트에 Dependent 객체 추가
                dependents.add(dependent);
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

        return dependents;
    }
}
