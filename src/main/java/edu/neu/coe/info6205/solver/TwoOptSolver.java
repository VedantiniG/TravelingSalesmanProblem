package main.java.edu.neu.coe.info6205.solver;

import main.java.edu.neu.coe.info6205.model.City;

import java.util.ArrayList;
import java.util.List;

public class TwoOptSolver {
    public static List<Integer> twoOpt(List<Integer> circuit, List<City> cities) {
        int numberOfCities = cities.size();
        List<Integer> improvedPath = new ArrayList<>();
        improvedPath.addAll(circuit);
        boolean improvement = true;
        while(improvement) {
            improvement = false;
            for (int i = 0; i < numberOfCities - 2; i ++) {
                for (int j = i + 2; j < numberOfCities; j++) {
                    double d1 = distanceCal(cities, improvedPath.get(i), improvedPath.get(i + 1))
                            + distanceCal(cities, improvedPath.get(j), improvedPath.get((j + 1)%numberOfCities));
                    double d2 = distanceCal(cities, improvedPath.get(i), improvedPath.get(j))
                            + distanceCal(cities, improvedPath.get(i + 1), improvedPath.get((j + 1)%numberOfCities));
                    if (d1 > d2) {
                        reverseOrder( improvedPath, i + 1, j);
                        improvement = true;
                    }
                }
            }
        }
        return improvedPath;
    }

    private static double distanceCal(List<City> cities, int c1, int c2) {
        double x1 = cities.get(c1).getLongitude();
        double y1 = cities.get(c1).getLatitude();
        double x2 = cities.get(c2).getLongitude();
        double y2 = cities.get(c2).getLatitude();
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    private static void reverseOrder(List<Integer> path, int x, int y) {
        while (x < y) {
            int temp = path.get(x);
            path.set(x, path.get(y));
            path.set(y, temp);
            x ++;
            y --;
        }
    }
}
