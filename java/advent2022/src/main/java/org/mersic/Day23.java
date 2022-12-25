package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day23 {

    enum DIR { NORTH, SOUTH, WEST, EAST;
        private static final DIR[] vals = values();
        public DIR next() {
            return vals[(this.ordinal()+1) % vals.length];
        }
    }
    
    record Posn(int x, int y) {}

    record Elv(int x, int y) {
        Posn propose(DIR dir) {
            return switch (dir) {
                case NORTH -> new Posn(x, y+1);
                case SOUTH -> new Posn(x, y-1);
                case WEST -> new Posn(x-1, y);
                case EAST -> new Posn(x+1, y);
            };
        }

        boolean hasNeighbors(Set<Elv> elves, DIR dir) {
            return switch (dir) {
                case NORTH -> elves.contains(new Elv(x-1, y+1)) || elves.contains(new Elv(x, y+1)) || elves.contains(new Elv(x+1, y+1));
                case SOUTH -> elves.contains(new Elv(x-1, y-1)) || elves.contains(new Elv(x, y-1)) || elves.contains(new Elv(x+1, y-1));
                case WEST  -> elves.contains(new Elv(x-1, y+1)) || elves.contains(new Elv(x-1, y)) || elves.contains(new Elv(x-1, y-1));
                case EAST ->  elves.contains(new Elv(x+1, y+1)) || elves.contains(new Elv(x+1, y)) || elves.contains(new Elv(x+1, y-1));
            };
        }
        
        boolean hasNeighbors(Set<Elv> elves) {
            return    elves.contains(new Elv(x-1, y))
                   || elves.contains(new Elv(x-1,y+1))
                   || elves.contains(new Elv(x-1,y-1)) 
                   || elves.contains(new Elv(x,y+1))
                   || elves.contains(new Elv(x,y-1))
                   || elves.contains(new Elv(x+1, y))
                   || elves.contains(new Elv(x+1,y+1))
                   || elves.contains(new Elv(x+1,y-1))                    
                   ;
        }
    }
    
    public static Set<Elv> getElves(List<String> input) {
        Set<Elv> elves = new HashSet<>();
        for (int i = 0; i < input.size(); i++) {
            char[] line = input.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                if (line[j] == '#') {
                    elves.add(new Elv(j, (input.size()-1)-i));
                }
            }
        }
        return elves;
    }
    
    public static void print(Set<Elv> elves, int len) {
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (elves.contains(new Elv(j, (len-1)-i))) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static int applyRounds(Set<Elv> elves, int rounds) {
        DIR dir = DIR.NORTH;
        for (int i = 0; i < rounds; i++) {
            Map<Posn, Set<Elv>> map = new HashMap<>();
            for (Elv e : elves) {
                if (!e.hasNeighbors(elves)) {
                    continue;
                }
                DIR next = dir;
                for (int j = 0; j < DIR.vals.length; j++) {
                    if (!e.hasNeighbors(elves, next)) {
                        Posn p = e.propose(next);
                        if (map.containsKey(p)) {
                            map.get(p).add(e);
                        } else {
                            Set<Elv> E = new HashSet<>(); E.add(e); map.put(p, E);
                        }
                        break;
                    } else {
                        next = next.next();
                    }
                }
            }
            dir = dir.next();
            boolean moved = false;
            for (Posn p : map.keySet()) {
                Set<Elv> E = map.get(p);
                if (E.size() == 1) {
                    for (Elv e : E) {
                        elves.remove(e);
                        elves.add(new Elv(p.x(), p.y()));
                        moved = true;
                    }
                }
            }
            if (!moved) {
                return (i+1);
            }
        }
        return rounds;
    }
    
    public static int emptySpace(Set<Elv> elves) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Elv e : elves) {
            minX = Math.min(minX, e.x());
            minY = Math.min(minY, e.y());
            maxX = Math.max(maxX, e.x());
            maxY = Math.max(maxY, e.y());
        }
        
        return (((maxX-minX)+1) * ((maxY-minY)+1)) - elves.size();
    }
    
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day23.class.getClassLoader().getResource("day.23.input").toURI()));
        
        Set<Elv> elves = getElves(input);
        
        applyRounds(elves, 10);
        int part1 = emptySpace(elves);
        int part2 = applyRounds(getElves(input), Integer.MAX_VALUE);
        
        System.out.println("day 23 part 1: " + part1);
        System.out.println("day 23 part 2: " + part2);
    }
}
