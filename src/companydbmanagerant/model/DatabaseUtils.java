/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import raven.toast.Notifications;

/**
 *
 * @author PHC
 */
public class DatabaseUtils {
    private static String URL = "localhost:3306";
    private static String USER = "root";
    private static String PASS = "qwer123";
    private static String DBNAME = "companydb";
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+ URL + "/"+ DBNAME +"?zeroDateTimeBehavior=CONVERT_TO_NULL", USER, PASS);
        } catch (Exception e) {
             Notifications.getInstance().show(Notifications.Type.ERROR,e.getMessage());
            //JOptionPane.showMessageDialog(null, e);
        }
        return conn;  // 이제 연결 객체를 반환.
    }
    
    public static boolean try_login(LoginFormDataDTO logindata) {

        
        Connection conn = null;
        String temp = String.valueOf(logindata.getPassword());
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+ logindata.getUrl() + "/"+ logindata.getDbName() +"?zeroDateTimeBehavior=CONVERT_TO_NULL", logindata.getUserId(), temp);
            USER = logindata.getUserId();
            PASS = temp;
            DBNAME = logindata.getDbName();
            URL = logindata.getUrl();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
            Notifications.getInstance().show(Notifications.Type.ERROR,e.getMessage());
            System.err.println(e);

            return false;
        }  finally {
            // 사용한 자원 반환
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        
        return true;  
    }
}
