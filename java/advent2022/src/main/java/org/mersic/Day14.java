package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

record Pair(int x, int y) { }
record Rock(Pair[] p) { }

public class Day14 {
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day14.class.getClassLoader().getResource("day.14.input").toURI()));
        
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        
        List<Rock> rocks = new ArrayList<>();
        for (String line : input) {
            String[] pairs = line.split(" -> ");
            Pair[] p = new Pair[pairs.length];
            for (int i = 0; i < pairs.length; i++) {
                String[] xy = pairs[i].split(",");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                p[i] = new Pair(x, y);
                if (x < minX) { minX = x; }
                if (x > maxX) { maxX = x; }
                if (y > maxY) { maxY = y; }
            }
            rocks.add(new Rock(p));
        }
        
        int part1 = simSand(rocks, minX, maxX, maxY);
        System.out.println("day 14 part 1: " + part1);
        
        Pair[] floor = new Pair[2];
        floor[0] = new Pair(0, maxY+2);
        floor[1] = new Pair(maxX*2, maxY+2);
        rocks.add(new Rock(floor));
        int part2 = simSand(rocks, 0, maxX*2, maxY+2);
        
        System.out.println("day 14 part 2: " + part2);
    }

    public static int simSand(List<Rock> rocks, int minX, int maxX, int maxY) {
        int[][] cave = new int[maxY+1][(maxX-minX)+1];
        for (Rock rock : rocks) {
            Pair[] pairs = rock.p();
            Pair current = pairs[0];
            for (int i = 1; i < pairs.length; i++) {
                Pair next = pairs[i];
                if (current.x() != next.x()) {
                    int from = Math.min(current.x(), next.x());
                    int to   = Math.max(current.x(), next.x());
                    for (int j = from; j <= to; j++) {
                        cave[current.y()][j-minX] = 1;
                    }
                } else {
                    int from = Math.min(current.y(), next.y());
                    int to   = Math.max(current.y(), next.y());
                    for (int j = from; j <= to; j++) {
                        cave[j][current.x()-minX] = 1;
                    }
                }
                current = next;
            }
        }

        int x = 500-minX;
        int y = 0;
        int count = 0;
        while (true) {
            if (cave[y][x] != 0) {
                break;
            }
            if (y+1 > maxY) {
                break;
            }
            if (cave[y+1][x] == 0) {
                y++;
                continue;
            }
            if (x-1 < 0) {
                break;
            }
            if (cave[y+1][x-1] == 0) {
                y++;
                x--;
                continue;
            }
            if (x+1 < maxX && cave[y+1][x+1] == 0) {
                y++;
                x++;
                continue;
            }

            cave[y][x] = 2;
            count++;
            x = 500-(minX);
            y = 0;
        }
        return count;
    }

    private static void print(int[][] cave) {
        System.out.println();
        for (int i = 0; i < cave.length; i++) {
            for (int j = 0; j < cave[0].length; j++) {
                switch (cave[i][j]) {
                    case 0 -> System.out.print(".");
                    case 1 -> System.out.print("#");
                    case 2 -> System.out.print("o");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void print(int[][] cave, int x, int y) {
        System.out.println();
        for (int i = 0; i < cave.length; i++) {
            for (int j = 0; j < cave[0].length; j++) {
                if (i == y && j == x) {
                    System.out.print("X");
                } else {
                    switch (cave[i][j]) {
                        case 0 -> System.out.print(".");
                        case 1 -> System.out.print("#");
                        case 2 -> System.out.print("o");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}
