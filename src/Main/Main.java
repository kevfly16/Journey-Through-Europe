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

    static String PROPERTY_TYPES_LIST = "property_types.txt";
    static String UI_PROPERTIES_FILE_NAME = "properties.xml";
    static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    static String DATA_PATH = "./data/";

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

            Scene scene = new Scene(mainPane, mainPane.getWidth() + 20, mainPane.getHeight() + 10);
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
        /* WINDOW DIMENSIONS */
        WINDOW_WIDTH, WINDOW_HEIGHT,
        /* LEVEL OPTIONS PROPERTIES */
        LEVEL_OPTIONS, LEVEL_FILES, LEVEL_IMAGE_NAMES,
        /* GAME TEXT */
        SPLASH_SCREEN_TITLE_TEXT, GAME_TITLE_TEXT, GAME_SUBHEADER_TEXT, WIN_DISPLAY_TEXT, LOSE_DISPLAY_TEXT, NEXT_LEVEL_TEXT, SELECT_LEVEL_TEXT, RESTART_LEVEL_TEXT, GAME_RESULTS_TEXT, GUESS_LABEL, LETTER_OPTIONS, EXIT_REQUEST_TEXT, YES_TEXT, NO_TEXT, DEFAULT_YES_TEXT, DEFAULT_NO_TEXT, DEFAULT_EXIT_TEXT,
        /* IMAGE FILE NAMES */
        WINDOW_ICON, SPLASH_SCREEN_IMAGE_NAME, GAME_IMG_NAME, STATS_IMG_NAME, HELP_IMG_NAME, EXIT_IMG_NAME, NEW_GAME_IMG_NAME, HOME_IMG_NAME, WALL_IMAGE, BOX_IMAGE, PLACE_IMAGE, BOX_RED_IMAGE, SOKOBAN_IMAGE, RESTART_IMG_NAME, UNDO_IMG_NAME, BACK_IMG_NAME,
        /* DATA FILE STUFF */
        STATS_FILE_NAME, HELP_FILE_NAME, STATS_DOC_NAME, LEVEL_FILE_NAME,
        /* AUDIO FILE STUFF */
        THEME_AUDIO, GRUNT_AUDIO, STEP_AUDIO, BOX_AUDIO,
        /* TOOLTIPS */
        GAME_TOOLTIP, STATS_TOOLTIP, HELP_TOOLTIP, EXIT_TOOLTIP, NEW_GAME_TOOLTIP, HOME_TOOLTIP,
        /*
         * THESE ARE FOR LANGUAGE-DEPENDENT ERROR HANDLING, LIKE FOR TEXT PUT
         * INTO DIALOG BOXES TO NOTIFY THE USER WHEN AN ERROR HAS OCCURED
         */
        ERROR_DIALOG_TITLE_TEXT, IMAGE_LOADING_ERROR_TEXT, INVALID_URL_ERROR_TEXT, INVALID_DOC_ERROR_TEXT, INVALID_XML_FILE_ERROR_TEXT, INVALID_GUESS_LENGTH_ERROR_TEXT, WORD_NOT_IN_DICTIONARY_ERROR_TEXT, INVALID_DICTIONARY_ERROR_TEXT,
        INSETS,
        /* LANGUAGE FILE USED TO DEFINE NECESSARY IMAGES */
        LANGUAGE_FILE
    }
}
