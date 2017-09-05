/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;

import globalclasses.DataStorage;
import globalclasses.ErrorHandler;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Nicklas
 */
public class MiscContactController implements Initializable {
//------------------------------FXML VARIABLES--------------------------------\\    
    @FXML
    private Button navigationForwardButton;
    @FXML
    private Label errorLabel;

//-----------------------------NON-FXML VARIABLES-----------------------------\\    
    private final String currentSceneIs = "MiscContact";
    private String url = "";
    
    private final int menuTier = 2;

//-------------------------------ON START-UP----------------------------------\\    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            //Sets what "tier" in menus the program is currently on
            DataStorage.getInstance().setCurrentMenuTierPage(menuTier, currentSceneIs);

            //For testing
            System.out.println(DataStorage.getInstance().getCurrentMenuTier());
            for(int i = 0; i < 4; i++){
                System.out.println(DataStorage.getInstance().getStringArray(i));
            }
            System.out.println("-" + DataStorage.getInstance().getStringArray(menuTier+1)+ "-");
            //End testing
            
            //Checks if the forwardButton in the navigation bar should be clickable
            MethodHandler.getInstance().navigationBarForwardButtonCheck(navigationForwardButton, menuTier);
        
        }catch(Exception ex){
            displayError(0);                                                    //If an error occurs, display message no.0
        }
    }    

//------------------------------FXML METHODS----------------------------------\\    
    //Check what happens if a button is clicked.
    @FXML
    private void buttonClick(ActionEvent event){
        String text = ((Button)event.getSource()).getText();                    //Convert the text of a button to a string to check which button that has been clicked
        errorLabel.setText("");                                                 //Reset the error labels text
        
        switch(text){                                                           //Check if either FB, Twitter, Patreon, Youtube, backwards, forward or the home button was clicked
            case "Facebook":                                                    //Then go to their urls
                url = "https://www.facebook.com/tekkeninformationcentre/?fref=ts";
                openBrowser();
                break;
            case "Twitter":
                url = "https://twitter.com/TIC_App";
                openBrowser();
                break;
            case "Patreon":
                url = "https://www.patreon.com/";
                openBrowser();
                break;
            case "Youtube":
                url = "https://www.youtube.com/channel/UCAI5B-cjPRUbbD1BUix4FqA";
                openBrowser();
                break;
            case "Backwards":
                MethodHandler.getInstance().sceneSwitch(event,"FXML" + DataStorage.getInstance().getPreviousBackwardsScene() + ".fxml" );   //Switch scene to the previous one.
                break;
            case "Home":
                DataStorage.getInstance().resetNavigationBarPages();            //Reset the list of pages
                MethodHandler.getInstance().sceneSwitch(event, "FXMLMenuMainScreen.fxml");  //Switch scene to the main menu scene.
                break;
            case "Forward":
                MethodHandler.getInstance().sceneSwitch(event,"FXML" + DataStorage.getInstance().getPreviousForwardScene() + ".fxml" );     //Switch scene to the next scene
                break;
            default:
                displayError(1);                                                //If an error occurs, display error message no.1
                break;
        }
    }
    
    //Changes the style of a button while you are hovering over it.
    @FXML
    private void onMouseOver(MouseEvent event){
        try{
            //If the button you hover over is a navigation bar button, use one of those styles.
            if(((Button)event.getSource()).getText().equals("Backwards") || ((Button)event.getSource()).getText().equals("Home") || ((Button)event.getSource()).getText().equals("Forward")){
                MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button)event.getSource()).getText() + "ButtonOnHover");
            }//If the button is any other button(In this scene arrow buttons) use those styles.
            else{
                MethodHandler.getInstance().onMouseOverMenuButton(event, "contact" + ((Button)event.getSource()).getText() + "ButtonOnHover");
            }
        }catch(Exception ex){   
            displayError(2);                                                    //If an error occurs, display error message no.2
        }
    }
    
    //Changes the style of a button when you stop hovering over it.
    @FXML
    private void onMouseExit(MouseEvent event){
        try{
            //If the button you hover over is a navigation bar button, use one of those styles.
            if(((Button)event.getSource()).getText().equals("Backwards") || ((Button)event.getSource()).getText().equals("Home") || ((Button)event.getSource()).getText().equals("Forward")){
                MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button)event.getSource()).getText() + "ButtonNormal");
            }//If the button is any other button(In this scene arrow buttons) use those styles.
            else{
                MethodHandler.getInstance().onMouseExitMenuButton(event, "contact" + ((Button)event.getSource()).getText() + "ButtonNormal");
            }
        }catch(Exception ex){
            displayError(2);                                                    //If an error occurs, display error message no.2
        }
    }
    
//-----------------------------NON-FXML METHODS-------------------------------\\       
    //Opens up a new web-browser window and loads the selected page.
    private void openBrowser(){
        try{
            if(Desktop.isDesktopSupported()){                                   //If the OS is supported
                Desktop desktop = Desktop.getDesktop();

                try {
                    desktop.browse(new URI(url));                               //Start a new browser with the url loaded
                }catch(IOException | URISyntaxException e){
                        e.printStackTrace();
                }
            }
            else{
                Runtime runtime = Runtime.getRuntime();

                try {
                    runtime.exec("xdg-open " + url);                            //Otherwise do it the linux style
                } catch (IOException e) {
                    e.printStackTrace();
                }
             
            }
        }catch(Exception ex){
            displayError(10);                                                   //If an error occurs, display error message no.10
        }
    }
    
    //Checks what error message that should be displayed and displays it.
    private void displayError(int errorId){                                     //ID as inparameter, to decide what message to get from the array.
        try{
            String errorText = ErrorHandler.getInstance().getErrorMessage(errorId);     //Gets the arrays content and adds it to the String.
            int textLength = errorText.length();                                //Gets the length of the string.
            
            errorLabel.setLayoutX(695 - (textLength*5) + 12);                   //Calculates the position of where the label should be placed.
            errorLabel.setText(errorText);                                      //Sets the error text.
        }catch(Exception ex){
            String errorText = "There was an error when displaying the error.";
            int textLength = errorText.length();
        
            errorLabel.setLayoutX(695 - (textLength*5) + 12);
            errorLabel.setText(errorText);                                      //If an error occurs, display a default error message,                                                    
        }
    }
}