package main.java.edu.neu.coe.info6205.solver;

import main.java.edu.neu.coe.info6205.model.City;

import java.util.ArrayList;
import java.util.List;

import static main.java.edu.neu.coe.info6205.util.DistanceCalculator.distanceCalculator;

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
                    double d1 = distanceCalculator(cities, improvedPath.get(i), improvedPath.get(i + 1))
                            + distanceCalculator(cities, improvedPath.get(j), improvedPath.get((j + 1)%numberOfCities));
                    double d2 = distanceCalculator(cities, improvedPath.get(i), improvedPath.get(j))
                            + distanceCalculator(cities, improvedPath.get(i + 1), improvedPath.get((j + 1)%numberOfCities));
                    if (d1 > d2) {
                        reverseOrder( improvedPath, i + 1, j);
                        improvement = true;
                    }
                }
            }
        }

        return improvedPath;
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
