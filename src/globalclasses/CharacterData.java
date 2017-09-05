/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package globalclasses;

import java.io.Serializable;

/**
 *
 * @author Nicklas
 */
public class CharacterData implements Serializable{
    private String characterName;
    private int currentRank;
    private double rankPoints;

    public CharacterData(String characterName, int currentRank, double rankPoints) {
        this.characterName = characterName;
        this.currentRank = currentRank;
        this.rankPoints = rankPoints;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }

    public double getRankPoints() {
        return rankPoints;
    }

    public void setRankPoints(double rankPoints) {
        this.rankPoints = rankPoints;
    }
    
    public String getData(){
        String data = "";
        data = "Character name: " + characterName + "\nCurrent rank: " + currentRank + "\nCurrent points: " + rankPoints + "\n";
        return data;
    }
    
    
}
