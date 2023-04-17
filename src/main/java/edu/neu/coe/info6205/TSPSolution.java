package main.java.edu.neu.coe.info6205;

import main.java.edu.neu.coe.info6205.model.City;
import main.java.edu.neu.coe.info6205.model.Edge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static main.java.edu.neu.coe.info6205.solver.ChristofidesSolver.*;

public class TSPSolution {
    static String csv = "resources/dataset.csv";
    static String l;
    static String split = ",";

    public static void main(String[] args) {

        List<City> cities = new ArrayList<>();

        //add cities to list
        addCities(cities);

        //print list of cities
        for (City city : cities) {
            System.out.println(city);
        }

        double[][] distMatrix = new double[cities.size()][cities.size()];
        //create distance matrix
        createDistanceMatrix(cities, distMatrix);

        // print matrix
        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                System.out.printf("%10.2f", distMatrix[i][j]);
            }
            System.out.println();
        }

        List<Edge> mst = primsAlgoMST(cities);

        // print the edges of the MST
        System.out.println("Minimum Spanning Tree:");
        System.out.println("Size of MST:" + mst.size());
        for (Edge edge : mst) {
            System.out.println(cities.get(edge.getSource()).getId() + " - " + cities.get(edge.getDestination()).getId() +
                    "  distance: " + String.format("%.2f", edge.getWt()));
        }

        List<Integer> oVertices = new ArrayList<>();
        int[] degree = new int[cities.size()];
        for (Edge edge : mst) {
            degree[edge.getSource()]++;
            degree[edge.getDestination()]++;
        }
        for (int i = 0; i < degree.length; i++) {
            if (degree[i] % 2 == 1) {
                oVertices.add(i);
            }
        }
        System.out.println("Vertices with odd degree: " + oVertices);
        System.out.println("Vertices with odd degree: " + oVertices.size());

        double[][] wtMatrix = new double[oVertices.size()][oVertices.size()];
        for (int i = 0; i < oVertices.size(); i++) {
            for (int j = i + 1; j < oVertices.size(); j++) {
                //int x = oVertices.get(i);
                //int y = oVertices.get(j);
                wtMatrix[i][j] = wtMatrix[j][i] = distanceCalculator(cities.get(i), cities.get(j));
            }
        }

        List<Edge> matching = matching(distMatrix, oVertices);

        System.out.println("Minimum-weight perfect matching:");
        double sum = 0;
        for (Edge edge : matching) {
            System.out.println(edge.getSource() + " -- " + edge.getDestination() + " : " + edge.getWt());
            sum += edge.getWt();
        }
        System.out.println(sum);

        List<Edge> multiGraph = mstAndMatching(mst, matching);

        double multiSum = 0;

        System.out.println("Edges of the multigraph:");
        for (Edge edge : multiGraph) {
            System.out.println(edge.getSource() + " - " + edge.getDestination() + " : " + edge.getWt());
            multiSum += edge.getWt();
        }
        System.out.println("MultiGraph: " + multiSum);
        System.out.println("MultiGraph: " + multiGraph.size());


        List<Integer> circuit = eulerianCircuit(multiGraph);
        System.out.println("Eulerian circuit: " + circuit + circuit.size());


        List<Integer> hamiltonianCircuit = hamiltonianCircuit(circuit);

        // print the Hamiltonian circuit
        System.out.println("Hamiltonian circuit: " + hamiltonianCircuit + hamiltonianCircuit.size());

        double tspSolution = getSolution(circuit, cities);
        System.out.println("TSP Solution: " + tspSolution);

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
