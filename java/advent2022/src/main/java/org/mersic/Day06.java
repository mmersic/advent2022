package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day06 {
    
    public static int scanForDistinct(char[] chars, int distinctCount) {
        int[] seen = new int[256];
        int distinct = 0;
        int len = 0;
        for (int i = 0; i < chars.length; i++) {
            if (distinct == distinctCount) {
                return i;
            }
            if (len == distinctCount) {
                char c = chars[i-distinctCount];
                seen[c]--;
                len--;
                if (seen[c] == 0) {
                    distinct--;
                }
            }
            seen[chars[i]]++;
            len++;
            if (seen[chars[i]] == 1) {
                distinct++;
            }
        }
        throw new RuntimeException((distinctCount + " chars not found"));
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day05.class.getClassLoader().getResource("day.06.input").toURI()));
        char[] chars = input.get(0).toCharArray();
        
        System.out.println("day 6 part 1: "+ scanForDistinct(chars, 4));
        System.out.println("day 6 part 2: "+ scanForDistinct(chars, 14));
        
    }
}
