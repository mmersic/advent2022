package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day22 {

    static final int RIGHT = 0;
    static final int DOWN  = 1;
    static final int LEFT  = 2;
    static final int UP    = 3;
    
    record Posn(char face, int x, int y) {}
    
    static class Node {
        public char val;
        public Node[] n = new Node[4];
        public Posn p;
        
        public Node(char c, Posn p) {
            this.p = p;
            this.val = c;
        }
    }
    
    public static Node createGraph(List<String> input) {
        int maxLength = -1;
        for (int i = 0; i < input.size()-2; i++) {
            if (input.get(i).length() > maxLength) {
                maxLength = input.get(i).length();
            }
        }
        Node[][] graph = new Node[input.size()-1][maxLength+1];

        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                graph[i][j] = new Node(' ', new Posn(' ',j+1, i+1));
            }
        }
        
        for (int i = 0; i < input.size()-2; i++) {
            char[] line = input.get(i).toCharArray();
            for (int j = 0; j < maxLength; j++) {
                if (j < line.length) {
                    graph[i][j].val = line[j];
                }
            }
        }
        
        Node root = null;
        for (int i = 0; i < graph.length; i++) {
            Node left = null;
            for (int j = 0; j < graph[0].length; j++) {
                if (graph[i][j].val == ' ' && left != null) {
                    graph[i][j-1].n[RIGHT] = left;
                    left.n[LEFT] = graph[i][j-1];
                    break;
                } else if (graph[i][j].val == ' ') {
                    continue;
                } else if (left == null) {
                    left  = graph[i][j];
                    if (i == 0) {
                        root = left;
                    }
                } else {
                    graph[i][j-1].n[RIGHT] = graph[i][j];
                    graph[i][j].n[LEFT]    = graph[i][j-1];
                }
            }
        }

        for (int i = 0; i < graph[0].length; i++) {
            Node top = null;
            for (int j = 0; j < graph.length; j++) {
                if (graph[j][i].val == ' ' && top != null) {
                    graph[j-1][i].n[DOWN] = top;
                    top.n[UP] = graph[j-1][i];
                    break;
                } else if (graph[j][i].val == ' ') {
                    continue;
                } else if (top == null) {
                    top = graph[j][i];
                } else {
                    graph[j-1][i].n[DOWN] = graph[j][i];
                    graph[j][i].n[UP]    = graph[j-1][i];
                }
            }
        }
        
        return root;
    }
    
    public static Node[][] getNodes(List<String> input, char face, int x, int y, int xlen, int ylen) {
        Node G[][] = new Node[ylen][xlen];
        for (int i = 0; i < ylen; i++) {
            char[] line = input.get(y+i).toCharArray();
            for (int j = 0; j < xlen; j++) {
                G[i][j] = new Node(line[x+j], new Posn(face,x+j+1, y+i+1));
            }
        }
        
        for (int i = 0; i < ylen; i++) {
            G[i][0].n[RIGHT] = G[i][1];
            G[i][xlen-1].n[LEFT] = G[i][xlen-2];
            for (int j = 1; j < xlen-1; j++) {
                G[i][j].n[RIGHT] = G[i][j+1];
                G[i][j].n[LEFT]  = G[i][j-1];
            }
        }

        for (int i = 0; i < ylen; i++) {
            G[0][i].n[DOWN] = G[1][i];
            G[ylen-1][i].n[UP] = G[ylen-2][i];
            for (int j = 1; j < xlen-1; j++) {
                G[j][i].n[DOWN] = G[j+1][i];
                G[j][i].n[UP]  = G[j-1][i];
            }
        }
        
        return G;
    }
    
    public static void connectEdge(int edge, Node[][] G, Node[][]H) {
        switch (edge) {
            case 0 -> { //right edge of G to left edge of H
                int xLen = G[0].length-1;
                for (int i = 0; i < G.length; i++) {
                    if (G[i][xLen].n[RIGHT] != null) {
                        throw new RuntimeException("G[" + i + "][" + xLen + "] should be null");
                    }
                    if (H[i][0].n[LEFT] != null) {
                        throw new RuntimeException("H[" + i + "][0] should be null");
                    }
                    G[i][xLen].n[RIGHT] = H[i][0];
                    H[i][0].n[LEFT] = G[i][xLen];
                }
            }
            case 1 -> { //right edge of G, to bottom edge of H
                int xLen = G[0].length-1;
                int yLen = H.length-1;
                for (int i = 0; i < G.length; i++) {
                    if (G[i][xLen].n[RIGHT] != null) {
                        throw new RuntimeException("G[" + i + "][" + xLen + "] should be null");
                    }
                    if (H[yLen][i].n[DOWN] != null) {
                        throw new RuntimeException("H[" + yLen + "][" + i + "] should be null");
                    }                    
                    G[i][xLen].n[RIGHT] = H[yLen][i];
                    H[yLen][i].n[DOWN] = G[i][xLen]; 
                }
            }
            case 2 -> { //right edge of G, to inverted right edge of H
                int xLen = G[0].length-1;
                int yLen = G.length-1;
                for (int i = 0; i < G.length; i++) {
                    if (G[i][xLen].n[RIGHT] != null) {
                        throw new RuntimeException("G[" + i + "][" + xLen + "] should be null");
                    }
                    if (H[yLen-i][xLen].n[RIGHT] != null) {
                        throw new RuntimeException("H[" + (yLen-i) + "][" + 0 + "] should be null");
                    }
                    G[i][xLen].n[RIGHT] = H[yLen-i][xLen];
                    H[yLen-i][xLen].n[RIGHT] = G[i][xLen]; 
                }
            }
            case 3 -> { //top of G to left of H
                for (int i = 0; i < G[0].length; i++) {
                    if (G[0][i].n[UP] != null) {
                        throw new RuntimeException("G[" + 0 + "][" + i + "] should be null");
                    }
                    if (H[i][0].n[LEFT] != null) {
                        throw new RuntimeException("H[" + i + "][" + 0 + "] should be null");
                    }
                    G[0][i].n[UP] = H[i][0];
                    H[i][0].n[LEFT] = G[0][i];
                }
            }
            case 4 -> { //bottom of G to top of H
                int yLen = G.length-1;
                for (int i = 0; i < G[0].length; i++) {
                    if (G[yLen][i].n[DOWN] != null) {
                        throw new RuntimeException("G[" + yLen + "][" + i + "] should be null");
                    }
                    if (H[0][i].n[UP] != null) {
                        throw new RuntimeException("H[" + 0 + "][" + i + "] should be null");
                    }
                    G[yLen][i].n[DOWN] = H[0][i];
                    H[0][i].n[UP] = G[yLen][i];
                }
            }
            case 5 -> {
                int yLen = G.length-1;
                int xLen = G[0].length-1;
                for (int i = 0; i < G.length; i++) {
                    if (G[i][0].n[LEFT] != null) {
                        throw new RuntimeException("G[" + i + "][" + 0 + "] should be null");
                    }
                    if (H[yLen-i][0].n[LEFT] != null) {
                        throw new RuntimeException("H[" + (yLen-i) + "][" + xLen + "] should be null");
                    }
                    G[i][0].n[LEFT] = H[yLen-i][0];
                    H[yLen-i][0].n[LEFT] = G[i][0];
                }
            }
        }
    }
    
    public static Node createGraphPart2(List<String> input) {
        Node BACK[][] = getNodes(input, 'K',50, 0, 50, 50);
        Node RIGHT[][] = getNodes(input, 'R',100, 0, 50, 50);
        Node TOP[][] = getNodes(input, 'P',50, 50, 50, 50);
        Node LEFT[][] = getNodes(input, 'L',0, 100, 50, 50);
        Node FRONT[][] = getNodes(input, 'F',50, 100, 50, 50);
        Node BOTTOM[][] = getNodes(input, 'M',0, 150, 50, 50);
        
        connectEdge(0, BACK, RIGHT);   //right of G to left of H
        connectEdge(0, LEFT, FRONT);   //right of G to left of H
        connectEdge(1, TOP, RIGHT);    //right of G to bottom of H
        connectEdge(1, BOTTOM, FRONT); //right of G to bottom of H
        connectEdge(2, FRONT, RIGHT);  //right of G to inverted right of H
        connectEdge(3, LEFT, TOP);     //top of G to left of H
        connectEdge(3, BACK, BOTTOM);  //top of G to left of H
        connectEdge(4, BACK, TOP);     //bottom of G to top of H
        connectEdge(4, TOP, FRONT);    //bottom of G to top of H
        connectEdge(4, LEFT, BOTTOM);  //bottom of G to top of H
        connectEdge(4, BOTTOM, RIGHT); //bottom of G to top of H
        connectEdge(5, BACK, LEFT);    //left of G to inverted left of H
        
        return BACK[0][0];
    }
    
    public static int newFace(Node from, Node to, int face) {
        if (from.p.face() == ' ') {
            return face;
        }
        /* case 0 */
        //do nothing
        
        /* case 1 */
        //RIGHT to TOP
        if (from.p.face() == 'R' && to.p.face() == 'P') {
            return LEFT;
        }
        //TOP to RIGHT
        if (from.p.face() == 'P' && to.p.face() == 'R') {
            return UP;
        }
        //FRONT to BOTTOM
        if (from.p.face() == 'F' && to.p.face() == 'M') {
            return LEFT;
        }
        //BOTTOM to FRONT
        if (from.p.face() == 'M' && to.p.face() == 'F') {
            return UP;
        }
        
        /* case 2 */
        //RIGHT to FRONT
        if (from.p.face() == 'R' && to.p.face() == 'F') {
            return LEFT;
        }
        //FRONT to RIGHT 
        if (from.p.face() == 'F' && to.p.face() == 'R') {
            return LEFT;
        }
        
        /* case 3 */
        //LEFT to TOP
        if (from.p.face() == 'L' && to.p.face() == 'P') {
            return RIGHT;
        }
        //TOP to LEFT
        if (from.p.face() == 'P' && to.p.face() == 'L') {
            return DOWN;
        }
        //BACK to BOTTOM
        if (from.p.face() == 'K' && to.p.face() == 'M') {
            return RIGHT;
        }
        //BOTTOM to BACK
        if (from.p.face() == 'M' && to.p.face() == 'K') {
            return DOWN;
        }
        
        /* case 4 */
        //do nothing
        
        /* case 5 */
        //BACK to LEFT
        if (from.p.face() == 'K' && to.p.face() == 'L') {
            return RIGHT;
        }
        //LEFT to BACK
        if (from.p.face() == 'L' && to.p.face() == 'K') {
            return RIGHT;
        }
        
        return face;
    }
    
    public static int traverse(Node root, String ins) {
        String[] num = ins.split("R|L");
        String[] dir = ins.split("\\d+");
        int face = RIGHT;
        
        Node current = root;
        for (int i = 0; i < num.length; i++) {
            int X = Integer.parseInt(num[i]);
            for (int j = 0; j < X; j++) {
                if (current.n[face].val == '.') {
                    int newFace = newFace(current, current.n[face], face);
                    current = current.n[face];
                    face = newFace;
                }
            }
            if (i+1 < dir.length) {
                if (dir[i+1].equals("R")) {
                    face = face == 3 ? 0 : face+1;
                } else {
                    face = face == 0 ? 3 : face-1;
                }
            }
        }
        return (current.p.x()*4)+(current.p.y()*1000)+face;
    }
    
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day22.class.getClassLoader().getResource("day.22.input").toURI()));
        
        Node G = createGraph(input);
        int part1 = traverse(G, input.get(input.size()-1));
        
        G = createGraphPart2(input);
        int part2 = traverse(G, input.get(input.size()-1));
        
        System.out.println("day 22 part 1: " + part1);
        System.out.println("day 22 part 2: " + part2);
    }
}
