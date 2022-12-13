package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Day12 {
    static class Node implements Comparable<Node> {
        public List<Node> n = new ArrayList<>();
        public String id;
        public char elevation;
        public boolean start;
        public boolean end;
        public Node prev = null;
        public long distance = Integer.MAX_VALUE;

        @Override
        public int compareTo(Node o) {
            if (this.distance == o.distance) {
                return 0;
            } else {
                return this.distance < o.distance ? -1 : 1;
            }
        }
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day12.class.getClassLoader().getResource("day.12.input").toURI()));
        String[] lines = input.toArray(new String[0]);
        char[][] map = new char[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++) {
            map[i] = lines[i].toCharArray();
        }
        
        Node start = null;
        Node end   = null;
        
        Map<String, Node> graph = new HashMap<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                Node n = new Node();
                n.id = i + "." + j;
                if ('S' == map[i][j]) {
                    n.start = true;
                    n.elevation = 'a';
                    start = n;
                    n.distance = 0;
                } else if ('E' == map[i][j]) {
                    n.end = true;
                    n.elevation = 'z';
                    end = n;
                } else {
                    n.elevation = map[i][j];
                }
                graph.put(n.id, n);
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                Node n = graph.get(i + "." + j);
                if (i > 0) {
                    Node x = graph.get((i-1) + "." + j);
                    if (!n.n.contains(x) && (x.elevation <= n.elevation+1)) {
                        n.n.add(x);
                    }
                }
                if (i < map.length-1) {
                    Node x = graph.get((i+1) + "." + j);
                    if (!n.n.contains(x) && (x.elevation <= n.elevation+1)) {
                        n.n.add(x);
                    }
                }
                if (j > 0) {
                    Node x = graph.get(i + "." + (j-1));
                    if (!n.n.contains(x) && (x.elevation <= n.elevation+1)) {
                        n.n.add(x);
                    }
                }
                if (j < map[0].length-1) {
                    Node x = graph.get(i + "." + (j+1));
                    if (!n.n.contains(x) && (x.elevation <= n.elevation+1)) {
                        n.n.add(x);
                    }
                }
            }
        }
        
        List<Node> possibilities = new ArrayList<>();
        for (Node n : graph.values()) {
            if (n.elevation == 'a') {
                possibilities.add(n);
            }
        }
        
        long minDistance = Integer.MAX_VALUE;
        long part1Distance = Integer.MAX_VALUE;
        boolean part1 = false;
        for (Node n : possibilities) {
            if (n == start) {
                part1 = true;
            } else {
                part1 = false;
            }
            
            Queue<Node> Q = new PriorityQueue<>();
            for (Node x : graph.values()) {
                x.distance = Integer.MAX_VALUE;
                x.prev = null;
                if (x == n) {
                    x.distance = 0;
                }
                Q.add(x);
            }
            
            while (Q.size() > 0) {
                Node u = Q.remove();
                if (u == end) {
                    break;
                }

                for (Node v : u.n) {
                    if (v.distance > u.distance + 1) {
                        v.distance = u.distance + 1;
                        v.prev = u;
                        Q.remove(v);
                        Q.add(v);
                    }
                }
            }
            
            if (end.distance < minDistance) {
                minDistance = end.distance;
            }
            if (part1) {
                part1Distance = end.distance;
            }
        }
        
        System.out.println("day 12 part 1: " + part1Distance);
        System.out.println("day 12 part 2: " + minDistance);
    }
}
