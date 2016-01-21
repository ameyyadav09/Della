/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package della;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Amey
 */
public class DBConnection {
    
   InternetConnection ic = new InternetConnection();
    public Connection getConnection() throws ClassNotFoundException, SQLException{
        if(ic.OnlineConnection()){
            System.out.println("Connecting to OnlineDB");
            Connection conn;
            //10.10.10.115:3306
            String link = "jdbc:mysql://localhost/"+"della"+"?user="+"root"+"&password="+"root";
            Class.forName("com.mysql.jdbc.Driver");        
            conn = (Connection) DriverManager.getConnection(link);
            return conn;
        }
        else{
//           System.out.println("Connecting to LOCALdb");
//            Connection conn;
//            String link = "jdbc:mysql://localhost/"+"della"+"?user="+"root"+"&password="+"root";
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = (Connection) DriverManager.getConnection(link);
            return null; 
        }
    }
}
