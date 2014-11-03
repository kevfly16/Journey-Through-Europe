/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import Game.City;
import Game.Map;
import Main.Main.PropertyType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public static void loadCityPoints(Map map) throws IOException {
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
}
