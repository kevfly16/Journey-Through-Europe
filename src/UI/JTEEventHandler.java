/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Game.City;
import Game.GameData;
import Game.Player;
import Main.Main;
import UI.UI.UIState;
import java.util.LinkedList;
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
        ui.getGSM().startGame();
        ui.changeWorkspace(UIState.GAMEPLAY_SCREEN_STATE);
        ui.dealAnimate(new LinkedList<>(GameData.getPlayers()));
        ui.initPlayerIcons();
    }

    public void movePlayer(City city) {
        if(ui.isAnimRunning())
            return;
        Player player = ui.getGSM().getGameData().getCurrentPlayer();
        ui.getGSM().move(player, city);
    }
    
    public void rollDie() {
        if(ui.isAnimRunning())
            return;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int roll = ui.getGSM().rollDie();
        if(roll == 0) {
            return;
        }
        
        ui.loadDie(roll, props.getPropertyOptionsList(Main.PropertyType.DIE_IMG).get(roll - 1));
    }
}