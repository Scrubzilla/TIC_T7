/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;

import globalclasses.DataStorage;
import displayclasses.Rank;
import globalclasses.ErrorHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Nicklas
 */
public class GuidesRankingController implements Initializable {
//------------------------------FXML VARIABLES--------------------------------\\    
    @FXML
    private Button navigationForwardButton, arrowUp, arrowDown;
    @FXML
    private ImageView rankImage1, rankImage2,rankImage3,rankImage4,rankImage5,rankImage6,rankImage7;
    @FXML
    private Label pointsToPromo, pointsToDemo, errorLabel;
//-----------------------------NON-FXML VARIABLES-----------------------------\\    
    private final String currentSceneIs = "GuidesRanking";

    private final Rank[] rankList = new Rank[36];
    
    private final int menuTier = 2;
    private int selectedRank = 1;
    
//-------------------------------ON START-UP----------------------------------\\    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            //Creates all of the ranks that are used
            createRanks();
            
            //For testing
            System.out.println(DataStorage.getInstance().getCurrentMenuTier());
            for(int i = 0; i < 4; i++){
                System.out.println(DataStorage.getInstance().getStringArray(i));
            }
            System.out.println("-" + DataStorage.getInstance().getStringArray(menuTier+1)+ "-");
            //End testing
            
            //Sets what "tier" in menus the program is currently on
            DataStorage.getInstance().setCurrentMenuTierPage(menuTier, currentSceneIs);

            //Checks if the forwardButton in the navigation bar should be clickable
            MethodHandler.getInstance().navigationBarForwardButtonCheck(navigationForwardButton, menuTier);
          
        }catch(Exception ex){
            displayError(0);                                                    //If an error occurs, display error message no.0
        }
        
    }    

//------------------------------FXML METHODS----------------------------------\\    
    //Checks what happpens if a button is clicked.
    @FXML
    private void buttonClick(ActionEvent event){
        String text = ((Button)event.getSource()).getText();                    //Convert the text of a button to a string to chech which button that has been clicked.
        errorLabel.setText("");                                                 //Reset the error labels text
        
        switch(text){               
            case "ArrowUp":                                                     //If the arrowUp button has been clicked
                selectedRank++;                                                 //Increse the selected rank with +1;
                refreshRanks();                                                 //Refresh the displayed ranks.
                
                if(selectedRank == 2 || selectedRank == 36){                    //If the selected ranks is now 2, you should be able to use the downArrow, if selectedRank is now 36, you should not be able to use this arrow.
                    if(selectedRank == 2){
                        arrowDown.setDisable(false);                            //unDisable the downArrow
                    }
                    else if(selectedRank == 36){
                        arrowUp.setDisable(true);                               //Disable this arrow.
                    }
                }
                break;
            case "ArrowDown":                                                   //If the arrowDown button has been clicked.
                selectedRank--;                                                 //Decrese the selected rank with -1;
                refreshRanks();                                                 //Refresh the displayed ranks.
                
                if(selectedRank == 1 || selectedRank == 35){                    //If the selected rank is 1, you should not be able to use this button, if it is now 36, you should be able to use the arrowUp again.
                    if(selectedRank == 1){
                        arrowDown.setDisable(true);                             //Disable this arrow.
                    }
                    else if(selectedRank == 35){
                        arrowUp.setDisable(false);                              //UnDisable the arrowUp.
                    }
                }
                break;
            case "Backwards":                                                   //If the navigation bar backwards button has been clicked.
                MethodHandler.getInstance().sceneSwitch(event,"FXML" + DataStorage.getInstance().getPreviousBackwardsScene() + ".fxml" );   //Switch scene to the previous one.
                break;
            case "Home":                                                        //If the navigation bar home button has been clicked.    
                DataStorage.getInstance().resetNavigationBarPages();            //Reset the list of pages
                MethodHandler.getInstance().sceneSwitch(event, "FXMLMenuMainScreen.fxml");      //Switch scene to the main menu scene.
                break;
            case "Forward":                                                     //If the navigation bar forward button has been clicked(disabled in this scene)
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
                MethodHandler.getInstance().onMouseOverMenuButton(event, "ranking" + ((Button)event.getSource()).getText() + "ButtonOnHover");
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
               MethodHandler.getInstance().onMouseExitMenuButton(event, "ranking" + ((Button)event.getSource()).getText() + "ButtonNormal");
           }
        }catch(Exception ex){
            displayError(2);                                                    //If an error occurs, display error message no.2
        }
    }                    
        
//-----------------------------NON-FXML METHODS-------------------------------\\
    //Refreshes the rank list so that the correct ranks are shown when the user clicks the arrow buttons.
    private void refreshRanks(){                                                
        try{
            int rankSelectionModifier = 4;                                      //This number is used to calculate which ranks that should be displayed.
            Image tempImage[] = new Image[7];                                   //7 ranks should be displayed and therefore 7 images has to be stored.      

            for(int i = 0; i <= 6; i++){                                        //Calculates which 7 ranks that should be displayed depending on what rank that is selected.
                try{
                    tempImage[i] = rankList[selectedRank - rankSelectionModifier].getRankImage();   //Calculates which rank that should be picked. Takes the (selectedRank - what spot in the list that the rank is placed).
                    rankSelectionModifier--;                                    //Increse the spot with +1.
                }catch(Exception ex){
                    tempImage[i] = null;                                        //If the spot in the list does not exist, there should not be a picture.
                    rankSelectionModifier--;                                    //Increase the spot with +1;
                }
            }

            checkPromoText();                                                   //Check where the promo/demo numbers should be placed and set them.

            rankImage1.setImage(tempImage[0]);                                  //Sets the different images.
            rankImage2.setImage(tempImage[1]); 
            rankImage3.setImage(tempImage[2]); 
            rankImage4.setImage(tempImage[3]); 
            rankImage5.setImage(tempImage[4]);
            rankImage6.setImage(tempImage[5]);
            rankImage7.setImage(tempImage[6]);
            
        }catch(Exception ex){
            displayError(3);                                                    //If an error occurs display error message no.3
        }
        
    }                              
    
    //Creates all of the ranks when the scene is loaded.
    private void createRanks(){
        try{
            for(int i = 1; i <= 36; i++){                                       //Loops and creates 36 different ranks.
                Image rankImage = new Image("/Resources/Images/Ranks/" + i + ".png");   //Gets the correct image for the rank.

                int promoPoints = 0;
                int demoPoints= 0;

                if(i <= 3){                                                     //Sets the correct promo/demo points depending on which ranknumber that is created.
                    promoPoints = 2;
                    demoPoints = 2;
                }
                else if(i >= 4 && i <=7){
                    promoPoints = 3;
                    demoPoints = 3;
                }
                else if(i >= 8 && i <= 11){
                    promoPoints = 4;
                    demoPoints = 4;
                }
                else if(i >= 12 && i <= 15){
                    promoPoints = 5;
                    demoPoints = 5;
                }
                else if(i >= 16 && i <= 19){
                    promoPoints = 6;
                    demoPoints = 6;
                }
                else if(i >= 20 && i <= 23){
                    promoPoints = 7;
                    demoPoints = 7;
                }
                else if(i >= 24 && i <= 27){
                    promoPoints = 8;
                    demoPoints = 8;
                }
                else if(i >= 28 && i <= 31){
                    promoPoints = 9;
                    demoPoints = 9;
                }
                else if(i >= 32 && i <= 34){
                    promoPoints = 10;
                    demoPoints = 10;
                }
                else if(i >= 35 && i <= 36){
                    promoPoints = 11;
                    demoPoints = 11;
                }

                Rank newRank = new Rank(i , rankImage, promoPoints*8, demoPoints*8);    //Creates the rank with the correct data.
                rankList[i-1] = newRank;                                        //Puts the rank into the list at spot i-1 (0,1,2,3,4...) since i starts at 1.
            }

            refreshRanks();                                                     //Displays the ranks that should be displayed on initialization.
        
        }catch(Exception ex){
            displayError(4);                                                    //If an error occurs, display error message no.4                                               
        }
    }                                  
    
    //Checks where the promotion numbers should be positioned.
    private void checkPromoText(){                                              
        try{
            String textToCheck = Integer.toString(rankList[selectedRank-1].getAmountOfPointsPromotion());   //Store one of the numbers in a string.
            
            pointsToPromo.setLayoutX(634 - (textToCheck.length()*7));           //Position the promotion nu,bers at posX 634 - the length of the word*7 which is the amount of pixels a letter takes.
            pointsToDemo.setLayoutX(644 - (textToCheck.length()*7));            //Same as above but 641 instead of 634.

            pointsToPromo.setText(Integer.toString(rankList[selectedRank-1].getAmountOfPointsPromotion()));     //Set the text of the label to the value you get from the rank.
            pointsToDemo.setText(Integer.toString(rankList[selectedRank-1].getAmountOfPointsDemotion()));       //Set the text of the label to the value you get from the rank.
        }catch(Exception ex){
            displayError(5);                                                    //If an error occurs, display error message no.5                                     
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
