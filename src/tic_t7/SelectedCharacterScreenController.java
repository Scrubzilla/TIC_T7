/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;

import displayclasses.Combo;
import displayclasses.Move;
import globalclasses.DataStorage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Nicklas
 */
public class SelectedCharacterScreenController implements Initializable {
//------------------------------FXML VARIABLES--------------------------------\\    

    @FXML
    private CheckBox normalMovesBox, pokesBox, goodMovesBox, launchersBox, tailspinMovesBox, grabsBox, specFrameBox, isPlusOnBlock, wallCombosBox, nonWallCombosBox, rageArtBox, rageDriveBox, powerCrushBox, homingBox;
    @FXML
    private ImageView pOnePortrait, pTwoPortrait;
    @FXML
    private Image tempImage;
    @FXML
    private ComboBox playerList, opponentList, frameBox;
    @FXML
    private TableView displayWindow;
    @FXML
    private TableColumn<Move, String> columnName, columnCommand, columnHitLevel, columnDamage, columnStartUpFrame, columnBlockFrame, columnHitFrame, columnCounterHitFrame;
    @FXML
    private TableColumn<Combo, String> columnLauncher, columnFollowUp, columnComboDamage, columnWall, columnComment;
    @FXML
    private Label punishersLabel, punishersForFrameLabel;
    @FXML
    private Button frameDataButton, punishersButton, jugglesButton;
    @FXML
    private AnchorPane blockInfoScreen;

//-----------------------------NON-FXML VARIABLES-----------------------------\\  
    private final ObservableList<String> characterList = FXCollections.observableArrayList("Akuma", "Alisa", "Asuka", "Bob", "Bryan", "Claudio", "Devil Jin", "Dragunov", "Eddy", "Eliza", "Feng", "Gigas", "Heihachi", "Hwoarang", "Jack-7", "Jin", "Josie", "Katarina", "Kazumi", "Kazuya", "King", "Kuma", "Lars", "Law", "Lee", "Leo", "Lili", "Lucky Chloe", "Master Raven", "Miguel", "Nina", "Panda", "Paul", "Shaheen", "Steve", "Xiaoyu", "Yoshimitsu");

    private final ObservableList<String> frameList = FXCollections.observableArrayList("-10", "-11", "-12", "-13", "-14", "-15", "-16", "-17");

    private final String currentSceneIs = "SelectedCharacterScreen";
    private final int menuTier = 3;

    private ObservableList<Move> playerMoveList = FXCollections.observableArrayList();
    private ObservableList<Move> opponentMoveList = FXCollections.observableArrayList();
    private ObservableList<Move> displayList = FXCollections.observableArrayList();
    private ObservableList<Combo> playerJuggleList = FXCollections.observableArrayList();
    private ObservableList<Combo> displayJuggleList = FXCollections.observableArrayList();

    private String[] playerPunishers = new String[8];
    private List<String> readLines;
    private Path filePath = Paths.get("");
    private boolean firstBoot = true;
    private boolean viewPlayerMoves = false;
    private boolean viewOpponentMoves = false;
    private boolean viewPlayerJuggles = false;

//-------------------------------ON START-UP----------------------------------\\
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DataStorage.getInstance().setCurrentMenuTierPage(menuTier, currentSceneIs);
        specFrameBox.setStyle("-fx-font-size: 12;");
        isPlusOnBlock.setStyle("-fx-font-size: 12;");

        //For testing
        System.out.println(DataStorage.getInstance().getCurrentMenuTier());
        for (int i = 0; i < 4; i++) {
            System.out.println(DataStorage.getInstance().getStringArray(i));
        }

        System.out.println("Start...Success");
        //End of testing 

        //Set lists
        playerList.setItems(characterList);
        playerList.setValue(DataStorage.getInstance().getPlayerOneCharacter());
        opponentList.setItems(characterList);

        if (DataStorage.getInstance().getPlayerTwoCharacter() != null) {
            opponentList.setValue(DataStorage.getInstance().getPlayerTwoCharacter());
        }

        frameBox.setItems(frameList);
        frameBox.setValue(DataStorage.getInstance().getSelectedFrame());

        //Do player one operations
        tempImage = new Image("/Resources/Images/FDPortraits/" + DataStorage.getInstance().getPlayerOneCharacter() + ".png");
        pOnePortrait.setImage(tempImage);

        createPlayerMoveList(DataStorage.getInstance().getPlayerOneCharacter());
        readPunishers(DataStorage.getInstance().getPlayerOneCharacter());
        createJuggles(DataStorage.getInstance().getPlayerOneCharacter());
        
        //Do player two operations
        if (DataStorage.getInstance().getPlayerTwoCharacter() != null) {
            tempImage = new Image("/Resources/Images/FDPortraits/" + DataStorage.getInstance().getPlayerTwoCharacter() + ".png");
            pTwoPortrait.setImage(tempImage);
            createOpponentMoveList(DataStorage.getInstance().getPlayerTwoCharacter());
            opponentList.setValue(DataStorage.getInstance().getPlayerTwoCharacter());
        } else {
            tempImage = new Image("/Resources/Images/FDPortraits/Unknown.png");
            pTwoPortrait.setImage(tempImage);
        }

        firstBoot = false;

        if (DataStorage.getInstance().getLastSelectedView().equals("Punishers")) {
            setSelectedButton("Punishers");
            viewPlayerMoves = false;
            viewOpponentMoves = true;
            viewPlayerJuggles = false;
            setCheckBoxes("Punishers");
            setColumns("Punishers");
            displayOpponentMoveList();
            displayWindow.setPlaceholder(new Label("Select an opponent first!"));
            punishersLabel.setVisible(true);
            punishersForFrameLabel.setVisible(true);
            updatePunisherLabel((String) frameBox.getValue(), frameBox.getSelectionModel().getSelectedIndex());

        } else if (DataStorage.getInstance().getLastSelectedView().equals("Juggles")) {
            setSelectedButton("Juggles");
            viewPlayerJuggles = true;
            viewOpponentMoves = false;
            viewPlayerMoves = false;
            setCheckBoxes("Juggles");
            setColumns("Juggles");
            displayJuggles();
            punishersLabel.setVisible(false);
            punishersForFrameLabel.setVisible(false);
        } else {
            setSelectedButton("Frame data");
            viewOpponentMoves = false;
            viewPlayerMoves = true;
            viewPlayerJuggles = false;
            setCheckBoxes("Frame data");
            setColumns("Frame data");
            displayMoveList();
            punishersLabel.setVisible(false);
            punishersForFrameLabel.setVisible(false);
        }

    }

//--------------------------------FXML METHODS--------------------------------\\    
    @FXML
    private void buttonClick(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();

        switch (text) {
            case "Frame data":
                setSelectedButton(text);
                setColumns(text);
                viewOpponentMoves = false;
                viewPlayerMoves = true;
                viewPlayerJuggles = false;
                setCheckBoxes(text);
                displayMoveList();

                DataStorage.getInstance().setLastSelectedView(text);
                punishersLabel.setVisible(false);
                punishersForFrameLabel.setVisible(false);
                break;
            case "Punishers":
                setSelectedButton(text);
                setColumns(text);
                viewPlayerMoves = false;
                viewOpponentMoves = true;
                viewPlayerJuggles = false;
                setCheckBoxes(text);
                displayWindow.setPlaceholder(new Label("Select an opponent first!"));
                displayOpponentMoveList();
                updatePunisherLabel((String) frameBox.getValue(), frameBox.getSelectionModel().getSelectedIndex());
                DataStorage.getInstance().setLastSelectedView(text);
                punishersLabel.setVisible(true);
                punishersForFrameLabel.setVisible(true);
                break;
            case "Juggles":
                setSelectedButton(text);
                setColumns(text);
                viewPlayerMoves = false;
                viewOpponentMoves = false;
                viewPlayerJuggles = true;
                setCheckBoxes(text);
                displayWindow.setPlaceholder(new Label("No juggles found!"));
                displayJuggles();
                DataStorage.getInstance().setLastSelectedView("Juggles");
                punishersLabel.setVisible(false);
                punishersForFrameLabel.setVisible(false);
                break;
            case "Help":
                blockInfoScreen.setVisible(true);
                break;
            case "X":
                blockInfoScreen.setVisible(false);
                break;
            case "Backwards":
                System.out.println("Backwards");
                MethodHandler.getInstance().sceneSwitch(event, "FXML" + DataStorage.getInstance().getPreviousBackwardsScene() + ".fxml");
                break;
            case "Home":
                System.out.println("Home");
                DataStorage.getInstance().resetNavigationBarPages();
                DataStorage.getInstance().setPlayerOneCharacter(null);
                DataStorage.getInstance().setPlayerTwoCharacter(null);
                DataStorage.getInstance().setSelectedFrame("-10");
                DataStorage.getInstance().setLastSelectedView("Home");
                firstBoot = true;
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
    private void onMouseOver(MouseEvent event) {
        if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonOnHover");
        } else if (((Button) event.getSource()).getText().equals("Help") || ((Button) event.getSource()).getText().equals("Legend") || ((Button) event.getSource()).getText().equals("X")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "program" + ((Button) event.getSource()).getText() + "ButtonOnHover");
        } else {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "menu" + ((Button) event.getSource()).getText() + "ButtonOnHover");
        }
    }

    @FXML
    private void onMouseExit(MouseEvent event) {
        if (((Button) event.getSource()).getText().equals("Backwards") || ((Button) event.getSource()).getText().equals("Home") || ((Button) event.getSource()).getText().equals("Forward")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "navigationBar" + ((Button) event.getSource()).getText() + "ButtonNormal");
        } else if (((Button) event.getSource()).getText().equals("Help") || ((Button) event.getSource()).getText().equals("Legend") || ((Button) event.getSource()).getText().equals("X")) {
            MethodHandler.getInstance().onMouseOverMenuButton(event, "program" + ((Button) event.getSource()).getText() + "ButtonNormal");
        } else {
            MethodHandler.getInstance().onMouseExitMenuButton(event, "menu" + ((Button) event.getSource()).getText() + "ButtonNormal");
        }
    }

    @FXML
    private void setSelectedButton(String buttonText) {
        if (buttonText.equals("Frame data")) {
            frameDataButton.setId("buttonSelected");
            punishersButton.setId("button");
            jugglesButton.setId("button");
        } else if (buttonText.equals("Punishers")) {
            frameDataButton.setId("button");
            punishersButton.setId("buttonSelected");
            jugglesButton.setId("button");
        } else if (buttonText.equals("Juggles")) {
            frameDataButton.setId("button");
            punishersButton.setId("button");
            jugglesButton.setId("buttonSelected");
        }
    }

    @FXML
    private void openPopUpWindow() throws IOException {
        if (DataStorage.getInstance().getPopUpCount() == false) {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLPopUpWindow.fxml"));
            Scene secondScene = new Scene(root);
            Stage secondStage = new Stage();

            secondStage.setScene(secondScene);
            secondStage.setResizable(false);
            secondStage.sizeToScene();
            secondStage.setTitle("Legend");

            Image programIcon = new Image("/Resources/Images/ProgramImages/logo.png");
            secondStage.getIcons().setAll(programIcon);

            //Set position of second window, related to primary window.
            secondStage.setX(250);
            secondStage.setY(100);

            secondStage.show();

            secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.out.println("Closing pop-up window.");
                    DataStorage.getInstance().setPopUpCount(false);

                }
            });

            DataStorage.getInstance().setPopUpCount(true);
        }
    }

    @FXML
    private void refreshPlayer() {
        if (firstBoot == false) {
            String selectedPlayer = (String) playerList.getSelectionModel().getSelectedItem();
            System.out.println(selectedPlayer);

            tempImage = new Image("/Resources/Images/FDPortraits/" + selectedPlayer + ".png");
            DataStorage.getInstance().setPlayerOneCharacter(selectedPlayer);
            pOnePortrait.setImage(tempImage);

            createPlayerMoveList(selectedPlayer);
            readPunishers(selectedPlayer);
            createJuggles(selectedPlayer);

            if (viewPlayerMoves == true) {
                displayMoveList();
            } else if (viewPlayerJuggles == true) {
                displayJuggles();
            }
        }
    }

    @FXML
    private void refreshOpponent() {
        if (firstBoot == false) {
            String selectedOpponent = (String) opponentList.getSelectionModel().getSelectedItem();
            System.out.println(selectedOpponent);

            tempImage = new Image("/Resources/Images/FDPortraits/" + selectedOpponent + ".png");
            DataStorage.getInstance().setPlayerTwoCharacter(selectedOpponent);
            pTwoPortrait.setImage(tempImage);

            createOpponentMoveList(selectedOpponent);

            if (viewOpponentMoves == true) {
                displayOpponentMoveList();
            }
        }
    }

    @FXML
    private void displayMoveList() {
        if (viewPlayerMoves == true) {
            displayList.clear();
            displayJuggleList.clear();

            for (int i = 0; i < playerMoveList.size(); i++) {
                //System.out.println(playerMoveList.get(i).getPrimaryAttr());
                if (isPlusOnBlock.isSelected() == true) {
                    if (playerMoveList.get(i).getBlockFrame() >= 0) {
                        if (normalMovesBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Normal")) {
                            displayList.add(playerMoveList.get(i));
                            //    System.out.println("Added: " + displayList.get(i).getCommand());
                        } else if (pokesBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Poke")) {
                            displayList.add(playerMoveList.get(i));
                            //  System.out.println("Added: " + displayList.get(i).getCommand());
                        } else if (goodMovesBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Good move")) {
                            displayList.add(playerMoveList.get(i));
                            //System.out.println("Added: " + displayList.get(i).getCommand());
                        } else if (launchersBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Launcher")) {
                            displayList.add(playerMoveList.get(i));
                            //System.out.println("Added: " + displayList.get(i).getCommand());
                        } else if (tailspinMovesBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Tail spin")) {
                            displayList.add(playerMoveList.get(i));
                            //System.out.println("Added: " + displayList.get(i).getCommand());
                        } else if (grabsBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Grab")) {
                            displayList.add(playerMoveList.get(i));
                            //System.out.println("Added: " + displayList.get(i).getCommand());
                        } else if (powerCrushBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Power crush")) {
                            displayList.add(playerMoveList.get(i));
                            //System.out.println("Added: " + displayList.get(i).getCommand());
                        } else if (rageDriveBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Rage drive")) {
                            displayList.add(playerMoveList.get(i));
                            //System.out.println("Added: " + displayList.get(i).getCommand());
                        } else if (rageArtBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Rage art")) {
                            displayList.add(playerMoveList.get(i));
                            //System.out.println("Added: " + displayList.get(i).getCommand());
                        } else if (homingBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Homing")) {
                            displayList.add(playerMoveList.get(i));
                            //System.out.println("Added: " + displayList.get(i).getCommand());
                        }

                    }
                } else if (normalMovesBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Normal")) {
                    displayList.add(playerMoveList.get(i));
                    //    System.out.println("Added: " + displayList.get(i).getCommand());
                } else if (pokesBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Poke")) {
                    displayList.add(playerMoveList.get(i));
                    //  System.out.println("Added: " + displayList.get(i).getCommand());
                } else if (goodMovesBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Good move")) {
                    displayList.add(playerMoveList.get(i));
                    //System.out.println("Added: " + displayList.get(i).getCommand());
                } else if (launchersBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Launcher")) {
                    displayList.add(playerMoveList.get(i));
                    //System.out.println("Added: " + displayList.get(i).getCommand());
                } else if (tailspinMovesBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Tail spin")) {
                    displayList.add(playerMoveList.get(i));
                    //System.out.println("Added: " + displayList.get(i).getCommand());
                } else if (grabsBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Grab")) {
                    displayList.add(playerMoveList.get(i));
                    //System.out.println("Added: " + displayList.get(i).getCommand());
                } else if (powerCrushBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Power crush")) {
                    displayList.add(playerMoveList.get(i));
                    //System.out.println("Added: " + displayList.get(i).getCommand());
                } else if (rageDriveBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Rage drive")) {
                    displayList.add(playerMoveList.get(i));
                    //System.out.println("Added: " + displayList.get(i).getCommand());
                } else if (rageArtBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Rage art")) {
                    displayList.add(playerMoveList.get(i));
                    //System.out.println("Added: " + displayList.get(i).getCommand());
                } else if (homingBox.isSelected() && playerMoveList.get(i).getPrimaryAttr().contains("Homing")) {
                    displayList.add(playerMoveList.get(i));
                    //System.out.println("Added: " + displayList.get(i).getCommand());
                }

                displayWindow.setPlaceholder(new Label("There are no moves to display!"));

            }

            columnCommand.setCellValueFactory(new PropertyValueFactory<>("command"));

            columnHitLevel.setCellValueFactory(new PropertyValueFactory<>("hitLevel"));
            columnDamage.setCellValueFactory(new PropertyValueFactory<>("damage"));
            columnStartUpFrame.setCellValueFactory(new PropertyValueFactory<>("startUpFrame"));
            columnBlockFrame.setCellValueFactory(new PropertyValueFactory<>("displayBlockFrame"));
            columnHitFrame.setCellValueFactory(new PropertyValueFactory<>("hitFrame"));
            columnCounterHitFrame.setCellValueFactory(new PropertyValueFactory<>("counterHitFrame"));

            displayWindow.setItems(displayList);

            //Testing for gif-popups
            /*
            displayWindow.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {

                }
            });*/
        }
    }

//--------------------------NON-FXML METHODS----------------------------------\\
    private void createPlayerMoveList(String selectedPlayer) {
        playerMoveList.clear();
        filePath = Paths.get("./src/Resources/TextFiles/Movelists", selectedPlayer + ".cha");

        try {
            readLines = Files.readAllLines(filePath);
        } catch (IOException ex) {
            System.out.println("Error!");
        }

        if (playerMoveList.isEmpty()) {
            for (int i = 0; i < readLines.size(); i += 10) {
                playerMoveList.add(new Move("", readLines.get(i), readLines.get(i + 1), readLines.get(i + 2), readLines.get(i + 3), Integer.parseInt(readLines.get(i + 4)), readLines.get(i + 5), readLines.get(i + 6), readLines.get(i + 7), readLines.get(i + 8)));
            }

        }

        for (int j = 0; j < playerMoveList.size(); j++) {
            System.out.println("Name: " + playerMoveList.get(j).getCharacterName());
            System.out.println("Command: " + playerMoveList.get(j).getCommand());
            System.out.println("Hit Level: " + playerMoveList.get(j).getHitLevel());
            System.out.println("Damage: " + playerMoveList.get(j).getDamage());
            System.out.println("Start Up Frame: " + playerMoveList.get(j).getStartUpFrame());
            System.out.println("Block Frame: " + playerMoveList.get(j).getBlockFrame());
            System.out.println("Display Block Frame: " + playerMoveList.get(j).getDisplayBlockFrame());
            System.out.println("Hit Frame: " + playerMoveList.get(j).getHitFrame());
            System.out.println("Counter Hit Frame: " + playerMoveList.get(j).getCounterHitFrame());
            System.out.println("Primary Attribute: " + playerMoveList.get(j).getPrimaryAttr());
        }
    }

    @FXML
    private void displayOpponentMoveList() {
        if (viewOpponentMoves == true) {
            displayList.clear();
            displayJuggleList.clear();

            if (specFrameBox.isSelected() == true) {
                for (int i = 0; i < opponentMoveList.size(); i++) {
                    //System.out.println(playerMoveList.get(i).getPrimaryAttr());
                    if (opponentMoveList.get(i).getBlockFrame() == Integer.parseInt((String) frameBox.getValue())) {
                        displayList.add(opponentMoveList.get(i));
                    }
                }
            } else {
                for (int i = 0; i < opponentMoveList.size(); i++) {
                    //System.out.println(playerMoveList.get(i).getPrimaryAttr());
                    if (opponentMoveList.get(i).getBlockFrame() <= Integer.parseInt((String) frameBox.getValue())) {
                        displayList.add(opponentMoveList.get(i));
                    }
                }
            }

            columnCommand.setCellValueFactory(new PropertyValueFactory<>("command"));
            columnHitLevel.setCellValueFactory(new PropertyValueFactory<>("hitLevel"));
            columnDamage.setCellValueFactory(new PropertyValueFactory<>("damage"));
            columnStartUpFrame.setCellValueFactory(new PropertyValueFactory<>("startUpFrame"));
            columnBlockFrame.setCellValueFactory(new PropertyValueFactory<>("displayBlockFrame"));
            columnHitFrame.setCellValueFactory(new PropertyValueFactory<>("hitFrame"));
            columnCounterHitFrame.setCellValueFactory(new PropertyValueFactory<>("counterHitFrame"));

            displayWindow.setItems(displayList);
            if (opponentList.getSelectionModel().getSelectedItem() == null) {
                displayWindow.setPlaceholder(new Label("Select an opponent first!"));

            } else if (displayList.isEmpty()) {
                displayWindow.setPlaceholder(new Label("There are no moves to display!"));
            }
        }
    }

    private void createOpponentMoveList(String selectedOpponent) {
        opponentMoveList.clear();
        filePath = Paths.get("./src/Resources/TextFiles/Movelists", selectedOpponent + ".cha");

        try {
            readLines = Files.readAllLines(filePath);
        } catch (IOException ex) {
            System.out.println("Error!");
        }

        if (opponentMoveList.isEmpty()) {
            for (int i = 0; i < readLines.size(); i += 10) {
                opponentMoveList.add(new Move("", readLines.get(i), readLines.get(i + 1), readLines.get(i + 2), readLines.get(i + 3), Integer.parseInt(readLines.get(i + 4)), readLines.get(i + 5), readLines.get(i + 6), readLines.get(i + 7), readLines.get(i + 8)));
            }
        }

        for (int j = 0; j < opponentMoveList.size(); j++) {
            System.out.println("Name: " + opponentMoveList.get(j).getCharacterName());
            System.out.println("Command: " + opponentMoveList.get(j).getCommand());
            System.out.println("Hit Level: " + opponentMoveList.get(j).getHitLevel());
            System.out.println("Damage: " + opponentMoveList.get(j).getDamage());
            System.out.println("Start Up Frame: " + opponentMoveList.get(j).getStartUpFrame());
            System.out.println("Block Frame: " + opponentMoveList.get(j).getBlockFrame());
            System.out.println("Display Block Frame: " + opponentMoveList.get(j).getDisplayBlockFrame());
            System.out.println("Hit Frame: " + opponentMoveList.get(j).getHitFrame());
            System.out.println("Counter Hit Frame: " + opponentMoveList.get(j).getCounterHitFrame());
            System.out.println("Primary Attribute: " + opponentMoveList.get(j).getPrimaryAttr());
        }
    }

    @FXML
    private void updateSelectedFrame() {
        DataStorage.getInstance().setSelectedFrame((String) frameBox.getValue());

        if (viewOpponentMoves == true) {
            displayOpponentMoveList();
            updatePunisherLabel((String) frameBox.getValue(), frameBox.getSelectionModel().getSelectedIndex());
        }
    }

    private void readPunishers(String selectedPlayer) {
        try {
            System.out.println("Read punishers");
            filePath = Paths.get("./src/Resources/TextFiles/Punishers", selectedPlayer + ".txt");
            readLines = Files.readAllLines(filePath);

            for (int i = 0; i < readLines.size(); i++) {
                System.out.println("Read punisher " + readLines.get(i));
            }

            for (int i = 0; i < readLines.size(); i++) {
                String[] splittedPunisher = readLines.get(i).split(":");

                String combinedPunishers = "";

                for (int j = 0; j < splittedPunisher.length; j++) {
                    if (j == 0) {
                        combinedPunishers = splittedPunisher[j];
                    } else {
                        combinedPunishers = combinedPunishers + " or " + splittedPunisher[j];
                    }

                }

                playerPunishers[i] = combinedPunishers;

            }

            for (int i = 0; i < playerPunishers.length; i++) {
                System.out.println(playerPunishers[i]);
            }

        } catch (IOException io) {
            System.out.println("File not found.");
        }
    }

    @FXML
    private void displayJuggles() {
        if (viewPlayerJuggles == true) {
            displayJuggleList.clear();
            displayList.clear();

            for (int i = 0; i < playerJuggleList.size(); i++) {
                if (wallCombosBox.isSelected() == true && playerJuggleList.get(i).getWall().equals("W!")) {
                    displayJuggleList.add(playerJuggleList.get(i));
                } else if (nonWallCombosBox.isSelected() == true && playerJuggleList.get(i).getWall().equals("non-W!")) {
                    displayJuggleList.add(playerJuggleList.get(i));
                }
            }

            columnLauncher.setCellValueFactory(new PropertyValueFactory<>("launcher"));
            columnFollowUp.setCellValueFactory(new PropertyValueFactory<>("followUp"));
            columnComboDamage.setCellValueFactory(new PropertyValueFactory<>("damage"));
            columnWall.setCellValueFactory(new PropertyValueFactory<>("wall"));
            columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));

            displayWindow.setItems(displayJuggleList);

            if (displayList.isEmpty()) {
                displayWindow.setPlaceholder(new Label("There are no juggles to display!"));
            }
        }
    }

    private void createJuggles(String selectedPlayer) {
        playerJuggleList.clear();
        filePath = Paths.get("./src/Resources/TextFiles/Juggles/", selectedPlayer + ".txt");

        try {
            readLines = Files.readAllLines(filePath);

            for (int i = 0; i < readLines.size(); i++) {
                System.out.println(readLines.get(i));
            }
        } catch (IOException ex) {
            System.out.println("Error!");
        }

        for (int i = 0; i < readLines.size(); i += 6) {
            playerJuggleList.add(new Combo(readLines.get(i), readLines.get(i + 1), readLines.get(i + 2), readLines.get(i + 3), readLines.get(i + 4)));
        }

        for (int j = 0; j < playerJuggleList.size(); j++) {
            System.out.println("Launcher: " + playerJuggleList.get(j).getLauncher());
            System.out.println("Follow-up: " + playerJuggleList.get(j).getFollowUp());
            System.out.println("Damage: " + playerJuggleList.get(j).getDamage());
            System.out.println("Wall: " + playerJuggleList.get(j).getWall());
            System.out.println("Comment: " + playerJuggleList.get(j).getComment());
        }
    }

    @FXML
    private void updatePunisherLabel(String frameValue, int frameIndex) {
        punishersLabel.setText("Your punishers for " + frameValue + " frames are: ");
        punishersForFrameLabel.setText(playerPunishers[frameIndex]);
    }

    @FXML
    private void setColumns(String buttonSelected) {
        switch (buttonSelected) {
            case "Frame data":
                //For Frame data and punishers
                columnCommand.setVisible(true);
                columnHitLevel.setVisible(true);
                columnDamage.setVisible(true);
                columnStartUpFrame.setVisible(true);
                columnBlockFrame.setVisible(true);
                columnHitFrame.setVisible(true);
                columnCounterHitFrame.setVisible(true);

                //For juggles
                columnLauncher.setVisible(false);
                columnFollowUp.setVisible(false);
                columnComboDamage.setVisible(false);
                columnWall.setVisible(false);
                columnComment.setVisible(false);
                break;
            case "Punishers":

                //For Frame data and punishers
                columnCommand.setVisible(true);
                columnHitLevel.setVisible(true);
                columnDamage.setVisible(true);
                columnStartUpFrame.setVisible(true);
                columnBlockFrame.setVisible(true);
                columnHitFrame.setVisible(true);
                columnCounterHitFrame.setVisible(true);

                //For juggles
                columnLauncher.setVisible(false);
                columnFollowUp.setVisible(false);
                columnComboDamage.setVisible(false);
                columnWall.setVisible(false);
                columnComment.setVisible(false);
                break;
            case "Juggles":
                //For Frame data and punishers
                columnCommand.setVisible(false);
                columnHitLevel.setVisible(false);
                columnDamage.setVisible(false);
                columnStartUpFrame.setVisible(false);
                columnBlockFrame.setVisible(false);
                columnHitFrame.setVisible(false);
                columnCounterHitFrame.setVisible(false);

                //For juggles
                columnLauncher.setVisible(true);
                columnFollowUp.setVisible(true);
                columnComboDamage.setVisible(true);
                columnWall.setVisible(true);
                columnComment.setVisible(true);
                break;
            default:
                break;
        }
    }

    private void setCheckBoxes(String buttonPressed) {
        if (buttonPressed.equals("Frame data")) {
            normalMovesBox.setVisible(true);
            pokesBox.setVisible(true);
            goodMovesBox.setVisible(true);
            launchersBox.setVisible(true);
            tailspinMovesBox.setVisible(true);
            grabsBox.setVisible(true);
            powerCrushBox.setVisible(true);
            rageDriveBox.setVisible(true);
            rageArtBox.setVisible(true);
            homingBox.setVisible(true);
            wallCombosBox.setVisible(false);
            nonWallCombosBox.setVisible(false);

        } else if (buttonPressed.equals("Punishers")) {
            normalMovesBox.setVisible(false);
            pokesBox.setVisible(false);
            goodMovesBox.setVisible(false);
            launchersBox.setVisible(false);
            tailspinMovesBox.setVisible(false);
            grabsBox.setVisible(false);
            powerCrushBox.setVisible(false);
            rageDriveBox.setVisible(false);
            rageArtBox.setVisible(false);
            homingBox.setVisible(false);
            wallCombosBox.setVisible(false);
            nonWallCombosBox.setVisible(false);

        } else if (buttonPressed.equals("Juggles")) {
            normalMovesBox.setVisible(false);
            pokesBox.setVisible(false);
            goodMovesBox.setVisible(false);
            launchersBox.setVisible(false);
            tailspinMovesBox.setVisible(false);
            grabsBox.setVisible(false);
            powerCrushBox.setVisible(false);
            rageDriveBox.setVisible(false);
            rageArtBox.setVisible(false);
            homingBox.setVisible(false);

            wallCombosBox.setVisible(true);
            nonWallCombosBox.setVisible(true);
        }

    }

}
