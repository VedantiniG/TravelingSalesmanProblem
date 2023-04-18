package main.java.edu.neu.coe.info6205;

import main.java.edu.neu.coe.info6205.model.City;
import main.java.edu.neu.coe.info6205.model.Edge;
import main.java.edu.neu.coe.info6205.solver.AntColonySolver;
import main.java.edu.neu.coe.info6205.solver.SimulatedAnnealingSolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static main.java.edu.neu.coe.info6205.solver.ChristofidesSolver.*;
import static main.java.edu.neu.coe.info6205.solver.ThreeOptSolver.threeOpt;
import static main.java.edu.neu.coe.info6205.solver.TwoOptSolver.twoOpt;
import static main.java.edu.neu.coe.info6205.util.CircuitUtil.circuitLength;

public class TSPSolution {
    static String csv = "resources/dataset.csv";
    static String l;
    static String split = ",";

    public static void main(String[] args) {

        List<City> cities = new ArrayList<>();

        //add cities to list
        addCities(cities);

        double[][] distMatrix = new double[cities.size()][cities.size()];
        //create distance matrix
        createDistanceMatrix(cities, distMatrix);

        List<Edge> mst = primsAlgoMST(cities.size(), distMatrix);
        List<Integer> oVertices = findingOddVertices(cities.size(), mst);
        List<Edge> match = matching(cities.size(), distMatrix, oVertices);
        List<Edge> graph = new ArrayList<>(mst);

        graph.addAll(match);

        List<Edge> eCircuit = eulerianCircuit(cities.size(), mst, graph);
        List<Integer> hCircuit = hamiltonianCircuit(cities.size(), eCircuit);

        System.out.println("Circuit length: " + circuitLength(cities.size(), distMatrix, hCircuit));
        System.out.print("Minimum Spanning Tree Length: " + getLengthOfMst(mst));

        List<Integer> twoOptCircuit = twoOpt(hCircuit, cities);

        double twoOptSolution = circuitLength(cities.size(), distMatrix, twoOptCircuit);
        System.out.print("\n");
        System.out.println("2-opt Solution: " + twoOptSolution);

//        List<Integer> threeOptCircuit = threeOpt(hCircuit, distMatrix, cities.size());
//
//        double threeOptSolution = circuitLength(cities.size(), distMatrix, threeOptCircuit);
//        System.out.print("\n");
//        System.out.println("3-opt Solution: " + threeOptSolution);

        //Simulated Annealing
        SimulatedAnnealingSolver sim = new SimulatedAnnealingSolver(cities.size(), distMatrix);
        List<Integer> edges =  sim.solve();
//        System.out.println("Simulation circuit: " + edges + edges.size());
        double simWeight = circuitLength(cities.size(), distMatrix, edges);
        System.out.println("Simulated Annealing Solution: " + simWeight);

        //Ant Colony
        AntColonySolver ant = new AntColonySolver(cities.size(), distMatrix);
        List<Integer> antEdges =  ant.solve();
//        System.out.println("AntColony circuit: " + antEdges + antEdges.size());
        double antWeight = circuitLength(cities.size(), distMatrix, antEdges);
        System.out.println("Ant Colony  Solution: " + antWeight);

        System.out.println("-----------Done-------------");

    }

    //method to add cities from csv file
    private static void addCities(List<City> cities) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
            bufferedReader.readLine();
            while ((l = bufferedReader.readLine()) != null) {

                // splitting into cols
                String[] cols = l.split(split);

                // create new city and add to list
                cities.add(new City(cols[0], Double.parseDouble(cols[1]), Double.parseDouble(cols[2])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //method to create the distance matrix
    private static void createDistanceMatrix (List<City> cities, double[][] distMatrix) {
        for (int i = 0; i < cities.size(); i++) {
            for (int j = i + 1; j < cities.size(); j++) {
                double distance = distanceCalculator(cities.get(i), cities.get(j));
                distMatrix[i][j] = distance;
                distMatrix[j][i] = distance;
            }
        }
    }
}
