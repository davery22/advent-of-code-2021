package advent.of.code.day12;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.*;

public class Day12 {
    public static void part1(Input in, Output out) {
        var graph = parseGraph(in);
        long[] paths = { 0 };
        visit1("start", graph, new HashSet<>(), paths);
        out.writeln(paths[0]);
    }
    
    public static void part2(Input in, Output out) {
        var graph = parseGraph(in);
        long[] paths = { 0 };
        visit2("start", graph, new HashSet<>(), null, paths);
        out.writeln(paths[0]);
    }
    
    private static Map<String, List<String>> parseGraph(Input in) {
        var graph = new HashMap<String, List<String>>();
        in.lines().map(line -> line.split("-")).forEach(pair -> {
            graph.computeIfAbsent(pair[0], k -> new ArrayList<>()).add(pair[1]);
            graph.computeIfAbsent(pair[1], k -> new ArrayList<>()).add(pair[0]);
        });
        return graph;
    }
    
    private static void visit1(
        String cave,
        Map<String, List<String>> graph,
        Set<String> visited,
        long[] paths
    ) {
        if ("end".equals(cave)) { paths[0]++; return; }
        if (isSmallCave(cave) && !visited.add(cave)) return;
        for (var neighbor : graph.get(cave)) visit1(neighbor, graph, visited, paths);
        visited.remove(cave);
    }
    
    private static void visit2(
        String cave,
        Map<String, List<String>> graph,
        Set<String> visited,
        String visitedTwice,
        long[] paths
    ) {
        if ("end".equals(cave)) { paths[0]++; return; }
        if (isSmallCave(cave) && !visited.add(cave)) {
            if (visitedTwice != null || "start".equals(cave)) return;
            visitedTwice = cave;
        }
        for (var neighbor : graph.get(cave)) visit2(neighbor, graph, visited, visitedTwice, paths);
        if (!cave.equals(visitedTwice)) visited.remove(cave);
    }
    
    private static boolean isSmallCave(String cave) {
        return Character.isLowerCase(cave.charAt(0));
    }
}
