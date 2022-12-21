package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day15 {
    
    static class Range implements Comparable<Range> {
        public Range(int x1, int x2, boolean part1, int max) {
            if (!part1 && x1 < 0) {
                x1  = 0;
            } 
            if (!part1 && x2 > max) {
                x2 = max;
            }
            this.x1 = x1;
            this.x2 = x2;
        }
        public int x1;
        public int x2;
        public String toString() { return "R=[" + x1 + ".." + x2 + "]"; }
        
        public int compareTo(Range other) {
            if (this.x1 == other.x1) {
                return 0;
            } else if (this.x1 < other.x1) {
                return -1;
            } else {
                return 1;
            }
        }
    }
    
    static class Sensor {
        public Sensor(int x, int y, int bx, int by) {
            this.x = x; this.y = y; 
            this.bx = bx; this.by = by;
            this.md = Math.abs(x-bx) + Math.abs(y-by);
        }
        public int x;
        public int y;
        public int bx;
        public int by;
        public int md;
        public String toString() {
            return "S(" + x + "," + y + ")..B(" + bx + "," + by + ")..md=" + md;
        }
    }
    
    public static List<Range> findOverlaps(List<Sensor> sensors, int y, boolean part1, int max) {
        List<Range> ranges = new ArrayList<>();
        for (Sensor s : sensors) {
            int dd = -1;
            if (y == s.y) {
                dd = s.md;
            } else if (y > s.y && s.y+s.md >= y) {
                int d = y-s.y;
                dd = s.md-d;
            } else if (y < s.y && s.y-s.md <= y) {
                int d = s.y-y;
                dd = s.md-d;
            }
            if (part1 && s.by == y) {
                int l = s.x-dd;
                int r = s.x+dd;
                if (s.bx == l && (r-l) > 1) {
                    ranges.add(new Range(l+1, r, part1, max));
                } else if (s.bx == r && (r-l) > 1) {
                    ranges.add(new Range(l, r-1, part1, max));
                } else if (l != r) {
                    ranges.add(new Range(l, s.bx-1, part1, max));
                    ranges.add(new Range(s.bx+1, r, part1, max));
                }
            } else {
                if (dd >= 0) {
                    Range R = new Range(s.x - dd, s.x + dd, part1, max);
                    ranges.add(R);
                }
            }
        }
        return ranges;
    }
    
    public static int intersect(List<Range> ranges, int l) {
        ranges.sort(Comparator.naturalOrder());
        if (ranges.size() > 0 && ranges.get(0).x1 != l) {
            return 0;
        }
        int max = -1;
        for (int i = 1; i < ranges.size(); i++) {
            if (ranges.get(i-1).x2 > max) {
                max = ranges.get(i-1).x2;
            }
            if (max < (ranges.get(i).x1-1)) {
                return ranges.get(i).x1-1;
            }
        }
        return -1;
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day15.class.getClassLoader().getResource("day.15.input").toURI()));
        
        List<Sensor> sensors = new ArrayList<>();
        for (String line : input) {
            line = line.substring(10);
            String[] split = line.split(": closest beacon is at ");
            String[] sensor = split[0].split(", ");
            String[] beacon = split[1].split(", ");
            int sx = Integer.parseInt(sensor[0].substring(2));
            int sy = Integer.parseInt(sensor[1].substring(2));
            int bx = Integer.parseInt(beacon[0].substring(2));
            int by = Integer.parseInt(beacon[1].substring(2));
            Sensor S = new Sensor(sx, sy, bx, by);
            sensors.add(S);
        }

        boolean sample = false;
        int max = sample ? 20 : 4000000;
        int toFind = sample ? 10 : 2000000;
        
        List<Range> ranges = findOverlaps(sensors, toFind, true, max);
        Set<Integer> part1 = new HashSet<>();
        for (Range r : ranges) {
            for (int i = r.x1; i <= r.x2; i++) {
                part1.add(i);
            }
        }
        
        int dx = -1;
        int dy = -1;
        for (int i = 0; i < max; i++) {
            ranges = findOverlaps(sensors, i, false, max);
            int x = intersect(ranges, 0);
            if (x >= 0 && x <= max) {
                dx = x;
                dy = i;
                break;
            }
        }
        
        long part2 = dx * 4000000l;
        part2 += dy;
        System.out.println("day 15 part 1: " + part1.size());
        System.out.println("day 15 part 2: " + part2);
    }
}
