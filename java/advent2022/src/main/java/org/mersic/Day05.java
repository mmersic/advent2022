package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day05 {
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day05.class.getClassLoader().getResource("day.05.input").toURI()));
        
        List<List<Character>> stacks = new ArrayList<>();
        
        String f = input.get(0);
        for (int j = 0; j < (f.length()+1)/4; j++) {
            stacks.add(new ArrayList<>());
        }
        
        int i = 0;
        for (; i < input.size(); i++) {
            String line = input.get(i);
            if (!line.contains("[")) {
                break;
            }
            int index = -1;
            int last = -1;
            while (true) {
                index = line.indexOf("[", last);
                if (index >= 0) {
                    char c = line.charAt(index + 1);
                    stacks.get(index/4).add(c);
                    last = index + 1;
                } else {
                    break;
                }
            }
        }
        
        i+=2; //blank line
        
        List<List<Character>> stacks2 = new ArrayList<>();
        for (List<Character> s : stacks) {
            stacks2.add(new ArrayList<>(s));
        }
        
        int starting = i;
        for (; i < input.size(); i++) {
            String line = input.get(i);
            String[] p = line.split(" ");
            int count = Integer.parseInt(p[1]);
            List<Character> from  = stacks.get(Integer.parseInt(p[3])-1);
            List<Character> to    = stacks.get(Integer.parseInt(p[5])-1);
            
            for (int j = 0; j < count; j++) {
                Character c = from.remove(0);
                to.add(0,c);
            }
        }

        i = starting;
        for (; i < input.size(); i++) {
            String line = input.get(i);

            String[] p = line.split(" ");
            int count = Integer.parseInt(p[1]);
            List<Character> from  = stacks2.get(Integer.parseInt(p[3])-1);
            List<Character> to    = stacks2.get(Integer.parseInt(p[5])-1);
            List<Character> temp = new ArrayList<>();
            
            for (int j = 0; j < count; j++) {
                Character c = from.remove(0);
                temp.add(c);
            }
            
            for (int j = temp.size()-1; j >= 0; j--) {
                to.add(0, temp.get(j));
            }
        }        
        
        String result = "";
        
        for (List<Character> s : stacks) {
            result += s.get(0);
        }
        
        String result2 = "";
        for (List<Character> s : stacks2) {
            result2 += s.get(0);
        }

        System.out.println("day 5 part 1: " + result);

        System.out.println("day 5 part 2: " + result2);
    }
}
