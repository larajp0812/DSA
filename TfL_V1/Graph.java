public class Graph {
    public static final double INTERCHANGE_TIME = 2.0;
    private static final int MAX_STATIONS = 250;

    private final Station[] stations;
    private int stationCount;

    public Graph() {
        stations = new Station[MAX_STATIONS];
        stationCount = 0;
    }

    // -----------------------------------------------------------------------
    // Network construction
    // -----------------------------------------------------------------------

    public Station addStation(String name) {
        Station s = new Station(stationCount, name);
        stations[stationCount++] = s;
        return s;
    }

    /** Adds a single directed edge. Call twice (swapped args) for bidirectional tracks. */
    public void addEdge(String fromName, String toName, String line, String direction, double time) {
        Station from = findStation(fromName);
        Station to   = findStation(toName);
        if (from == null) { System.err.println("Unknown station: " + fromName); return; }
        if (to   == null) { System.err.println("Unknown station: " + toName);   return; }
        from.addEdge(new Edge(to.id, line, direction, time));
    }

    // -----------------------------------------------------------------------
    // Lookup helpers
    // -----------------------------------------------------------------------

    public Station findStation(String name) {
        for (int i = 0; i < stationCount; i++) {
            if (stations[i].name.equalsIgnoreCase(name)) return stations[i];
        }
        return null;
    }

    public Station getStation(int id)  { return stations[id]; }
    public int     getStationCount()   { return stationCount; }

    // -----------------------------------------------------------------------
    // Engineer operations
    // -----------------------------------------------------------------------

    /**
     * Returns edges from fromId to toId that match the given line+direction filter.
     * Pass null for line or direction to match any value.
     * result[] must be pre-allocated to at least Station.MAX_EDGES.
     */
    private int matchEdges(int fromId, int toId, String line, String direction, Edge[] result) {
        Station from = stations[fromId];
        int count = 0;
        for (int i = 0; i < from.getEdgeCount(); i++) {
            Edge e = from.getEdge(i);
            // Check if this edge matches the criteria: correct destination, and line/direction
            if (e.toId == toId
                    && (line      == null || line.equalsIgnoreCase(e.line))
                    && (direction == null || direction.equalsIgnoreCase(e.direction))) {
                result[count++] = e;
            }
        }
        return count;
    }

    /** Returns the number of edges to which the delay was added. */
    public int addDelay(int fromId, int toId, String line, String direction, double delay) {
        Edge[] buf = new Edge[stations[fromId].getMaxEdges()];
        int n = matchEdges(fromId, toId, line, direction, buf);
        for (int i = 0; i < n; i++) buf[i].delay += delay;
        return n;
    }

    /** Returns the number of edges that actually had a delay reduced (skips edges with no existing delay). */
    public int removeDelay(int fromId, int toId, String line, String direction, double delay) {
        Edge[] buf = new Edge[stations[fromId].getMaxEdges()];
        int n = matchEdges(fromId, toId, line, direction, buf);
        int changed = 0;
        for (int i = 0; i < n; i++) {
            if (buf[i].delay > 0) {
                buf[i].delay = Math.max(0, buf[i].delay - delay);
                changed++;
            }
        }
        return changed;
    }

    /** Returns the number of edges that were actually closed (skips already-closed edges). */
    public int closeTrack(int fromId, int toId, String line, String direction) {
        Edge[] buf = new Edge[stations[fromId].getMaxEdges()];
        int n = matchEdges(fromId, toId, line, direction, buf);
        int changed = 0;
        for (int i = 0; i < n; i++) {
            if (!buf[i].closed) {
                buf[i].closed = true;
                changed++;
            }
        }
        return changed;
    }

    /** Returns the number of edges that were actually opened (skips already-open edges). */
    public int openTrack(int fromId, int toId, String line, String direction) {
        Edge[] buf = new Edge[stations[fromId].getMaxEdges()];
        int n = matchEdges(fromId, toId, line, direction, buf);
        int changed = 0;
        for (int i = 0; i < n; i++) {
            if (buf[i].closed) {
                buf[i].closed = false;
                changed++;
            }
        }
        return changed;
    }

    // -----------------------------------------------------------------------
    // Reporting
    // -----------------------------------------------------------------------

    public void printClosed() {
        System.out.println("Closed track sections:");
        boolean any = false;
        for (int i = 0; i < stationCount; i++) {
            Station s = stations[i];
            for (int j = 0; j < s.getEdgeCount(); j++) {
                Edge e = s.getEdge(j);
                if (e.closed) {
                    System.out.printf("  %s: %s - %s - closed%n",
                            e.lineDirection(), s.name, stations[e.toId].name);
                    any = true;
                }
            }
        }
        if (!any) System.out.println("  None.");
    }

    public void printDelayed() {
        System.out.println("Delayed track sections:");
        boolean any = false;
        for (int i = 0; i < stationCount; i++) {
            Station s = stations[i];
            for (int j = 0; j < s.getEdgeCount(); j++) {
                Edge e = s.getEdge(j);
                if (e.delay > 0) {
                    System.out.printf("  %s: %s to %s - %.2fmin normal, %.2fmin delayed%n",
                            e.lineDirection(), s.name, stations[e.toId].name,
                            e.baseTime, e.effectiveTime());
                    any = true;
                }
            }
        }
        if (!any) System.out.println("  None.");
    }

    public void printStationInfo(int stationId) {
        Station s = stations[stationId];
        System.out.println("Station: " + s.name);

        // Collect distinct line names served by this station.
        String[] lineNames = new String[20];
        int lineCount = 0;
        for (int i = 0; i < s.getEdgeCount(); i++) {
            String ln = s.getEdge(i).line;
            boolean seen = false;
            for (int j = 0; j < lineCount; j++) {
                if (lineNames[j].equals(ln)) { seen = true; break; }
            }
            if (!seen && lineCount < lineNames.length) lineNames[lineCount++] = ln;
        }

        System.out.println("Lines served:");
        for (int i = 0; i < lineCount; i++) System.out.println("  - " + lineNames[i]);

        System.out.println("Direct connections:");
        for (int i = 0; i < s.getEdgeCount(); i++) {
            Edge e = s.getEdge(i);
            String status = e.closed ? " [CLOSED]" : "";
            System.out.printf("  -> %-30s via %-30s %.2fmin%s%n",
                    stations[e.toId].name, e.lineDirection(), e.effectiveTime(), status);
        }

        if (lineCount > 1) {
            System.out.print("Interchange possible between:");
            for (int i = 0; i < lineCount; i++) System.out.print(" " + lineNames[i] + ";");
            System.out.println();
        }
    }

    // -----------------------------------------------------------------------
    // Menu helpers — edge queries used by TfLApp
    // -----------------------------------------------------------------------

    /**
     * Prints a numbered list of directed edges from fromId to toId.
     * Returns the number of edges found.
     */
    public int listEdgesBetween(int fromId, int toId) {
        Station s = stations[fromId];
        int count = 0;
        for (int i = 0; i < s.getEdgeCount(); i++) {
            Edge e = s.getEdge(i);
            if (e.toId == toId) {
                System.out.printf("  %d. %s  %.2fmin%n", ++count, e.lineDirection(), e.effectiveTime());
            }
        }
        return count;
    }

    /**
     * Returns the nth directed edge from fromId to toId (1-indexed, matching listEdgesBetween).
     */
    public Edge getEdgeBetween(int fromId, int toId, int n) {
        Station s = stations[fromId];
        int count = 0;
        for (int i = 0; i < s.getEdgeCount(); i++) {
            Edge e = s.getEdge(i);
            if (e.toId == toId && ++count == n) return e;
        }
        return null;
    }
}
