/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayclasses;

/**
 *
 * @author Igotballz
 */
public class Notation {
    String notation;
    String meaning;
    String extra;

    public Notation(String notation, String meaning, String extra) {
        this.notation = notation;
        this.meaning = meaning;
        this.extra = extra;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
    
    
}
