package main.java.edu.neu.coe.info6205.ui;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

public class TSPVisualSwing extends JFrame {

    private Tour bestTour;

    public TSPVisualizationSwing() {
        super("TSP Christofides");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        BorderPane mainRoot = new BorderPane();
        mainRoot.setPadding(new Insets(14));
        JPanel root = new JPanel();
        root.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        root.setLayout(null);
        mainRoot.add(root, BorderLayout.CENTER);

        JPanel buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.Y_AXIS));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton fileButton = new JButton("Load From File");
        buttonBox.add(fileButton);
        JLabel label = new JLabel();
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 100, 15, 100));
        mainRoot.add(label, BorderLayout.SOUTH);
        mainRoot.add(buttonBox, BorderLayout.EAST);

        add(mainRoot);

        fileButton.addActionListener(e -> {
            root.requestFocus();
            CityManager.getInstance().clear();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(Paths.get("").toAbsolutePath().toString()));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
                }

                public String getDescription() {
                    return "Text Files (*.txt)";
                }
            });
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                root.removeAll();
                try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                    List<City> collect = reader.lines()
                            .map(line -> {
                                String[] split = line.split(" ");
                                return new City(Double.parseDouble(split[0]) * 5, Double.parseDouble(split[1]) * 5);
                            })
                            .collect(Collectors.toList());
                    collect.forEach(city -> {
                        CityManager.getInstance().addCity(city);
                        JPanel circle = new JPanel();
                        circle.setBounds((int) city.getLocation().getX() - 10, (int) city.getLocation().getY() - 10, 20, 20);
                        circle.setBackground(Color.BLUE);
                        circle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                        root.add(circle);
                        bestTour = null;
                        label.setText("Total City: " + CityManager.getInstance().numberOfCities() + " | Ctrl+Space: GA, Space: PSO");
                    });
                } catch (FileNotFoundException ex) {

                } catch (IOException ex) {

                }
            }
            root.revalidate();
            root.repaint();
        });

        root.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                root.requestFocus();
                CityManager.getInstance().addCity(new City(event.getX(), event.getY()));
                JPanel circle = new JPanel();
                circle.setBounds(event.getX() - 10, event.getY() - 10, 20, 20);
                circle.setBackground(Color.BLUE);
                circle.setBorder(BorderFactory.createLineBorder(Color,BLACK, 1));
                root.add(circle);
                bestTour = null;
                label.setText("Total City: " + CityManager.getInstance().numberOfCities() + " | Ctrl+Space: GA, Space: PSO");
                root.revalidate();
                root.repaint();
                }
                });
                root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "solve_pso");
                root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK), "solve_ga");
                root.getActionMap().put("solve_pso", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        if (CityManager.getInstance().numberOfCities() < 2) {
                            return;
                        }
                        Swarm swarm = new Swarm(CityManager.getInstance().numberOfCities());
                        Tour tour = swarm.solve();
                        if (bestTour == null || tour.getFitness() < bestTour.getFitness()) {
                            bestTour = tour;
                            label.setText("Total City: " + CityManager.getInstance().numberOfCities() + " | PSO: " + bestTour.getFitness());
                            root.repaint();
                        }
                    }
                });
                root.getActionMap().put("solve_ga", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        if (CityManager.getInstance().numberOfCities() < 2) {
                            return;
                        }
                        int popSize = 50;
                        double eliteRate = 0.1;
                        double mutationRate = 0.01;
                        int maxGen = 200;
                        Population population = new Population(CityManager.getInstance().numberOfCities(), popSize);
                        TSPSolver solver = new GASolver(population, eliteRate, mutationRate);
                        GeneticAlgorithmEngine engine = new GeneticAlgorithmEngine(solver, maxGen);
                        Tour tour = engine.solve();
                        if (bestTour == null || tour.getFitness() < bestTour.getFitness()) {
                            bestTour = tour;
                            label.setText("Total City: " + CityManager.getInstance().numberOfCities() + " | GA: " + bestTour.getFitness());
                            root.repaint();
                        }
                    }
                });
            }
            
            public void paint(Graphics g) {
                super.paint(g);
                if (bestTour != null) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(2));
                    g2.setColor(Color.RED);
                    for (int i = 0; i < bestTour.getSize() - 1; i++) {
                        City c1 = bestTour.getCity(i);
                        City c2 = bestTour.getCity(i + 1);
                        g2.drawLine((int) c1.getLocation().getX(), (int) c1.getLocation().getY(), (int) c2.getLocation().getX(), (int) c2.getLocation().getY());
                    }
                }
            }
            
            public static void main(String[] args) {
                TSPVisualizationSwing tsp = new TSPVisualizationSwing();
                tsp.setVisible(true);
            }
        }
            
