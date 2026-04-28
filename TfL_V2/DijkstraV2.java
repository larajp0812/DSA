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
    Library-based version of Dijkstra. Same algorithm as V1, same interchange
    rule, same output format. The working state uses HashMaps and a
    PriorityQueue from java.util in place of the parallel arrays and linear
    min-scan that V1 uses.
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

        // Working state — same purpose as V1's parallel arrays, just stored
        // in HashMaps keyed by station ID.
        Map<Integer, Double>  dist     = new HashMap<>();
        Map<Integer, Integer> prev     = new HashMap<>();
        Map<Integer, EdgeV2>  prevEdge = new HashMap<>();
        Set<Integer>          visited  = new HashSet<>();

        for (int i = 0; i < n; i++) dist.put(i, Double.MAX_VALUE);
        dist.put(startId, 0.0);

        // The frontier — a min-heap on cumulative distance. Replaces V1's
        // linear scan to find the cheapest unvisited station each step.
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(startId, 0.0));

        while (!pq.isEmpty()) {
            Node uNode = pq.poll();
            int u = uNode.stationId;

            // A station can appear in the heap more than once if it was
            // relaxed multiple times. Skip the stale entries.
            if (!visited.add(u)) continue;

            // Stop once the destination is settled
            if (u == endId) break;

            // Relax every edge leaving u
            StationV2 cur = g.getStation(u);
            for (EdgeV2 e : cur.getEdges()) {
                int v = e.toId;
                if (visited.contains(v)) continue;
                if (e.closed) continue;

                // Same interchange rule as V1: compare the line+direction we
                // arrived at u on (prevEdge[u]) to the edge we're about to ride.
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

        // No path was found
        if (dist.get(endId) == Double.MAX_VALUE) {
            System.out.println("No route found from " + g.getStation(startId).name
                    + " to " + g.getStation(endId).name + ".");
            return;
        }

        // Walk backwards from end to start. prev.get(startId) returns null,
        // which terminates the loop.
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

    /*
    Prints the route in the numbered step format the spec requires.
    Same output as V1 — the two versions can be diffed directly.
    */
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

            // Insert a Change step when the line+direction switches
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

            // Travel segment
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
