/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;
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

    public Player(String n, String f, boolean c) {
        name = n;
        flagURL = f;
        skippedTurn = false;
        currentPosition = null;
        currentPoints = 0;
        cards = new ArrayList();
        visited = new ArrayList();
        roll = 0;
        iconURL = initIconURL();
        computer = c;
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
        for (Card card : cards) {
            City city = GameData.getMap().getCity(card.getCity());
            if (!visited.contains(city)) {
                return false;
            }
        }

        return true;
    }

    public void rollDie() {
        currentPoints = roll = (int) (Math.random() * 6 + 1);
    }

    public int getRoll() {
        return roll;
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

    public void setPlayerIcon(ImageView p) {
        playerIcon = p;
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

    private String initIconURL() {
        String color = flagURL.split("_")[0];
        return "piece_" + color + ".png";
    }
}
