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
    private boolean skipTurn;
    private int pointsLost;
    private City destCity;
    private boolean rule;
    
    public Card(String c, String desc, boolean s, int points, City dest, boolean r) {
        city = c;
        cardDescription = desc;
        skipTurn = s;
        pointsLost = points;
        destCity = dest;
        rule = r;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getCardDescription() {
        return cardDescription;
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
