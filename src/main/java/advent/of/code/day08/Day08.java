package advent.of.code.day08;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day08 {
    public static void part1(Input in, Output out) {
        var count = in.lines().map(line -> line.split(Pattern.quote(" | ")))
            .mapToLong(io -> Arrays.stream(io[1].split(" "))
                .filter(o -> o.length() == 2 || o.length() == 3 || o.length() == 4 || o.length() == 7)
                .count())
            .sum();
        out.writeln(count);
    }
    
    // Part 2 notes:
    //
    //  aaaa
    // b    c
    // b    c
    //  dddd
    // e    f
    // e    f
    //  gggg
    //
    // display number | segments | number of segments | overlapped display numbers
    // 0 | abc_efg | 6 | 01_____7__
    // 1 | __c__f_ | 2 | _1________
    // 2 | a_cdef_ | 5 | __2_______
    // 3 | a_cd_fg | 5 | _1_3___7__
    // 4 | _bcd_f_ | 4 | _1__4_____
    // 5 | ab_d_fg | 5 | _____5____
    // 6 | ab_defg | 6 | _____56___
    // 7 | a_c__f_ | 3 | _1_____7__
    // 8 | abcdefg | 7 | 0123456789
    // 9 | abcd_fg | 6 | _1_345_7_9
    //
    // segment | display numbers | number of display numbers
    // a | 0_23_56789 |  8
    // b | 0___456_89 |  6
    // c | 01234__789 |  8
    // d | __23456_89 |  7
    // e | 0_2___6_8_ |  4
    // f | 0123456789 | 10
    // g | 0__3_56_89 |  6
    //
    // number of segments | display numbers with that number of segments
    // 2 | 1
    // 3 | 7
    // 4 | 4
    // 5 | 2, 3, 5
    // 6 | 0, 6, 9
    // 7 | 8
    //
    // Idea:
    // 1. Determine segments for 1, 7, 4, 8
    // 2. Use number of segments + fact that some display numbers overlap to deduce segments for other display numbers.
    //
    // Important note:
    // Ten UNIQUE signal patterns!
    
    public static void part2(Input in, Output out) {
        var count = in.lines().map(line -> line.split(Pattern.quote(" | ")))
            .mapToLong(io -> {
                @SuppressWarnings("unchecked") // It ok trus me
                var segs = (Set<Integer>[]) new Set[10];
                var inputs  = io[0].split(" ");
                var outputs = io[1].split(" ");
                var patternsBySegCount = Arrays.stream(inputs)
                    .map(Day08::toCharSet)
                    .collect(Collectors.groupingBy(Collection::size));
                segs[1] = patternsBySegCount.get(2).get(0);
                segs[7] = patternsBySegCount.get(3).get(0);
                segs[4] = patternsBySegCount.get(4).get(0);
                segs[8] = patternsBySegCount.get(7).get(0);
                for (var pattern : patternsBySegCount.get(6)) { // 0, 6, 9
                    if     (!pattern.containsAll(segs[1])) segs[6] = pattern; // 0 and 9 overlap 1; 6 does not
                }
                for (var pattern : patternsBySegCount.get(5)) { // 2, 3, 5
                    if      (pattern.containsAll(segs[1])) segs[3] = pattern; // 3 overlaps 1; 2 and 5 do not
                    else if (segs[6].containsAll(pattern)) segs[5] = pattern; // 5 is overlapped by 6; 2 is not
                    else                                   segs[2] = pattern;
                }
                for (var pattern : patternsBySegCount.get(6)) { // 0, 6, 9
                    if      (pattern.equals(segs[6]))      ;                  // We already have 6
                    else if (pattern.containsAll(segs[5])) segs[9] = pattern; // 9 overlaps 5; 0 does not
                    else                                   segs[0] = pattern;
                }
                var outputStr = Arrays.stream(outputs)
                    .map(Day08::toCharSet)
                    .map(pattern -> IntStream.rangeClosed(0, 9)
                        .filter(i -> pattern.equals(segs[i]))
                        .findFirst()
                        .orElseThrow())
                    .map(String::valueOf)
                    .collect(Collectors.joining());
                return Integer.parseInt(outputStr);
            })
            .sum();
        out.writeln(count);
    }
    
    private static Set<Integer> toCharSet(String s) {
        return s.chars().boxed().collect(Collectors.toSet());
    }
}
