package main.java.edu.neu.coe.info6205.util;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

import main.java.edu.neu.coe.info6205.model.City;
import main.java.edu.neu.coe.info6205.solver.ThreeOptSolver;
import main.java.edu.neu.coe.info6205.solver.TwoOptSolver;
import main.java.edu.neu.coe.info6205.solver.SimulatedAnnealingSolver;
import main.java.edu.neu.coe.info6205.solver.AntColonySolver;


public class CircuitUtil {
    public static double circuitLength(int numberOfCities, double[][] distMatrix, List<Integer> circuit) {
        double len = 0;
        for (int i = 0; i < numberOfCities; i++) {
            int j = (i + 1) % numberOfCities;
            len += distMatrix[circuit.get(i)][circuit.get(j)];
        }
        return len;
    }

    public static void displayCircuit(List<City> cities, double[][] distMatrix, List<Integer> circuit) {
        TourScatterplot circuitplot = new TourScatterplot(cities, circuit, 1400, 1000);
        int n = cities.size();

        JScrollPane scrollPane = new JScrollPane(circuitplot);
        scrollPane.setPreferredSize(circuitplot.getPreferredSize());

        MouseWheelListener zoomListener = e -> {


            if (e.getWheelRotation() < 0) {

                circuitplot.setZoomFactor(1.1 * circuitplot.getZoomFactor());

                circuitplot.revalidate();
                circuitplot.repaint();

            }

            if (e.getWheelRotation() > 0) {

                circuitplot.setZoomFactor(circuitplot.getZoomFactor() / 1.1);

                circuitplot.revalidate();
                circuitplot.repaint();
            }


        };
        scrollPane.addMouseWheelListener(zoomListener);

        JFrame frame = new JFrame("Circuit plot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton twoOptButton = new JButton("Two-Opt");
        JButton threeOptButton = new JButton("Three-Opt");
        JButton simulatedButton = new JButton("Simulated Annealing");
        JButton antColony = new JButton("Ant Colony");

        buttonPanel.add(twoOptButton);
        buttonPanel.add(threeOptButton);
        buttonPanel.add(simulatedButton);
        buttonPanel.add(antColony);
        frame.getContentPane().add(buttonPanel, BorderLayout.EAST);

        twoOptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("----------------Two-Opt----------------");
                List<Integer> twoOptTour = TwoOptSolver.twoOpt(circuit, cities);
                double twoOptTourLength = CircuitUtil.circuitLength(n, distMatrix, twoOptTour);
                System.out.println("Optimized Circuit length: " + twoOptTourLength);
                displayCircuit(cities, distMatrix, twoOptTour);
            }
        });

        threeOptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("----------------Three-Opt----------------");
                List<Integer> threeOptCircuit = ThreeOptSolver.threeOpt(circuit, distMatrix, cities.size());
                double threeOptCircuitLength = CircuitUtil.circuitLength(n, distMatrix, threeOptCircuit);
                System.out.println("Optimized Circuit length: " + threeOptCircuitLength);
                displayCircuit(cities, distMatrix, threeOptCircuit);
            }
        });

        simulatedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("----------------Simulated Anealing----------------");
                //  List<Integer> simulatedCircuit = SimulatedAnnealingSolver.solve(n, dist, tour);
                //  double simulatedCircuitLength = CircuitUtil.circuitLength(n, dist, simulatedCircuit);
                //  System.out.println("Optimized tour length: " + simulatedCircuitLength);
                //  displayTour(cities, dist, simulatedCircuit);
            }
        });

        antColony.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("----------------Three-Opt----------------");
                
                AntColonySolver antColony = new AntColonySolver(n, distMatrix);
                List<Integer> antColonyCircuit = antColony.solve();
                double antColonyCircuitLength = CircuitUtil.circuitLength(n, distMatrix, antColonyCircuit);
                System.out.println("Optimized Circuit length: " + antColonyCircuitLength);
                displayCircuit(cities, distMatrix, antColonyCircuit);
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

public static class TourScatterplot extends JPanel {
    private static final int POINT_SIZE = 5;
    private List<City> cities;
    private List<Integer> tour;
    private int width, height;
    private double minX, maxX, minY, maxY;

    public double zoomFactor = 1;

    public boolean zoomer = true;

    public AffineTransform at;

    public TourScatterplot(List<City> cities, List<Integer> tour, int width, int height) {
        this.cities = cities;
        this.tour = tour;
        this.width = width;
        this.height = height;
        this.minX = cities.get(0).getLongitude();
        this.maxX = cities.get(0).getLongitude();
        this.minY = cities.get(0).getLatitude();
        this.maxY = cities.get(0).getLatitude();
        for (City city : cities) {
            if (city.getLongitude() < minX) minX = city.getLongitude();
            if (city.getLongitude() > maxX) maxX = city.getLongitude();
            if (city.getLatitude() < minY) minY = city.getLatitude();
            if (city.getLatitude() > maxY) maxY = city.getLatitude();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (zoomer) {
            at = g2.getTransform();
            at.scale(zoomFactor, zoomFactor);
            zoomer = false;
        }
        g2.transform(at);

        g.setColor(Color.GREEN);
        for (int i = 0; i < tour.size() - 1; i++) {
            int city1Index = tour.get(i);
            int city2Index = tour.get(i + 1);
            City city1 = cities.get(city1Index);
            City city2 = cities.get(city2Index);
            int x1 = (int) ((city1.getLongitude() - minX) / (maxX - minX) * width / 3);
            int y1 = (int) ((city1.getLatitude() - minY) / (maxY - minY) * height / 3);
            int x2 = (int) ((city2.getLongitude() - minX) / (maxX - minX) * width / 3);
            int y2 = (int) ((city2.getLatitude() - minY) / (maxY - minY) * height / 3);
            g.drawLine(x1, y1, x2, y2);
        }
        g.setColor(Color.YELLOW);
        int i = 0;
        for (City city : cities) {
            int x = (int) ((city.getLongitude() - minX) / (maxX - minX) * width / 3);
            int y = (int) ((city.getLatitude() - minY) / (maxY - minY) * height / 3);
            g.fillOval(x - POINT_SIZE / 4, y - POINT_SIZE / 4, POINT_SIZE, POINT_SIZE);

            FontMetrics fm = g.getFontMetrics();
            double textWidth = fm.getStringBounds(String.valueOf(i), g).getWidth();
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(i), (int) (x - textWidth / 2),
                    (int) (y + fm.getMaxAscent() / 2));

            i++;
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public void setZoomFactor(double factor) {

        if (factor < this.zoomFactor) {
            this.zoomFactor = this.zoomFactor / 1.1;
        } else {
            this.zoomFactor = factor;
        }
        this.zoomer = true;
    }


    public double getZoomFactor() {
        return zoomFactor;
    }
}
}
