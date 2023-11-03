use std::str::FromStr;

pub fn go() {
    let lines: _ = include_str!("../inputs/day.05.input").split("\n").collect::<Vec<&str>>();
    
    let mut v1 : Vec<Vec<char>> = vec![Vec::new(); ((lines[0].len() / 4)+1)];
    
    let mut moves = 0;
    
    for (j, line) in (&lines).into_iter().enumerate() {
        if *line == " 1   2   3   4   5   6   7   8   9 " {
            moves = j + 2;
            break;
        }
        for (i, c) in line.chars().enumerate() {
            if c == ' ' || c == '[' || c == ']' {
                //do nothing
            } else {
                v1[i / 4].insert(0, c);
            }
        }
    }

    let mut v2 = v1.clone();
    
    for i in moves..lines.len() {
        let lv : Vec<&str> = lines[i].split(" ").collect();
        let count = usize::from_str(lv[1]).expect("invalid count");
        let from = usize::from_str(lv[3]).expect("invalid from")-1;
        let to = usize::from_str(lv[5]).expect("invalid to")-1;

        let mut t : Vec<char> = Vec::new();
        for _ in 0..count {
            let c1 = v1[from].pop().expect("should have a value");
            v1[to].push(c1);
            let c2 = v2[from].pop().expect("should have a value");
            t.insert(0, c2);
        }
        v2[to].append(&mut t);
    }
    
    let mut part1 : String = String::new();
    for v in v1 {
        part1.push(*v.last().expect("should have at least one"));
    }
    println!("part1: {}", part1);

    let mut part2 : String = String::new();
    for v in v2 {
        part2.push(*v.last().expect("should have at least one"));
    }
    
    println!("part2: {}", part2);
}