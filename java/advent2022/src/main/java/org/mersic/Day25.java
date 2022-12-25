package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day25 {
    public static long toDecimal(String in) {
        char[] c = in.toCharArray();
        long sum = 0;
        for (int i = 0; i < c.length; i++) {
            long D = switch (c[i]) {
                case '=' -> -2;
                case '-' -> -1;
                case '0' -> 0;
                case '1' -> 1;
                case '2' -> 2;
                default -> throw new IllegalStateException("Unexpected value: " + c[i]);
            };
            sum += D * Math.pow(5, c.length-(i+1));
        }
        return sum;
    }
    
    public static String toSNAFU(long val) {
        long X[][] = new long[27][5];
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < 5; j++) {
                X[i][j] = (long) Math.pow(5, i) * (j-2);
            }
        }

        String snafu = "";
        long target = 0;
        int maxExp = 27;
        while (val != target) {
            long min = Long.MAX_VALUE;
            int minJ = -1;
            if (maxExp == 27) {
                int minI = -1;
                for (int i = 0; i < maxExp; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (Math.abs((target + X[i][j]) - val) < min) {
                            minJ = j;
                            minI = i;
                            min = Math.abs((target + X[i][j]) - val);
                        }
                    }
                }
                maxExp = minI-1;
            } else {
                for (int j = 0; j < 5; j++) {
                    if (Math.abs((target + X[maxExp][j]) - val) < min) {
                        minJ = j;
                        min = Math.abs((target + X[maxExp][j]) - val);
                    }
                }
                maxExp--;
            }
            target += X[maxExp+1][minJ];
            snafu += switch (minJ) {
                case 0 -> "=";
                case 1 -> "-";
                case 2  -> "0";
                case 3  -> "1";
                case 4  -> "2";
                default -> throw new IllegalStateException("Unexpected j: " + minJ);
            };
            if (val == target) {
                for (int j = 0; j < maxExp+1; j++) {
                    snafu += "0";
                }
            }
        }
        
        return snafu;
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day25.class.getClassLoader().getResource("day.25.input").toURI()));

        long sum = 0;
        for (String line : input) {
            sum += toDecimal(line);
        }
        
        String part1 = toSNAFU(sum);
        
        System.out.println("day 25 part 1: " + part1);
        System.out.println("day 25 part 2: " );
    }
}
