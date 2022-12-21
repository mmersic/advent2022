package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {
    static class BP {
        public int bpNum;
        public int oreOreCost;
        public int clayOreCost;
        public int obsOreCost;
        public int obsClayCost;
        public int geoOreCost;
        public int geoObsCost;
        public BP(int bpNum, int oreOreCost, int clayOreCost, int obsOreCost, int obsClayCost, int geoOreCost, int geoObsCost) {
            this.bpNum = bpNum;
            this.oreOreCost = oreOreCost;
            this.clayOreCost = clayOreCost;
            this.obsOreCost = obsOreCost;
            this.obsClayCost = obsClayCost;
            this.geoOreCost = geoOreCost;
            this.geoObsCost = geoObsCost;
        }
        public String toString() {
            return "bpNum: " + bpNum + ", ore[ore cost]: " + oreOreCost + ", clay[ore cost]: " + clayOreCost + ", obs[ore cost]: " + obsOreCost + ", obs[clay cost]: " + obsClayCost + ", geo[ore cost]: " + geoOreCost + " geo[obs cost]: " + geoObsCost;
        }
    }
    
    record Resources(int ore, int clay, int obs, int geodes, int oreRobots, int clayRobots, int obsRobots, int geodeRobots) { 
        public Resources mine() {
            return new Resources(ore + oreRobots, clay + clayRobots, obs + obsRobots, geodes + geodeRobots, oreRobots, clayRobots, obsRobots, geodeRobots);
        }
        public boolean canBuildOreRobot(BP bp) {
            return bp.oreOreCost <= ore;
        }
        public Resources buildOreRobot(BP bp) {
            return new Resources((ore + oreRobots)-bp.oreOreCost, clay + clayRobots, obs + obsRobots, geodes + geodeRobots, oreRobots+1, clayRobots, obsRobots, geodeRobots);
        }
        public boolean canBuildClayRobot(BP bp) {
            return bp.clayOreCost <= ore;
        }
        public Resources buildClayRobot(BP bp) {
            return new Resources((ore + oreRobots)-bp.clayOreCost, clay + clayRobots, obs + obsRobots, geodes + geodeRobots, oreRobots, clayRobots+1, obsRobots, geodeRobots);
        }
        public boolean canBuildObsRobot(BP bp) {
            return bp.obsOreCost <= ore && bp.obsClayCost <= clay;
        }
        public Resources buildObsRobot(BP bp) {
            return new Resources((ore + oreRobots)-bp.obsOreCost, (clay + clayRobots)-bp.obsClayCost, obs + obsRobots, geodes + geodeRobots, oreRobots, clayRobots, obsRobots+1, geodeRobots);
        }
        public boolean canBuildGeodeRobot(BP bp) {
            return bp.geoOreCost <= ore && bp.geoObsCost <= obs;
        }
        public Resources buildGeodeRobot(BP bp) {
            return new Resources((ore + oreRobots)-bp.geoOreCost, (clay + clayRobots), (obs + obsRobots)-bp.geoObsCost, geodes + geodeRobots, oreRobots, clayRobots, obsRobots, geodeRobots+1);
        }
    }


    record State(int oreRobots, int clayRobots, int obsRobots, int geodeRobots, int clay, int obs, int geodes, int startTime) {}
    static Map<State, Integer> memo = new HashMap<>();
    public static int findMaxGeodes(BP bp, int startTime, int endTime, Resources resources) {
        if (startTime == endTime) {
            return resources.geodes();
        }
        
        State state = new State(resources.oreRobots(), resources.clayRobots(), resources.obsRobots(), resources.geodeRobots(), resources.clay(), resources.obs(), resources.geodes(), startTime);
        if (memo.containsKey(state)) {
            return memo.get(state);
        }
        
        int max = -1;
        if (resources.canBuildGeodeRobot(bp)) {
            max = Math.max(max, findMaxGeodes(bp, startTime + 1, endTime, resources.buildGeodeRobot(bp)));
        } else {
            if (resources.canBuildOreRobot(bp)) {
                max = Math.max(max, findMaxGeodes(bp, startTime + 1, endTime, resources.buildOreRobot(bp)));
            }
            if (resources.canBuildClayRobot(bp)) {
                max = Math.max(max, findMaxGeodes(bp, startTime + 1, endTime, resources.buildClayRobot(bp)));
            }
            if (resources.canBuildObsRobot(bp)) {
                max = Math.max(max, findMaxGeodes(bp, startTime + 1, endTime, resources.buildObsRobot(bp)));
            }
            max = Math.max(max, findMaxGeodes(bp, startTime + 1, endTime, resources.mine()));
        }        
        memo.put(state, max);

        return max;
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day19.class.getClassLoader().getResource("day.19.input").toURI()));

        List<BP> bluePrints = new ArrayList<>();
        for (String line : input) {
            String[] s = line.split("Blueprint |: Each ore robot costs | ore. Each clay robot costs | ore. Each obsidian robot costs | clay. Each geode robot costs | ore and | obsidian.");
            BP bp = new BP(Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]),Integer.parseInt(s[4]),Integer.parseInt(s[5]),Integer.parseInt(s[6]),Integer.parseInt(s[7]));
            bluePrints.add(bp);
        }
        long part1Start = System.currentTimeMillis();
        int part1 = 0;
        for (BP bp : bluePrints) {
            memo = new HashMap<>();
            int maxGeodes = findMaxGeodes(bp, 1, 24, new Resources(1, 0, 0, 0,1, 0, 0, 0));
            part1 += (bp.bpNum * maxGeodes);
        }
        long part1Finish = System.currentTimeMillis();
        int part2 = 1;
        for (int i = 0; i < 3 && i < bluePrints.size(); i++) {
            memo = new HashMap<>();
            int maxGeodes = findMaxGeodes(bluePrints.get(i), 1, 32, new Resources(1, 0, 0, 0,1, 0, 0, 0));
            part2 *= maxGeodes;
        }
        long part2Finish = System.currentTimeMillis();
        
        System.out.println("day 19 part 1: " + part1 + " in time: " + (part1Finish-part1Start) + "ms");
        System.out.println("day 19 part 2: " + part2 + " in time: " + (part2Finish-part1Finish) + "ms");
    }
}
