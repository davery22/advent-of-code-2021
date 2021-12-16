package advent.of.code.day13;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day13 {
    public static void part1(Input in, Output out) {
        var dots = parseDots(in);
        parseFolds(in).limit(1).forEach(fold -> applyFold(fold, dots));
        out.writeln(dots.size());
    }
    
    public static void part2(Input in, Output out) {
        var dots = parseDots(in);
        parseFolds(in).forEach(fold -> applyFold(fold, dots));
        var dimX = 1 + dots.stream().mapToInt(Dot::x).max().orElseThrow();
        var dimY = 1 + dots.stream().mapToInt(Dot::y).max().orElseThrow();
        var grid = new int[dimY][dimX];
        for (var dot : dots) grid[dot.y][dot.x] = 1;
        for (var row : grid) out.writeln(Arrays.stream(row).mapToObj(i -> i == 0 ? " " : "█").collect(Collectors.joining()));
    }
    
    enum Axis { X, Y }
    record Dot(int x, int y) {}
    record Fold(Axis axis, int unit) {}
    
    private static Set<Dot> parseDots(Input in) {
        return in.lines().takeWhile(line -> !line.isEmpty())
            .map(line -> line.split(","))
            .map(parts -> new Dot(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])))
            .collect(Collectors.toSet());
    }
    
    private static Stream<Fold> parseFolds(Input in) {
        return in.lines()
            .map(line -> line.substring("fold along ".length()).split("="))
            .map(parts -> new Fold("x".equals(parts[0]) ? Axis.X : Axis.Y, Integer.parseInt(parts[1])));
    }
    
    private static void applyFold(Fold fold, Set<Dot> dots) {
        var moved = new ArrayList<Dot>();
        for (var iter = dots.iterator(); iter.hasNext();) {
            var dot = iter.next();
            if (fold.axis == Axis.X && dot.x > fold.unit) {
                iter.remove();
                moved.add(new Dot(2 * fold.unit - dot.x, dot.y));
            }
            if (fold.axis == Axis.Y && dot.y > fold.unit) {
                iter.remove();
                moved.add(new Dot(dot.x, 2 * fold.unit - dot.y));
            }
        }
        dots.addAll(moved);
    }
}
