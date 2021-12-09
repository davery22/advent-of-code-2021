package advent.of.code.day09;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Day09 {
    public static void part1(Input in, Output out) {
        var floor = parseFloor(in);
        var risk = IntStream.range(0, floor.length).parallel()
            .flatMap(i -> IntStream.range(0, floor[i].length)
                .filter(j -> isLowPoint(floor, i, j))
                .map(j -> floor[i][j] + 1))
            .sum();
        out.writeln(risk);
    }
    
    public static void part2(Input in, Output out) {
        var floor = parseFloor(in);
        var sizes= IntStream.range(0, floor.length) // parallel() is not safe for this one
            .flatMap(i -> IntStream.range(0, floor[i].length)
                .filter(j -> isLowPoint(floor, i, j)) // filter is technically unnecessary
                .map(j -> coalesceBasin(floor, i, j)))
            .sorted()
            .toArray();
        var product = Arrays.stream(sizes, sizes.length-3, sizes.length)
            .reduce(1, (p, size) -> p * size);
        out.writeln(product);
    }
    
    private static int[][] parseFloor(Input in) {
        return in.lines()
            .map(line -> line.chars().map(Character::getNumericValue).toArray())
            .toArray(int[][]::new);
    }
    
    private static boolean isLowPoint(int[][] floor, int i, int j) {
        return (i == 0                 || floor[i-1][j] > floor[i][j])
            && (i == floor.length-1    || floor[i+1][j] > floor[i][j])
            && (j == 0                 || floor[i][j-1] > floor[i][j])
            && (j == floor[i].length-1 || floor[i][j+1] > floor[i][j]);
    }
    
    private static int coalesceBasin(int[][] floor, int i, int j) {
        // Breadth-first traversal
        record Pos(int x, int y) {}
        var size = 0;
        var queue = new ArrayDeque<Pos>();
        queue.offer(new Pos(i, j));
        while (!queue.isEmpty()) {
            var curr = queue.poll();
            if (floor[curr.x][curr.y] == 9) continue;
            floor[curr.x][curr.y] = 9;
            size++;
            if (curr.x > 0)                      queue.offer(new Pos(curr.x-1, curr.y));
            if (curr.x < floor.length-1)         queue.offer(new Pos(curr.x+1, curr.y));
            if (curr.y > 0)                      queue.offer(new Pos(curr.x, curr.y-1));
            if (curr.y < floor[curr.x].length-1) queue.offer(new Pos(curr.x, curr.y+1));
        }
        return size;
    }
}
