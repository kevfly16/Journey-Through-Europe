/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;
import java.util.LinkedList;
import javafx.scene.image.ImageView;

/**
 *
 * @author Kevin
 */
public class Player {

    private final String name;
    private final String flagURL;
    private final String iconURL;
    private ImageView playerIcon;
    private boolean skippedTurn;
    private City currentPosition;
    private int currentPoints;
    private ArrayList<Card> cards;
    private final ArrayList<City> visited;
    private int roll;
    private final boolean computer;
    private City previousPosition;
    private boolean turnStarted;
    private LinkedList<City> path;
    private boolean flied;
    private int flightPoints;
    private int skip;
    private int score;
    private int nextRoll;
    private boolean doubleRoll;

    public Player(String n, String f, String i, boolean c) {
        name = n;
        flagURL = f;
        skippedTurn = false;
        currentPosition = null;
        currentPoints = 0;
        cards = new ArrayList();
        visited = new ArrayList();
        roll = 0;
        iconURL = i;
        computer = c;
        previousPosition = null;
        turnStarted = false;
        flied = false;
        skip = 0;
        score = 0;
        nextRoll = 0;
        doubleRoll = false;
    }

    public String getName() {
        return name;
    }

    public String getFlagURL() {
        return flagURL;
    }

    public String getIconURL() {
        return iconURL;
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
        previousPosition = currentPosition;
        currentPosition = city;
    }
    
    public City getPreviousPosition() {
        return previousPosition;
    }
    
    public int getCurrentPoints() {
        return currentPoints;
    }
    
    public void resetCurrentPoints() {
        currentPoints = 0;
    }

    public void decPoints(int dec) {
        currentPoints -= dec;
    }
    
    public void addPoints(int add) {
        currentPoints += add;
    }

    public void setCards(ArrayList<Card> c) {
        cards = c;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
    
    public void addCard(Card card) {
        cards.add(card);
    }

    public void setScore(int s) {
        score = s;
    }
    
    public boolean hasWon() {
        return visited.size() == GameData.getCardsDealt();
    }

    public void rollDie() {
        if(nextRoll > 0) {
            currentPoints = roll = nextRoll;
            nextRoll = 0;
            return;
        }
        currentPoints = roll = (int) (Math.random() * 6 + 1);
        currentPoints += score;
        score = 0;
        if(currentPoints < 0) {
            currentPoints = 0;
        }
        if(doubleRoll) {
            currentPoints *= 2;
            doubleRoll = false;
        }
    }
    
    public void setDoubleRoll(boolean d) {
        doubleRoll = d;
    }
    
    public void setNextRoll(int next) {
        nextRoll = next;
    }

    public int getRoll() {
        return roll;
    }
    
    public void resetRoll() {
        roll = 0;
    }
    
    public void setRollAgain() {
        roll = 6;
    }

    public City getStartingCity() {
        return GameData.getMap().getCity(cards.get(0).getCity().toUpperCase());
    }

    public ArrayList<City> getVisited() {
        return visited;
    }

    public void addVisited(City city) {
        visited.add(city);
    }
    
    public int getCard(City city) {
        int index = 0;
        for(Card card : cards) {
            if(card.getCity().equalsIgnoreCase(city.getName())) {
                break;
            }
            index++;
        }
        return index;
    }

    public void setPlayerIcon(ImageView p) {
        playerIcon = p;
    }
    
    public boolean hasVisited(String name) {
        for(City city : visited) {
            if(city.getName().equalsIgnoreCase(name))
                return true;
        }
        
        return false;
    }
    
    public Card removeCard(City city) {
        int index = 0;
        for(Card card : cards) {
            if(card.getCity().equalsIgnoreCase(city.getName())) {
                return cards.remove(index);  
            }
            index++;
        }
        
        return null;
    }

    public ImageView getPlayerIcon() {
        return playerIcon;
    }

    public boolean isComputer() {
        return computer;
    }

    public boolean isPlayer() {
        return !computer;
    }
    
    public void setPreviousPosition(City city) {
        previousPosition = city;
    }
    
    public void setTurnStarted(boolean ts) {
        turnStarted = ts;
    }
    
    public boolean isTurnStarted() {
        return turnStarted;
    }
    
    public City getNextPath() {
        City city = path.removeFirst();
        System.out.println(city.getName());
        return city;
    }
    
    public void createPath() {
        for(int i = 1; i < cards.size(); i++) {
            City city = GameData.getMap().getCity(cards.get(i).getCity().toUpperCase());
            if(!visited.contains(city)) {
                path = new LinkedList<>(GameData.getMap().getPath(currentPosition, city));
                return;
            }
        }
        
        path = new LinkedList<>(GameData.getMap().getPath(currentPosition, getStartingCity()));
    }
    
    public void setFlied(boolean f) {
        flied = f;
    }
    
    public boolean hasFlied() {
        return flied;
    }
    
    public int getFlightPoints() {
        int tmp = flightPoints;
        flightPoints = 0;
        return tmp;
    }
    
    public boolean hasPointsToFly(City dest) {
        City src = currentPosition;
        src = GameData.getMap().getFlight(src.getName());
        dest = GameData.getMap().getFlight(dest.getName());
        if(Math.abs(src.getQuarter() - dest.getQuarter()) == 0) { 
            if(currentPoints >= 2) {
                flightPoints = 2;
                return true;
            }
            return false;
        } else if(Math.abs(src.getQuarter() - dest.getQuarter()) == 1 || Math.abs(src.getQuarter() - dest.getQuarter()) == 3) {
            if(currentPoints >= 4) {
                flightPoints = 4;
                return true;
            }
            return false;
        }
        
        return false;
    }
    
    public City getLastCity() {
        return GameData.getMap().getCity(cards.get(cards.size() - 1).getCity().toUpperCase());
    }
    
    public int getSkip() {
        return skip;
    }
    
    public void setSkip(int s) {
        skip = s;
    }
    
    public void decSkip() {
        skip--;
    }
}
