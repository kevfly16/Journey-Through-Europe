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
public class City implements Comparable {

    private final ArrayList<City> cities;
    private HashMap<City, Integer> weights;
    private final String name;
    private Point2D pos;
    private static double radius;
    private boolean ferry;
    private boolean plane;
    private double minDistance;
    private City previousCity;
    private final ArrayList<City> seaConnections;
    private final ArrayList<City> landConnections;

    public City(String n) {
        name = n;
        cities = new ArrayList();
        seaConnections = new ArrayList();
        landConnections = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public int getNumLinks() {
        return cities.size();
    }

    public ArrayList<City> getCities() {
        return cities;
    }
    
    public void addCity(boolean isLand, City city) {
        if(isLand)
            landConnections.add(city);
        else
            seaConnections.add(city);
        cities.add(city);
    }
    
    public boolean isSeaConnection(City city) {
        return seaConnections.contains(city);
    }

    public City getCity(City city) {
        return cities.get(cities.indexOf(city));
    }

    public boolean getFerry() {
        return ferry;
    }

    public void setFerry(boolean f) {
        ferry = f;
    }

    public boolean hasPlane() {
        return plane;
    }

    public void setPlane(boolean p) {
        plane = p;
    }

    public boolean hasCity(City city) {
        return cities.contains(city);
    }

    public int getWeight(City city) {
        return weights.get(city);
    }

    public void setWeight(City city, int weight) {
        weights.put(city, weight);
    }

    public void setPos(Point2D point) {
        pos = point;
    }

    public void setPos(double x, double y) {
        setPos(new Point2D(x, y));
    }

    public Point2D getPos() {
        return pos;
    }

    public static double getRadius() {
        return radius;
    }

    public static void setRadius(double r) {
        radius = r;
    }

    public double computeDistance(Point2D point) {
        return point.distance(pos);
    }
    
    public void setMinDistance(double min) {
        minDistance = min;
    }
    
    public double getMinDistance() {
        return minDistance;
    }
    
    public void setPreviousCity(City city) {
        previousCity = city;
    }
    
    public City getPreviousCity() {
        return previousCity;
    }

    @Override
    public int compareTo(Object o) {
        City c = (City) o;
        if (pos.getX() < c.getPos().getX()) {
            return -1;
        }
        if (pos.getX() == c.getPos().getX()) {
            return 0;
        } else {
            return 1;
        }
    }

}
