/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import UI.UI;

/**
 *
 * @author Kevin
 */
public class GameStateManager {
    private final UI ui;
    private GameData gameData;
    
    public GameStateManager(UI initUI) {
        ui = initUI;
        gameData = new GameData();
    }
    
    
}
