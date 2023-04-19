package main.java.edu.neu.coe.info6205.solver;

import main.java.edu.neu.coe.info6205.model.City;

import java.util.List;

public class SimulatedAnnealingSolver {
    public static List<Integer> simulatedAnnealing(List<Integer> circuit, double[][] distMatrix, List<City> cities) {

        double circuitLength = circuitLength(cities.size(), distMatrix, circuit);
        double temp = 500.0;
        double coolingRate = 0.001;
        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {

            List<Integer> newCircuit = TwoOptSolver.twoOpt(circuit, cities);
            int newCircuitLength = circuitLength(cities.size(), distMatrix, newCircuit);

            if (newCircuitLength < circuitLength) {

                circuit = newCircuit;
                circuitLength = newCircuitLength;

            } else {

                double acceptanceProbability = acceptanceProbability(circuitLength, newCircuitLength, temp);

                if (Math.random() < acceptanceProbability) {

                    circuit = newCircuit;
                    circuitLength = newCircuitLength;

                }

            }

            temp *= (1 - coolingRate);
        }

        return circuit;
    }

    public static int circuitLength(int numberOfCities, double[][] distMatrix, List<Integer> circuit) {

        int len = 0;

        for (int i = 0; i < numberOfCities - 1; i++) {

            len += distMatrix[circuit.get(i)][circuit.get(i+1)];

        }

        len += distMatrix[circuit.get(numberOfCities-1)][circuit.get(0)];

        return len;
    }

    // Calculate acceptance probability for simulated annealing
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {

        if (newEnergy < energy) {
            return 1.0;
        }

        return Math.exp((energy - newEnergy) / temperature);
    }
}
