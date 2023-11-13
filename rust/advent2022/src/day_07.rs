use std::str::FromStr;

#[derive(Debug)]
struct File {
    parent_id : Option<usize>,
    name : String,
    dir : bool,
    files : Vec<usize>,
    size : usize
}

#[derive(Debug)]
struct Tree {
    root_id : usize,
    nodes : Vec<File>
}

impl Tree {
    pub fn new() -> Tree {
        let mut tree = Tree {
            root_id: 0,
            nodes: vec![],
        };
        
        let root = File {
            parent_id: None,
            name: "/".to_string(),
            dir: true,
            files: vec![],
            size: 0,
        };
        
        tree.nodes.push(root);
        
        return tree;
    }
    
    fn contains_file(&self, dir : &File, name : &String, is_dir : bool) -> bool {
        for node_id in &dir.files {
            let file = self.nodes.get(*node_id).unwrap();
            if file.dir == is_dir && &file.name == name {
                return true
            }
        }
        return false
    }
    
    pub fn get_dir(&self, dir_id : usize, name : String) -> usize {
        let current_dir = self.nodes.get(dir_id).unwrap();
        
        for f in &current_dir.files {
            let d = self.nodes.get(*f).unwrap();
            if d.dir && d.name == name {
                return *f;
            }
        }
        panic!("did not find dir: {}", name);
    }

    pub fn insert_dir_with_parent(&mut self, name : String, parent_id : usize) {
        if self.contains_file(self.nodes.get(parent_id).unwrap(), &name, true) {
            return
        }
        
        let dir = File {
            parent_id : Some(parent_id),
            name,
            dir: true,
            files: vec![],
            size: 0,
        };
        self.nodes.push(dir);
        let node_id = self.nodes.len()-1;
        self.nodes.get_mut(parent_id).unwrap().files.push(node_id);
    }
    
    pub fn insert_file_with_parent(&mut self, name : String, parent_id : usize, size : usize) {
        if self.contains_file(self.nodes.get(parent_id).unwrap(), &name, false) {
            return
        }

        let file = File {
            parent_id : Some(parent_id),
            name,
            dir: false,
            files: vec![],
            size,
        };
        self.nodes.push(file);
        let node_id = self.nodes.len()-1;
        let mut parent_node = self.nodes.get_mut(parent_id).unwrap();
        parent_node.files.push(node_id);
        parent_node.size += size;
        
        let mut current_id = parent_node.parent_id;
        while current_id.is_some() {
            let mut current_node = self.nodes.get_mut(current_id.unwrap()).unwrap();
            current_node.size += size;
            current_id = current_node.parent_id;
        }
    }
    
    pub fn traverse(&self, current_id : usize, max_size : usize) -> usize {
        let mut acc : usize = 0;
        let current_node = self.nodes.get(current_id).unwrap();
        
        if current_node.size < max_size {
            acc += current_node.size;
        }
        
        for f in &current_node.files {
            let file = self.nodes.get(*f).unwrap();
            if file.dir {
                acc += self.traverse(*f, max_size);
            }
        }
        
        return acc
    }
    
    pub fn traverse2(&self, current_id : usize, mut smallest: usize, needed : usize) -> usize {
        let current_node = self.nodes.get(current_id).unwrap();
        if current_node.size > needed && current_node.size < smallest {
            smallest = current_node.size;
        }

        for f in &current_node.files {
            let file = self.nodes.get(*f).unwrap();
            if file.dir {
                smallest = self.traverse2(*f, smallest, needed);
            }
        }

        smallest
    }
}

pub fn go() {
    let lines: _ = include_str!("../inputs/sample.input").split("\n").collect::<Vec<&str>>();
    
    let mut tree = Tree::new();
    let mut current_id = tree.root_id;
    
    let mut listing = false;
    for l in lines.iter().skip(1) {
        let parts: Vec<_> = l.split(" ").collect();
        if listing {
            if parts[0] == "$" {
                listing = false
            } else {
                if parts[0] == "dir" {
                    tree.insert_dir_with_parent(parts[1].to_string(), current_id);
                } else {
                    tree.insert_file_with_parent(parts[1].to_string(), current_id, usize::from_str(parts[0]).unwrap());
                }
            }
        }
        
        if parts[1] == "ls" {
            listing = true
        } else if parts[1] == "cd" {
            if parts[2] == ".." {
                current_id = tree.nodes.get(current_id).unwrap().parent_id.unwrap();
            } else if parts[2] == "/" {
                current_id = tree.root_id;
            } else {
                current_id = tree.get_dir(current_id, parts[2].to_string());
            }
        }
    }
    
    let part1 = tree.traverse(tree.root_id, 100000);
    println!("part1: {}", part1);

    let available = 70000000 - tree.nodes.get(tree.root_id).unwrap().size;
    let needed = 30000000 - available;
    let part2 = tree.traverse2(tree.root_id, 1000000000, needed);
    println!("part2: {}", part2);
}