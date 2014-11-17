/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Game.Card;
import Game.City;
import Game.GameData;
import Game.Player;
import UI.UI.UIState;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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
        ArrayList<Player> players = new ArrayList(playerSelectPanes.size());
        int index = 0;
        for (GridPane pane : playerSelectPanes) {
            String name = "";
            String flag = ui.getFlagPaths().get(index);
            for (Node node : pane.getChildren()) {
                if (node instanceof TextField) {
                    TextField text = (TextField) node;
                    name = text.getText();
                }
            }
            Player player = new Player(name, flag);
            players.add(player);
            player.setCards(new ArrayList<>(GameData.generateCards()));
            player.setCurrentPosition(player.getStartingCity());
            index++;
        }
        ui.changeWorkspace(UIState.GAMEPLAY_SCREEN_STATE);
        GameData.setPlayers(players);
        LinkedList cards = new LinkedList(players.get(0).getCards());
        ui.cardAnimate(cards, cards.size());
        ui.initPlayerIcons();
    }

    public void movePlayer(City city) {
        Player player = ui.getGSM().getGameData().getCurrentPlayer();
        double x = city.getPos().getX() - player.getCurrentPosition().getPos().getX();
        double y = city.getPos().getY() - player.getCurrentPosition().getPos().getY();
        ui.movePlayer(player, x, y, city);
    }
}
