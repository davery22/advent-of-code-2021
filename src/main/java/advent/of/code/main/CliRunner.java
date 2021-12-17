package advent.of.code.main;

import advent.of.code.day01.Day01;
import advent.of.code.day02.Day02;
import advent.of.code.day03.Day03;
import advent.of.code.day04.Day04;
import advent.of.code.day05.Day05;
import advent.of.code.day06.Day06;
import advent.of.code.day07.Day07;
import advent.of.code.day08.Day08;
import advent.of.code.day09.Day09;
import advent.of.code.day10.Day10;
import advent.of.code.day11.Day11;
import advent.of.code.day12.Day12;
import advent.of.code.day13.Day13;
import advent.of.code.day14.Day14;
import advent.of.code.day15.Day15;
import advent.of.code.day16.Day16;
import advent.of.code.day17.Day17;
import advent.of.code.day18.Day18;
import advent.of.code.day19.Day19;
import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.io.IOException;
import java.util.Arrays;

public class CliRunner {
    public void run() {
        var out = Output.of(System.out);
//        try (var in = Input.of(Day01.class, "input.txt")) { Day01.part1(in, out); }
//        try (var in = Input.of(Day01.class, "input.txt")) { Day01.part2(in, out); }
//        try (var in = Input.of(Day02.class, "input.txt")) { Day02.part1(in, out); }
//        try (var in = Input.of(Day02.class, "input.txt")) { Day02.part2(in, out); }
//        try (var in = Input.of(Day03.class, "input.txt")) { Day03.part1(in, out); }
//        try (var in = Input.of(Day03.class, "input.txt")) { Day03.part2(in, out); }
//        try (var in = Input.of(Day04.class, "input.txt")) { Day04.part1(in, out); }
//        try (var in = Input.of(Day04.class, "input.txt")) { Day04.part2(in, out); }
//        try (var in = Input.of(Day05.class, "input.txt")) { Day05.part1(in, out); }
//        try (var in = Input.of(Day05.class, "input.txt")) { Day05.part2(in, out); }
//        try (var in = Input.of(Day06.class, "input.txt")) { Day06.part1(in, out); }
//        try (var in = Input.of(Day06.class, "input.txt")) { Day06.part2(in, out); }
//        try (var in = Input.of(Day07.class, "input.txt")) { Day07.part1(in, out); }
//        try (var in = Input.of(Day07.class, "input.txt")) { Day07.part2(in, out); }
//        try (var in = Input.of(Day08.class, "input.txt")) { Day08.part1(in, out); }
//        try (var in = Input.of(Day08.class, "input.txt")) { Day08.part2(in, out); }
//        try (var in = Input.of(Day09.class, "input.txt")) { Day09.part1(in, out); }
//        try (var in = Input.of(Day09.class, "input.txt")) { Day09.part2(in, out); }
//        try (var in = Input.of(Day10.class, "input.txt")) { Day10.part1(in, out); }
//        try (var in = Input.of(Day10.class, "input.txt")) { Day10.part2(in, out); }
//        try (var in = Input.of(Day11.class, "input.txt")) { Day11.part1(in, out); }
//        try (var in = Input.of(Day11.class, "input.txt")) { Day11.part2(in, out); }
//        try (var in = Input.of(Day12.class, "input.txt")) { Day12.part1(in, out); }
//        try (var in = Input.of(Day12.class, "input.txt")) { Day12.part2(in, out); }
//        try (var in = Input.of(Day13.class, "input.txt")) { Day13.part1(in, out); }
//        try (var in = Input.of(Day13.class, "input.txt")) { Day13.part2(in, out); }
//        try (var in = Input.of(Day14.class, "input.txt")) { Day14.part1(in, out); }
//        try (var in = Input.of(Day14.class, "input.txt")) { Day14.part2(in, out); }
//        try (var in = Input.of(Day15.class, "input.txt")) { Day15.part1(in, out); }
//        try (var in = Input.of(Day15.class, "input.txt")) { Day15.part2(in, out); }
//        try (var in = Input.of(Day16.class, "input.txt")) { Day16.part1(in, out); }
//        try (var in = Input.of(Day16.class, "input.txt")) { Day16.part2(in, out); }
//        try (var in = Input.of(Day17.class, "input.txt")) { Day17.part1(in, out); }
//        try (var in = Input.of(Day17.class, "input.txt")) { Day17.part2(in, out); }
//        try (var in = Input.of(Day18.class, "input.txt")) { Day18.part1(in, out); }
//        try (var in = Input.of(Day18.class, "input.txt")) { Day18.part2(in, out); }
        try (var in = Input.of(Day19.class, "input.txt")) { Day19.part1(in, out); }
        try (var in = Input.of(Day19.class, "input.txt")) { Day19.part2(in, out); }
    }
    
    /**
     * Returns an Input piped from the output of a curl command stored in a text file.
     * ...Which I still have to manually overwrite periodically (by copying from my browser), since the curl uses a
     * cookie with expiring session keys, and I'm too lazy to code the session handshakes myself.
     */
    private static Input curl() throws IOException {
        try (var curl = Input.of(CliRunner.class, "curl.txt")) {
            String[] cmd = curl.lines()
                .flatMap(line -> Arrays.stream(line.replaceAll("'|\\\\", "").trim().split(" ", 2)))
                .toArray(String[]::new);
            return Input.of(new ProcessBuilder(cmd).start().getInputStream());
        }
    }
}
