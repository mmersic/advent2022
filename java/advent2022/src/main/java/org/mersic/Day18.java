package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day18 {
    
    record Posn(int x, int y, int z) {}
    
    private static int calcTotalSurfaceArea(List<Posn> posns) {
        int surfaceArea = 0;
        for (Posn p : posns) {
            int sa = 6;
            for (Posn q : posns) {
                if (p == q) {
                    continue;
                }
                if (p.x() == q.x() && p.y() == q.y() && Math.abs(p.z()-q.z()) == 1) {
                    sa--;
                }
                if (p.x() == q.x() && p.z() == q.z() && Math.abs(p.y()-q.y()) == 1) {
                    sa--;
                }
                if (p.y() == q.y() && p.z() == q.z() && Math.abs(p.x()-q.x()) == 1) {
                    sa--;
                }
            }
            surfaceArea += sa;
        }
        return surfaceArea;
    }
    
    private static int calcExteriorSurfaceArea(Set<Posn> posns) {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (Posn p : posns) {
            if (p.x() > maxX) {
                maxX = p.x()+1;
            }
            if (p.y() > maxY) {
                maxY = p.y()+1;
            }
            if (p.z() > maxZ) {
                maxZ = p.z()+1;
            }
        }
        Posn air = new Posn(maxX, maxY, maxZ);
        int exterior = 0;
        for (Posn p : posns) {
            if (!posns.contains(new Posn(p.x()+1, p.y(), p.z())) && bfs(posns, new Posn(p.x()+1, p.y(), p.z()),air)) {
                exterior++;
            }
            if (!posns.contains(new Posn(p.x()-1, p.y(), p.z())) && bfs(posns, new Posn(p.x()-1, p.y(), p.z()),air)) {
                exterior++;
            }
            if (!posns.contains(new Posn(p.x(), p.y()+1, p.z())) && bfs(posns, new Posn(p.x(), p.y()+1, p.z()),air)) {
                exterior++;
            }
            if (!posns.contains(new Posn(p.x(), p.y()-1, p.z())) && bfs(posns, new Posn(p.x(), p.y()-1, p.z()),air)) {
                exterior++;
            }
            if (!posns.contains(new Posn(p.x(), p.y(), p.z()+1)) && bfs(posns, new Posn(p.x(), p.y(), p.z()+1),air)) {
                exterior++;
            }
            if (!posns.contains(new Posn(p.x(), p.y(), p.z()-1)) && bfs(posns, new Posn(p.x(), p.y(), p.z()-1),air)) {
                exterior++;
            }
        }
        
        return exterior;
    }

    public static List<Posn> neighbors(Posn from) {
        List<Posn> N = new ArrayList<>();
        
        if (from.x() < 20)
            N.add(new Posn(from.x()+1, from.y(), from.z()));
        if (from.x() > 0)
            N.add(new Posn(from.x()-1, from.y(), from.z()));
        if (from.y() < 20)
            N.add(new Posn(from.x(), from.y()+1, from.z()));
        if (from.y() > 0)
            N.add(new Posn(from.x(), from.y()-1, from.z()));
        if (from.z() < 20) 
            N.add(new Posn(from.x(), from.y(), from.z()+1));
        if (from.z() > 0)
            N.add(new Posn(from.x(), from.y(), from.z()-1));
        return N;
    }
    
    public static boolean bfs(Set<Posn> posns, Posn from, Posn to) {
        List<Posn> Q = new ArrayList<>(neighbors(from));
        Set<Posn> S = new HashSet<>(Q);
        Set<Posn> visited = new HashSet<>();
        
        while (!Q.isEmpty()) {
            Posn F = Q.remove(0);
            visited.add(F);
            S.remove(F);
            if (posns.contains(F)) {
                continue;
            }
            if (F.equals(to)) {
                return true;
            } else {
                for (Posn n : neighbors(F)) {
                    if (!S.contains(n) && !visited.contains(n)) {
                        S.add(n);
                        if (Q.size() == 0) {
                            Q.add(n);
                        } else {
                            Q.add(Q.size() - 1, n);
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day18.class.getClassLoader().getResource("day.18.input").toURI()));
        List<Posn> posns = input.stream().map(x -> {
            String[] y = x.split(",");
            return new Posn(Integer.parseInt(y[0]),Integer.parseInt(y[1]),Integer.parseInt(y[2]));
        }).toList();
        
        int part1 = calcTotalSurfaceArea(posns);     
        Set<Posn> set = new HashSet<>(posns);
        int part2 = calcExteriorSurfaceArea(set);
        
        System.out.println("day 18 part 1: " + part1);
        System.out.println("day 18 part 2: " + part2);
    }


}
