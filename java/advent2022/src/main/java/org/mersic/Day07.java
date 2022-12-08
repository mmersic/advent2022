package org.mersic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class File {
    public File parent = null;
    public List<File> files = new ArrayList<>();
    public String name;
    public boolean dir;
    public int size;
    
    public void addDir(String name) {
        if (this.containsDir(name)) {
            return;
        }
        File f = new File();
        f.name = name;
        f.dir = true;
        f.parent = this;
        files.add(f);
    }
    
    public boolean containsDir(String name) {
        for (File f : files) {
            if (f.dir && f.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public void addFile(String name, int size) {
        if (this.containsFile(name)) {
            return;
        }
        File f = new File();
        f.name = name;
        f.dir = false;
        f.size = size;
        this.size += size;
        f.parent = this;
        File current = parent;
        while (current != null) {
            current.size += size;
            current = current.parent;
        }
        files.add(f);
    }
    
    public boolean containsFile(String name) {
        for (File f : files) {
            if (!f.dir && f.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public File getDir(String name) {
        for (File f : files) {
            if (f.dir && f.name.equals(name)) {
                return f;
            }
        }
        return null;
    }
}


public class Day07 {
    public static void main(String args[]) throws Exception {
        List<String> input = Files.readAllLines(Path.of(Day07.class.getClassLoader().getResource("day.07.input").toURI()));
        
        File root = new File();
        root.name = "/";
        root.dir = true;

        File current = root;
        
        for (int i = 1; i < input.size(); i++) {
            String[] l = input.get(i).split(" ");
            
            if (l[1].equals("ls")) {
                while (i+1 < input.size() && !input.get(i+1).startsWith("$")) {
                    i++;
                    String[] line = input.get(i).split(" ");
                    if (line[0].startsWith("dir")) {
                        current.addDir(line[1]);
                    } else {
                        current.addFile(line[1], Integer.parseInt(line[0]));
                    }
                }
            } else if (l[1].equals("cd")) {
                if (l[2].equals("..")) {
                    current = current.parent;
                } else if (l[2].equals("/")) {
                    current = root;
                } else {
                    current = current.getDir(l[2]);
                }
            }
        }

        int part1 = traverse(root, 0, 100000);
        System.out.println("day 7 part 1: " + part1);
        
        int available = 70000000 - root.size;
        int needed = 30000000 - available;
        int part2 = traverse2(root, 1000000000, needed);
        System.out.println("day 7 part 2: " + part2);
    }
    
    private static int traverse(File root, int acc, int maxSize) {
        if (root.size < maxSize) {
            acc += root.size;
        } 
        
        for (File f : root.files) {
            if (f.dir) {
                acc = traverse(f, acc, maxSize);
            }
        }
        
        return acc;
    }

    private static int traverse2(File root, int smallest, int needed) {
        if (root.size > needed && root.size < smallest) {
            smallest = root.size;
        }

        for (File f : root.files) {
            if (f.dir) {
                smallest = traverse2(f, smallest, needed);
            }
        }
        
        return smallest;
    }    
}
