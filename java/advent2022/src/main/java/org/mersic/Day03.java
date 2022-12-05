package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day03 {
    
    private static int score(char c) {
        if (c <= 'Z') {
            return (c-'A') + 27;
        } else {
            return (c-'a') + 1;
        }
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day03.class.getClassLoader().getResource("day.03.input").toURI()));
        
        int totalPriority = 0;
        for (String s : input) {
            String s1 = s.substring(0, s.length()/2);
            String s2 = s.substring(s.length()/2);
            for (char c : s1.toCharArray()) {
                if (s2.indexOf(c) >= 0) {
                    totalPriority += score(c);
                    break;
                }
            }
        }
        
        int totalPriority2 = 0;
        for (int i = 0; i < input.size(); i+=3) {
            String s1 = input.get(i);
            String s2 = input.get(i+1);
            String s3 = input.get(i+2);

            for (char c : s1.toCharArray()) {
                if (s2.indexOf(c) >= 0 && s3.indexOf(c) >= 0) {
                    totalPriority2 += score(c);
                    break;
                }
            }
        }
        
        System.out.println("day 3 part 1: " + totalPriority);
        System.out.println("day 3 part 2: " + totalPriority2);
    }
    
    
}
