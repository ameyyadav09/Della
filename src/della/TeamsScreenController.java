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

public class TeamsScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    DBConnection db = new DBConnection();
    Connection conn;
    @FXML
    private TextField tfTeamName;
    @FXML
    private Button btAddToListTeams,btRemoveFromListTeams,btRemoveAssociationTeams,btAddAssociationTeams;
    @FXML
    private ListView lvTeamsByDellaTeams,lvAvailableMembersTeams,lvCurrentMembersForTeams;
    @FXML
    private Text txtAvailablemembers,txtCurrentmembers;
    String value,Membertoadd,Membertodelt;
     public Label NotConnected4,Connected4;
    InternetConnection ic = new InternetConnection();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(ic.OnlineConnection()){
           Connected4.setText("ONLINE");
       }
       else{
           NotConnected4.setText("OFFLINE");
       }
      loadTeams();
      
    }    

    private void loadTeams() {
        tfTeamName.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
            if(t1.equals(""))
               btAddToListTeams.setDisable(true);
            else
               btAddToListTeams.setDisable(false);
         }
        });
        displayTeams();
        lvTeamsByDellaTeams.getSelectionModel().clearSelection();
        lvTeamsByDellaTeams.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
            value=newValue.toString();
            txtAvailablemembers.setText("Available members for team "+newValue.toString());
            txtCurrentmembers.setText("Current members for team "+newValue.toString());
            btRemoveFromListTeams.setDisable(false);
                try {
                    loadAvailableMembers(newValue.toString());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(TeamsScreenController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(TeamsScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    loadCurrentMembers(newValue.toString());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(TeamsScreenController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(TeamsScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
         });
    }

    private void displayTeams() {
        try {
            conn= db.getConnection();
            if(conn != null){
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from teams");
            ResultSet rs = pst.executeQuery();
            ArrayList<String> arr = new ArrayList<String>();
            while(rs.next()){
                arr.add(rs.getString(1));
            }
            ObservableList<String> data = FXCollections.observableArrayList(arr);
            System.out.println(data);
            lvTeamsByDellaTeams.setItems(data);
            conn.close();
        }
            
            
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
    }
    
    public void removeTeam(){
        try {
            System.out.println(value);
            conn = db.getConnection();
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("delete from teams where name=?");
            pst.setString(1,value);
            int rs = pst.executeUpdate();
            loadTeams();
            tfTeamName.setText(value);
            txtAvailablemembers.setText("Available teams for ");
            txtCurrentmembers.setText("Current teams for ");
            btRemoveFromListTeams.setDisable(true);
            conn.close();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
    }
    
    public void insertTeam(ActionEvent event){
        String name = tfTeamName.getText();
        try {
            conn = db.getConnection();
           // System.out.println(name);
            PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from teams where name=?");
            pst.setString(1,name);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                btAddToListTeams.setDisable(true);
                tfTeamName.clear();
            }
            else{
                pst =(PreparedStatement) conn.prepareStatement("insert into teams (name) values (?)"); 
                pst.setString(1,name);
                pst.executeUpdate();
                tfTeamName.clear();
                loadTeams();
                txtAvailablemembers.setText("Available teams for "+name);
                txtCurrentmembers.setText("Current teams for "+name);
            }
            conn.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
        
    }
    
    
    public void RemoveAssociationTeams(ActionEvent event){
        
        try{//Teamtoadd,value
           conn = db.getConnection();
           //System.out.println(Teamtodelt+"??????????????????????????"+value);
           //System.out.println(" AddAffiliationMember");
           PreparedStatement pst =(PreparedStatement) conn.prepareStatement("delete from assign where name=? and teams=?");
           pst.setString(1, Membertodelt);
           pst.setString(2,value);
           pst.executeUpdate();
           //System.out.println(" addedddddddddddddddddd");
           loadAvailableMembers(value);
           //System.out.println(" loded avl teams");
           loadCurrentMembers(value);
       }
       catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
    }
    
    public void AddAssociationTeams(ActionEvent event){
        try{//Teamtoadd,value
           conn = db.getConnection();
           //System.out.println(Teamtoadd+"??????????????????????????"+value);
           //System.out.println(" AddAffiliationMember");
           PreparedStatement pst =(PreparedStatement) conn.prepareStatement("insert into assign values(?,?)");
           pst.setString(1, Membertoadd);
           pst.setString(2,value);
           pst.executeUpdate();
           //System.out.println(" addedddddddddddddddddd");
           loadAvailableMembers(value);
           //System.out.println(" loded avl teams");
           loadCurrentMembers(value);
       }
       catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception");
        } catch (SQLException ex) {
            System.out.println("SQLException");
        }
        
    }

    private void loadAvailableMembers(String toString) throws ClassNotFoundException, SQLException {
        conn=db.getConnection();
        
        PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from members where name not in(select name from assign where teams=?)");
        pst.setString(1,value);
        ResultSet rs = pst.executeQuery();
        ArrayList<String> arr = new ArrayList<String>();
            while(rs.next()){
                arr.add(rs.getString(1));
            }
            ObservableList<String> data = FXCollections.observableArrayList(arr);
            lvAvailableMembersTeams.setItems(data);
            conn.close();
           
           lvAvailableMembersTeams.getSelectionModel().clearSelection(); 
        lvAvailableMembersTeams.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//             if (selectionModel.getSelectedIndices().contains(index)) {
//                        selectionModel.clearSelection(index);
//                    } else {
//                        selectionModel.select(index);
//                    }
             System.out.println("clicked "+newValue);
                
             if(newValue != (null)){
                 Membertoadd = newValue.toString();
                 //lvAvailableTeamsForMembers.getSelectionModel().clearSelection();
                 btAddAssociationTeams.setDisable(false);
                 btRemoveAssociationTeams.setDisable(true);
             }
             else{
                 btAddAssociationTeams.setDisable(true);
                 btRemoveAssociationTeams.setDisable(true);
             }
            
            });
    }

    private void loadCurrentMembers(String toString) throws ClassNotFoundException, SQLException {
        conn=db.getConnection();
        //System.out.println("Loaing curnt tms");
        PreparedStatement pst =(PreparedStatement) conn.prepareStatement("select name from members where name in(select name from assign where teams=?)");
        pst.setString(1,value);
        ResultSet rs = pst.executeQuery();
        ArrayList<String> arr = new ArrayList<String>();
            while(rs.next()){
                arr.add(rs.getString(1));
            }
            ObservableList<String> data = FXCollections.observableArrayList(arr);
            lvCurrentMembersForTeams.setItems(data);
            conn.close();
            //System.out.println(" Done Loaing curnt tms");
            lvCurrentMembersForTeams.getSelectionModel().clearSelection();
            lvCurrentMembersForTeams.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
         //System.out.println("clicked --"+newValue);
             if(newValue != (null)){
                 Membertodelt = newValue.toString();
                 //lvCurrentTeamsForMembers.getSelectionModel().clearSelection();
                 btRemoveAssociationTeams.setDisable(false);
                 btAddAssociationTeams.setDisable(true);
             }
             else{
                btRemoveAssociationTeams.setDisable(true); 
                btAddAssociationTeams.setDisable(true);
             }
            
            });
    }
}
