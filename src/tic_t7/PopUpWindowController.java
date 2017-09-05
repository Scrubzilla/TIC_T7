/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic_t7;

import displayclasses.Move;
import displayclasses.Notation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Nicklas
 */
public class PopUpWindowController implements Initializable {

    @FXML
    private TableView commandWindow, notationWindow, groundedPosWindow, movePropsWindow, hitRangeWIndow, comboSpecAbbWindow, charSpecAbbWindow;
    @FXML
    private TableColumn<Notation, String> columnNotation, columnMeaning, columnExtra, columnNotation1, columnMeaning1, columnExtra1, columnNotation2, columnMeaning2, columnExtra2, columnNotation3, columnMeaning3, columnExtra3, columnNotation4, columnMeaning4, columnExtra4, columnNotation5, columnMeaning5, columnExtra5, columnNotation6, columnMeaning6, columnExtra6;

    private ObservableList<Notation> commandList = FXCollections.observableArrayList();
    private ObservableList<Notation> notationList = FXCollections.observableArrayList();
    private ObservableList<Notation> groundedPosList = FXCollections.observableArrayList();
    private ObservableList<Notation> movePropsList = FXCollections.observableArrayList();
    private ObservableList<Notation> hitRangeList = FXCollections.observableArrayList();
    private ObservableList<Notation> comboSpecList = FXCollections.observableArrayList();
    private ObservableList<Notation> charSpecList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addNotations();
    }

    private void addNotations() {
        commandList.add(new Notation("1", "left punch", ""));
        commandList.add(new Notation("2", "right punch", ""));
        commandList.add(new Notation("3", "left kick", ""));
        commandList.add(new Notation("4", "right kick", ""));
        commandList.add(new Notation("f", "tap forward", ""));
        commandList.add(new Notation("F", "hold forward", ""));
        commandList.add(new Notation("d", "tap down", ""));
        commandList.add(new Notation("D", "hold down", ""));
        commandList.add(new Notation("b", "tap back", ""));
        commandList.add(new Notation("B", "hold back", ""));
        commandList.add(new Notation("u", "tap up", ""));
        commandList.add(new Notation("U", "hold up", ""));
        commandList.add(new Notation("d/f", "tap down forward", ""));
        commandList.add(new Notation("D/F", "hold down forward", ""));
        commandList.add(new Notation("d/b", "tap down back", ""));
        commandList.add(new Notation("D/B", "hold down back", ""));
        commandList.add(new Notation("u/f", "tap up forward", ""));
        commandList.add(new Notation("U/F", "hold up forward", ""));
        commandList.add(new Notation("u/b", "tap up back", ""));
        commandList.add(new Notation("U/B", "hold up back", ""));
        commandList.add(new Notation("qcf", "quarter circle forward", ""));
        commandList.add(new Notation("qcb", "quarter circle back", ""));
        commandList.add(new Notation("hcf", "half circle forward", ""));
        commandList.add(new Notation("hcb", "half circle back", ""));

        notationList.add(new Notation("FC", "full crouch animation", ""));
        notationList.add(new Notation("WS", "while standing up", ""));
        notationList.add(new Notation("N", "joystick in neutral", ""));
        notationList.add(new Notation("WR", "while running", ""));
        notationList.add(new Notation("SS", "side step either way", ""));
        notationList.add(new Notation("SSL", "side step to left", ""));
        notationList.add(new Notation("SSR", "side step to right", ""));
        notationList.add(new Notation("[ ]", "optional command", ""));
        notationList.add(new Notation(",", "followed by", ""));
        notationList.add(new Notation("~", "immediately after", ""));
        notationList.add(new Notation("+", "at the same time", ""));
        notationList.add(new Notation("( _ )", "or", ""));
        notationList.add(new Notation("<", "delayed input", ""));
        notationList.add(new Notation("=", "next in sequence", ""));
        notationList.add(new Notation("°", "push and hold button", ""));
        notationList.add(new Notation(":", "requires just frame input", ""));

        groundedPosList.add(new Notation("PLD", "play dead position", "face up & feet away"));
        groundedPosList.add(new Notation("KND", "knockdown position", "face up & feet towards"));
        groundedPosList.add(new Notation("SLD", "slide position", "face down & feet away"));
        groundedPosList.add(new Notation("FCD", "face down position", "face down & feet towards"));

        movePropsList.add(new Notation("RA!", "This move is a rage art.", ""));
        movePropsList.add(new Notation("RD!", "This move is a rage drive.", ""));
        movePropsList.add(new Notation("PC!", "This move is a power crush move.", ""));
        movePropsList.add(new Notation("S!", "this move cause spin state", ""));
        movePropsList.add(new Notation("BT", "your back turned to the opponent", ""));
        movePropsList.add(new Notation("FF", "face forward towards opponent", ""));
        movePropsList.add(new Notation("OB", "forces opponent's back to face you", ""));
        movePropsList.add(new Notation("OC", "forces opponent into crouch", ""));
        movePropsList.add(new Notation("OS", "forces opponent's side to face you", ""));
        movePropsList.add(new Notation("JG", "juggle starter", ""));
        movePropsList.add(new Notation("BN", "bounce juggle starter", ""));
        movePropsList.add(new Notation("RC", "recover crouching after a move", ""));
        movePropsList.add(new Notation("RCj", "joystick modifier, need to hold D during the move to RC", ""));
        movePropsList.add(new Notation("CH", "requires a counter hit", ""));
        movePropsList.add(new Notation("DS", "double over stun", "tap f to escape in most cases, you can usually launch opponent"));
        movePropsList.add(new Notation("FS", "fall back stun", "tap f to escape in most cases, you can usually launch opponent"));
        movePropsList.add(new Notation("MS", "minor stun", "various animations, in most cases doesn't lead to guaranteed hits"));
        movePropsList.add(new Notation("KS", "kneel stun", "opponent is forced onto one knee, usually does not act as a combo starter"));
        movePropsList.add(new Notation("CS", "crumple stun", "opponent crumples to the ground in front of you right away, usually acts as a combo starter"));
        movePropsList.add(new Notation("CF", "crumple fall", "opponent stay briefly upright before slumping to the ground, usually acts a combo starter"));
        movePropsList.add(new Notation("CFS", "crumple fall stun", "opponent is pushed away while crumping to the ground, can be a combo starter"));
        movePropsList.add(new Notation("BS", "block stun (to attacking character, ex. Law d/b+4)", ""));
        movePropsList.add(new Notation("SH", "stagger hit", ""));
        movePropsList.add(new Notation("GB", "guard break", "opponent is immobalized briefly, the opponent usually has the offensive advantage"));
        movePropsList.add(new Notation("TT", "throw transition (results in throw on hit)", ""));
        movePropsList.add(new Notation("TC", "technically crouching state", "crouch status during this move cruches high attacks"));
        movePropsList.add(new Notation("TJ", "technically jumping state", "jump status during this move crushes low attacks"));
        movePropsList.add(new Notation("HA", "homing attack", "move tracks both left and right"));
        movePropsList.add(new Notation("B!", "this move cause bound state", "bound moves usually also cause floor destuction"));
        movePropsList.add(new Notation("F!", "this move cause floor destruction ", "mostly for throws that don't cause a bound state but break floors"));
        movePropsList.add(new Notation("#", "see corresponding footnote", ""));
        movePropsList.add(new Notation("[2]", "hit modifier (eg RC[2] property applies to 2nd hit)", ""));
        movePropsList.add(new Notation("b", "Block modifier (eg. OCb opponent crouch on block)", ""));
        movePropsList.add(new Notation("c", "CH modifier (eg. JGc is a juggle starter on counter hit)", ""));
        movePropsList.add(new Notation("co", "crouching opponent modifier (eg. KSco)", ""));
        movePropsList.add(new Notation("cco", "CH on crouching opponent modifier (eg. FScco)", ""));

        hitRangeList.add(new Notation("l", "hits low (block d/b)", ""));
        hitRangeList.add(new Notation("m", "hits mid (block b)", ""));
        hitRangeList.add(new Notation("s", "hits special mid (block d/b or b)", ""));
        hitRangeList.add(new Notation("h", "hits high (block b or duck)", ""));
        hitRangeList.add(new Notation("L", "hits low and grounded opponents (block d/b)", ""));
        hitRangeList.add(new Notation("M", "hits mid and grounded opponents (block b)", ""));
        hitRangeList.add(new Notation("S", "hits special mid and grounded opponents(block d/b or b)", ""));
        hitRangeList.add(new Notation("H", "hits high and grounded opponents (block b or duck)", ""));
        hitRangeList.add(new Notation("!", "unblockable hit", ""));
        hitRangeList.add(new Notation("(!)", "unblockable hit which can be ducked", ""));
        hitRangeList.add(new Notation("[!]", "unblockable hits grounded opponents", ""));
        hitRangeList.add(new Notation("\"", "indicates block point in string hits", ""));

        comboSpecList.add(new Notation("cc", "crouch cancel", "tap u or f,f while crouching"));
        comboSpecList.add(new Notation("cd", "crouch dash", "f,N,d,d/f (usually)"));
        comboSpecList.add(new Notation("iWS", "instant while standing", "d,d/b,N_d,d/f,N (usually)"));
        comboSpecList.add(new Notation("wgf", "wind godfist", "f,N,d,d/f+2"));
        comboSpecList.add(new Notation("ewgf", "electric wind godfist", "f,N,d~d/f+2"));
        comboSpecList.add(new Notation("tgf", "thunder godfist", "f,N,d,d/f+1"));
        comboSpecList.add(new Notation("big", "big character combo", "only works on big characters"));
        comboSpecList.add(new Notation("( )", "missing hit", "is required for the next hit"));

        charSpecList.add(new Notation("HSP", "handstand position", "Eddy Gordo"));
        charSpecList.add(new Notation("RLX", "relaxed position", "Eddy Gordo"));
        charSpecList.add(new Notation("AOP", "art of phoenix", "Ling Xiaoyu"));
        charSpecList.add(new Notation("RDS", "rain dance stance", "Ling Xiaoyu"));
        charSpecList.add(new Notation("LFF", "left foot forward", "Hwoarang"));
        charSpecList.add(new Notation("RFF", "right foot forward", "Hwoarang"));
        charSpecList.add(new Notation("LFS", "left flamingo stance", "Hwoarang"));
        charSpecList.add(new Notation("RFS", "right flamingo stance", "Hwoarang"));
        charSpecList.add(new Notation("DSS", "dragon sign stance", "Marshall Law"));
        charSpecList.add(new Notation("DFS", "dragon fake step", "Marshall Law"));
        charSpecList.add(new Notation("HMS", "hit man stance", "Lee Chaolan - Violet"));
        charSpecList.add(new Notation("HBS", "hunting bear stance", "Kuma - Panda"));
        charSpecList.add(new Notation("ROL", "prowling grizzly roll", "Kuma - Panda"));
        charSpecList.add(new Notation("SIT", "sit down", "Kuma - Panda"));
        charSpecList.add(new Notation("INS", "indian sit", "Yoshimitsu"));
        charSpecList.add(new Notation("FLE", "flea stance", "Yoshimitsu"));
        charSpecList.add(new Notation("DGF", "dragonfly stance", "Yoshimitsu"));
        charSpecList.add(new Notation("MED", "meditation", "Yoshimitsu"));
        charSpecList.add(new Notation("KIN", "kinchou stance", "Yoshimitsu"));
        charSpecList.add(new Notation("NSS", "no sword stance", "Yoshimitsu"));
        charSpecList.add(new Notation("KNP", "kenpo step", "Feng Wei"));
        charSpecList.add(new Notation("STC", "shifting clouds", "Feng Wei"));
        charSpecList.add(new Notation("SDW", "shadow stance", "Master Raven"));
        charSpecList.add(new Notation("HAZ", "haze stance", "Master Raven"));
        charSpecList.add(new Notation("CDS", "crouching demon stance", "Jin Kazama"));
        charSpecList.add(new Notation("ALB", "albatross spin", "Steve Fox"));
        charSpecList.add(new Notation("DCK", "ducking", "Steve Fox"));
        charSpecList.add(new Notation("PKB", "peekaboo", "Steve Fox"));
        charSpecList.add(new Notation("SWY", "sway", "Steve Fox"));
        charSpecList.add(new Notation("LWV", "left weave", "Steve Fox"));
        charSpecList.add(new Notation("RWV", "right weave", "Steve Fox"));
        charSpecList.add(new Notation("FLK", "flicker", "Steve Fox"));
        charSpecList.add(new Notation("HPF", "haze palm fist", "Asuka"));
        charSpecList.add(new Notation("LCT", "leg cutter", "Asuka"));
        charSpecList.add(new Notation("DES", "destroy form", "Alisa Bosconovitch"));
        charSpecList.add(new Notation("SBT", "single boot", "Alisa Bosconovitch"));
        charSpecList.add(new Notation("DBT", "dual boot", "Alisa Bosconovitch"));
        charSpecList.add(new Notation("SEN", "silent entry", "Lars Alexandersson"));
        charSpecList.add(new Notation("DEN", "dynamic entry", "Lars Alexandersson"));
        charSpecList.add(new Notation("SAV", "savage stance", "Miguel Caballero Rojo"));
        charSpecList.add(new Notation("MTS", "muay thai stance", "Josie Rizal"));
        charSpecList.add(new Notation("BAL", "spinning ball", "Bob"));

        columnNotation.setCellValueFactory(new PropertyValueFactory<>("notation"));
        columnMeaning.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        columnExtra.setCellValueFactory(new PropertyValueFactory<>("extra"));

        columnNotation1.setCellValueFactory(new PropertyValueFactory<>("notation"));
        columnMeaning1.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        columnExtra1.setCellValueFactory(new PropertyValueFactory<>("extra"));

        columnNotation2.setCellValueFactory(new PropertyValueFactory<>("notation"));
        columnMeaning2.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        columnExtra2.setCellValueFactory(new PropertyValueFactory<>("extra"));

        columnNotation3.setCellValueFactory(new PropertyValueFactory<>("notation"));
        columnMeaning3.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        columnExtra3.setCellValueFactory(new PropertyValueFactory<>("extra"));

        columnNotation4.setCellValueFactory(new PropertyValueFactory<>("notation"));
        columnMeaning4.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        columnExtra4.setCellValueFactory(new PropertyValueFactory<>("extra"));

        columnNotation5.setCellValueFactory(new PropertyValueFactory<>("notation"));
        columnMeaning5.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        columnExtra5.setCellValueFactory(new PropertyValueFactory<>("extra"));

        columnNotation6.setCellValueFactory(new PropertyValueFactory<>("notation"));
        columnMeaning6.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        columnExtra6.setCellValueFactory(new PropertyValueFactory<>("extra"));

        commandWindow.setItems(commandList);
        notationWindow.setItems(notationList);
        groundedPosWindow.setItems(groundedPosList);
        movePropsWindow.setItems(movePropsList);
        hitRangeWIndow.setItems(hitRangeList);
        comboSpecAbbWindow.setItems(comboSpecList);
        charSpecAbbWindow.setItems(charSpecList);
    }

}
