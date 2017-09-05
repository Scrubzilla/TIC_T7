/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package globalclasses;

/**
 *
 * @author Nicklas
 */
public class ErrorHandler {
    private static ErrorHandler errorHandler;
    private String[] errorMessageList = {
    "Something unexpected occured when initializing the scene.",                //0
    "Unable to perform the correct button-action.",                             //1
    "There was a problem when switching the style of the button.",              //2
    "There was a problem when the program was refreshing the ranks.",           //3
    "There was a problem when the program was creating the ranks.",             //4
    "There was a problem when the program was calculating the texts position.", //5
    "Unable to calculate the position of the video description.",               //6
    "There was a problem when the program tried to play the video.",            //7
    "There was an error when creating the tutorials.",                          //8
    "There was a problem when adding the tutorials to the list.",               //9
    "There was a problem when the program tried to open the browser.",          //10
    "There was a problem when the program tried to display the text of the log."  //11
    };
    
    private ErrorHandler(){

    }
    
    public static ErrorHandler getInstance(){
        if(errorHandler == null){
            errorHandler = new ErrorHandler();
        }
        
        return errorHandler;
    }
    
    public String getErrorMessage(int id){
        return errorMessageList[id];
    }   
}

//Error ID list:
// 0 = Something unexpected occured when initializing the scene.
// 1 = Unable to perform the correct button-action.
// 2 = There was a problem when switching the layout of the button.
// 3 = There was a problem when the program was refreshing the ranks.
// 4 = There was a problem when the program was creating the ranks.
// 5 = There was a problem when the program was calculating the texts position.
// 6 = Unable to calculate the position of the video description.
// 7 = There was a problem when the program tried to play the video.
// 8 = There was an error when creating the tutorials.
// 9 = There was a problem when adding the tutorials to the list.
// 10 = There was a problem when the program tried to open the browser.
// 11 = There was a problem when the program tried to display the text of the log.
