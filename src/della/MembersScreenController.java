/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package della;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Amey
 */
public class MembersScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btAddToListMembers,btRemoveFromListMembers,btAddAffiliationMembers,btRemoveAffiliationMembers;
    @FXML
    private TextField tfNameMemberMembers;
    @FXML
    private ListView lvDisplayMembers,lvAvailableTeamsForMembers,lvCurrentTeamsForMembers;
    @FXML
    private Text txtAvailableteams,txtCurrentteams;
    Connection conn;
    DBConnection db = new DBConnection();
    String value,Teamtodelt;
    public Label NotConnected3,Connected3;
    InternetConnection ic = new InternetConnection();
    String Teamtoadd;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        if(ic.OnlineConnection()){
           Connected3.setText("ONLINE");
       }
       else{
           NotConnected3.setText("OFFLINE");
       }
      loadMembers();
      
    }  
    
    
    
    public void loadMembers(){
         tfNameMemberMembers.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
            if(t1.equals(""))
               btAddToListMembers.setDisable(true);
            else
               btAddToListMembers.setDisable(false);
         }
        });
         displayMembers();
         lvDisplayMembers.getSelectionModel().clearSelection();
         lvDisplayMembers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
         //System.out.println("clicked "+newValue);
             if(newValue != (null)){
             value =  newValue.toString();
             txtAvailableteams.setText("Available teams for "+newValue.toString());
             txtCurrentteams.setText("Current teams for "+newValue.toString());
             btRemoveFromListMembers.setDisable(false);
             try {
                 loadAvailableTeams(newValue.toString());
                 loadCurrentTeams(newValue.toString());
              
             } catch (ClassNotFoundException ex) {
                 Logger.getLogger(MembersScreenController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (SQLException ex) {
                 Logger.getLogger(MembersScreenController.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         });
         
//         listView.setOnMouseClicked(new EventHandler<MouseEvent>(){
// 
//          @Override
//          public void handle(MouseEvent arg0) {
//             
//              label.setText("Selected: " +
//                  listView.getSelectionModel().getSelectedItems());
//          }
// 
//      });
//         
         
     }
    
    private void loadAvailableTeams(String name) throws ClassNotFoundException, SQLException{
        
        conn=db.getConnection();
        
        PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from teams where name not in(select teams from assign where name=?)");
        pst.setString(1,name);
        ResultSet rs = pst.executeQuery();
        ArrayList<String> arr = new ArrayList<String>();
            while(rs.next()){
                arr.add(rs.getString(1));
            }
            ObservableList<String> data = FXCollections.observableArrayList(arr);
            lvAvailableTeamsForMembers.setItems(data);
            conn.close();
           
           enable();
    }
    
    private void loadCurrentTeams(String name) throws ClassNotFoundException, SQLException {
        conn=db.getConnection();
        //System.out.println("Loaing curnt tms");
        PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from teams where name in(select teams from assign where name=?)");
        pst.setString(1,name);
        ResultSet rs = pst.executeQuery();
        ArrayList<String> arr = new ArrayList<String>();
            while(rs.next()){
                arr.add(rs.getString(1));
            }
            ObservableList<String> data = FXCollections.observableArrayList(arr);
            lvCurrentTeamsForMembers.setItems(data);
            conn.close();
            //System.out.println(" Done Loaing curnt tms");
            lvCurrentTeamsForMembers.getSelectionModel().clearSelection();
            lvCurrentTeamsForMembers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
         //System.out.println("clicked --"+newValue);
             if(newValue != (null)){
                 Teamtodelt = newValue.toString();
                 //lvCurrentTeamsForMembers.getSelectionModel().clearSelection();
                 btRemoveAffiliationMembers.setDisable(false);
                 btAddAffiliationMembers.setDisable(true);
             }
             else{
                btRemoveAffiliationMembers.setDisable(true); 
                btAddAffiliationMembers.setDisable(true);
             }
            
            });
    }
    
    private void enable(){
         lvAvailableTeamsForMembers.getSelectionModel().clearSelection(); 
        lvAvailableTeamsForMembers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//             if (selectionModel.getSelectedIndices().contains(index)) {
//                        selectionModel.clearSelection(index);
//                    } else {
//                        selectionModel.select(index);
//                    }
             System.out.println("clicked "+newValue);
                
             if(newValue != (null)){
                 Teamtoadd = newValue.toString();
                 //lvAvailableTeamsForMembers.getSelectionModel().clearSelection();
                 btAddAffiliationMembers.setDisable(false);
                 btRemoveAffiliationMembers.setDisable(true);
             }
             else{
                 btAddAffiliationMembers.setDisable(true);
                 btRemoveAffiliationMembers.setDisable(true);
             }
            
            });
    }
    
    private void displayMembers() {
        try {
            conn= db.getConnection();
            if(conn != null){
                PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from members");
            ResultSet rs = pst.executeQuery();
            ArrayList<String> arr = new ArrayList<String>();
            while(rs.next()){
                arr.add(rs.getString(1));
            }
            ObservableList<String> data = FXCollections.observableArrayList(arr);
            lvDisplayMembers.setItems(data);
            conn.close();
            }
   
            
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
    }
    
    
    public void insertMember(ActionEvent event){
        String name = tfNameMemberMembers.getText();
        try {
            conn = db.getConnection();
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from members where name=?");
            pst.setString(1,name);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                //System.out.println("Duplicate");
                btAddToListMembers.setDisable(true);
                tfNameMemberMembers.clear();
            }
            else{
                pst =(PreparedStatement) conn.prepareStatement("insert into members (name) values (?)"); 
                pst.setString(1,name);
                pst.executeUpdate();
                tfNameMemberMembers.clear();
                loadMembers();
                //displayMembers();
                txtAvailableteams.setText("Available teams for "+name);
            txtCurrentteams.setText("Current teams for "+name);
                //System.out.println("Inserted");
            }
            conn.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
        
    }
    
    public void removeMember(){
        try {
            conn = db.getConnection();
           System.out.println(value);
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("delete from members where name=?");
            pst.setString(1,value);
            int rs = pst.executeUpdate();
            //System.out.println("Deleted");
            tfNameMemberMembers.setText(value);
            txtAvailableteams.setText("Available teams for ");
            txtCurrentteams.setText("Current teams for ");
            btRemoveFromListMembers.setDisable(true);
            loadMembers();
            conn.close();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
    }

    
    
    public void AddAffiliationMember(ActionEvent event){
       try{//Teamtoadd,value
           conn = db.getConnection();
           //System.out.println(Teamtoadd+"??????????????????????????"+value);
           //System.out.println(" AddAffiliationMember");
           PreparedStatement pst =(PreparedStatement) conn.prepareStatement("insert into assign values(?,?)");
           pst.setString(1, value);
           pst.setString(2,Teamtoadd);
           pst.executeUpdate();
           //System.out.println(" addedddddddddddddddddd");
           loadAvailableTeams(value);
           //System.out.println(" loded avl teams");
           loadCurrentTeams(value);
       }
       catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
    }
    @FXML private void RemoveAffiliationMember(ActionEvent event){
     
        try{//Teamtoadd,value
           conn = db.getConnection();
           //System.out.println(Teamtodelt+"??????????????????????????"+value);
           //System.out.println(" AddAffiliationMember");
           PreparedStatement pst =(PreparedStatement) conn.prepareStatement("delete from assign where name=? and teams=?");
           pst.setString(1, value);
           pst.setString(2,Teamtodelt);
           pst.executeUpdate();
           //System.out.println(" addedddddddddddddddddd");
           loadAvailableTeams(value);
           //System.out.println(" loded avl teams");
           loadCurrentTeams(value);
       }
       catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
    }
    
}