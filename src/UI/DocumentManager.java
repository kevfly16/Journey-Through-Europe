/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import File.FileLoader;
import Game.GameStateManager;
import Game.Move;
import Main.Main.PropertyType;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import properties_manager.PropertiesManager;

/**
 *
 * @author Kevin
 */
class DocumentManager {

    private final UI ui;

    public DocumentManager(UI initUI) {
        ui = initUI;
    }

    // THESE ARE THE DOCUMENTS WE'LL BE UPDATING HERE
    private HTMLDocument movesDoc;

    // WE'LL USE THESE TO BUILD OUR HTML
    private final String START_TAG = "<";
    private final String END_TAG = ">";
    private final String SLASH = "/";
    private final String SPACE = " ";
    private final String EMPTY_TEXT = "";
    private final String NL = "\n";
    private final String QUOTE = "\"";
    private final String OPEN_PAREN = "(";
    private final String CLOSE_PAREN = ")";
    private final String COLON = ":";
    private final String EQUAL = "=";
    private final String COMMA = ",";
    private final String RGB = "rgb";
    
    private final String MOVE_HISTORY_LIST_ID = "move_history_list";

    public void setMovesDoc(HTMLDocument initMovesDoc) {
        movesDoc = initMovesDoc;
    }

    public HTMLDocument getMovesDoc() {
        return movesDoc;
    }

    public void addMovesToHistoryFile(Move move) {
        try {
            // ADD THE SUBHEADER
            PropertiesManager props = PropertiesManager.getPropertiesManager();

            // AND NOW ADD THE LATEST GAME TO THE LIST
            Element ol = movesDoc.getElement(MOVE_HISTORY_LIST_ID);
            String moveSummary = move.toString(); 
            String htmlText = START_TAG + HTML.Tag.LI + END_TAG + moveSummary + START_TAG + SLASH + HTML.Tag.LI + END_TAG + NL;
            movesDoc.insertBeforeEnd(ol, htmlText);
            FileLoader.writeHistoryFile(loadHTML());
        } // WE'LL LET THE ERROR HANDLER TAKE CARE OF ANY ERRORS,
        // WHICH COULD HAPPEN IF XML SETUP FILES ARE IMPROPERLY
        // FORMATTED
        catch (BadLocationException | IOException e) {
            System.out.println(e.getMessage());
            ErrorHandler errorHandler = UI.getErrorHandler();
            errorHandler.processError(PropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }

    public String loadHTML() throws IOException, BadLocationException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String file = props.getProperty(PropertyType.DEFAULT_FILE_NAME);
        String text = FileLoader.loadTextFile(file);
        // CREATES NEW DOC AND WRITES THE default.html TO IT
        Reader stringReader = new StringReader(text);
        HTMLEditorKit htmlKit = new HTMLEditorKit();
        HTMLDocument doc = (HTMLDocument) htmlKit.createDefaultDocument();
        htmlKit.read(stringReader, doc, 0);

        // LOADS ALL MOVES ONTO HTML PAGE
        ArrayList<Move> moves = ui.getGSM().getGameData().getMoves();
        for (Move move : moves) {
            Element ol = doc.getElement(MOVE_HISTORY_LIST_ID);
            String moveSummary = move.toString(); 
            String htmlText = START_TAG + HTML.Tag.LI + END_TAG + moveSummary + START_TAG + SLASH + HTML.Tag.LI + END_TAG + NL;
            doc.insertBeforeEnd(ol, htmlText);
        }

        HTMLEditorKit kit = new HTMLEditorKit();
        StringWriter writer = new StringWriter();
        kit.write(writer, doc, 0, doc.getLength());
        text = writer.toString();
        return text;
    }
}
