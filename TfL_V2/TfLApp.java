import java.util.*;

public class TfLApp {
    // Edge in the adjacency list graph.
    static class Edge {
        String to;
        double time;
        String line;
        Edge(String to, double time, String line) {
            this.to = to;
            this.time = time;
            this.line = line;
        }
    }

    // Priority queue node for Dijkstra.
    static class Node implements Comparable<Node> {
        String station;
        double dist;
        Node(String station, double dist) {
            this.station = station;
            this.dist = dist;
        }
        public int compareTo(Node other) {
            return Double.compare(this.dist, other.dist);
        }
    }

    // Graph and operational state for Version 2 (library-based implementation).
    static Map<String, List<Edge>> graph = new HashMap<>();
    static Map<String, Map<String, Boolean>> closed = new HashMap<>();
    static Map<String, Map<String, Double>> delays = new HashMap<>();
    static Map<String, String> stationLines = new HashMap<>();
    // Interchange penalty between different lines.
    static final double INTERCHANGE_TIME = 2.0;

    static {
        // Initialize station containers for a simplified demo network.
        String[] stations = {"Baker Street", "Bond Street", "Green Park", "Victoria", "Warren Street", "Oxford Circus", "Paddington", "King's Cross", "Euston", "Camden Town"};
        for (String s : stations) {
            graph.put(s, new ArrayList<>());
            closed.put(s, new HashMap<>());
            delays.put(s, new HashMap<>());
        }

        // Circle line sample links.
        // Circle
        addEdge("Paddington", "Baker Street", 2.5, "Circle");
        addEdge("Baker Street", "King's Cross", 3.0, "Circle");
        addEdge("King's Cross", "Euston", 2.0, "Circle");
        addEdge("Euston", "Camden Town", 2.5, "Circle");
        addEdge("Camden Town", "Paddington", 4.0, "Circle");

        // Jubilee line sample links.
        // Jubilee
        addEdge("Baker Street", "Bond Street", 2.0, "Jubilee");
        addEdge("Bond Street", "Green Park", 2.5, "Jubilee");
        addEdge("Green Park", "Victoria", 3.0, "Jubilee");

        // Victoria line sample links.
        // Victoria
        addEdge("Victoria", "Warren Street", 2.0, "Victoria");
        addEdge("Warren Street", "Oxford Circus", 2.5, "Victoria");
        addEdge("Oxford Circus", "Green Park", 3.0, "Victoria");

        // Simple station -> lines summary for station info output.
        // Station lines
        stationLines.put("Baker Street", "Circle,Jubilee");
        stationLines.put("Bond Street", "Jubilee");
        stationLines.put("Green Park", "Jubilee,Victoria");
        stationLines.put("Victoria", "Circle,Jubilee,Victoria");
        stationLines.put("Warren Street", "Victoria");
        stationLines.put("Oxford Circus", "Victoria");
        stationLines.put("Paddington", "Circle");
        stationLines.put("King's Cross", "Circle");
        stationLines.put("Euston", "Circle");
        stationLines.put("Camden Town", "Circle");
    }

    // Adds a bidirectional section and initializes status/delay metadata.
    static void addEdge(String from, String to, double time, String line) {
        graph.get(from).add(new Edge(to, time, line));
        graph.get(to).add(new Edge(from, time, line)); // bidirectional
        closed.get(from).put(to, false);
        closed.get(to).put(from, false);
        delays.get(from).put(to, 0.0);
        delays.get(to).put(from, 0.0);
    }

    // Adds delay to both directions.
    static void addDelay(String from, String to, double delay) {
        delays.get(from).put(to, delays.get(from).get(to) + delay);
        delays.get(to).put(from, delays.get(to).get(from) + delay);
    }

    // Removes delay (floored at zero) for both directions.
    static void removeDelay(String from, String to, double delay) {
        delays.get(from).put(to, Math.max(0, delays.get(from).get(to) - delay));
        delays.get(to).put(from, Math.max(0, delays.get(to).get(from) - delay));
    }

    // Closes both directions of a section.
    static void closeTrack(String from, String to) {
        closed.get(from).put(to, true);
        closed.get(to).put(from, true);
    }

    // Opens both directions of a section.
    static void openTrack(String from, String to) {
        closed.get(from).put(to, false);
        closed.get(to).put(from, false);
    }

    // Prints current closed sections.
    static void printClosed() {
        System.out.println("Closed track sections:");
        for (String from : graph.keySet()) {
            for (Edge e : graph.get(from)) {
                if (closed.get(from).get(e.to)) {
                    System.out.println(e.line + ": " + from + " - " + e.to + " - closed");
                }
            }
        }
    }

    // Prints sections where operational delays are active.
    static void printDelayed() {
        System.out.println("Delayed track sections:");
        for (String from : delays.keySet()) {
            for (Map.Entry<String, Double> entry : delays.get(from).entrySet()) {
                if (entry.getValue() > 0) {
                    double normal = getBaseTime(from, entry.getKey());
                    double delayed = normal + entry.getValue();
                    String line = getLine(from, entry.getKey());
                    System.out.println(line + ": " + from + " to " + entry.getKey() + " - " + normal + " min normal, " + delayed + " min delayed");
                }
            }
        }
    }

    // Helper: returns base edge time for directed section.
    static double getBaseTime(String from, String to) {
        for (Edge e : graph.get(from)) {
            if (e.to.equals(to)) return e.time;
        }
        return 0;
    }

    // Helper: returns line name for directed section.
    static String getLine(String from, String to) {
        for (Edge e : graph.get(from)) {
            if (e.to.equals(to)) return e.line;
        }
        return "";
    }

    // Dijkstra shortest-path by time with closure, delay, and line interchange cost.
    static void findRoute(String start, String end) {
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Map<String, String> prevLine = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (String s : graph.keySet()) {
            dist.put(s, Double.MAX_VALUE);
        }
        dist.put(start, 0.0);
        pq.add(new Node(start, 0.0));

        while (!pq.isEmpty()) {
            Node uNode = pq.poll();
            String u = uNode.station;
            if (dist.get(u) < uNode.dist) continue;

            for (Edge e : graph.get(u)) {
                String v = e.to;
                if (closed.get(u).get(v)) continue;
                double edgeTime = e.time + delays.get(u).get(v);
                String currentLine = prevLine.getOrDefault(u, "");
                double interchange = (!currentLine.equals(e.line)) ? INTERCHANGE_TIME : 0;
                double newDist = dist.get(u) + edgeTime + interchange;
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    prevLine.put(v, e.line);
                    pq.add(new Node(v, newDist));
                }
            }
        }

        if (dist.get(end) == Double.MAX_VALUE) {
            System.out.println("No route found from " + start + " to " + end);
            return;
        }

        // Reconstruct path from end to start, then reverse.
        List<String> path = new ArrayList<>();
        List<String> pathLines = new ArrayList<>();
        for (String at = end; at != null; at = prev.get(at)) {
            path.add(at);
            pathLines.add(prevLine.getOrDefault(at, ""));
        }
        Collections.reverse(path);
        Collections.reverse(pathLines);

        System.out.println("Route from " + start + " to " + end + ":");
        double totalTime = 0;
        String currentLine = "";
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            double time = getBaseTime(from, to) + delays.get(from).get(to);
            String line = getLine(from, to);
            if (!currentLine.equals(line)) {
                if (!currentLine.equals("")) {
                    System.out.println("Change to " + line + " " + INTERCHANGE_TIME + " min");
                    totalTime += INTERCHANGE_TIME;
                }
                currentLine = line;
            }
            System.out.println((i + 1) + ". " + line + ": " + from + " to " + to + " " + time + " min");
            totalTime += time;
        }
        System.out.println("Total Journey Time: " + totalTime + " minutes");
    }

    // Prints full station summary with lines and immediate connections.
    static void printStationInfo(String station) {
        System.out.println("Station: " + station);
        System.out.println("Lines: " + stationLines.get(station));
        System.out.println("Connections:");
        for (Edge e : graph.get(station)) {
            double time = e.time + delays.get(station).get(e.to);
            System.out.println("- " + e.to + " via " + e.line + " " + time + " min");
        }
    }

    // Prints menu-friendly station numbering.
    static void printStations() {
        System.out.println("Stations:");
        int index = 1;
        for (String station : graph.keySet()) {
            System.out.println((index++) + ". " + station);
        }
    }

    // Console menu for user operations.
    static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        List<String> stationList = new ArrayList<>(graph.keySet());
        while (true) {
            System.out.println("\nTfL Version 2 Menu:");
            System.out.println("1. List stations");
            System.out.println("2. Add delay");
            System.out.println("3. Remove delay");
            System.out.println("4. Close track");
            System.out.println("5. Open track");
            System.out.println("6. Print closed tracks");
            System.out.println("7. Print delayed tracks");
            System.out.println("8. Find fastest route");
            System.out.println("9. Station information");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            if (choice == 0) break;
            switch (choice) {
                case 1:
                    for (int i = 0; i < stationList.size(); i++) {
                        System.out.println((i + 1) + ". " + stationList.get(i));
                    }
                    break;
                case 2:
                    for (int i = 0; i < stationList.size(); i++) {
                        System.out.println((i + 1) + ". " + stationList.get(i));
                    }
                    System.out.print("From station number: ");
                    int fromIndex = scanner.nextInt() - 1;
                    System.out.print("To station number: ");
                    int toIndex = scanner.nextInt() - 1;
                    System.out.print("Delay minutes: ");
                    double delay = scanner.nextDouble();
                    addDelay(stationList.get(fromIndex), stationList.get(toIndex), delay);
                    System.out.println("Delay added.");
                    break;
                case 3:
                    for (int i = 0; i < stationList.size(); i++) {
                        System.out.println((i + 1) + ". " + stationList.get(i));
                    }
                    System.out.print("From station number: ");
                    fromIndex = scanner.nextInt() - 1;
                    System.out.print("To station number: ");
                    toIndex = scanner.nextInt() - 1;
                    System.out.print("Remove delay minutes: ");
                    delay = scanner.nextDouble();
                    removeDelay(stationList.get(fromIndex), stationList.get(toIndex), delay);
                    System.out.println("Delay removed.");
                    break;
                case 4:
                    for (int i = 0; i < stationList.size(); i++) {
                        System.out.println((i + 1) + ". " + stationList.get(i));
                    }
                    System.out.print("From station number: ");
                    fromIndex = scanner.nextInt() - 1;
                    System.out.print("To station number: ");
                    toIndex = scanner.nextInt() - 1;
                    closeTrack(stationList.get(fromIndex), stationList.get(toIndex));
                    System.out.println("Track closed.");
                    break;
                case 5:
                    for (int i = 0; i < stationList.size(); i++) {
                        System.out.println((i + 1) + ". " + stationList.get(i));
                    }
                    System.out.print("From station number: ");
                    fromIndex = scanner.nextInt() - 1;
                    System.out.print("To station number: ");
                    toIndex = scanner.nextInt() - 1;
                    openTrack(stationList.get(fromIndex), stationList.get(toIndex));
                    System.out.println("Track opened.");
                    break;
                case 6:
                    printClosed();
                    break;
                case 7:
                    printDelayed();
                    break;
                case 8:
                    for (int i = 0; i < stationList.size(); i++) {
                        System.out.println((i + 1) + ". " + stationList.get(i));
                    }
                    System.out.print("Start station number: ");
                    fromIndex = scanner.nextInt() - 1;
                    System.out.print("End station number: ");
                    toIndex = scanner.nextInt() - 1;
                    findRoute(stationList.get(fromIndex), stationList.get(toIndex));
                    break;
                case 9:
                    for (int i = 0; i < stationList.size(); i++) {
                        System.out.println((i + 1) + ". " + stationList.get(i));
                    }
                    System.out.print("Station number: ");
                    int stationIndex = scanner.nextInt() - 1;
                    printStationInfo(stationList.get(stationIndex));
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }

    // Deterministic sample run used to generate Version 2 output file.
    static void runSample() {
        System.out.println("=== TfL Application Version 2 ===");
        addDelay("Baker Street", "Bond Street", 5.0);
        closeTrack("Bond Street", "Green Park");
        System.out.println("After adding delay and closing track:");
        printDelayed();
        printClosed();
        System.out.println();
        findRoute("Paddington", "King's Cross");
        System.out.println();
        printStationInfo("Baker Street");
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("menu")) {
            showMenu();
        } else {
            runSample();
        }
    }
}