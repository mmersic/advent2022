package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day20 {
    
    static class Node {
        public long val;
        public Node next;
        public Node prev;
        public Node(long val) {
            this.val = val;
        }
        public String toString() {
            return ""+val;
        }
    }
    
    private static Node mix(List<Node> list) {
        Node zeroNode = null;
        
        int length = list.size();
        
        for (Node current : list) {
            if (current.val == 0) {
                zeroNode = current;
            } else {
                int val = (int) (current.val % (length-1));
                if (val > 0) {
                    for (int i = 0; i < val; i++) {
                        Node next = current.next;
                        Node prev = current.prev;
                        Node nextnext = current.next.next;
                        prev.next = next;
                        next.prev = prev;
                        next.next = current;
                        current.prev = next;
                        current.next = nextnext;
                        nextnext.prev = current;
                    }
                } else if (val < 0) {
                    for (int i = val; i<0; i++) {
                        Node prev = current.prev;
                        Node prevprev = prev.prev;
                        Node next = current.next;
                        prevprev.next = current;
                        current.prev = prevprev;
                        current.next = prev;
                        prev.prev = current;
                        prev.next = next;
                        next.prev = prev;
                    }
                }
            }
        }
        
        return zeroNode;
    }
    
    public static void print(Node head) {
        Node current = head;
        do {
            System.out.print(current.val + ",");
            current = current.next;
        } while(current != head);
        System.out.println();
    }
    
    public static long part1(List<String> input) {
        Node head = new Node(Integer.parseInt(input.get(0)));
        Node current = head;
        for (int i = 1; i < input.size(); i++) {
            Node n = new Node(Integer.parseInt(input.get(i)));
            n.prev = current;
            current.next = n;
            current = n;
        }
        head.prev = current;
        current.next = head;

        List<Node> list = new ArrayList<>();
        Node temp = head;
        do {
            list.add(temp);
            temp = temp.next;
        } while (temp != head);

        //print(head);
        current = mix(list);
        //print(current);
        for (int i = 0; i < 1000; i++) {
            current = current.next;
        }
        long x = current.val;
        for (int i = 0; i < 1000; i++) {
            current = current.next;
        }
        long y = current.val;
        for (int i = 0; i < 1000; i++) {
            current = current.next;
        }
        long z = current.val;

        return x + y + z;
    }
    
    public static long part2(List<String> input) {
        Node head = new Node(Integer.parseInt(input.get(0))*811589153l);
        Node current = head;
        for (int i = 1; i < input.size(); i++) {
            Node n = new Node(Integer.parseInt(input.get(i)));
            n.val *= 811589153l;
            n.prev = current;
            current.next = n;
            current = n;
        }
        head.prev = current;
        current.next = head;

        Node temp = head;
        List<Node> list = new ArrayList<>();
        do {
            list.add(temp);
            temp = temp.next;
        } while (temp != head);

        for (int i = 0; i < 10; i++) {
            current = mix(list);
        }
        for (int i = 0; i < 1000; i++) {
            current = current.next;
        }
        long x = current.val;
        for (int i = 0; i < 1000; i++) {
            current = current.next;
        }
        long y = current.val;
        for (int i = 0; i < 1000; i++) {
            current = current.next;
        }
        long z = current.val;
        
        return x + y + z;
    }
    
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day20.class.getClassLoader().getResource("day.20.input").toURI()));
        
        System.out.println("day 20 part 1: " + part1(input));
        System.out.println("day 20 part 2: " + part2(input));
    }
}
