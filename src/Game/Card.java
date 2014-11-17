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
    private final boolean skipTurn;
    private final int pointsLost;
    private final City destCity;
    private final boolean rule;
    private final String path;
    private ImageView cardIcon;
    
    public Card(String c, String desc, String col, boolean s, int points, City dest, boolean r, String p) {
        city = c;
        cardDescription = desc;
        color = col;
        skipTurn = s;
        pointsLost = points;
        destCity = dest;
        rule = r;
        path = p;
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
    
    public boolean hasSkipTurn() {
        return skipTurn;
    }
    
    public int getPointsLost() {
        return pointsLost;
    }
    
    public City getDestCity() {
        return destCity;
    }
    
    public boolean hasRule() {
        return rule;
    }
    
    public void setCardIcon(ImageView card) {
        cardIcon = card;
    }
    
    public ImageView getCardIcon() {
        return cardIcon;
    }
}
