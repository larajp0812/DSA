# TfL Route Finder Application

This project implements two versions of a Transport for London (TfL) route finder application in Java, as part of a Data Structures and Algorithms coursework.

## Versions

### Version 1 (TfL_V1)

- Hand-coded implementation using only core Java classes
- Uses arrays for graph representation and adjacency matrices
- Custom Dijkstra's algorithm with manual priority queue

### Version 2 (TfL_V2)

- Uses Java collections (HashMap, ArrayList, PriorityQueue)
- Improved data structures and algorithms for better performance

## Features

- Add/remove journey time delays between stations
- Close/open track sections
- Print lists of closed and delayed track sections
- Find the fastest route between two stations (with interchanges)
- Display station information
- Terminal-based user interface

## Network Model

The application models a subset of TfL's Circle, Jubilee, and Victoria lines with the following stations:

- Baker Street, Bond Street, Green Park, Victoria, Warren Street, Oxford Circus, Paddington, King's Cross, Euston, Camden Town

## How to Run

### Version 1

1. Open Terminal and go to the Version 1 folder:
   ```bash
   cd "/Users/larajohnson/AUOW/SEM2/Data Structures and Algorithms/CW/DSA/TfL_V1"
   ```
2. Compile:
   ```bash
   javac TfLApp.java
   ```
3. Run the interactive menu:
   ```bash
   java TfLApp menu
   ```
4. If you want the sample output mode instead of the menu, run:
   ```bash
   java TfLApp
   ```

### Version 2

1. Open Terminal and go to the Version 2 folder:
   ```bash
   cd "/Users/larajohnson/AUOW/SEM2/Data Structures and Algorithms/CW/DSA/TfL_V2"
   ```
2. Compile:
   ```bash
   javac TfLApp.java
   ```
3. Run the interactive menu:
   ```bash
   java TfLApp menu
   ```
4. For sample output mode instead of the menu, run:
   ```bash
   java TfLApp
   ```

The application will then show the TfL menu and allow you to interact with the network.

## Sample Output

- V1_output.txt: Sample output from Version 1
- V2_output.txt: Sample output from Version 2

## Coursework Requirements

This fulfills parts 1 and 2 of the coursework deliverables:

1. Software developed: Java projects for two versions
2. Sample output: Text files with outputs for both versions

## Notes

- Version 1 adheres to the constraint of using only hand-coded data structures
- Version 2 demonstrates the use of Java library classes for improved efficiency
- The network is simplified for demonstration purposes
