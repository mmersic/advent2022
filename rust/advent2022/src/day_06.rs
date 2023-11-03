use std::collections::HashSet;

/// O(size_of_input * distinct_count)
pub fn dc(input : &str, distinct_count : usize) -> Option<usize> {

    for (i, w) in input.chars().collect::<Vec<char>>().windows(distinct_count).enumerate() {
        let mut set : HashSet<char> = HashSet::new();
        for c in w {
            set.insert(*c);
        }
        if set.len() == distinct_count {
            return Some(i + distinct_count);
        }
    }
    None
}

/// O(size_of_input)
pub fn dc_efficient(input : &str, distinct_count : usize) -> Option<usize> {
    
    let ic = input.as_bytes();
    let mut seen : [usize; 256] = [0; 256];
    
    let mut distinct = 0;
    for i in 0 .. distinct_count {
        seen[ic[i] as usize] = seen[ic[i] as usize] + 1;
        if seen[ic[i] as usize] == 1 {
            distinct += 1;
        }
    }
    
    for i in distinct_count .. ic.len() {
        if distinct == distinct_count {
            return Some(i);
        }

        seen[ic[i] as usize] += 1;
        if seen[ic[i] as usize] == 1 {
            distinct += 1;
        }

        seen[ic[i-distinct_count] as usize] -= 1;
        if seen[ic[i-distinct_count] as usize] == 0 {
            distinct -= 1;
        }
    }
    
    None
}

pub fn go() {
    let input: _ = include_str!("../inputs/day.06.input");
    
    let part1 = dc_efficient(input, 4).expect("should have found a packet");
    let part2 = dc_efficient(input, 14).expect("should have found a packet");
    
    println!("part1: {}", part1);
    println!("part2: {}", part2)
}