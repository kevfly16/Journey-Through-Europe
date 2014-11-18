/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import File.FileLoader;
import Game.Card;
import Game.City;
import Game.GameData;
import Game.GameStateManager;
import Game.GameStateManager.GameState;
import Game.Player;
import Main.Main.PropertyType;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import properties_manager.PropertiesManager;

/**
 *
 * @author Kevin
 */
public class UI {

    private final GameStateManager gsm;
    private CardAnimation cardAnim;
    private final MoveAnimation moveAnim;
    private final DocumentManager docManager;
    private final JTEEventHandler eventHandler;
    private static ErrorHandler errorHandler;
    private Stage primaryStage;
    private BorderPane mainPane;
    private Pane workspace;
    private Pane mapPane;
    private ScrollPane gamePane;
    private ImageView mapImage;
    private Pane splashScreenPane;
    private GridPane gameSetupPane;
    private ArrayList<GridPane> playerSelectPanes;
    private BorderPane gameplayScreenPane;
    private Pane aboutScreenPane;
    private Pane moveHistoryScreenPane;
    private Pane flightPlanPane;
    private int paneWidth;
    private int paneHeight;
    private Button playButton;
    private Button loadButton;
    private Button quitButton;
    private Button goButton;
    private Button startButton;
    private Button moveHistoryButton;
    private Button aboutButton;
    private Button rollButton;
    private Button flightButton;
    private final String imgPath;
    private Insets marginlessInsets;
    private WebView browser;
    private WebEngine webEngine;
    private Pane previousScreenPane;
    private boolean dragging;
    private Label cityName;
    private ParallelTransition parallelTransition;
    private boolean animRunning;
    private Pane cardsPane;
    private VBox infoPane;
    private int cardWidth;
    private int dieWidth;
    private int cardHeight;
    private Point2D currentPos;
    private ArrayList<String> flagPaths;
    private final int cardYFactor = 65;
    private final int cardYConstant = 25;
    private final int velocity = 4;
    private ImageView die;

    /**
     * The UIState represents the four screen states that are possible for the
     * Journey Through Europe game application. Depending on which state is in
     * current use, different controls will be visible.
     */
    public enum UIState {

        SPLASH_SCREEN_STATE, ABOUT_SCREEN_STATE, GAME_SETUP_STATE, GAMEPLAY_SCREEN_STATE, PREVIOUS_SCREEN_STATE, HISTORY_SCREEN_STATE
    }

    public UI() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        imgPath = "file:" + props.getProperty(PropertyType.IMG_PATH);
        gsm = new GameStateManager(this);
        moveAnim = new MoveAnimation();
        docManager = new DocumentManager();
        eventHandler = new JTEEventHandler(this);
        errorHandler = new ErrorHandler(primaryStage);
        initMainPane();
        initSplashScreenPane();
        initGameSetupPane();
        initGameplayScreenPane();
        initAboutScreenPane();
        initMoveHistoryScreenPane();
        initWorkspace();
        dragging = false;
        animRunning = false;
        currentPos = new Point2D(0, 0);
    }

    private void initMainPane() {
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
        mainPane.setStyle("-fx-background-color: #D1B48C;");
    }

    private void initSplashScreenPane() {
        // INIT THE SPLASH SCREEN CONTROLS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String splashScreenImagePath = props
                .getProperty(PropertyType.SPLASH_SCREEN_IMAGE_NAME);
        props.addProperty(PropertyType.INSETS, "5");
        String str = props.getProperty(PropertyType.INSETS);

        splashScreenPane = new Pane();

        Image splashScreenImage = loadImage(splashScreenImagePath);
        ImageView splashScreenImageView = new ImageView(splashScreenImage);
        splashScreenImageView.setFitWidth(paneWidth);
        splashScreenImageView.setFitHeight(paneHeight);
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
            System.out.println("TODO");
        });
        aboutButton = initButton(props.getProperty(PropertyType.ABOUT_BUTTON));
        aboutButton.setOnAction((ActionEvent e) -> {
            changeWorkspace(UIState.ABOUT_SCREEN_STATE);
        });
        quitButton = initButton(props.getProperty(PropertyType.QUIT_BUTTON));
        quitButton.setOnAction((ActionEvent e) -> {
            System.exit(0);
        });
        previousScreenPane = splashScreenPane;
    }

    private void initGameSetupPane() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        gameSetupPane = new GridPane();
        ChoiceBox choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("1", "2", "3", "4", "5", "6");
        choiceBox.setValue("6");
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                initPanes((int) newValue + 1);
            }

        });
        initPanes(6);
        Label l = new Label(props.getProperty(PropertyType.NUM_PLAYERS_TEXT) + "\t");
        goButton = initButton(props.getProperty(PropertyType.GO_BUTTON));
        goButton.setPadding(new Insets(0, 0, 0, 5));
        goButton.setOnAction((ActionEvent e) -> {
            eventHandler.startGame();
        });
        HBox top = new HBox(5);
        ObservableList children = top.getChildren();
        children.add(l);
        children.add(choiceBox);
        children.add(goButton);
        top.setPadding(new Insets(0, 0, 10, 0));
        gameSetupPane.add(top, 0, 0);
    }

    public void initPanes(int panes) {
        for (int i = 0; playerSelectPanes != null && i < playerSelectPanes.size(); i++) {
            gameSetupPane.getChildren().remove(playerSelectPanes.get(i));
        }
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // GET FLAGS
        ArrayList<String> flags = props.getPropertyOptionsList(PropertyType.FLAG);
        flagPaths = flags;
        String player = props.getProperty(PropertyType.PLAYER_TEXT);
        String computer = props.getProperty(PropertyType.COMPUTER_TEXT);
        String nameStr = props.getProperty(PropertyType.NAME_TEXT);
        playerSelectPanes = new ArrayList();
        for (int i = 1; i <= panes; i++) {
            GridPane playerSelectPane = new GridPane();
            BorderStroke stroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, null);
            playerSelectPane.setPadding(new Insets(paneHeight * 0.15 + 15, paneWidth * 0.055, paneHeight * 0.15 + 15, paneWidth * 0.055));
            playerSelectPane.setBorder(new Border(stroke));
            Label flag = new Label();
            flag.setGraphic(new ImageView(loadImage(flags.get(i - 1))));
            flag.setPadding(new Insets(-10, 10, 0, 0));
            playerSelectPane.add(flag, 0, 1);
            ToggleGroup group = new ToggleGroup();
            RadioButton buttonPlayer = new RadioButton(player);
            buttonPlayer.setPadding(new Insets(0, 30, 0, 0));
            RadioButton buttonComputer = new RadioButton(computer);
            buttonComputer.setPadding(new Insets(-10, 10, 0, 0));
            buttonPlayer.setToggleGroup(group);
            buttonComputer.setToggleGroup(group);
            if (i == 1) {
                buttonPlayer.setSelected(true);
            } else {
                buttonComputer.setSelected(true);
            }
            playerSelectPane.add(buttonPlayer, 1, 0);
            playerSelectPane.add(buttonComputer, 1, 1);
            Label name = new Label(nameStr);
            playerSelectPane.add(name, 2, 0);
            TextField nameField = new TextField(player + " " + i);
            nameField.setPrefWidth(100);
            playerSelectPane.add(nameField, 2, 1);
            if (i <= 3) {
                gameSetupPane.add(playerSelectPane, i - 1, 2);
            } else {
                gameSetupPane.add(playerSelectPane, i - 4, 3);
            }
            playerSelectPanes.add(playerSelectPane);
        }
    }

    private void initGameplayScreenPane() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        gameplayScreenPane = new BorderPane();
        cardWidth = Integer.parseInt(props.getProperty(PropertyType.CARD_WIDTH));
        cardHeight = Integer.parseInt(props.getProperty(PropertyType.CARD_HEIGHT));
        dieWidth = Integer.parseInt(props.getProperty(PropertyType.DIE_WIDTH));
        mapPane = new Pane();
        mapImage = new ImageView(loadImage(props.getProperty(PropertyType.MAP_IMG)));
        mapPane.getChildren().setAll(mapImage);
        // wrap the scene contents in a pannable scroll pane.
        gamePane = createScrollPane(mapPane);
        // center the scroll contents.
        gamePane.setHvalue(gamePane.getHmin() + (gamePane.getHmax() - gamePane.getHmin()) / 2);
        gamePane.setVvalue(gamePane.getVmin() + (gamePane.getVmax() - gamePane.getVmin()) / 2);
        mapImage.setOnMouseDragged((MouseEvent e) -> {
            dragging = true;
        });
        mapImage.setOnMouseClicked((MouseEvent e) -> {
            currentPos = new Point2D(e.getX(), e.getY());
            if (!dragging) {
                Point2D point = new Point2D(e.getX(), e.getY());
                City city = GameData.getMap().findCity(point);
                if (city != null) {
                    eventHandler.movePlayer(city);
                }
            }
            dragging = false;
        });

        Button about = initButton(props.getProperty(PropertyType.ABOUT_BUTTON));
        about.setOnAction((ActionEvent e) -> {
            changeWorkspace(UIState.ABOUT_SCREEN_STATE);
        });

        moveHistoryButton = initButton(props.getProperty(PropertyType.HISTORY_BUTTON));
        moveHistoryButton.setOnAction((ActionEvent e) -> {
            changeWorkspace(UIState.HISTORY_SCREEN_STATE);
        });

        cityName = new Label();
        HBox top = new HBox(2);
        top.getChildren().addAll(about, moveHistoryButton, cityName);

        cardsPane = new Pane();
        Label label = new Label();
        label.setLayoutX(0);
        label.setLayoutY(0);
        cardsPane.getChildren().add(label);

        infoPane = new VBox();
        die = new ImageView(loadImage(props.getPropertyOptionsList(PropertyType.DIE_IMG).get(5)));
        rollButton = initButton(props.getProperty(PropertyType.ROLL_BUTTON));
        rollButton.setOnAction((ActionEvent e) -> {
            eventHandler.rollDie();
        });
        flightButton = initButton(props.getProperty(PropertyType.FLIGHT_BUTTON));
        infoPane.getChildren().addAll(die, rollButton, flightButton);

        gameplayScreenPane.setTop(top);
        gameplayScreenPane.setCenter(gamePane);
        gameplayScreenPane.setLeft(cardsPane);
        gameplayScreenPane.setRight(infoPane);
    }

    private void initAboutScreenPane() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        aboutScreenPane = new Pane();
        Label logo = new Label();
        logo.setGraphic(new ImageView(loadImage(props.getProperty(PropertyType.LOGO_IMG))));
        browser = new WebView();
        webEngine = browser.getEngine();
        loadPage(webEngine, PropertyType.ABOUT_FILE_NAME);
        logo.setLayoutX(paneWidth * 0.37);
        browser.setLayoutX(paneWidth * 0.15);
        browser.setLayoutY(paneHeight * 0.25);
        Button back = initButton(props.getProperty(PropertyType.BACK_BUTTON));
        back.setOnAction((ActionEvent e) -> {
            changeWorkspace(UIState.PREVIOUS_SCREEN_STATE);
        });
        back.setAlignment(Pos.TOP_LEFT);
        aboutScreenPane.getChildren().addAll(logo, browser, back);
    }

    private void initMoveHistoryScreenPane() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        moveHistoryScreenPane = new Pane();
        ScrollPane moveScroll = new ScrollPane();
        Label logo = new Label();
        logo.setGraphic(new ImageView(loadImage(props.getProperty(PropertyType.LOGO_IMG))));
        browser = new WebView();
        webEngine = browser.getEngine();
        loadPage(webEngine, PropertyType.HISTORY_FILE_NAME);
        logo.setLayoutX(paneWidth * 0.37);
        moveScroll.setContent(browser);
        moveScroll.setMaxHeight(525);
        moveScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        moveScroll.setLayoutX(paneWidth * 0.15);
        moveScroll.setLayoutY(paneHeight * 0.25);
        Button back = initButton(props.getProperty(PropertyType.BACK_BUTTON));
        back.setOnAction((ActionEvent e) -> {
            changeWorkspace(UIState.PREVIOUS_SCREEN_STATE);
        });
        back.setAlignment(Pos.TOP_LEFT);
        moveHistoryScreenPane.getChildren().addAll(logo, moveScroll, back);
    }

    private void initWorkspace() {
        System.out.println("Init Workspace");
        workspace = new Pane();
        workspace.getChildren().addAll(splashScreenPane, gameSetupPane,
                aboutScreenPane, moveHistoryScreenPane, gameplayScreenPane);
        splashScreenPane.setVisible(true);
        gameSetupPane.setVisible(false);
        aboutScreenPane.setVisible(false);
        moveHistoryScreenPane.setVisible(false);
        gameplayScreenPane.setVisible(false);
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
            // FOR LATER IMPLEMENTATION
            //button.setStyle(STYLE_PRESSED);
        });

        button.setOnMouseReleased((MouseEvent e) -> {
            // FOR LATER IMPLEMENTATION
            //button.setStyle(STYLE_RELEASED);
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
                previousScreenPane = splashScreenPane;
                splashScreenPane.setVisible(true);
                aboutScreenPane.setVisible(false);
                break;
            case ABOUT_SCREEN_STATE:
                splashScreenPane.setVisible(false);
                gameplayScreenPane.setVisible(false);
                aboutScreenPane.setVisible(true);
                break;
            case GAME_SETUP_STATE:
                splashScreenPane.setVisible(false);
                gameSetupPane.setVisible(true);
                break;
            case GAMEPLAY_SCREEN_STATE:
                previousScreenPane = gameplayScreenPane;
                gameSetupPane.setVisible(false);
                moveHistoryScreenPane.setVisible(false);
                gameplayScreenPane.setVisible(true);
                break;
            case PREVIOUS_SCREEN_STATE:
                splashScreenPane.setVisible(false);
                aboutScreenPane.setVisible(false);
                gameSetupPane.setVisible(false);
                gameplayScreenPane.setVisible(false);
                moveHistoryScreenPane.setVisible(false);
                previousScreenPane.setVisible(true);
                break;
            case HISTORY_SCREEN_STATE:
                gameplayScreenPane.setVisible(false);
                moveHistoryScreenPane.setVisible(true);
                break;
        }
    }

    /**
     * @return a ScrollPane which scrolls the layout.
     */
    private ScrollPane createScrollPane(Pane layout) {
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(true);
        scroll.setPrefSize(paneWidth - cardWidth - dieWidth - dieWidth * 0.5, paneHeight);
        scroll.setContent(layout);
        return scroll;
    }

    public void loadPage(WebEngine engine, PropertyType type) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String filePath = props.getProperty(type);
        try {
            String text = FileLoader.loadTextFile(filePath);
            webEngine.loadContent(text);
            Reader stringReader = new StringReader(text);
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            HTMLDocument doc = (HTMLDocument) htmlKit.createDefaultDocument();
            htmlKit.read(stringReader, doc, 0);
        } catch (IOException | BadLocationException ex) {
            errorHandler.processError(PropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }

    public void dealAnimate(LinkedList<Player> players) {
        ObservableList<Node> children = cardsPane.getChildren();
        if (players.size() == 0) {
            Player player = gsm.getGameData().getCurrentPlayer();
            children.remove(1, children.size());
            Label label = (Label) children.get(0);
            label.setText(player.getName());
            ArrayList<Card> cards = player.getCards();
            for (Card card : cards) {
                cardsPane.getChildren().add(card.getCardIcon());
            }
            return;
        }
        LinkedList cards = new LinkedList(players.getFirst().getCards());
        if (children.size() > 1) {
            children.remove(1, children.size());
        }
        Label label = (Label) children.get(0);
        label.setText(players.getFirst().getName());
        players.removeFirst();
        cardsAnimate(players, cards, cards.size());
    }

    public void cardsAnimate(LinkedList<Player> players, LinkedList<Card> cards, int size) {
        if (cards.size() < 1) {
            animRunning = false;
            dealAnimate(players);
            return;
        }
        animRunning = true;
        ImageView card = new ImageView(loadImage(cards.getFirst().getPath()));
        cards.getFirst().setCardIcon(card);
        card.setFitWidth(cardWidth);
        card.setFitHeight(cardHeight);
        card.setOnMouseClicked(e -> {
            if (!animRunning) {

            }
        });
        cardsPane.getChildren().add(card);
        TranslateTransition translateTransition
                = new TranslateTransition(Duration.millis(3000), card);
        translateTransition.setFromX(paneWidth);
        translateTransition.setToX(0);
        translateTransition.setFromY(paneHeight * 0.3);
        translateTransition.setToY(cardYFactor * (size - cards.size()) + cardYConstant);
        translateTransition.setCycleCount(1);
        ScaleTransition scaleTransition
                = new ScaleTransition(Duration.millis(1500), card);
        scaleTransition.setToX(2f);
        scaleTransition.setToY(2f);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                translateTransition,
                scaleTransition
        );

        parallelTransition.setOnFinished((ActionEvent event) -> {
            cards.removeFirst();
            cardsAnimate(players, cards, size);
        });

        parallelTransition.play();
    }

    public void initPlayerIcons() {
        ArrayList<Player> players = GameData.getPlayers();
        int index = 0;
        for (Player player : players) {
            ImageView playerIcon = new ImageView(loadImage(player.getIconURL()));
            playerIcon.setLayoutX(player.getStartingCity().getPos().getX() - 50);
            playerIcon.setLayoutY(player.getStartingCity().getPos().getY() - 100);
            mapPane.getChildren().add(playerIcon);
            player.setPlayerIcon(playerIcon);
            index++;
        }
    }

    public void movePlayer(Player player, double x, double y, City city) {
        if (animRunning) {
            return;
        }
        animRunning = true;
        ImageView playerIcon = player.getPlayerIcon();
        double time = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * velocity;
        TranslateTransition translateTransition
                = new TranslateTransition(Duration.millis(time), playerIcon);
        translateTransition.setFromX(0);
        translateTransition.setToX(x);
        translateTransition.setFromY(0);
        translateTransition.setToY(y);
        translateTransition.setCycleCount(1);
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setOnFinished((ActionEvent event) -> {
            animRunning = false;
            player.setCurrentPosition(city);
            playerIcon.setTranslateX(0);
            playerIcon.setTranslateY(0);
            playerIcon.setLayoutX(playerIcon.getLayoutX() + x);
            playerIcon.setLayoutY(playerIcon.getLayoutY() + y);
            gsm.nextMove();
        });
        translateTransition.play();
    }

    public ArrayList<GridPane> getPlayerSelectPanes() {
        return playerSelectPanes;
    }

    public static ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public CardAnimation getCardAnimation() {
        return cardAnim;
    }

    public ScrollPane getGamePane() {
        return gamePane;
    }

    public ArrayList<String> getFlagPaths() {
        return flagPaths;
    }

    public Pane getMapPane() {
        return mapPane;
    }

    public GameStateManager getGSM() {
        return gsm;
    }

    public boolean isAnimRunning() {
        return animRunning;
    }

    public void loadDie(String path) {
        die = new ImageView(loadImage(path));
        ObservableList children = infoPane.getChildren();
        children.remove(0);
        children.add(0, die);
    }

    public Pane getCardsPane() {
        return cardsPane;
    }
}
