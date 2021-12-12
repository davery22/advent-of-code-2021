package advent.of.code.day03;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day03 {
    public static void part1(Input in, Output out) {
        var balances = new int[12];
        in.lines().forEach(binary -> {
            for (var i = 0; i < 12; i++) {
                balances[i] += binary.charAt(i) == '0' ? -1 : 1;
            }
        });
        var powerConsumption = Arrays.stream(balances).boxed()
            .collect(Collectors.teeing(
                Collectors.mapping(balance -> balance > 0 ? "1" : "0", Collectors.joining()),
                Collectors.mapping(balance -> balance > 0 ? "0" : "1", Collectors.joining()),
                (gammaRateBinary, epsilonRateBinary) -> {
                    var gammaRate   = Integer.parseInt(gammaRateBinary, 2);
                    var epsilonRate = Integer.parseInt(epsilonRateBinary, 2);
                    return gammaRate * epsilonRate;
                }
            ));
        out.writeln(powerConsumption);
    }
    
    public static void part2(Input in, Output out) {
        var lines = in.lines().toList();
        var oxygenRating      = getRating(lines, '1', '0');
        var co2ScrubberRating = getRating(lines, '0', '1');
        var lifeSupportRating = oxygenRating * co2ScrubberRating;
        out.writeln(lifeSupportRating);
    }
    
    private static int getRating(List<String> lines, char hi, char lo) {
        var curr = new ArrayList<>(lines);
        for (var i = 0;; i++) {
            var j = i;
            var balance = curr.stream().mapToInt(binary -> binary.charAt(j) == '0' ? -1 : 1).sum();
            var keep = balance >= 0 ? hi : lo;
            curr.removeIf(binary -> binary.charAt(j) != keep);
            if (curr.size() == 1) {
                return Integer.parseInt(curr.get(0), 2);
            }
        }
    }
}
