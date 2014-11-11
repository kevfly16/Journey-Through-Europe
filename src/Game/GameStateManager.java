/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Main.Main.PropertyType;
import UI.UI;
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
        PLAYER_MOVE, PLAYER_ROLL, GAME_NOT_STARTED, FLIGHT_PLAN
    };
    
    public GameStateManager(UI initUI) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int num = Integer.parseInt(props.getProperty(PropertyType.NUM_CARDS));
        ui = initUI;
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
    
    public boolean canMove() {
        //TODO
        return false;
    }
    
    public boolean canMove(Player player) {
        //TODO
        return false;
    }
    
    public boolean move(Player player, City dest) {
        //TODO
        return canMove(player);
    }
    
    public boolean hasWon(Player player) {
        return player.hasWon();
    }
    
    
}
