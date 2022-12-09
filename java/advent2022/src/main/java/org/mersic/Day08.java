package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day08 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day08.class.getClassLoader().getResource("day.08.input").toURI()));
        
        int[][] t = new int[input.size()][input.get(0).length()];
        
        int i = 0;
        for (String line : input) {
            int j = 0;
            for (char c : line.toCharArray()) {
                t[i][j] = c-'0';
                j++;
            }
            i++;
        }
        
        int countVisible = t.length*2 + t[0].length*2 - 4;
        for (i = 1; i < t.length-1; i++) {
            for (int j = 1; j < t[0].length-1; j++) {
                boolean visible = true;
                for (int k = i-1; k >= 0; k--) {
                    if (t[k][j] >= t[i][j]) {
                        visible = false;
                        break;
                    }
                }
                if (visible) {
                    countVisible++;
                    continue;
                }
                visible = true;
                for (int k = i+1; k < t.length; k++) {
                    if (t[k][j] >= t[i][j]) {
                        visible = false;
                        break;
                    }
                }
                if (visible) {
                    countVisible++;
                    continue;
                }
                visible = true;
                for (int k = j-1; k >= 0; k--) {
                    if (t[i][k] >= t[i][j]) {
                        visible = false;
                        break;
                    }
                }
                if (visible) {
                    countVisible++;
                    continue;
                }
                visible = true;
                for (int k = j+1; k < t[0].length; k++) {
                    if (t[i][k] >= t[i][j]) {
                        visible = false;
                        break;
                    }
                }
                if (visible) {
                    countVisible++;
                    continue;
                }
            }
        }

        int scenicScore = 0;
        for (i = 0; i < t.length-1; i++) {
            for (int j = 0; j < t[0].length-1; j++) {
                int sScore = 1;
                int counter = 0;
                for (int k = i-1; k >= 0; k--) {
                    counter++;
                    if (t[k][j] >= t[i][j]) {
                        break;
                    }
                }
                sScore *= counter;
                counter = 0;
                for (int k = i+1; k < t.length; k++) {
                    counter++;
                    if (t[k][j] >= t[i][j]) {
                        break;
                    }
                }
                sScore *= counter;
                counter = 0;
                for (int k = j-1; k >= 0; k--) {
                    counter++;
                    if (t[i][k] >= t[i][j]) {
                        break;
                    }
                }
                sScore *= counter;
                counter = 0;
                for (int k = j+1; k < t[0].length; k++) {
                    counter++;
                    if (t[i][k] >= t[i][j]) {
                        break;
                    }
                }
                sScore *= counter;
                if (sScore > scenicScore) {
                    scenicScore = sScore;
                }
            }
        }
        
        System.out.println("Day 08 Part 1: " + countVisible);
        System.out.println("Day 08 Part 2: " + scenicScore);
    }
    
}
