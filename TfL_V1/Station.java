public class Station {
    // Upper bound on edges per station; covers all interchange + line edges.
    private static final int MAX_EDGES = 30;

    public final int id;
    public final String name;

    private final Edge[] edges;
    private int edgeCount;

    public Station(int id, String name) {
        this.id = id;
        this.name = name;
        this.edges = new Edge[MAX_EDGES];
        this.edgeCount = 0;
    }

    public void addEdge(Edge edge) {
        if (edgeCount < MAX_EDGES) {
            edges[edgeCount++] = edge;
        }
    }

    public int getEdgeCount() { return edgeCount; }
    public Edge getEdge(int i) { return edges[i]; }
    public int getMaxEdges() { return MAX_EDGES; }
}
