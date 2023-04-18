package main.java.edu.neu.coe.info6205.solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import main.java.edu.neu.coe.info6205.model.City;

import java.util.ArrayList;
import java.util.List;

public class SimulatedAnnealingSolver {

    private int numCities; // Number of cities
    private double[][] distanceMatrix; // Distance matrix for cities
    private static final double INITIAL_TEMPERATURE = 10000; // Initial temperature for simulated annealing
    private static final double COOLING_RATE = 0.003; // Cooling rate for simulated annealing
    private static final int NUM_ITERATIONS_PER_TEMP = 100; // Number of iterations per temperature for simulated annealing

    public SimulatedAnnealingSolver(int numCities, double[][] distanceMatrix) {
        this.numCities = numCities;
        this.distanceMatrix = distanceMatrix;
    }

    /**
     * Method to solve the TSP using Simulated Annealing algorithm.
     *
     * @return List of integers representing the order of cities in the solution
     */
    public List<Integer> solve() {
        List<Integer> currentSolution = generateRandomSolution(); // Generate a random initial solution
        double currentEnergy = calculateEnergy(currentSolution); // Calculate energy (total distance) of current solution
        double temperature = INITIAL_TEMPERATURE; // Initialize temperature

        while (temperature > 1) {
            for (int i = 0; i < NUM_ITERATIONS_PER_TEMP; i++) {
                List<Integer> newSolution = generateNeighborSolution(currentSolution); // Generate a neighboring solution
                double newEnergy = calculateEnergy(newSolution); // Calculate energy of new solution

                // Decide whether to accept the new solution based on energy difference and temperature
                if (acceptSolution(currentEnergy, newEnergy, temperature)) {
                    currentSolution = newSolution; // Update current solution
                    currentEnergy = newEnergy; // Update current energy
                }
            }
            temperature *= (1 - COOLING_RATE); // Cool down temperature
        }

        return currentSolution;
    }

    /**
     * Method to generate a random initial solution.
     *
     * @return List of integers representing the order of cities in the solution
     */
    private List<Integer> generateRandomSolution() {
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i < numCities; i++) {
            solution.add(i);
        }
        java.util.Collections.shuffle(solution); // Shuffle the list to generate a random solution
        return solution;
    }

    /**
     * Method to generate a neighboring solution by swapping two random cities.
     *
     * @param solution Current solution
     * @return List of integers representing the order of cities in the neighboring solution
     */
    private List<Integer> generateNeighborSolution(List<Integer> solution) {
        List<Integer> newSolution = new ArrayList<>(solution);
        int index1 = (int) (Math.random() * numCities); // Randomly select index of first city
        int index2 = (int) (Math.random() * numCities); // Randomly select index of second city
        while (index2 == index1) {
            index2 = (int) (Math.random() * numCities); // Make sure index2 is different from index1
        }
        // Swap the two cities to generate a neighboring solution
        int temp = newSolution.get(index1);
        newSolution.set(index1, newSolution.get(index2));
        newSolution.set(index2, temp);
        return newSolution;
    }

    
    private double calculateEnergy(List<Integer> solution) {
    double energy = 0;
    for (int i = 0; i < numCities - 1; i++) {
        int city1 = solution.get(i);
        int city2 = solution.get(i + 1);
        energy += distanceMatrix[city1][city2]; // Add distance between consecutive cities
    }
    int lastCity = solution.get(numCities - 1);
    int firstCity = solution.get(0);
    energy += distanceMatrix[lastCity][firstCity]; // Add distance between last and first city to complete the loop
    return energy;
}

/**
 * Method to accept or reject a new solution based on energy difference and temperature.
 *
 * @param currentEnergy Current energy (total distance) of the current solution
 * @param newEnergy     Energy (total distance) of the new solution
 * @param temperature   Current temperature
 * @return true if the new solution is accepted, false otherwise
 */
private boolean acceptSolution(double currentEnergy, double newEnergy, double temperature) {
    if (newEnergy < currentEnergy) {
        return true; // Accept the new solution if it has lower energy (shorter distance)
    } else {
        double acceptanceProbability = Math.exp((currentEnergy - newEnergy) / temperature);
        return Math.random() < acceptanceProbability; // Accept the new solution with a certain probability
    }
}
}
