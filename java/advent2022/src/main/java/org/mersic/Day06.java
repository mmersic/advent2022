package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day06 {
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day05.class.getClassLoader().getResource("day.06.input").toURI()));
        char[] chars = input.get(0).toCharArray();

        for (int i = 3; i < chars.length; i++) {
            Set<Character> set = new HashSet<>();
            for (int j = 0; j < 4; j++) {
                set.add(chars[i-j]);
            }
            if (set.size() == 4) {
                System.out.println("day 6 part 1: "+ (i+ 1));
                break;
            }
        }

        for (int i = 13; i < chars.length; i++) {
            Set<Character> set = new HashSet<>();
            for (int j = 0; j < 14; j++) {
                set.add(chars[i-j]);
            }
            if (set.size() == 14) {
                System.out.println("day 6 part 2: "+ (i+ 1));
                break;
            }
        }
        
    }
}
