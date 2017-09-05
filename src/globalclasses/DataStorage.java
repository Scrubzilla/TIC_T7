/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package globalclasses;

import javafx.scene.Parent;

/**
 *
 * @author Nicklas
 */
public class DataStorage {
//---------------------------------VARIABLES----------------------------------\\   

    private static DataStorage dataStorage;
    private int currentMenuTier = 0;
    private String[] navigationBarPages = new String[4];
    private String pOneCharacter = null;
    private String pTwoCharacter = null;
    private CharacterData onlineTrackerPlayer = null;
    private String onlineTrackOpponent = null;
    private String selectedFrame = "-10";
    private boolean popUpCount = false;
    private String lastView = "";
    private String onlineTrackOpponentSearch = null;

//--------------------------------CONSTRUCTOR---------------------------------\\    
    private DataStorage() {

    }

//----------------------------------METHODS-----------------------------------\\    
    public static DataStorage getInstance() {
        //If there is no cartStorage it will be created
        if (dataStorage == null) {
            dataStorage = new DataStorage();
        }

        //Otherwise, it returns what's saved in the cartStorage
        return dataStorage;
    }

//-------------------------Program functions----------------------------------\\    
    public void setCurrentMenuTierPage(int menuTier, String currentSceneIs) {
        this.currentMenuTier = menuTier;

        if (currentSceneIs != (navigationBarPages[currentMenuTier])) {
            if (currentMenuTier == 1) {
                navigationBarPages[2] = "";
                navigationBarPages[3] = "";
            } else if (currentMenuTier == 2) {
                navigationBarPages[3] = "";
            }
        }
        navigationBarPages[currentMenuTier] = currentSceneIs;
    }

    public String getPreviousBackwardsScene() {
        currentMenuTier--;
        return navigationBarPages[currentMenuTier];

    }

    public String getPreviousForwardScene() {
        currentMenuTier++;
        return navigationBarPages[currentMenuTier];
    }

    public int getCurrentMenuTier() {
        return currentMenuTier;
    }

    public String getStringArray(int i) {
        return navigationBarPages[i];
    }

    public void setCurrentMenuTier(int currentMenuTier) {
        this.currentMenuTier = currentMenuTier;
    }

    public void resetNavigationBarPages() {
        for (int i = 0; i < navigationBarPages.length; i++) {
            navigationBarPages[i] = "";
        }
    }

    public void setLastSelectedView(String lastView) {
        this.lastView = lastView;
    }

    public String getLastSelectedView() {
        return lastView;
    }
//-------------------------SELECTED CHARACTER---------------------------------\\

    public void setPlayerOneCharacter(String selectedCharacter) {
        pOneCharacter = selectedCharacter;
    }

    public String getPlayerOneCharacter() {
        return pOneCharacter;
    }

    public void setPlayerTwoCharacter(String selectedCharacter) {
        pTwoCharacter = selectedCharacter;
    }

    public String getPlayerTwoCharacter() {
        return pTwoCharacter;
    }

    public String getSelectedFrame() {
        return selectedFrame;
    }

    public void setSelectedFrame(String selectedFrame) {
        this.selectedFrame = selectedFrame;
    }

    public boolean getPopUpCount() {
        return popUpCount;
    }

    public void setPopUpCount(boolean popUpCount) {
        this.popUpCount = popUpCount;
    }

//---------------------------ONLINE TRACKER-----------------------------------\\

    public CharacterData getOnlineTrackerPlayer() {
        return onlineTrackerPlayer;
    }

    public void setOnlineTrackerPlayer(CharacterData onlineTrackerPlayer) {
        this.onlineTrackerPlayer = onlineTrackerPlayer;
    }

    public String getOnlineTrackOpponentSearch() {
        return onlineTrackOpponentSearch;
    }

    public void setOnlineTrackOpponentSearch(String onlineTrackOpponentSearch) {
        this.onlineTrackOpponentSearch = onlineTrackOpponentSearch;
    }
    
    


}
