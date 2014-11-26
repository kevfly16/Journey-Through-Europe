/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import javafx.geometry.Point2D;

/**
 *
 * @author Kevin
 */
public class Map {

    private HashMap<String, City> cities;
    private ArrayList<City> locations;
    private ArrayList<City> flights;

    public Map() {
        cities = new HashMap();
        locations = new ArrayList();
        flights = new ArrayList();
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

        src.setMinDistance(0);
        PriorityQueue<City> cityQueue = new PriorityQueue<>();
        cityQueue.add(src);

        while (!cityQueue.isEmpty()) {
            City city = cityQueue.poll();
            // visit each connection a city has
            for (City c : city.getCities()) {
                double distance = city.getMinDistance() + 1;
                if (distance < c.getMinDistance()) {
                    cityQueue.remove(c);
                    c.setMinDistance(distance);
                    c.setPreviousCity(city);
                    cityQueue.add(c);
                }
            }
        }
        
        ArrayList<City> path = new ArrayList();
        City city = dest;
        while (city != src) {
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
    
    public void addFlight(City city) {
        flights.add(city);
    }
    
    public ArrayList<City> getFlights() {
        return flights;
    }
    
    public City getFlight(String name) {
        for(City city : flights) {
            if(city.getName().equalsIgnoreCase(name))
                return city;
        }
        
        return null;
    }
    
    public void setFlights(ArrayList<City> l) {
        flights = l;
    }
    
    public boolean hasFlight(City src, City dest) {
        boolean hasSrc = false;
        boolean hasDest = false;
        for(City city : flights) {
            if(city.getName().equalsIgnoreCase(src.getName())) {
                hasSrc = true;
            }
            
            if(city.getName().equalsIgnoreCase(dest.getName())) {
                hasDest = true;
            }
        }
        
        return hasSrc && hasDest;
    }

    public void sort(ArrayList<City> list) {
        Collections.sort(list);
    }

    public City findCity(Point2D point, ArrayList<City> list) {
        int minIndex = binSearch(point.getX() - 20, list);
        int min = (int) point.getX() - 20;
        int max = (int) point.getX() + 20;

        if (minIndex == -1) {
            return null;
        }

        while (list.get(minIndex).getPos().getX() > min && minIndex != 0) {
            minIndex--;
        }

        for (int i = minIndex; i < list.size() && list.get(i).getPos().getX() < max; i++) {
            City city = list.get(i);
            if (city.computeDistance(point) < City.getRadius()) {
                return city;
            }
        }

        return null;
    }

    private int binSearch(double val, ArrayList<City> list) {
        int low = 0;
        int high = list.size();
        int mid = -1;
        while (low < high) {
            mid = (low + high) / 2;
            int x = (int) list.get(mid).getPos().getX();
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
