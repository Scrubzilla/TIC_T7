/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;

import globalclasses.DataStorage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Nicklas
 */
public class TIC_T7 extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLMenuMainScreen.fxml"));

        Scene scene = new Scene(root);
        
        //Set scene properties
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        
        //Set title to be Tekken Information Centre
        primaryStage.setTitle("TEKKEN Information Centre");

        //Adds an image and sets the program icon to be that image.
        Image programIcon = new Image("/Resources/Images/ProgramImages/logo.png");
        primaryStage.getIcons().setAll(programIcon);
        
        primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event) {
                try{
                    Platform.exit();
                }catch(Exception ex){
                    System.out.println("Error when exiting!");
                }
            }
        }));

        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

/*
    -Can be used for checking dates ex christmas / t7 release    

    Calendar cal = Calendar.getInstance();
        
    int curMonth = cal.get(Calendar.MONTH);
    int curDate = cal.get(Calendar.DATE);
    
    System.out.println(curDate + " - " + curMonth);
-----------------------------
    -Use root in sceneswitch method to change bgs / sizes etc alternatively save root in datastorage
*/
