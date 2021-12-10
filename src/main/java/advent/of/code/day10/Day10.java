package advent.of.code.day10;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.ArrayDeque;
import java.util.stream.Stream;

public class Day10 {
    public static void part1(Input in, Output out) {
        var score = in.lines().parallel()
            .mapToLong(line -> {
                var stack = new ArrayDeque<Character>();
                for (var i = 0; i < line.length(); i++) {
                    switch (line.charAt(i)) {
                        case '(', '[', '{', '<' -> stack.push(line.charAt(i));
                        case ')' -> { if (stack.pop() != '(') return 3;     }
                        case ']' -> { if (stack.pop() != '[') return 57;    }
                        case '}' -> { if (stack.pop() != '{') return 1197;  }
                        case '>' -> { if (stack.pop() != '<') return 25137; }
                    }
                }
                return 0; // line is not corrupted (just incomplete)
            })
            .sum();
        out.writeln(score);
    }
    
    public static void part2(Input in, Output out) {
        var scores = in.lines().parallel()
            .flatMap(line -> {
                var stack = new ArrayDeque<Character>();
                for (var i = 0; i < line.length(); i++) {
                    switch (line.charAt(i)) {
                        case '(', '[', '{', '<' -> stack.push(line.charAt(i));
                        case ')' -> { if (stack.pop() != '(') return Stream.empty(); }
                        case ']' -> { if (stack.pop() != '[') return Stream.empty(); }
                        case '}' -> { if (stack.pop() != '{') return Stream.empty(); }
                        case '>' -> { if (stack.pop() != '<') return Stream.empty(); }
                    }
                }
                return Stream.of(stack);
            })
            .mapToLong(stack -> {
                var score = 0L;
                while (!stack.isEmpty()) {
                    score *= 5;
                    switch (stack.pop()) {
                        case '(' -> score += 1;
                        case '[' -> score += 2;
                        case '{' -> score += 3;
                        case '<' -> score += 4;
                    }
                }
                return score;
            })
            .sorted()
            .toArray();
        out.writeln(scores[scores.length/2]);
    }
}
