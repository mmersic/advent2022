use std::cmp;
use std::str::FromStr;

pub fn overlap(x1 : u32, x2 : u32, y1 : u32, y2 : u32) -> Vec<u8> {
    let range = cmp::max(x2, y2) + 1;
    let mut over = Vec::new();
    for _ in 0..range {
        over.push(0 as u8);
    }
    
    for i in x1..=x2 {
        over[i as usize] += 1;
    }
    
    for i in y1..=y2 {
        over[i as usize] += 1;
    }
    
    return over;
}

pub fn rcontains(x1 : u32, x2 : u32, y1 : u32, y2 : u32) -> bool {
    let x = overlap(x1, x2, y1, y2);
    let ranges = vec![x1, x2, y1, y2];

    'next: for i in (0..4).step_by(2) {
        for j in ranges[i]..=ranges[i+1] {
            if x[j as usize] != 2 {
                continue 'next;
            }
        }
        return true;
    }
    return false;
}


pub fn go() {
    let lines: _ = include_str!("../inputs/day.04.input").split("\n").collect::<Vec<&str>>();
    
    let mut part1 = 0;
    let mut part2 = 0;
    for line in lines {
        let ranges : _ = line.split(',').collect::<Vec<&str>>();
        let r1 = ranges[0].split('-').collect::<Vec<&str>>();
        let r2 = ranges[1].split('-').collect::<Vec<&str>>();
        
        let x1= u32::from_str(r1[0]).expect("parsing error");
        let x2 = u32::from_str(r1[1]).expect("parsing error");
        let y1 = u32::from_str(r2[0]).expect("parsing error");
        let y2 = u32::from_str(r2[1]).expect("parsing error");
        
        if rcontains(x1, x2, y1, y2) {
            part1 += 1;
        }
        
        let over = overlap(x1, x2, y1, y2);
        if over.contains(&2) {
            part2 += 1;
        }
    }
    
    println!("day 4 part 1 {}", part1);
    println!("day 4 part 2 {}", part2);
}