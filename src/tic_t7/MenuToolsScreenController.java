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
 * @author Igotballz
 */
public class MenuToolsScreenController implements Initializable {
//------------------------------FXML VARIABLES--------------------------------\\

    @FXML
    private Label errorLabel;
    @FXML
    private Button navigationForwardButton;

//-----------------------------NON_FXML VARIABLES-----------------------------\\    
    private final String currentSceneIs = "MenuToolsScreen";
    private final int menuTier = 1;

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
            case "OnlineTracker":
                System.out.println("OnlineTracker");
                MethodHandler.getInstance().sceneSwitch(event, "FXMLToolsOnlineTracker.fxml");
                break;
            case "ThrowBreaking":
                System.out.println("ThrowBreaking");
                MethodHandler.getInstance().sceneSwitch(event, "FXMLGuidesThrowbreaking.fxml");
                break;
            case "Notes":
                System.out.println("Notes");
                MethodHandler.getInstance().sceneSwitch(event, "FXMLMiscNotes.fxml");
                break;
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
                errorLabel.setLayoutX(441);
                errorLabel.setText("ERROR: A problem occured when the program tried to go to the next scene.");
                break;
        }
    }

    @FXML
    private void onMouseOver(MouseEvent event) {
        if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonOnHover");
        } else {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "menu" + ((Button) event.getSource()).getText() + "ButtonOnHover");
        }
    }

    @FXML
    private void onMouseExit(MouseEvent event) {
        if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonNormal");
        } else {
            MethodHandler.getInstance().onMouseExitMenuButton(event, "menu" + ((Button) event.getSource()).getText() + "ButtonNormal");
        }
    }

}
