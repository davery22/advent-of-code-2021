package advent.of.code.day15;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.*;
import java.util.stream.IntStream;

public class Day15 {
    public static void part1(Input in, Output out) {
        var grid = parseGrid(in);
        solve(grid, out);
    }
    
    public static void part2(Input in, Output out) {
        var grid0 = parseGrid(in);
        var grid = new int[grid0.length*5][grid0[0].length*5];
        for (var i = 0; i < 5; i++) {
            for (var j = 0; j < 5; j++) {
                for (var x = 0; x < grid0.length; x++) {
                    for (var y = 0; y < grid0[x].length; y++) {
                        var risk = (grid0[x][y] + i + j - 1) % 9 + 1;
                        grid[i * grid0.length + x][j * grid0[x].length + y] = risk;
                    }
                }
            }
        }
        solve(grid, out);
    }
    
    private static int[][] parseGrid(Input in) {
        return in.lines()
            .map(line -> line.chars().map(Character::getNumericValue).toArray())
            .toArray(int[][]::new);
    }
    
    private interface IntBiConsumer {
        void accept(int i, int j);
    }
    
    private record Node(int x, int y, long totalRisk) {
        void forEachAdjacent(int xdim, int ydim, IntBiConsumer action) {
            if (x > 0)      action.accept(x-1, y);
            if (x < xdim-1) action.accept(x+1, y);
            if (y > 0)      action.accept(x, y-1);
            if (y < ydim-1) action.accept(x, y+1);
        }
    }
    
    private static void solve(int[][] risk, Output out) {
        // Dijkstra's algo
        var totalRisk = Arrays.stream(risk)
            .map(row -> IntStream.range(0, row.length).mapToLong(i -> Long.MAX_VALUE).toArray())
            .toArray(long[][]::new);
        totalRisk[0][0] = 0;
        var queue = new PriorityQueue<>(Comparator.comparingLong(Node::totalRisk));
        queue.offer(new Node(0, 0, 0));
        while (!queue.isEmpty()) {
            var curr = queue.poll();
            if (curr.x == risk.length-1 && curr.y == risk[curr.x].length-1) {
                out.writeln(totalRisk[curr.x][curr.y]);
                return;
            }
            curr.forEachAdjacent(totalRisk.length, totalRisk[curr.x].length, (x, y) -> {
                var altRisk = totalRisk[curr.x][curr.y] + risk[x][y];
                if (altRisk < totalRisk[x][y]) {
                    totalRisk[x][y] = altRisk;
                    queue.offer(new Node(x, y, altRisk));
                }
            });
        }
    }
}
