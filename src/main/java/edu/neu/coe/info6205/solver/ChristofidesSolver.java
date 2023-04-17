package main.java.edu.neu.coe.info6205.solver;

import main.java.edu.neu.coe.info6205.model.City;
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
    public static List<Edge> primsAlgoMST(List<City> cities) {

        int numberOfCities = cities.size();
        int[] parent = new int[numberOfCities];
        double[] key = new double[numberOfCities];
        boolean[] mstSet = new boolean[numberOfCities];

        Arrays.fill(key, Double.POSITIVE_INFINITY);

        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
        priorityQueue.offer(new Edge(-1, 0, 0));
        key[0] = 0;
        parent[0] = -1;

        while (!priorityQueue.isEmpty()) {
            Edge edge = priorityQueue.poll();
            int x = edge.getDestination();

            if (mstSet[x]) continue;

            mstSet[x] = true;
            for (int y = 0; y < numberOfCities; y++) {
                double weight = distanceCalculator(cities.get(x), cities.get(y));
                if (!mstSet[y] && weight < key[y]) {
                    key[y] = weight;
                    priorityQueue.offer(new Edge(x, y, weight));
                    parent[y] = x;
                }
            }
        }

        List<Edge> mst = new ArrayList<>();
        double addition = 0;
        for (int x = 1; x < numberOfCities; x++) {
            int y = parent[x];
            double weight = distanceCalculator(cities.get(x), cities.get(y));
            mst.add(new Edge(x, y, weight));
            addition += weight;
        }
        System.out.println("MST ======================================================== " + addition);
        return mst;
    }

    public static List<Edge> matching(double[][] weightMatrix, List<Integer> oVertices) {
        List<Edge> matching = new ArrayList<>();
        int n = oVertices.size();
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        double[] distance = new double[n];

        for (int i = 0; i < n - 1; i++) {
            int y = -1;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (y == -1 || distance[j] < distance[y])) {
                    y = j;
                }
            }
            visited[y] = true;
            for (int x = 0; x < n; x++) {
                if (weightMatrix[oVertices.get(x)][oVertices.get(y)] < distance[x]) {
                    distance[x] = weightMatrix[oVertices.get(x)][oVertices.get(y)];
                    parent[x] = y;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (parent[i] != i) {
                int x = oVertices.get(i);
                int y = oVertices.get(parent[i]);
                double wt = weightMatrix[x][y];
                matching.add(new Edge(x, y, wt));
            }
        }
        return matching;
    }

    public static List<Edge> mstAndMatching(List<Edge> mst, List<Edge> matching) {
        List<Edge> combined = new ArrayList<>(mst);
        for (Edge edge : matching) {
            int x = edge.getSource();
            int y = edge.getDestination();
            boolean contains = false;
            for (Edge edge1 : combined) {
                if ((edge1.getSource() == x && edge1.getDestination() == y) || (edge1.getSource() == y && edge1.getDestination() == x)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                combined.add(edge);
            }
        }
        return combined;
    }

    public static List<Integer> eulerianCircuit(List<Edge> edges) {

        // building adjacency matrix to represent multi graph
        Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
        for (Edge edge : edges) {
            int x = edge.getSource();
            int y = edge.getDestination();
            adjacencyList.computeIfAbsent(x, k -> new ArrayList<>()).add(y);
            adjacencyList.computeIfAbsent(y, k -> new ArrayList<>()).add(x);
        }

        // initializing a stack and circuit list
        Stack<Integer> stack = new Stack<>();
        List<Integer> circuit = new ArrayList<>();

        // starting at random point and adding it to stack
        int start = edges.get(0).getSource();
        stack.push(start);

        while (!stack.isEmpty()) {
            int z = stack.peek();

            // add one to stack if current point has neighbours
            if (adjacencyList.containsKey(z) && !adjacencyList.get(z).isEmpty()) {
                int u = adjacencyList.get(z).get(0);
                stack.push(u);
                adjacencyList.get(z).remove(Integer.valueOf(u));
                adjacencyList.get(u).remove(Integer.valueOf(z));
            } else {
                // current point is part of circuit if no neighbours present
                circuit.add(stack.pop());
            }
        }

        // Reverse the circuit
        Collections.reverse(circuit);
        return circuit;
    }

    public static List<Integer> hamiltonianCircuit(List<Integer> circuit) {

        List<Integer> hCircuit = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        for (int point : circuit) {
            if (!visited.contains(point)) {
                hCircuit.add(point);
                visited.add(point);
            }
        }

        return hCircuit;
    }

    public static double getSolution(List<Integer> circuit, List<City> cities) {
        double totalCost = 0.0;

        for (int i = 0; i < circuit.size() - 1; i++) {
            int x = circuit.get(i);
            int y = circuit.get(i + 1);
            totalCost += distanceCalculator(cities.get(x), cities.get(y));
        }

        // adding cost to go back to start city
        int fCity = circuit.get(0);
        int lCity = circuit.get(circuit.size() - 1);
        totalCost += distanceCalculator(cities.get(lCity), cities.get(fCity));
        return totalCost;
    }
}
