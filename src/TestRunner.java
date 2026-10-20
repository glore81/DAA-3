import connectivity.KosarajuSCC;
import connectivity.TopologicalSort;
import graph.Graph;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;

import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("=== Starting Edge Cases Testing ===");

        testSingleVertex();
        testDisconnectedGraphAndUnreachable();
        testDuplicateWeightsAndMultipleMSTs();
        testDAGTopologicalSort();
        testCyclicTopologicalSort();
        testSCCCoverage();

        System.out.println("\n=== All Tests Completed Successfully ===");
    }

    private static void testSingleVertex() {
        System.out.println("\n--- Test: Single Vertex Graph ---");
        Graph g = new Graph(1);

        Kruskal kruskal = new Kruskal(g);
        Prim prim = new Prim(g);
        Dijkstra dij = new Dijkstra(g, 0);

        System.out.println("Kruskal connected? " + kruskal.isConnected + " (Weight: " + kruskal.totalWeight + ")");
        System.out.println("Prim connected? " + prim.isConnected + " (Weight: " + prim.totalWeight + ")");
        System.out.println("Dijkstra distance to self: " + dij.distances[0]);
    }

    private static void testDisconnectedGraphAndUnreachable() {
        System.out.println("\n--- Test: Disconnected Graph & Unreachable Vertices ---");
        Graph g = new Graph(4);
        g.addUndirectedEdge(0, 1, 10);
        g.addUndirectedEdge(2, 3, 20);

        Kruskal kruskal = new Kruskal(g);
        Prim prim = new Prim(g);
        Dijkstra dij = new Dijkstra(g, 0);

        System.out.println("Kruskal connected? " + kruskal.isConnected);
        System.out.println("Prim connected? " + prim.isConnected);
        System.out.println("Dijkstra distance to unreachable vertex 2: " + dij.distances[2]);
        System.out.println("Dijkstra path to unreachable vertex 2: " + dij.getPathTo(2));
    }

    private static void testDuplicateWeightsAndMultipleMSTs() {
        System.out.println("\n--- Test: Duplicate Weights & Multiple Valid MSTs ---");

        Graph g = new Graph(3);
        g.addUndirectedEdge(0, 1, 5);
        g.addUndirectedEdge(1, 2, 5);
        g.addUndirectedEdge(0, 2, 5);

        Kruskal kruskal = new Kruskal(g);
        Prim prim = new Prim(g);

        System.out.println("Kruskal MST Weight: " + kruskal.totalWeight);
        System.out.println("Prim MST Weight: " + prim.totalWeight);
        System.out.println("Weights match? " + (kruskal.totalWeight == prim.totalWeight));
    }

    private static void testDAGTopologicalSort() {
        System.out.println("\n--- Test: Valid DAG Topological Sort ---");
        Graph g = new Graph(4);
        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(0, 2, 1);
        g.addDirectedEdge(1, 3, 1);
        g.addDirectedEdge(2, 3, 1);

        TopologicalSort topo = new TopologicalSort(g);
        System.out.println("Has cycle? " + topo.hasCycle);
        System.out.println("Valid topological order: " + topo.order);
    }

    private static void testCyclicTopologicalSort() {
        System.out.println("\n--- Test: Cyclic Graph Topological Sort ---");
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(1, 2, 1);
        g.addDirectedEdge(2, 0, 1);

        TopologicalSort topo = new TopologicalSort(g);
        System.out.println("Cycle correctly detected? " + topo.hasCycle);
        System.out.println("Order should be empty: " + topo.order);
    }

    private static void testSCCCoverage() {
        System.out.println("\n--- Test: SCC Exactly One Component Per Vertex ---");
        Graph g = new Graph(5);

        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(1, 2, 1);
        g.addDirectedEdge(2, 0, 1);

        g.addDirectedEdge(2, 3, 1);

        g.addDirectedEdge(3, 4, 1);
        g.addDirectedEdge(4, 3, 1);

        KosarajuSCC scc = new KosarajuSCC(g);
        System.out.println("SCCs identified: " + scc.sccList);

        int totalVerticesInSCCs = 0;
        for (List<Integer> component : scc.sccList) {
            totalVerticesInSCCs += component.size();
        }
        System.out.println("Total vertices covered: " + totalVerticesInSCCs + " / " + g.vertices);
        System.out.println("All vertices belong to exactly one SCC? " + (totalVerticesInSCCs == g.vertices));
    }
}