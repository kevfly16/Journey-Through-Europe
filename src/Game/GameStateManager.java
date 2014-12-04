/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import File.FileLoader;
import Main.Main.PropertyType;
import UI.UI;
import UI.UI.UIState;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private GameData gameData;
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

    public void setGameState(String state) {
        switch (state) {
            case "PLAYER_MOVE":
                setGameState(GameState.PLAYER_MOVE);
                break;
            case "COMPUTER_MOVE":
                setGameState(GameState.COMPUTER_MOVE);
                break;
            case "PLAYER_ROLL":
                setGameState(GameState.PLAYER_ROLL);
                break;
            case "COMPUTER_ROLL":
                setGameState(GameState.PLAYER_MOVE);
                break;
        }
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
            gameState = GameState.COMPUTER_ROLL;
        }
    }

    public void stopGame() {
        gameState = GameState.GAME_NOT_STARTED;
        GameData.setPlayers(null);
        gameData = new GameData();
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
            ArrayList<Card> cards = new ArrayList<>(GameData.generateCards());
            if (computer) {
                while (cards.contains(GameData.getCard("Tirane"))) {
                    cards = new ArrayList<>(GameData.generateCards());
                }
            }
            player.setCards(cards);
            player.setCurrentPosition(player.getStartingCity());
            index++;
        }

        GameData.setPlayers(players);
    }

    public boolean isPlayerTurn() {
        return gameState == GameState.PLAYER_MOVE;
    }
    
    public boolean isPlayerRoll() {
        return gameState == GameState.PLAYER_ROLL;
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

    public boolean move(Player player, City dest, boolean rule, boolean flying) {
        if (!rule) {
            if (!canMove(player)) {
                return false;
            }
            if (player.getCurrentPosition() == dest) {
                return false;
            }
            if (!GameData.getMap().getCity(player.getCurrentPosition().getName()).hasCity(dest)) {
                if (!flying || !GameData.getMap().hasFlight(player.getCurrentPosition(), dest)) {
                    return false;
                }
            }
            if (player.getPreviousPosition() == dest) {
                return false;
            }
            if (player.getCurrentPosition().isSeaConnection(dest) && !player.isTurnStarted()) {
                return false;
            }
            if (GameData.getMap().hasFlight(player.getCurrentPosition(), dest) && !player.hasFlied() && flying) {
                if (player.hasPointsToFly(dest)) {
                    player.setFlied(true);
                } else {
                    return false;
                }
            }
        }
        double x = dest.getPos().getX() - player.getCurrentPosition().getPos().getX();
        double y = dest.getPos().getY() - player.getCurrentPosition().getPos().getY();
        ui.movePlayer(player, x, y, dest);
        return true;
    }

    public boolean decCurrentPoints() {
        Player player = gameData.getCurrentPlayer();
        if (player.getCurrentPoints() == 0) {
            nextMove();
            return false;
        }
        int flightPoints = player.getFlightPoints();
        if (flightPoints > 0) {
            player.decPoints(flightPoints);
        } else {
            player.decPoints(1);
        }
        ui.loadPointsLeft(player.getCurrentPoints());
        ui.removeLines();
        if (player.getCurrentPoints() == 0) {
            nextMove();
            return false;
        }
        if (player.isPlayer()) {
            ui.drawLines();
        }
        return true;
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
        if (player.getCurrentPoints() == 0) {
            return -1;
        }
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
            if (p.isPlayer()) {
                setGameState(GameState.PLAYER_ROLL);
            } else {
                setGameState(GameState.COMPUTER_ROLL);
            }
            ui.loadRollAgain();
            if (p.isComputer()) {
                p.createPath();
                ui.getEventHandler().rollDie();
            }
            return;
        }
        gameData.incCurrentMove();
        p = gameData.getCurrentPlayer();
        if (p.getSkip() > 0) {
            p.decSkip();
            gameData.incCurrentMove();
            p = gameData.getCurrentPlayer();
        }
        if (p.isPlayer()) {
            setGameState(GameState.PLAYER_ROLL);
        } else {
            setGameState(GameState.COMPUTER_ROLL);
        }
        ObservableList<Node> children = ui.getCardsPane().getChildren();
        children.remove(1, children.size());
        Label label = (Label) children.get(0);
        label.setText(p.getName());
        ArrayList<Card> cards = p.getCards();
        for (Card card : cards) {
            if (!p.hasVisited(card.getCity())) {
                children.add(card.getCardIcon());
            }
        }
        p.setPreviousPosition(null);
        p.setTurnStarted(true);
        p.setFlied(false);
        ui.loadPlayer(p.getName());
        if (p.isComputer()) {
            p.createPath();
            ui.getEventHandler().rollDie();
        }
    }

    public void applyRule(Card card) {
        Player player = gameData.getCurrentPlayer();
        if (card.hasRule()) {
            switch (card.getRuleName()) {
                case "Travel":
                    System.out.println("Travel");
                    City city = card.getDestCity();
                    if (city == null) {
                        city = player.getLastCity();
                    }
                    player.resetCurrentPoints();
                    move(player, city, true, false);
                    break;
                case "Skip":
                    System.out.println("Skip");
                    player.setSkip(card.getSkipTurn());
                    player.resetRoll();
                    nextMove();
                    break;
                case "Roll Again":
                    System.out.println("Roll Again");
                    player.setRollAgain();
                    nextMove();
                    break;
                case "New Card":
                    System.out.println("New Card");
                    player.addCard(GameData.generateCard());
                    player.resetRoll();
                    nextMove();
                    break;
                case "Score":
                    System.out.println("Score");
                    player.setScore(card.getScore());
                    player.resetRoll();
                    nextMove();
                    break;
                case "Points":
                    System.out.println("Points");
                    player.addPoints(card.getPoints());
                    if (decCurrentPoints()) {
                        if (player.isComputer()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            player.createPath();
                            ui.getEventHandler().movePlayer(player.getNextPath(), false);
                        }
                    }
                    break;
                case "Roll":
                    System.out.println("Roll");
                    player.setNextRoll(card.getRoll());
                    player.resetRoll();
                    nextMove();
                    break;
                case "Double":
                    System.out.println("Double");
                    player.setDoubleRoll(true);
                    player.resetRoll();
                    nextMove();
                    break;
            }
        } else {
            if (decCurrentPoints()) {
                if (player.isComputer()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    player.createPath();
                    ui.getEventHandler().movePlayer(player.getNextPath(), false);
                }
            }
        }
    }

    public void saveGame() {
        FileLoader.saveGame(this);
    }
    
    public void loadGame() {
        FileLoader.loadGame(this);
        ui.loadUI();
        ui.changeWorkspace(UIState.GAMEPLAY_SCREEN_STATE);
        
    }
}
