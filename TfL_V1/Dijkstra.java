public class Dijkstra {

    /*
    Dijkstra's algorithm for finding the fastest route between two stations.
    Cost is journey time in minutes, with a 2 minute penalty added whenever
    the route changes line or direction at a station.

    Three arrays track the state, indexed by station ID:
        dist[]      best total time found so far to reach that station
        prev[]      which station we came from on the best path
        prevEdge[]  the directed edge we travelled on to reach it

    prevEdge[] is what lets us detect interchanges. By remembering which line
    and direction brought us into a station, we can tell whether the next edge
    continues on the same service or needs a platform change.
    */

    public static void findFastestRoute(Graph g, int startId, int endId) {
        int n = g.getStationCount();

        // Working arrays. dist starts at "infinity" so any real path wins,
        // and prev starts at -1 to mean "no predecessor yet".
        double[]  dist     = new double[n];
        int[]     prev     = new int[n];
        Edge[]    prevEdge = new Edge[n];
        boolean[] visited  = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Double.MAX_VALUE;
            prev[i] = -1;
        }

        // The start costs nothing to reach. prevEdge[startId] stays null,
        // which is how we know there's no incoming edge, so the first leg
        // never charges an interchange.
        dist[startId] = 0;

        // Each iteration of the main loop finalises one station — the
        // closest unvisited one — and relaxes its outgoing edges.
        for (int iter = 0; iter < n; iter++) {

            // Pick the unvisited station with the lowest dist
            int    u    = -1;
            double minD = Double.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && dist[i] < minD) {
                    minD = dist[i];
                    u    = i;
                }
            }

            // u == -1 means no reachable stations are left.
            // u == endId means we're done — its dist won't change again.
            if (u == -1 || u == endId) break;

            visited[u] = true;

            // Relax every edge leaving u
            Station cur = g.getStation(u);
            for (int i = 0; i < cur.getEdgeCount(); i++) {
                Edge e = cur.getEdge(i);
                int  v = e.toId;

                if (visited[v]) continue;
                if (e.closed) continue;

                // Interchange cost: if the line+direction we arrived at u on
                // is different from the edge we're about to ride, add the 2
                // minute penalty. prevEdge[u] is null for the start station,
                // so the first leg is always free.
                double interchange = 0;
                if (prevEdge[u] != null
                        && !prevEdge[u].lineDirection().equals(e.lineDirection())) {
                    interchange = Graph.INTERCHANGE_TIME;
                }

                double candidate = dist[u] + e.effectiveTime() + interchange;

                // Update v only if this route is better than the best one
                // found so far
                if (candidate < dist[v]) {
                    dist[v]     = candidate;
                    prev[v]     = u;
                    prevEdge[v] = e;
                }
            }
        }

        // No path exists if dist[endId] never got updated
        if (dist[endId] == Double.MAX_VALUE) {
            System.out.println("No route found from " + g.getStation(startId).name
                    + " to " + g.getStation(endId).name + ".");
            return;
        }

        // Walk backwards from end to start using prev[]. prev[startId] is -1,
        // which terminates the chain.
        int[]  path      = new int[n];
        Edge[] pathEdges = new Edge[n];
        int len = 0;

        for (int at = endId; at != -1; at = prev[at]) {
            path[len]      = at;
            pathEdges[len] = prevEdge[at];
            len++;
        }

        // Reverse so the arrays go start to end. After this, path[0] is the
        // start and pathEdges[i] is the edge that goes from path[i-1] to path[i].
        // pathEdges[0] is null (no edge arrives at the start).
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

    /*
    Prints the route in the numbered step format the spec requires.
    A "Change:" step is inserted whenever the current segment's line+direction
    is different from the previous one.
    */
    private static void printRoute(Graph g, int startId, int endId,
                                   int[] path, Edge[] pathEdges, int len) {

        System.out.println("Route: " + g.getStation(startId).name
                + " to " + g.getStation(endId).name + ":");

        // Grab the first leg's line+direction so the Start line is labelled correctly
        String currentLineDir = (len > 1) ? pathEdges[1].lineDirection() : "";
        System.out.println("(1) Start: " + g.getStation(startId).name + ", " + currentLineDir);

        int    step      = 2;
        double totalTime = 0;

        for (int i = 1; i < len; i++) {
            Edge   e  = pathEdges[i];
            String ld = e.lineDirection();

            // If the line+direction has changed, insert a Change step before
            // the travel segment
            if (i > 1 && !ld.equals(currentLineDir)) {
                System.out.printf("(%d) Change: %s %s to %s %.2fmin%n",
                        step++,
                        g.getStation(path[i - 1]).name,
                        currentLineDir,
                        ld,
                        Graph.INTERCHANGE_TIME);
                totalTime     += Graph.INTERCHANGE_TIME;
                currentLineDir = ld;
            }

            // Travel segment
            System.out.printf("(%d) %s: %s to %s %.2fmin%n",
                    step++,
                    ld,
                    g.getStation(path[i - 1]).name,
                    g.getStation(path[i]).name,
                    e.effectiveTime());
            totalTime += e.effectiveTime();
        }

        System.out.println("(" + step + ") End: " + g.getStation(endId).name + ", " + currentLineDir);
        System.out.printf("Total Journey Time: %.2f minutes%n", totalTime);
    }
}
