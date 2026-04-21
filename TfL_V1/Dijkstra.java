public class Dijkstra {

    /*
    Dijkstra's shortest-path algorithm adapted for the TfL network.
    
    Standard Dijkstra finds the lowest-cost path in a weighted graph by
    greedily settling the closest unvisited node at each step. Here "cost"
    is journey time in minutes, extended to include a 2-minute interchange
    penalty whenever the traveller must change line or direction at a station.
    
    Three parallel arrays form the algorithm's working state, one slot per station (indexed by station ID):
     *   dist[]     – best total time found so far to reach that station
     *   prev[]     – which station we came from on that best path
     *   prevEdge[] – which directed edge we travelled on to reach it

     prevEdge[] is what makes interchange detection possible: by remembering
     which line+direction brought us into a station, we can tell whether the
     next edge continues the same service or requires a platform change.
     */

    public static void findFastestRoute(Graph g, int startId, int endId) {
        int n = g.getStationCount();

        // ── Step 1: Initialise working arrays ────────────────────────────────
        // dist[] holds the best known cumulative journey time to every station.
        // Set to "infinity" (MAX_VALUE) so that any real path will be cheaper.
        double[]  dist     = new double[n];
        // prev[] records which station sits immediately before each station on
        // the best path found so far. Used to trace the route backwards at the end.
        int[]     prev     = new int[n];
        // prevEdge[] records the actual directed Edge object that was used to
        // arrive at each station. Needed to detect line changes (interchanges).
        Edge[]    prevEdge = new Edge[n];
        // visited[] marks stations whose shortest path has been finalised.
        // Once a station is visited, dist[station] will never decrease again.
        boolean[] visited  = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Double.MAX_VALUE; // no path known yet
            prev[i] = -1;               // no predecessor yet
        }

        // The start station costs nothing to reach
        // prevEdge[startId] remains null, meaning "no arriving edge" which
        // signals to the interchange check that this is the journey's origin.
        dist[startId] = 0;

        // ── Step 2: Main Dijkstra loop ────────────────────────────────────────
        // Each iteration finalises exactly one station (the nearest unvisited one).
        // The loop runs at most n times; in practice it stops as soon as the
        // destination is settled or no reachable stations remain.
        for (int iter = 0; iter < n; iter++) {

            // ── Step 2a: Select the unvisited station with the lowest dist[] ──
            // This is the "greedy" step that makes Dijkstra correct: because all
            // edge weights are non-negative, the cheapest unvisited station cannot
            // be improved by any path that goes through a more expensive station first.
            int    u    = -1;
            double minD = Double.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && dist[i] < minD) {
                    minD = dist[i];
                    u    = i;
                }
            }

            // If u is still -1 the remaining stations are all unreachable
            // (disconnected graph or every path is closed). Stop early.
            // If u is the destination, its shortest distance is now finalised —
            // no further iteration can improve it, so stop early.
            if (u == -1 || u == endId) break;

            // Mark u as settled. Its dist[u] value is now permanent.
            visited[u] = true;

            // ── Step 2b: Relax all outgoing edges from u ──────────────────────
            // "Relaxing" an edge means: if going via u to neighbour v is cheaper
            // than the best path to v found so far, update v's record.
            Station cur = g.getStation(u);
            for (int i = 0; i < cur.getEdgeCount(); i++) {
                Edge e = cur.getEdge(i);
                int  v = e.toId;

                // Skip v if its shortest path is already finalised.
                if (visited[v]) continue;

                // Skip this edge if the track section is currently closed.
                if (e.closed) continue;

                // ── Interchange cost ──────────────────────────────────────────
                // prevEdge[u] is the edge we rode to arrive at station u.
                // e is the edge we are considering riding out of u.
                // If both exist AND their line+direction labels differ, the
                // traveller must physically change platforms — add INTERCHANGE_TIME.
                //
                // prevEdge[u] == null means u is the start station (no arriving
                // edge), so no interchange is charged for the very first leg.
                double interchange = 0;
                if (prevEdge[u] != null
                        && !prevEdge[u].lineDirection().equals(e.lineDirection())) {
                    interchange = Graph.INTERCHANGE_TIME;
                }

                // Total cost to reach v via u on this edge:
                //   time already spent reaching u
                // + effective travel time of this segment (base + any delay)
                // + any interchange penalty
                double candidate = dist[u] + e.effectiveTime() + interchange;

                // ── Relaxation condition ──────────────────────────────────────
                // Only update v's record if this new route is strictly better
                // than whatever path to v was found previously.
                if (candidate < dist[v]) {
                    dist[v]     = candidate; // new best time to reach v
                    prev[v]     = u;         // came from station u
                    prevEdge[v] = e;         // via this specific edge
                }
            }
        }

        // ── Step 3: Check reachability ────────────────────────────────────────
        // If dist[endId] is still MAX_VALUE, no path exists (all routes are
        // closed, or the two stations are on disconnected parts of the network).
        if (dist[endId] == Double.MAX_VALUE) {
            System.out.println("No route found from " + g.getStation(startId).name
                    + " to " + g.getStation(endId).name + ".");
            return;
        }

        // ── Step 4: Reconstruct the path ──────────────────────────────────────
        // Dijkstra works forward but only records predecessors, so we trace
        // backwards from end to start using the prev[] chain.
        int[]  path      = new int[n];
        Edge[] pathEdges = new Edge[n];
        int len = 0;

        // Walk the prev[] chain: end → ... → start.
        // Store each station ID and the edge used to arrive at it.
        // prevEdge[startId] is null (no arriving edge at the origin).
        for (int at = endId; at != -1; at = prev[at]) {
            path[len]      = at;
            pathEdges[len] = prevEdge[at];
            len++;
        }

        // ── Step 5: Reverse into start → end order ────────────────────────────
        // Swap elements from both ends towards the middle (in-place reversal).
        // After this, path[0] = startId, path[len-1] = endId.
        // pathEdges[0] = null (no edge arrives at the start).
        // pathEdges[i] for i >= 1 = the edge travelling from path[i-1] to path[i].
        for (int i = 0; i < len / 2; i++) {
            int  ti = path[i];
            path[i]           = path[len - 1 - i];
            path[len - 1 - i] = ti;

            Edge te = pathEdges[i];
            pathEdges[i]           = pathEdges[len - 1 - i];
            pathEdges[len - 1 - i] = te;
        }

        printRoute(g, startId, endId, path, pathEdges, len);
    }

    // -----------------------------------------------------------------------
    // Route output
    // -----------------------------------------------------------------------

    /**
     * Prints the route in the numbered step format required by the spec.
     *
     * pathEdges[0] is null (no edge arrives at the start station).
     * pathEdges[i] for i >= 1 is the directed Edge from path[i-1] to path[i].
     *
     * When pathEdges[i].lineDirection() differs from pathEdges[i-1].lineDirection()
     * an extra "Change:" step is inserted before the travel step, naming the
     * station where the passenger swaps platforms and the two line+direction labels.
     */
    private static void printRoute(Graph g, int startId, int endId,
                                   int[] path, Edge[] pathEdges, int len) {

        System.out.println("Route: " + g.getStation(startId).name
                + " to " + g.getStation(endId).name + ":");

        // Read the first outgoing edge to know which line+direction the journey
        // starts on, so we can label the (1) Start line correctly.
        String currentLineDir = (len > 1) ? pathEdges[1].lineDirection() : "";
        System.out.println("(1) Start: " + g.getStation(startId).name + ", " + currentLineDir);

        // step counts the output lines (Start is already step 1).
        int    step      = 2;
        double totalTime = 0;

        // Walk every segment of the path (i = 1 means the first edge taken).
        for (int i = 1; i < len; i++) {
            Edge   e  = pathEdges[i];     // edge from path[i-1] to path[i]
            String ld = e.lineDirection(); // e.g. "Jubilee (Westbound)"

            // ── Detect an interchange ─────────────────────────────────────────
            // Compare this segment's line+direction to the one we were riding.
            // Skip the check on the very first segment (i == 1) because there
            // is no "previous" segment to compare against.
            if (i > 1 && !ld.equals(currentLineDir)) {
                // The passenger is standing at path[i-1] having arrived via
                // currentLineDir; they must now change to ld before continuing.
                System.out.printf("(%d) Change: %s  %s  to  %s  %.2fmin%n",
                        step++,
                        g.getStation(path[i - 1]).name, // station where change happens
                        currentLineDir,                  // the line just ridden
                        ld,                              // the line about to be taken
                        Graph.INTERCHANGE_TIME);
                totalTime     += Graph.INTERCHANGE_TIME; // interchange adds 2 minutes
                currentLineDir = ld;                     // now riding the new line
            }

            // ── Print the travel segment ──────────────────────────────────────
            // effectiveTime() = baseTime + any current delay on this edge.
            System.out.printf("(%d) %s: %s to %s %.2fmin%n",
                    step++,
                    ld,
                    g.getStation(path[i - 1]).name,
                    g.getStation(path[i]).name,
                    e.effectiveTime());
            totalTime += e.effectiveTime();
        }

        // Final step: the destination station with the line the passenger arrives on.
        System.out.println("(" + step + ") End: " + g.getStation(endId).name + ", " + currentLineDir);

        // Grand total includes all travel segments AND all interchange penalties.
        System.out.printf("Total Journey Time: %.2f minutes%n", totalTime);
    }
}
