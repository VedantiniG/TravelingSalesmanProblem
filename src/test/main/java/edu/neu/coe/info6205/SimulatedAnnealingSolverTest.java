package test.main.java.edu.neu.coe.info6205;

import main.java.edu.neu.coe.info6205.model.City;
import main.java.edu.neu.coe.info6205.solver.SimulatedAnnealingSolver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class SimulatedAnnealingSolverTest {

    private double[][] distMatrix = {{0.0, 10.0, 20.0, 30.0},
            {10.0, 0.0, 15.0, 25.0},
            {20.0, 15.0, 0.0, 35.0},
            {30.0, 25.0, 35.0, 0.0}};
    private List<City> cities = Arrays.asList(new City("A", 0.0, 0.0),
            new City("B", 0.0, 10.0),
            new City("C", 10.0, 0.0),
            new City("D", 10.0, 10.0));

    @Test
    public void testSimulatedAnnealing() {
        List<Integer> circuit = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        List<Integer> expectedCircuit = new ArrayList<>(Arrays.asList(0, 1, 3, 2));
        List<Integer> actualCircuit = SimulatedAnnealingSolver.simulatedAnnealing(circuit, distMatrix, cities);
        assertEquals(expectedCircuit, actualCircuit);
    }

}
