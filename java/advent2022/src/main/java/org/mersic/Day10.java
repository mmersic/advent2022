package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day10 {
    public static void main (String args[]) throws Exception {

        List<String> input = Files.readAllLines(Path.of(Day10.class.getClassLoader().getResource("day.10.input").toURI()));
        
        int X = 1;
        int cycle = 0;
        int opLength = 0;
        int sigStrength = 0;
        int[][] screen = new int[6][40];
        for (String line : input) {
            String[] l = line.split(" ");
            int toAdd = 0;
            if ("noop".equals(l[0])) {
                opLength = 1;
            } else if ("addx".equals(l[0])) {
                toAdd = Integer.parseInt(l[1]);
                opLength = 2;
            }
            while (opLength > 0) {
                if (cycle%40 == X || cycle%40 == X-1 || cycle%40 == X+1) {
                    screen[cycle / 40][cycle%40] = 1;
                }
                cycle++;
                opLength--;
                if (cycle == 20 || (cycle-20) % 40 == 0) {
                    sigStrength += (cycle * X);
                    System.out.println("cycle: " + cycle + " X: " + X + " sigStrength: " + sigStrength);
                }
            }
            X += toAdd;
        }
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 40; j++) {
                System.out.print(screen[i][j] == 1 ? "#" : ".");
            }
            System.out.println();
        }
        
        //17180
        System.out.println("day 10 part 1: " + sigStrength);
        
        //REHPRLUB
        System.out.println("day 10 part 2: ");
    }
}
