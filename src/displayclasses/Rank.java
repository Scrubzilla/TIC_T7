/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayclasses;

import javafx.scene.image.Image;

/**
 *
 * @author Nicklas
 */
public class Rank {
    int danNumber;
    Image rankImage;
    int amountOfPointsPromotion;
    int amountOfPointsDemotion;

    public Rank(int danNumber, Image rankImage, int amountOfPointsPromotion, int amountOfPointsDemotion) {
        this.danNumber = danNumber;
        this.rankImage = rankImage;
        this.amountOfPointsPromotion = amountOfPointsPromotion;
        this.amountOfPointsDemotion = amountOfPointsDemotion;
    }

    
    
    public int getDanNumber() {
        return danNumber;
    }

    public void setDanNumber(int danNumber) {
        this.danNumber = danNumber;
    }

    public Image getRankImage() {
        return rankImage;
    }

    public void setRankImage(Image rankImage) {
        this.rankImage = rankImage;
    }

    public int getAmountOfPointsPromotion() {
        return amountOfPointsPromotion;
    }

    public void setAmountOfPointsPromotion(int amountOfPointsPromotion) {
        this.amountOfPointsPromotion = amountOfPointsPromotion;
    }

    public int getAmountOfPointsDemotion() {
        return amountOfPointsDemotion;
    }

    public void setAmountOfPointsDemotion(int amountOfPointsDemotion) {
        this.amountOfPointsDemotion = amountOfPointsDemotion;
    }
    
    
}
