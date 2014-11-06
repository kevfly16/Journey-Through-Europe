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
    
    public GameStateManager(UI initUI) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int num = Integer.parseInt(props.getProperty(PropertyType.NUM_CARDS));
        ui = initUI;
        gameData = new GameData();
        GameData.setCardsDealt(num);
    }
    
    
}
