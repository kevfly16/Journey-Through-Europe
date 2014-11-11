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
    private HashMap<String, Card> usedCards;
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
    
    public void setPlayers(ArrayList<Player> p) {
        players = p;
    }
    
    public ArrayList<Player> getPlayers() {
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
    
    public static ArrayList<Card> generateCards() {
        return new ArrayList();
    }
    
    public Move getLastMove(Player player) {
        //TODO
        return new Move(player, null);
    }
    
    public int getCurrentMove() {
        return currentMove;
    }
    
    public void incCurrentMove() {
        currentMove++;
        currentMove %= players.size();
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
            FileLoader.loadCityPoints(map);
            map.sort();
        } catch (IOException ex) {
            UI.getErrorHandler().processError(PropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }
    
    private void initCards() {
        //TODO
        cards = new HashMap();
    }
}
