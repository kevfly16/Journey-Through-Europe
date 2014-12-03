/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import Game.Card;
import Game.City;
import Game.GameData;
import Game.GameStateManager;
import Game.Map;
import Game.Player;
import Main.Main.PropertyType;
import UI.UI;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.ImageView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import properties_manager.PropertiesManager;

/**
 *
 * @author Kevin
 */
public class FileLoader {

    /**
     * This method loads the complete contents of the textFile argument into a
     * String and returns it.
     *
     * @param textFile The name of the text file to load. Note that the path
     * will be added by this method.
     *
     * @return All the contents of the text file in a single String.
     *
     * @throws IOException This exception is thrown when textFile is an invalid
     * file or there is some problem in accessing the file.
     */
    public static String loadTextFile(String textFile) throws IOException {
        // ADD THE PATH TO THE FILE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        textFile = props.getProperty(PropertyType.DATA_PATH) + textFile;

        // WE'LL ADD ALL THE CONTENTS OF THE TEXT FILE TO THIS STRING
        String textToReturn = "";

        // OPEN A STREAM TO READ THE TEXT FILE
        FileReader fr = new FileReader(textFile);
        BufferedReader reader = new BufferedReader(fr);

        // READ THE FILE, ONE LINE OF TEXT AT A TIME
        String inputLine = reader.readLine();
        while (inputLine != null) {
            // APPEND EACH LINE TO THE STRING
            textToReturn += inputLine + "\n";

            // READ THE NEXT LINE
            inputLine = reader.readLine();
        }

        // RETURN THE TEXT
        return textToReturn;
    }

    public static void loadMap(Map map) throws IOException {
        loadCityPoints(map);
        loadFlightPoints(map);
        loadConnections(map);
    }

    private static void loadCityPoints(Map map) throws IOException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(PropertyType.DATA_PATH);
        String citiesFile = props.getProperty(PropertyType.CITIES_FILE);
        String path = dataPath + citiesFile;
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        String seperator = "\t";
        while ((line = br.readLine()) != null) {
            // use space as separator
            String[] city = line.split(seperator);
            City c = new City(city[0]);
            c.setPos(Double.parseDouble(city[3]), Double.parseDouble(city[4]));
            map.addCity(c);
            map.addLocation(c);
        }
    }

    private static void loadFlightPoints(Map map) throws IOException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(PropertyType.DATA_PATH);
        String flightsFile = props.getProperty(PropertyType.FLIGHTS_FILE);
        String path = dataPath + flightsFile;
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        String seperator = "\t";
        while ((line = br.readLine()) != null) {
            // use space as separator
            String[] city = line.split(seperator);
            City c = new City(city[0], Integer.parseInt(city[3]));
            map.getCity(city[0]).setPlane(true);
            c.setPos(Double.parseDouble(city[1]), Double.parseDouble(city[2]));
            map.addFlight(c);
        }
    }

    private static void loadConnections(Map map) {
        try {
            Document dom = parseXmlFile();
            if (dom == null) {
                return;
            }
            parseDocument(dom, map);

        } catch (org.xml.sax.SAXException ex) {
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Document parseXmlFile() throws org.xml.sax.SAXException {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(PropertyType.DATA_PATH);
            String connectionsFile = props.getProperty(PropertyType.CONNECTIONS_FILE);
            //parse using builder to get DOM representation of the XML file
            return db.parse(dataPath + connectionsFile);

        } catch (ParserConfigurationException | IOException pce) {

        }

        return null;
    }

    private static void parseDocument(Document dom, Map map) {
        //get the root element
        Element docEle = dom.getDocumentElement();

        NodeList nl = docEle.getElementsByTagName("cityConnections");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element ele = (Element) nl.item(i);
                String name = ele.getElementsByTagName("name").item(0).getTextContent();
                Element landConnections = (Element) ele.getElementsByTagName("land").item(0);
                Element seaConnections = (Element) ele.getElementsByTagName("sea").item(0);
                NodeList lc = landConnections.getElementsByTagName("city");
                NodeList sc = seaConnections.getElementsByTagName("city");
                City city = map.getCity(name.replace("_", " "));
                if (city == null) {
                    continue;
                }
                for (int j = 0; j < lc.getLength(); j++) {
                    city.addCity(true, map.getCity(lc.item(j).getTextContent()));
                }

                for (int j = 0; j < sc.getLength(); j++) {
                    city.addCity(false, map.getCity(sc.item(j).getTextContent()));
                }
            }
        }
    }

    /**
     *
     * @param cards
     * @throws IOException
     */
    public static void loadCards(HashMap<String, Card> cards) throws IOException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(PropertyType.DATA_PATH);
        String citiesFile = props.getProperty(PropertyType.CARDS_FILE);
        String path = dataPath + citiesFile;
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        String seperator = ",";
        while ((line = br.readLine()) != null) {
            // use space as separator
            String[] city = line.split(seperator);
            Card card = new Card(city[0], "", city[1], city[2]);
            cards.put(city[0], card);
            if (city.length > 3) {
                applyRule(card, city[3]);
            }
        }
    }

    private static void applyRule(Card card, String rule) {
        String arr[] = rule.split(":");
        String ruleName = arr[0];
        String ruleVal = arr[1];
        if (null != ruleName) {
            switch (ruleName) {
                case "Travel":
                    card.setRule(true);
                    card.setRuleName("Travel");
                    if (!ruleVal.equals("card")) {
                        card.setDestCity(GameData.getMap().getCity(ruleVal.toUpperCase()));
                    }
                    break;
                case "Skip":
                    card.setRule(true);
                    card.setRuleName("Skip");
                    card.setSkipTurn(Integer.parseInt(ruleVal));
                    break;
                case "Roll Again":
                    card.setRule(true);
                    card.setRuleName("Roll Again");
                    break;
                case "New Card":
                    card.setRule(true);
                    card.setRuleName("New Card");
                    break;
                case "Score":
                    card.setRule(true);
                    card.setRuleName("Score");
                    card.setScore(Integer.parseInt(ruleVal));
                    break;
                case "Points":
                    card.setRule(true);
                    card.setRuleName("Points");
                    card.setPoints(Integer.parseInt(ruleVal));
                    break;
                case "Roll":
                    card.setRule(true);
                    card.setRuleName("Roll");
                    card.setRoll(Integer.parseInt(ruleVal));
                    break;
                case "Flight":
                    card.setRule(true);
                    card.setRuleName("Flight");
                    break;
                case "Double":
                    card.setRule(true);
                    card.setRuleName("Double");
                    break;
            }
        }
    }

    public static void saveGame(GameStateManager gsm) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(PropertyType.DATA_PATH);
        String savePath = props.getProperty(PropertyType.SAVE_FILE);
        File file = new File(dataPath + savePath);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("Player Turn:" + gsm.getGameData().getCurrentMove() + "\n");
            writer.write("Game State:" + gsm.getGameState().toString() + "\n");
            for(Player player : gsm.getGameData().getPlayers()) {
                String name = player.getName();
                String flagURL = player.getFlagURL();
                String iconURL = player.getIconURL();
                String skippedTurn = player.hasSkippedTurn() + "";
                String currentPosition = player.getCurrentPosition().getName();
                String currentPoints = player.getCurrentPoints() + "";
                String cards = "";
                cards = player.getCards().stream().map((card) -> card.getCity() + " ").reduce(cards, String::concat);
                cards = cards.trim();
                String visited = "";
                visited = player.getVisited().stream().map((city) -> city.getName() + " ").reduce(visited, String::concat);
                visited = visited.trim();
                String roll = player.getRoll() + "";
                String computer = player.isComputer() + "";
                String previousPosition = player.getPreviousPosition().getName();
                String turnStarted = player.isTurnStarted() + "";
                String flied = player.hasFlied() + "";
                String flightPoints = player.getFlightPoints() + "";
                String skip = player.getSkip() + "";
                String score = player.getScore() + "";
                String nextRoll = player.getNextRoll() + "";
                String doubleRoll = player.isDoubleRoll() + "";
                writer.write(name+"|"+flagURL+"|"+iconURL+"|"+skippedTurn+"|"+currentPosition+"|"+currentPoints+"|"
                        +cards+"|"+visited+"|"+roll+"|"+computer+"|"+previousPosition+"|"+turnStarted+"|"+flied+"|"
                        +flightPoints+"|"+skip+"|"+score+"|"+nextRoll+"|"+doubleRoll+"\n");
            }
        } catch (Exception e) {

        } finally {
            try {
                if(writer != null)
                    writer.close();
            } catch (IOException ex) {
                Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void loadGame(GameStateManager gsm) {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(PropertyType.DATA_PATH);
            String savePath = props.getProperty(PropertyType.SAVE_FILE);
            ArrayList<String> list = new ArrayList<>();
            for (String line : Files.readAllLines(Paths.get(dataPath + savePath))) {
                list.add(line);
            }
            gsm.getGameData().setCurrentMove(Integer.valueOf(list.get(0).split(":")[1]));
            gsm.setGameState(list.get(1).split(":")[1]);
            ArrayList<Player> players = new ArrayList();
            for(int i = 2; i < list.size(); i++) {
                String arr[] = list.get(i).split("\\|");
                Player player = new Player(arr[0], arr[1], arr[2],getBool(arr[9]));
                player.setSkippedTurn(getBool(arr[3]));
                player.setCurrentPosition(GameData.getMap().getCity(arr[4]));
                player.setCurrentPoints(Integer.valueOf(arr[5]));
                ArrayList<Card> cards = new ArrayList();
                int index = 0;
                for(String str : arr[6].split(" ")) {
                    Card card = GameData.getCard(str);
                    ImageView cardView = new ImageView(gsm.getUI().loadImage(card.getPath()));
                    cardView.setFitWidth(UI.getCardWidth());
                    cardView.setFitHeight(UI.getCardHeight());
                    cardView.setLayoutX(0);
                    cardView.setLayoutY(UI.getCardYFactor()*index + UI.getCardYConstant());
                    card.setCardIcon(cardView);
                    cards.add(card);
                    index++;
                }
                player.setCards(cards);
                ArrayList<City> visited = new ArrayList();
                for(String str : arr[7].split(" ")) {
                    if(str.equals(""))
                        continue;
                    visited.add(GameData.getMap().getCity(str));
                }
                player.setVisited(visited);
                player.setRoll(Integer.valueOf(arr[8]));
                player.setPreviousPosition(GameData.getMap().getCity(arr[10]));
                player.setTurnStarted(getBool(arr[11]));
                player.setFlied(getBool(arr[12]));
                player.setFlightPoints(Integer.valueOf(arr[13]));
                player.setSkip(Integer.valueOf(arr[14]));
                player.setScore(Integer.valueOf(arr[15]));
                player.setNextRoll(Integer.valueOf(arr[16]));
                player.setDoubleRoll(getBool(arr[17]));
                players.add(player);
            }
            GameData.setPlayers(players);
        } catch (IOException ex) {
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static boolean getBool(String bool) {
        if(bool.equals("true"))
            return true;
        return false;
    }
}
