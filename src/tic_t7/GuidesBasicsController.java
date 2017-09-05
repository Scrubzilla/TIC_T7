/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;

import globalclasses.DataStorage;
import globalclasses.ErrorHandler;
import displayclasses.Tutorial;
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
public class GuidesBasicsController implements Initializable {
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

    private final String currentSceneIs = "GuidesBasics";

    private final int menuTier = 3;

    private boolean noTutorialSelected = true;

//-------------------------------ON START-UP----------------------------------\\    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Sets what "tier" in menus the program is currently on
            DataStorage.getInstance().setCurrentMenuTierPage(menuTier, currentSceneIs);

            //For testing
            System.out.println(DataStorage.getInstance().getCurrentMenuTier());
            for (int i = 0; i < 4; i++) {
                System.out.println(DataStorage.getInstance().getStringArray(i));
            }
            //End testing

            //Creates all the tutorials
            createTutorials();

            //Sets the layout of the list and displays the tutorials
            displayTutorials();
        } catch (Exception ex) {
            displayError(0);                                                    //If an error occurs, display message no.0
        }
    }

//------------------------------FXML METHODS----------------------------------\\    
    //Checks what happpens if a button is clicked.
    @FXML
    private void buttonClick(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();                    //Convert the text of a button to a string to check which button that has been clicked
        errorLabel.setText("");                                                 //Reset the error labels text

        switch (text) {
            case "Play":                                                        //If the playbutton has been pressed
                playVideo();                                                    //Play the video
                break;
            case "Backwards":                                                   //If the navigation bar backwards button has been clicked.
                MethodHandler.getInstance().sceneSwitch(event, "FXML" + DataStorage.getInstance().getPreviousBackwardsScene() + ".fxml");   //Switch scene to the previous one.
                break;
            case "Home":                                                        //If the navigation bar home button has been clicked. 
                DataStorage.getInstance().resetNavigationBarPages();            //Reset the list of pages
                MethodHandler.getInstance().sceneSwitch(event, "FXMLMenuMainScreen.fxml");  //Switch scene to the main menu scene.
                break;
            case "Forward":                                                      //If the navigation bar forward button has been clicked(disabled in this scene)
                MethodHandler.getInstance().sceneSwitch(event, "FXML" + DataStorage.getInstance().getPreviousForwardScene() + ".fxml");     //Switch scene to the next scene
                break;
            default:
                displayError(1);                                                //If an error occurs, display error message no.1
                break;
        }
    }

    //Sets the video-description of the selected item in the list
    @FXML
    private void setTutorialDescription() {
        try {
            if (noTutorialSelected == true) {                                     //Checks if a tutorial is selected or not
                noTutorialSelected = false;
                playButton.setDisable(false);                                   //If an item is not selected, disable the play button
            }

            if (tutorialsDisplay.getSelectionModel().getSelectedItem() == null) { //If the user for some reason ctrl+click a marked item so there is no item selected, set the description to blank and disable the play button
                tutorialDescription.setText(" ");
                playButton.setDisable(true);
                noTutorialSelected = true;
            } else {                                                               //Otherwise get the description from the object and set it.
                String tempDescription = ((Tutorial) tutorialsDisplay.getSelectionModel().getSelectedItem()).getDescription();
                tutorialDescription.setText(tempDescription);
            }
        } catch (Exception ex) {
            displayError(6);                                                    //If an error occurs, display error message no.6
        }
    }

    //Changes the style of a button while you are hovering over it.
    @FXML
    private void onMouseOver(MouseEvent event) {
        try {
            //If the button you hover over is a navigation bar button, use one of those styles.
            if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
                MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonOnHover");
            }//If the button is any other button(In this scene arrow buttons) use those styles.
            else {
                MethodHandler.getInstance().onMouseOverMenuButton(event, "tutorial" + ((Button) event.getSource()).getText() + "ButtonOnHover");
            }
        } catch (Exception ex) {
            displayError(2);                                                    //If an error occurs, display error message no.2
        }
    }

    //Changes the style of a button when you stop hovering over it.
    @FXML
    private void onMouseExit(MouseEvent event) {
        try {
            //If the button you hover over is a navigation bar button, use one of those styles.
            if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
                MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonNormal");
            }//If the button is any other button(In this scene arrow buttons) use those styles.
            else {
                MethodHandler.getInstance().onMouseExitMenuButton(event, "tutorial" + ((Button) event.getSource()).getText() + "ButtonNormal");
            }
        } catch (Exception ex) {
            displayError(2);                                                    //If an error occurs, display error message no.2
        }
    }

//-----------------------------NON-FXML METHODS-------------------------------\\    
    //Simply changes the style that the list should have and displays the tutorials in the window
    private void displayTutorials() {
        try {
            errorLabel.setText("");                                             //When a new item is selected, reset errorlabel
            tutorialsDisplay.setItems(tutorialList);                            //Display the list with the created items
            uploaderColumn.setCellValueFactory(new PropertyValueFactory<>("uploaderLogo"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            uploaderColumn.setStyle("-fx-alignment: CENTER;");                  //Set uploader logos in the center of the column
        } catch (Exception ex) {
            displayError(9);                                                    //If an error occurs, display error message no.9
        }
    }

    //Opens up a new browser window and loads the selected video
    private void playVideo() {
        String url = ((Tutorial) tutorialsDisplay.getSelectionModel().getSelectedItem()).getUrl();   //Get the url from the selected object

        try {
            if (Desktop.isDesktopSupported()) {                                 //If the OS is supported
                Desktop desktop = Desktop.getDesktop();

                try {
                    desktop.browse(new URI(url));                               //Start a new browser with the url loaded
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                Runtime runtime = Runtime.getRuntime();

                try {
                    runtime.exec("xdg-open " + url);                            //Otherwise do it the linux style
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception ex) {
            displayError(7);                                                    //If an error occurs, display error message no.7
        }
    }

    //Checks what error message that should be displayed and displays it.
    private void displayError(int errorId) {                                    //ID as inparameter, to decide what message to get from the array.
        try {
            String errorText = ErrorHandler.getInstance().getErrorMessage(errorId);     //Gets the arrays content and adds it to the String.
            int textLength = errorText.length();                                //Gets the length of the string.

            tutorialDescription.setText("");
            errorLabel.setLayoutX(695 - (textLength * 5) + 12);                 //Calculates the position of where the label should be placed.
            errorLabel.setText(errorText);                                      //Sets the error text.
        } catch (Exception ex) {
            String errorText = "There was an error when displaying the error.";
            int textLength = errorText.length();

            errorLabel.setLayoutX(695 - (textLength * 5) + 12);
            errorLabel.setText(errorText);                                      //If an error occurs, display a default error message,                                                    
        }
    }

    //Creates the tutorials that will be used.
    private void createTutorials() {
        try {
            //Uploader, Title, Description, Link
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Advanced Seminar - Chickens and Reversals", "One of the more obscure and advanced topics in Tekken, chickening allows you to counter parries with additional buffered inputs. Aris explores some reversals on airborne moves at the request of the chat afterward, so I left it intact.", "https://www.youtube.com/watch?v=aXgJFss14qI"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Intermediates - Crouch Cancelling", "Crouch cancelling allows you to do standing moves from crouching states. It's subtle but it's necessary to convert a number of combos for some characters. Aris uses Asuka's Sacred Blade [3~4] as an example, but many other characters use or even require this!", "https://www.youtube.com/watch?v=wSNnuos4T7o"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Intermediates - Double Over and Nosebleed Stun", "Aris covered the topic in Tag 2, but a refresher is always nice (for him). He covers some of the more notorious examples of these types of stuns, after learning about Paul's!", "https://www.youtube.com/watch?v=T3m6_dSVq-Y"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Beginners - Introduction to Crouch Dashing [F, N, D, D/F]", "Aris covers the basic building block of wavedashing - the dashing crouch dash. He touches upon more advanced techniques here but doesn't go into great detail. Left some bonus material at the end!", "https://www.youtube.com/watch?v=mGF3vop20RQ"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Beginners - Don't Get Discouraged By Losses", "Even if you use all the tutorials available (and you should), you'll still run into your share of losses. The important part is to play to improve, even when you can't see it.", "https://www.youtube.com/watch?v=6r_NJGRlvqQ"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Intermediates - Options Near the Wall: Offense, Walljumping", "Aris talks about how the wall functions in a 3d game, including how it affects spacing, offensive options, and move attributes. He also recommends looking for supplementary material to help you figure out what works for your character at the wall", "https://www.youtube.com/watch?v=2SW7kCmA23o"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Intermediates - Low Parry vs. Blocking: Get to Know Law's Junkyard [B + 2,3,4]", "Aris helps you get started on universal parries with a notorious of example of where it's necessary. This tutorial assumes you already know the input: down-forward at the time of low impact.", "https://www.youtube.com/watch?v=vCRsk35toYE"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Beginners - Input Buffering: Combos and Tick Throws", "Followups can be buffered pretty liberally throughout the recovery frames of preceding moves. Aris demonstrates this by showing you how to execute bnb combos and tick command throws. Use the system mechanics to your advantage!", "https://www.youtube.com/watch?v=6O5FMVD4IZU"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Beginners - Thinking Critically about Defense, Examples at the Wall", "Aris explains the methodology and rationale to help you improve your defensive game, by making use of Tekken's record functions in training mode. He also covers how to weather the opponent's offense when your back is to the wall.", "https://www.youtube.com/watch?v=_oyQXrpdubg"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Beginners - Getting Off The Ground", "Aris covers some of the many options you have once you hit the deck. Finer details are omitted, but this should be enough to get you started!", "https://www.youtube.com/watch?v=j-PDc20Jezg"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Beginners - Breaking Throws", "Throws have changed quite a bit in Tekken 7, but Aris covers them from the ground up for players just starting out. There's some additional material on throwing armored moves in the latter half, which I left intact.", "https://www.youtube.com/watch?v=m4em9HzrwFI"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Beginners - Understanding Frame Advantage, Dealing with Distractions", "Also a good tutorial on how to keep your train of thought! Obviously this was in the context of Tekken, but this applies to all fighting games.", "https://www.youtube.com/watch?v=E_q_hWDBxts"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Beginners: Button Buffering - Ki Charge, Dragunov Combo Applications", "Aris talks about some of the mechanics more unique to Tekken, and their practical applications for Dragunov's wall and step kick combos. If anyone has any other character-specific examples, feel free to leave a comment! ", "https://www.youtube.com/watch?v=wBpSTK4Sjio"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Beginners: Crush Moves, Examples with Josie & Lucky Chloe", "Aris covers evasive moves in Tekken. The interplay between 3d collision detection and crushing result in some wild situations. What moves crush what depends on the character, be sure to explore your movelists!", "https://www.youtube.com/watch?v=BAkyAtCxyoM"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips For Beginners - Introduction to Effective Sidestepping", "Targeted at those who already know how to sidestep, this video reinforces some of the other principles covered in earlier videos. The rationale is to make a calculated decision to sidestep, in order to evade a specific move or threat. Otherwise, it's better to just block.", "https://www.youtube.com/watch?v=ncCRFrZeAXY"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Beginners: Making Decisions in Neutral, Kazuya Case Study", "Aris breaks down the midscreen from the perspective of decision-making. I included a clip of Aris applying some of the principles in a ranked match at the end.", "https://www.youtube.com/watch?v=PCKGGWNFXKw"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Beginners: Blocking Lows, Punishing, While Standing", "Aris covers an often-overlooked basic technique: punishing after blocking! He goes into some depth on the while-standing system, as well as some rules of thumb for safety (or lack thereof).", "https://www.youtube.com/watch?v=vmVZTlT48Eg"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Intermediates - Instant While Running Primer", "An important part of the gameplan for some characters, notably Dragunov. Aris explains how Dragunov's iWR2 works, and gives a few tips on helping you get it out consistently. Check out Aris' guide from T6 for another perspective!", "https://www.youtube.com/watch?v=0iDmqmyL2kw"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Beginners: Mids vs. Special Mids - Know the Difference", "Crouch jab is a really common move you see at all levels of play, so I wanted to capture this in a separate video.", "https://www.youtube.com/watch?v=wC1Q-k8qZVQ"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 Tips for Beginners: Exploring Your Strings/Combos, Using Backdash", "Aris shows how to get started in practice mode, by teaching you how to verify your strings to find natural combos. He also touches upon some of the basic movement and relates it to 2d games.", "https://www.youtube.com/watch?v=KbtwQxNQfxY"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "Tekken 7 FAQ: I'm New, Who Should I Play?", "Early 2017 is here, what could be a more appropriate question to kick things off?", "https://www.youtube.com/watch?v=o26Ij7uStxo"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "The Basics of Tekken (Tekken 7) - Familiarity with situations makes a player", "", "https://www.youtube.com/watch?v=yF7LA-sS3h4"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "The Basics of Tekken (Tekken 7) - Tying moves together with movement", "", "https://www.youtube.com/watch?v=hJEJ8PJXoaM"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "The Basics of Tekken (Tekken 7) - The Application of Frame Data", "", "https://www.youtube.com/watch?v=35sf8A9Wnv0"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "TTT2 Korean Back Dash Tutorial Part 1 (1P Side)", "Hopefully this video helps people understant how to back dash cancel on the 1p side.", "https://www.youtube.com/watch?v=gLp3Y0PdV94"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ATP.png"), "TTT2 Korean Back Dash Tutorial Part 2 (2P Side)", "In today's video, I talk about how to back dash cancel on the 2p side. Practice! ", "https://www.youtube.com/watch?v=gGee7eL9Vrk"));

            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/SpaghettiRip.png"), "[TEKKEN 7] - HOW TO GET HIGH RANKS ONLINE", "5 steps on how you can improve your level and rank up online much easier!", "https://www.youtube.com/watch?v=hhNvSwX80io"));
            
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ThatBlastedSalami.png"), "Tekken 7 Character Overviews", "Based on Fergus's excellent guide. An overview of every single character in the roster, using footage from some of the best players in the world. Designed to help beginners, and maybe even some veterans, get the general gist of the characters' playstyles and ease of use.", "https://www.youtube.com/watch?v=Nbewxse8DlI"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ThatBlastedSalami.png"), "Tekken 7 - Mix Ups, Turtles & Movement", "This tutorial explains some Tekken Terminology relating to Mix Ups and Turtling, while also covering some fundamentals such as spacing, punishment, counter hits, frame advantage, and the importance of Movement.", "https://www.youtube.com/watch?v=weR0poIEkKE"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ThatBlastedSalami.png"), "Tekken 7 - Pokes, Frames & Sidesteps", "This tutorial aims to demystify Poking, Sidestepping and Frame Data, while also covering fundamentals such as counter hit baiting, hit confirming, putting moves in sequences, and why Tekken is not played in \"turns\".", "https://www.youtube.com/watch?v=p5iVJXd7toE"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/ThatBlastedSalami.png"), "Tekken 7 - Crushing, Hitboxes & Evasion", "An in depth examination of the Crush System; High, Low and Powercrush, written by tournament veteran Dinosaur. This tutorial also covers Hitbox and Hurtbox Interactions, Rage Arts, and some general Tekken jankiness.", "https://www.youtube.com/watch?v=AvnDpz_lmEI"));
            
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/BNEA.png"), "TEKKEN 7 Game Guide - Power Crush", "Check out how to use Power Crush in TEKKEN 7!", "https://www.youtube.com/watch?v=n8Ri0bxqBZo"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/BNEA.png"), "TEKKEN 7 Game Guide - Rage Art", "Learn how to unleash the power of Rage Art in TEKKEN 7! ", "https://www.youtube.com/watch?v=E2IckcQru3U"));
            tutorialList.add(new Tutorial(new ImageView("/Resources/Images/TutorialLogos/BNEA.png"), "TEKKEN 7 Game Guide - Movement", "TEKKEN 7 is an immersive 3D fighting game that tests a player's reflex, skill and mental prowess within the environmental battlefield of game play. One of the most basic of all skills you'll need to make use of in T7 is MOVEMENT", "https://www.youtube.com/watch?v=UCN3D_m3D7Y"));

        } catch (Exception ex) {
            displayError(8);                                                    //If an error occurs, display error message no.8
        }
    }

}
