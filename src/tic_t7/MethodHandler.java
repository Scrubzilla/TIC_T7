/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;
import globalclasses.DataStorage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Nicklas
 */
public class MethodHandler {
//-------------------------------VARIABLES------------------------------------\\
    private static MethodHandler methodHandler;

//------------------------------CONSTRUCTOR-----------------------------------\\    
    private MethodHandler(){

    }

//--------------------------------METHODS-------------------------------------\\    
    public static MethodHandler getInstance(){
        //If there is no cartStorage it will be created
        if(methodHandler == null){
            methodHandler = new MethodHandler();
        }
        
        //Otherwise, it returns what's saved in the cartStorage
        return methodHandler;
    }
    
    public void sceneSwitch(ActionEvent event,String newSceneIs){
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(newSceneIs));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);

        }catch (IOException ex) {
            //System.out.println("ERROR SCENESWITCH!");
            ex.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void onMouseOverMenuButton(MouseEvent event, String styleId){
        ((Button) event.getSource()).setId(styleId);
    }
    
    public void onMouseExitMenuButton(MouseEvent event, String styleId){
        ((Button) event.getSource()).setId(styleId);
    }

    public void navigationBarForwardButtonCheck(Button forwardButton, int menuTier){
        if(DataStorage.getInstance().getStringArray(menuTier+1) == null || DataStorage.getInstance().getStringArray(menuTier+1).equals("")){
            forwardButton.setId("navigationBarForwardButtonDisabled");
            forwardButton.setDisable(true);
        }
    }

}

