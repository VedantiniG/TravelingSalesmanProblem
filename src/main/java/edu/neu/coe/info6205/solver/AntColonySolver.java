
package main.java.edu.neu.coe.info6205.solver;

import main.java.edu.neu.coe.info6205.model.City;
import main.java.edu.neu.coe.info6205.model.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColonySolver {

    private int numCities; // Number of cities
    private double[][] distances; // Distance matrix between cities
    private int numAnts; // Number of ants
    private int maxIterations; // Maximum number of iterations
    private double alpha; // Alpha parameter for pheromone importance
    private double beta; // Beta parameter for distance importance
    private double evaporationRate; // Evaporation rate of pheromone
    private double initialPheromone; // Initial pheromone value
    private double[][] pheromones; // Pheromone matrix
    private Random random; // Random number generator

    public AntColonySolver(int numCities, double[][] distances) {
        this.numCities = numCities;
        this.distances = distances;
        this.numAnts = 10; // Default number of ants
        this.maxIterations = 100; // Default maximum number of iterations
        this.alpha = 1.0; // Default alpha value
        this.beta = 5.0; // Default beta value
        this.evaporationRate = 0.5; // Default evaporation rate
        this.initialPheromone = 0.1; // Default initial pheromone value
        this.pheromones = new double[numCities][numCities];
        this.random = new Random();
        initializePheromones();
    }
    private void initializePheromones() {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] = initialPheromone;
            }
        }
    }

    public List<Integer> solve() {
        // Initialize pheromone matrix
        this.pheromones = new double[numCities][numCities];
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] = initialPheromone;
            }
        }

        List<Integer> bestTour = new ArrayList<>();
        double bestTourLength = Double.POSITIVE_INFINITY;

        // Main loop for ant colony optimization
        for (int iter = 0; iter < maxIterations; iter++) {
            List<List<Integer>> antTours = new ArrayList<>();

            // Construct tours for each ant
            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> tour = constructTour();
                antTours.add(tour);
            }

            // Update pheromone matrix
            updatePheromones(antTours);

            // Find best tour among all ants
            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> tour = antTours.get(ant);
                double tourLength = calculateTourLength(tour);
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = tour;
                }
            }

            // Evaporate pheromone
            evaporatePheromones();
        }

        return bestTour;
    }

    private List<Integer> constructTour() {
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[numCities];
        int startCity = random.nextInt(numCities);
        tour.add(startCity);
        visited[startCity] = true;

        while (tour.size() < numCities) {
            int currentCity = tour.get(tour.size() - 1);
            int nextCity = selectNextCity(currentCity, visited);
            tour.add(nextCity);
            visited[nextCity] = true;
        }

        return tour;
    }

    private int selectNextCity(int currentCity, boolean[] visited) {
        List<Integer> unvisitedCities = new ArrayList<>();
        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
                unvisitedCities.add(i);
            }
        }

        double[] probabilities = new double[unvisitedCities.size()];
        double totalProbability = 0.0;

        for (int i = 0; i < unvisitedCities.size(); i++) {
            int nextCity = unvisitedCities.get(i);
            double pheromone = pheromones[currentCity][nextCity];
            double distance = distances[currentCity][nextCity];
            probabilities[i] = Math.pow(pheromone, alpha) * Math.pow(1.0 / distance, beta);
            totalProbability += probabilities[i];
        }

        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < unvisitedCities.size(); i++) {
            int nextCity = unvisitedCities.get(i);
            cumulativeProbability += probabilities[i] / totalProbability;
            if (randomValue <= cumulativeProbability) {
                return nextCity;
            }
        }

        // If no city is selected, choose the city with the highest probability
        return unvisitedCities.get(unvisitedCities.size() - 1);
    }

    private void updatePheromones(List<List<Integer>> antTours) {
        // Update pheromone matrix with ant tours
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] *= (1 - evaporationRate);
            }
        }

        for (List<Integer> tour : antTours) {
            double tourLength = calculateTourLength(tour);
            double deltaPheromone = 1.0 / tourLength;

            for (int i = 0; i < numCities - 1; i++) {
                int city1 = tour.get(i);
                int city2 = tour.get(i + 1);
                pheromones[city1][city2] += deltaPheromone;
                pheromones[city2][city1] += deltaPheromone;
            }

            int lastCity = tour.get(numCities - 1);
            int firstCity = tour.get(0);
            pheromones[lastCity][firstCity] += deltaPheromone;
            pheromones[firstCity][lastCity] += deltaPheromone;
        }
    }

    private void evaporatePheromones() {
        // Evaporate pheromone from all edges
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] *= (1 - evaporationRate);
            }
        }
    }

    private double calculateTourLength(List<Integer> tour) {
        double tourLength = 0.0;
        for (int i = 0; i < numCities - 1; i++) {
            int city1 = tour.get(i);
            int city2 = tour.get(i + 1);
            tourLength += distances[city1][city2];
        }
        int lastCity = tour.get(numCities - 1);
        int firstCity = tour.get(0);
        tourLength += distances[lastCity][firstCity]; // Add distance from last city to first city
        return tourLength;
    }

}