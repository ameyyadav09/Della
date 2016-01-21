/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package della;

import com.mysql.jdbc.Connection;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Amey
 */
public class ConsoleScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ComboBox cbdiaplayActionItems;
    @FXML
    private TextField tfNameConsole;
    Connection conn = null;
    DBConnection db = new DBConnection();
    @FXML
    private TextArea taDescriptionConsole,taResolutionConsole;
     public Label NotConnected,Connected;
    InternetConnection ic = new InternetConnection();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(ic.OnlineConnection()){
           Connected.setText("ONLINE");
       }
       else{
           NotConnected.setText("OFFLINE");
       }
        loadData();      
        //String name=tfSelActItemsNameActionItems.getText(),desc=taDescriptionActionItems.getText(),res=taResolutionActionItems.getText();
    }
    public void loadData(){
        ArrayList<String> arr = new ArrayList<String>();
        try{
        
            conn=db.getConnection();
            if(conn != null){
               File f = new File("Console.txt");
            PrintWriter pw = new PrintWriter(f);
            PreparedStatement ps = (PreparedStatement) conn.prepareStatement("select * from actionitems");
            ResultSet rx = ps.executeQuery();
            while(rx.next()) {
                pw.println(rx.getString(1) + "  " + rx.getString(2) + "  " + rx.getString(3) + "  " + rx.getString(4) + "  " + rx.getString(5) + "  " + rx.getString(6) );
            }
            pw.close();
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from actionitems"); 
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                arr.add(rs.getString(1));
            }
            System.out.println(arr);
            ObservableList<String> data = FXCollections.observableArrayList(arr);
            System.out.println(data);                        
            cbdiaplayActionItems.setItems(data); 
            }
            else{
               arr = new ArrayList();
                Scanner scan = new Scanner(new File("Console.txt"));
                while(scan.hasNextLine()) {
                    String a = scan.nextLine();
                    String[] b = a.split("  ");
                    arr.add(b[0]);
                }
                ObservableList<String> data = FXCollections.observableArrayList(arr);
                cbdiaplayActionItems.setItems(data); 
            }
        }
        catch(SQLException s)
		{
			System.out.println("SQLException: Unable to open");
            
		}
		catch(Exception e)
		{
			System.out.println("Exception: Unable to open");
            
		}
    }
    
    @FXML
    public void SelectCombox(ActionEvent event){
        String str = (String) cbdiaplayActionItems.getValue();
        System.out.println(str);
        try{
            conn=db.getConnection();
            if(conn != null){
                //System.out.println("Not Connected to internet");
                System.out.println("Connected....");
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name,description,resolution from actionitems where name=?");
            pst.setString(1,str);
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                tfNameConsole.setText(rs.getString("name"));
                taDescriptionConsole.setText(rs.getString("description"));
                taResolutionConsole.setText(rs.getString("resolution"));
            }   
                
            }
            else{
                Scanner scan = new Scanner(new File("Console.txt"));
                while(scan.hasNextLine()) {
                    String a = scan.nextLine();
                    String[] b = a.split("  ");
//                    arr.add(b[0]);
                    if(str.equals(b[0])){
                        tfNameConsole.setText(b[0]);
                        taDescriptionConsole.setText(b[1]);
                        taResolutionConsole.setText(b[2]);
                        //lbCreationDate.setText(b[4]);
                        //cbActionItemsDatesActionItems.setValue(b[5]);
                    }
                }
            }
        }
        catch(SQLException s){
            System.out.println("SQLException: Unable to open");
	}
	catch(Exception e)
	{
            System.out.println("Exception: Unable to open");           
	}
    }
}
