package advent.of.code.day06;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.Arrays;

public class Day06 {
    public static void part1(Input in, Output out) {
        findPopulationOnDay(in, out, 80);
    }
    
    public static void part2(Input in, Output out) {
        findPopulationOnDay(in, out, 256);
    }
    
    private static void findPopulationOnDay(Input in, Output out, int day) {
        var counts = new long[9];
        Arrays.stream(in.readLine().split(","))
            .map(Integer::parseInt)
            .forEach(i -> counts[i]++);
        for (var i = 0; i < day; i++) {
            // There are arguably clearer alternatives (eg Map.put(), AtomicLongArray.getAndSet(), Collections.rotate()).
            // However, today we're performance kings. And temp jugglers. Kings and jesters.
            var temp1 = counts[8]; counts[8] = counts[0];
            var temp2 = counts[7]; counts[7] = temp1;
                temp1 = counts[6]; counts[6] = temp2 + counts[0];
                temp2 = counts[5]; counts[5] = temp1;
                temp1 = counts[4]; counts[4] = temp2;
                temp2 = counts[3]; counts[3] = temp1;
                temp1 = counts[2]; counts[2] = temp2;
                temp2 = counts[1]; counts[1] = temp1;
                                   counts[0] = temp2;
        }
        var totalCount = Arrays.stream(counts).sum();
        out.writeln(totalCount);
    }
}
