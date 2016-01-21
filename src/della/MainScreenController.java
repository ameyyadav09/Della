/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package della;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Amey
 */
public class MainScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane actionitempane,consolepane,Memberpane,teampane;
    public Button teamScreen,memberScreen;
       @FXML
    private SplitPane Splitpane;
        @FXML
    private Stage stage;
        @FXML
    public Label DisplayInternetConnection,DisplayInternetConnection1;
    InternetConnection ic = new InternetConnection();
        
    @FXML    
    public void handleConsoleButton(ActionEvent event) throws IOException{
        if((ic.OnlineConnection())){
            memberScreen.setDisable(false);
           teamScreen.setDisable(false);
        }
        else{
            memberScreen.setDisable(true);
           teamScreen.setDisable(true);
        }
        consolepane= FXMLLoader.load(getClass().getResource("ConsoleScreen.fxml"));
        Splitpane.getItems().set(1,consolepane);
    }
    
    @FXML
     public void handleActionItemsButton(ActionEvent event) throws IOException{
         if((ic.OnlineConnection())){
            memberScreen.setDisable(false);
           teamScreen.setDisable(false);
        }
         else{
            memberScreen.setDisable(true);
           teamScreen.setDisable(true);
        }
        actionitempane= FXMLLoader.load(getClass().getResource("ActionItemScreen.fxml"));
        Splitpane.getItems().set(1,actionitempane);
        
    }
     
    @FXML 
     public void handleMemberScreenButton(ActionEvent event) throws IOException{
        Memberpane= FXMLLoader.load(getClass().getResource("MembersScreen.fxml"));
        Splitpane.getItems().set(1,Memberpane);
    }
     
    @FXML 
    public void handleTeamScreenButton(ActionEvent event) throws IOException{
        teampane= FXMLLoader.load(getClass().getResource("TeamsScreen.fxml"));
        Splitpane.getItems().set(1,teampane);
    }
    
    @FXML 
    public void handleQuitButton(ActionEvent event){
        
        
         System.exit(0);
    }
      
    
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
       try {
            Connection conn = null;
            DBConnection db = new DBConnection();
            conn=db.getConnection();
            if(conn!=null){
                PreparedStatement pst =(PreparedStatement) conn.prepareStatement("update actionitems set usable='yes' where usable='no'");
                //pst.setString(1,name1);
                pst.executeUpdate();
                DisplayInternetConnection1.setText("ONLINE");
                memberScreen.setDisable(false);
                teamScreen.setDisable(false);
            }
            else{
                DisplayInternetConnection.setText("OFFLINE");
                memberScreen.setDisable(true);
                teamScreen.setDisable(true);
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(ActionItemScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}