package advent.of.code.day05;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

public class Day05 {
    // For added Christmas cheer, I "parallelized" this one.
    // (It has no effect unless the lines() Stream is splittable. Which it currently isn't :P )
    
    public static void part1(Input in, Output out) {
        var overlap = new AtomicInteger(0);
        var grid = initGrid(1000, 1000);
        in.lines().parallel()
            .map(Day05::parseLine)
            .forEach(line -> {
                var dx = Integer.compare(line.x2, line.x1);
                var dy = Integer.compare(line.y2, line.y1);
                if (dx != 0 && dy != 0) return;
                var x = line.x1; var y = line.y1;
                while (true) {
                    if (grid[x].getAndIncrement(y) == 1) overlap.getAndIncrement();
                    if (x == line.x2 && y == line.y2) break;
                    x += dx; y += dy;
                }
            });
        out.writeln(overlap.get());
    }
    
    public static void part2(Input in, Output out) {
        // part 2 is the same except simpler
        var overlap = new AtomicInteger(0);
        var grid = initGrid(1000, 1000);
        in.lines().parallel()
            .map(Day05::parseLine)
            .forEach(line -> {
                var dx = Integer.compare(line.x2, line.x1);
                var dy = Integer.compare(line.y2, line.y1);
                var x = line.x1; var y = line.y1;
                while (true) {
                    if (grid[x].getAndIncrement(y) == 1) overlap.getAndIncrement();
                    if (x == line.x2 && y == line.y2) break;
                    x += dx; y += dy;
                }
            });
        out.writeln(overlap.get());
    }
    
    private record Line(int x1, int y1, int x2, int y2) {}
    
    private static AtomicIntegerArray[] initGrid(int x, int y) {
        return IntStream.range(0, x)
            .mapToObj(i -> new AtomicIntegerArray(y))
            .toArray(AtomicIntegerArray[]::new);
    }
    
    private static Line parseLine(String line) {
        var arr = Arrays.stream(line.split(" -> "))
            .flatMapToInt(point -> Arrays.stream(point.split(",")).mapToInt(Integer::parseInt))
            .toArray();
        return new Line(arr[0], arr[1], arr[2], arr[3]);
    }
}
