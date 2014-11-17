/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *
 * @author Kevin
 */
public class Card {
    private String city;
    private String cardDescription;
    private String color;
    private boolean skipTurn;
    private int pointsLost;
    private City destCity;
    private boolean rule;
    private String path;
    
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
}
