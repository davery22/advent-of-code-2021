package advent.of.code.day21;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.HashMap;
import java.util.Map;

public class Day21 {
    public static void part1(Input in, Output out) {
        var pos1 = readStartPos(in);
        var pos2 = readStartPos(in);
        int score1 = 0, score2 = 0, die = 1, rolls = 0;
        var isP1Turn = true;
        for (;;) {
            var dieSum = 0;
            for (var i = 0; i < 3; i++) {
                rolls++;
                dieSum += die;
                die = (die == 100) ? 1 : die + 1;
            }
            if (isP1Turn) {
                pos1 = ((pos1 + dieSum - 1) % 10) + 1;
                score1 += pos1;
                if (score1 >= 1000) { out.writeln(score2 * rolls); return; }
            } else {
                pos2 = ((pos2 + dieSum - 1) % 10) + 1;
                score2 += pos2;
                if (score2 >= 1000) { out.writeln(score1 * rolls); return; }
            }
            isP1Turn = !isP1Turn;
        }
    }
    
    public static void part2(Input in, Output out) {
        // Idea: Memoization.
        // The actual number of unique states {posA, posB, scoreA, scoreB} is fairly limited (roughly, 10*10*30*30 = 90000).
        // If we save off results as we encounter new states, we can short-circuit when those states come up again.
        var pos1 = readStartPos(in);
        var pos2 = readStartPos(in);
        var seenStates = new HashMap<State, Results>();
        var results = simulate(new State(pos1, pos2, 0, 0), seenStates);
        out.writeln(Math.max(results.winsA, results.winsB));
    }
    
    private static int readStartPos(Input in) {
        return Integer.parseInt(in.readLine().substring("Player x starting position: ".length()));
    }
    
    private record State(int posA, int posB, int scoreA, int scoreB) {}
    private record Results(long winsA, long winsB) {}
    
    private static Results simulate(State state, Map<State, Results> seenStates) {
        var seenResults = seenStates.get(state);
        if (seenResults != null) return seenResults;
        long winsA = 0, winsB = 0;
        for (var dieSum = 3; dieSum <= 9; dieSum++) {
            var universes = universesForDieSum(dieSum);
            var newPosA = ((state.posA + dieSum - 1) % 10) + 1;
            var newScoreA = state.scoreA + newPosA;
            if (newScoreA >= 21) {
                winsA += universes;
            } else {
                var results = simulate(new State(state.posB, newPosA, state.scoreB, newScoreA), seenStates);
                winsA += universes * results.winsB;
                winsB += universes * results.winsA;
            }
        }
        var results = new Results(winsA, winsB);
        seenStates.put(state, results);
        return results;
    }
    
    private static int universesForDieSum(int dieSum) {
        return switch (dieSum) {
            case 3,9 -> 1;
            case 4,8 -> 3;
            case 5,7 -> 6;
            case 6   -> 7;
            default -> throw new AssertionError();
        };
    }
}
