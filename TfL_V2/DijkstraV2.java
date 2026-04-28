import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraV2 {

    /*
     Dijkstra's shortest-path algorithm — library-based implementation.

     Algorithmically identical to V1: same interchange logic (track the line+direction
     used to arrive at each station, charge INTERCHANGE_TIME when the next edge differs),
     same relaxation rule, same path reconstruction.

     The differences are purely in the data structures used to support the algorithm:

       V1                                 →  V2
       --                                    --
       parallel arrays  (double[] dist,    →  HashMap<Integer, ...>
                         int[] prev,
                         Edge[] prevEdge,
                         boolean[] visited)
       linear scan for min-dist station   →  PriorityQueue<Node> (binary heap)
       reverse with manual swap loop      →  Collections.reverse

     Theoretical complexity changes:
       V1 main loop:  O(V^2)        (V iterations × O(V) min-scan)
       V2 main loop:  O((V+E) log V) (each edge can push one heap entry; lazy deletion)
     */

    private static class Node implements Comparable<Node> {
        final int stationId;
        final double dist;
        Node(int stationId, double dist) {
            this.stationId = stationId;
            this.dist = dist;
        }
        @Override
        public int compareTo(Node other) {
            return Double.compare(this.dist, other.dist);
        }
    }

    public static void findFastestRoute(GraphV2 g, int startId, int endId) {
        int n = g.getStationCount();

        // ── Step 1: Initialise working state ─────────────────────────────────
        // V1 used parallel arrays indexed by station ID. V2 uses HashMaps keyed by ID.
        // (Arrays would still work — using maps demonstrates the library swap, and
        //  is the more typical idiom when station identifiers aren't dense integers.)
        Map<Integer, Double>  dist     = new HashMap<>();
        Map<Integer, Integer> prev     = new HashMap<>();
        Map<Integer, EdgeV2>  prevEdge = new HashMap<>();
        Set<Integer>          visited  = new HashSet<>();

        for (int i = 0; i < n; i++) dist.put(i, Double.MAX_VALUE);
        dist.put(startId, 0.0);

        // The frontier — a min-heap on cumulative distance. PriorityQueue replaces
        // V1's O(V) linear scan that picked the cheapest unvisited station each step.
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(startId, 0.0));

        // ── Step 2: Main Dijkstra loop ───────────────────────────────────────
        while (!pq.isEmpty()) {
            Node uNode = pq.poll();
            int u = uNode.stationId;

            // Lazy deletion: a station may appear in the heap multiple times if
            // it was relaxed more than once. Skip stale entries.
            if (!visited.add(u)) continue;

            // Destination settled — its dist is now permanent, so we can stop.
            if (u == endId) break;

            // ── Step 2b: Relax all outgoing edges from u ─────────────────────
            StationV2 cur = g.getStation(u);
            for (EdgeV2 e : cur.getEdges()) {
                int v = e.toId;
                if (visited.contains(v)) continue;
                if (e.closed) continue;

                // ── Interchange cost ─────────────────────────────────────────
                // Identical rule to V1: compare the line+direction of the edge we
                // arrived at u on (prevEdge[u]) to the line+direction of the edge
                // about to be ridden out of u. Different → 2-minute platform change.
                // prevEdge.get(startId) returns null, so the first leg is free.
                double interchange = 0;
                EdgeV2 arriving = prevEdge.get(u);
                if (arriving != null
                        && !arriving.lineDirection().equals(e.lineDirection())) {
                    interchange = GraphV2.INTERCHANGE_TIME;
                }

                double candidate = dist.get(u) + e.effectiveTime() + interchange;

                if (candidate < dist.get(v)) {
                    dist.put(v, candidate);
                    prev.put(v, u);
                    prevEdge.put(v, e);
                    pq.add(new Node(v, candidate));
                }
            }
        }

        // ── Step 3: Reachability check ───────────────────────────────────────
        if (dist.get(endId) == Double.MAX_VALUE) {
            System.out.println("No route found from " + g.getStation(startId).name
                    + " to " + g.getStation(endId).name + ".");
            return;
        }

        // ── Step 4: Reconstruct the path ─────────────────────────────────────
        // V1 walked the prev[] chain into pre-allocated arrays and reversed in place.
        // V2 walks into ArrayLists and uses Collections.reverse — same behaviour.
        // prev.get(startId) returns null, terminating the walk.
        List<Integer> path = new ArrayList<>();
        List<EdgeV2>  pathEdges = new ArrayList<>();
        Integer at = endId;
        while (at != null) {
            path.add(at);
            pathEdges.add(prevEdge.get(at));
            at = prev.get(at);
        }
        Collections.reverse(path);
        Collections.reverse(pathEdges);

        printRoute(g, startId, endId, path, pathEdges);
    }

    // -----------------------------------------------------------------------
    // Route output — identical format to V1 so the two versions can be diffed.
    // -----------------------------------------------------------------------

    private static void printRoute(GraphV2 g, int startId, int endId,
                                   List<Integer> path, List<EdgeV2> pathEdges) {

        System.out.println("Route: " + g.getStation(startId).name
                + " to " + g.getStation(endId).name + ":");

        String currentLineDir = (path.size() > 1) ? pathEdges.get(1).lineDirection() : "";
        System.out.println("(1) Start: " + g.getStation(startId).name + ", " + currentLineDir);

        int    step      = 2;
        double totalTime = 0;

        for (int i = 1; i < path.size(); i++) {
            EdgeV2 e  = pathEdges.get(i);
            String ld = e.lineDirection();

            if (i > 1 && !ld.equals(currentLineDir)) {
                System.out.printf("(%d) Change: %s %s to %s %.2fmin%n",
                        step++,
                        g.getStation(path.get(i - 1)).name,
                        currentLineDir,
                        ld,
                        GraphV2.INTERCHANGE_TIME);
                totalTime     += GraphV2.INTERCHANGE_TIME;
                currentLineDir = ld;
            }

            System.out.printf("(%d) %s: %s to %s %.2fmin%n",
                    step++,
                    ld,
                    g.getStation(path.get(i - 1)).name,
                    g.getStation(path.get(i)).name,
                    e.effectiveTime());
            totalTime += e.effectiveTime();
        }

        System.out.println("(" + step + ") End: " + g.getStation(endId).name + ", " + currentLineDir);
        System.out.printf("Total Journey Time: %.2f minutes%n", totalTime);
    }
}
