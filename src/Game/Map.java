/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;
import java.util.Collections;
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

    public ArrayList<City> getPath(City src, City dest) {
        for (City city : locations) {
            city.setMinDistance(Integer.MAX_VALUE);
            city.setPreviousCity(null);
        }
        
        City city = src;
        City next = null;
        ArrayList<City> citySet = new ArrayList();
        citySet.add(src);
        city.setMinDistance(0);
        while (citySet.size() < locations.size()) {
            int min = Integer.MAX_VALUE;
            ArrayList<City> nodes = city.getCities();
            for (City node : nodes) {
                int distance = src.getMinDistance() + city.getWeight(node);
                if (distance < node.getMinDistance()) {
                    node.setMinDistance(distance);
                    node.setPreviousCity(city);
                }
                if (node.getMinDistance() < min) {
                    min = node.getMinDistance();
                    next = node;
                }
            }
            
            citySet.add(city = next);
        }
        
        ArrayList<City> path = new ArrayList();
        city = dest;
        while(city != src) {
            path.add(0, city);
            city = city.getPreviousCity();
        }
        
        return path;
    }

    public void addCity(City city) {
        cities.put(city.getName(), city);
    }
    
    public City getCity(String city) {
        return cities.get(city);
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

    public void sort() {
        Collections.sort(locations);
    }

    public City findCity(Point2D point) {
        int minIndex = binSearch(point.getX() - 20);
        int min = (int) point.getX() - 20;
        int max = (int) point.getX() + 20;

        if (minIndex == -1) {
            return null;
        }

        while (locations.get(minIndex).getPos().getX() > min && minIndex != 0) {
            minIndex--;
        }

        for (int i = minIndex; i < locations.size() && locations.get(i).getPos().getX() < max; i++) {
            City city = locations.get(i);
            if (city.computeDistance(point) < City.getRadius()) {
                return city;
            }
        }

        return null;
    }

    private int binSearch(double val) {
        int low = 0;
        int high = locations.size();
        int mid = -1;
        while (low < high) {
            mid = (low + high) / 2;
            int x = (int) locations.get(mid).getPos().getX();
            if (x > val - 10 && x < val + 10) {
                break;
            }
            if (x < val) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return mid;
    }
}
