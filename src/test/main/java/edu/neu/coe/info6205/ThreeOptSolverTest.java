package test.main.java.edu.neu.coe.info6205;

import main.java.edu.neu.coe.info6205.solver.ThreeOptSolver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ThreeOptSolverTest {
    @Test
    public void testThreeOpt() {
        // test case 1
        List<Integer> circuit1 = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        double[][] dist1 = {{0, 1, 2, 3}, {1, 0, 4, 5}, {2, 4, 0, 6}, {3, 5, 6, 0}};
        int n1 = 4;
        List<Integer> result1 = ThreeOptSolver.threeOpt(circuit1, dist1, n1);
        List<Integer> expected1 = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        assertEquals(expected1, result1);

        // test case 2
        List<Integer> circuit2 = new ArrayList<>(Arrays.asList(0, 2, 1, 4, 3));
        double[][] dist2 = {{0, 1, 2, 3, 4}, {1, 0, 5, 6, 7}, {2, 5, 0, 8, 9}, {3, 6, 8, 0, 10}, {4, 7, 9, 10, 0}};
        int n2 = 5;
        List<Integer> result2 = ThreeOptSolver.threeOpt(circuit2, dist2, n2);
        List<Integer> expected2 = new ArrayList<>(Arrays.asList(0, 2, 1, 4, 3));
        assertEquals(expected2, result2);
    }
}
