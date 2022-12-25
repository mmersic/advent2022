package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 {
    
    static class Monkey {
        public Monkey(String name, long val) {
            this.name = name;
            this.number = true;
            this.val = val;
        }
        
        public Monkey(String name, String lh, String rh, char op) {
            this.name = name;
            this.lh = lh;
            this.rh = rh;
            this.op = op;
        }

        String name;

        boolean number = false;
        long val = 0;

        long lhn;
        long rhn;
        String lh;
        String rh;
        char op;
    }
    
    public static long resolvePart1(Monkey root, Map<String, Monkey> map) {
        if (root.number) {
            return root.val;
        }
        root.number = true;
        long lhs = resolvePart1(map.get(root.lh), map);
        long rhs = resolvePart1(map.get(root.rh), map);
        root.val = switch (root.op) {
            case '+' -> lhs + rhs;
            case '-' -> lhs - rhs;
            case '*' -> lhs * rhs;
            case '/' -> lhs / rhs;
            case '=' -> lhs == rhs ? 0 : lhs > rhs ? 1 : -1;
            default -> throw new RuntimeException("invalid op: " + root.op);
        };
        return root.val;
    }
    
    public static boolean checkForHumn(Monkey root, Map<String, Monkey> map) {
        if ("humn".equals(root.name)) {
            return true;
        } else if (root.number) {
            return false;
        } else {
            return checkForHumn(map.get(root.lh), map) || checkForHumn(map.get(root.rh), map);
        }
    }
    
    public static long inverse(boolean lhs, Monkey m, long value) {
        if (lhs) {
            return switch (m.op) {
                case '+' -> value - m.lhn;
                case '-' -> -value + m.lhn;
                case '*' -> value / m.lhn;
                case '/' -> m.lhn / value;
                default -> throw new RuntimeException("invalid op");
            };
        } else {
            return switch (m.op) {
                case '+' -> value - m.rhn;
                case '-' -> value + m.rhn;
                case '*' -> value / m.rhn;
                case '/' -> value * m.rhn;
                default -> throw new RuntimeException("invalid op");
            };
        }
    }
    
    public static long resolveForValue(Monkey root, Map<String, Monkey> map, long value) {
        if ("humn".equals(root.name)) {
            root.val = value;
            root.number = true;
            return root.val;
        }
        
        if (root.number) {
            return root.val;
        }
        
        if (!checkForHumn(map.get(root.lh), map)) {
            root.lhn = resolvePart1(map.get(root.lh), map);
            return resolveForValue(map.get(root.rh), map, inverse(true, root, value));
        } else if (!checkForHumn(map.get(root.rh), map)) {
            root.rhn = resolvePart1(map.get(root.rh), map);
            return resolveForValue(map.get(root.lh), map, inverse(false, root, value));
        } else {
            throw new RuntimeException("both sides have humn");
        }
    }

    public static long resolvePart2(Monkey root, Map<String, Monkey> map) {
        if (root.number) {
            return root.val;
        }
        root.number = true;
        long rhs = resolvePart1(map.get(root.rh), map);
        long lhs = resolveForValue(map.get(root.lh), map, rhs);
        root.lhn = lhs;
        root.rhn = rhs;
        root.val = switch (root.op) {
            case '+' -> lhs + rhs;
            case '-' -> lhs - rhs;
            case '*' -> lhs * rhs;
            case '/' -> lhs / rhs;
            case '=' -> lhs == rhs ? 0 : lhs > rhs ? 1 : -1;
            default -> throw new RuntimeException("invalid op: " + root.op);
        };
        return root.val;
    }

    public static long part1(List<String> input) {
        Map<String, Monkey> monkeyMap = new HashMap<>();

        for (String line : input) {
            String[] s = line.split(": | ");
            if (s.length == 2) {
                Monkey m = new Monkey(s[0], Long.parseLong(s[1]));
                monkeyMap.put(s[0], m);
            } else {
                Monkey m = new Monkey(s[0], s[1], s[3], s[2].toCharArray()[0]);
                monkeyMap.put(s[0], m);
            }
        }

        return resolvePart1(monkeyMap.get("root"), monkeyMap);
    }
    
    public static long part2(List<String> input) {
        Map<String, Monkey> monkeyMap = new HashMap<>();
        
        for (String line : input) {
            String[] s = line.split(": | ");
            if (s.length == 2) {
                Monkey m = new Monkey(s[0], Long.parseLong(s[1]));
                monkeyMap.put(s[0], m);
            } else {
                Monkey m = new Monkey(s[0], s[1], s[3], s[2].toCharArray()[0]);
                monkeyMap.put(s[0], m);
            }
        }
        monkeyMap.get("root").op = '=';
        resolvePart2(monkeyMap.get("root"), monkeyMap);
        return monkeyMap.get("humn").val;
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day21.class.getClassLoader().getResource("day.21.input").toURI()));
        
        System.out.println("day 21 part 1: " + part1(input));        
        System.out.println("day 21 part 2: " + part2(input));
    }
}
