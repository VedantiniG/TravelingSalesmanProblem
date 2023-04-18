package main.java.edu.neu.coe.info6205.util;

import main.java.edu.neu.coe.info6205.model.City;

import java.util.List;

public class DistanceCalculator {

    public static double distanceCalculator (List<City> cities, int c1, int c2) {
        double x1 = cities.get(c1).getLongitude();
        double y1 = cities.get(c1).getLatitude();
        double x2 = cities.get(c2).getLongitude();
        double y2 = cities.get(c2).getLatitude();
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

}
