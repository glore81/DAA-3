package graph;

public class Edge implements Comparable<Edge>{
    public final int u;
    public final int v;
    public final double weight;

    public Edge(int u, int v, double weight){
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other){
        return Double.compare(this.weight, other.weight);
    }
}
