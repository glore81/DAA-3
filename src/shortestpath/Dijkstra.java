package shortestpath;

import graph.Edge;
import graph.Graph;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra  {
    public double[] distances;
    public int[] parent;
    public long operations = 0;

    private static class NodeDistance implements Comparable<NodeDistance> {
        public final int node;
        public final double dist;

        public NodeDistance(int node, double dist) {
            this.node = node;
            this.dist = dist;
        }

        @Override
        public int compareTo(NodeDistance other){
            return Double.compare(this.dist, other.dist);
        }
    }

    public Dijkstra(Graph graph, int source){
        int n = graph.vertices;
        distances = new double[n];
        parent = new int[n];

        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
        distances[source] = 0;

        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        pq.add(new NodeDistance(source, 0));

        while(!pq.isEmpty()) {
            NodeDistance curr = pq.poll();
            operations++;
            int u = curr.node;

            if (curr.dist > distances[u]){
                continue;
            }

            for (Edge e : graph.adj.get(u)){
                operations++;

                if(e.weight < 0){
                    throw new IllegalArgumentException("Dijkstra's algorithm does not support negative edge weights.");
                }

                int v = e.v;
                if(distances[u] + e.weight < distances[v]) {
                    distances[v] = distances[u] + e.weight;
                    parent[v] = u;
                    pq.add(new NodeDistance(v, distances[v]));
                }
            }
        }
    }

    public List<Integer> getPathTo(int target) {
        if(distances[target] == Double.POSITIVE_INFINITY) {
            return Collections.emptyList();
        }

        LinkedList<Integer> path = new LinkedList<>();
        for (int at = target; at != -1; at = parent[at]){
            path.addFirst(at);
        }
        return path;
    }
}
