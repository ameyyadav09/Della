/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package della;

import java.net.HttpURLConnection;
import java.net.URL;
import javafx.fxml.FXML;

/**
 *
 * @author Amey
 */


public class InternetConnection {
    @FXML public boolean OnlineConnection(){
          try {
	            try {
                        System.getProperties().put( "proxySet", "true" );
                        System.getProperties().put( "proxyHost", "10.10.10.3" );
                        System.getProperties().put( "proxyPort", "3128" );
	                URL urltest = new URL("http://www.facebook.com");
	                System.out.println(urltest.getHost());
	                HttpURLConnection con = (HttpURLConnection) urltest.openConnection();
	                con.connect();
//	                if (con.getResponseCode() == 503 || con.getResponseCode() == 200){
//                            System.out.println("lol");
//	                    System.out.println("Connection established!!");
//                            return true;
//	                }
                        return true;
	            }
                    catch (Exception exception) {
	               System.out.println("No Connection");
                       exception.printStackTrace();
                       return false;
	            }
	        } 
            catch (Exception e) {
            e.printStackTrace();
	  }
         return false;
    }
}
