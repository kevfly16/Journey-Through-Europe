/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import Game.Card;
import Game.City;
import Game.Map;
import Main.Main.PropertyType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            Card card = new Card(city[0], "", city[1], false, 0, null, false, city[2]);
            cards.put(city[0], card);
        }
    }
}
