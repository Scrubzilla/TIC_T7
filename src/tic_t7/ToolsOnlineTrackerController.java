package tic_t7;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import com.jfoenix.controls.JFXTextField;
import displayclasses.Rank;
import globalclasses.CharacterData;
import globalclasses.DataStorage;
import globalclasses.Opponent;
import globalclasses.OpponentCharacter;
import java.net.URL;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Scrubzilla
 */
public class ToolsOnlineTrackerController implements Initializable {

//------------------------------FXML VARIABLES--------------------------------\\    
    @FXML
    private Button navigationForwardButton, matchAddBut, alreadyStartedBut, freshStartBut, blockConfirmBut;
    @FXML
    private ProgressBar ptsBar;
    @FXML
    private PieChart winLossChart;
    @FXML
    private AnchorPane blockScreen, blockInfoScreen;
    @FXML
    private ComboBox playerCharList, opponentCharList, matchResultBox, opponentMatchRank, matchTypeBox, blockRankList;
    @FXML
    private ImageView pOnePortrait, pTwoPortrait, playerRank, demoRank, promoRank;
    @FXML
    private Image tempImage;
    @FXML
    private Label currentRankLabel, rankPointsLabel, pointsLabel, reqDemoPtsLabel, curPtsLabel, reqPromoPtsLabel, opponentName, winsLosses, winLossRatio, opponentTopChars, opponentTopCharsHead, opponentTopCharsRatio, blockInfoLabel;
    @FXML
    private JFXTextField oppSearchField;
    @FXML
    private TextField blockRankPointsInput;

//-----------------------------NON-FXML VARIABLES-----------------------------\\   
    private final ObservableList<String> characterList = FXCollections.observableArrayList("Akuma", "Alisa", "Asuka", "Bob", "Bryan", "Claudio", "Devil Jin", "Dragunov", "Eddy", "Eliza", "Feng", "Gigas", "Heihachi", "Hwoarang", "Jack-7", "Jin", "Josie", "Katarina", "Kazumi", "Kazuya", "King", "Kuma", "Lars", "Law", "Lee", "Leo", "Lili", "Lucky Chloe", "Master Raven", "Miguel", "Nina", "Panda", "Paul", "Shaheen", "Steve", "Xiaoyu", "Yoshimitsu");
    private final ObservableList<String> matchResultList = FXCollections.observableArrayList("Win", "Loss");
    private final ObservableList<String> matchTypeList = FXCollections.observableArrayList("Ranked", "Non-ranked");
    private ObservableList<Image> matchRanksList = FXCollections.observableArrayList();

    private final String currentSceneIs = "ToolsOnlineTracker";
    private Path opponentDataPath;
    private final Path playerDataPath = Paths.get("./src/Resources/TextFiles/OnlineTracker/PlayerData.ser");
    private ArrayList<CharacterData> playerDataCache = new ArrayList<>();
    private final Rank[] rankList = new Rank[36];
    private final int menuTier = 2;
    private int totalPts = 0;
    private int charSlotId = -1;

//-------------------------------ON START-UP----------------------------------\\    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DataStorage.getInstance().setCurrentMenuTierPage(menuTier, currentSceneIs);

        playerCharList.setItems(characterList);
        opponentCharList.setItems(characterList);

        //For testing
        System.out.println(DataStorage.getInstance().getCurrentMenuTier());
        for (int i = 0; i < 4; i++) {
            System.out.println(DataStorage.getInstance().getStringArray(i));
        }
        System.out.println("-" + DataStorage.getInstance().getStringArray(menuTier + 1) + "-");
        //End testing

        MethodHandler.getInstance().navigationBarForwardButtonCheck(navigationForwardButton, menuTier);

        createRanks();
        readPlayerData();

        if (DataStorage.getInstance().getOnlineTrackOpponentSearch() != null) {
            oppSearchField.setText(DataStorage.getInstance().getOnlineTrackOpponentSearch());
        }

        if (DataStorage.getInstance().getOnlineTrackerPlayer() != null) {
            tempImage = new Image("/Resources/Images/FDPortraits/" + DataStorage.getInstance().getOnlineTrackerPlayer().getCharacterName() + ".png");
            pOnePortrait.setImage(tempImage);
            playerCharList.setValue(DataStorage.getInstance().getOnlineTrackerPlayer().getCharacterName());

            playerRank.setImage(rankList[DataStorage.getInstance().getOnlineTrackerPlayer().getCurrentRank() - 1].getRankImage());

            double rankPts = DataStorage.getInstance().getOnlineTrackerPlayer().getRankPoints();

            if (rankPts == 0) {
                pointsLabel.setText("+/- " + Double.toString(rankPts));
            } else if (rankPts > 0) {
                pointsLabel.setText("+ " + Double.toString(rankPts));
            } else {
                pointsLabel.setText(Double.toString(rankPts));

            }
            currentRankLabel.setVisible(true);
            rankPointsLabel.setVisible(true);

            for (int i = 0; i < playerDataCache.size(); i++) {

                if (playerDataCache.get(i).getCharacterName().equals(DataStorage.getInstance().getOnlineTrackerPlayer().getCharacterName())) {
                    charSlotId = i;
                    System.out.println("Character is on slot: " + charSlotId);
                    System.out.println("Character: " + playerDataCache.get(charSlotId).getCharacterName());
                    break;
                }

            }

            //Enable all buttons
            initializeRankMeter();

            matchTypeBox.setVisible(true);
            matchResultBox.setVisible(true);
            opponentCharList.setVisible(true);
            opponentMatchRank.setVisible(true);
            matchAddBut.setVisible(true);

        } else {
            tempImage = new Image("/Resources/Images/FDPortraits/Unknown.png");
            pOnePortrait.setImage(tempImage);
        }

        blockRankList.setItems(matchRanksList);
        matchTypeBox.setItems(matchTypeList);
        matchTypeBox.getSelectionModel().selectFirst();
        matchResultBox.setItems(matchResultList);
        opponentMatchRank.setItems(matchRanksList);

        createRankImageList(blockRankList);
        createRankImageList(opponentMatchRank);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("CLOSING HOOK");
                writePlayerData();
            }
        }));

        /*
         if (DataStorage.getInstance().getOnlineTrackOpponent() != null) {
         tempImage = new Image("/Resources/Images/FDPortraits/" + DataStorage.getInstance().getOnlineTrackOpponent() + ".png");
         pOnePortrait.setImage(tempImage);

         } else {
         tempImage = new Image("/Resources/Images/FDPortraits/Unknown.png");
         pTwoPortrait.setImage(tempImage);
         }*/
    }

//------------------------------FXML METHODS----------------------------------\\    
    @FXML
    private void buttonClick(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();

        switch (text) {
            case "Fresh ranking":
                playerDataCache.add(new CharacterData((String) playerCharList.getSelectionModel().getSelectedItem(), 1, 0));
                charSlotId = (playerDataCache.size() - 1);
                initializeCharacterDataUI();
                blockScreen.setVisible(false);
                demoRank.setImage(null);
                break;
            case "Already started":
                blockInfoLabel.setText("Please select your character's current rank and the amount of\npoints that you currently have.");
                blockInfoLabel.setLayoutX(20);
                alreadyStartedBut.setVisible(false);
                freshStartBut.setVisible(false);
                blockRankList.setVisible(true);
                blockRankPointsInput.setVisible(true);
                blockConfirmBut.setVisible(true);
                break;
            case "Confirm":
                if (blockRankList.getSelectionModel().getSelectedIndex() >= 0) {
                    try {
                        int inputPts = Integer.parseInt(blockRankPointsInput.getText());

                        if (inputPts < 0) {
                            int rankDemoPts = (rankList[blockRankList.getSelectionModel().getSelectedIndex()].getAmountOfPointsDemotion() - 1);
                            System.out.println(-rankDemoPts);
                            if (inputPts >= -rankDemoPts) {
                                playerDataCache.add(new CharacterData((String) playerCharList.getSelectionModel().getSelectedItem(), (blockRankList.getSelectionModel().getSelectedIndex() + 1), inputPts));
                                charSlotId = (playerDataCache.size() - 1);
                                initializeCharacterDataUI();
                                System.out.println("Successfully added! Negative");
                                blockInfoLabel.setText("");
                                freshStartBut.setVisible(true);
                                alreadyStartedBut.setVisible(true);
                                blockRankList.setVisible(false);
                                blockRankPointsInput.setVisible(false);
                                blockConfirmBut.setVisible(false);
                                blockScreen.setVisible(false);

                            } else {
                                System.out.println("Input points are too low!");
                            }
                        } else if (inputPts >= 0) {
                            int rankPromoPts = (rankList[blockRankList.getSelectionModel().getSelectedIndex()].getAmountOfPointsPromotion() - 1); //Max points for rank is retrieved. Lowered by 1 to avoid errors.
                            System.out.println(rankPromoPts);
                            if (inputPts <= rankPromoPts) {
                                playerDataCache.add(new CharacterData((String) playerCharList.getSelectionModel().getSelectedItem(), (blockRankList.getSelectionModel().getSelectedIndex() + 1), inputPts));
                                charSlotId = (playerDataCache.size() - 1);
                                initializeCharacterDataUI();
                                System.out.println("Successfully added! Positive");
                                blockInfoLabel.setText("");
                                freshStartBut.setVisible(true);
                                alreadyStartedBut.setVisible(true);
                                blockRankList.setVisible(false);
                                blockRankPointsInput.setVisible(false);
                                blockConfirmBut.setVisible(false);
                                blockScreen.setVisible(false);

                            } else {
                                System.out.println("Input points are too high!");
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Not a number!");
                    } catch (Exception ex) {
                        System.out.println("An error occured with input!");
                    }
                } else {
                    System.out.println("Select a rank!");
                }
                System.out.println(blockRankList.getSelectionModel().getSelectedIndex());

                break;
            case "X":
                blockInfoLabel.setText("");
                freshStartBut.setVisible(true);
                alreadyStartedBut.setVisible(true);
                blockRankList.setVisible(false);
                blockRankPointsInput.setVisible(false);
                blockConfirmBut.setVisible(false);
                blockScreen.setVisible(false);
                blockInfoScreen.setVisible(false);
                break;
            case "Help":
                blockInfoScreen.setVisible(true);
                break;
            case "Add":
                addMatchResult();
                break;
            case "Search":
                Opponent opp = readOpponentData(oppSearchField.getText());
                if (opp != null) {
                    displayOpponentUI(opp);
                }
                break;
            case "Backwards":
                System.out.println("Backwards");

                if (playerCharList.getSelectionModel().getSelectedIndex() >= 0) {
                    //Add data to datastorage to be remembered

                    DataStorage.getInstance().setOnlineTrackerPlayer(new CharacterData(playerDataCache.get(charSlotId).getCharacterName(), playerDataCache.get(charSlotId).getCurrentRank(), playerDataCache.get(charSlotId).getRankPoints()));

                }
                DataStorage.getInstance().setOnlineTrackOpponentSearch(oppSearchField.getText());
                writePlayerData();
                MethodHandler.getInstance().sceneSwitch(event, "FXML" + DataStorage.getInstance().getPreviousBackwardsScene() + ".fxml");
                break;
            case "Home":
                System.out.println("Home");
                writePlayerData();
                DataStorage.getInstance().resetNavigationBarPages();
                DataStorage.getInstance().setOnlineTrackerPlayer(null);
                DataStorage.getInstance().setOnlineTrackOpponentSearch(null);
                writePlayerData();
                MethodHandler.getInstance().sceneSwitch(event, "FXMLMenuMainScreen.fxml");
                break;
            case "Forward":
                System.out.println("Forward");
                MethodHandler.getInstance().sceneSwitch(event, "FXML" + DataStorage.getInstance().getPreviousForwardScene() + ".fxml");
                break;
            default:
                break;
        }
    }

    @FXML
    public void onEnter(ActionEvent ae) {
        Opponent opp = readOpponentData(oppSearchField.getText());
        if (opp != null) {
            displayOpponentUI(opp);
        }
    }

    @FXML
    private void onMouseOver(MouseEvent event) {
        if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonOnHover");
        } else if (((Button) event.getSource()).getText().equals("Help") || ((Button) event.getSource()).getText().equals("X")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "program" + ((Button) event.getSource()).getText() + "ButtonOnHover");
        } else {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "program" + ((Button) event.getSource()).getText() + "ButtonOnHover");
        }
    }

    @FXML
    private void onMouseExit(MouseEvent event) {
        if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonNormal");
        } else if (((Button) event.getSource()).getText().equals("Help") || ((Button) event.getSource()).getText().equals("X")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "program" + ((Button) event.getSource()).getText() + "ButtonNormal");
        } else {
            MethodHandler.getInstance().onMouseExitMenuButton(event, "menu" + ((Button) event.getSource()).getText() + "ButtonNormal");
        }
    }

    @FXML
    private void refreshPlayer() {
        String selectedCharacter = (String) playerCharList.getSelectionModel().getSelectedItem();
        System.out.println("Selected character is: " + selectedCharacter);

        boolean characterExists = false;

        for (int i = 0; i < playerDataCache.size(); i++) {
            if (playerDataCache.get(i).getCharacterName().equals(selectedCharacter)) {
                charSlotId = i;
                System.out.println("Character is on slot: " + charSlotId);
                System.out.println("Character: " + playerDataCache.get(charSlotId).getCharacterName());
                characterExists = true;
                break;
            }
        }

        if (characterExists == false) {
            blockInfoLabel.setText("This character has not been used before.\nPlease select if it is a fresh start in the online mode for this character\n or if you have played online with this character before.\n\nIf you choose \"Already Started\" please select your current rank and the\n amount of points you think that you currently have. (If you do\n not know, leave at 0).");
            blockInfoLabel.setLayoutX(10);
            alreadyStartedBut.setVisible(true);
            freshStartBut.setVisible(true);
            blockScreen.setVisible(true);
        } else {
            initializeCharacterDataUI();
        }
    }

//-----------------------------NON-FXML METHODS-------------------------------\\
    //----------------------------UI METHODS------------------------------\\
    private void initializeCharacterDataUI() {
        //Set the current character & image
        tempImage = new Image("/Resources/Images/FDPortraits/" + playerDataCache.get(charSlotId).getCharacterName() + ".png");
        pOnePortrait.setImage(tempImage);

        //Set the current characters rank
        playerRank.setImage(rankList[(playerDataCache.get(charSlotId).getCurrentRank() - 1)].getRankImage());

        //Set the current rank points
        if (playerDataCache.get(charSlotId).getRankPoints() > 0) {
            pointsLabel.setText("+ " + playerDataCache.get(charSlotId).getRankPoints());

        } else if (playerDataCache.get(charSlotId).getRankPoints() < 0) {
            pointsLabel.setText(Double.toString(playerDataCache.get(charSlotId).getRankPoints()));
        } else {
            pointsLabel.setText("+/- " + Double.toString(playerDataCache.get(charSlotId).getRankPoints()));
        }

        System.out.println("Character is on slot: " + charSlotId);
        System.out.println("Character: " + playerDataCache.get(charSlotId).getCharacterName());

        //Make data visible
        currentRankLabel.setVisible(true);
        rankPointsLabel.setVisible(true);

        initializeRankMeter();

        matchTypeBox.setVisible(true);
        matchResultBox.setVisible(true);
        opponentCharList.setVisible(true);
        opponentMatchRank.setVisible(true);
        matchAddBut.setVisible(true);
    }

    private void initializeRankMeter() {
        double curRankPoints = playerDataCache.get(charSlotId).getRankPoints();
        int curRank = playerDataCache.get(charSlotId).getCurrentRank();

        double promoPoints = rankList[curRank].getAmountOfPointsPromotion();
        double curProgress = curRankPoints / promoPoints;

        System.out.println(curRankPoints + " / " + promoPoints);
        System.out.println(curProgress);

        curProgress = 0.5 + (curProgress / 2);

        animateBar(curProgress, 0);

        if (curRank == 1) {
            tempImage = rankList[curRank].getRankImage();
            promoRank.setImage(tempImage);

            reqDemoPtsLabel.setText("");
            reqPromoPtsLabel.setText("+" + Integer.toString(rankList[curRank - 1].getAmountOfPointsPromotion()));
        } else if (curRank == 36) {
            tempImage = rankList[(curRank - 2)].getRankImage();
            demoRank.setImage(tempImage);

            reqDemoPtsLabel.setText("-" + Integer.toString(rankList[(curRank - 1)].getAmountOfPointsDemotion()));
            reqPromoPtsLabel.setText("");
        } else {
            tempImage = rankList[(curRank - 2)].getRankImage();
            demoRank.setImage(tempImage);

            tempImage = rankList[curRank].getRankImage();
            promoRank.setImage(tempImage);

            reqDemoPtsLabel.setText("-" + Integer.toString(rankList[(curRank - 1)].getAmountOfPointsDemotion()));
            reqPromoPtsLabel.setText("+" + Integer.toString(rankList[(curRank - 1)].getAmountOfPointsPromotion()));
        }

        if (curRankPoints > 0) {
            curPtsLabel.setText("+" + Double.toString(curRankPoints));
        } else {
            curPtsLabel.setText(Double.toString(curRankPoints));
        }

        ptsBar.setVisible(true);
        reqDemoPtsLabel.setVisible(true);
        curPtsLabel.setVisible(true);
        reqPromoPtsLabel.setVisible(true);

    }

    private void updateRankMeter(int rankingStatus) {
        double curRankPoints = playerDataCache.get(charSlotId).getRankPoints();
        int curRank = playerDataCache.get(charSlotId).getCurrentRank();

        double promoPoints = rankList[curRank].getAmountOfPointsPromotion();
        double curProgress = curRankPoints / promoPoints;

        System.out.println(curRankPoints + " / " + promoPoints);
        System.out.println(curProgress);

        curProgress = 0.5 + ((curProgress) / 2);

        animateBar(curProgress, rankingStatus);

        if (curRank == 1) {
            tempImage = rankList[curRank].getRankImage();
            promoRank.setImage(tempImage);
            demoRank.setImage(null);

            reqDemoPtsLabel.setText("");
            reqPromoPtsLabel.setText("+" + Integer.toString(rankList[(curRank - 1)].getAmountOfPointsPromotion()));
        } else if (curRank == 36) {
            tempImage = rankList[(curRank - 2)].getRankImage();
            demoRank.setImage(tempImage);
            promoRank.setImage(null);

            reqDemoPtsLabel.setText("-" + Integer.toString(rankList[(curRank - 1)].getAmountOfPointsDemotion()));
            reqPromoPtsLabel.setText("");
        } else {
            tempImage = rankList[(curRank - 2)].getRankImage();
            demoRank.setImage(tempImage);

            tempImage = rankList[curRank].getRankImage();
            promoRank.setImage(tempImage);

            reqDemoPtsLabel.setText("-" + Integer.toString(rankList[(curRank - 1)].getAmountOfPointsDemotion()));
            reqPromoPtsLabel.setText("+" + Integer.toString(rankList[(curRank - 1)].getAmountOfPointsPromotion()));
        }

        if (curRankPoints > 0) {
            curPtsLabel.setText("+" + Double.toString(curRankPoints));
        } else {
            curPtsLabel.setText(Double.toString(curRankPoints));
        }

    }

    private void updatePlayerDataUI() {
        //Set the current characters rank
        playerRank.setImage(rankList[(playerDataCache.get(charSlotId).getCurrentRank() - 1)].getRankImage());

        //Set the current rank points
        if (playerDataCache.get(charSlotId).getRankPoints() > 0) {
            pointsLabel.setText("+ " + playerDataCache.get(charSlotId).getRankPoints());

        } else if (playerDataCache.get(charSlotId).getRankPoints() < 0) {
            pointsLabel.setText(Double.toString(playerDataCache.get(charSlotId).getRankPoints()));
        } else {
            pointsLabel.setText("+/- " + Double.toString(playerDataCache.get(charSlotId).getRankPoints()));
        }
    }

    private void displayOpponentUI(Opponent opp) {
        //Print top 5 characters with win/loss ratio
        OpponentCharacter[] topFive = opp.getTopFive();
        String topFiveString = "";
        String topFiveRatio = "";
        int i = 1;

        for (OpponentCharacter oppChar : topFive) {
            int wins = oppChar.getGamesWon();
            int losses = oppChar.getGamesLost();

            double wlRatio = 100 * ((double) wins / (double) (wins + losses));
            String roundedResult = String.format("%.0f", wlRatio);

            if (i == 1) {
                topFiveString = i + ". " + oppChar.getCharName();
                topFiveRatio = "W/L Ratio: " + roundedResult + "%";
            } else {
                topFiveString = topFiveString + "\n" + i + ". " + oppChar.getCharName();
                topFiveRatio = topFiveRatio + "\nW/L Ratio: " + roundedResult + "%";
            }
            i++;
        }

        int totalWins = opp.getWins();
        int totalLosses = opp.getLosses();

        //Start displaying the Opponent info
        tempImage = new Image("/Resources/Images/FDPortraits/" + topFive[0].getCharName() + ".png");
        pTwoPortrait.setImage(tempImage);
        opponentName.setText(opp.getName());
        drawChart(totalWins, totalLosses);

        double totalRatio = 100 * ((double) totalWins / (double) (totalWins + totalLosses));
        String roundedResult = String.format("%.0f", totalRatio);

        winsLosses.setVisible(true);
        winLossRatio.setText("W/L Ratio: " + roundedResult + "%");

        opponentTopCharsHead.setVisible(true);
        opponentTopChars.setText(topFiveString);
        opponentTopCharsRatio.setText(topFiveRatio);
    }

    //---------------------------CALCULATIONS-----------------------------\\
    private void addMatchResult() {
        if (oppSearchField.getText() != null && !oppSearchField.getText().contains(" ") && matchResultBox.getSelectionModel().isEmpty() == false && opponentCharList.getSelectionModel().isEmpty() == false && opponentMatchRank.getSelectionModel().isEmpty() == false) {
            String matchType = (String) matchTypeBox.getSelectionModel().getSelectedItem();
            String oppName = oppSearchField.getText();
            String result = (String) matchResultBox.getSelectionModel().getSelectedItem();
            String oppChar = (String) opponentCharList.getSelectionModel().getSelectedItem();
            int rank = opponentMatchRank.getSelectionModel().getSelectedIndex() + 1;

            if (matchType.equals("Ranked")) {
                calculateNewRankPoints(result, rank);
            }

            Opponent opp = readOpponentData(oppName);

            if (opp != null) {
                modifyOpponentData(opp, result, oppChar);
            } else {
                opp = new Opponent(oppName, 0, 0);
                ArrayList<OpponentCharacter> oppChars = new ArrayList<>();
                opp.setCharacters(oppChars);
                modifyOpponentData(opp, result, oppChar);
            }
        } else {
            System.out.println("ERROR!");
            System.out.println("Search field = " + oppSearchField.getText());
            System.out.println("MatchResultBox = " + matchResultBox.getSelectionModel().getSelectedItem());
            System.out.println("Opponent char = " + opponentCharList.getSelectionModel().getSelectedItem());
            System.out.println("Opponent Rank = " + opponentMatchRank.getSelectionModel().getSelectedIndex());
        }

    }

    private void calculateNewRankPoints(String result, int oppRank) {
        int tempPlayerRank = playerDataCache.get(charSlotId).getCurrentRank();
        int rankDifference = oppRank - tempPlayerRank;
        double updatedPts = playerDataCache.get(charSlotId).getRankPoints();
        int rankingStatus = 0;

        System.out.println(oppRank + " - " + tempPlayerRank + " = " + rankDifference);
        System.out.println("PTS BEFORE: " + updatedPts);
        boolean maxRankFullPts = false;
        boolean lowRankLowPts = false;

        if (tempPlayerRank == 36 && updatedPts >= rankList[tempPlayerRank - 1].getAmountOfPointsPromotion()) {
            maxRankFullPts = true;

        } else if (tempPlayerRank == 1 && updatedPts <= (-rankList[tempPlayerRank - 1].getAmountOfPointsPromotion())) {
            lowRankLowPts = true;
        }

        if (result.equals("Win") && maxRankFullPts == false) {

            if (rankDifference == 3) {
                updatedPts = updatedPts += 1;
            } else if (rankDifference == 2) {
                updatedPts = updatedPts += 2;
            } else if (rankDifference == 1) {
                updatedPts = updatedPts += 4;
            } else if (rankDifference == 0) {
                updatedPts = updatedPts += 8;
            } else if (rankDifference == -1) {
                updatedPts = updatedPts += 4;
            } else if (rankDifference == -2) {
                updatedPts = updatedPts += 2;
            }
            System.out.println("POINTS WIN: " + updatedPts);

            if (tempPlayerRank == 36 && updatedPts <= rankList[tempPlayerRank - 1].getAmountOfPointsPromotion()) {
                updatedPts = rankList[tempPlayerRank - 1].getAmountOfPointsPromotion();
            }

            if (updatedPts >= rankList[tempPlayerRank - 1].getAmountOfPointsPromotion() && tempPlayerRank != 36) {
                System.out.println("ENTERED PROMO IF STATEMENT!");
                updatedPts = updatedPts - rankList[(tempPlayerRank - 1)].getAmountOfPointsPromotion();
                playerDataCache.get(charSlotId).setCurrentRank((tempPlayerRank + 1));
                rankingStatus = 1;
            }

            playerDataCache.get(charSlotId).setRankPoints(updatedPts);

        } else if (result.equals("Loss") && lowRankLowPts == false) {

            if (rankDifference == 3) {
                updatedPts = updatedPts += -1;
            } else if (rankDifference == 2) {
                updatedPts = updatedPts += -2;
            } else if (rankDifference == 1) {
                updatedPts = updatedPts += -4;
            } else if (rankDifference == 0) {
                updatedPts = updatedPts += -8;
            } else if (rankDifference == -1) {
                updatedPts = updatedPts += -4;
            } else if (rankDifference == -2) {
                updatedPts = updatedPts += -2;
            }
            System.out.println("POINTS LOSS: " + updatedPts);

            if (tempPlayerRank == 1 && updatedPts <= (-rankList[tempPlayerRank - 1].getAmountOfPointsDemotion())) {
                updatedPts = (-rankList[tempPlayerRank - 1].getAmountOfPointsDemotion());
            }

            if (updatedPts <= (-rankList[tempPlayerRank - 1].getAmountOfPointsDemotion()) && tempPlayerRank != 1) {
                System.out.println("ENTERED DEMO IF STATEMENT!");
                System.out.println("CALCING: " + updatedPts + rankList[(tempPlayerRank - 1)].getAmountOfPointsDemotion());
                updatedPts = updatedPts + rankList[(tempPlayerRank - 1)].getAmountOfPointsDemotion();
                playerDataCache.get(charSlotId).setCurrentRank((tempPlayerRank - 1));
                rankingStatus = -1;
            }

            playerDataCache.get(charSlotId).setRankPoints(updatedPts);
        }

        updateRankMeter(rankingStatus);
        updatePlayerDataUI();
    }

    //Creates all of the ranks when the scene is loaded.
    private void createRanks() {
        try {
            for (int i = 1; i <= 36; i++) {                                       //Loops and creates 36 different ranks.
                Image rankImage = new Image("/Resources/Images/Ranks/" + i + ".png");   //Gets the correct image for the rank.
                matchRanksList.add(rankImage);

                int promoPoints = 0;
                int demoPoints = 0;

                if (i <= 3) {                                                     //Sets the correct promo/demo points depending on which ranknumber that is created.
                    promoPoints = 2;
                    demoPoints = 2;
                } else if (i >= 4 && i <= 7) {
                    promoPoints = 3;
                    demoPoints = 3;
                } else if (i >= 8 && i <= 11) {
                    promoPoints = 4;
                    demoPoints = 4;
                } else if (i >= 12 && i <= 15) {
                    promoPoints = 5;
                    demoPoints = 5;
                } else if (i >= 16 && i <= 19) {
                    promoPoints = 6;
                    demoPoints = 6;
                } else if (i >= 20 && i <= 23) {
                    promoPoints = 7;
                    demoPoints = 7;
                } else if (i >= 24 && i <= 27) {
                    promoPoints = 8;
                    demoPoints = 8;
                } else if (i >= 28 && i <= 31) {
                    promoPoints = 9;
                    demoPoints = 9;
                } else if (i >= 32 && i <= 34) {
                    promoPoints = 10;
                    demoPoints = 10;
                } else if (i >= 35 && i <= 36) {
                    promoPoints = 11;
                    demoPoints = 11;
                }

                Rank newRank = new Rank(i, rankImage, promoPoints * 8, demoPoints * 8);    //Creates the rank with the correct data.
                rankList[i - 1] = newRank;                                        //Puts the rank into the list at spot i-1 (0,1,2,3,4...) since i starts at 1.
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void modifyOpponentData(Opponent opp, String result, String oppChar) {
        ArrayList<OpponentCharacter> oppChars = opp.getCharacters();

        //Can be changed to another more code-saving computation later.
        boolean matched = false;

        for (OpponentCharacter oChar : oppChars) {
            if (oChar.getCharName().equals(oppChar)) {
                matched = true;

                oChar.setGamesPlayed((oChar.getGamesPlayed() + 1));
                if (result.equals("Win")) {
                    oChar.setGamesWon((oChar.getGamesWon() + 1));
                    opp.setWins((opp.getWins() + 1));
                } else {
                    oChar.setGamesLost((oChar.getGamesLost() + 1));
                    opp.setLosses((opp.getLosses() + 1));
                }
                break;
            }
        }

        if (matched == false) {
            if (result.equals("Win")) {
                oppChars.add(new OpponentCharacter(oppChar, 1, 1, 0));
                opp.setWins((opp.getWins() + 1));
            } else {
                oppChars.add(new OpponentCharacter(oppChar, 1, 0, 1));
                opp.setLosses((opp.getLosses() + 1));
            }

        }

        opp.setCharacters(oppChars);
        writeOpponentData(opp.getName(), opp);
        displayOpponentUI(opp);
    }

    //--------------------------ANIMATION & DRAWING------------------------\\
    private void animateBar(double curProgress, int rankingStatus) {

        Timeline timeline = new Timeline();
        Timeline timeline2 = new Timeline();
        Timeline timeline3 = new Timeline();

        KeyValue keyValueResetBar;
        KeyValue keyValueFillBar;
        KeyValue keyValueIncBar;

        keyValueIncBar = new KeyValue(ptsBar.progressProperty(), curProgress);
        KeyFrame keyFrame = new KeyFrame(new Duration(1000), keyValueIncBar);
        timeline3.getKeyFrames().add(keyFrame);

        if (rankingStatus == 1) {
            keyValueFillBar = new KeyValue(ptsBar.progressProperty(), 1);
            KeyFrame keyFrame3 = new KeyFrame(new Duration(1000), keyValueFillBar);

            keyValueResetBar = new KeyValue(ptsBar.progressProperty(), 0.5);
            KeyFrame keyFrame2 = new KeyFrame(new Duration(300), keyValueResetBar);

            timeline.getKeyFrames().add(keyFrame3);
            timeline2.getKeyFrames().add(keyFrame2);

            timeline.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    timeline2.play();
                }
            });

            timeline2.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    timeline3.play();
                }
            });

            timeline.play();

        } else if (rankingStatus == -1) {
            keyValueFillBar = new KeyValue(ptsBar.progressProperty(), 0);
            KeyFrame keyFrame3 = new KeyFrame(new Duration(1000), keyValueFillBar);

            keyValueResetBar = new KeyValue(ptsBar.progressProperty(), 0.5);
            KeyFrame keyFrame2 = new KeyFrame(new Duration(300), keyValueResetBar);

            timeline.getKeyFrames().add(keyFrame3);
            timeline2.getKeyFrames().add(keyFrame2);
            timeline.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    timeline2.play();
                }
            });

            timeline2.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    timeline3.play();
                }
            });

            timeline.play();

        } else {

            timeline3.play();
        }

        colorizeMeter(curProgress);

    }

    private void colorizeMeter(double progress) {
        if (progress <= 0.25) {
            ptsBar.setStyle("-fx-accent: #933b37;");

        } else if (progress < 0.75) {
            ptsBar.setStyle("-fx-accent: #a2ae20;");

        } else {
            ptsBar.setStyle("-fx-accent: #3b9337;");

        }
    }

    private void drawChart(int wins, int losses) {

        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Wins", wins),
                        new PieChart.Data("Losses", losses));

        winLossChart.setData(pieChartData);

        pieChartData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(data.getName(), ":\n ", data.pieValueProperty().intValue())
        )
        );
    }

    //-----------------------------FILE OPERATIONS-------------------------\\
    private void readPlayerData() {
        File playerFile = new File(playerDataPath.toString());

        if (playerFile.exists()) {
            try {
                InputStream in = Files.newInputStream(playerDataPath);
                ObjectInputStream ois = new ObjectInputStream(in);
                playerDataCache = (ArrayList<CharacterData>) ois.readObject();
                System.out.println(playerDataCache.size());

                for (int i = 0; i < playerDataCache.size(); i++) {
                    System.out.println("Index: " + i);
                    System.out.println(playerDataCache.get(i).getData());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void writePlayerData() {
        try {
            OutputStream fos = Files.newOutputStream(playerDataPath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<CharacterData>(playerDataCache));
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Opponent readOpponentData(String opponent) {
        opponentDataPath = Paths.get("./src/Resources/TextFiles/OnlineTracker/Opponents/" + opponent + ".txt");

        File f = new File(opponentDataPath.toString());
        Opponent opp = null;

        if (f.exists()) {
            try {
                List<String> readLines = Files.readAllLines(opponentDataPath);
                opp = new Opponent(readLines.get(0), Integer.parseInt(readLines.get(1)), Integer.parseInt(readLines.get(2)));
                ArrayList<OpponentCharacter> oppChars = new ArrayList<>();

                if (readLines.size() > 3) {
                    for (int i = 3; i < readLines.size(); i += 4) {
                        oppChars.add(new OpponentCharacter(readLines.get(i), Integer.parseInt((readLines.get(i + 1))), Integer.parseInt((readLines.get(i + 2))), Integer.parseInt((readLines.get(i + 3)))));
                    }

                }

                for (OpponentCharacter oppChar : oppChars) {
                    System.out.println("Created: " + oppChar.getCharName());
                    System.out.println(oppChar.getGamesPlayed());
                    System.out.println(oppChar.getGamesWon());
                    System.out.println(oppChar.getGamesLost());
                }

                opp.setCharacters(oppChars);
            } catch (IOException ex) {
                Logger.getLogger(ToolsOnlineTrackerController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //Opponent was not found!
            System.out.println("User not found!");
        }
        return opp;
    }

    private void writeOpponentData(String oppName, Opponent opp) {
        FileWriter fooWriter = null;

        try {
            File myFoo = new File("./src/Resources/TextFiles/OnlineTracker/Opponents/" + oppName + ".txt");
            fooWriter = new FileWriter(myFoo, false);

            String output = "";

            output = oppName + "\r\n" + opp.getWins() + "\r\n" + opp.getLosses();
            ArrayList<OpponentCharacter> oppChars = opp.getCharacters();

            for (int i = 0; i < oppChars.size(); i++) {
                output = output + "\r\n" + oppChars.get(i).getCharName() + "\r\n" + oppChars.get(i).getGamesPlayed() + "\r\n" + oppChars.get(i).getGamesWon() + "\r\n" + oppChars.get(i).getGamesLost();
            }

            fooWriter.write(output);

        } catch (IOException ex) {
            Logger.getLogger(ToolsOnlineTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fooWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(ToolsOnlineTrackerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void createRankImageList(ComboBox inputList) {
        //Displays the rank images in the list
        inputList.setCellFactory(listView -> new ListCell<Image>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Image item, boolean empty) {
                System.out.println("Entering updateItem");
                super.updateItem(item, empty);
                if (empty) {
                    System.out.println("NULL");
                    setText(null);
                    setGraphic(null);
                } else {

                    //System.out.println(friend);
                    System.out.println("SUPER:" + super.getIndex());
                    System.out.println(empty);
                    item = matchRanksList.get(super.getIndex());                  //new Image("/Resources/Ranks/" + friend + ".png");

                    imageView.setImage(item);
                    //imageView.setFitHeight(40);
                    imageView.setFitWidth(125);
                    //setText(friend);
                    setGraphic(imageView);
                }
            }
        });

        //Makes sure that the image of the selected rank is displayed when a value in the list is chosen.
        inputList.setButtonCell(new ListCell<Image>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    ImageView view = new ImageView(item);

                    //view.setFitHeight(40);
                    view.setFitWidth(125);
                    setText(null);
                    setGraphic(view);
                }
            }
        });
    }

}

//Check player rank points on back-forward loadup
//Match buttons not reappearing on back-forward loadup
//Can add opponent without a name
