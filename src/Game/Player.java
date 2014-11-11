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
    
    private final String name;
    private final String flag;
    private boolean skippedTurn;
    private City currentPosition;
    private int currentPoints;
    private ArrayList<Card> cards;
    private final ArrayList<City> visited;
    private int roll;
    
    public Player(String n, String f) {
        name = n;
        flag = f;
        skippedTurn = false;
        currentPosition = null;
        currentPoints = 0;
        cards = new ArrayList();
        visited = new ArrayList();
        roll = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public String getFlag() {
        return flag;
    }
    
    public void setSkippedTurn(boolean skip) {
        skippedTurn = skip;
    }
    
    public boolean hasSkippedTurn() {
        return skippedTurn;
    }
    
    public City getCurrentPosition() {
        return currentPosition;
    }
    
    public void setCurrentPosition(City city) {
        currentPosition = city;
    }
    
    public City getFastestPath() {
        //TODO
        return null;
    }
    
    public int getCurrentPoints() { 
        return currentPoints;
    }
    
    public void decPoints(int dec) {
        currentPoints -= dec;
    }
    
    public void setCards(ArrayList<Card> c) {
        cards = c;
    }
    
    public ArrayList<Card> getCards() {
        return cards;
    }
    
    public boolean hasWon() {
       for(Card card : cards) {
           City city = GameData.getMap().getCity(card.getCity());
           if(!visited.contains(city))
               return false;
       }
       
       return true;
    }
    
    public void rollDice() {
        roll = (int) (Math.random() * 7);
    }
    
    public int getRoll() {
        return roll;
    }
    
    public ArrayList<City> getVisited() {
        return visited;
    }
    
    public void addVisited(City city) {
        visited.add(city);
    }
}
