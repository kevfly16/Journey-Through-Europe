/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import UI.UI.UIState;
import java.util.ArrayList;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Kevin
 */
public class EventHandler {
    
    private final UI ui;
    
    public EventHandler(UI initUI) {
        ui = initUI;
    }
    
    public void startGame() {
        ArrayList<GridPane> playerSelectPanes = ui.getPlayerSelectPanes();
        ui.changeWorkspace(UIState.GAMEPLAY_SCREEN_STATE);
    }
}
