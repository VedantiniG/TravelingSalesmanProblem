package main.java.edu.neu.coe.info6205.model;

public class Edge implements Comparable<Edge>{
    private int source;
    private int destination;
    private double wt;

    public Edge(int source, int destination, double wt) {
        this.source = source;
        this.destination = destination;
        this.wt = wt;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public double getWt() {
        return wt;
    }

    public void setWt(double wt) {
        this.wt = wt;
    }

    @Override
    public int compareTo(Edge edge) {
        return Double.compare(wt, edge.wt);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", destination=" + destination +
                ", wt=" + wt +
                '}';
    }
}
