package com.d.wordsearch;

/**
 * Created by kaushiksekar on 27/01/17.
 */

public class WordHistoryModel {

    String cardName;
    String cardDefName;

    public String getCardDefName() {
        return cardDefName;
    }

    public void setCardDefName(String cardDefName) {
        this.cardDefName = cardDefName;
    }

    int isFav;
    int isTurned;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public int getIsTurned() {
        return isTurned;
    }

    public void setIsTurned(int isTurned) {
        this.isTurned = isTurned;
    }
}
