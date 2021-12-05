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
        var matrix = initMatrix(1000, 1000);
        in.lines().parallel()
            .map(Day05::parseLine)
            .forEach(line -> {
                if (line.x1 == line.x2) {
                    drawVertical(overlap, matrix, line);
                } else if (line.y1 == line.y2) {
                    drawHorizontal(overlap, matrix, line);
                }
            });
        out.writeln(overlap.get());
    }
    
    public static void part2(Input in, Output out) {
        var overlap = new AtomicInteger(0);
        var matrix = initMatrix(1000, 1000);
        in.lines().parallel()
            .map(Day05::parseLine)
            .forEach(line -> {
                if (line.x1 == line.x2) {
                    drawVertical(overlap, matrix, line);
                } else if (line.y1 == line.y2) {
                    drawHorizontal(overlap, matrix, line);
                } else {
                    drawDiagonal(overlap, matrix, line);
                }
            });
        out.writeln(overlap.get());
    }
    
    private static record Line(int x1, int y1, int x2, int y2) {
        Line reverse() { return new Line(x2, y2, x1, y1); }
    }
    
    private static AtomicIntegerArray[] initMatrix(int x, int y) {
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
    
    private static void drawVertical(AtomicInteger overlap, AtomicIntegerArray[] matrix, Line line) {
        if (line.y1 > line.y2)
            line = line.reverse();
        for (var i = 0; line.y1+i <= line.y2; i++)
            if (matrix[line.x1].getAndIncrement(line.y1+i) == 1)
                overlap.getAndIncrement();
    }
    
    private static void drawHorizontal(AtomicInteger overlap, AtomicIntegerArray[] matrix, Line line) {
        if (line.x1 > line.x2)
            line = line.reverse();
        for (var i = 0; line.x1+i <= line.x2; i++)
            if (matrix[line.x1+i].getAndIncrement(line.y1) == 1)
                overlap.getAndIncrement();
    }
    
    private static void drawDiagonal(AtomicInteger overlap, AtomicIntegerArray[] matrix, Line line) {
        if (line.x1 > line.x2)
            line = line.reverse();
        if (line.y1 < line.y2) { // Looks like '/'
            for (var i = 0; line.x1+i <= line.x2; i++)
                if (matrix[line.x1+i].getAndIncrement(line.y1+i) == 1)
                    overlap.getAndIncrement();
        } else { // Looks like '\'
            for (var i = 0; line.x1+i <= line.x2; i++)
                if (matrix[line.x1+i].getAndIncrement(line.y1-i) == 1)
                    overlap.getAndIncrement();
        }
    }
}
