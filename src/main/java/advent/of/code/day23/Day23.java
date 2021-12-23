package advent.of.code.day23;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day23 {
    public static void part1(Input in, Output out) {
        var lines = in.lines().toList();
        solve(lines, out);
    }
    
    public static void part2(Input in, Output out) {
        // LOL, simulation finishes MUCH faster in part 2, because many paths dead-end sooner :D
        var lines = in.lines().collect(Collectors.toList());
        lines.addAll(3, List.of(
            "  #D#C#B#A#",
            "  #D#B#A#C#"
        ));
        solve(lines, out);
    }
    
    private static void solve(List<String> lines, Output out) {
        var grid = new char[lines.size()][lines.get(0).length()];
        for (var i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            for (var j = 0; j < grid[0].length; j++) {
                grid[i][j] = j >= line.length() ? ' ' : line.charAt(j);
            }
        }
        var minEnergy = simulate(grid, 0L, Long.MAX_VALUE);
        out.writeln(minEnergy);
    }
    
    private static long simulate(char[][] grid, long energyUsedSoFar, long bound) {
        var moves = getLegalMoves(grid);
        if (moves == null) { // Done
//            System.out.println(energyUsedSoFar); // Uncomment to see progress
            return energyUsedSoFar;
        }
        for (var move : moves) {
            var amphipod = grid[move.p1.x][move.p1.y];
            var nextEnergy = energyUsedSoFar + move.p1.manhattanDist(move.p2) * energyToMove(amphipod);
            if (nextEnergy >= bound) continue;
            grid[move.p1.x][move.p1.y] = '.';
            grid[move.p2.x][move.p2.y] = amphipod;
            bound = Math.min(bound, simulate(grid, nextEnergy, bound));
            grid[move.p2.x][move.p2.y] = '.';
            grid[move.p1.x][move.p1.y] = amphipod;
        }
        return bound;
    }
    
    private static List<Move> getLegalMoves(char[][] grid) {
        // Setup metadata.
        int roomRear = grid.length-1, roomAFront = roomRear, roomBFront = roomRear, roomCFront = roomRear, roomDFront = roomRear;
        boolean                       roomAAvail = true,     roomBAvail = true,     roomCAvail = true,     roomDAvail = true;
        for (var x = roomRear-1; x > 1; x--) {
            var tA = grid[x][3]; roomAAvail &= tA == 'A' || tA == '.'; if (tA != '.') roomAFront--;
            var tB = grid[x][5]; roomBAvail &= tB == 'B' || tB == '.'; if (tB != '.') roomBFront--;
            var tC = grid[x][7]; roomCAvail &= tC == 'C' || tC == '.'; if (tC != '.') roomCFront--;
            var tD = grid[x][9]; roomDAvail &= tD == 'D' || tD == '.'; if (tD != '.') roomDFront--;
        }
        if (   roomAAvail      && roomBAvail      && roomCAvail      && roomDAvail
            && roomAFront == 2 && roomBFront == 2 && roomCFront == 2 && roomDFront == 2
        ) {
            return null; // Special value meaning 'done'
        }
        // See if amphipods in the hall can move into their destination room yet.
        var moves = new ArrayList<Move>();
        for (var y = 1; y <= 11; y++) {
            // Destination room must be available.
            int roomY = -1, roomFront = -1;
            switch (grid[1][y]) {
                case '.' -> { continue; }
                case 'A' -> { if (!roomAAvail) continue; roomY = 3; roomFront = roomAFront; }
                case 'B' -> { if (!roomBAvail) continue; roomY = 5; roomFront = roomBFront; }
                case 'C' -> { if (!roomCAvail) continue; roomY = 7; roomFront = roomCFront; }
                case 'D' -> { if (!roomDAvail) continue; roomY = 9; roomFront = roomDFront; }
            }
            // Path to room must be clear.
            var dy = roomY < y ? -1 : 1;
            for (var j = y+dy;;j+=dy) {
                if (grid[1][j] != '.') break;
                if (j == roomY) {
                    moves.add(new Move(new Pos(1, y), new Pos(roomFront-1, roomY)));
                    break;
                }
            }
        }
        // See if amphipods at the front of their room can move into the hall.
        if (!roomAAvail) addExitRoomMoves(new Pos(roomAFront, 3), grid, moves);
        if (!roomBAvail) addExitRoomMoves(new Pos(roomBFront, 5), grid, moves);
        if (!roomCAvail) addExitRoomMoves(new Pos(roomCFront, 7), grid, moves);
        if (!roomDAvail) addExitRoomMoves(new Pos(roomDFront, 9), grid, moves);
        return moves;
    }
    
    private static void addExitRoomMoves(Pos start, char[][] grid, List<Move> moves) {
        for (var y = start.y+1; y <= 11; y++) {
            if (y == 3 || y == 5 || y == 7 || y == 9) continue;
            if (grid[1][y] != '.') break;
            moves.add(new Move(start, new Pos(1, y)));
        }
        for (var y = start.y-1; y >= 1; y--) {
            if (y == 3 || y == 5 || y == 7 || y == 9) continue;
            if (grid[1][y] != '.') break;
            moves.add(new Move(start, new Pos(1, y)));
        }
    }
    
    private record Move(Pos p1, Pos p2) {}
    private record Pos(int x, int y) {
        long manhattanDist(Pos o) { return Math.abs(x-o.x) + Math.abs(y-o.y); }
    }
    
    private static long energyToMove(char amphipodType) {
        return switch (amphipodType) {
            case 'A' -> 1;
            case 'B' -> 10;
            case 'C' -> 100;
            case 'D' -> 1000;
            default -> throw new AssertionError();
        };
    }
}
