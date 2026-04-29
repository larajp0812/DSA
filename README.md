# TfL Route Finder Application

Two Java implementations of a Transport for London route finder, written to compare a hand-rolled data-structure approach against the standard `java.util` collections.

## Versions

### Version 1 ([TfL_V1/](TfL_V1/))

Hand-coded implementation using only core Java types and arrays — no `java.util.Collections`.

Files: `Station.java`, `Edge.java`, `Graph.java`, `Dijkstra.java`, `TfLApp_v1.java`.

Fixed-size arrays for stations and edges; Dijkstra picks the next frontier station by linear scan (O(V²)).

### Version 2 ([TfL_V2/](TfL_V2/))

Library-based mirror of V1. Same network data, same edge model (directional, line + direction), same interchange logic, same output format — only the internal data structures change.

Files: `StationV2.java`, `EdgeV2.java`, `GraphV2.java`, `DijkstraV2.java`, `TfLApp_v2.java`.

Uses `ArrayList`, `HashMap`, `PriorityQueue`, and `Stream`. Class names are suffixed with `V2` so they don't collide with V1's default-package classes when both folders share the IntelliJ classpath.

## Network

Both versions model the same TfL sections (~120 stations):

- Full Circle line, plus stations within it (Bakerloo, Central, Piccadilly, Northern, etc.)
- Full Jubilee line (Stanmore → Stratford)
- Full Victoria line (Walthamstow Central → Brixton)
- Mildmay overground line (Richmond → Stratford)
- H&C, Metropolitan, District, and Elizabeth where they intersect the above

Edge times are directional — Eastbound/Westbound, Northbound/Southbound, or Inner/Outer (Circle line) — and the two directions of the same track section can differ. A 2-minute interchange penalty is added whenever a route changes line+direction at a station.

## Features

- Add/remove journey time delays between stations (per line+direction, with optional reverse-direction mirror)
- Close/open track sections (with optional reverse-direction mirror)
- Print closed and delayed sections
- Find the fastest route between two stations (timed per call)
- Display station information
- Benchmark Dijkstra over every station pair, with configurable warm-up and run counts
- Terminal menu

## How to Run

### Version 1

```bash
cd TfL_V1
javac *.java
java TfLApp_v1          # interactive menu (default)
java TfLApp_v1 sample   # deterministic sample output
```

### Version 2

```bash
cd TfL_V2
javac *.java
java TfLApp_v2          # interactive menu (default)
java TfLApp_v2 sample   # deterministic sample output
```

## Tests

Each version has a hand-rolled test runner ([V1Tests.java](TfL_V1/V1Tests.java), [V2Tests.java](TfL_V2/V2Tests.java)) covering five core cases: a reference route with two interchanges, engineer-ops state changes, closure rerouting, delay handling, and the no-route edge case. Compiled by `javac *.java`; run with:

```bash
cd TfL_V1 && java V1Tests
cd TfL_V2 && java V2Tests
```

## Sample Output

The two versions can be made to print a deterministic sample run to stdout, useful for diffing V1 against V2:

```bash
cd TfL_V1 && java TfLApp_v1 sample > V1_output.txt
cd TfL_V2 && java TfLApp_v2 sample > V2_output.txt
```

V1 and V2 sample outputs are byte-identical apart from the version label in the header, confirming the two implementations produce the same routes.

## Design Notes

- V1 deliberately avoids `java.util` collections, modelling stations and edges as fixed-size arrays so the data-structure cost of Dijkstra is fully visible.
- V2 swaps in `java.util` library classes (`ArrayList`, `HashMap`, `PriorityQueue`, `HashSet`); behaviour is intentionally identical to V1 so the two can be benchmarked head-to-head.
