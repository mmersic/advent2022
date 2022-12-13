package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
    This isn't mine.  Mostly copied from https://github.com/abnew123/aoc2022/blob/main/src/aoc2022/Day13.java
 */

public class Day13Better {
    static class NI implements Comparable<NI> {
        int val = -1;
        boolean isNumber = false;
        List<NI> list = new ArrayList<>();
        
        public NI(String toParse) {
            if (toParse.equals("[]")) {
                //do nothing.
            } else if (!toParse.startsWith("[")) {
                val = Integer.parseInt(toParse);
                isNumber = true;
            } else {
                toParse = toParse.substring(1,toParse.length()-1);
                String nextString = "";
                int depth = 0;
                for (char c : toParse.toCharArray()) {
                    if (c == ',' && depth == 0) {
                        list.add(new NI(nextString));
                        nextString = "";
                    } else {
                        depth += '[' == c ? 1 : ']' == c ? -1 : 0;
                        nextString += c;
                    }
                }
                if (!nextString.equals("")) {
                    list.add(new NI(nextString));
                }
            }
        }
        
        public int compareTo(NI right) {
            if (this.isNumber && right.isNumber) {
                return right.val - this.val;
            } else if (!this.isNumber && !right.isNumber) {
                for (int i = 0; i < Math.min(this.list.size(), right.list.size()); i++) {
                    int val = this.list.get(i).compareTo(right.list.get(i));
                    if (val != 0) { 
                        return val;
                    }
                }
                return right.list.size() - this.list.size();
            } else {
                NI n1 = this.isNumber ? new NI("[" + this.val + "]") : this;
                NI n2 = right.isNumber ? new NI( "[" + right.val + "]") : right;
                return n1.compareTo(n2);
            }
        }
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day13.class.getClassLoader().getResource("day.13.input").toURI()));
        
        int part1 = 0;
        int pair = 0;
        List<NI> niList = new ArrayList<>();
        for (int i = 0; i < input.size(); i+=3) {
            NI left = new NI(input.get(i));
            NI right = new NI(input.get(i+1));
            niList.add(left);
            niList.add(right);
            pair++;
            if (left.compareTo(right) > 0) {
                part1 += pair;
            }
        }
        
        NI two = new NI("[[2]]");
        NI six = new NI("[[6]]");
        niList.add(two);
        niList.add(six);
        
        niList.sort(Comparator.reverseOrder());
        
        int part2 = (niList.indexOf(two)+1) * (niList.indexOf(six)+1);
        
        System.out.println("day 13 part 1: " + part1);
        System.out.println("day 13 part 2: " + part2);
    }
}
