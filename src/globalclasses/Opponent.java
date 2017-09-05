/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package globalclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Nicklas
 */
public class Opponent {

    private String name;
    private int wins;
    private int losses;
    private ArrayList<OpponentCharacter> characters = new ArrayList<>();

    public Opponent(String name, int wins, int losses) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public ArrayList<OpponentCharacter> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<OpponentCharacter> characters) {
        this.characters = characters;
    }

    public OpponentCharacter[] getTopFive() {
        if (characters.size() > 1) {
            Collections.sort(characters, new Comparator<OpponentCharacter>() {
                @Override
                public int compare(OpponentCharacter char1, OpponentCharacter char2) {

                    return char1.getGamesPlayed() - char2.getGamesPlayed();
                }
            });
            Collections.reverse(characters);
        }

        OpponentCharacter[] returnChars;

        if (characters.size() < 5) {
            returnChars = new OpponentCharacter[characters.size()];
            
            for (int i = 0; i < characters.size(); i++) {
                returnChars[i] = characters.get(i);
            }
        } else {
            returnChars = new OpponentCharacter[5];
            
            returnChars[0] = characters.get(0);
            returnChars[1] = characters.get(1);
            returnChars[2] = characters.get(2);
            returnChars[3] = characters.get(3);
            returnChars[4] = characters.get(4);
        }
        return returnChars;
    }

}
