package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Day02 {
    private static final Map<String, Integer> SCORE_P1 = new HashMap<>();
    private static final Map<String, Integer> SCORE_P2 = new HashMap<>();
    static {
        //ROCK
        SCORE_P1.put("A X", 1+3);
        SCORE_P1.put("A Y", 2+6);
        SCORE_P1.put("A Z", 3+0);

        //PAPER
        SCORE_P1.put("B X", 1+0);
        SCORE_P1.put("B Y", 2+3);
        SCORE_P1.put("B Z", 3+6);

        //SCISSORS
        SCORE_P1.put("C X", 1+6);
        SCORE_P1.put("C Y", 2+0);
        SCORE_P1.put("C Z", 3+3);

        //ROCK 
        SCORE_P2.put("A X", 3+0);
        SCORE_P2.put("A Y", 1+3);
        SCORE_P2.put("A Z", 2+6);

        //PAPER
        SCORE_P2.put("B X", 1+0);
        SCORE_P2.put("B Y", 2+3);
        SCORE_P2.put("B Z", 3+6);

        //SCISSORS
        SCORE_P2.put("C X", 2+0);
        SCORE_P2.put("C Y", 3+3);
        SCORE_P2.put("C Z", 1+6);
    }
    
    public static void main(String[] args) throws Exception {
        record Scores(int s1, int s2) {}
        Scores s = Files.readAllLines(Path.of(Day02.class.getClassLoader().getResource("day.02.input").toURI())).stream()
                .map(x -> new Scores(SCORE_P1.get(x), SCORE_P2.get(x)))
                .reduce((x,y)->new Scores(x.s1 + y.s1, x.s2 + y.s2)).get();

        System.out.println("day 2 part 1: " + s.s1);
        System.out.println("day 2 part 2: " + s.s2);
    }
}
