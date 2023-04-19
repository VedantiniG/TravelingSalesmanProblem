package test.main.java.edu.neu.coe.info6205;

import main.java.edu.neu.coe.info6205.model.City;
import main.java.edu.neu.coe.info6205.model.Edge;
import main.java.edu.neu.coe.info6205.solver.ChristofidesSolver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static main.java.edu.neu.coe.info6205.solver.ChristofidesSolver.*;
import static org.junit.Assert.assertEquals;

public class ChristofidesSolverTest {

    @Test
    public void testDistanceCalculator() {
        City c1 = new City("Boston", 42.3601, -71.0589);
        City c2 = new City("New York City", 40.7128, -74.0060);
        assertEquals(306.1084942114748, distanceCalculator(c1, c2), 0.0001);
    }

    @Test
    public void testPrimsAlgoMST() {
        double[][] distMatrix = new double[][]{
                {0, 2, 4, 1},
                {2, 0, 5, 6},
                {4, 5, 0, 3},
                {1, 6, 3, 0}};
        List<Edge> mst = primsAlgoMST(4, distMatrix);
        double length = ChristofidesSolver.getLengthOfMst(mst);
        assertEquals(6.0, length, 0.0);
    }

    @Test
    public void testGetLengthOfMst() {
        List<Edge> mst = Arrays.asList(new Edge(0, 1, 1.0), new Edge(0, 2, 2.0), new Edge(1, 3, 5.0));
        assertEquals(8.0, ChristofidesSolver.getLengthOfMst(mst), 0.0001);
    }

    @Test
    public void testFindingOddVertices() {
        List<Edge> mst = Arrays.asList(new Edge(0, 1, 1.0), new Edge(0, 2, 2.0), new Edge(1, 3, 5.0));
        List<Integer> expectedOddVertices = Arrays.asList(2, 3);
        assertEquals(expectedOddVertices, findingOddVertices(4, mst));
    }

    @Test
    public void testMatching() {
        double[][] distMatrix = new double[][]{
                {0, 2, 4, 1},
                {2, 0, 5, 6},
                {4, 5, 0, 3},
                {1, 6, 3, 0}};
        List<Integer> oddVertices = new ArrayList<>();
        oddVertices.add(1);
        oddVertices.add(3);
        List<Edge> matching = ChristofidesSolver.matching(4, distMatrix, oddVertices);
        assertEquals(1, matching.size());
    }

    @Test
    public void eulerianCircuitTest() {
        City city0 = new City("Boston", 42.3601, -71.0589);
        City city1 = new City("New York City", 40.7128, -74.0060);
        City city2 = new City("Seattle", 49.7128, -74.9068);
        City city3 = new City("Chicago", 46.9828, -75.0070);

        // Create distance matrix
        double[][] distMatrix = {
                {0, distanceCalculator(city0, city1), distanceCalculator(city0, city2), distanceCalculator(city0, city3)},
                {distanceCalculator(city1, city0), 0, distanceCalculator(city1, city2), distanceCalculator(city1, city3)},
                {distanceCalculator(city2, city0), distanceCalculator(city2, city1), 0, distanceCalculator(city2, city3)},
                {distanceCalculator(city3, city0), distanceCalculator(city3, city1), distanceCalculator(city3, city2), 0}
        };

        // Find MST and matching
        List<Edge> mst = primsAlgoMST(4, distMatrix);
        List<Integer> oVertices = findingOddVertices(4, mst);
        List<Edge> match = matching(4, distMatrix, oVertices);

        // Find Eulerian circuit
        List<Edge> eCircuit = eulerianCircuit(4, mst, match);

        // Check that the circuit visits every vertex exactly once
        int[] visits = new int[4];
        for (Edge e : eCircuit) {
            visits[e.getSource()]++;
            visits[e.getDestination()]++;
        }

        int[] expected = new int[4];
        expected[0] = 1;
        expected[1] = 2;
        expected[2] = 2;
        expected[3] = 1;

        for(int i = 0; i < 4; i ++){
            assertEquals(expected[i],visits[i]);
        }
    }

        @Test
        public void testHamiltonianCircuit() {
            // Create cities
            City city0 = new City("City 0", 0.0, 0.0);
            City city1 = new City("City 1", 1.0, 1.0);
            City city2 = new City("City 2", 2.0, 2.0);
            City city3 = new City("City 3", 3.0, 3.0);

            // Create edges
            Edge edge01 = new Edge(0, 1, ChristofidesSolver.distanceCalculator(city0, city1));
            Edge edge12 = new Edge(1, 2, ChristofidesSolver.distanceCalculator(city1, city2));
            Edge edge23 = new Edge(2, 3, ChristofidesSolver.distanceCalculator(city2, city3));
            Edge edge30 = new Edge(3, 0, ChristofidesSolver.distanceCalculator(city3, city0));
            Edge edge02 = new Edge(0, 2, ChristofidesSolver.distanceCalculator(city0, city2));
            Edge edge13 = new Edge(1, 3, ChristofidesSolver.distanceCalculator(city1, city3));

            // Create list of edges
            List<Edge> edges = new ArrayList<>();
            edges.add(edge01);
            edges.add(edge12);
            edges.add(edge23);
            edges.add(edge30);
            edges.add(edge02);
            edges.add(edge13);

            // Find the Eulerian circuit
            List<Edge> eulerianCircuit = ChristofidesSolver.eulerianCircuit(4, edges, edges);

            // Find the Hamiltonian circuit
            List<Integer> hCircuit = ChristofidesSolver.hamiltonianCircuit(4, eulerianCircuit);

            // Expected output
            List<Integer> expected = new ArrayList<>();
            expected.add(0);
            expected.add(1);
            expected.add(2);
            expected.add(3);
            expected.add(0);

            // Check if the actual output is equal to the expected output
            assertEquals(expected, hCircuit);
        }

}
