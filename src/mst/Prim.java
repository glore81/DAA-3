package mst;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {
    public double totalWeight = 0;
    public boolean isConnected = false;
    public long operations = 0;
    public List<Edge> mstEdges = new ArrayList<>();

    public Prim(Graph graph) {
        int vCount = graph.vertices;
        if(vCount == 0){
            isConnected = true;
            return;
        }

        boolean[] inMST = new boolean[vCount];
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        inMST[0] = true;
        for(Edge edge: graph.adj.get(0)) {
            pq.add(edge);
            operations++;
        }

        while (!pq.isEmpty() && mstEdges.size() < vCount - 1){
            Edge edge = pq.poll();
            operations++;

            int nextVertex = -1;
            if(inMST[edge.u] && !inMST[edge.v]){
                nextVertex = edge.v;
            }
            else if (!inMST[edge.u] && inMST[edge.v]){
                nextVertex = edge.u;
            }

            if(nextVertex != -1){
                inMST[nextVertex] = true;
                mstEdges.add(edge);
                totalWeight += edge.weight;

                for(Edge nextEdge : graph.adj.get(nextVertex)) {
                    if(!inMST[nextEdge.v]) {
                        pq.add(nextEdge);
                        operations++;
                    }
                }
            }
        }
        if ((vCount <= 1) || (mstEdges.size() == vCount - 1)) {
            isConnected = true;
        }
        else {
            isConnected = false;
        }
    }
}
