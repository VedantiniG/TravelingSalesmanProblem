package main.java.edu.neu.coe.info6205.solver;

import java.util.ArrayList;
import java.util.List;

public class ThreeOptSolver {
    public static List<Integer> threeOpt(List<Integer> circuit, double[][] dist, int numberOfCities) {
        boolean improvement = true;

        while (improvement) {
            improvement = false;

            for (int i = 0; i < numberOfCities - 2; i++) {

                for (int j = i + 2; j < numberOfCities - 1; j++) {

                    for (int k = j + 2; k < numberOfCities; k++) {

                        double oldCost = calculateCircuitCost(circuit, dist);
                        List<Integer> imporvedPath = reverse(circuit, i + 1, j, k);
                        double newCost = calculateCircuitCost(imporvedPath, dist);

                        if (newCost < oldCost) {

                            circuit = imporvedPath;
                            improvement = true;

                        }

                    }

                }

            }
        }

        return circuit;
    }

    public static List<Integer> reverse(List<Integer> circuit, int i, int j, int k) {
        List<Integer> improvedPath = new ArrayList<>();
        for (int x = 0; x < i; x++) {
            improvedPath.add(circuit.get(x));
        }
        for (int x = j; x >= i; x--) {
            improvedPath.add(circuit.get(x));
        }
        for (int x = k; x > j; x--) {
            improvedPath.add(circuit.get(x));
        }
        for (int x = k + 1; x < circuit.size(); x++) {
            improvedPath.add(circuit.get(x));
        }
        return improvedPath;
    }

    public static double calculateCircuitCost(List<Integer> circuit, double[][] tsp_g) {
        double cost = 0;
        for (int i = 0; i < circuit.size() - 1; i++) {
            cost += tsp_g[circuit.get(i)][circuit.get(i + 1)];
        }
        cost += tsp_g[circuit.get(circuit.size() - 1)][circuit.get(0)];
        return cost;
    }
}