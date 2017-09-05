/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;

import displayclasses.Tutorial;
import globalclasses.DataStorage;
import globalclasses.ErrorHandler;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Nicklas
 */
public class GuidesSpecificsController implements Initializable {
//------------------------------FXML VARIABLES--------------------------------\\    
    @FXML
    private Button navigationForwardButton, playButton;
    @FXML
    private TableView tutorialsDisplay;
    @FXML
    private TableColumn<Tutorial, ImageView> uploaderColumn;
    @FXML
    private TableColumn<Tutorial, String> titleColumn;
    @FXML
    private Label tutorialDescription, errorLabel;
//-----------------------------NON-FXML VARIABLES-----------------------------\\    
    private final ObservableList<Tutorial> tutorialList = FXCollections.observableArrayList();
    
    private final String currentSceneIs = "GuidesSpecifics";
    
    private final int menuTier = 3;
    
    private boolean noTutorialSelected = true;
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
            //End testing
            
            //Creates all the tutorials
            createTutorials();

            //Sets the layout of the list and displays the tutorials
            displayTutorials();
        }catch(Exception ex){
            displayError(0);                                                    //If an error occurs, display message no.0
        }
    }    

//------------------------------FXML METHODS----------------------------------\\    
    //Checks what happpens if a button is clicked.
    @FXML
    private void buttonClick(ActionEvent event){
        String text = ((Button)event.getSource()).getText();                    //Convert the text of a button to a string to check which button that has been clicked
        errorLabel.setText("");                                                 //Reset the error labels text
        
        switch(text){
            case "Play":                                                        //If the playbutton has been pressed
                playVideo();                                                    //Play the video
                break;
            case "Backwards":                                                   //If the navigation bar backwards button has been clicked.
                MethodHandler.getInstance().sceneSwitch(event,"FXML" + DataStorage.getInstance().getPreviousBackwardsScene() + ".fxml" );   //Switch scene to the previous one.
                break;
            case "Home":                                                        //If the navigation bar home button has been clicked. 
                DataStorage.getInstance().resetNavigationBarPages();            //Reset the list of pages
                MethodHandler.getInstance().sceneSwitch(event, "FXMLMenuMainScreen.fxml");  //Switch scene to the main menu scene.
                break;
            case "Forward":                                                      //If the navigation bar forward button has been clicked(disabled in this scene)
                MethodHandler.getInstance().sceneSwitch(event,"FXML" + DataStorage.getInstance().getPreviousForwardScene() + ".fxml" );     //Switch scene to the next scene
                break;
            default:
                displayError(1);                                                //If an error occurs, display error message no.1
                break;
        }
    }
    
    //Sets the video-description of the selected item in the list
    @FXML
    private void setTutorialDescription(){
        try{
            if(noTutorialSelected == true){                                     //Checks if a tutorial is selected or not
                noTutorialSelected = false;                                         
                playButton.setDisable(false);                                   //If an item is not selected, disable the play button
            }

            if(tutorialsDisplay.getSelectionModel().getSelectedItem() == null){ //If the user for some reason ctrl+click a marked item so there is no item selected, set the description to blank and disable the play button
                tutorialDescription.setText(" ");
                playButton.setDisable(true);
                noTutorialSelected = true;
            }
            else{                                                               //Otherwise get the description from the object and set it.
                String tempDescription = ((Tutorial)tutorialsDisplay.getSelectionModel().getSelectedItem()).getDescription();
                tutorialDescription.setText(tempDescription);
            }
        }catch(Exception ex){
            displayError(6);                                                    //If an error occurs, display error message no.6
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
                MethodHandler.getInstance().onMouseOverMenuButton(event, "tutorial" + ((Button)event.getSource()).getText() + "ButtonOnHover");
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
                MethodHandler.getInstance().onMouseExitMenuButton(event, "tutorial" + ((Button)event.getSource()).getText() + "ButtonNormal");
            }
        }catch(Exception ex){
            displayError(2);                                                    //If an error occurs, display error message no.2
        }
    }
    
//-----------------------------NON-FXML METHODS-------------------------------\\    
    //Simply changes the style that the list should have and displays the tutorials in the window
    private void displayTutorials(){
        try{
            errorLabel.setText("");                                             //When a new item is selected, reset errorlabel
            tutorialsDisplay.setItems(tutorialList);                            //Display the list with the created items
            uploaderColumn.setCellValueFactory(new PropertyValueFactory<>("uploaderLogo"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            uploaderColumn.setStyle("-fx-alignment: CENTER;");                  //Set uploader logos in the center of the column
        }catch(Exception ex){
            displayError(9);                                                    //If an error occurs, display error message no.9
        }
    }
    
    //Opens up a new browser window and loads the selected video
    private void playVideo(){
        String url = ((Tutorial)tutorialsDisplay.getSelectionModel().getSelectedItem()).getUrl();   //Get the url from the selected object
        
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
            displayError(7);                                                    //If an error occurs, display error message no.7
        }
    }
    
    //Checks what error message that should be displayed and displays it.
    private void displayError(int errorId){                                     //ID as inparameter, to decide what message to get from the array.
        try{
            String errorText = ErrorHandler.getInstance().getErrorMessage(errorId);     //Gets the arrays content and adds it to the String.
            int textLength = errorText.length();                                //Gets the length of the string.
            
            tutorialDescription.setText("");
            errorLabel.setLayoutX(695 - (textLength*5) + 12);                   //Calculates the position of where the label should be placed.
            errorLabel.setText(errorText);                                      //Sets the error text.
        }catch(Exception ex){
            String errorText = "There was an error when displaying the error.";
            int textLength = errorText.length();
        
            errorLabel.setLayoutX(695 - (textLength*5) + 12);
            errorLabel.setText(errorText);                                      //If an error occurs, display a default error message,                                                    
        }
    }
    
    //Creates the tutorials that will be used.
    private void createTutorials(){
        try{
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/TMM.png"), "TEKKEN 7 | EWGF Ultimate Guide", "There is a lot of information in this video. Watch at your own pace!\nIncluded almost everything I know about the move.", "https://www.youtube.com/watch?v=DQ45XhqFgyU"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/TMM.png"), "Devil Jin Guide (Tekken Tag 2)", "Its finally here!", "https://www.youtube.com/watch?v=ZZ29NSb1r6s"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/TMM.png"), "Heihachi Mishima Guide (Tekken Tag 2)", "Like the Kazuya guide this took forever to make, hope you all enjoy!", "https://www.youtube.com/watch?v=15ywUJzskuU"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/TMM.png"), "Kazuya Mishima Guide (Tekken Tag 2)", "This is far from a perfect guide, but I did my best. Took A LOT of hard work to put toghether!", "https://www.youtube.com/watch?v=ZSTEXcJqxGM"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/TMM.png"), "Bryan Fury Advanced Techniques", "An explanation and in depth tutorial regarding some of the harder techniques Bryan has in his arsenal.", "https://www.youtube.com/watch?v=1rKDeVFP4E8"));
            
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/SpaghettiRip.png"), "[Tekken] - Ultimate Anti-Capo Tutorial", "A guide against the main tools that the capos use.", "https://www.youtube.com/watch?v=4xJjAsC9sIU"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/SpaghettiRip.png"), "[Tekken] - Anti-Steve Fox Tutorial", "A guide against the main tools that Steve uses.", "https://www.youtube.com/watch?v=RdwhVPAlaJo"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/SpaghettiRip.png"), "[Tekken] - Anti-Ling Xiaoyu Tutorial", "A guide against the main tools that Ling uses.", "https://www.youtube.com/watch?v=YGOgWrM7hYo"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/SpaghettiRip.png"), "[TEKKEN 7] - KATARINA ALVES TUTORIAL (With Combos)", "This is the character I chose to learn when Tekken 7 came to the shores of Europe a year ago. She's insanely fun!", "https://www.youtube.com/watch?v=QSILsOmLtFI"));
            
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ThatBlastedSalami.png"), "Tekken 7 - Asuka Kazama Guide By CKT Fergus", "Courtesy of multi-tournament winning Asuka player, Fergus. This will be followed up shortly by a much more in-depth tutorial that covers every single one of Asuka's moves in great detail.", "https://www.youtube.com/watch?v=Si8qZF6zoEA"));
            
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "In the Lab With Lars: Basic Combos, Effectve Pokes", "Aris took Lars into Training Mode, learning enough to play the character online, I thought this would help new players interested in the character. ", "https://www.youtube.com/watch?v=s0tT3wvtAdU"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "In the Tekken 7 Lab with Paul: Learning About Paul's Full-Crouch Just Frame [ FC D/F+2:1]", "Paul's FC DF2 and FF2 share more common followups in Tekken 7. Aris doesn't know everything, and sometimes it's on the chat to teach him new things about characters he doesn't use!", "https://www.youtube.com/watch?v=Q1pt83cPzEw"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "In the Lab with Dragunov - Exploring Combos and Setups at the Wall", "Aris takes his main chicken to the wall, and finds a few answers for damage.", "https://www.youtube.com/watch?v=mJc62NhT4NI"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Aris Plays Tekken 7 - Learning About Miguel in Training Mode", "Aris takes Miguel into the experimentation room to go over his moveset and movement, refreshing some of the info since Tekken 6 and Tag 2. ", "https://www.youtube.com/watch?v=cBo6Z2eyMLk"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Aris Plays Tekken 7 - Learning About Lucky Chloe in Training Mode", "Another character deep-dive, checking out her movelist, moveset and other intricacies that are standard practice for learning about a character for defensive purposes.", "https://www.youtube.com/watch?v=MaL1ulOIAvg"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "In the Lab with Asuka - Exposing Fake Combos, Exploring Her Rage Drive", "Aris shows that Asuka's Grace [FC DF2] on counterhit does NOT combo directly into rage drive or rage art - someone didn't set the CPU secondary action to \"Guard All\". He does manage to find some beefy damage off these tools though!", "https://www.youtube.com/watch?v=Z74rkXAo7hw"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Aris Plays Tekken 7 - Learning About Gigas in Training Mode", "Aris checks out Gigas to familiarize himself with the character's movement, moveset, frame data and counter-strategies. This may look daunting but it's actually part of the standard procedure for learning a new character! It's good info, but it's even better to understand how he's approaching the process- so you can do it for characters you need to know!", "https://www.youtube.com/watch?v=M3_yxEGxV8E"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "TTT2 - Enemigo Dragunov Tutorial", "Editing and uploading this tutorial as one long video was a royal pain in the ass. Hope you amigos learn something. Looking forward to the next tutorial.", "https://www.youtube.com/watch?v=zj4cc1ldx70"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "TTT2 - Feng tutorial", "This is my first crack at a character tutorial. It ended up taking longer than I thought it would so I split it into 5 parts.", "https://www.youtube.com/watch?v=1DY_W39NqdA&list=PLMCyLSAjLlQ2QQDcxykiFBqdWr9WR11fW"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Intermediates - Applied Crouch Cancelling: Wall Carry with Dragunov", "Aris shows off crouch cancelling, this time with one of Dragunov's staple (but tricky) combos. Nail this and you've got massive wall carry! Check out the end cards for more tips on both Dragunov and crouch cancelling.", "https://www.youtube.com/watch?v=Me48bV1L3YY"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Beginners - Inputting Bryan's Fisherman Slam [WS+2, F+2]", "Keep in mind that WS+2 is safe, while committing to the Fisherman's Slam extension on block is -10, i.e. jab punishable. Aris shows you how to hit one of Bryan's basic moves. The move list is slightly misleading, but you can basically input fisherman's slam as a string, or just mash it out.", "https://www.youtube.com/watch?v=vIxGYg-l9C4"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Intermediates: Instant While Standing - Nina's Fluttering Butterfly [WS1]", "Commonly abbreviated as iWS, Aris shows off the technique by doing one of trickier combos Nina picked up in Tekken 6.", "https://www.youtube.com/watch?v=vTONjcjdhM8"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Intermediates - Clean Hits: Revisiting Paul's Deathfist [QCF + 2]", "If you were ever wondering why deathfist did so much damage, here's a short explanation! Clean hits change the properties of specific moves when you hit them deep. They typically change damage scaling, and they can happen in a combo.", "https://www.youtube.com/watch?v=W7bOSYJuBsk"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Intermediates - Intro to Law's Dragon Sign Stance (DSS) Cancel", "Aris explains DSS cancelling, as best as someone who doesn't play Law can do.", "https://www.youtube.com/watch?v=tf8FtqxLfcg"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Intermediates - How to Do King's Rolling Death Cradle: Applied Button Buffering", "Aris teaches you how to do one of the flashiest throws in the game, while reinforcing some of the techniques touched upon in other videos. ", "https://www.youtube.com/watch?v=gZG35QQdr64"));
        }catch(Exception ex){
            displayError(8);                                                    //If an error occurs, display error message no.8
        }
    }    

}
