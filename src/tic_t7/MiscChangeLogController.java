/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;

import globalclasses.DataStorage;
import globalclasses.ErrorHandler;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Nicklas
 */
public class MiscChangeLogController implements Initializable {
//------------------------------FXML VARIABLES--------------------------------\\    
    @FXML
    private Button navigationForwardButton;
    @FXML
    private ListView logsVersions;
    @FXML
    private TextArea logsDisplay;
//-----------------------------NON-FXML VARIABLES-----------------------------\\    
    private final ObservableList<String> logList = FXCollections.observableArrayList("2017-07-09", "2016-11-21", "2016-02-20", "2015-12-24", "2015-11-05");
    
    private final String currentSceneIs = "MiscChangeLog";
    
    private final int menuTier = 2;
    
    private boolean firstTimeLoad = true;
    private boolean errorRecentlyOccured = false;
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
            
            //Adds all of the different versions to the list and then displays the latest changes in the textbox.
            logsVersions.setItems(logList);
            displayLogText();
        }catch(Exception ex){
            displayError(0);                                                    //If an error occurs, display message no.0
        }
        
    }    

//------------------------------FXML METHODS----------------------------------\\    
    //Check what happens if a button is clicked.
    @FXML
    private void buttonClick(ActionEvent event){
        String text = ((Button)event.getSource()).getText();                    //Convert the text of a button to a string to check which button that has been clicked
        
        switch(text){                                                           //Check if either backwards, forward or the home button was clicked
            case "Backwards":
                MethodHandler.getInstance().sceneSwitch(event,"FXML" + DataStorage.getInstance().getPreviousBackwardsScene() + ".fxml" );   //Switch scene to the previous one.
                break;
            case "Home":
                DataStorage.getInstance().resetNavigationBarPages();            //Reset the list of pages
                MethodHandler.getInstance().sceneSwitch(event, "FXMLMenuMainScreen.fxml");  //Switch scene to the main menu scene.
                break;
            case "Forward":
                MethodHandler.getInstance().sceneSwitch(event,"FXML" + DataStorage.getInstance().getPreviousForwardScene() + ".fxml" ); //Switch scene to the next scene
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
                MethodHandler.getInstance().onMouseOverMenuButton(event, "menu" + ((Button)event.getSource()).getText() + "ButtonOnHover");
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
                MethodHandler.getInstance().onMouseExitMenuButton(event, "menu" + ((Button)event.getSource()).getText() + "ButtonNormal");
            }
        }catch(Exception ex){
            displayError(2);                                                    //If an error occurs, display error message no.2
        }
    }
    
    //Displays the text of the selected log.
    @FXML
    private void displayLogText(){
        try {
            String fileToLoad = ""; 
            String logText = "";
            
            if(errorRecentlyOccured = true){                                    //If an error has recently occured, change back the text color to normal.
                logsDisplay.setStyle("-fx-text-fill: #d9d9d9;");
                errorRecentlyOccured = false;                                   //Change back the boolean to that this does not happen every time.
            }
            
            if(firstTimeLoad == true){                                          //If it is the first time loading up the scene the latest log should be displayed.
                fileToLoad = logList.get(0);                                    //Set the target log to be the latest one.
                
                Path logPath = Paths.get("./src/Resources/Textfiles/Changelogs", fileToLoad + ".txt");  //Set the target path to the latest log.
            
                List<String>readLines = Files.readAllLines(logPath);            //Add all of the text from the log to an array

                for(int i = 0; i < readLines.size(); i++){
                    logText = logText  + readLines.get(i)+ "\n";                //Set the logText to be the content of the array.
                }
            }
            else if(logsVersions.getSelectionModel().getSelectedItem() == null){    //If the user for some reason CTRL+click one of the logs in the listView, the textbox should be empty
                logText = "";
            }   
            else{                                                               //Otherwise the following will happen: 
                fileToLoad = logList.get(logsVersions.getSelectionModel().getSelectedIndex());  //Set the target log to be the selected one.
                System.out.println(logsVersions.getSelectionModel().getSelectedIndex());
                Path logPath = Paths.get("./src/Resources/Textfiles/Changelogs", fileToLoad + ".txt");  //Set the target path to the selected log.
            
                List<String>readLines = Files.readAllLines(logPath);            //Add all of the text from the log to an array

                for(int i = 0; i < readLines.size(); i++){
                    logText = logText  + readLines.get(i)+ "\n";                //Set the logText to be the content of the array.
                }
            }
            
            if(firstTimeLoad == true){                                          //If it's the first time loading up the scene, a little extra text should be added, otherwise only the logtext should be displayed.
                logsDisplay.setText("Latest changes: \n\n" + logText);
                firstTimeLoad = false;
            }else{
                logsDisplay.setText(logText);
            }
            
        } catch (Exception ex) {
            displayError(11);                                                   //If an error occurs, display error message no.11
        }
    }
    
//---------------------------NON-FXML METHODS---------------------------------\\    
    //Checks what error message that should be displayed and displays it.
    private void displayError(int errorId){                                     //ID as inparameter, to decide what message to get from the array.
        logsDisplay.setStyle("-fx-text-fill: #bf0000;");                        //Set the text color to the appropriate errorText color.
        
        try{
            String errorText = ErrorHandler.getInstance().getErrorMessage(errorId);     //Gets the arrays content and adds it to the String.
            logsDisplay.setText(errorText);                                     //Set the text in textbox to the errormessage.
            errorRecentlyOccured = true;                                        //Set the boolean to that an error has recently occured.
        }catch(Exception ex){
            String errorText = "There was an error when displaying the error."; //If an error occurs, display a default error message,      
            logsDisplay.setText(errorText);                                     //Set the text in textbox to the errormessage.
            errorRecentlyOccured = true;                                        //Set the boolean to that an error has recently occured.
        }
    }
    
}
