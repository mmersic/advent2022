package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day24 {
    
    public static void print(Set<Character> board[][]) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].size() == 0) {
                    System.out.print('.');
                } else if (board[i][j].size() > 1) {
                    System.out.print(board[i][j].size());
                } else {
                    for (Character c : board[i][j]) {
                        System.out.print(c);
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static Set<Character>[][] simBlizzards(Set<Character> old[][]) {
        Set<Character> next[][] = new HashSet[old.length][old[0].length];
        
        for (int i = 0; i < old.length; i++) {
            for (int j = 0; j < old[0].length; j++) {
                next[i][j] = new HashSet<>();
            }
        }

        for (int i = 0; i < old.length; i++) {
            for (int j = 0; j < old[0].length; j++) {
                for (Character c : old[i][j]) {
                    switch (c) {
                        case '#' -> next[i][j].add('#');
                        case '>' -> { 
                            if (j < old[0].length-2) {
                                next[i][j+1].add('>');
                            } else if (j == old[0].length-2) {
                                next[i][1].add('>');
                            }
                        }
                        case '<' -> {
                            if (j > 1) {
                                next[i][j-1].add('<');
                            } else if (j == 1) {
                                next[i][old[0].length-2].add('<');
                            }
                        }
                        case '^' -> {
                            if (i > 1) {
                                next[i-1][j].add('^');
                            } else if (i == 1) {
                                next[old.length-2][j].add('^');
                            }
                        }
                        case 'v' -> {
                            if (i < old.length-2) {
                                next[i+1][j].add('v');
                            } else if (i == old.length-2) {
                                next[1][j].add('v');
                            }
                        }
                    }
                }
            }
        }
        return next;
    }
    
    record Posn(int x, int y) {}
    
    public static int findTarget(Set<Character>[][] board, Posn from, Posn target) {
        Set<Posn> candidates = new HashSet<>();
        candidates.add(from);
        
        int counter = 0;
        for (int i = 0; i < 2000; i++) {
            counter++;
            Set<Character>[][] next = simBlizzards(board);
            //print(board);
            Set<Posn> nextCandidates = new HashSet<>();
            for (Posn p : candidates) {
                if (p.y() < next.length-1 && next[p.y()+1][p.x()].size() == 0) {
                    nextCandidates.add(new Posn(p.x(), p.y()+1));
                }
                if (p.y() >= 1 && next[p.y()-1][p.x()].size() == 0) {
                    nextCandidates.add(new Posn(p.x(), p.y()-1));
                }
                if (p.x() < next[0].length-1 && next[p.y()][p.x()+1].size() == 0) {
                    nextCandidates.add(new Posn(p.x()+1, p.y()));
                }
                if (p.x() > 1 && next[p.y()][p.x()-1].size() == 0) {
                    nextCandidates.add(new Posn(p.x()-1, p.y()));
                }
                if (next[p.y()][p.x()].size() == 0) {
                    nextCandidates.add(p);
                }
            }
            if (nextCandidates.contains(target)) {
                break;
            } else {
                candidates = nextCandidates;
                board = next;
            }
        }
        return counter;
    }
    
    public static int findTargetPart2(Set<Character>[][] board, Posn from, Posn target) {
        Set<Posn> candidates = new HashSet<>();
        candidates.add(from);

        int counter = 0;
        for (int i = 0; i < 2000; i++) {
            counter++;
            Set<Character>[][] next = simBlizzards(board);
            //print(board);
            Set<Posn> nextCandidates = new HashSet<>();
            for (Posn p : candidates) {
                if (p.y() < next.length-1 && next[p.y()+1][p.x()].size() == 0) {
                    nextCandidates.add(new Posn(p.x(), p.y()+1));
                }
                if (p.y() >= 1 && next[p.y()-1][p.x()].size() == 0) {
                    nextCandidates.add(new Posn(p.x(), p.y()-1));
                }
                if (p.x() < next[0].length-1 && next[p.y()][p.x()+1].size() == 0) {
                    nextCandidates.add(new Posn(p.x()+1, p.y()));
                }
                if (p.x() > 1 && next[p.y()][p.x()-1].size() == 0) {
                    nextCandidates.add(new Posn(p.x()-1, p.y()));
                }
                if (next[p.y()][p.x()].size() == 0) {
                    nextCandidates.add(p);
                }
            }
            if (nextCandidates.contains(target)) {
                candidates = new HashSet<>();
                candidates.add(target);
                board = next;
                break;
            } else {
                candidates = nextCandidates;
                board = next;
            }
        }

        for (int i = 0; i < 2000; i++) {
            counter++;
            Set<Character>[][] next = simBlizzards(board);
            Set<Posn> nextCandidates = new HashSet<>();
            for (Posn p : candidates) {
                if (p.y() < next.length-1 && next[p.y()+1][p.x()].size() == 0) {
                    nextCandidates.add(new Posn(p.x(), p.y()+1));
                }
                if (p.y() >= 1 && next[p.y()-1][p.x()].size() == 0) {
                    nextCandidates.add(new Posn(p.x(), p.y()-1));
                }
                if (p.x() < next[0].length-1 && next[p.y()][p.x()+1].size() == 0) {
                    nextCandidates.add(new Posn(p.x()+1, p.y()));
                }
                if (p.x() > 1 && next[p.y()][p.x()-1].size() == 0) {
                    nextCandidates.add(new Posn(p.x()-1, p.y()));
                }
                if (next[p.y()][p.x()].size() == 0) {
                    nextCandidates.add(p);
                }
            }
            if (nextCandidates.contains(from)) {
                candidates = new HashSet<>();
                candidates.add(from);
                board = next;
                break;
            } else {
                candidates = nextCandidates;
                board = next;
            }
        }

        for (int i = 0; i < 2000; i++) {
            counter++;
            Set<Character>[][] next = simBlizzards(board);
            Set<Posn> nextCandidates = new HashSet<>();
            for (Posn p : candidates) {
                if (p.y() < next.length-1 && next[p.y()+1][p.x()].size() == 0) {
                    nextCandidates.add(new Posn(p.x(), p.y()+1));
                }
                if (p.y() >= 1 && next[p.y()-1][p.x()].size() == 0) {
                    nextCandidates.add(new Posn(p.x(), p.y()-1));
                }
                if (p.x() < next[0].length-1 && next[p.y()][p.x()+1].size() == 0) {
                    nextCandidates.add(new Posn(p.x()+1, p.y()));
                }
                if (p.x() > 1 && next[p.y()][p.x()-1].size() == 0) {
                    nextCandidates.add(new Posn(p.x()-1, p.y()));
                }
                if (next[p.y()][p.x()].size() == 0) {
                    nextCandidates.add(p);
                }
            }
            if (nextCandidates.contains(target)) {
                candidates = new HashSet<>();
                candidates.add(target);
                break;
            } else {
                candidates = nextCandidates;
                board = next;
            }
        }        
        
        return counter;
    }        
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day24.class.getClassLoader().getResource("day.24.input").toURI()));
        
        Set<Character> board[][] = new HashSet[input.size()][input.get(0).length()];
        
        for (int i = 0; i < input.size(); i++) {
            char[] chars = input.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                Set<Character> S = new HashSet<>();
                S.add(chars[j]);
                board[i][j] = S;
            }
        }
        
        Posn from = new Posn(input.get(0).indexOf('.'), 0);
        Posn target = new Posn(input.get(input.size()-1).indexOf('.'), input.size()-1);
        int part1 = findTarget(board, from, target);
        int part2 = findTargetPart2(board, from, target);
        
        System.out.println("day 24 part 1: " + part1);
        
        //961 is too high
        System.out.println("day 24 part 2: " + part2);
    }
}
