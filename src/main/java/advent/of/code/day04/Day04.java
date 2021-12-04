package advent.of.code.day04;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class Day04 {
    // This solution makes destructive updates to the boards using a special value (Integer.MAX_VALUE).
    // But we got away with it... phew.
    
    public static void part1(Input in, Output out) throws IOException {
        var draws = parseDraws(in);
        var boards = parseBoards(in);
        for (var draw : draws) {
            for (var board : boards) {
                mark(board, draw);
                if (check(board)) {
                    out.writeln(getScore(board, draw));
                    return;
                }
            }
        }
    }
    
    public static void part2(Input in, Output out) throws IOException {
        var draws = parseDraws(in);
        var boards = parseBoards(in);
        var winners = new HashSet<Integer>();
        var scores = new ArrayList<Integer>();
        for (var draw : draws) {
            for (int i = 0; i < boards.size(); i++) {
                var board = boards.get(i);
                mark(board, draw);
                if (!winners.contains(i) && check(board)) {
                    winners.add(i);
                    scores.add(getScore(board, draw));
                }
            }
        }
        out.writeln(scores.get(scores.size()-1));
    }
    
    private static int[] parseDraws(Input in) throws IOException {
        return Arrays.stream(in.readLine().split(","))
            .mapToInt(Integer::parseInt)
            .toArray();
    }
    
    private static List<int[][]> parseBoards(Input in) throws IOException {
        var boards = new ArrayList<int[][]>();
        while (in.readLine() != null) { // Discard blank lines
            var board = in.lines().limit(5)
                .map(line -> Arrays.stream(line.trim().split("\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray())
                .toArray(int[][]::new);
            boards.add(board);
        }
        return boards;
    }
    
    private static void mark(int[][] board, int draw) {
        IntStream.range(0, 5).forEach(i -> IntStream.range(0, 5).forEach(j -> {
            if (board[i][j] == draw) {
                board[i][j] = Integer.MAX_VALUE;
            }
        }));
    }
    
    private static boolean check(int[][] board) {
        // Rows or columns. Diagonals don't count!
        return IntStream.range(0, 5)
            .anyMatch(i -> IntStream.range(0, 5).allMatch(j -> board[i][j] == Integer.MAX_VALUE)
                        || IntStream.range(0, 5).allMatch(j -> board[j][i] == Integer.MAX_VALUE));
    }
    
    private static int getScore(int[][] board, int winningDraw) {
        int sumUnmarked = Arrays.stream(board)
            .flatMapToInt(Arrays::stream)
            .filter(v -> v != Integer.MAX_VALUE)
            .sum();
        return sumUnmarked * winningDraw;
    }
}
