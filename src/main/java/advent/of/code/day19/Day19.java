package advent.of.code.day19;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day19 {
    // Yeah this got out of hand.
    
    public static void part1(Input in, Output out) {
        var scanners = parseScanners(in);
        var graph = buildGraph(scanners);
        var beacons = new HashSet<Point>();
        visit1(0, p -> p, new HashSet<>(), graph, scanners, beacons);
        out.writeln(beacons.size());
    }
    
    public static void part2(Input in, Output out) {
        var scanners = parseScanners(in);
        var graph = buildGraph(scanners);
        var positions = new ArrayList<Point>();
        visit2(0, p -> p, p -> p, new HashSet<>(), graph, scanners, positions);
        var max = combinations(positions.size(), 2).stream()
            .mapToInt(pair -> positions.get(pair[0]).minus(positions.get(pair[1])).manhattanDist())
            .max().orElseThrow();
        out.writeln(max);
    }
    
    private static List<Set<Point>> parseScanners(Input in) {
        var scanners = new ArrayList<Set<Point>>();
        while (in.readLine() != null) {
            var scanner = in.lines().takeWhile(line -> !line.isEmpty())
                .map(line -> Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray())
                .map(arr -> new Point(arr[0], arr[1], arr[2]))
                .collect(Collectors.toSet());
            scanners.add(scanner);
            in.readLine();
        }
        return scanners;
    }
    
    private static Map<Integer, List<Edge>> buildGraph(List<Set<Point>> scanners) {
        return combinations(scanners.size(), 2).stream().parallel()
            .flatMap(pair -> tryConnect(scanners, pair[0], pair[1], 12).entrySet().stream())
            .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }
    
    private static List<int[]> combinations(int n, int r) {
        var combinations = new ArrayList<int[]>();
        combinationsHelper(combinations, new int[r], 0, n-1, 0);
        return combinations;
    }
    
    private static void combinationsHelper(List<int[]> combinations, int[] data, int start, int end, int idx) {
        if (idx == data.length) combinations.add(data.clone());
        else {
            var max = Math.min(end, end - data.length + idx + 1);
            for (var i = start; i <= max; i++) {
                data[idx] = i;
                combinationsHelper(combinations, data, i + 1, end, idx + 1);
            }
        }
    }
    
    private interface Transform extends Function<Point, Point> {}
    private record Edge(int to, Transform orientation, Transform transform) {}
    private record Point(int x, int y, int z) {
        Point minus(Point o) { return new Point(x-o.x, y-o.y, z-o.z); }
        Point plus(Point o)  { return new Point(x+o.x, y+o.y, z+o.z); }
        int manhattanDist()  { return Math.abs(x) + Math.abs(y) + Math.abs(z); }
    }
    
    // Precomputed orientation transforms and their inverses, anyone?
    private record InvertibleTransform(Transform transform, int idx, int invIdx) {}
    private static final List<InvertibleTransform> ORIENTATIONS = List.of(
        // Roll    = (x,y,z) -> (x,-z,y)
        // TurnCW  = (x,y,z) -> (y,-x,z)
        // TurnCCW = (x,y,z) -> (-y,x,z)
        new InvertibleTransform(p -> new Point( p.x, -p.z,  p.y),  0, 12), // Roll 0
        new InvertibleTransform(p -> new Point(-p.z, -p.x,  p.y),  1, 19), // TurnCW 0
        new InvertibleTransform(p -> new Point(-p.x,  p.z,  p.y),  2,  2), // TurnCW 1
        new InvertibleTransform(p -> new Point( p.z,  p.x,  p.y),  3,  5), // TurnCW 2
        new InvertibleTransform(p -> new Point( p.z, -p.y,  p.x),  4,  4), // Roll 1
        new InvertibleTransform(p -> new Point( p.y,  p.z,  p.x),  5,  3), // TurnCCW 0
        new InvertibleTransform(p -> new Point(-p.z,  p.y,  p.x),  6, 16), // TurnCCW 1
        new InvertibleTransform(p -> new Point(-p.y, -p.z,  p.x),  7, 15), // TurnCCW 2
        new InvertibleTransform(p -> new Point(-p.y, -p.x, -p.z),  8,  8), // Roll 2
        new InvertibleTransform(p -> new Point(-p.x,  p.y, -p.z),  9,  9), // TurnCW 0
        new InvertibleTransform(p -> new Point( p.y,  p.x, -p.z), 10, 10), // TurnCW 1
        new InvertibleTransform(p -> new Point( p.x, -p.y, -p.z), 11, 11), // TurnCW 2
        new InvertibleTransform(p -> new Point( p.x,  p.z, -p.y), 12,  0), // Roll 3
        new InvertibleTransform(p -> new Point(-p.z,  p.x, -p.y), 13, 17), // TurnCCW 0
        new InvertibleTransform(p -> new Point(-p.x, -p.z, -p.y), 14, 14), // TurnCCW 1
        new InvertibleTransform(p -> new Point( p.z, -p.x, -p.y), 15,  7), // TurnCCW 2
        new InvertibleTransform(p -> new Point( p.z,  p.y, -p.x), 16,  6), // Roll 4
        new InvertibleTransform(p -> new Point( p.y, -p.z, -p.x), 17, 13), // TurnCW 0
        new InvertibleTransform(p -> new Point(-p.z, -p.y, -p.x), 18, 18), // TurnCW 1
        new InvertibleTransform(p -> new Point(-p.y,  p.z, -p.x), 19,  1), // TurnCW 2
        new InvertibleTransform(p -> new Point(-p.y,  p.x,  p.z), 20, 22), // Roll 5
        new InvertibleTransform(p -> new Point(-p.x, -p.y,  p.z), 21, 21), // TurnCCW 0
        new InvertibleTransform(p -> new Point( p.y, -p.x,  p.z), 22, 20), // TurnCCW 1
        new InvertibleTransform(p -> new Point( p.x,  p.y,  p.z), 23, 23)  // TurnCCW 2
    );
    
    private static Map<Integer, Edge> tryConnect(List<Set<Point>> scanners, int i, int j, int required) {
        var scanner1 = scanners.get(i);
        var scanner2 = scanners.get(j);
        for (var p1 : scanner1) {
            for (var p2 : scanner2) {
                for (var orientation : ORIENTATIONS) {
                    var p2Oriented = orientation.transform.apply(p2);
                    var s2Position = p1.minus(p2Oriented);
                    Transform transform = p -> orientation.transform.apply(p).plus(s2Position);
                    int found = 0, remaining = scanner2.size();
                    for (var p2i : scanner2) {
                        if (remaining-- < required-found) break;
                        if (scanner1.contains(transform.apply(p2i)) && ++found == required) {
                            var invOrientation = ORIENTATIONS.get(orientation.invIdx);
                            var p1Oriented = invOrientation.transform.apply(p1);
                            var s1Position = p2.minus(p1Oriented);
                            Transform invTransform = p -> invOrientation.transform.apply(p).plus(s1Position);
                            return Map.of(
                                i, new Edge(j, orientation.transform, transform),
                                j, new Edge(i, invOrientation.transform, invTransform)
                            );
                        }
                    }
                }
            }
        }
        return Map.of();
    }
    
    private static void visit1(
        int curr,
        Function<Point, Point> transform,
        Set<Integer> visited,
        Map<Integer, List<Edge>> graph,
        List<Set<Point>> scanners,
        Set<Point> beacons
    ) {
        if (!visited.add(curr)) return;
        scanners.get(curr).stream().map(transform).forEach(beacons::add);
        for (var edge : graph.get(curr))
            visit1(edge.to, transform.compose(edge.transform), visited, graph, scanners, beacons);
    }
    
    private static void visit2(
        int curr,
        Function<Point, Point> orientation,
        Function<Point, Point> transform,
        Set<Integer> visited,
        Map<Integer, List<Edge>> graph,
        List<Set<Point>> scanners,
        List<Point> positions
    ) {
        if (!visited.add(curr)) return;
        var p2 = scanners.get(curr).iterator().next();
        var p1 = transform.apply(p2);
        var p2Oriented = orientation.apply(p2);
        var s2Position = p1.minus(p2Oriented);
        positions.add(s2Position);
        for (var edge : graph.get(curr))
            visit2(edge.to, orientation.compose(edge.orientation), transform.compose(edge.transform), visited, graph, scanners, positions);
    }
}
