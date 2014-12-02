/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import javafx.scene.image.ImageView;

/**
 *
 * @author Kevin
 */
public class Card {
    private final String city;
    private final String cardDescription;
    private final String color;
    private int skipTurn;
    private int points;
    private int score;
    private int roll;
    private City destCity;
    private boolean rule;
    private String ruleName;
    private final String path;
    private ImageView cardIcon;

    public Card(String c, String desc, String col, String p) {
        city = c;
        cardDescription = desc;
        color = col;
        path = p;
        skipTurn = 0;
        points = 0;
        destCity = null;
        score = 0;
        roll = 0;
        rule = false;
    }

    public String getCity() {
        return city;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public String getColor() {
        return color;
    }

    public String getPath() {
        return path;
    }

    public int getPoints() {
        return points;
    }

    public City getDestCity() {
        return destCity;
    }

    public boolean hasRule() {
        return isRule();
    }
    
    public void setRule(boolean r) {
        rule = r;
    }
    
    public void setRuleName(String rn) {
        ruleName = rn;
    }

    public void setCardIcon(ImageView card) {
        cardIcon = card;
    }

    public ImageView getCardIcon() {
        return cardIcon;
    }

    /**
     * @return the skipTurn
     */
    public int getSkipTurn() {
        return skipTurn;
    }

    /**
     * @param skipTurn the skipTurn to set
     */
    public void setSkipTurn(int skipTurn) {
        this.skipTurn = skipTurn;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @param destCity the destCity to set
     */
    public void setDestCity(City destCity) {
        this.destCity = destCity;
    }

    /**
     * @return the rule
     */
    public boolean isRule() {
        return rule;
    }

    /**
     * @return the ruleName
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * @return the roll
     */
    public int getRoll() {
        return roll;
    }

    /**
     * @param roll the roll to set
     */
    public void setRoll(int roll) {
        this.roll = roll;
    }
}
