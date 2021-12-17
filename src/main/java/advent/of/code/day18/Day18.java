package advent.of.code.day18;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day18 {
    public static void part1(Input in, Output out) {
        var sum = in.lines().map(Day18::tokenize)
            .reduce((left, right) -> reduce(add(left, right)))
            .orElseThrow();
        out.writeln(magnitude(sum));
    }
    
    public static void part2(Input in, Output out) {
        var numbers = in.lines().map(Day18::tokenize).toList();
        var max = IntStream.range(0, numbers.size()).parallel()
            .flatMap(i -> IntStream.range(0, numbers.size()).filter(j -> j != i)
                .map(j -> magnitude(reduce(add(numbers.get(i), numbers.get(j))))))
            .max().orElseThrow();
        out.writeln(max);
    }
    
    private sealed interface Token permits Symbol, Num {}
    private enum Symbol implements Token { L_BRACE, R_BRACE, COMMA }
    private record Num(int n) implements Token {}
    
    private static List<Token> tokenize(String s) {
        return s.chars()
            .mapToObj(ch -> switch (ch) {
                case '[' -> Symbol.L_BRACE;
                case ']' -> Symbol.R_BRACE;
                case ',' -> Symbol.COMMA;
                default -> new Num(Character.getNumericValue(ch));
            })
            .collect(Collectors.toCollection(LinkedList::new));
    }
    
    private static List<Token> add(List<Token> left, List<Token> right) {
        var sum = new LinkedList<Token>();
        sum.add(Symbol.L_BRACE);
        sum.addAll(left);
        sum.add(Symbol.COMMA);
        sum.addAll(right);
        sum.add(Symbol.R_BRACE);
        return sum;
    }
    
    private static List<Token> reduce(List<Token> source) {
        // Fun fact: If we didn't have ListIterator, we'd use two stacks.
        var tokens = new LinkedList<>(source);
        loop: for (;;) {
            // Look for explodes
            var nest = 0;
            var iter = tokens.listIterator();
            while (iter.hasNext()) {
                var token = iter.next();
                if      (token == Symbol.L_BRACE) nest++;
                else if (token == Symbol.R_BRACE) nest--;
                else if (token == Symbol.COMMA) {
                    if (nest <= 4) continue;
                    // We are in the middle of a pair that needs exploded.
                    iter.previous();
                    var idx = iter.nextIndex();
                    // Add left if possible.
                    var left = (Num) iter.previous();
                    while (iter.hasPrevious()) {
                        if (iter.previous() instanceof Num num) {
                            iter.set(new Num(num.n + left.n));
                            break;
                        }
                    }
                    // Return to comma.
                    while (iter.nextIndex() <= idx) iter.next();
                    // Add right if possible.
                    var right = (Num) iter.next();
                    while (iter.hasNext()) {
                        if (iter.next() instanceof Num num) {
                            iter.set(new Num(num.n + right.n));
                            break;
                        }
                    }
                    // Return to left brace before comma.
                    while (iter.previousIndex() >= idx-2) iter.previous();
                    // Remove 5 items: [ N , N ]
                    for (var i = 0; i < 5; i++) {
                        iter.next();
                        iter.remove();
                    }
                    // Add 1 item: 0
                    iter.add(new Num(0));
                    nest--;
                }
            }
            // Look for splits
            iter = tokens.listIterator();
            while (iter.hasNext()) {
                if (iter.next() instanceof Num num && num.n >= 10) {
                    iter.remove();
                    iter.add(Symbol.L_BRACE);
                    iter.add(new Num(num.n/2));
                    iter.add(Symbol.COMMA);
                    iter.add(new Num((num.n+1)/2));
                    iter.add(Symbol.R_BRACE);
                    continue loop;
                }
            }
            break;
        }
        return tokens;
    }
    
    private static int magnitude(List<Token> tokens) {
        var stack = new ArrayDeque<Integer>();
        for (var token : tokens) {
            if (token instanceof Num num) stack.push(num.n);
            else if (token == Symbol.R_BRACE) {
                var second = stack.pop();
                var first  = stack.pop();
                stack.push(3*first + 2*second);
            }
        }
        return stack.pop();
    }
}
