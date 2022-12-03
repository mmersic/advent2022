pub fn go() {
    let mut cals: _ = include_str!("../inputs/day.01.input")
        .split("\n\n")
        .map(|x| x.split("\n").map(|x | x.parse::<u32>().unwrap()).sum())
        .collect::<Vec<u32>>();
    
    cals.sort_by(|x, y| x.cmp(y).reverse());
    
    println!("day 1 part 1: {}", cals[0]);
    println!("day 1 part 2: {}", cals[0] + cals[1] + cals[2]);
    
}