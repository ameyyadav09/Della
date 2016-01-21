/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package della;

import com.mysql.jdbc.Connection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Amey
 */
public class loginController implements Initializable {
    
    @FXML
    private TextField Username;
    @FXML
    
    private PasswordField Password;
      @FXML
    private Stage stage1 =new Stage();
    @FXML
    private SplitPane Splitpane;
    DBConnection db = new DBConnection();
    
    /*
    String url = "jdbc:mysql://localhost:3306/"; 
    String dbName = "demo” 
    String driver = "com.mysql.jdbc.Driver"; 
    String userName = "root"; String password = "mypasswd"; 
    try { Class.forName(driver).newInstance(); 
    Connection conn = DriverManager.getConnection(url+dbName,userName,password); 
    conn.close(); } 
    catch (Exception e) { e.printStackTrace(); } }
    

    */
    @FXML
    private void Signinbox(ActionEvent event) throws Exception {
        Connection conn = null;
        String user = Username.getText(),pasw = Password.getText();
//        String url = "jdbc:mysql://10.10.10.238:3306/"; 
//    String dbName = "della";
//    String driver = "com.mysql.jdbc.Driver"; 
//    String userName = "root"; String password = "root"; 
    
		try
		{
		//10.10.10.115:3306	
                    //String url = "jdbc:mysql://10.3.1.165:3306/"+"della"+"?user="+"root"+"&password="+"root";
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			//conn = (Connection) DriverManager.getConnection(url);
                    
//                    Class.forName(driver).newInstance(); 
//                    conn = (Connection) DriverManager.getConnection(url+dbName,userName,password); 
                        conn = db.getConnection();
                        if(conn == null){
                            //Connection conn;
                             String link = "jdbc:mysql://localhost/"+"della"+"?user="+"root"+"&password="+"root";
                            Class.forName("com.mysql.jdbc.Driver");
                            conn = (Connection) DriverManager.getConnection(link);
                            
                            
                        }
                        System.out.println("Connected....");
                        PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select username,password from logindetails where username=? and password=?"); 
                        pst.setString(1,user);  
                        pst.setString(2,pasw); 
                        System.out.println(user+" "+pasw);
                        ResultSet r1 = pst.executeQuery();
                        if(r1.next()){
                            System.out.println("Valid");
                            //Della.stage.close();
                            Splitpane= FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                            Scene scene = new Scene(Splitpane);
                            stage1.setScene(scene);
                            stage1.show();
//                            tabpane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//                                public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
//                                         // do something...
//                                    if(newValue == (Number)1){
//                                        //ActionItem
//                                        ActionItem actionitem = new ActionItem();
//                                        
//                                    }
//                                }
//                             }); 
                        }
                        else{
                            System.out.println("Not Valid");
                        }
          
		}
		catch(SQLException s)
		{
			System.out.println("SQLException: Unable to open");
			throw s;
		}
		catch(Exception e)
		{
			System.out.println("Exception: Unable to open");
			throw e;
		}
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
