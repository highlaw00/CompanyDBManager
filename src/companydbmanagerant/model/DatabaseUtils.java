/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package companydbmanagerant.model;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import raven.toast.Notifications;

/**
 *
 * @author PHC
 */
public class DatabaseUtils {
    private static String URL = "root";
    private static String USER = "root";
    private static String PASS = "qwer123";
    private static String DBNAME = "companydb";
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ DBNAME +"?zeroDateTimeBehavior=CONVERT_TO_NULL", USER, PASS);
        } catch (Exception e) {
             Notifications.getInstance().show(Notifications.Type.ERROR,e.getMessage());
            //JOptionPane.showMessageDialog(null, e);
        }
        return conn;  // 이제 연결 객체를 반환합니다.
    }
    
    public static boolean try_login(String user,char[] pass,String dbname,String url) {
        Connection conn = null;
        String temp = String.valueOf(pass);
        try {
            
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+ url + "/"+ dbname +"?zeroDateTimeBehavior=CONVERT_TO_NULL", user, temp);
            USER = user;
            PASS = temp;
            DBNAME = dbname;
            URL = url;
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
            Notifications.getInstance().show(Notifications.Type.ERROR,e.getMessage());
            System.err.println(e);

            return false;
        }
        
        return true;  
    }
}
