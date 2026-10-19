package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class TopologicalSort {
    public List<Integer> order = new ArrayList<>();
    public boolean hasCycle = false;
    public long operations = 0;

    public TopologicalSort(Graph graph){
        int vCount = graph.vertices;
        int[] inDegree = new int[vCount];

        for(int u = 0; u < vCount; u++){
            for (Edge e : graph.adj.get(u)){
                inDegree[e.v]++;
                operations++;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < vCount; i++){
            if (inDegree[i] == 0) queue.add(i);
        }

        while(!queue.isEmpty()){
            int u = queue.poll();
            order.add(u);
            operations++;

            for(Edge e : graph.adj.get(u)){
                inDegree[e.v]--;
                operations++;
                if(inDegree[e.v] == 0){
                    queue.add(e.v);
                }
            }
        }
        if(order.size() != vCount){
            hasCycle = true;
            order.clear();
        }
    }

}
