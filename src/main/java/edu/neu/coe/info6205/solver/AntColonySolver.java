//package main.java.edu.neu.coe.info6205.solver;
//
//
//import main.java.edu.neu.coe.info6205.model.City;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AntColonySolver {
//    private int numCities;
//    private double[][] distanceMatrix;
//    private int numAnts;
//    private int maxIterations;
//    private double alpha;
//    private double beta;
//    private double evaporationRate;
//    private double pheromoneDecay;
//    private double initialPheromone;
//    private double[][] pheromoneMatrix;
//    private Ant[] ants; // ants
//
//
//    public AntColonySolver(int numCities, double[][] distanceMatrix) {
//        this.numCities = numCities;
//        this.distanceMatrix = distanceMatrix;
//        this.numAnts = 10; // default number of ants
//        this.maxIterations = 100; // default maximum iterations
//        this.alpha = 1.0; // default alpha value
//        this.beta = 5.0; // default beta value
//        this.evaporationRate = 0.1; // default evaporation rate
//        this.pheromoneDecay = 0.1; // default pheromone decay rate
//        this.initialPheromone = 0.1; // default initial pheromone value
//        this.pheromoneMatrix = new double[numCities][numCities];
//        this.ants = new Ant[numAnts];
//        initializePheromoneMatrix();
//        initializeAnts();
//    }
//
//    public List<Integer> solve() {
//        // Initialize pheromone matrix
//        initializePheromoneMatrix();
//
//        List<Integer> bestTour = null;
//        double bestTourLength = Double.MAX_VALUE;
//
//        for (int i = 0; i < maxIterations; i++) {
//            List<List<Integer>> antTours = new ArrayList<>();
//            double[] antTourLengths = new double[numAnts];
//
//            // Generate tours for each ant
//            for (int j = 0; j < numAnts; j++) {
//                List<Integer> tour = generateAntTour();
//                antTours.add(tour);
//                antTourLengths[j] = calculateTourLength(tour);
//            }
//
//            // Update pheromone matrix
//            updatePheromoneMatrix(antTours, antTourLengths);
//
//            // Find best tour of this iteration
//            for (int j = 0; j < numAnts; j++) {
//                if (antTourLengths[j] < bestTourLength) {
//                    bestTourLength = antTourLengths[j];
//                    bestTour = antTours.get(j);
//                }
//            }
//
//            // Evaporate pheromone
//            evaporatePheromone();
//        }
//
//        return bestTour;
//    }
//
//   
//
//    private List<Integer> generateAntTour() {
//        List<Integer> tour = new ArrayList<>();
//        boolean[] visited = new boolean[numCities];
//        int currentCity = 0; // Start from city 0
//
//        // Mark the current city as visited
//        visited[currentCity] = true;
//        tour.add(currentCity);
//
//        while (tour.size() < numCities) {
//            int nextCity = selectNextCity(currentCity, visited);
//            visited[nextCity] = true;
//            tour.add(nextCity);
//            currentCity = nextCity;
//        }
//
//        // Return to the starting city to complete the tour
//        tour.add(0);
//        
//        return tour;
//    }
//
//    private int selectNextCity(int currentCity, boolean[] visited) {
//        double[] probabilities = new double[numCities];
//        double totalProbability = 0.0;
//
//        // Calculate the probability of selecting each unvisited city
//        for (int i = 0; i < numCities; i++) {
//            if (!visited[i]) {
//                probabilities[i] = Math.pow(pheromoneMatrix[currentCity][i], alpha)
//                        * Math.pow(1.0 / distanceMatrix[currentCity][i], beta);
//                totalProbability += probabilities[i];
//            }
//        }
//
//        // Choose the next city based on the calculated probabilities
//        double random = Math.random() * totalProbability;
//        double cumulativeProbability = 0.0;
//        for (int i = 0; i < numCities; i++) {
//            if (!visited[i]) {
//                cumulativeProbability += probabilities[i];
//                if (cumulativeProbability >= random) {
//                    return i;
//                }
//            }
//        }
//
//        // Fallback to selecting the city with the maximum pheromone if all cities are visited
//        for (int i = 0; i < numCities; i++) {
//            if (!visited[i]) {
//                return i;
//            }
//        }
//
//        return -1;
//    }
//
//    private double calculateTourLength(List<Integer> tour) {
//        double length = 0.0;
//        for (int i = 0; i < numCities - 1; i++) {
//            length += distanceMatrix[tour.get(i)][tour.get(i + 1)];
//        }
//        // Add distance from last city back to starting city
//        length += distanceMatrix[tour.get(numCities - 1)][tour.get(0)];
//        return length;
//    }
//
//    private void updatePheromoneMatrix(List<List<Integer>> antTours, double[] antTourLengths) {
//        // Update pheromone on each edge based on ant tours
//        for (int i = 0; i < numCities; i++) {
//            for (int j = 0; j < numCities; j++) {
//                if (i != j) {
//                    double deltaPheromone = 0.0;
//                    for (int k = 0; k < numAnts; k++) {
//                        if (antTours.get(k).contains(i) && antTours.get(k).contains(j)) {
//                            deltaPheromone += 1.0 / antTourLengths[k];
//                        }
//                    }
//                    pheromoneMatrix[i][j] = (1.0 - pheromoneDecay) * pheromoneMatrix[i][j] + deltaPheromone;
//                }
//            }
//        }
//    }
//
//    private void evaporatePheromone() {
//        // Evaporate pheromone on each edge by the evaporation rate
//        for (int i = 0; i < numCities; i++) {
//            for (int j = 0; j < numCities; j++) {
//                pheromoneMatrix[i][j] *= (1.0 - evaporationRate);
//            }
//        }
//    }
//
//    // Setters and Getters for parameters
//
//    public void setNumAnts(int numAnts) {
//        this.numAnts = numAnts;
//    }
//
//    public void setMaxIterations(int maxIterations) {
//        this.maxIterations = maxIterations;
//    }
//
//    public void setAlpha(double alpha) {
//        this.alpha = alpha;
//    }
//
//    public void setBeta(double beta) {
//        this.beta = beta;
//    }
//
//    public void setPheromoneDecay(double pheromoneDecay) {
//        this.pheromoneDecay = pheromoneDecay;
//    }
//
//    public void setEvaporationRate(double evaporationRate) {
//        this.evaporationRate = evaporationRate;
//    }
//
//    // Utility methods
//
//    private void initializePheromoneMatrix() {
//        for (int i = 0; i < numCities; i++) {
//            for (int j = 0; j < numCities; j++) {
//                pheromoneMatrix[i][j] = initialPheromone;
//            }
//        }
//    }
//
//    private void initializeAnts() {
//        // Initialize ants' positions to random cities
//        for (int i = 0; i < numAnts; i++) {
//            ants[i] = new Ant();
//            ants[i].setPosition((int) (Math.random() * numCities));
//        }
//    }
//
//    // Internal class for Ant representation
//
//    private class Ant {
//        private int position;
//        private List<Integer> tour;
//
//        public Ant() {
//            tour = new ArrayList<>();
//        }
//
//        public int getPosition() {
//            return position;
//        }
//
//        public void setPosition(int position) {
//            this.position = position;
//            tour.add(position);
//        }
//
//        public List<Integer> getTour() {
//            return tour;
//        }
//    }
//}
//
//

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
