/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import File.FileLoader;
import Main.Main;
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
    private HashMap<String, Card> usedCards;
    private int currentMove;

    public GameData() {
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
}
