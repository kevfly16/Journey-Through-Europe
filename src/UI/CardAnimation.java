/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Main.Main;
import Main.Main.PropertyType;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import properties_manager.PropertiesManager;

/**
 *
 * @author Kevin
 */
public class CardAnimation extends Thread {

    private final UI ui;
    private final int mapWidth;
    private final int mapHeight;
    private final double x1;
    private final double x2;
    private final double y1;
    private final double y2;

    public CardAnimation(UI initUI, Point2D point1, Point2D point2) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        mapWidth = Integer.parseInt(props.getProperty(PropertyType.MAP_WIDTH));
        mapHeight = Integer.parseInt(props.getProperty(PropertyType.MAP_HEIGHT));
        ui = initUI;
        x1 = point1.getX();
        y1 = point1.getY();
        x2 = point2.getX();
        y2 = point2.getY();
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            double i = x1;
            double j = y1;
            while(i < x2) {
                i+=1;
                j+=1;
                ui.getGamePane().setHvalue(i/mapWidth);
                ui.getGamePane().setVvalue(j/mapHeight);
            }
        });
    }
}
