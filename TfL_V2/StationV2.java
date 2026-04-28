import java.util.ArrayList;
import java.util.List;

public class StationV2 {
    public final int id;
    public final String name;

    // V1 used a fixed-size Edge[] with manual count tracking.
    // V2 uses java.util.ArrayList — grows dynamically and removes the MAX_EDGES cap.
    private final List<EdgeV2> edges;

    public StationV2(int id, String name) {
        this.id = id;
        this.name = name;
        this.edges = new ArrayList<>();
    }

    public void addEdge(EdgeV2 edge) {
        edges.add(edge);
    }

    public List<EdgeV2> getEdges() { return edges; }
    public int getEdgeCount()      { return edges.size(); }
    public EdgeV2 getEdge(int i)   { return edges.get(i); }
}
