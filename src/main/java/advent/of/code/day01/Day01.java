package advent.of.code.day01;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.io.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Day01 {
    public static void part1(Input in, Output out) throws IOException {
        var depths = in.lines().mapToInt(Integer::parseInt).toArray();
        var count = IntStream.range(1, depths.length)
            .map(i -> depths[i] > depths[i-1] ? 1 : 0)
            .parallel()
            .sum();
        out.writeln(count);
    }
    
    public static void part2(Input in, Output out) throws IOException {
        var depths = in.lines().mapToInt(Integer::parseInt).toArray();
        var count = IntStream.range(3, depths.length)
            .map(i -> {
                int prev = Arrays.stream(depths, i-3, i).sum();
                int curr = Arrays.stream(depths, i-2, i+1).sum();
                return curr > prev ? 1 : 0;
            })
            .parallel()
            .sum();
        out.writeln(count);
    }
}
