import java.util.Scanner;

public class TfLApp_v1 {

    // -----------------------------------------------------------------------
    // Network construction
    // -----------------------------------------------------------------------

    static Graph buildNetwork() {
        Graph g = new Graph();

        // Stations listed once, in the order they first appear across all lines.
        // Station name normalisation applied throughout:
        //   BAKER STREET (MET/METROPOLITAN/CIRCLE) → Baker Street
        //   PADDINGTON (H&C/CIRCLE/Dis)            → Paddington
        //   KINGS CROSS / KINGS CROSS ST PANCRAS   → King's Cross St. Pancras
        //   EUSTON (CX) / EUSTON (CITY)            → Euston
        //   HIGHBURY                               → Highbury & Islington
        //   HAMMERSMITH (H&C)                      → Hammersmith
        //   WEST HAMSTEAD (Mildmay typo)           → West Hampstead
        //   BRONDERSBURY / BRONDERSBURY PARK       → Brondesbury / Brondesbury Park
        //   HAMSTEAD HEATH                         → Hampstead Heath
        //   WILLESDEN JUNCTION A / D / JUNTION D   → Willesden Junction
        //   KENSINGTON (OLYMPIA)                   → Kensington Olympia
        //   SHEPHERDS BUSH (trailing spaces)       → Shepherd's Bush
        //   ST JAMES PARK                          → St James's Park
        //   MONUMENT (Circle/District)             → Bank  (same station complex as Bank)

        // Circle / H&C / Metropolitan (inner ring and shared track)
        g.addStation("Edgware Road");
        g.addStation("Paddington");
        g.addStation("Bayswater");
        g.addStation("Notting Hill Gate");
        g.addStation("High Street Kensington");
        g.addStation("Gloucester Road");
        g.addStation("South Kensington");
        g.addStation("Sloane Square");
        g.addStation("Victoria");
        g.addStation("St James's Park");
        g.addStation("Westminster");
        g.addStation("Embankment");
        g.addStation("Temple");
        g.addStation("Blackfriars");
        g.addStation("Mansion House");
        g.addStation("Cannon Street");
        // Monument merged into Bank (same station complex; Circle/District edges below use "Bank")
        g.addStation("Tower Hill");
        g.addStation("Aldgate");
        g.addStation("Liverpool Street");
        g.addStation("Moorgate");
        g.addStation("Barbican");
        g.addStation("Farringdon");
        g.addStation("King's Cross St. Pancras");
        g.addStation("Euston Square");
        g.addStation("Great Portland Street");
        g.addStation("Baker Street");
        g.addStation("Hammersmith");
        g.addStation("Goldhawk Road");
        g.addStation("Shepherd's Bush Market");
        g.addStation("Wood Lane");
        g.addStation("Latimer Road");
        g.addStation("Ladbroke Grove");
        g.addStation("Westbourne Park");
        g.addStation("Royal Oak");
        g.addStation("Aldgate East");       // H&C / District eastern terminus

        // Jubilee line
        g.addStation("Stanmore");
        g.addStation("Canons Park");
        g.addStation("Queensbury");
        g.addStation("Kingsbury");
        g.addStation("Wembley Park");
        g.addStation("Neasden");
        g.addStation("Dollis Hill");
        g.addStation("Willesden Green");
        g.addStation("Kilburn");
        g.addStation("West Hampstead");
        g.addStation("Finchley Road");
        g.addStation("Swiss Cottage");
        g.addStation("St John's Wood");
        g.addStation("Bond Street");
        g.addStation("Green Park");
        g.addStation("Waterloo");
        g.addStation("Southwark");
        g.addStation("London Bridge");
        g.addStation("Bermondsey");
        g.addStation("Canada Water");
        g.addStation("Canary Wharf");
        g.addStation("North Greenwich");
        g.addStation("Canning Town");
        g.addStation("West Ham");
        g.addStation("Stratford");

        // Victoria line
        g.addStation("Walthamstow Central");
        g.addStation("Blackhorse Road");
        g.addStation("Tottenham Hale");
        g.addStation("Seven Sisters");
        g.addStation("Finsbury Park");
        g.addStation("Highbury & Islington");
        g.addStation("Euston");
        g.addStation("Warren Street");
        g.addStation("Oxford Circus");
        g.addStation("Pimlico");
        g.addStation("Vauxhall");
        g.addStation("Stockwell");
        g.addStation("Brixton");

        // Northern line
        g.addStation("Camden Town");
        g.addStation("Goodge Street");
        g.addStation("Tottenham Court Road");
        g.addStation("Leicester Square");
        g.addStation("Charing Cross");
        g.addStation("Angel");
        g.addStation("Old Street");
        g.addStation("Bank");

        // Bakerloo line
        g.addStation("Marylebone");
        g.addStation("Regents Park");
        g.addStation("Piccadilly Circus");

        // Central line
        g.addStation("Queensway");
        g.addStation("Lancaster Gate");
        g.addStation("Marble Arch");
        g.addStation("Holborn");
        g.addStation("Chancery Lane");
        g.addStation("St Paul's");

        // Piccadilly line
        g.addStation("Earl's Court");
        g.addStation("Knightsbridge");
        g.addStation("Hyde Park Corner");
        g.addStation("Covent Garden");
        g.addStation("Russell Square");

        // Mildmay line (London Overground)
        // Note: Shepherd's Bush (Mildmay/Overground) is a different station from
        //       Shepherd's Bush Market (H&C/Circle), separated by a short walk.
        // Note: Finchley Road & Frognal (Mildmay) is distinct from
        //       Finchley Road (Jubilee/Metropolitan), a few minutes apart.
        g.addStation("Richmond");
        g.addStation("Kew Gardens");
        g.addStation("Gunnersbury");
        g.addStation("South Acton");
        g.addStation("Acton Central");
        g.addStation("Willesden Junction");
        g.addStation("Clapham Junction");
        g.addStation("Imperial Wharf");
        g.addStation("West Brompton");
        g.addStation("Kensington Olympia");
        g.addStation("Shepherd's Bush");
        g.addStation("Kensal Rise");
        g.addStation("Brondesbury Park");
        g.addStation("Brondesbury");
        g.addStation("Finchley Road & Frognal");
        g.addStation("Hampstead Heath");
        g.addStation("Gospel Oak");
        g.addStation("Kentish Town West");
        g.addStation("Camden Road");
        g.addStation("Caledonian Road & Barnsbury");
        g.addStation("Canonbury");
        g.addStation("Dalston Kingsland");
        g.addStation("Hackney Central");
        g.addStation("Homerton");
        g.addStation("Hackney Wick");

        addCircle(g);
        addHammersmithAndCity(g);
        addMetropolitan(g);
        addDistrict(g);
        addJubilee(g);
        addVictoria(g);
        addNorthern(g);
        addBakerloo(g);
        addCentral(g);
        addPiccadilly(g);
        addMildmay(g);
        addElizabeth(g);

        return g;
    }

    // -----------------------------------------------------------------------
    // Per-line edge data (real times from TfL operational data)
    // Times differ between directions on almost every segment, so each
    // direction is entered as a separate addEdge
    // -----------------------------------------------------------------------

    private static void addCircle(Graph g) {
        // Inner = anti-clockwise (Edgware Road → Paddington → south loop → City → back).
        // Outer = clockwise (Hammersmith → H&C stem → Baker Street → City → south loop → back).
        // The Circle route visits Paddington and Edgware Road twice each:
        // once on the main loop and once on the western H&C stem to Hammersmith.

        // Inner — main loop
        g.addEdge("Edgware Road",            "Paddington",               "Circle", "Inner", 1.85);
        g.addEdge("Paddington",              "Bayswater",                "Circle", "Inner", 1.65);
        g.addEdge("Bayswater",               "Notting Hill Gate",        "Circle", "Inner", 1.47);
        g.addEdge("Notting Hill Gate",       "High Street Kensington",   "Circle", "Inner", 1.58);
        g.addEdge("High Street Kensington",  "Gloucester Road",          "Circle", "Inner", 1.80);
        g.addEdge("Gloucester Road",         "South Kensington",         "Circle", "Inner", 1.43);
        g.addEdge("South Kensington",        "Sloane Square",            "Circle", "Inner", 1.98);
        g.addEdge("Sloane Square",           "Victoria",                 "Circle", "Inner", 1.80);
        g.addEdge("Victoria",                "St James's Park",          "Circle", "Inner", 1.42);
        g.addEdge("St James's Park",         "Westminster",              "Circle", "Inner", 1.50);
        g.addEdge("Westminster",             "Embankment",               "Circle", "Inner", 1.37);
        g.addEdge("Embankment",              "Temple",                   "Circle", "Inner", 1.37);
        g.addEdge("Temple",                  "Blackfriars",              "Circle", "Inner", 1.40);
        g.addEdge("Blackfriars",             "Mansion House",            "Circle", "Inner", 1.52);
        g.addEdge("Mansion House",           "Cannon Street",            "Circle", "Inner", 0.98);
        g.addEdge("Cannon Street",           "Bank",                 "Circle", "Inner", 0.97);
        g.addEdge("Bank",                "Tower Hill",               "Circle", "Inner", 1.80);
        g.addEdge("Tower Hill",              "Aldgate",                  "Circle", "Inner", 1.30);
        g.addEdge("Aldgate",                 "Liverpool Street",         "Circle", "Inner", 1.75);
        g.addEdge("Liverpool Street",        "Moorgate",                 "Circle", "Inner", 1.32);
        g.addEdge("Moorgate",                "Barbican",                 "Circle", "Inner", 1.38);
        g.addEdge("Barbican",                "Farringdon",               "Circle", "Inner", 1.20);
        g.addEdge("Farringdon",              "King's Cross St. Pancras", "Circle", "Inner", 3.12);
        g.addEdge("King's Cross St. Pancras","Euston Square",            "Circle", "Inner", 1.65);
        g.addEdge("Euston Square",           "Great Portland Street",    "Circle", "Inner", 1.30);
        g.addEdge("Great Portland Street",   "Baker Street",             "Circle", "Inner", 1.57);
        g.addEdge("Baker Street",            "Edgware Road",             "Circle", "Inner", 1.88);
        // Inner — western H&C stem to Hammersmith
        g.addEdge("Edgware Road",            "Paddington",               "Circle", "Inner", 2.08);
        g.addEdge("Paddington",              "Royal Oak",                "Circle", "Inner", 1.33);
        g.addEdge("Royal Oak",               "Westbourne Park",          "Circle", "Inner", 1.72);
        g.addEdge("Westbourne Park",         "Ladbroke Grove",           "Circle", "Inner", 1.48);
        g.addEdge("Ladbroke Grove",          "Latimer Road",             "Circle", "Inner", 1.28);
        g.addEdge("Latimer Road",            "Wood Lane",                "Circle", "Inner", 1.50);
        g.addEdge("Wood Lane",               "Shepherd's Bush Market",   "Circle", "Inner", 1.50);
        g.addEdge("Shepherd's Bush Market",  "Goldhawk Road",            "Circle", "Inner", 1.15);
        g.addEdge("Goldhawk Road",           "Hammersmith",              "Circle", "Inner", 2.43);

        // Outer — clockwise
        g.addEdge("Hammersmith",             "Goldhawk Road",            "Circle", "Outer", 2.05);
        g.addEdge("Goldhawk Road",           "Shepherd's Bush Market",   "Circle", "Outer", 1.15);
        g.addEdge("Shepherd's Bush Market",  "Wood Lane",                "Circle", "Outer", 1.50);
        g.addEdge("Wood Lane",               "Latimer Road",             "Circle", "Outer", 1.50);
        g.addEdge("Latimer Road",            "Ladbroke Grove",           "Circle", "Outer", 1.37);
        g.addEdge("Ladbroke Grove",          "Westbourne Park",          "Circle", "Outer", 1.48);
        g.addEdge("Westbourne Park",         "Royal Oak",                "Circle", "Outer", 1.78);
        g.addEdge("Royal Oak",               "Paddington",               "Circle", "Outer", 1.52);
        g.addEdge("Paddington",              "Edgware Road",             "Circle", "Outer", 2.33);
        g.addEdge("Edgware Road",            "Baker Street",             "Circle", "Outer", 1.47);
        g.addEdge("Baker Street",            "Great Portland Street",    "Circle", "Outer", 1.90);
        g.addEdge("Great Portland Street",   "Euston Square",            "Circle", "Outer", 1.25);
        g.addEdge("Euston Square",           "King's Cross St. Pancras", "Circle", "Outer", 1.75);
        g.addEdge("King's Cross St. Pancras","Farringdon",               "Circle", "Outer", 2.98);
        g.addEdge("Farringdon",              "Barbican",                 "Circle", "Outer", 1.22);
        g.addEdge("Barbican",                "Moorgate",                 "Circle", "Outer", 1.32);
        g.addEdge("Moorgate",                "Liverpool Street",         "Circle", "Outer", 1.18);
        g.addEdge("Liverpool Street",        "Aldgate",                  "Circle", "Outer", 2.18);
        g.addEdge("Aldgate",                 "Tower Hill",               "Circle", "Outer", 1.37);
        g.addEdge("Tower Hill",              "Bank",                 "Circle", "Outer", 1.48);
        g.addEdge("Bank",                "Cannon Street",            "Circle", "Outer", 0.88);
        g.addEdge("Cannon Street",           "Mansion House",            "Circle", "Outer", 0.93);
        g.addEdge("Mansion House",           "Blackfriars",              "Circle", "Outer", 1.22);
        g.addEdge("Blackfriars",             "Temple",                   "Circle", "Outer", 1.37);
        g.addEdge("Temple",                  "Embankment",               "Circle", "Outer", 1.43);
        g.addEdge("Embankment",              "Westminster",              "Circle", "Outer", 1.40);
        g.addEdge("Westminster",             "St James's Park",          "Circle", "Outer", 1.52);
        g.addEdge("St James's Park",         "Victoria",                 "Circle", "Outer", 1.33);
        g.addEdge("Victoria",                "Sloane Square",            "Circle", "Outer", 1.75);
        g.addEdge("Sloane Square",           "South Kensington",         "Circle", "Outer", 2.00);
        g.addEdge("South Kensington",        "Gloucester Road",          "Circle", "Outer", 1.48);
        g.addEdge("Gloucester Road",         "High Street Kensington",   "Circle", "Outer", 2.23);
        g.addEdge("High Street Kensington",  "Notting Hill Gate",        "Circle", "Outer", 1.68);
        g.addEdge("Notting Hill Gate",       "Bayswater",                "Circle", "Outer", 1.77);
        g.addEdge("Bayswater",               "Paddington",               "Circle", "Outer", 1.63);
        g.addEdge("Paddington",              "Edgware Road",             "Circle", "Outer", 2.15);
    }

    private static void addHammersmithAndCity(Graph g) {
        // Eastbound: Hammersmith → Aldgate East
        g.addEdge("Hammersmith",             "Goldhawk Road",            "H & C", "Eastbound", 2.05);
        g.addEdge("Goldhawk Road",           "Shepherd's Bush Market",   "H & C", "Eastbound", 1.15);
        g.addEdge("Shepherd's Bush Market",  "Wood Lane",                "H & C", "Eastbound", 1.50);
        g.addEdge("Wood Lane",               "Latimer Road",             "H & C", "Eastbound", 1.50);
        g.addEdge("Latimer Road",            "Ladbroke Grove",           "H & C", "Eastbound", 1.37);
        g.addEdge("Ladbroke Grove",          "Westbourne Park",          "H & C", "Eastbound", 1.48);
        g.addEdge("Westbourne Park",         "Royal Oak",                "H & C", "Eastbound", 1.78);
        g.addEdge("Royal Oak",               "Paddington",               "H & C", "Eastbound", 1.52);
        g.addEdge("Paddington",              "Edgware Road",             "H & C", "Eastbound", 2.33);
        g.addEdge("Edgware Road",            "Baker Street",             "H & C", "Eastbound", 1.47);
        g.addEdge("Baker Street",            "Great Portland Street",    "H & C", "Eastbound", 1.90);
        g.addEdge("Great Portland Street",   "Euston Square",            "H & C", "Eastbound", 1.25);
        g.addEdge("Euston Square",           "King's Cross St. Pancras", "H & C", "Eastbound", 1.75);
        g.addEdge("King's Cross St. Pancras","Farringdon",               "H & C", "Eastbound", 2.98);
        g.addEdge("Farringdon",              "Barbican",                 "H & C", "Eastbound", 1.22);
        g.addEdge("Barbican",                "Moorgate",                 "H & C", "Eastbound", 1.32);
        g.addEdge("Moorgate",                "Liverpool Street",         "H & C", "Eastbound", 1.18);
        g.addEdge("Liverpool Street",        "Aldgate East",             "H & C", "Eastbound", 1.93);

        // Westbound: Aldgate East → Hammersmith
        g.addEdge("Aldgate East",            "Liverpool Street",         "H & C", "Westbound", 2.12);
        g.addEdge("Liverpool Street",        "Moorgate",                 "H & C", "Westbound", 1.32);
        g.addEdge("Moorgate",                "Barbican",                 "H & C", "Westbound", 1.38);
        g.addEdge("Barbican",                "Farringdon",               "H & C", "Westbound", 1.20);
        g.addEdge("Farringdon",              "King's Cross St. Pancras", "H & C", "Westbound", 3.12);
        g.addEdge("King's Cross St. Pancras","Euston Square",            "H & C", "Westbound", 1.65);
        g.addEdge("Euston Square",           "Great Portland Street",    "H & C", "Westbound", 1.30);
        g.addEdge("Great Portland Street",   "Baker Street",             "H & C", "Westbound", 1.57);
        g.addEdge("Baker Street",            "Edgware Road",             "H & C", "Westbound", 1.88);
        g.addEdge("Edgware Road",            "Paddington",               "H & C", "Westbound", 2.08);
        g.addEdge("Paddington",              "Royal Oak",                "H & C", "Westbound", 1.33);
        g.addEdge("Royal Oak",               "Westbourne Park",          "H & C", "Westbound", 1.72);
        g.addEdge("Westbourne Park",         "Ladbroke Grove",           "H & C", "Westbound", 1.48);
        g.addEdge("Ladbroke Grove",          "Latimer Road",             "H & C", "Westbound", 1.28);
        g.addEdge("Latimer Road",            "Wood Lane",                "H & C", "Westbound", 1.50); // blank in source; using eastbound value
        g.addEdge("Wood Lane",               "Shepherd's Bush Market",   "H & C", "Westbound", 1.50); // blank in source; using eastbound value
        g.addEdge("Shepherd's Bush Market",  "Goldhawk Road",            "H & C", "Westbound", 1.15);
        g.addEdge("Goldhawk Road",           "Hammersmith",              "H & C", "Westbound", 2.43);
    }

    private static void addMetropolitan(Graph g) {
        // Westbound: Aldgate → Baker Street
        g.addEdge("Aldgate",                 "Liverpool Street",         "Metropolitan", "Westbound", 1.75);
        g.addEdge("Liverpool Street",        "Moorgate",                 "Metropolitan", "Westbound", 1.32);
        g.addEdge("Moorgate",                "Barbican",                 "Metropolitan", "Westbound", 1.38);
        g.addEdge("Barbican",                "Farringdon",               "Metropolitan", "Westbound", 1.20);
        g.addEdge("Farringdon",              "King's Cross St. Pancras", "Metropolitan", "Westbound", 3.12);
        g.addEdge("King's Cross St. Pancras","Euston Square",            "Metropolitan", "Westbound", 1.65);
        g.addEdge("Euston Square",           "Great Portland Street",    "Metropolitan", "Westbound", 1.30);
        g.addEdge("Great Portland Street",   "Baker Street",             "Metropolitan", "Westbound", 2.23);

        // Eastbound: Baker Street → Aldgate
        g.addEdge("Baker Street",            "Great Portland Street",    "Metropolitan", "Eastbound", 1.98);
        g.addEdge("Great Portland Street",   "Euston Square",            "Metropolitan", "Eastbound", 1.25);
        g.addEdge("Euston Square",           "King's Cross St. Pancras", "Metropolitan", "Eastbound", 1.75);
        g.addEdge("King's Cross St. Pancras","Farringdon",               "Metropolitan", "Eastbound", 2.98);
        g.addEdge("Farringdon",              "Barbican",                 "Metropolitan", "Eastbound", 1.22);
        g.addEdge("Barbican",                "Moorgate",                 "Metropolitan", "Eastbound", 1.32);
        g.addEdge("Moorgate",                "Liverpool Street",         "Metropolitan", "Eastbound", 1.18);
        g.addEdge("Liverpool Street",        "Aldgate",                  "Metropolitan", "Eastbound", 2.18);
    }

    private static void addDistrict(Graph g) {
        // Westbound — two branches both terminating at Earl's Court.
        // Branch 1 (main, via south): Aldgate East → Earl's Court
        g.addEdge("Aldgate East",            "Tower Hill",               "District", "Westbound", 1.87);
        g.addEdge("Tower Hill",              "Bank",                 "District", "Westbound", 1.48);
        g.addEdge("Bank",                "Cannon Street",            "District", "Westbound", 0.88);
        g.addEdge("Cannon Street",           "Mansion House",            "District", "Westbound", 0.93);
        g.addEdge("Mansion House",           "Blackfriars",              "District", "Westbound", 1.22);
        g.addEdge("Blackfriars",             "Temple",                   "District", "Westbound", 1.37);
        g.addEdge("Temple",                  "Embankment",               "District", "Westbound", 1.43);
        g.addEdge("Embankment",              "Westminster",              "District", "Westbound", 1.40);
        g.addEdge("Westminster",             "St James's Park",          "District", "Westbound", 1.52);
        g.addEdge("St James's Park",         "Victoria",                 "District", "Westbound", 1.33);
        g.addEdge("Victoria",                "Sloane Square",            "District", "Westbound", 1.75);
        g.addEdge("Sloane Square",           "South Kensington",         "District", "Westbound", 2.00);
        g.addEdge("South Kensington",        "Gloucester Road",          "District", "Westbound", 1.48);
        g.addEdge("Gloucester Road",         "Earl's Court",             "District", "Westbound", 1.82);
        // Branch 2 (northern): Edgware Road → Earl's Court via Paddington
        g.addEdge("Edgware Road",            "Paddington",               "District", "Westbound", 1.85);
        g.addEdge("Paddington",              "Bayswater",                "District", "Westbound", 1.65);
        g.addEdge("Bayswater",               "Notting Hill Gate",        "District", "Westbound", 1.47);
        g.addEdge("Notting Hill Gate",       "High Street Kensington",   "District", "Westbound", 1.58);
        g.addEdge("High Street Kensington",  "Earl's Court",             "District", "Westbound", 2.72);

        // Eastbound — two branches both originating at Earl's Court.
        // Branch 1 (northern): Earl's Court → Edgware Road via High Street Kensington
        g.addEdge("Earl's Court",            "High Street Kensington",   "District", "Eastbound", 3.05);
        g.addEdge("High Street Kensington",  "Notting Hill Gate",        "District", "Eastbound", 1.68);
        g.addEdge("Notting Hill Gate",       "Bayswater",                "District", "Eastbound", 1.77);
        g.addEdge("Bayswater",               "Paddington",               "District", "Eastbound", 1.63);
        g.addEdge("Paddington",              "Edgware Road",             "District", "Eastbound", 2.15);
        // Branch 2 (main, via south): Earl's Court → Aldgate East
        g.addEdge("Earl's Court",            "Gloucester Road",          "District", "Eastbound", 1.92);
        g.addEdge("Gloucester Road",         "South Kensington",         "District", "Eastbound", 1.43);
        g.addEdge("South Kensington",        "Sloane Square",            "District", "Eastbound", 1.98);
        g.addEdge("Sloane Square",           "Victoria",                 "District", "Eastbound", 1.80);
        g.addEdge("Victoria",                "St James's Park",          "District", "Eastbound", 1.42);
        g.addEdge("St James's Park",         "Westminster",              "District", "Eastbound", 1.50);
        g.addEdge("Westminster",             "Embankment",               "District", "Eastbound", 1.37);
        g.addEdge("Embankment",              "Temple",                   "District", "Eastbound", 1.37);
        g.addEdge("Temple",                  "Blackfriars",              "District", "Eastbound", 1.40);
        g.addEdge("Blackfriars",             "Mansion House",            "District", "Eastbound", 1.52);
        g.addEdge("Mansion House",           "Cannon Street",            "District", "Eastbound", 0.98);
        g.addEdge("Cannon Street",           "Bank",                 "District", "Eastbound", 0.97);
        g.addEdge("Bank",                "Tower Hill",               "District", "Eastbound", 1.80);
        g.addEdge("Tower Hill",              "Aldgate East",             "District", "Eastbound", 1.88);
    }

    private static void addJubilee(Graph g) {
        // Eastbound: Stanmore → Stratford
        g.addEdge("Stanmore",                "Canons Park",              "Jubilee", "Eastbound", 1.95);
        g.addEdge("Canons Park",             "Queensbury",               "Jubilee", "Eastbound", 1.93);
        g.addEdge("Queensbury",              "Kingsbury",                "Jubilee", "Eastbound", 1.72);
        g.addEdge("Kingsbury",               "Wembley Park",             "Jubilee", "Eastbound", 3.47);
        g.addEdge("Wembley Park",            "Neasden",                  "Jubilee", "Eastbound", 2.60);
        g.addEdge("Neasden",                 "Dollis Hill",              "Jubilee", "Eastbound", 1.43);
        g.addEdge("Dollis Hill",             "Willesden Green",          "Jubilee", "Eastbound", 1.80);
        g.addEdge("Willesden Green",         "Kilburn",                  "Jubilee", "Eastbound", 1.68);
        g.addEdge("Kilburn",                 "West Hampstead",           "Jubilee", "Eastbound", 1.63);
        g.addEdge("West Hampstead",          "Finchley Road",            "Jubilee", "Eastbound", 1.25);
        g.addEdge("Finchley Road",           "Swiss Cottage",            "Jubilee", "Eastbound", 1.18);
        g.addEdge("Swiss Cottage",           "St John's Wood",           "Jubilee", "Eastbound", 1.52);
        g.addEdge("St John's Wood",          "Baker Street",             "Jubilee", "Eastbound", 2.77);
        g.addEdge("Baker Street",            "Bond Street",              "Jubilee", "Eastbound", 2.10);
        g.addEdge("Bond Street",             "Green Park",               "Jubilee", "Eastbound", 1.78);
        g.addEdge("Green Park",              "Westminster",              "Jubilee", "Eastbound", 1.87);
        g.addEdge("Westminster",             "Waterloo",                 "Jubilee", "Eastbound", 1.38);
        g.addEdge("Waterloo",                "Southwark",                "Jubilee", "Eastbound", 1.02);
        g.addEdge("Southwark",               "London Bridge",            "Jubilee", "Eastbound", 1.65);
        g.addEdge("London Bridge",           "Bermondsey",               "Jubilee", "Eastbound", 2.25);
        g.addEdge("Bermondsey",              "Canada Water",             "Jubilee", "Eastbound", 1.48);
        g.addEdge("Canada Water",            "Canary Wharf",             "Jubilee", "Eastbound", 2.50);
        g.addEdge("Canary Wharf",            "North Greenwich",          "Jubilee", "Eastbound", 2.23);
        g.addEdge("North Greenwich",         "Canning Town",             "Jubilee", "Eastbound", 2.15);
        g.addEdge("Canning Town",            "West Ham",                 "Jubilee", "Eastbound", 2.15);
        g.addEdge("West Ham",                "Stratford",                "Jubilee", "Eastbound", 3.15);

        // Westbound: Stratford → Stanmore
        g.addEdge("Stratford",               "West Ham",                 "Jubilee", "Westbound", 2.42);
        g.addEdge("West Ham",                "Canning Town",             "Jubilee", "Westbound", 2.13);
        g.addEdge("Canning Town",            "North Greenwich",          "Jubilee", "Westbound", 2.17);
        g.addEdge("North Greenwich",         "Canary Wharf",             "Jubilee", "Westbound", 1.98);
        g.addEdge("Canary Wharf",            "Canada Water",             "Jubilee", "Westbound", 2.63);
        g.addEdge("Canada Water",            "Bermondsey",               "Jubilee", "Westbound", 1.52);
        g.addEdge("Bermondsey",              "London Bridge",            "Jubilee", "Westbound", 2.17);
        g.addEdge("London Bridge",           "Southwark",                "Jubilee", "Westbound", 1.77);
        g.addEdge("Southwark",               "Waterloo",                 "Jubilee", "Westbound", 0.97);
        g.addEdge("Waterloo",                "Westminster",              "Jubilee", "Westbound", 1.43);
        g.addEdge("Westminster",             "Green Park",               "Jubilee", "Westbound", 1.82);
        g.addEdge("Green Park",              "Bond Street",              "Jubilee", "Westbound", 1.82);
        g.addEdge("Bond Street",             "Baker Street",             "Jubilee", "Westbound", 2.28);
        g.addEdge("Baker Street",            "St John's Wood",           "Jubilee", "Westbound", 2.85);
        g.addEdge("St John's Wood",          "Swiss Cottage",            "Jubilee", "Westbound", 1.52);
        g.addEdge("Swiss Cottage",           "Finchley Road",            "Jubilee", "Westbound", 1.18);
        g.addEdge("Finchley Road",           "West Hampstead",           "Jubilee", "Westbound", 1.20);
        g.addEdge("West Hampstead",          "Kilburn",                  "Jubilee", "Westbound", 1.55);
        g.addEdge("Kilburn",                 "Willesden Green",          "Jubilee", "Westbound", 2.07);
        g.addEdge("Willesden Green",         "Dollis Hill",              "Jubilee", "Westbound", 1.67);
        g.addEdge("Dollis Hill",             "Neasden",                  "Jubilee", "Westbound", 1.38);
        g.addEdge("Neasden",                 "Wembley Park",             "Jubilee", "Westbound", 2.65);
        g.addEdge("Wembley Park",            "Kingsbury",                "Jubilee", "Westbound", 3.47);
        g.addEdge("Kingsbury",               "Queensbury",               "Jubilee", "Westbound", 1.85);
        g.addEdge("Queensbury",              "Canons Park",              "Jubilee", "Westbound", 2.23);
        g.addEdge("Canons Park",             "Stanmore",                 "Jubilee", "Westbound", 2.87);
    }

    private static void addVictoria(Graph g) {
        // Southbound: Walthamstow Central → Brixton
        g.addEdge("Walthamstow Central",     "Blackhorse Road",          "Victoria", "Southbound", 2.12);
        g.addEdge("Blackhorse Road",         "Tottenham Hale",           "Victoria", "Southbound", 1.90);
        g.addEdge("Tottenham Hale",          "Seven Sisters",            "Victoria", "Southbound", 2.00);
        g.addEdge("Seven Sisters",           "Finsbury Park",            "Victoria", "Southbound", 3.77);
        g.addEdge("Finsbury Park",           "Highbury & Islington",     "Victoria", "Southbound", 2.90);
        g.addEdge("Highbury & Islington",    "King's Cross St. Pancras", "Victoria", "Southbound", 2.77);
        g.addEdge("King's Cross St. Pancras","Euston",                   "Victoria", "Southbound", 1.32);
        g.addEdge("Euston",                  "Warren Street",            "Victoria", "Southbound", 1.30);
        g.addEdge("Warren Street",           "Oxford Circus",            "Victoria", "Southbound", 1.72);
        g.addEdge("Oxford Circus",           "Green Park",               "Victoria", "Southbound", 1.78);
        g.addEdge("Green Park",              "Victoria",                 "Victoria", "Southbound", 1.88);
        g.addEdge("Victoria",                "Pimlico",                  "Victoria", "Southbound", 1.83);
        g.addEdge("Pimlico",                 "Vauxhall",                 "Victoria", "Southbound", 1.40);
        g.addEdge("Vauxhall",                "Stockwell",                "Victoria", "Southbound", 2.30);
        g.addEdge("Stockwell",               "Brixton",                  "Victoria", "Southbound", 2.03);

        // Northbound: Brixton → Walthamstow Central
        g.addEdge("Brixton",                 "Stockwell",                "Victoria", "Northbound", 2.18);
        g.addEdge("Stockwell",               "Vauxhall",                 "Victoria", "Northbound", 2.23);
        g.addEdge("Vauxhall",                "Pimlico",                  "Victoria", "Northbound", 1.37);
        g.addEdge("Pimlico",                 "Victoria",                 "Victoria", "Northbound", 2.18);
        g.addEdge("Victoria",                "Green Park",               "Victoria", "Northbound", 1.95);
        g.addEdge("Green Park",              "Oxford Circus",            "Victoria", "Northbound", 1.97);
        g.addEdge("Oxford Circus",           "Warren Street",            "Victoria", "Northbound", 1.53);
        g.addEdge("Warren Street",           "Euston",                   "Victoria", "Northbound", 1.32);
        g.addEdge("Euston",                  "King's Cross St. Pancras", "Victoria", "Northbound", 1.35);
        g.addEdge("King's Cross St. Pancras","Highbury & Islington",     "Victoria", "Northbound", 2.87);
        g.addEdge("Highbury & Islington",    "Finsbury Park",            "Victoria", "Northbound", 2.40);
        g.addEdge("Finsbury Park",           "Seven Sisters",            "Victoria", "Northbound", 4.25);
        g.addEdge("Seven Sisters",           "Tottenham Hale",           "Victoria", "Northbound", 1.60);
        g.addEdge("Tottenham Hale",          "Blackhorse Road",          "Victoria", "Northbound", 1.97);
        g.addEdge("Blackhorse Road",         "Walthamstow Central",      "Victoria", "Northbound", 2.12);
    }

    private static void addNorthern(Graph g) {
        // The Northern line has two separate branches through central London.
        // Both branches share Euston.
        // Dijkstra can route between branches at Euston at no extra interchange cost
        // because both carry the same line+direction label "Northern (Southbound/Northbound)".

        // Southbound — Charing Cross branch: Euston → Embankment
        g.addEdge("Euston",                  "Warren Street",            "Northern", "Southbound", 1.18);
        g.addEdge("Warren Street",           "Goodge Street",            "Northern", "Southbound", 1.07);
        g.addEdge("Goodge Street",           "Tottenham Court Road",     "Northern", "Southbound", 1.32);
        g.addEdge("Tottenham Court Road",    "Leicester Square",         "Northern", "Southbound", 0.98);
        g.addEdge("Leicester Square",        "Charing Cross",            "Northern", "Southbound", 1.20);
        g.addEdge("Charing Cross",           "Embankment",               "Northern", "Southbound", 0.80);
        // Southbound — City branch: Camden Town → Bank
        g.addEdge("Camden Town",             "Euston",                   "Northern", "Southbound", 2.95);
        g.addEdge("Euston",                  "King's Cross St. Pancras", "Northern", "Southbound", 1.53);
        g.addEdge("King's Cross St. Pancras","Angel",                    "Northern", "Southbound", 2.18);
        g.addEdge("Angel",                   "Old Street",               "Northern", "Southbound", 2.87);
        g.addEdge("Old Street",              "Moorgate",                 "Northern", "Southbound", 1.37);
        g.addEdge("Moorgate",                "Bank",                     "Northern", "Southbound", 1.90);
        g.addEdge("Bank",                    "London Bridge",            "Northern", "Southbound", 2.22);

        // Northbound — City branch: London Bridge → Camden Town
        g.addEdge("London Bridge",           "Bank",                     "Northern", "Northbound", 2.25);
        g.addEdge("Bank",                    "Moorgate",                 "Northern", "Northbound", 1.77);
        g.addEdge("Moorgate",                "Old Street",               "Northern", "Northbound", 1.45);
        g.addEdge("Old Street",              "Angel",                    "Northern", "Northbound", 2.43);
        g.addEdge("Angel",                   "King's Cross St. Pancras", "Northern", "Northbound", 2.15);
        g.addEdge("King's Cross St. Pancras","Euston",                   "Northern", "Northbound", 1.33);
        g.addEdge("Euston",                  "Camden Town",              "Northern", "Northbound", 3.88);
        // Northbound — Charing Cross branch: Embankment → Euston
        g.addEdge("Embankment",              "Charing Cross",            "Northern", "Northbound", 0.87);
        g.addEdge("Charing Cross",           "Leicester Square",         "Northern", "Northbound", 1.17);
        g.addEdge("Leicester Square",        "Tottenham Court Road",     "Northern", "Northbound", 1.03);
        g.addEdge("Tottenham Court Road",    "Goodge Street",            "Northern", "Northbound", 1.28);
        g.addEdge("Goodge Street",           "Warren Street",            "Northern", "Northbound", 1.07);
        g.addEdge("Warren Street",           "Euston",                   "Northern", "Northbound", 1.18);
    }

    private static void addBakerloo(Graph g) {
        // Southbound: Paddington → Embankment
        g.addEdge("Paddington",              "Edgware Road",             "Bakerloo", "Southbound", 1.57);
        g.addEdge("Edgware Road",            "Marylebone",               "Bakerloo", "Southbound", 1.12);
        g.addEdge("Marylebone",              "Baker Street",             "Bakerloo", "Southbound", 1.17);
        g.addEdge("Baker Street",            "Regents Park",             "Bakerloo", "Southbound", 1.68);
        g.addEdge("Regents Park",            "Oxford Circus",            "Bakerloo", "Southbound", 1.85);
        g.addEdge("Oxford Circus",           "Piccadilly Circus",        "Bakerloo", "Southbound", 1.95);
        g.addEdge("Piccadilly Circus",       "Charing Cross",            "Bakerloo", "Southbound", 1.35);
        g.addEdge("Charing Cross",           "Embankment",               "Bakerloo", "Southbound", 0.95);
        g.addEdge("Embankment",              "Waterloo",                 "Bakerloo", "Southbound", 1.37);

        // Northbound: Waterloo → Paddington
        g.addEdge("Waterloo",                "Embankment",               "Bakerloo", "Northbound", 1.40);
        g.addEdge("Embankment",              "Charing Cross",            "Bakerloo", "Northbound", 0.97);
        g.addEdge("Charing Cross",           "Piccadilly Circus",        "Bakerloo", "Northbound", 1.60);
        g.addEdge("Piccadilly Circus",       "Oxford Circus",            "Bakerloo", "Northbound", 2.02);
        g.addEdge("Oxford Circus",           "Regents Park",             "Bakerloo", "Northbound", 1.72);
        g.addEdge("Regents Park",            "Baker Street",             "Bakerloo", "Northbound", 1.65);
        g.addEdge("Baker Street",            "Marylebone",               "Bakerloo", "Northbound", 1.08);
        g.addEdge("Marylebone",              "Edgware Road",             "Bakerloo", "Northbound", 1.08);
        g.addEdge("Edgware Road",            "Paddington",               "Bakerloo", "Northbound", 1.62);
    }

    private static void addCentral(Graph g) {
        // Eastbound: Notting Hill Gate → Liverpool Street
        g.addEdge("Notting Hill Gate",       "Queensway",                "Central", "Eastbound", 1.17);
        g.addEdge("Queensway",               "Lancaster Gate",           "Central", "Eastbound", 1.35);
        g.addEdge("Lancaster Gate",          "Marble Arch",              "Central", "Eastbound", 1.92);
        g.addEdge("Marble Arch",             "Bond Street",              "Central", "Eastbound", 1.00);
        g.addEdge("Bond Street",             "Oxford Circus",            "Central", "Eastbound", 1.10);
        g.addEdge("Oxford Circus",           "Tottenham Court Road",     "Central", "Eastbound", 0.98);
        g.addEdge("Tottenham Court Road",    "Holborn",                  "Central", "Eastbound", 1.63);
        g.addEdge("Holborn",                 "Chancery Lane",            "Central", "Eastbound", 0.87);
        g.addEdge("Chancery Lane",           "St Paul's",                "Central", "Eastbound", 1.52);
        g.addEdge("St Paul's",               "Bank",                     "Central", "Eastbound", 1.62);
        g.addEdge("Bank",                    "Liverpool Street",         "Central", "Eastbound", 1.62);

        // Westbound: Liverpool Street → Notting Hill Gate
        g.addEdge("Liverpool Street",        "Bank",                     "Central", "Westbound", 1.65);
        g.addEdge("Bank",                    "St Paul's",                "Central", "Westbound", 1.63);
        g.addEdge("St Paul's",               "Chancery Lane",            "Central", "Westbound", 1.52);
        g.addEdge("Chancery Lane",           "Holborn",                  "Central", "Westbound", 0.85);
        g.addEdge("Holborn",                 "Tottenham Court Road",     "Central", "Westbound", 1.38);
        g.addEdge("Tottenham Court Road",    "Oxford Circus",            "Central", "Westbound", 1.02);
        g.addEdge("Oxford Circus",           "Bond Street",              "Central", "Westbound", 1.03);
        g.addEdge("Bond Street",             "Marble Arch",              "Central", "Westbound", 1.02);
        g.addEdge("Marble Arch",             "Lancaster Gate",           "Central", "Westbound", 1.62);
        g.addEdge("Lancaster Gate",          "Queensway",                "Central", "Westbound", 1.65);
        g.addEdge("Queensway",               "Notting Hill Gate",        "Central", "Westbound", 1.17);
    }

    private static void addPiccadilly(Graph g) {
        // Eastbound: Earl's Court → King's Cross St. Pancras
        g.addEdge("Earl's Court",            "Gloucester Road",          "Piccadilly", "Eastbound", 1.37);
        g.addEdge("Gloucester Road",         "South Kensington",         "Piccadilly", "Eastbound", 1.28);
        g.addEdge("South Kensington",        "Knightsbridge",            "Piccadilly", "Eastbound", 2.48);
        g.addEdge("Knightsbridge",           "Hyde Park Corner",         "Piccadilly", "Eastbound", 1.10);
        g.addEdge("Hyde Park Corner",        "Green Park",               "Piccadilly", "Eastbound", 1.73);
        g.addEdge("Green Park",              "Piccadilly Circus",        "Piccadilly", "Eastbound", 1.10);
        g.addEdge("Piccadilly Circus",       "Leicester Square",         "Piccadilly", "Eastbound", 1.17);
        g.addEdge("Leicester Square",        "Covent Garden",            "Piccadilly", "Eastbound", 0.77);
        g.addEdge("Covent Garden",           "Holborn",                  "Piccadilly", "Eastbound", 1.40);
        g.addEdge("Holborn",                 "Russell Square",           "Piccadilly", "Eastbound", 1.55);
        g.addEdge("Russell Square",          "King's Cross St. Pancras", "Piccadilly", "Eastbound", 1.90);

        // Westbound: King's Cross St. Pancras → Earl's Court
        g.addEdge("King's Cross St. Pancras","Russell Square",           "Piccadilly", "Westbound", 1.67);
        g.addEdge("Russell Square",          "Holborn",                  "Piccadilly", "Westbound", 1.37);
        g.addEdge("Holborn",                 "Covent Garden",            "Piccadilly", "Westbound", 1.53);
        g.addEdge("Covent Garden",           "Leicester Square",         "Piccadilly", "Westbound", 0.77);
        g.addEdge("Leicester Square",        "Piccadilly Circus",        "Piccadilly", "Westbound", 1.07);
        g.addEdge("Piccadilly Circus",       "Green Park",               "Piccadilly", "Westbound", 1.18);
        g.addEdge("Green Park",              "Hyde Park Corner",         "Piccadilly", "Westbound", 1.73);
        g.addEdge("Hyde Park Corner",        "Knightsbridge",            "Piccadilly", "Westbound", 1.12);
        g.addEdge("Knightsbridge",           "South Kensington",         "Piccadilly", "Westbound", 2.23);
        g.addEdge("South Kensington",        "Gloucester Road",          "Piccadilly", "Westbound", 1.37);
        g.addEdge("Gloucester Road",         "Earl's Court",             "Piccadilly", "Westbound", 1.37);
    }

    private static void addMildmay(Graph g) {
        // Mildmay Line (London Overground). The line splits at Willesden Junction
        // into three branches: Richmond, Clapham Junction, and Stratford.
        // WILLESDEN JUNCTION A (Richmond branch) and WILLESDEN JUNCTION D
        // (Clapham/Stratford branch) are merged into one node "Willesden Junction"
        // so Dijkstra can route across branches at that station.

        // Eastbound — Richmond branch: Richmond → Willesden Junction
        g.addEdge("Richmond",                "Kew Gardens",              "Mildmay", "Eastbound", 3.00);
        g.addEdge("Kew Gardens",             "Gunnersbury",              "Mildmay", "Eastbound", 3.00);
        g.addEdge("Gunnersbury",             "South Acton",              "Mildmay", "Eastbound", 3.00);
        g.addEdge("South Acton",             "Acton Central",            "Mildmay", "Eastbound", 3.00);
        g.addEdge("Acton Central",           "Willesden Junction",       "Mildmay", "Eastbound", 6.00);
        // Eastbound — Clapham Junction branch: Clapham Junction → Willesden Junction
        g.addEdge("Clapham Junction",        "Imperial Wharf",           "Mildmay", "Eastbound", 5.00);
        g.addEdge("Imperial Wharf",          "West Brompton",            "Mildmay", "Eastbound", 3.00);
        g.addEdge("West Brompton",           "Kensington Olympia",       "Mildmay", "Eastbound", 3.00);
        g.addEdge("Kensington Olympia",      "Shepherd's Bush",          "Mildmay", "Eastbound", 3.00);
        g.addEdge("Shepherd's Bush",         "Willesden Junction",       "Mildmay", "Eastbound", 9.00);
        // Eastbound — main line: Willesden Junction → Stratford
        g.addEdge("Willesden Junction",      "Kensal Rise",              "Mildmay", "Eastbound", 2.00);
        g.addEdge("Kensal Rise",             "Brondesbury Park",         "Mildmay", "Eastbound", 2.00);
        g.addEdge("Brondesbury Park",        "Brondesbury",              "Mildmay", "Eastbound", 2.00);
        g.addEdge("Brondesbury",             "West Hampstead",           "Mildmay", "Eastbound", 2.00);
        g.addEdge("West Hampstead",          "Finchley Road & Frognal",  "Mildmay", "Eastbound", 1.00);
        g.addEdge("Finchley Road & Frognal", "Hampstead Heath",          "Mildmay", "Eastbound", 2.00);
        g.addEdge("Hampstead Heath",         "Gospel Oak",               "Mildmay", "Eastbound", 3.00);
        g.addEdge("Gospel Oak",              "Kentish Town West",        "Mildmay", "Eastbound", 2.00);
        g.addEdge("Kentish Town West",       "Camden Road",              "Mildmay", "Eastbound", 3.00);
        g.addEdge("Camden Road",             "Caledonian Road & Barnsbury","Mildmay","Eastbound", 3.00);
        g.addEdge("Caledonian Road & Barnsbury","Highbury & Islington",  "Mildmay", "Eastbound", 2.00);
        g.addEdge("Highbury & Islington",    "Canonbury",                "Mildmay", "Eastbound", 2.00);
        g.addEdge("Canonbury",               "Dalston Kingsland",        "Mildmay", "Eastbound", 2.00);
        g.addEdge("Dalston Kingsland",       "Hackney Central",          "Mildmay", "Eastbound", 2.00);
        g.addEdge("Hackney Central",         "Homerton",                 "Mildmay", "Eastbound", 2.00);
        g.addEdge("Homerton",                "Hackney Wick",             "Mildmay", "Eastbound", 3.00);
        g.addEdge("Hackney Wick",            "Stratford",                "Mildmay", "Eastbound", 6.00);

        // Westbound — main line: Stratford → Willesden Junction
        g.addEdge("Stratford",               "Hackney Wick",             "Mildmay", "Westbound", 3.00);
        g.addEdge("Hackney Wick",            "Homerton",                 "Mildmay", "Westbound", 3.00);
        g.addEdge("Homerton",                "Hackney Central",          "Mildmay", "Westbound", 3.00);
        g.addEdge("Hackney Central",         "Dalston Kingsland",        "Mildmay", "Westbound", 3.00);
        g.addEdge("Dalston Kingsland",       "Canonbury",                "Mildmay", "Westbound", 2.00);
        g.addEdge("Canonbury",               "Highbury & Islington",     "Mildmay", "Westbound", 3.00);
        g.addEdge("Highbury & Islington",    "Caledonian Road & Barnsbury","Mildmay","Westbound", 1.00);
        g.addEdge("Caledonian Road & Barnsbury","Camden Road",           "Mildmay", "Westbound", 4.00);
        g.addEdge("Camden Road",             "Kentish Town West",        "Mildmay", "Westbound", 2.00);
        g.addEdge("Kentish Town West",       "Gospel Oak",               "Mildmay", "Westbound", 3.00);
        g.addEdge("Gospel Oak",              "Hampstead Heath",          "Mildmay", "Westbound", 2.00);
        g.addEdge("Hampstead Heath",         "Finchley Road & Frognal",  "Mildmay", "Westbound", 2.00);
        g.addEdge("Finchley Road & Frognal", "West Hampstead",           "Mildmay", "Westbound", 2.00);
        g.addEdge("West Hampstead",          "Brondesbury",              "Mildmay", "Westbound", 2.00);
        g.addEdge("Brondesbury",             "Brondesbury Park",         "Mildmay", "Westbound", 2.00);
        g.addEdge("Brondesbury Park",        "Kensal Rise",              "Mildmay", "Westbound", 2.00);
        g.addEdge("Kensal Rise",             "Willesden Junction",       "Mildmay", "Westbound", 4.00);
        // Westbound — Clapham Junction branch: Willesden Junction → Clapham Junction
        g.addEdge("Willesden Junction",      "Shepherd's Bush",          "Mildmay", "Westbound", 9.00);
        g.addEdge("Shepherd's Bush",         "Kensington Olympia",       "Mildmay", "Westbound", 2.00);
        g.addEdge("Kensington Olympia",      "West Brompton",            "Mildmay", "Westbound", 3.00);
        g.addEdge("West Brompton",           "Imperial Wharf",           "Mildmay", "Westbound", 2.00);
        g.addEdge("Imperial Wharf",          "Clapham Junction",         "Mildmay", "Westbound", 6.00);
        // Westbound — Richmond branch: Willesden Junction → Richmond
        g.addEdge("Willesden Junction",      "Acton Central",            "Mildmay", "Westbound", 6.00);
        g.addEdge("Acton Central",           "South Acton",              "Mildmay", "Westbound", 3.00);
        g.addEdge("South Acton",             "Gunnersbury",              "Mildmay", "Westbound", 3.00);
        g.addEdge("Gunnersbury",             "Kew Gardens",              "Mildmay", "Westbound", 3.00);
        g.addEdge("Kew Gardens",             "Richmond",                 "Mildmay", "Westbound", 3.00);
    }

    private static void addElizabeth(Graph g) {
        // Westbound: Liverpool Street → Paddington
        g.addEdge("Liverpool Street",        "Farringdon",               "Elizabeth", "Westbound", 2.00);
        g.addEdge("Farringdon",              "Tottenham Court Road",     "Elizabeth", "Westbound", 3.00);
        g.addEdge("Tottenham Court Road",    "Bond Street",              "Elizabeth", "Westbound", 2.00);
        g.addEdge("Bond Street",             "Paddington",               "Elizabeth", "Westbound", 4.00);

        // Eastbound: Paddington → Liverpool Street
        g.addEdge("Paddington",              "Bond Street",              "Elizabeth", "Eastbound", 3.00);
        g.addEdge("Bond Street",             "Tottenham Court Road",     "Elizabeth", "Eastbound", 2.00);
        g.addEdge("Tottenham Court Road",    "Farringdon",               "Elizabeth", "Eastbound", 3.00);
        g.addEdge("Farringdon",              "Liverpool Street",         "Elizabeth", "Eastbound", 3.00);
    }

    // -----------------------------------------------------------------------
    // Terminal menu
    // -----------------------------------------------------------------------

    static void showMenu(Graph g) {
        try (Scanner sc = new Scanner(System.in)) {
            showMenuLoop(g, sc);
        }
    }

    private static void showMenuLoop(Graph g, Scanner sc) {
        while (true) {
            System.out.println("\n=== TfL Application (Version 1) ===");
            System.out.println("1.  List stations");
            System.out.println("2.  Add delay");
            System.out.println("3.  Remove delay");
            System.out.println("4.  Close track section");
            System.out.println("5.  Open track section");
            System.out.println("6.  Print closed sections");
            System.out.println("7.  Print delayed sections");
            System.out.println("8.  Find fastest route");
            System.out.println("9.  Station information");
            System.out.println("0.  Exit");
            System.out.print("Choice: ");

            int choice;
            try { choice = Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a number."); continue; }

            switch (choice) {
                case 0:
                    System.out.println("Goodbye.");
                    return;
                case 1:
                    listStations(g);
                    break;
                case 2:
                    modifyDelay(g, sc, true);
                    break;
                case 3:
                    modifyDelay(g, sc, false);
                    break;
                case 4:
                    modifyTrack(g, sc, true);
                    break;
                case 5:
                    modifyTrack(g, sc, false);
                    break;
                case 6:
                    g.printClosed();
                    break;
                case 7:
                    g.printDelayed();
                    break;
                case 8:
                    findRoute(g, sc);
                    break;
                case 9:
                    showStationInfo(g, sc);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // -----------------------------------------------------------------------
    // Menu action helpers
    // -----------------------------------------------------------------------

    private static void listStations(Graph g) {
        System.out.println("Stations (" + g.getStationCount() + " total):");
        for (int i = 0; i < g.getStationCount(); i++) {
            System.out.printf("  %3d. %s%n", i + 1, g.getStation(i).name);
        }
    }

    private static void modifyDelay(Graph g, Scanner sc, boolean adding) {
        listStations(g);
        int from = pickStation(g, sc, "From station number: ") - 1;
        int to   = pickStation(g, sc, "To station number:   ") - 1;
        if (from < 0 || to < 0) return;

        int edgeCount = g.listEdgesBetween(from, to);
        if (edgeCount == 0) {
            System.out.println("No direct edge from "
                    + g.getStation(from).name + " to " + g.getStation(to).name + ".");
            return;
        }

        System.out.print("Apply to all edges (0) or pick one: ");
        int pick = readInt(sc);
        String line      = null;
        String direction = null;
        if (pick > 0) {
            Edge e = g.getEdgeBetween(from, to, pick);
            if (e == null) { System.out.println("Invalid selection."); return; }
            line      = e.line;
            direction = e.direction;
        }

        System.out.print("Delay minutes: ");
        double minutes = readDouble(sc);
        boolean ok = adding
                ? g.addDelay(from, to, line, direction, minutes)
                : g.removeDelay(from, to, line, direction, minutes);
        System.out.println(ok ? (adding ? "Delay added." : "Delay removed.") : "Edge not found.");
    }

    private static void modifyTrack(Graph g, Scanner sc, boolean closing) {
        listStations(g);
        int from = pickStation(g, sc, "From station number: ") - 1;
        int to   = pickStation(g, sc, "To station number:   ") - 1;
        if (from < 0 || to < 0) return;

        int edgeCount = g.listEdgesBetween(from, to);
        if (edgeCount == 0) {
            System.out.println("No direct edge found.");
            return;
        }

        System.out.print("Apply to all edges (0) or pick one: ");
        int pick = readInt(sc);
        String line      = null;
        String direction = null;
        if (pick > 0) {
            Edge e = g.getEdgeBetween(from, to, pick);
            if (e == null) { System.out.println("Invalid selection."); return; }
            line      = e.line;
            direction = e.direction;
        }

        boolean ok = closing
                ? g.closeTrack(from, to, line, direction)
                : g.openTrack(from, to, line, direction);
        System.out.println(ok ? (closing ? "Track closed." : "Track opened.") : "Edge not found.");
    }

    private static void findRoute(Graph g, Scanner sc) {
        listStations(g);
        int from = pickStation(g, sc, "Start station number: ") - 1;
        int to   = pickStation(g, sc, "End station number:   ") - 1;
        if (from < 0 || to < 0) return;
        Dijkstra.findFastestRoute(g, from, to);
    }

    private static void showStationInfo(Graph g, Scanner sc) {
        listStations(g);
        int id = pickStation(g, sc, "Station number: ") - 1;
        if (id < 0) return;
        g.printStationInfo(id);
    }

    // -----------------------------------------------------------------------
    // Input helpers
    // -----------------------------------------------------------------------

    private static int pickStation(Graph g, Scanner sc, String prompt) {
        System.out.print(prompt);
        int n = readInt(sc);
        if (n < 1 || n > g.getStationCount()) {
            System.out.println("Invalid station number.");
            return -1;
        }
        return n;
    }

    private static int readInt(Scanner sc) {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private static double readDouble(Scanner sc) {
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    // -----------------------------------------------------------------------
    // Sample run
    // -----------------------------------------------------------------------

    static void runSample(Graph g) {
        System.out.println("=== TfL Application Version 1 — Sample Output ===\n");

        // Engineer operations
        g.addDelay(
                g.findStation("Warren Street").id,
                g.findStation("Oxford Circus").id,
                "Victoria", "Southbound", 5.0);

        g.closeTrack(
                g.findStation("Bond Street").id,
                g.findStation("Green Park").id,
                "Jubilee", "Eastbound");

        System.out.println("--- Delayed sections ---");
        g.printDelayed();

        System.out.println("\n--- Closed sections ---");
        g.printClosed();

        System.out.println();
        Dijkstra.findFastestRoute(g,
                g.findStation("Baker Street").id,
                g.findStation("Waterloo").id);

        System.out.println();
        g.printStationInfo(g.findStation("Baker Street").id);
    }

    // -----------------------------------------------------------------------
    // Entry point
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        Graph g = buildNetwork();
        if (args.length > 0 && args[0].equalsIgnoreCase("sample")) {
            runSample(g);
        } else {
            showMenu(g);
        }
    }
}
