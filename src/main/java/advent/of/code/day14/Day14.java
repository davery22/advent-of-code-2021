package advent.of.code.day14;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 {
    public static void part1(Input in, Output out) {
        solve(in, out, 10);
    }
    
    public static void part2(Input in, Output out) {
        solve(in, out, 40);
    }
    
    // Key insights:
    // Our result does not require being able to construct the final string. We can work on pairs independently.
    // Each insertion replaces a pair with two new pairs.
    // We can aggregate counts for each distinct pair and apply replacements in bulk.
    // Each char will be captured in exactly two pairs, except for the first and last char of the original template.
    
    private static void solve(Input in, Output out, int steps) {
        var template = in.readLine();
        var rules = in.lines().map(line -> line.split(" -> "))
            .collect(Collectors.toMap(p -> p[0], p -> p[1]));
        var pairCounts = IntStream.range(0, template.length()-1)
            .mapToObj(i -> template.substring(i, i+2))
            .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        for (var step = 0; step < steps; step++) {
            var upcoming = new HashMap<String, Long>();
            rules.forEach((pair, insert) -> {
                var count = pairCounts.remove(pair);
                if (count != null) {
                    upcoming.merge(pair.charAt(0) + insert, count, Long::sum);
                    upcoming.merge(insert + pair.charAt(1), count, Long::sum);
                }
            });
            upcoming.forEach((pair, count) -> pairCounts.merge(pair, count, Long::sum));
        }
        var charCounts = new HashMap<Character, Long>();
        pairCounts.forEach((pair, count) -> {
            charCounts.merge(pair.charAt(0), count, Long::sum);
            charCounts.merge(pair.charAt(1), count, Long::sum);
        });
        charCounts.replaceAll((ch, count) -> count / 2); // Fix double-counting
        charCounts.merge(template.charAt(0),                   1L, Long::sum);
        charCounts.merge(template.charAt(template.length()-1), 1L, Long::sum);
        var stats = charCounts.values().stream().mapToLong(i -> i).summaryStatistics();
        out.writeln(stats.getMax() - stats.getMin());
    }
}
