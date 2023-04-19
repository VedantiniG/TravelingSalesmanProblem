package test.main.java.edu.neu.coe.info6205;

import main.java.edu.neu.coe.info6205.solver.AntColonySolver;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class AntColonySolverTest {
    private double[][] distances = {
            {0, 1, 2},
            {1, 0, 3},
            {2, 3, 0}
    };
    private int numCities = distances.length;
    AntColonySolver solver = new AntColonySolver(numCities, distances);

    @Test
    public void testSolve() {
        List<Integer> tour = solver.solve();
        assertEquals(numCities, tour.size());
        assertEquals(6.0, solver.calculateTourLength(tour), 0.0001);
    }

}
