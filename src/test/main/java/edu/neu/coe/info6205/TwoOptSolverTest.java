package test.main.java.edu.neu.coe.info6205;

import main.java.edu.neu.coe.info6205.model.City;
import main.java.edu.neu.coe.info6205.solver.ThreeOptSolver;
import main.java.edu.neu.coe.info6205.solver.TwoOptSolver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TwoOptSolverTest {
    @Test
    public void testTwoOpt() {
        List<City> cities;
        List<Integer> circuit;
        cities = new ArrayList<>();
        cities.add(new City("City 0", 0.0, 0.0));
        cities.add(new City("City 1", 1.0, 1.0));
        cities.add(new City("City 2", 2.0, 2.0));
        cities.add(new City("City 3", 3.0, 3.0));

        circuit = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        List<Integer> expected = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        List<Integer> actual = TwoOptSolver.twoOpt(circuit, cities);
        assertEquals(expected, actual);
    }

}
