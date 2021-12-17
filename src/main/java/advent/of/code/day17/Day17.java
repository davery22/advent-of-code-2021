package advent.of.code.day17;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day17 {
    // My original submission was sketchier. I went back and math'd things up to be more efficient.
    // Assumption: The target is ahead of us (target.x0 is positive). This is for implementation convenience.
    // Assumption: The target is below us (target.y1 is negative). This is for part 1 optimization.
    
    public static void part1(Input in, Output out) {
        var target = parseArea(in);
        var bounds = getBounds(target);
        var xs     = getXStats(target, bounds);
        for (var dy0 = bounds.maxdy0; dy0 > 0; dy0--) {
            // If we fire upwards, we will always return to y=0 at a later time step.
            // We use this to skip some steps by starting at y=0 on the way down.
            int y = 0, dy = -dy0-1, t = dy0*2+1;
            while (target.y0 < y) {
                y += dy; dy--; t++;
                if (target.y0 <= y && y <= target.y1 && xs.inRangeAtTime(t).findAny().isPresent()) {
                    out.writeln(gauss(dy0));
                    return;
                }
            }
        }
        out.writeln(0);
    }
    
    public static void part2(Input in, Output out) {
        var target = parseArea(in);
        var bounds = getBounds(target);
        var xs     = getXStats(target, bounds);
        var count = 0;
        for (var dy0 = bounds.mindy0; dy0 <= bounds.maxdy0; dy0++) {
            var xseen = new HashSet<Integer>();
            int y = 0, dy = dy0, t = 0;
            while (target.y0 < y) {
                y += dy; dy--; t++;
                if (target.y0 <= y && y <= target.y1) xs.inRangeAtTime(t).forEach(xseen::add);
            }
            count += xseen.size();
        }
        out.writeln(count);
    }
    
    private record Area(int x0, int x1, int y0, int y1) {}
    private record Bounds(int mindx0, int maxdx0, int mindy0, int maxdy0) {}
    private record XStats(Map<Integer, Set<Integer>> xpassed, TreeMap<Integer, Set<Integer>> xstopped) {
        Stream<Integer> inRangeAtTime(int time) {
            return Stream.concat(
                xpassed.getOrDefault(time, Set.of()).stream(),
                xstopped.headMap(time+1).values().stream().flatMap(Set::stream)
            );
        }
    }
    
    private static Area parseArea(Input in) {
        var arr = Pattern.compile("-?\\d+").matcher(in.readLine()).results()
            .mapToInt(m -> Integer.parseInt(m.group()))
            .toArray();
        return new Area(arr[0], arr[1], arr[2], arr[3]);
    }
    
    private static Bounds getBounds(Area target) {
        return new Bounds(invgauss(target.x0), target.x1, target.y0, -target.y0-1);
    }
    
    private static XStats getXStats(Area target, Bounds bounds) {
        var xpassed  = new HashMap<Integer, Set<Integer>>();
        var xstopped = new TreeMap<Integer, Set<Integer>>();
        for (var dx0 = bounds.mindx0; dx0 <= bounds.maxdx0; dx0++) {
            int x = 0, dx = dx0, t = 0;
            while (target.x1 > x) {
                x += dx; dx--; t++;
                if (target.x0 <= x && x <= target.x1)
                    (dx == 0 ? xstopped : xpassed).computeIfAbsent(t, k -> new HashSet<>()).add(dx0);
                if (dx == 0) break;
            }
        }
        return new XStats(xpassed, xstopped);
    }
    
    private static int gauss(int n) {
        // y = n(n+1)/2 [Guass's Formula]
        return n*(n+1)/2;
    }
    
    private static int invgauss(int n) {
        // y = n(n+1)/2 [Guass's Formula]
        // n^2/2 + n/2 - y = 0
        // n = -1/2 +- sqrt(1/4 + 2y) [Quadratic Formula]
        return (int) Math.ceil(Math.sqrt(0.25 + 2*n) - 0.5);
    }
}
