/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Game.City;
import Game.GameData;
import Game.GameStateManager;
import Game.GameStateManager.GameState;
import Game.Player;
import Main.Main;
import Main.Main.PropertyType;
import UI.UI.UIState;
import java.util.ArrayList;
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

    public void movePlayer(City city, boolean flying) {
        if (ui.isAnimRunning()) {
            return;
        }
        Player player = ui.getGSM().getGameData().getCurrentPlayer();
        if (!ui.getGSM().move(player, city, false, flying)) {
            if(player.isComputer()) {
                ui.getGSM().nextMove();
            } else {
                System.out.println("Invalid Move!");
            }
        }
    }

    public void rollDie() {
        if (ui.isAnimRunning()) {
            return;
        }
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int roll = ui.getGSM().rollDie();
        if (roll == 0) {
            return;
        }
        if(roll == -1) {
            if(ui.getGSM().getGameState() == GameState.PLAYER_ROLL)
                ui.loadNegRollDialog();
            else
                ui.getGSM().nextMove();
            return;
        }
        ui.loadDie(props.getPropertyOptionsList(PropertyType.DIE_IMG).get(roll - 1));
        Player player = ui.getGSM().getGameData().getCurrentPlayer();
        if (player.isComputer()) {
            movePlayer(player.getNextPath(), false);
        } else {
            ui.removeLines();
            ui.drawLines();
        }
    }
}
