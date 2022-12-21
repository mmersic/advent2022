package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day17 {
    static int[][] wideBar = new int[][] { {1, 1, 1, 1 } };
    static int[][] cross   = new int[][] { {0, 1, 0 },
                                           {1, 1, 1 },
                                           {0, 1, 0 } };
    static int[][] backEl  = new int [][] { {1, 1, 1 },
                                            {0, 0, 1 },
                                            {0, 0, 1 } };
    static int[][] tallBar = new int [][] { {1},
                                            {1},
                                            {1},
                                            {1} };
    static int[][] box     = new int [][] { {1, 1},
                                            {1, 1} };
    static public int[][] piece(int p) {
        return switch (p) {
            case 0 -> wideBar;
            case 1 -> cross;
            case 2 -> backEl;
            case 3 -> tallBar;
            case 4 -> box;
            default -> throw new IllegalStateException("Unexpected value: " + p);
        };
    }
    
    private static class State {
        public int jet;
        public int top;
    }

//    private static long jPrevTop = 0;
//    private static long jPrevRock = 0;
//    private static long tPrevTop = 0;
//    private static long tPrevRock = 0;
    private static void fall(int[][] board, char[] jets, State state, int[][] piece) {
        int x = 2;
        int y = state.top + 3; //bottom left coord of new piece
        int turn = 0;
//        if ((rockCount >= 3466 && rockCount <= (3466+144))||(rockCount >= 5201 && rockCount <=(5201+144))) {
//            System.out.println("[T]: top is: "+ state.top + " diff: " + (state.top-tPrevTop) + " rock: " + (rockCount) + " diff: "+ (rockCount-tPrevRock));
//            tPrevTop = state.top;
//            tPrevRock = rockCount;
//        }
        stop: while (true) {
            if (turn == 0) { //jet
                int newX = x;
                if (jets[state.jet++%jets.length] == '<') {
                    newX--;
                } else {
                    newX++;
                }
//                if (state.jet%jets.length == 0) {
//                    //System.out.println("[JJJJJJJ]: top is: "+ state.top + " diff: " + (state.top-jPrevTop) + " rock: " + rockCount + " diff: "+ (rockCount-jPrevRock));
//                    jPrevTop = state.top;
//                    jPrevRock = rockCount;
//                }
                if (newX >= 0 && newX+piece[0].length <= (board[0].length)) {
                    for (int i = 0; i < piece.length; i++) {
                        for (int j = 0; j < piece[i].length; j++) {
                            if (board[y+i][j+newX] == 1 && piece[i][j] == 1) {
                                //blocked, can't move.
                                turn = ((turn + 1) % 2);
                                continue stop;
                            }
                        }
                    }
                    x = newX;
                } else {
                    //do nothing
                }
            } else if (turn == 1) {//fall
                int newY = y-1;
                if (newY < 0) {
                    break;
                }
                for (int i = 0; i < piece.length; i++) {
                    for (int j = 0; j < piece[i].length; j++) {
                        if (board[newY+i][j+x] == 1 && piece[i][j] == 1) {
                            break stop;
                        }
                    }
                }
                y = newY;
            }
            turn = ((turn + 1) % 2);
        }
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] == 1) 
                    board[y+i][x+j] = piece[i][j];
            }
        }
        state.top = Math.max(state.top, y + piece.length);
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day17.class.getClassLoader().getResource("day.17.input").toURI()));
        
        char[] jets = input.get(0).toCharArray();
        State state = new State();
        int[][] board = new int[5000][7];
        for (int rock = 0; rock < 2022; rock++) {
                fall(board, jets, state, piece(rock%5));
                //print(board, state.top, -20);
        }
        //sample should be 3068.
        //part1: 3100
        long part1 = state.top;
        long part2 = 1540634005751l;
        //Height of 1731 is 2646
        //Then the period is 1735 with height 2673
        //Remaining rocks are (1000000000000-1731)%1735 == 144, with height 230
        //Height of the 1735s is ((1000000000000-1731)/1735) * 2673 = 576,368,875 * 2673 = 1,540,634,002,875
        //Finally, 2646 + 1,540,634,002,875 + 230 = 1,540,634,005,751
        //1540634005751 is correct
        //1540634005529 nope.
        //1539792387543 too low.
        //rock 1731, height 2646
        System.out.println("day 17 part 1: " + part1);
        System.out.println("day 17 part 2: " + part2);
    }
    
    public static void print(int[][] board, int top, int fromTop) {
        System.out.println();
        for (int i = top+2; i >= top-fromTop; i--) {
            System.out.print("|");
            for (int j = 0; j < board[0].length; j++) {
                switch (board[i][j]) {
                    case 0 -> System.out.print(".");
                    case 1 -> System.out.print("#");
                    default -> throw new IllegalStateException("invalid board state");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("---------");
    }

}
