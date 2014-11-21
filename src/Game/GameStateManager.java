/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Main.Main.PropertyType;
import UI.UI;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import properties_manager.PropertiesManager;

/**
 *
 * @author Kevin
 */
public class GameStateManager {

    private final UI ui;
    private final GameData gameData;
    private GameState gameState;

    public enum GameState {

        PLAYER_MOVE, PLAYER_ROLL, GAME_NOT_STARTED, FLIGHT_PLAN, COMPUTER_MOVE, COMPUTER_ROLL
    };

    public GameStateManager(UI initUI) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int num = Integer.parseInt(props.getProperty(PropertyType.NUM_CARDS));
        ui = initUI;
        gameState = GameState.GAME_NOT_STARTED;
        gameData = new GameData();
        GameData.setCardsDealt(num);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        gameState = state;
    }

    public GameData getGameData() {
        return gameData;
    }

    public UI getUI() {
        return ui;
    }

    public void startGame() {
        initPlayers();

        if (gameData.getCurrentPlayer().isPlayer()) {
            gameState = GameState.PLAYER_ROLL;
        } else {
            gameState = GameState.COMPUTER_MOVE;
        }
    }

    private void initPlayers() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
         // GET FLAGS
        ArrayList<String> flags = props.getPropertyOptionsList(PropertyType.PLAYER_FLAG);
        ArrayList<String> icons = props.getPropertyOptionsList(PropertyType.PLAYER_ICON);
        ArrayList<GridPane> playerSelectPanes = ui.getPlayerSelectPanes();
        ArrayList<Player> players = new ArrayList(playerSelectPanes.size());
        int index = 0;
        for (GridPane pane : playerSelectPanes) {
            String name = "";
            String flag = flags.get(index);
            String icon = icons.get(index);
            boolean computer = false;
            for (Node node : pane.getChildren()) {
                if (node instanceof TextField) {
                    TextField text = (TextField) node;
                    name = text.getText();
                }

                if (node instanceof RadioButton) {
                    RadioButton rb = (RadioButton) node;
                    if (rb.getText().equals(props.getProperty(PropertyType.PLAYER_TEXT))) {
                        computer = !rb.isSelected();
                    } else {
                        computer = rb.isSelected();
                    }
                }
            }
            Player player = new Player(name, flag, icon, computer);
            players.add(player);
            player.setCards(new ArrayList<>(GameData.generateCards()));
            player.setCurrentPosition(player.getStartingCity());
            index++;
        }

        GameData.setPlayers(players);
    }

    public boolean canMove() {
        return gameState == GameState.PLAYER_MOVE || gameState == GameState.COMPUTER_MOVE;
    }

    public boolean canMove(Player player) {
        if (canMove()) {
            return gameData.getCurrentPlayer() == player;
        } else {
            return false;
        }
    }

    public boolean move(Player player, City dest) {
        if (!canMove(player)) {
            return false;
        }
        if (!GameData.getMap().getCity(player.getCurrentPosition().getName()).hasCity(dest)) {
            return false;
        }
        if(player.getPreviousPosition() == dest) {
            return false;
        }
        double x = dest.getPos().getX() - player.getCurrentPosition().getPos().getX();
        double y = dest.getPos().getY() - player.getCurrentPosition().getPos().getY();
        ui.movePlayer(player, x, y, dest);
        return true;
    }

    public void decCurrentPoints() {
        Player player = gameData.getCurrentPlayer();
        player.decPoints(1);
        ui.loadPointsLeft(player.getCurrentPoints());
        ui.removeLines();
        if (player.getCurrentPoints() == 0) {
            nextMove();
            return;
        }
        ui.drawLines();
    }

    public boolean hasWon(Player player) {
        return player.hasWon();
    }

    public int rollDie() {
        if (gameState != GameState.PLAYER_ROLL && gameState != GameState.COMPUTER_ROLL) {
            return 0;
        }
        Player player = gameData.getCurrentPlayer();
        player.rollDie();
        if (gameState == GameState.PLAYER_ROLL) {
            gameState = GameState.PLAYER_MOVE;
        } else {
            gameState = GameState.COMPUTER_MOVE;
        }
        return player.getRoll();
    }

    public void nextMove() {
        Player p = gameData.getCurrentPlayer();
        if (p.getRoll() == 6) {
            if (gameData.getCurrentPlayer().isPlayer()) {
                setGameState(GameState.PLAYER_ROLL);
            } else {
                setGameState(GameState.COMPUTER_ROLL);
            }
            ui.loadRollAgain();
            return;
        }
        gameData.incCurrentMove();
        if (gameData.getCurrentPlayer().isPlayer()) {
            setGameState(GameState.PLAYER_ROLL);
        } else {
            setGameState(GameState.COMPUTER_ROLL);
        }
        p = gameData.getCurrentPlayer();
        ObservableList<Node> children = ui.getCardsPane().getChildren();
        children.remove(1, children.size());
        Label label = (Label) children.get(0);
        label.setText(p.getName());
        ArrayList<Card> cards = p.getCards();
        for (Card card : cards) {
            if(!p.hasVisited(card.getCity()))
                children.add(card.getCardIcon());
        }
        ui.loadPlayer(p.getName());
    }

}
