/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Point2D;

/**
 *
 * @author Kevin
 */
public class Map {

    private HashMap<String, City> cities;
    private ArrayList<City> locations;

    public Map() {
        cities = new HashMap();
        locations = new ArrayList();
    }

    public ArrayList<City> getCities() {
        return locations;
    }

    public void setCities(HashMap<String, City> c) {
        cities = c;
    }

    public City breadthFirst(String city) {
        // TODO
        return null;
    }

    public City breadthFirst(City city) {
        // TODO
        return null;
    }

    public City depthFirst(String city) {
        // TODO
        return null;
    }

    public City depthFirst(City city) {
        // TODO
        return null;
    }

    public ArrayList<City> getPath(City src, City dest) {
        return null;
    }

    public void addCity(City city) {
        cities.put(city.getName(), city);
    }

    public void addLocation(City city) {
        locations.add(city);
    }

    public ArrayList<City> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<City> l) {
        locations = l;
    }

    public City findCity(Point2D point) {
        for (City location : locations) {
            if (location.computeDistance(point) < 20) {
                return location;
            }
        }

        return null;
    }
}
