/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayclasses;

/**
 *
 * @author Nicklas
 */
public class Combo {

    String launcher;
    String followUp;
    String damage;
    String wall;
    String comment;

    public Combo(String launcher, String followUp, String damage, String wall, String comment) {
        this.launcher = launcher;
        this.followUp = followUp;
        this.damage = damage;
        this.wall = wall;
        this.comment = comment;
    }

    public String getLauncher() {
        return launcher;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }

    public String getFollowUp() {
        return followUp;
    }

    public void setFollowUp(String fullCombo) {
        this.followUp = fullCombo;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getWall() {
        return wall;
    }

    public void setWall(String wall) {
        this.wall = wall;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
