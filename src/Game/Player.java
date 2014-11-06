/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;

/**
 *
 * @author Kevin
 */
public class Player {
    
    private String name;
    private ArrayList<Card> cards;
    
    public Player(String n) {
        name = n;
    }
    
    public void setCards(ArrayList<Card> c) {
        cards = c;
    }
    
    public ArrayList<Card> getCards() {
        return cards;
    }
    
}
