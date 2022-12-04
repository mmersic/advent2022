package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day03 {
    
    private static int score(char c) {
        if (c <= 'Z') {
            return (c-'A') + 27;
        } else {
            return (c-'a') + 1;
        }
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day02.class.getClassLoader().getResource("day.03.input").toURI()));
        
        int totalPriority = 0;
        for (String s : input) {
            String s1 = s.substring(0, s.length()/2);
            String s2 = s.substring(s.length()/2);
            Set<Character> set1 = new HashSet<>();
            for (char c : s1.toCharArray()) {
                set1.add(c);
            }
            for (char c : s2.toCharArray()) {
                if (set1.contains(c)) {                     
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
            
            Set<Character> set1 = new HashSet<>();
            for (char c : s1.toCharArray()) {
                set1.add(c);
            }
            Set<Character> set2 = new HashSet<>();
            for (char c : s2.toCharArray()) {
                if (set1.contains(c)) {
                    set2.add(c);
                }
            }
            for (char c : s3.toCharArray()) {
                if (set2.contains(c)) {
                    totalPriority2 += score(c);
                    break;
                }
            }
        }
        
        System.out.println("day 3 part 1: " + totalPriority);
        System.out.println("day 3 part 2: " + totalPriority2);
    }
    
    
}
