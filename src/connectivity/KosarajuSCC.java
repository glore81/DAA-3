package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class KosarajuSCC {
    public long operations = 0;
    public List<List<Integer>> sccList = new ArrayList<>();

    public KosarajuSCC(Graph graph){
        int n = graph.vertices;
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for(int i = 0; i < n; i++){
            if(!visited[i]){
                dfsOrder(i, graph, visited, stack);
            }
        }

        Graph transposed = new Graph(n);
        for(int u = 0; u < n; u++){
            for (Edge e : graph.adj.get(u)){
                transposed.addDirectedEdge(e.v, u, e.weight);
                operations++;
            }
        }

        visited = new boolean[n];
        while (!stack.isEmpty()){
            int u = stack.pop();
            operations++;
            if(!visited[u]){
                List<Integer> component = new ArrayList<>();
                dfsCollect(u, transposed, visited, component);
                sccList.add(component);
            }
        }
    }

    private void dfsOrder(int u, Graph graph, boolean[] visited, Deque<Integer> stack) {
        visited[u] = true;
        operations++;
        for(Edge e : graph.adj.get(u)) {
            operations++;
            if(!visited[e.v]) {
                dfsOrder(e.v, graph, visited, stack);
            }
        }

        stack.push(u);
    }

    private void dfsCollect(int u, Graph transposed, boolean[] visited, List<Integer> component) {
        visited[u] = true;
        component.add(u);
        operations++;
        for(Edge e : transposed.adj.get(u)){
            operations++;
            if(!visited[e.v]){
                dfsCollect(e.v, transposed, visited, component);
            }
        }
    }
}
