pub fn get_priority(c : &char) -> usize {
    if *c as usize <= 'Z' as usize {
        return *c as usize - 'A' as usize + 27
    } else {
        return *c as usize - 'a' as usize + 1
    }
}

pub fn go() {
    let lines: _ = include_str!("../inputs/day.03.input").split("\n").collect::<Vec<&str>>();

    let priority = lines
        .iter()
        .map(|x| {
            let (y1, y2) = x.split_at(x.len() / 2);
            for c in y1.chars() {
                if y2.contains(c) {
                    return get_priority(&c);
                }
            }
            panic!("no matching char");
        })
        .sum::<usize>();
    
    let priority_2 = lines
        .chunks(3)
        .map(|chunk| {
            for c in chunk[0].chars() {
                if chunk[1].contains(c) && chunk[2].contains(c) {
                    return get_priority(&c);
                }
            }
            panic!("no matching char in chunk");
        })
        .sum::<usize>();
    
    
    println!("day 3 part 1 {}", priority);
    println!("day 3 part 2 {}", priority_2);
}