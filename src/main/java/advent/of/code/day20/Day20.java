package advent.of.code.day20;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20 {
    public static void part1(Input in, Output out) {
        solve(in, out, 2);
    }
    
    public static void part2(Input in, Output out) {
        solve(in, out, 50);
    }
    
    private static void solve(Input in, Output out, int times) {
        var algo = in.readLine();
        var image = in.lines().toArray(String[]::new);
        var outer = 0;
        for (var i = 0; i < times; i++) {
            image = enhance(image, algo, outer);
            outer = algo.charAt(outer == 0 ? 0 : 511) == '#' ? 1 : 0;
        }
        var count = Arrays.stream(image).mapToLong(line -> line.chars().filter(c -> '#' == c).count()).sum();
        out.writeln(outer == 1 ? "infinite" : count); // Not that the former would actually be asked for.
    }
    
    private static String[] enhance(String[] image, String algo, int outer) {
        return IntStream.range(-1, image.length+1).parallel()
            .mapToObj(i -> IntStream.range(-1, image[0].length()+1)
                .mapToObj(j -> String.valueOf(decode(i, j, image, algo, outer)))
                .collect(Collectors.joining()))
            .toArray(String[]::new);
    }
    
    private static char decode(int x, int y, String[] image, String algo, int outer) {
        var value = 0;
        for (var i = x-1; i < x+2; i++) {
            for (var j = y-1; j < y+2; j++) {
                value <<= 1;
                if      (i < 0 || i >= image.length)      value+=outer;
                else if (j < 0 || j >= image[0].length()) value+=outer;
                else if (image[i].charAt(j) == '#')       value++;
            }
        }
        return algo.charAt(value);
    }
}
