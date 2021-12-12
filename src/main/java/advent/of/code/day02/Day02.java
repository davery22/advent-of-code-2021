package advent.of.code.day02;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

public class Day02 {
    public static void part1(Input in, Output out) {
        class Position { int horizontal; int depth; }
        var pos = new Position();
        in.lines().map(line -> line.split(" ")).forEach(cmd -> {
            var units = Integer.parseInt(cmd[1]);
            switch (cmd[0]) {
                case "up"      -> pos.depth      -= units;
                case "down"    -> pos.depth      += units;
                case "forward" -> pos.horizontal += units;
            }
        });
        out.writeln(pos.horizontal * pos.depth);
    }
    
    public static void part2(Input in, Output out) {
        class Position { int horizontal; int depth; int aim; }
        var pos = new Position();
        in.lines().map(line -> line.split(" ")).forEach(cmd -> {
            var units = Integer.parseInt(cmd[1]);
            switch (cmd[0]) {
                case "up"      -> pos.aim -= units;
                case "down"    -> pos.aim += units;
                case "forward" -> {
                    pos.horizontal += units;
                    pos.depth      += units * pos.aim;
                }
            }
        });
        out.writeln(pos.horizontal * pos.depth);
    }
}
