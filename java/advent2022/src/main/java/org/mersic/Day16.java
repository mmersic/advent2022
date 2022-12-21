package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class Day16 {
    record State(String root, Set<String> openSet, int minRemaining, int other) {
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(root);
            sb.append('.');
            List<String> list = new ArrayList<>(openSet);
            Collections.sort(list);
            for (String s : list) {
                sb.append(s);
                sb.append('.');
            }
            sb.append(minRemaining);
            sb.append('.');
            sb.append(other);
            return sb.toString();
        }
    }
    
    static class Node {
        String name;
        int rate;
        List<Node> adj = new ArrayList<>();
        Map<Node,Integer> distance = new HashMap<>();
        
        public String toString() {
            return name;
        }
    }
    
    static Map<String, Integer> memo = new HashMap<>();
    public static int findMaxFlow(Node root, int min, int endTime, Set<String> openSet, int other, Node AA) {
        if (min == endTime) {
            return other > 0 ? findMaxFlow(AA, 1, endTime, openSet, other-1, AA) : 0;
        }

        State state = new State(root.name, openSet, min, other);
        if (memo.containsKey(state.toString())) {
            return memo.get(state.toString());
        }
        
        if (!openSet.contains(root.name) && root.rate > 0) {
            openSet.add(root.name);
            int openMax = root.rate*(endTime-(min)) + findMaxFlow(root,min+1, endTime, openSet, other, AA);
            openSet.remove(root.name);
            //memo.put(state.toString(), openMax); //faster not to memoize this. because there are no choices?
            return openMax;
        } else {
            int nextMax = 0;
            for (Node n : root.adj) {
                if ((endTime - min + 1) > root.distance.get(n)) {
                    nextMax = Math.max(nextMax, findMaxFlow(n, min + root.distance.get(n), endTime, openSet, other, AA));
                }
            }
            memo.put(state.toString(), nextMax);
            return nextMax;
        }
    }

    //Remove 0 rate nodes except for the start node
    public static Node sparseGraph(Map<String, Node> nodes) {
        Node sparseRoot = nodes.get("AA");
        while (true) {
            Node toRemove = null;
            for (Node n : nodes.values()) {
                if (n.rate == 0 && !"AA".equals(n.name)) {
                    toRemove = n;
                    break;
                }
            }
            if (toRemove == null) {
                break;
            }
            nodes.remove(toRemove.name);
            System.out.println("removed: " + toRemove);
            for (Node j : toRemove.adj) {
                j.adj.remove(toRemove);
                int jn = j.distance.get(toRemove);
                for (Node k : toRemove.adj) {
                    if (j != k) {
                        k.adj.remove(toRemove);
                        int kn = k.distance.get(toRemove);
                        j.adj.add(k);
                        k.adj.add(j);
                        j.distance.put(k, jn + kn);
                        k.distance.put(j, jn + kn);
                    }
                }
            }
        }
        for (Node n : nodes.values()) {
            next: while (true) {
                for (int i = 0; i < n.adj.size(); i++) {
                    for (int j = i+1; j < n.adj.size(); j++) {
                        if (n.adj.get(i) == n.adj.get(j)) {
                            n.adj.remove(j);
                            continue next;
                        }
                    }
                }
                break;
            }
        }
        return sparseRoot;
    }
    
    public static void main(String args[]) throws Exception {
        //Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        List<String> input = Files.readAllLines(Path.of(Day16.class.getClassLoader().getResource("day.16.input").toURI()));
        
        Map<String, Node> nodes = new HashMap<>();
        
        for (String line : input) {
            String[] split = line.split("Valve | has flow rate=|; tunnels lead to valves |, |; tunnel leads to valve ");
            Node n = new Node();
            n.name = split[1];
            n.rate = Integer.parseInt(split[2]);
            nodes.put(n.name, n);
        }

        for (String line : input) {
            String[] split = line.split("Valve | has flow rate=|; tunnels lead to valves |, |; tunnel leads to valve ");
            Node n = nodes.get(split[1]);
            for (int i = 3; i < split.length; i++) {
                n.adj.add(nodes.get(split[i]));
                n.distance.put(nodes.get(split[i]), 1);
            }
        }
        
        Node sparseRoot = sparseGraph(nodes);
        long startTime = System.currentTimeMillis();
        int part1 = findMaxFlow(sparseRoot,1, 30, new HashSet<>(), 0, sparseRoot);
        long finishTime = System.currentTimeMillis();
        long part1Time = (finishTime-startTime);
        memo = new HashMap<>();
        System.out.println();
        startTime = System.currentTimeMillis();
        int part2 = findMaxFlow(sparseRoot,1, 26, new HashSet<>(), 1, sparseRoot);
        finishTime = System.currentTimeMillis();
        
        //original: 937ms, 34,296ms
        //sparseGraph: 384ms, 12,001ms
        //always open sparseGraph: 55ms, 996ms
        System.out.println("day 16 part 1: " + part1 + " in time: " + part1Time + "ms");
        System.out.println("day 16 part 2: " + part2 + " in time: " + (finishTime-startTime)+ "ms");
    }


}
