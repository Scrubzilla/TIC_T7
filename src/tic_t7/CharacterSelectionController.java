/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;

import globalclasses.DataStorage;
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
public class CharacterSelectionController implements Initializable {
//----------------------------FXML VARIABLES----------------------------------\\    

    @FXML
    private Button navigationForwardButton;
    @FXML
    private Label punishersLabel;
//-------------------------NON-FXML VARIABLES---------------------------------\\    
    private final String currentSceneIs = "CharacterSelection";
    private final int menuTier = 2;

//------------------------------ON START-UP-----------------------------------\\
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DataStorage.getInstance().setCurrentMenuTierPage(menuTier, currentSceneIs);

        //For testing
        System.out.println(DataStorage.getInstance().getCurrentMenuTier());
        for (int i = 0; i < 4; i++) {
            System.out.println(DataStorage.getInstance().getStringArray(i));
        }
        System.out.println("-" + DataStorage.getInstance().getStringArray(menuTier + 1) + "-");

        MethodHandler.getInstance().navigationBarForwardButtonCheck(navigationForwardButton, menuTier);
    }

//------------------------------FXML METHODS----------------------------------\\
    @FXML
    private void buttonClick(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();

        switch (text) {
            case "Backwards":
                System.out.println("Backwards");
                MethodHandler.getInstance().sceneSwitch(event, "FXML" + DataStorage.getInstance().getPreviousBackwardsScene() + ".fxml");
                break;
            case "Home":
                System.out.println("Home");
                DataStorage.getInstance().resetNavigationBarPages();
                MethodHandler.getInstance().sceneSwitch(event, "FXMLMenuMainScreen.fxml");
                break;
            case "Forward":
                System.out.println("Forward");
                MethodHandler.getInstance().sceneSwitch(event, "FXML" + DataStorage.getInstance().getPreviousForwardScene() + ".fxml");
                break;
            default:
                if (text.equals("DevilJin")) {
                    text = "Devil Jin";
                } else if (text.equals("Jack7")) {
                    text = "Jack-7";
                } else if (text.equals("LuckyChloe")) {
                    text = "Lucky Chloe";
                }else if (text.equals("MasterRaven")) {
                    text = "Master Raven";
                }

                if (!text.equals(DataStorage.getInstance().getPlayerOneCharacter())) {
                    DataStorage.getInstance().setSelectedFrame("-10");
                    DataStorage.getInstance().setLastSelectedView("Character Selection");
                }

                System.out.println("HERROW");

                DataStorage.getInstance().setPlayerOneCharacter(text);
                DataStorage.getInstance().setPlayerTwoCharacter(null);

                MethodHandler.getInstance().sceneSwitch(event, "FXMLSelectedCharacterScreen.fxml");
                break;
        }
    }

    @FXML
    private void onMouseOver(MouseEvent event) {
        if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonOnHover");
        } else {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "character" + ((Button) event.getSource()).getText() + "OnHover");
        }
    }

    @FXML
    private void onMouseExit(MouseEvent event) {
        if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonNormal");
        } else {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "character" + ((Button) event.getSource()).getText() + "Normal");
        }
    }

}
