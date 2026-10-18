package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public final int vertices;
    public final List<List<Edge>> adj;
    public final List<Edge> edges;

    public Graph(int vertices){
        this.vertices = vertices;
        this.adj = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++){
            this.adj.add(new ArrayList<>());
        }
        this.edges = new ArrayList<>();
    }

    public void addUndirectedEdge(int u, int v, int weight){
        Edge e1 = new Edge(u, v, weight);
        Edge e2 = new Edge(u, v, weight);
        adj.get(u).add(e1);
        adj.get(v).add(e2);
        edges.add(e1);
    }

    public void addDirectedEdge(int u, int v, int weight) {
        Edge e = new Edge(u, v, weight);
        adj.get(u).add(e);
        edges.add(e);
    }
}
