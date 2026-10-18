package mst;

import graph.Edge;
import java.util.List;

public class MSTResult {
    public final List<Edge> mstEdges;
    public final double totalWeight;
    public final boolean isConnected;
    public final long operations;

    public MSTResult(List<Edge> mstEdges, double totalWeight, boolean isConnected, long operations) {
        this.mstEdges = mstEdges;
        this.totalWeight = totalWeight;
        this.isConnected = isConnected;
        this.operations = operations;
    }
}
