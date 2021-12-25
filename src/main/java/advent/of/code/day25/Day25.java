package advent.of.code.day25;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.ArrayList;

public class Day25 {
    public static void part1(Input in, Output out) {
        // Parse input
        record Pos(int x, int y) {}
        var lines = in.lines().toList();
        var grid = new int[lines.size()][lines.get(0).length()];
        var eastFacing  = new ArrayList<Pos>();
        var southFacing = new ArrayList<Pos>();
        for (var i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            for (var j = 0; j < line.length(); j++) {
                switch (grid[i][j] = line.charAt(j)) {
                    case '>' -> eastFacing.add(new Pos(i, j));
                    case 'v' -> southFacing.add(new Pos(i, j));
                }
            }
        }
        // Solve
        var moved = new ArrayList<Pos>();
        for (var step = 1;; step++) {
            var didChange = false;
            // East-facing
            for (var iter = eastFacing.listIterator(); iter.hasNext();) {
                var pos = iter.next();
                var nextY = pos.y == grid[pos.x].length-1 ? 0 : pos.y+1;
                if (grid[pos.x][nextY] == '.') {
                    didChange = true;
                    iter.set(new Pos(pos.x, nextY));
                    moved.add(pos);
                }
            }
            // Update grid
            for (var pos : moved) {
                var nextY = pos.y == grid[pos.x].length-1 ? 0 : pos.y+1;
                grid[pos.x][pos.y] = '.';
                grid[pos.x][nextY] = '>';
            }
            moved.clear();
            // South-facing
            for (var iter = southFacing.listIterator(); iter.hasNext();) {
                var pos = iter.next();
                var nextX = pos.x == grid.length-1 ? 0 : pos.x+1;
                if (grid[nextX][pos.y] == '.') {
                    iter.set(new Pos(nextX, pos.y));
                    moved.add(pos);
                    didChange = true;
                }
            }
            // Update grid
            for (var pos : moved) {
                var nextX = pos.x == grid.length-1 ? 0 : pos.x+1;
                grid[pos.x][pos.y] = '.';
                grid[nextX][pos.y] = 'v';
            }
            moved.clear();
            // Check
            if (!didChange) {
                out.writeln(step);
                return;
            }
        }
    }
    
    public static void part2(Input in, Output out) {
        out.writeln("SLEIGH REMOTELY STARTED!");
    }
}
