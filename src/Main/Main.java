/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import UI.UI;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 *
 * @author Kevin
 */
public class Main extends Application {

    static String UI_PROPERTIES_FILE_NAME = "properties.xml";
    static String PROPERTIES_SCHEMA_FILE_NAME = "schema.xsd";
    static String DATA_PATH = "./data/";

    @Override
    public void start(Stage primaryStage) {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertyType.UI_PROPERTIES_FILE_NAME,
                    UI_PROPERTIES_FILE_NAME);
            props.addProperty(PropertyType.PROPERTIES_SCHEMA_FILE_NAME,
                    PROPERTIES_SCHEMA_FILE_NAME);
            props.addProperty(PropertyType.DATA_PATH.toString(),
                    DATA_PATH);
            props.loadProperties(UI_PROPERTIES_FILE_NAME,
                    PROPERTIES_SCHEMA_FILE_NAME);

            // GET THE SELECTED LANGUAGE & IT'S XML FILE
            ArrayList<String> languages = props.getPropertyOptionsList(PropertyType.LANGUAGE_FILE);
            String langSchema = props.getProperty(PropertyType.PROPERTIES_SCHEMA_FILE_NAME);
            props.loadProperties(languages.get(0), langSchema);

            // GET THE LOADED TITLE AND SET IT IN THE FRAME
            String title = props.getProperty(PropertyType.SPLASH_SCREEN_TITLE_TEXT);
            primaryStage.setTitle(title);

            UI root = new UI();
            BorderPane mainPane = root.getMainPane();
            root.setStage(primaryStage);

            Scene scene = new Scene(mainPane, mainPane.getWidth(), mainPane.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public enum PropertyType {
        /* SETUP FILE NAMES */

        UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME,
        /* DIRECTORIES FOR FILE LOADING */
        DATA_PATH, IMG_PATH, MUS_PATH,
        /* DIMENSIONS */
        WINDOW_WIDTH, WINDOW_HEIGHT, CARD_WIDTH, CARD_HEIGHT, DIE_WIDTH,
        /* CONSTANTS */
        RADIUS, NUM_CARDS, MAP_WIDTH, MAP_HEIGHT,
        /* HTML FILE NAMES */
        ABOUT_FILE_NAME, HISTORY_FILE_NAME,
        /* DATA FILE NAMES */
        CITIES_FILE, CARDS_FILE, CONNECTIONS_FILE,
        /* GAME TEXT */
        SPLASH_SCREEN_TITLE_TEXT, GAME_TITLE_TEXT, GAME_SUBHEADER_TEXT, WIN_DISPLAY_TEXT, YES_TEXT, OK_TEXT, NO_TEXT, DEFAULT_YES_TEXT, DEFAULT_NO_TEXT, DEFAULT_EXIT_TEXT, PLAYER_TEXT, COMPUTER_TEXT, NAME_TEXT, NUM_PLAYERS_TEXT, POINTS_TEXT, ROLL_AGAIN_TEXT,
        /* IMAGE FILE NAMES */
        WINDOW_ICON, SPLASH_SCREEN_IMAGE_NAME, START_BUTTON, LOAD_BUTTON, ABOUT_BUTTON, QUIT_BUTTON, BACK_BUTTON, HISTORY_BUTTON, ROLL_BUTTON, FLIGHT_BUTTON, GO_BUTTON, LOGO_IMG, PLAYER_ICON, PLAYER_FLAG, FLAG, MAP_IMG, FLIGHT_IMG, DIE_IMG, END_BUTTON,
        /* AUDIO FILE NAME */
        THEME_AUDIO,
        /* TOOLTIPS */
        GAME_TOOLTIP, STATS_TOOLTIP, HELP_TOOLTIP, EXIT_TOOLTIP, NEW_GAME_TOOLTIP, HOME_TOOLTIP,
        /*
         * THESE ARE FOR LANGUAGE-DEPENDENT ERROR HANDLING, LIKE FOR TEXT PUT
         * INTO DIALOG BOXES TO NOTIFY THE USER WHEN AN ERROR HAS OCCURED
         */
        ERROR_DIALOG_TITLE_TEXT, IMAGE_LOADING_ERROR_TEXT, INVALID_URL_ERROR_TEXT, INVALID_DOC_ERROR_TEXT, INVALID_XML_FILE_ERROR_TEXT, INVALID_GUESS_LENGTH_ERROR_TEXT, WORD_NOT_IN_DICTIONARY_ERROR_TEXT,
        INSETS,
        /* LANGUAGE FILE USED TO DEFINE NECESSARY IMAGES */
        LANGUAGE_FILE
    }
}
