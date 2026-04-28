import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphV2 {
    public static final double INTERCHANGE_TIME = 2.0;

    // V1 used a fixed-size Station[] plus linear scan for findStation.
    // V2 uses an ArrayList for ordered/id access and a HashMap (lowercase keys)
    // for O(1) name lookup — eliminates the linear scan during network build.
    private final List<StationV2> stations = new ArrayList<>();
    private final Map<String, StationV2> byName = new HashMap<>();

    // -----------------------------------------------------------------------
    // Network construction
    // -----------------------------------------------------------------------

    public StationV2 addStation(String name) {
        StationV2 s = new StationV2(stations.size(), name);
        stations.add(s);
        byName.put(name.toLowerCase(), s);
        return s;
    }

    /** Adds a single directed edge. Call twice (swapped args) for bidirectional tracks. */
    public void addEdge(String fromName, String toName, String line, String direction, double time) {
        StationV2 from = findStation(fromName);
        StationV2 to   = findStation(toName);
        if (from == null) { System.err.println("Unknown station: " + fromName); return; }
        if (to   == null) { System.err.println("Unknown station: " + toName);   return; }
        from.addEdge(new EdgeV2(to.id, line, direction, time));
    }

    // -----------------------------------------------------------------------
    // Lookup helpers
    // -----------------------------------------------------------------------

    public StationV2 findStation(String name) {
        return byName.get(name.toLowerCase());
    }

    public StationV2 getStation(int id) { return stations.get(id); }
    public int       getStationCount()  { return stations.size(); }

    // -----------------------------------------------------------------------
    // Engineer operations
    // -----------------------------------------------------------------------

    /**
     * Returns edges from fromId to toId that match the given line+direction filter.
     * Pass null for line or direction to match any value.
     * V1 returned matches via a pre-allocated Edge[] and an int count; V2 uses
     * java.util.stream and returns a List, removing the manual buffer plumbing.
     */
    private List<EdgeV2> matchEdges(int fromId, int toId, String line, String direction) {
        return stations.get(fromId).getEdges().stream()
                .filter(e -> e.toId == toId)
                .filter(e -> line      == null || line.equalsIgnoreCase(e.line))
                .filter(e -> direction == null || direction.equalsIgnoreCase(e.direction))
                .collect(Collectors.toList());
    }

    /** Returns the number of edges to which the delay was added. */
    public int addDelay(int fromId, int toId, String line, String direction, double delay) {
        List<EdgeV2> matches = matchEdges(fromId, toId, line, direction);
        matches.forEach(e -> e.delay += delay);
        return matches.size();
    }

    /** Returns the number of edges that actually had a delay reduced (skips edges with no existing delay). */
    public int removeDelay(int fromId, int toId, String line, String direction, double delay) {
        List<EdgeV2> matches = matchEdges(fromId, toId, line, direction);
        long changed = matches.stream().filter(e -> e.delay > 0).count();
        matches.stream()
                .filter(e -> e.delay > 0)
                .forEach(e -> e.delay = Math.max(0, e.delay - delay));
        return (int) changed;
    }

    /** Returns the number of edges that were actually closed (skips already-closed edges). */
    public int closeTrack(int fromId, int toId, String line, String direction) {
        List<EdgeV2> matches = matchEdges(fromId, toId, line, direction);
        long changed = matches.stream().filter(e -> !e.closed).count();
        matches.stream().filter(e -> !e.closed).forEach(e -> e.closed = true);
        return (int) changed;
    }

    /** Returns the number of edges that were actually opened (skips already-open edges). */
    public int openTrack(int fromId, int toId, String line, String direction) {
        List<EdgeV2> matches = matchEdges(fromId, toId, line, direction);
        long changed = matches.stream().filter(e -> e.closed).count();
        matches.stream().filter(e -> e.closed).forEach(e -> e.closed = false);
        return (int) changed;
    }

    // -----------------------------------------------------------------------
    // Reporting
    // -----------------------------------------------------------------------

    public void printClosed() {
        System.out.println("Closed track sections:");
        boolean any = false;
        for (StationV2 s : stations) {
            for (EdgeV2 e : s.getEdges()) {
                if (e.closed) {
                    System.out.printf("  %s: %s - %s - closed%n",
                            e.lineDirection(), s.name, stations.get(e.toId).name);
                    any = true;
                }
            }
        }
        if (!any) System.out.println("  None.");
    }

    public void printDelayed() {
        System.out.println("Delayed track sections:");
        boolean any = false;
        for (StationV2 s : stations) {
            for (EdgeV2 e : s.getEdges()) {
                if (e.delay > 0) {
                    System.out.printf("  %s: %s to %s - %.2fmin normal, %.2fmin delayed%n",
                            e.lineDirection(), s.name, stations.get(e.toId).name,
                            e.baseTime, e.effectiveTime());
                    any = true;
                }
            }
        }
        if (!any) System.out.println("  None.");
    }

    public void printStationInfo(int stationId) {
        StationV2 s = stations.get(stationId);
        System.out.println("Station: " + s.name);

        // V1 collected distinct line names manually with a String[] and a seen-flag loop.
        // V2 uses a LinkedHashSet via stream — preserves insertion order and removes duplicates in one pass.
        Set<String> lineNames = s.getEdges().stream()
                .map(e -> e.line)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        System.out.println("Lines served:");
        lineNames.forEach(ln -> System.out.println("  - " + ln));

        System.out.println("Direct connections:");
        for (EdgeV2 e : s.getEdges()) {
            String status = e.closed ? " [CLOSED]" : "";
            System.out.printf("  -> %-30s via %-30s %.2fmin%s%n",
                    stations.get(e.toId).name, e.lineDirection(), e.effectiveTime(), status);
        }

        if (lineNames.size() > 1) {
            System.out.print("Interchange possible between:");
            lineNames.forEach(ln -> System.out.print(" " + ln + ";"));
            System.out.println();
        }
    }

    // -----------------------------------------------------------------------
    // Menu helpers — edge queries used by TfLApp_v2
    // -----------------------------------------------------------------------

    public int listEdgesBetween(int fromId, int toId) {
        List<EdgeV2> edges = stations.get(fromId).getEdges().stream()
                .filter(e -> e.toId == toId)
                .collect(Collectors.toList());
        for (int i = 0; i < edges.size(); i++) {
            EdgeV2 e = edges.get(i);
            System.out.printf("  %d. %s  %.2fmin%n", i + 1, e.lineDirection(), e.effectiveTime());
        }
        return edges.size();
    }

    public EdgeV2 getEdgeBetween(int fromId, int toId, int n) {
        List<EdgeV2> edges = stations.get(fromId).getEdges().stream()
                .filter(e -> e.toId == toId)
                .collect(Collectors.toList());
        return (n >= 1 && n <= edges.size()) ? edges.get(n - 1) : null;
    }
}
