import connectivity.KosarajuSCC;
import connectivity.TopologicalSort;
import graph.Graph;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Benchmark {
    private static final int RUNS = 5;
    private static final long SEED = 42;

    public static void main(String[] args) {
        System.out.println("=== Starting Benchmark ===\n");

        new File("results/tables").mkdirs();

        int[] vertices = {10, 100, 1000};
        int[] edges = {30, 500, 5000};
        String[] sizes = {"Small", "Medium", "Large"};

        try (
                PrintWriter mstCsv = new PrintWriter(new File("results/tables/mst_results.csv"));
                PrintWriter sccCsv = new PrintWriter(new File("results/tables/scc_results.csv"));
                PrintWriter dijCsv = new PrintWriter(new File("results/tables/dijkstra_results.csv"))
        ) {

            System.out.println("\nTable 1: Minimum Spanning Tree (Kruskal vs Prim)");
            mstCsv.println("Size,V,E,Kruskal_Time_ms,Kruskal_Ops,Prim_Time_ms,Prim_Ops,Weight_Matches");

            for (int i = 0; i < sizes.length; i++) {
                int V = vertices[i];
                int E = edges[i];
                Graph g = generateConnectedUndirected(V, E, SEED + i);

                new Kruskal(g);
                new Prim(g);

                long totalKTime = 0, totalKOps = 0;
                long totalPTime = 0, totalPOps = 0;
                double kWeight = 0, pWeight = 0;

                for (int r = 0; r < RUNS; r++){
                    long startK = System.nanoTime();
                    Kruskal kruskal = new Kruskal(g);
                    totalKTime += (System.nanoTime() - startK);
                    totalKOps += kruskal.operations;
                    kWeight = kruskal.totalWeight;

                    long startP = System.nanoTime();
                    Prim prim = new Prim(g);
                    totalPTime += (System.nanoTime() - startP);
                    totalPOps += prim.operations;
                    pWeight = prim.totalWeight;
                }

                double kTimeMs = (totalKTime / (double) RUNS) / 1_000_000.0;
                double pTimeMs = (totalPTime / (double) RUNS) / 1_000_000.0;
                long kOps = totalKOps / RUNS;
                long pOps = totalPOps / RUNS;
                boolean matches = Math.abs(kWeight - pWeight) < 1e-6;

                System.out.printf("V=%-4d | E=%-4d | Kruskal: %.4f ms | Prim: %.4f ms\n", V, E, kTimeMs, pTimeMs);
                mstCsv.printf(Locale.US, "%s,%d,%d,%.4f,%d,%.4f,%d,%b\n", sizes[i], V, E, kTimeMs, kOps, pTimeMs, pOps, matches);
            }

            System.out.println("\nTable 2: Connectivity (Kosaraju SCC & Topological Sort)");
            sccCsv.println("Size,V,E,Kosaraju_Time_ms,Kosaraju_Ops,SCC_Count,Topo_Time_ms,Has_Cycle");

            for (int i = 0; i < sizes.length; i++) {
                int V = vertices[i];
                int E = edges[i];
                Graph directedCyclic = generateDirectedWithCycles(V, E, SEED + 10 + i);
                Graph directedDAG = generateDAG(V, E, SEED + 20 + i);

                new KosarajuSCC(directedCyclic);
                new TopologicalSort(directedDAG);

                long totalSccTime = 0, totalSccOps = 0;
                int sccCount = 0;

                for(int r = 0; r < RUNS; r++){
                    long start = System.nanoTime();
                    KosarajuSCC scc = new KosarajuSCC(directedCyclic);
                    totalSccTime += (System.nanoTime() - start);
                    totalSccOps += scc.operations;
                    sccCount = scc.sccList.size();
                }

                long totalTopoTime = 0;
                for(int r = 0; r < RUNS; r++){
                    long start = System.nanoTime();
                    new TopologicalSort(directedDAG);
                    totalTopoTime += (System.nanoTime() - start);
                }

                TopologicalSort cyclicTest = new TopologicalSort(directedCyclic);

                double sccTimeMs = (totalSccTime / (double) RUNS) / 1_000_000.0;
                double topoTimeMs = (totalTopoTime / (double) RUNS) / 1_000_000.0;
                long sccOps = totalSccOps / RUNS;

                System.out.printf("V=%-4d | E=%-4d | Kosaraju: %.4f ms | Topo: %.4f ms\n", V, E, sccTimeMs, topoTimeMs);
                sccCsv.printf(Locale.US, "%s,%d,%d,%.4f,%d,%d,%.4f,%b\n", sizes[i], V, E, sccTimeMs, sccOps, sccCount, topoTimeMs, cyclicTest.hasCycle);
            }

            System.out.println("\nTable 3: Dijkstra's Algorithm");
            dijCsv.println("Size,V,E,Dijkstra_Time_ms,Dijkstra_Ops,Reachable");

            for(int i = 0;i < sizes.length; i++){
                int V = vertices[i];
                int E = edges[i];
                Graph g = generateConnectedUndirected(V, E, SEED + 30 + i);

                new Dijkstra(g, 0);

                long totalDijTime = 0, totalDijOps = 0;
                int reachable = 0;

                for(int r = 0;r < RUNS; r++){
                    long start = System.nanoTime();
                    Dijkstra dij = new Dijkstra(g, 0);
                    totalDijTime += (System.nanoTime() - start);
                    totalDijOps += dij.operations;

                    reachable = 0;
                    for(double d : dij.distances){
                        if(d != Double.POSITIVE_INFINITY) reachable++;
                    }
                }
                double dijTimeMs = (totalDijTime / (double) RUNS) / 1_000_000.0;
                long dijOps = totalDijOps / RUNS;

                System.out.printf("V=%-4d | E=%-4d | Dijkstra: %.4f ms\n", V, E, dijTimeMs);
                dijCsv.printf(Locale.US, "%s,%d,%d,%.4f,%d,%d\n", sizes[i], V, E, dijTimeMs, dijOps, reachable);
            }
            System.out.println("\nBenchmarks Completed! CSV files saved to results/tables/");
        } catch (FileNotFoundException e) {
            System.err.println("Failed to create CSV files: " + e.getMessage());
        }
    }

    public static Graph generateConnectedUndirected(int V, int E, long seed){
        Random random = new Random(seed);
        Graph g = new Graph(V);

        List<Integer> nodes = new ArrayList<>();
        for(int i = 0; i < V; i++){
            nodes.add(i);
        }
        Collections.shuffle(nodes ,random);

        Set<String> edgeSet = new HashSet<>();

        for(int i = 1;i < V;i++){
            int u = nodes.get(i);
            int v = nodes.get(random.nextInt(i));
            double w = 1.0 + random.nextDouble() * 99.0;
            g.addUndirectedEdge(u, v, w);
            edgeSet.add(Math.min(u, v) + "-" + Math.max(u, v));
        }

        while (edgeSet.size() < E){
            int u = random.nextInt(V);
            int v = random.nextInt(V);
            if(u == v) continue;
            String key = Math.min(u,v) + "-" + Math.max(u,v);
            if(!edgeSet.contains(key)) {
                edgeSet.add(key);
                double w = 1.0 + random.nextDouble() * 99.0;
                g.addUndirectedEdge(u, v, w);
            }
        }
        return g;
    }

    public static Graph generateDirectedWithCycles(int V, int E, long seed){
        Random random = new Random(seed);
        Graph g = new Graph(V);
        Set<String> edgeSet = new HashSet<>();

        while (edgeSet.size() < E){
            int u = random.nextInt(V);
            int v = random.nextInt(V);
            if(u == v) continue;

            String key = u + "->" + v;
            if(!edgeSet.contains(key)) {
                edgeSet.add(key);
                g.addDirectedEdge(u, v, 1.0 + random.nextDouble() * 50);
            }
        }
        return g;
    }

    public static Graph generateDAG(int V, int E, long seed){
        Random random = new Random(seed);
        Graph g = new Graph(V);
        Set<String> edgeSet = new HashSet<>();

        long maxEdges = ((long) V * (V-1))/ 2;
        long targetEdges = Math.min(E, maxEdges);

        while(edgeSet.size() < targetEdges) {
            int u = random.nextInt(V);
            int v = random.nextInt(V);
            if (u >= v) continue;
            String key = u + "->" + v;
            if(!edgeSet.contains(key)){
                edgeSet.add(key);
                g.addDirectedEdge(u, v, 1.0 + random.nextDouble() * 50.0);
            }
        }
        return g;
    }

}
