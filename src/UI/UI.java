/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Game.GameStateManager;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Kevin
 */
public class UI {
    
    GameStateManager gsm;
    CardAnimation cardAnim;
    MoveAnimation moveAnim;
    DocumentManager docManager;
    EventHandler eventHandler;
    ErrorHandler errorHandler;
    Stage primaryStage;
    Pane mainPane;
    Pane workspace;
    Pane mapPane;
    ImageView mapImage;
    Pane splashScreenPane;
    Pane gameSetupPane;
    Pane playerSelectPane;
    Pane gameplayScreenPane;
    Pane aboutScreenPane;
    Pane moveHistoryScreenPane;
    Pane flightPlanPane;
    int paneWidth;
    int paneHeight;
    Button playButton;
    Button loadButton;
    Button quitButton;
    Button selectPlayerButton;
    Button startButton;
    Button moveHistoryButton;
    Button aboutButton;
    Button rollButton;
    Button flightButton;
    ArrayList<Image> playerIcons;
    
    public BorderPane getMainPane() {
        return new BorderPane();
    }
    
    public void setStage(Stage stage) {
        
    }
}
