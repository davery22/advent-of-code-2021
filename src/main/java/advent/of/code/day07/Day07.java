package advent.of.code.day07;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.Arrays;
import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;

public class Day07 {
    public static void part1(Input in, Output out) {
        solve(in, out, (pos, target) -> Math.abs(pos - target));
    }
    
    public static void part2(Input in, Output out) {
        solve(in, out, (pos, target) -> {
            // Talk to Gauss, folks.
            var n = Math.abs(pos - target);
            return n * (n + 1) / 2;
        });
    }
    
    private static void solve(Input in, Output out, IntBinaryOperator costFn) {
        var positions = Arrays.stream(in.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
        var minPos = Arrays.stream(positions).min().getAsInt();
        var maxPos = Arrays.stream(positions).max().getAsInt();
        var minCost = IntStream.rangeClosed(minPos, maxPos).parallel()
            .map(target -> Arrays.stream(positions).map(pos -> costFn.applyAsInt(pos, target)).sum())
            .min().getAsInt();
        out.writeln(minCost);
    }
}
