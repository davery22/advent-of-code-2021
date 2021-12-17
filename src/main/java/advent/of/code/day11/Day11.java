package advent.of.code.day11;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.IntStream;

public class Day11 {
    public static void part1(Input in, Output out) {
        var grid = parseGrid(in);
        var queue = new ArrayDeque<Pos>();
        long[] flashes = { 0 };
        IntStream.range(0, 100).forEach(step -> {
            IntStream.range(0, 10).forEach(i -> IntStream.range(0, 10).forEach(j -> {
                queue.push(new Pos(i, j));
                while (!queue.isEmpty()) {
                    var pos = queue.pop();
                    if (++grid[pos.x][pos.y] != 10) continue;
                    offerNeighbors(pos, queue);
                    flashes[0]++;
                }
            }));
            IntStream.range(0, 10).forEach(i -> IntStream.range(0, 10).forEach(j -> {
                if (grid[i][j] >= 10) grid[i][j] = 0;
            }));
        });
        out.writeln(flashes[0]);
    }
    
    public static void part2(Input in, Output out) {
        var grid = parseGrid(in);
        var queue = new ArrayDeque<Pos>();
        var flashyStep = IntStream.iterate(1, step -> step + 1).filter(step -> {
            IntStream.range(0, 10).forEach(i -> IntStream.range(0, 10).forEach(j -> {
                queue.push(new Pos(i, j));
                while (!queue.isEmpty()) {
                    var pos = queue.pop();
                    if (++grid[pos.x][pos.y] != 10) continue;
                    offerNeighbors(pos, queue);
                }
            }));
            boolean[] allFlashed = { true };
            IntStream.range(0, 10).forEach(i -> IntStream.range(0, 10).forEach(j -> {
                if (grid[i][j] >= 10) grid[i][j] = 0;
                allFlashed[0] &= grid[i][j] == 0;
            }));
            return allFlashed[0];
        }).findFirst().getAsInt();
        out.writeln(flashyStep);
    }
    
    private record Pos(int x, int y) {}
    
    private static int[][] parseGrid(Input in) {
        return in.lines()
            .map(line -> line.chars().map(Character::getNumericValue).toArray())
            .toArray(int[][]::new);
    }
    
    private static void offerNeighbors(Pos pos, Queue<Pos> queue) {
        if (pos.x > 0 && pos.y > 0) queue.offer(new Pos(pos.x-1, pos.y-1));
        if (pos.x > 0 && true     ) queue.offer(new Pos(pos.x-1, pos.y  ));
        if (pos.x > 0 && pos.y < 9) queue.offer(new Pos(pos.x-1, pos.y+1));
        if (true      && pos.y < 9) queue.offer(new Pos(pos.x,   pos.y+1));
        if (pos.x < 9 && pos.y < 9) queue.offer(new Pos(pos.x+1, pos.y+1));
        if (pos.x < 9 && true     ) queue.offer(new Pos(pos.x+1, pos.y  ));
        if (pos.x < 9 && pos.y > 0) queue.offer(new Pos(pos.x+1, pos.y-1));
        if (true      && pos.y > 0) queue.offer(new Pos(pos.x,   pos.y-1));
    }
}
