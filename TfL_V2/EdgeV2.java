public class EdgeV2 {
    public final int toId;
    public final String line;
    public final String direction;   // e.g. "Eastbound", "Westbound", "Inner", "Outer"
    public final double baseTime;
    public double delay;
    public boolean closed;

    public EdgeV2(int toId, String line, String direction, double baseTime) {
        this.toId = toId;
        this.line = line;
        this.direction = direction;
        this.baseTime = baseTime;
        this.delay = 0.0;
        this.closed = false;
    }

    public double effectiveTime() {
        return baseTime + delay;
    }

    // Canonical label used to detect line changes (and thus interchange cost).
    public String lineDirection() {
        return line + " (" + direction + ")";
    }
}
