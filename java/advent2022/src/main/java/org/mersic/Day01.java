package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 {
    public static void main(String[] args) throws Exception {
        /* To be clear, this is just for fun. I would never write code like this for a real application! */
        List<Integer> cals = Arrays.stream(Files.readAllLines(Path.of(Day01.class.getClassLoader().getResource("day.01.input").toURI()))
                        .stream().collect(Collectors.joining(","))
                        .split(",,"))
                .map(y -> y.split(","))
                .map(z -> Arrays.stream(z).mapToInt(Integer::parseInt).sum())
                .sorted(Comparator.reverseOrder())
                .toList();

        System.out.println("part1: " + cals.get(0));
        System.out.println("part2: " + cals.stream().limit(3).reduce(0, Integer::sum));
    }
}