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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Amey
 */
public class ActionItemScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField tfSelActItemsNameActionItems,tfDueActionItems;
    @FXML
    private TextArea taDescriptionActionItems,taResolutionActionItems;
    @FXML
    private ComboBox <String> cbActionItemsActionItems,cbActionItemsDatesActionItems,cbAssignMemActionItems,cbAssignTeamActionItems; 
    Connection conn = null;
    DBConnection db = new DBConnection();
     public Label NotConnected2,Connected2,lbCreationDate;
    InternetConnection ic = new InternetConnection();
    @FXML
    private Button btDeleteActionItemActionItems,btCreateActionItemActionItems,btClearFormActionItems,btUpdataActionItemActionItems;
    ObservableList<String> st = FXCollections.observableArrayList("open","close");
    
    String status;
    
    public void handleSelectionStatus(ActionEvent event){
        status = cbActionItemsDatesActionItems.getValue();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(ic.OnlineConnection()){
           Connected2.setText("ONLINE");
       }
       else{
           NotConnected2.setText("OFFLINE");
       }
        disableButtons();
        loadData();
        cbActionItemsDatesActionItems.setItems(st);
        
    }
    
    @FXML
    public void disableButtons(){
       if(NotConnected2.getText() == "OFFLINE"){
           btDeleteActionItemActionItems.setDisable(true);
           btCreateActionItemActionItems.setDisable(true);
           btClearFormActionItems.setDisable(true);
           btUpdataActionItemActionItems.setDisable(true);
       }
   }
    
    @FXML
    public void loadData(){
        ArrayList<String> arr = new ArrayList<String>();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:MM:SS");
        Calendar date = Calendar.getInstance();
        lbCreationDate.setText(df.format(date.getTime()));
        try{
            conn=(Connection) db.getConnection();
            if(conn != null){
                File f = new File("Action.txt");
                PrintWriter pw = new PrintWriter(f);
                PreparedStatement ps = (PreparedStatement) conn.prepareStatement("select * from actionitems");
                ResultSet rx = ps.executeQuery();
                while(rx.next()) {
                    pw.println(rx.getString(1) + "  " + rx.getString(2) + "  " + rx.getString(3) + "  " + rx.getString(4) + "  " + rx.getString(5) + "  " + rx.getString(6) + rx.getString(7) );
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
                cbActionItemsActionItems.setItems(data); 
                conn.close();
            }
            else{
                arr = new ArrayList();
                Scanner scan = new Scanner(new File("Action.txt"));
                while(scan.hasNextLine()) {
                    String a = scan.nextLine();
                    String[] b = a.split("  ");
                    arr.add(b[0]);
                }
                ObservableList<String> data = FXCollections.observableArrayList(arr);
                cbActionItemsActionItems.setItems(data); 
            } 
        }
        catch(SQLException s){
            System.out.println("SQLException: Unable to open");
        }
	catch(Exception e){
            System.out.println("Exception: Unable to open");
        }
    }
 
    
    
    @FXML
    public void ActionItemsComboBox(ActionEvent event)throws Exception{
        if(ic.OnlineConnection()){
             NotConnected2.setText("");
             Connected2.setText("ONLINE");
        }
        else{
           Connected2.setText("");
           NotConnected2.setText("OFFLINE");
           disableButtons();
        }
        //setStatus();
        String name = (String) cbActionItemsActionItems.getValue();
        System.out.println(name);
        try{
            conn=db.getConnection();
            if(conn != null ){
                System.out.println("Connected....");
                PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select * from actionitems where name=?");
                pst.setString(1,name);
                ResultSet rs = pst.executeQuery();
           
                    while(rs.next()){
                        System.out.println(rs.getString("usable"));
                        if(rs.getString("usable").equals("yes")){
                            tfSelActItemsNameActionItems.setText(rs.getString("name"));
                            taDescriptionActionItems.setText(rs.getString("description"));
                            taResolutionActionItems.setText(rs.getString("resolution"));
                            tfDueActionItems.setText(rs.getString("duedate"));
                            lbCreationDate.setText(rs.getString("creation"));
                            cbActionItemsDatesActionItems.setValue(rs.getString("status")); 
                            pst =(PreparedStatement) conn.prepareStatement("update actionitems set usable='no' where name=?");
                            pst.setString(1,name);
                            pst.executeUpdate();
                             
                        }
                        else{
                            Stage dialog = new Stage();
                            dialog.initStyle(StageStyle.UTILITY);
                            Scene scene = new Scene(new Group(new Text(25, 25, "ActionItem can't be used")),270,60);
                            dialog.setScene(scene);
                            dialog.show();   
                            tfSelActItemsNameActionItems.setText(null);
                            taDescriptionActionItems.setText(null);
                            taResolutionActionItems.setText(null);
                            tfDueActionItems.setText(null);
                            lbCreationDate.setText(null);
                            cbActionItemsDatesActionItems.setValue(null); 
                            cbActionItemsActionItems.setValue(null);
                        }
                    }
            }
            else{
                Scanner scan = new Scanner(new File("Action.txt"));
                while(scan.hasNextLine()) {
                    String a = scan.nextLine();
                    String[] b = a.split("  ");
//                  arr.add(b[0]);
                    if(name.equals(b[0])){
                        tfSelActItemsNameActionItems.setText(b[0]);
                        taDescriptionActionItems.setText(b[1]);
                        taResolutionActionItems.setText(b[2]);
                        lbCreationDate.setText(b[4]);
                        cbActionItemsDatesActionItems.setValue(b[5]);
                    }
                }
            }
        }
        catch(SQLException s){
            System.out.println("SQLException: Unable to open");
        }
        
	catch(Exception e){
            System.out.println("Exception: Unable to open");
        }
    }
    
    
    
    @FXML
    public void createActionItem(ActionEvent event) throws Exception{
//        if(ic.OnlineConnection()){
//             NotConnected2.setText("");
//             Connected2.setText("ONLINE");
//        }
//        else{
//           NotConnected2.setText("OFFLINE");
//           Connected2.setText("");
//        }
        String name=tfSelActItemsNameActionItems.getText(),desc=taDescriptionActionItems.getText(),res=taResolutionActionItems.getText();
        String duedate = tfDueActionItems.getText();
        String status = (String) cbActionItemsDatesActionItems.getValue(),usable = "yes";
  
        String verify="hi ",check=verify+name;
        
        if(verify.equals(check)){
            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UTILITY);
            Scene scene = new Scene(new Group(new Text(25, 25, "ActionItem name must not be empty")),270,60);
            dialog.setScene(scene);
            dialog.show();
        }
        else{
            try{
                conn=db.getConnection();
                if(conn!=null){
                    System.out.println("Connected....");
                    PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from actionitems where name=?");
                    pst.setString(1,name);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){
                        Stage dialog = new Stage();
                        dialog.initStyle(StageStyle.UTILITY);
                        Scene scene = new Scene(new Group(new Text(25, 25, "This ActionItem already exists")),270,60);
                        dialog.setScene(scene);
                        dialog.show();
                    }
                    else{
                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:MM:SS");
                        Calendar date = Calendar.getInstance();
                        lbCreationDate.setText(df.format(date.getTime()));
                        String creation = df.format(date.getTime());
                        pst =(PreparedStatement) conn.prepareStatement("insert into actionitems (name,description,resolution,duedate,creation,status,usable) values (?,?,?,?,?,?)"); 
                        pst.setString(1,name);  
                        pst.setString(2,desc); 
                        pst.setString(3,res);
                        pst.setString(4,duedate);
                        pst.setString(5,creation);
                        pst.setString(6,status);
                        pst.setString(7,usable);
                        pst.executeUpdate();
                        loadData();
                        tfSelActItemsNameActionItems.setText(name);
                        taDescriptionActionItems.setText(desc);
                        taResolutionActionItems.setText(res);
                        tfDueActionItems.setText(duedate);
                        //lbCreationDate.setText(Creation);
                        cbActionItemsDatesActionItems.setValue(status); 
                        cbActionItemsActionItems.setValue(name);
                    }
                } 
                else{
                    disableButtons();
                    Scanner scan = new Scanner(new File("Action.txt"));
                    String s = "";
                    String[] input = new String[8];
                    while(!s.equals(s) && scan.hasNextLine()){
                        String in = scan.nextLine();
                        input = in.split("  ");
                        s = input[0];
                    }
                    tfSelActItemsNameActionItems.setText(input[0]);
                    taDescriptionActionItems.setText(input[1]);
                    taResolutionActionItems.setText(input[2]);
                    tfDueActionItems.setText(input[3]);
                    lbCreationDate.setText(input[4]);
                    cbActionItemsDatesActionItems.setValue(input[5]);
                    cbActionItemsActionItems.setValue(input[6]);
                }    
            }
            catch(SQLException s){
                System.out.println("SQLException: Unable to open");
                throw s;
            }

            catch(Exception e){
                System.out.println("Exception: Unable to open");
                throw e;
            }
        }
    }
    
    @FXML
    public void clearForm(ActionEvent event)throws Exception{
        tfSelActItemsNameActionItems.setText(null);
        taDescriptionActionItems.setText(null);
        taResolutionActionItems.setText(null);
        tfDueActionItems.setText(null);
        lbCreationDate.setText(null);
        cbActionItemsDatesActionItems.setValue(null); 
        cbActionItemsActionItems.setValue(null);
    }
    
    @FXML
    public void DeleteActionItem(ActionEvent event) throws SQLException, Exception{
        String name = cbActionItemsActionItems.getValue();
        if(ic.OnlineConnection()){
           NotConnected2.setText("");
             Connected2.setText("ONLINE");
       }
       else{
           NotConnected2.setText("OFFLINE");
           Connected2.setText("");
       }
        String verify="hi ",check=verify+name;
       
       if(verify.equals(check)){
           Stage dialog = new Stage();
           dialog.initStyle(StageStyle.UTILITY);
           Scene scene = new Scene(new Group(new Text(25, 25, "Select an ActionItem")),270,60);
           dialog.setScene(scene);
           dialog.show();
       }
       else{
           try{
            conn=db.getConnection();
            System.out.println("Connected....");
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("delete from actionitems where name=?"); 
            pst.setString(1,name); 
            pst.executeUpdate();
            loadData();
            tfSelActItemsNameActionItems.setText(null);
            taDescriptionActionItems.setText(null);
            taResolutionActionItems.setText(null);
            tfDueActionItems.setText(null);
            lbCreationDate.setText(null);
            cbActionItemsDatesActionItems.setValue(null); 
            cbActionItemsActionItems.setValue(null);
          }
            catch(SQLException s){
                System.out.println("SQLException: Unable to open");
                throw s;
            }

            catch(Exception e){
                System.out.println("Exception: Unable to open");
                throw e;
            }
       }
    }
    
    
    @FXML
    public void UpdateActionItem(ActionEvent event) throws SQLException, Exception{
//        if(ic.OnlineConnection()){
//           NotConnected2.setText("");
//             Connected2.setText("ONLINE");
//        }
//        else{
//           NotConnected2.setText("OFFLINE");
//           Connected2.setText("");
//        }
        String name1 = tfSelActItemsNameActionItems.getText();
        String desc = taDescriptionActionItems.getText();
        String resc = taResolutionActionItems.getText();
        String duedate = tfDueActionItems.getText();
        String status = (String) cbActionItemsDatesActionItems.getValue();
        String name = cbActionItemsActionItems.getValue();
        
        //Connection conn = null;
        
        String verify="hi ",check=verify+name1;
        
       if(verify.equals(check)){
           Stage dialog = new Stage();
           dialog.initStyle(StageStyle.UTILITY);
           Scene scene = new Scene(new Group(new Text(25, 25, "Select an ActionItem")),270,60);
           dialog.setScene(scene);
           dialog.show();
       }
       else{
           try{
            conn=db.getConnection();
            System.out.println("Connected....");
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:MM:SS");
            Calendar date = Calendar.getInstance();
            lbCreationDate.setText(df.format(date.getTime()));
            String creation = df.format(date.getTime());
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("update actionitems set name=?,description=?,resolution=?,duedate=?,creation=?,status=? where name=?"); 
            pst.setString(1,name1); 
            pst.setString(2,desc); 
            pst.setString(3,resc); 
            pst.setString(4,duedate);
            pst.setString(5,creation);
            pst.setString(6,status);
            pst.setString(7,name); 
            pst.executeUpdate();
            loadData();
//            tfSelActItemsNameActionItems.setText(name1);
//            taDescriptionActionItems.setText(desc);
//            taResolutionActionItems.setText(resc);
//            tfDueActionItems.setText(duedate);
//            cbActionItemsDatesActionItems.setValue(status); 
//            cbActionItemsActionItems.setValue(name1);
            tfSelActItemsNameActionItems.setText(null);
            taDescriptionActionItems.setText(null);
            taResolutionActionItems.setText(null);
            tfDueActionItems.setText(null);
            lbCreationDate.setText(null);
            cbActionItemsDatesActionItems.setValue(null); 
//            cbActionItemsActionItems.setValue(null);
            setStatus(name1);
        }
        catch(SQLException s){
            System.out.println("SQLException: Unable to open");
            throw s;
        }

        catch(ClassNotFoundException e){
            System.out.println("Exception: Unable to open");
            throw e;
        }
       }
    }

    private void setStatus(String name1) throws ClassNotFoundException {
        try {
            conn=db.getConnection();
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("update actionitems set usable='yes' where name=?");
            pst.setString(1,name1);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ActionItemScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void dialog(){
       Stage dialog = new Stage();
           dialog.initStyle(StageStyle.UTILITY);
           Scene scene = new Scene(new Group(new Text(25, 25, "Select an ActionItem"),new Button("ok")),270,60);
           dialog.setScene(scene);
           dialog.show();
           
    }
}
