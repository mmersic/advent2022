package org.mersic;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

class Monkey {
    public List<Long> items = new ArrayList<>();
    public Function<Long, Long> operation;
    public Predicate<Long> predicate;
    public int trueMonkey;
    public int falseMonkey;
    public int inspectCount = 0;
}

public class Day11 {
    public static void main (String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day11.class.getClassLoader().getResource("day.11.input").toURI()));
        List<Monkey> monkeys = new ArrayList<>();
        
        long X = 1;
        
        for (int i = 1; i< input.size();) {
            String l1_line = input.get(i).replace(",", "");
            String[] l0 = l1_line.split(" ");
            Monkey m = new Monkey();
            for (int j = 4; j < l0.length; j++) {
                m.items.add(Long.parseLong(l0[j]));
            }
            String[] l1 = input.get(i+1).split(" ");
            String op = l1[6];
            if (l1[7].equals("old")) {
                if ("+".equals(op)) {
                    m.operation = (x) -> x + x;
                } else if ("*".equals(op)) {
                    m.operation = (x) -> x * x;
                }
            } else {
                long val = Long.parseLong(l1[7]);
                if ("+".equals(op)) {
                    m.operation = (x) -> x + val;
                } else if ("*".equals(op)) {
                    m.operation = (x) -> x * val;
                }
            }
            String[] l2 = input.get(i+2).split(" ");
            m.predicate = (x) -> x % Long.parseLong(l2[5]) == 0;
            X *= Long.parseLong(l2[5]);
            String[] l3 = input.get(i+3).split(" ");
            String[] l4 = input.get(i+4).split(" ");
            m.trueMonkey = Integer.parseInt(l3[9]);
            m.falseMonkey = Integer.parseInt(l4[9]);
            
            i += 7;
            monkeys.add(m);
        }
        
        for (int i = 0; i < 10000; i++) {
            for (Monkey m : monkeys) {
                while (m.items.size() > 0) { 
                    m.inspectCount++;
                    long item = m.items.remove(0);
                    long newItemWorry = (m.operation.apply(item)) % X;
                    if (m.predicate.test(newItemWorry)) {
                        monkeys.get(m.trueMonkey).items.add(newItemWorry);
                    } else {
                        monkeys.get(m.falseMonkey).items.add(newItemWorry);
                    }                    
                }
            }
        }
        
        List<Integer> inspects = new ArrayList<>();
        for (Monkey m : monkeys) {
            inspects.add(m.inspectCount);
        }
        inspects = inspects.stream().sorted(Comparator.reverseOrder()).toList();
        
        System.out.println("day 11 part 1: " + new BigInteger(String.valueOf(inspects.get(0))).multiply(new BigInteger(String.valueOf(inspects.get(1)))));
        System.out.println("day 11 part 2: ");
    }
}
