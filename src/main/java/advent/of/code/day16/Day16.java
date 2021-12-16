package advent.of.code.day16;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Day16 {
    public static void part1(Input in, Output out) {
        var binary = parseBinary(in);
        var c = new Cursor();
        parsePackets(binary, c);
        out.writeln(c.checksum);
    }
    
    public static void part2(Input in, Output out) {
        var binary = parseBinary(in);
        var c = new Cursor();
        var answer = parsePackets(binary, c);
        out.writeln(answer);
    }
    
    private static String parseBinary(Input in) {
        return in.readLine().chars()
            .map(Character::getNumericValue)
            .mapToObj(Integer::toBinaryString)
            .map(b -> b.length() == 4 ? b : String.format("%0" + (4-b.length()) + "d%s", 0, b))
            .collect(Collectors.joining());
    }
    
    private static class Cursor { int pos; long checksum; }
    
    private static long parsePackets(String binary, Cursor c) {
        var version = Integer.parseInt(binary.substring(c.pos, c.pos+=3), 2);
        var typeId = Integer.parseInt(binary.substring(c.pos, c.pos+=3), 2);
        c.checksum += version;
        if (typeId == 4) {
            var literal = 0L;
            while (true) {
                var stop = binary.charAt(c.pos++) == '0';
                literal <<= 4;
                literal += Integer.parseInt(binary.substring(c.pos, c.pos+=4), 2);
                if (stop) return literal;
            }
        } else {
            var lengthTypeId = binary.charAt(c.pos++) == '0' ? 0 : 1;
            var subPackets = new ArrayList<Long>();
            if (lengthTypeId == 0) {
                var lengthOfSubPackets = Integer.parseInt(binary.substring(c.pos, c.pos+=15), 2);
                var end = c.pos + lengthOfSubPackets;
                while (c.pos < end) subPackets.add(parsePackets(binary, c));
            } else {
                var numberOfSubPackets = Integer.parseInt(binary.substring(c.pos, c.pos+=11), 2);
                while (numberOfSubPackets-- > 0) subPackets.add(parsePackets(binary, c));
            }
            return switch (typeId) {
                case 0 -> subPackets.stream().mapToLong(i -> i).sum();
                case 1 -> subPackets.stream().mapToLong(i -> i).reduce(1, (p, i) -> p * i);
                case 2 -> subPackets.stream().mapToLong(i -> i).min().orElseThrow();
                case 3 -> subPackets.stream().mapToLong(i -> i).max().orElseThrow();
                case 5 -> subPackets.get(0) >      subPackets.get(1)  ? 1 : 0;
                case 6 -> subPackets.get(0) <      subPackets.get(1)  ? 1 : 0;
                case 7 -> subPackets.get(0).equals(subPackets.get(1)) ? 1 : 0;
                default -> throw new AssertionError();
            };
        }
    }
}
