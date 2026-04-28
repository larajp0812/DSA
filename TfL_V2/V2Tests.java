import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Hand-rolled test runner for V2 — mirrors V1Tests so the two are diffable.
 * Run:  javac *.java && java V2Tests
 */
public class V2Tests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== TfL V2 Tests ===\n");

        runTest("1. Spec reference route: Marble Arch -> Great Portland Street = 9.18 min with 2 interchanges",
                V2Tests::testSpecReferenceRoute);
        runTest("2. Engineer ops mutate state and are idempotent on close-already-closed",
                V2Tests::testEngineerOps);
        runTest("3. Closed track forces a longer reroute",
                V2Tests::testClosedTrackReroute);
        runTest("4. Delay on the only edge increases the route time",
                V2Tests::testDelayAffectsRoute);
        runTest("5. No route found when all outgoing edges are closed",
                V2Tests::testNoRouteFound);

        System.out.println();
        System.out.println("Results: " + passed + " passed, " + failed + " failed.");
        System.exit(failed > 0 ? 1 : 0);
    }

    // ------------------------------------------------------------------
    // Tests
    // ------------------------------------------------------------------

    private static void testSpecReferenceRoute() {
        GraphV2 g = TfLApp_v2.buildNetwork();
        int from = g.findStation("Marble Arch").id;
        int to   = g.findStation("Great Portland Street").id;

        String out = captureOutput(() -> DijkstraV2.findFastestRoute(g, from, to));

        assertContains(out, "Total Journey Time: 9.18 minutes", "journey time must match spec");
        assertContains(out, "Central (Eastbound)",  "must use Central EB on first leg");
        assertContains(out, "Jubilee (Westbound)",  "must use Jubilee WB middle leg");
        assertContains(out, "Circle (Outer)",       "must use Circle Outer final leg");
        int interchanges = countOccurrences(out, "Change:");
        if (interchanges != 2) {
            throw new AssertionError("expected 2 interchanges, got " + interchanges);
        }
    }

    private static void testEngineerOps() {
        GraphV2 g = TfLApp_v2.buildNetwork();
        int ws = g.findStation("Warren Street").id;
        int oc = g.findStation("Oxford Circus").id;
        int bs = g.findStation("Bond Street").id;
        int gp = g.findStation("Green Park").id;

        if (g.addDelay(ws, oc, "Victoria", "Southbound", 5.0) < 1) {
            throw new AssertionError("addDelay should change at least 1 edge");
        }
        if (g.closeTrack(bs, gp, "Jubilee", "Eastbound") < 1) {
            throw new AssertionError("closeTrack should change at least 1 edge");
        }
        int second = g.closeTrack(bs, gp, "Jubilee", "Eastbound");
        if (second != 0) {
            throw new AssertionError("closing already-closed should return 0, got " + second);
        }

        String delayed = captureOutput(g::printDelayed);
        assertContains(delayed, "Warren Street", "delayed report should mention Warren Street");
        assertContains(delayed, "Oxford Circus", "delayed report should mention Oxford Circus");

        String closed = captureOutput(g::printClosed);
        assertContains(closed, "Bond Street",   "closed report should mention Bond Street");
        assertContains(closed, "Green Park",    "closed report should mention Green Park");
        assertContains(closed, "Jubilee (Eastbound)", "closed report should label the line");
    }

    private static void testClosedTrackReroute() {
        GraphV2 g = TfLApp_v2.buildNetwork();
        int bs = g.findStation("Bond Street").id;
        int gp = g.findStation("Green Park").id;

        double before = parseJourneyTime(captureOutput(() -> DijkstraV2.findFastestRoute(g, bs, gp)));
        g.closeTrack(bs, gp, "Jubilee", "Eastbound");
        double after  = parseJourneyTime(captureOutput(() -> DijkstraV2.findFastestRoute(g, bs, gp)));

        if (!(after > before)) {
            throw new AssertionError("closure should force a longer path; before=" + before + " after=" + after);
        }
    }

    private static void testDelayAffectsRoute() {
        GraphV2 g = TfLApp_v2.buildNetwork();
        int ma = g.findStation("Marble Arch").id;
        int bs = g.findStation("Bond Street").id;

        double before = parseJourneyTime(captureOutput(() -> DijkstraV2.findFastestRoute(g, ma, bs)));
        g.addDelay(ma, bs, "Central", "Eastbound", 5.0);
        double after  = parseJourneyTime(captureOutput(() -> DijkstraV2.findFastestRoute(g, ma, bs)));

        double diff = after - before;
        if (Math.abs(diff - 5.0) > 0.01) {
            throw new AssertionError("delay of 5.0 should add 5.0 min; before=" + before
                    + " after=" + after + " diff=" + diff);
        }
    }

    private static void testNoRouteFound() {
        GraphV2 g = TfLApp_v2.buildNetwork();
        int ma = g.findStation("Marble Arch").id;
        int bs = g.findStation("Bond Street").id;
        int lg = g.findStation("Lancaster Gate").id;

        g.closeTrack(ma, bs, "Central", "Eastbound");
        g.closeTrack(ma, lg, "Central", "Westbound");

        String out = captureOutput(() -> DijkstraV2.findFastestRoute(g, ma, bs));
        assertContains(out, "No route found", "expected 'No route found' message");
    }

    // ------------------------------------------------------------------
    // Test harness helpers
    // ------------------------------------------------------------------

    private static void runTest(String name, Runnable test) {
        try {
            test.run();
            System.out.println("[PASS] " + name);
            passed++;
        } catch (AssertionError e) {
            System.out.println("[FAIL] " + name);
            System.out.println("       " + e.getMessage());
            failed++;
        } catch (Exception e) {
            System.out.println("[ERROR] " + name);
            System.out.println("       " + e.getClass().getSimpleName() + ": " + e.getMessage());
            failed++;
        }
    }

    private static String captureOutput(Runnable r) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(buf));
        try { r.run(); } finally { System.setOut(original); }
        return buf.toString();
    }

    private static void assertContains(String haystack, String needle, String msg) {
        if (!haystack.contains(needle)) {
            throw new AssertionError(msg + " — expected output to contain: " + needle);
        }
    }

    private static int countOccurrences(String haystack, String needle) {
        int count = 0;
        int idx = 0;
        while ((idx = haystack.indexOf(needle, idx)) != -1) {
            count++;
            idx += needle.length();
        }
        return count;
    }

    private static double parseJourneyTime(String routeOutput) {
        String marker = "Total Journey Time:";
        int idx = routeOutput.indexOf(marker);
        if (idx < 0) throw new AssertionError("route output missing 'Total Journey Time:'");
        String tail = routeOutput.substring(idx + marker.length()).trim();
        int spaceIdx = tail.indexOf(' ');
        return Double.parseDouble(tail.substring(0, spaceIdx));
    }
}
