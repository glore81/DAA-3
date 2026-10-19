package mst;

import dsu.DisjointSetUnion;
import graph.Graph;
import graph.Edge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kruskal {
    public double totalWeight = 0;
    public boolean isConnected = false;
    public long operations = 0;
    public List<Edge> mstEdges = new ArrayList<>();

    public Kruskal(Graph graph){
        int vCount = graph.vertices;
        if(vCount == 0){
            isConnected = true;
            return;
        }

        List<Edge> sortedEdges = new ArrayList<>(graph.edges);
        Collections.sort(sortedEdges);

        DisjointSetUnion dsu = new DisjointSetUnion(vCount);

        for (Edge edge : sortedEdges){
            operations++;
            if (dsu.union(edge.u, edge.v)){
                mstEdges.add(edge);
                totalWeight += edge.weight;
                if (mstEdges.size() == vCount - 1) break;
            }
        }

        operations += dsu.operationsCount;
        if((vCount <= 1) || (mstEdges.size() == vCount - 1)){
            isConnected = true;
        }
        else {
            isConnected = false;
        }
    }
}
