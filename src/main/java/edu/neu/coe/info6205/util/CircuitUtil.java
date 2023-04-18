package main.java.edu.neu.coe.info6205.util;

import java.util.List;

public class CircuitUtil {
    public static double circuitLength(int numberOfCities, double[][] distMatrix, List<Integer> circuit) {
        double len = 0;
        for (int i = 0; i < numberOfCities; i++) {
            int j = (i + 1) % numberOfCities;
            len += distMatrix[circuit.get(i)][circuit.get(j)];
        }
        return len;
    }
}
