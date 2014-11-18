/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import File.FileLoader;
import Main.Main.PropertyType;
import UI.UI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import properties_manager.PropertiesManager;

/**
 *
 * @author Kevin
 */
public class GameData {

    private ArrayList<Move> moves;
    private static Map map;
    private static ArrayList<Player> players;
    private static HashMap<String, Card> cards;
    private static int numCardsDealt;
    private static HashMap<String, Card> usedCards;
    private int currentMove;

    public GameData() {
        initMap();
        initCards();
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public static void setPlayers(ArrayList<Player> p) {
        players = p;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static Card getCard(String card) {
        return cards.get(card);
    }

    public static Map getMap() {
        return map;
    }

    public static int getCardsDealt() {
        return numCardsDealt;
    }

    public static void setCardsDealt(int num) {
        numCardsDealt = num;
    }

    public static LinkedList<Card> generateCards() {
        String[] c = new String[cards.size()];
        cards.keySet().toArray(c);
        LinkedList<Card> hand = new LinkedList();
        String[] colors = new String[]{"green", "red", "yellow"};
        for (int i = 0; i < numCardsDealt; i++) {
            int rand = (int) (Math.random() * c.length);
            Card card = cards.get(c[rand]);
            while (!card.getColor().equals(colors[i % colors.length]) || usedCards.get(card.getCity()) != null) {
                rand = (int) (Math.random() * c.length);
                card = cards.get(c[rand]);
            }
            hand.addLast(card);
            usedCards.put(card.getCity(), card);
        }
        return hand;
    }

    public Move getLastMove(Player player) {
        //TODO
        return new Move(player, null);
    }

    public int getCurrentMove() {
        return currentMove;
    }

    public void incCurrentMove() {
        if (currentMove == players.size() - 1) {
            currentMove = 0;
        } else {
            currentMove++;
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentMove);
    }

    public void deleteLastMove(Player player) {
        //TODO
    }

    private void initMap() {
        map = new Map();
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            City.setRadius(Double.parseDouble(props.getProperty(PropertyType.RADIUS)));
            FileLoader.loadMap(map);
            map.sort();
        } catch (IOException ex) {
            UI.getErrorHandler().processError(PropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }

    private void initCards() {
        try {
            cards = new HashMap();
            usedCards = new HashMap();
            FileLoader.loadCards(cards);
        } catch (IOException ex) {
            Logger.getLogger(GameData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
