import java.util.Scanner;

public class TfLApp {
    // Fixed-size hand-coded graph model for Version 1.
    static final int NUM_STATIONS = 10;
    // Station lookup by index.
    static String[] stationNames = {"Baker Street", "Bond Street", "Green Park", "Victoria", "Warren Street", "Oxford Circus", "Paddington", "King's Cross", "Euston", "Camden Town"};
    // Base travel time for directed edge i -> j (0 means no direct edge).
    static double[][] baseTimes = new double[NUM_STATIONS][NUM_STATIONS];
    // Track closure state for directed edge i -> j.
    static boolean[][] closed = new boolean[NUM_STATIONS][NUM_STATIONS];
    // Delay minutes applied to directed edge i -> j.
    static double[][] delays = new double[NUM_STATIONS][NUM_STATIONS];
    // Line name for directed edge i -> j.
    static String[][] lines = new String[NUM_STATIONS][NUM_STATIONS];
    // Interchange cost when moving from one line to a different line.
    static final double INTERCHANGE_TIME = 2.0;

    static {
        // Build a simplified, directed representation of Circle/Jubilee/Victoria links.
        // Circle: Paddington(6) - Baker(0) - King's Cross(7) - Euston(8) - Camden(9) - Paddington
        baseTimes[6][0] = 2.5; baseTimes[0][6] = 2.5; lines[6][0] = "Circle"; lines[0][6] = "Circle";
        baseTimes[0][7] = 3.0; baseTimes[7][0] = 3.0; lines[0][7] = "Circle"; lines[7][0] = "Circle";
        baseTimes[7][8] = 2.0; baseTimes[8][7] = 2.0; lines[7][8] = "Circle"; lines[8][7] = "Circle";
        baseTimes[8][9] = 2.5; baseTimes[9][8] = 2.5; lines[8][9] = "Circle"; lines[9][8] = "Circle";
        baseTimes[9][6] = 4.0; baseTimes[6][9] = 4.0; lines[9][6] = "Circle"; lines[6][9] = "Circle";

        // Jubilee: Baker(0) - Bond(1) - Green(2) - Victoria(3)
        baseTimes[0][1] = 2.0; baseTimes[1][0] = 2.0; lines[0][1] = "Jubilee"; lines[1][0] = "Jubilee";
        baseTimes[1][2] = 2.5; baseTimes[2][1] = 2.5; lines[1][2] = "Jubilee"; lines[2][1] = "Jubilee";
        baseTimes[2][3] = 3.0; baseTimes[3][2] = 3.0; lines[2][3] = "Jubilee"; lines[3][2] = "Jubilee";

        // Victoria: Victoria(3) - Warren(4) - Oxford(5) - Green(2)
        baseTimes[3][4] = 2.0; baseTimes[4][3] = 2.0; lines[3][4] = "Victoria"; lines[4][3] = "Victoria";
        baseTimes[4][5] = 2.5; baseTimes[5][4] = 2.5; lines[4][5] = "Victoria"; lines[5][4] = "Victoria";
        baseTimes[5][2] = 3.0; baseTimes[2][5] = 3.0; lines[5][2] = "Victoria"; lines[2][5] = "Victoria";
    }

    // Adds delay to both directions for this section.
    static void addDelay(int from, int to, double delay) {
        delays[from][to] += delay;
        delays[to][from] += delay; // bidirectional
    }

    // Removes delay (floored at zero) in both directions.
    static void removeDelay(int from, int to, double delay) {
        delays[from][to] = Math.max(0, delays[from][to] - delay);
        delays[to][from] = Math.max(0, delays[to][from] - delay);
    }

    // Closes both directions of a section.
    static void closeTrack(int from, int to) {
        closed[from][to] = true;
        closed[to][from] = true;
    }

    // Opens both directions of a section.
    static void openTrack(int from, int to) {
        closed[from][to] = false;
        closed[to][from] = false;
    }

    // Prints all closed sections once (upper-triangle only to avoid duplicates).
    static void printClosed() {
        System.out.println("Closed track sections:");
        for (int i = 0; i < NUM_STATIONS; i++) {
            for (int j = i + 1; j < NUM_STATIONS; j++) {
                if (closed[i][j]) {
                    System.out.println(lines[i][j] + ": " + stationNames[i] + " - " + stationNames[j] + " - closed");
                }
            }
        }
    }

    // Prints every directed section currently carrying extra delay.
    static void printDelayed() {
        System.out.println("Delayed track sections:");
        for (int i = 0; i < NUM_STATIONS; i++) {
            for (int j = 0; j < NUM_STATIONS; j++) {
                if (delays[i][j] > 0) {
                    double normal = baseTimes[i][j];
                    double delayed = normal + delays[i][j];
                    System.out.println(lines[i][j] + ": " + stationNames[i] + " to " + stationNames[j] + " - " + normal + " min normal, " + delayed + " min delayed");
                }
            }
        }
    }

    // Dijkstra shortest-path by time with closure, delay, and interchange costs.
    static void findRoute(int start, int end) {
        double[] dist = new double[NUM_STATIONS];
        boolean[] visited = new boolean[NUM_STATIONS];
        int[] prev = new int[NUM_STATIONS];
        String[] prevLine = new String[NUM_STATIONS];
        // Defaults distance to all station as a maximum
        for (int i = 0; i < NUM_STATIONS; i++) {
            dist[i] = Double.MAX_VALUE;
            prev[i] = -1;
            prevLine[i] = "";
        }
        // Starting point has distance 0 and is picked first by minDistance function
        dist[start] = 0;
        prevLine[start] = "";

        for (int count = 0; count < NUM_STATIONS - 1; count++) {
            int u = minDistance(dist, visited);
            if (u == -1) break;
            visited[u] = true;
            // Checks all adjacent stations to u and calculates the time taken to get there from starting station. 
            for (int v = 0; v < NUM_STATIONS; v++) {
                if (!visited[v] && !closed[u][v] && baseTimes[u][v] > 0 && dist[u] != Double.MAX_VALUE) {
                    double edgeTime = baseTimes[u][v] + delays[u][v];
                    double interchange = (!prevLine[u].equals("") && !prevLine[u].equals(lines[u][v])) ? INTERCHANGE_TIME : 0;
                    double newDist = dist[u] + edgeTime + interchange;
                    // If new journey path is less than the stored path, replace it
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        // Updates previous station
                        prev[v] = u;
                        prevLine[v] = lines[u][v];
                    }
                }
            }
        }

        if (dist[end] == Double.MAX_VALUE) {
            System.out.println("No route found from " + stationNames[start] + " to " + stationNames[end]);
            return;
        }

        // Reconstruct path from end back to start.
        int[] path = new int[NUM_STATIONS];
        String[] pathLines = new String[NUM_STATIONS];
        int pathIndex = 0;
        for (int at = end; at != -1; at = prev[at]) {
            path[pathIndex] = at;
            pathIndex++;
        }
        // Reverse into start -> end order.
        for (int i = 0; i < pathIndex / 2; i++) {
            int temp = path[i];
            path[i] = path[pathIndex - 1 - i];
            path[pathIndex - 1 - i] = temp;
            String tempLine = pathLines[i];
            pathLines[i] = pathLines[pathIndex - 1 - i];
            pathLines[pathIndex - 1 - i] = tempLine;
        }

        // Fill line names per segment.
        for (int i = 0; i < pathIndex - 1; i++) {
            pathLines[i] = lines[path[i]][path[i + 1]];
        }

        System.out.println("Route from " + stationNames[start] + " to " + stationNames[end] + ":");
        double totalTime = 0;
        String currentLine = "";
        for (int i = 0; i < pathIndex - 1; i++) {
            String line = pathLines[i];
            if (!currentLine.equals(line)) {
                if (!currentLine.equals("")) {
                    System.out.println("Change: " + currentLine + " to " + line + " " + INTERCHANGE_TIME + " min");
                    totalTime+= INTERCHANGE_TIME;
                }
                currentLine = line;
            }
            int from = path[i];
            int to = path[i + 1];
            double time = baseTimes[from][to] + delays[from][to];
            System.out.println((i + 1) + ". " + line + ": " + stationNames[from] + " to " + stationNames[to] + " " + time + " min");
            totalTime += time;
        }
        System.out.println("Total Journey Time: " + totalTime + " minutes");
    }

    // Returns index of the next unvisited node with smallest distance from starting node.
    static int minDistance(double[] dist, boolean[] visited) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < NUM_STATIONS; i++) {
            if (!visited[i] && dist[i] < min) {
                min = dist[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    // Prints station lines and immediate direct connections.
    static void printStationInfo(int station) {
        System.out.println("Station: " + stationNames[station]);
        System.out.println("Lines: ");
        boolean[] hasLine = new boolean[3]; // 0 Circle, 1 Jubilee, 2 Victoria
        for (int j = 0; j < NUM_STATIONS; j++) {
            if (baseTimes[station][j] > 0) {
                if (lines[station][j].equals("Circle")) hasLine[0] = true;
                else if (lines[station][j].equals("Jubilee")) hasLine[1] = true;
                else if (lines[station][j].equals("Victoria")) hasLine[2] = true;
            }
        }
        if (hasLine[0]) System.out.println("- Circle");
        if (hasLine[1]) System.out.println("- Jubilee");
        if (hasLine[2]) System.out.println("- Victoria");
        System.out.println("Connections:");
        for (int j = 0; j < NUM_STATIONS; j++) {
            if (baseTimes[station][j] > 0) {
                System.out.println("- " + stationNames[j] + " via " + lines[station][j] + " " + (baseTimes[station][j] + delays[station][j]) + " min");
            }
        }
    }

    // Prints all stations with menu-friendly numbering.
    static void printStations() {
        System.out.println("Stations:");
        for (int i = 0; i < NUM_STATIONS; i++) {
            System.out.println((i + 1) + ". " + stationNames[i]);
        }
    }

    // Console menu for engineer/customer operations.
    static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nTfL Version 1 Menu:");
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
                    printStations();
                    break;
                case 2:
                    printStations();
                    System.out.print("From station number: ");
                    int from = scanner.nextInt() - 1;
                    System.out.print("To station number: ");
                    int to = scanner.nextInt() - 1;
                    System.out.print("Delay minutes: ");
                    double delay = scanner.nextDouble();
                    addDelay(from, to, delay);
                    System.out.println("Delay added.");
                    break;
                case 3:
                    printStations();
                    System.out.print("From station number: ");
                    from = scanner.nextInt() - 1;
                    System.out.print("To station number: ");
                    to = scanner.nextInt() - 1;
                    System.out.print("Remove delay minutes: ");
                    delay = scanner.nextDouble();
                    removeDelay(from, to, delay);
                    System.out.println("Delay removed.");
                    break;
                case 4:
                    printStations();
                    System.out.print("From station number: ");
                    from = scanner.nextInt() - 1;
                    System.out.print("To station number: ");
                    to = scanner.nextInt() - 1;
                    closeTrack(from, to);
                    System.out.println("Track closed.");
                    break;
                case 5:
                    printStations();
                    System.out.print("From station number: ");
                    from = scanner.nextInt() - 1;
                    System.out.print("To station number: ");
                    to = scanner.nextInt() - 1;
                    openTrack(from, to);
                    System.out.println("Track opened.");
                    break;
                case 6:
                    printClosed();
                    break;
                case 7:
                    printDelayed();
                    break;
                case 8:
                    printStations();
                    System.out.print("Start station number: ");
                    from = scanner.nextInt() - 1;
                    System.out.print("End station number: ");
                    to = scanner.nextInt() - 1;
                    findRoute(from, to);
                    break;
                case 9:
                    printStations();
                    System.out.print("Station number: ");
                    int station = scanner.nextInt() - 1;
                    printStationInfo(station);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }

    // Deterministic sample run used to generate the coursework output file.
    static void runSample() {
        System.out.println("=== TfL Application Version 1 ===");
        addDelay(0, 1, 5.0); // Baker to Bond 5 min delay
        closeTrack(1, 2); // Bond to Green closed
        System.out.println("After adding delay and closing track:");
        printDelayed();
        printClosed();
        System.out.println();
        findRoute(6, 7); // Paddington to King's Cross (multi-stop)
        System.out.println();
        printStationInfo(0); // Baker Street
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("menu")) {
            showMenu();
        } else {
            runSample();
        }
    }
}