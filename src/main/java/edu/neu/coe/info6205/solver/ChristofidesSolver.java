package main.java.edu.neu.coe.info6205.solver;

import main.java.edu.neu.coe.info6205.model.City;
import main.java.edu.neu.coe.info6205.model.DisjointSet;
import main.java.edu.neu.coe.info6205.model.Edge;

import java.util.*;

public class ChristofidesSolver {

    public static double distanceCalculator(City c1, City c2) {

        double latitude1 = c1.getLatitude();
        double longitude1 = c1.getLongitude();
        double latitude2 = c2.getLatitude();
        double longitude2 = c2.getLongitude();

        double radiusOfEarth = 6371; //in km
        double dLatitude = Math.toRadians(latitude2 - latitude1);
        double dLongitude = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(dLatitude / 2) * Math.sin(dLatitude / 2) +
                Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                        Math.sin(dLongitude / 2) * Math.sin(dLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = radiusOfEarth * c;
        return d;
    }

    // using Prim's algo to construct minimum spanning tree
    public static List<Edge> primsAlgoMST(int numberOfCities, double[][] distMatrix) {

        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < numberOfCities; i++) {
            for (int j = i + 1; j < numberOfCities; j++) {
                edges.add(new Edge(i, j, distMatrix[i][j]));
            }
        }

        Collections.sort(edges);
        DisjointSet disjointSet = new DisjointSet(numberOfCities);
        List<Edge> mst = new ArrayList<>();
        for (Edge e : edges) {
            if (disjointSet.find(e.getSource()) != disjointSet.find(e.getDestination())) {
                disjointSet.union(e.getSource(), e.getDestination());
                mst.add(e);
            }
        }
        return mst;
    }

    public static double getLengthOfMst (List<Edge> mst) {
        double len = 0;
        for (Edge e : mst) {
            len += e.getWt();
        }

        return len;
    }

    public static List<Integer> findingOddVertices (int numberOfCities, List<Edge> mst) {
        int [] degree = new int[numberOfCities];

        for (Edge e : mst) {
            degree[e.getSource()] ++;
            degree[e.getDestination()] ++;
        }

        List<Integer> oVertices = new ArrayList<>();

        for (int i = 0; i < numberOfCities; i++) {
            if (degree[i] % 2 == 1) {
                oVertices.add(i);
            }
        }

        return oVertices;
    }

    public static List<Edge> matching(int numberOfCities, double[][] distMatrix, List<Integer> oVertices) {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < oVertices.size(); i++) {
            for (int j = i + 1; j < oVertices.size(); j ++) {
               int x = oVertices.get(i);
               int y = oVertices.get(j);
               edges.add(new Edge(x, y, distMatrix[x][y]));
            }
        }

        Collections.sort(edges);
        DisjointSet disjointSet = new DisjointSet(numberOfCities);
        List<Edge> match = new ArrayList<>();
        for (Edge edge : edges) {
            if (disjointSet.find(edge.getSource()) != disjointSet.find(edge.getDestination())) {
                disjointSet.union(edge.getSource(), edge.getDestination());
                match.add(edge);
            }
        }
        return match;
    }

    public static List<Edge> eulerianCircuit(int numberOfCities, List<Edge> mst, List<Edge> match) {
        List<Edge>[] multiGraph = new List[numberOfCities];

        for (int i = 0; i < numberOfCities; i ++) {
            multiGraph[i] = new ArrayList<>();
        }

        for (Edge e : mst) {
            multiGraph[e.getSource()].add(e);
            multiGraph[e.getDestination()].add(e);
        }

        for (Edge e: match) {
            multiGraph[e.getSource()].add(e);
            multiGraph[e.getDestination()].add(e);
        }

        boolean[] visits = new boolean[numberOfCities];

        List<Edge> circuit = new ArrayList<>();
        eulerianCircuitHelper(multiGraph, 0, circuit, visits);

        return circuit;
    }

    public static List<Integer> hamiltonianCircuit(int numberOfCities, List<Edge> eCircuit) {

        int[] position = new int[numberOfCities];

        for (int i = 0; i < eCircuit.size(); i++) {
            Edge e = eCircuit.get(i);
            position[e.getSource()] = i;
            position[e.getDestination()] = i;
        }

        boolean[] visits = new boolean[numberOfCities];
        List<Integer> circuit = new ArrayList<>();

        for (Edge edge : eCircuit) {
            if (!visits[edge.getSource()]) {
                circuit.add(edge.getSource());
                visits[edge.getSource()] = true;
            }
            if (!visits[edge.getDestination()]) {
                circuit.add(edge.getDestination());
                visits[edge.getDestination()] = true;
            }
            if (circuit.size() == numberOfCities) {
                break;
            }
        }

        int i = 0;

        while (circuit.get(i) != 0) {
            i++;
        }

        List<Integer> hCircuit = new ArrayList<>();

        for (int j = i; j < circuit.size(); j++) {
            hCircuit.add(circuit.get(j));
        }

        for (int j = 1; j < i; j++) {
            hCircuit.add(circuit.get(j));
        }

        hCircuit.add(0);

        return hCircuit;
    }

    private static void eulerianCircuitHelper(List<Edge>[] multiGraph, int num, List<Edge> circuit, boolean[] visits) {
        visits[num] = true;
        for (Edge e : multiGraph[num]) {
            int x = e.getSource() == num ? e.getDestination() : e.getSource();
            if (!visits[x]) {
                circuit.add(e);
                eulerianCircuitHelper(multiGraph, x, circuit, visits);
            }
        }
    }

}
