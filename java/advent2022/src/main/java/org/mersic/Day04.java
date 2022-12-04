package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day04 {
    
    public static int[] overlap(int x1, int x2, int y1, int y2) {
        int range = (Math.max(x2, y2)) + 1;
        int[] count = new int[range];

        for (int i = x1; i <= x2; i++) {
            count[i]++;
        }
        for (int i = y1; i <= y2; i++) {
            count[i]++;
        }
        
        return count;
    }
    
    public static boolean rcontains(int x1, int x2, int y1, int y2) {
        int[] count = overlap(x1, x2, y1, y2);
        int[] range = new int[] {x1, x2, y1, y2};

        next: for (int i = 0; i < range.length; i+=2) {
            for (int j = range[i]; j <= range[i+1]; j++) {
                if (count[j] != 2) {
                    continue next;
                }
            }
            return true;
        }
        return false;
    }
    
    public static boolean ranyoverlap(int x1, int x2, int y1, int y2) {
        int[] count = overlap(x1, x2, y1, y2);

        for (int c : count) {
            if (c > 1) {
                return true;
            }
        }
        
        return false;
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day04.class.getClassLoader().getResource("day.04.input").toURI()));
        
        int overlapCount = 0;
        int anyOverlapCount = 0;
        for (String line : input) {
            String[] l = line.split(",");
            String[] l1 = l[0].split("-");
            String[] l2 = l[1].split("-");
            
            int x1 = Integer.parseInt(l1[0]);
            int x2 = Integer.parseInt(l1[1]);
            int y1 = Integer.parseInt(l2[0]);
            int y2 = Integer.parseInt(l2[1]);

            if (rcontains(x1, x2, y1, y2)) {
                overlapCount++;
            }
            if (ranyoverlap(x1, x2, y1, y2)) {
                anyOverlapCount++;
            }
        }
        
        System.out.println("day 4 part 1: " + overlapCount);
        System.out.println("day 4 part 2: " + anyOverlapCount);
    }
}
