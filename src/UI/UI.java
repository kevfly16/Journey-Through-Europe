/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Game.GameStateManager;
import Main.Main.PropertyType;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

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
    BorderPane mainPane;
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
    private String imgPath;
    Insets marginlessInsets;

    private final String STYLE_RELEASED = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;";
    private final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6;";
    
    /**
     * The SokobanUIState represents the four screen states that are possible
     * for the Sokoban game application. Depending on which state is in current
     * use, different controls will be visible.
     */
    public enum UIState {

        SPLASH_SCREEN_STATE, ABOUT_SCREEN_STATE, GAME_SETUP_STATE
    }

    public UI() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        imgPath = "file:" + props.getProperty(PropertyType.IMG_PATH);
        gsm = new GameStateManager();
        cardAnim = new CardAnimation();
        moveAnim = new MoveAnimation();
        docManager = new DocumentManager();
        eventHandler = new EventHandler();
        errorHandler = new ErrorHandler();
        initMainPane();
        initSplashScreenPane();
        initGameSetupPane();
        initAboutScreenPane();
        initMoveHistoryScreenPane();
        initWorkspace();

    }

    public void initMainPane() {
        System.out.println("Init MainPane");
        marginlessInsets = new Insets(5, 5, 5, 5);
        mainPane = new BorderPane();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        paneWidth = Integer.parseInt(props
                .getProperty(PropertyType.WINDOW_WIDTH));
        paneHeight = Integer.parseInt(props
                .getProperty(PropertyType.WINDOW_HEIGHT));
        mainPane.resize(paneWidth, paneHeight);
        mainPane.setPadding(marginlessInsets);
    }

    public void initSplashScreenPane() {
        // INIT THE SPLASH SCREEN CONTROLS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String splashScreenImagePath = props
                .getProperty(PropertyType.SPLASH_SCREEN_IMAGE_NAME);
        props.addProperty(PropertyType.INSETS, "5");
        String str = props.getProperty(PropertyType.INSETS);

        splashScreenPane = new Pane();

        Image splashScreenImage = loadImage(splashScreenImagePath);
        ImageView splashScreenImageView = new ImageView(splashScreenImage);

        Label splashScreenImageLabel = new Label();
        splashScreenImageLabel.setGraphic(splashScreenImageView);
        
        
        initSplashScreenButtons();
        splashScreenPane.getChildren().add(splashScreenImageView);
        
        VBox buttons = new VBox(10);
        splashScreenPane.getChildren().add(buttons);
        buttons.setLayoutX(paneWidth * 0.45);
        buttons.setLayoutY(paneHeight * 0.65);
        ObservableList children = buttons.getChildren();
        children.add(startButton);
        children.add(loadButton);
        children.add(aboutButton);
        children.add(quitButton);
        splashScreenPane.setMaxSize(paneWidth, paneHeight);
        initTheme();
    }
    public void initSplashScreenButtons() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        startButton = initButton(props.getProperty(PropertyType.START_BUTTON));
        startButton.setOnAction((ActionEvent e) -> {
            changeWorkspace(UIState.GAME_SETUP_STATE);
        });
        loadButton = initButton(props.getProperty(PropertyType.LOAD_BUTTON));
        loadButton.setOnAction((ActionEvent e) -> {
            System.out.println("Not Yet Implemented");
        });
        aboutButton = initButton(props.getProperty(PropertyType.ABOUT_BUTTON));
        aboutButton.setOnAction((ActionEvent e) -> {
            changeWorkspace(UIState.ABOUT_SCREEN_STATE);
        });
        quitButton = initButton(props.getProperty(PropertyType.QUIT_BUTTON));
        quitButton.setOnAction((ActionEvent e) -> {
            System.exit(0);
        });
    }

    public void initGameSetupPane() {
        gameSetupPane = new Pane();
    }

    public void initAboutScreenPane() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        aboutScreenPane = new Pane();
        Label logo = new Label();
        logo.setGraphic(new ImageView(loadImage(props.getProperty(PropertyType.LOGO_IMG))));
        Label about = new Label("Journey through Europe is a family board game published by Ravensburger. "
                + "The board is a map of Europe with various major cities marked, for example,\nAthens, "
                + "Amsterdam and London. The players are given a home city from which they will begin "
                + "and are then dealt a number of cards with various\nother cities on them. "
                + "They must plan a route between each of the cities in their hand of cards. "
                + "On each turn they throw a die and move between the cities.\n"
                + "The winner is the first player to visit each of their cities "
                + "and then return to their home base.");
        logo.setLayoutX(paneWidth * 0.37);
        aboutScreenPane.getChildren().add(logo);
        about.setLayoutX(paneWidth*0.07);
        about.setLayoutY(paneHeight*0.45);
        aboutScreenPane.getChildren().add(about);
        Button back = initButton(props.getProperty(PropertyType.BACK_BUTTON));
        back.setOnAction((ActionEvent e) -> {
            changeWorkspace(UIState.SPLASH_SCREEN_STATE);
        });
        back.setAlignment(Pos.TOP_LEFT);
        aboutScreenPane.getChildren().add(back);
    }

    public void initMoveHistoryScreenPane() {
        moveHistoryScreenPane = new Pane();
    }

    public void initWorkspace() {
        System.out.println("Init Workspace");
        workspace = new Pane();
        ObservableList children = workspace.getChildren();
        children.add(splashScreenPane);
        children.add(gameSetupPane);
        children.add(aboutScreenPane);
        children.add(moveHistoryScreenPane);
        splashScreenPane.setVisible(true);
        gameSetupPane.setVisible(false);
        aboutScreenPane.setVisible(false);
        moveHistoryScreenPane.setVisible(false);
        mainPane.setCenter(workspace);
    }

    public void initTheme() {

    }

    public BorderPane getMainPane() {
        return mainPane;
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    private Image loadImage(String imageName) {
        Image img = new Image(imgPath + imageName);
        return img;
    }

    private Button initButton(String img) {
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent;"
                + "-fx-focus-color: transparent;");
        button.setGraphic(new ImageView(loadImage(img)));
        button.setPadding(marginlessInsets);

        button.setOnMousePressed((MouseEvent e) -> {
            button.setStyle(STYLE_PRESSED);
        });

        button.setOnMouseReleased((MouseEvent e) -> {
            button.setStyle(STYLE_RELEASED);
        });

        return button;
    }
    
     /**
     * This function selects the UI screen to display based on the uiScreen
     * argument. Note that we have 3 such screens: game, stats, and help.
     *
     * @param uiScreen The screen to be switched to.
     */
    public void changeWorkspace(UIState uiScreen) {
        switch (uiScreen) {
            case SPLASH_SCREEN_STATE:
                splashScreenPane.setVisible(true);
                aboutScreenPane.setVisible(false);
                break;
            case ABOUT_SCREEN_STATE:
                splashScreenPane.setVisible(false);
                aboutScreenPane.setVisible(true);
                break;
            case GAME_SETUP_STATE:
                splashScreenPane.setVisible(false);
                gameSetupPane.setVisible(true);
        }

    }
}
