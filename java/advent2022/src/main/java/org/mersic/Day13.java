package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day13 {
    static class NI {
        boolean isNumber = false;
        List<NI> list = new ArrayList<>();
        int val = -1;
        
        public NI() {}

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
        
        public String toString() {
            if (isNumber) {
                return "" + val;
            } else {
                if (list.size() == 0) {
                    return "[]";
                }
                String x = "[";
                for (NI n : list) {
                    x += n + ",";
                }
                return x.substring(0, x.length()-1) + "]";
            }
        }
    }

    public static boolean compare(NI left, NI right) {
        if (left.isNumber && right.isNumber) {
            if (left.val < right.val) {
                throw new ArithmeticException();
            } else if (right.val < left.val) {
                return false;
            } else {
                return true;
            }
        } else if (!left.isNumber && !right.isNumber) {
            int i = 0;
            if (left.list.size() == 0 && right.list.size() != 0) {
                throw new ArithmeticException();
            }
            if (right.list.size() == 0 && left.list.size() != 0) {
                return false;
            }
            if (left.list.size() == 0 && right.list.size() == 0) {
                return true;
            }
            for (; i < left.list.size() && i < right.list.size(); i++) {
                if (!compare(left.list.get(i), right.list.get(i))) {
                    return false;
                }
            }
            if (i == left.list.size() && i == right.list.size()) {
                return true;
            } if (i < left.list.size() && i == right.list.size()) {
                return false;
            } else {
                throw new ArithmeticException();
            }
        } else if (left.isNumber) {
            if (right.list.size() == 0) {
                return false;
            }
            NI box = new NI();
            box.list.add(left);
            return compare(box, right);
        } else if (right.isNumber) {
            if (left.list.size() == 0) {
                throw new ArithmeticException();
            }
            NI box = new NI();
            box.list.add(right);
            return compare(left, box);
        } else {
            throw new RuntimeException("should not be here");
        }
    }

    public static boolean compareNI(NI left, NI right) {
        try {
            return compare(left, right);
        } catch (ArithmeticException e) {
            return true;
        }
    }

    public static boolean compare(String l1, String l2) {
        NI left = new NI(l1);
        NI right = new NI(l2);

        return compareNI(left, right);
    }


    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day13.class.getClassLoader().getResource("day.13.input").toURI()));

        int pair = 0;
        int part1  = 0;
        for (int i = 0; i < input.size(); i+=3) {
            String l1 = input.get(i);
            String l2 = input.get(i+1);
            pair++;
            if (compare(l1, l2)) {
                part1 += pair;
            }
        }

        List<NI> list = new ArrayList<>();
        for (int i = 0; i < input.size(); i+=3) {
            list.add(new NI(input.get(i)));
            list.add(new NI(input.get(i+1)));
        }

        NI two = new NI("[[2]]");
        NI six = new NI("[[6]]");

        list.add(two);
        list.add(six);

        for (int i = 0; i < list.size(); i++) {
            for (int j = i+1; j < list.size(); j++) {
                if (!compareNI(list.get(i), list.get(j))) {
                    NI temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }

        int ti = list.indexOf(two)+1;
        int si = list.indexOf(six)+1;
        int part2 = ti * si;

        System.out.println("day 13 part 1: " + part1);
        System.out.println("day 13 part 2: " + part2);
    }
}
