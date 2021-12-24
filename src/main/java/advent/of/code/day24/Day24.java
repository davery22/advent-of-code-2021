package advent.of.code.day24;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.*;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day24 {
    // The input ALU program is composed of 14 repetitions/batches of the same template of instructions.
    // Condensed, the template looks like:
    //   1. w = input()
    //   2. x = (w == (z % 26) + P) ? 0 : 1
    //   3. z = (z / Q) * (25 * x + 1) + ((w + R) * x)
    // where Q = 26 or 1, and P and R are arbitrary
    //
    // Note that since x is either 0 or 1 on Line 3, Line 3 can be written as:
    //   3. z = (x == 0) ? (z / Q) : ((z / Q) * 26 + w + R))
    //
    // Our goal is to make z = 0 at the end of the program. We can observe that the value of z only decreases when
    // x == 0. Since 0 < w < 10, x == 0 is only possible when 0 < (z % 26) + P < 10. If P >= 10, it's already impossible
    // for that batch. It turns out that P < 10 iff Q == 26. If we assume that we will hit all possible x == 0 cases, we
    // can rewrite Line 3 one more time:
    //   3. z = (x == 0) ? (z / 26) : (z * 26 + w + R)
    //
    // It turns out that half of the batches have Q == 26 and P < 10, and the other half have Q == 1 and P >= 10. So we
    // actually do need to hit every x == 0 case to make z = 0 at the end of the program. The only trick is setting up
    // z so that, when P < 10, 0 < (z % 26) + P < 10. Due to the multiplication or division of z by 26 in each batch,
    // each 'multiplication batch' ends up paired with a later 'division batch', where the input of the former directly
    // determines the result of z % 26 in the latter. With that in mind, in each 'multiplication batch', we can choose
    // the input digit to be as high (part 1) or low (part 2) as possible, such that 0 < (z % 26) + P < 10 in the paired
    // 'division batch'.
    
    public static void part1(Input in, Output out) {
        solve(in, out, 9);
    }
    
    public static void part2(Input in, Output out) {
        solve(in, out, 1);
    }
    
    private static void solve(Input in, Output out, long digit) {
        var batches = parseInstrBatches(in);
        var alu = new ALU();
        var digits = LongStream.range(0, 14).map(i -> digit).toArray();
        var stack = new ArrayDeque<Integer>();
        for (var i = 0; i < batches.size(); i++) {
            var ii = i;
            alu.input = () -> digits[ii];
            var instrs = batches.get(i);
            for (var ip = 0; ip < instrs.size(); ip++) {
                var instr = instrs.get(ip);
                alu.execute(instr);
                if (ip == 5) { // instr is 'add x P'
                    var x = alu.vars[1];
                    var p = Integer.parseInt(instr.split(" ")[2]);
                    if (p < 10) { // This is one of the batches where it's possible to set w = x, causing z to be reduced.
                        if (0 < x && x < 10) { // We can just fix up our last input (in w) to equal x.
                            stack.pop();
                            digits[i] = alu.vars[0] = x;
                        } else { // We can't just fix up our last input, need to backtrack and fix up an earlier input.
                            i = stack.pop()-1;
                            digits[i+1] = digits[ii] + (x > 9 ? (9 - x) : (1 - x));
                            break;
                        }
                    } else {
                        stack.push(i);
                    }
                }
            }
        }
        var modelNumber = Arrays.stream(digits).mapToObj(String::valueOf).collect(Collectors.joining());
        out.writeln(modelNumber);
    }
   
    private static List<List<String>> parseInstrBatches(Input in) {
        var program = in.lines().toList();
        var batches = new ArrayList<List<String>>();
        int start = 0, end = 0;
        while (++end < program.size()) {
            if ("inp w".equals(program.get(end))) {
                batches.add(program.subList(start, end));
                start = end;
            }
        }
        batches.add(program.subList(start, end));
        return batches;
    }
    
    private static class ALU {
        long[] vars = new long[4];
        LongSupplier input;
    
        void execute(String instr) {
            var p = instr.split(" ");
            switch (p[0]) {
                case "inp" -> vars[id(p[1])]  = input.getAsLong();
                case "add" -> vars[id(p[1])] += value(p[2]);
                case "mul" -> vars[id(p[1])] *= value(p[2]);
                case "div" -> vars[id(p[1])] /= value(p[2]);
                case "mod" -> vars[id(p[1])] %= value(p[2]);
                case "eql" -> vars[id(p[1])]  = value(p[1]) == value(p[2]) ? 1 : 0;
            }
        }
        
        long value(String operand) {
            return switch (operand) {
                case "w" -> vars[0];
                case "x" -> vars[1];
                case "y" -> vars[2];
                case "z" -> vars[3];
                default -> Long.parseLong(operand);
            };
        }
        
        int id(String operand) {
            return switch (operand) {
                case "w" -> 0;
                case "x" -> 1;
                case "y" -> 2;
                case "z" -> 3;
                default -> throw new AssertionError();
            };
        }
    }
}
