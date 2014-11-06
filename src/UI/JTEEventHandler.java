/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Game.GameData;
import Game.Player;
import UI.UI.UIState;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.scene.layout.GridPane;
import properties_manager.PropertiesManager;

/**
 *
 * @author Kevin
 */
public class JTEEventHandler {

    private final UI ui;

    public JTEEventHandler(UI initUI) {
        ui = initUI;
    }

    public void startGame() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<GridPane> playerSelectPanes = ui.getPlayerSelectPanes();
        ui.changeWorkspace(UIState.GAMEPLAY_SCREEN_STATE);
        LinkedList<String> cards = new LinkedList();
        cards.add("green/BASEL.jpg");
        cards.add("red/ARHUS.jpg");
        cards.add("yellow/BARI.jpg");
        cards.add("green/ROTTERDAM.jpg");
        cards.add("red/AMSTERDAM.jpg");
        cards.add("yellow/CADIZ.jpg");
        cards.add("green/SALZBURG.jpg");
        cards.add("red/LONDON.jpg");
        cards.add("yellow/BOLOGNA.jpg");
        // TODO
        // Player player = new Player("Player");
        // player.setCards(GameData.generateCards());
        ui.cardAnimate(cards, cards.size());
    }
}
