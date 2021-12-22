package advent.of.code.day22;

import advent.of.code.io.Input;
import advent.of.code.io.Output;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day22 {
    public static void part1(Input in, Output out) {
        var regions = in.lines()
            .map(Day22::parseRegion)
            .filter(region ->
                   region.x0 >= -50 && region.x1 <= 50
                && region.y0 >= -50 && region.y1 <= 50
                && region.z0 >= -50 && region.z1 <= 50)
            .toList();
        solve(regions, out);
    }
    
    public static void part2(Input in, Output out) {
        var regions = in.lines().map(Day22::parseRegion).toList();
        solve(regions, out);
    }
    
    private static final Pattern NUMBER = Pattern.compile("-?\\d+");
    
    private static Region parseRegion(String line) {
        var arr = NUMBER.matcher(line).results().mapToInt(m -> Integer.parseInt(m.group())).toArray();
        return new Region(line.startsWith("on"), arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
    }
    
    public static void solve(List<Region> regions, Output out) {
        List<Region> nonOverlappingRegions = new ArrayList<>();
        for (var fore : regions) {
            nonOverlappingRegions = nonOverlappingRegions.stream()
                .flatMap(back -> removeOverlap(fore, back).stream())
                .collect(Collectors.toList());
            nonOverlappingRegions.add(fore);
        }
        var count = nonOverlappingRegions.stream().filter(Region::isOn).mapToLong(Region::size).sum();
        out.writeln(count);
    }
    
    private record Region(boolean isOn, int x0, int x1, int y0, int y1, int z0, int z1) {
        long size() { return (x1-x0+1L) * (y1-y0+1L) * (z1-z0+1L); }
    }
    
    private static List<Region> removeOverlap(Region fore, Region back) {
        var overlaps =
               fore.x1 >= back.x0 && back.x1 >= fore.x0
            && fore.y1 >= back.y0 && back.y1 >= fore.y0
            && fore.z1 >= back.z0 && back.z1 >= fore.z0;
        if (!overlaps) return List.of(back);
        // Split the background region into up to 26 non-overlapping regions.
        var diffx0 = back.x0 < fore.x0;
        var diffx1 = fore.x1 < back.x1;
        var diffy0 = back.y0 < fore.y0;
        var diffy1 = fore.y1 < back.y1;
        var diffz0 = back.z0 < fore.z0;
        var diffz1 = fore.z1 < back.z1;
        var maxx0 = Math.max(back.x0, fore.x0);
        var minx1 = Math.min(back.x1, fore.x1);
        var maxy0 = Math.max(back.y0, fore.y0);
        var miny1 = Math.min(back.y1, fore.y1);
        var maxz0 = Math.max(back.z0, fore.z0);
        var minz1 = Math.min(back.z1, fore.z1);
        var result = new ArrayList<Region>();
        // FACES
        // x
        if (diffx0) result.add(new Region(back.isOn, back.x0, fore.x0-1, maxy0, miny1, maxz0, minz1));
        if (diffx1) result.add(new Region(back.isOn, fore.x1+1, back.x1, maxy0, miny1, maxz0, minz1));
        // y
        if (diffy0) result.add(new Region(back.isOn, maxx0, minx1, back.y0, fore.y0-1, maxz0, minz1));
        if (diffy1) result.add(new Region(back.isOn, maxx0, minx1, fore.y1+1, back.y1, maxz0, minz1));
        // z
        if (diffz0) result.add(new Region(back.isOn, maxx0, minx1, maxy0, miny1, back.z0, fore.z0-1));
        if (diffz1) result.add(new Region(back.isOn, maxx0, minx1, maxy0, miny1, fore.z1+1, back.z1));
        // EDGES
        // xy
        if (diffx0 && diffy0) result.add(new Region(back.isOn, back.x0, fore.x0-1, back.y0, fore.y0-1, maxz0, minz1));
        if (diffx0 && diffy1) result.add(new Region(back.isOn, back.x0, fore.x0-1, fore.y1+1, back.y1, maxz0, minz1));
        if (diffx1 && diffy0) result.add(new Region(back.isOn, fore.x1+1, back.x1, back.y0, fore.y0-1, maxz0, minz1));
        if (diffx1 && diffy1) result.add(new Region(back.isOn, fore.x1+1, back.x1, fore.y1+1, back.y1, maxz0, minz1));
        // xz
        if (diffx0 && diffz0) result.add(new Region(back.isOn, back.x0, fore.x0-1, maxy0, miny1, back.z0, fore.z0-1));
        if (diffx0 && diffz1) result.add(new Region(back.isOn, back.x0, fore.x0-1, maxy0, miny1, fore.z1+1, back.z1));
        if (diffx1 && diffz0) result.add(new Region(back.isOn, fore.x1+1, back.x1, maxy0, miny1, back.z0, fore.z0-1));
        if (diffx1 && diffz1) result.add(new Region(back.isOn, fore.x1+1, back.x1, maxy0, miny1, fore.z1+1, back.z1));
        // yz
        if (diffy0 && diffz0) result.add(new Region(back.isOn, maxx0, minx1, back.y0, fore.y0-1, back.z0, fore.z0-1));
        if (diffy0 && diffz1) result.add(new Region(back.isOn, maxx0, minx1, back.y0, fore.y0-1, fore.z1+1, back.z1));
        if (diffy1 && diffz0) result.add(new Region(back.isOn, maxx0, minx1, fore.y1+1, back.y1, back.z0, fore.z0-1));
        if (diffy1 && diffz1) result.add(new Region(back.isOn, maxx0, minx1, fore.y1+1, back.y1, fore.z1+1, back.z1));
        // VERTICES
        // xyz
        if (diffx0 && diffy0 && diffz0) result.add(new Region(back.isOn, back.x0, fore.x0-1, back.y0, fore.y0-1, back.z0, fore.z0-1));
        if (diffx0 && diffy0 && diffz1) result.add(new Region(back.isOn, back.x0, fore.x0-1, back.y0, fore.y0-1, fore.z1+1, back.z1));
        if (diffx0 && diffy1 && diffz0) result.add(new Region(back.isOn, back.x0, fore.x0-1, fore.y1+1, back.y1, back.z0, fore.z0-1));
        if (diffx0 && diffy1 && diffz1) result.add(new Region(back.isOn, back.x0, fore.x0-1, fore.y1+1, back.y1, fore.z1+1, back.z1));
        if (diffx1 && diffy0 && diffz0) result.add(new Region(back.isOn, fore.x1+1, back.x1, back.y0, fore.y0-1, back.z0, fore.z0-1));
        if (diffx1 && diffy0 && diffz1) result.add(new Region(back.isOn, fore.x1+1, back.x1, back.y0, fore.y0-1, fore.z1+1, back.z1));
        if (diffx1 && diffy1 && diffz0) result.add(new Region(back.isOn, fore.x1+1, back.x1, fore.y1+1, back.y1, back.z0, fore.z0-1));
        if (diffx1 && diffy1 && diffz1) result.add(new Region(back.isOn, fore.x1+1, back.x1, fore.y1+1, back.y1, fore.z1+1, back.z1));
        return result;
    }
}
