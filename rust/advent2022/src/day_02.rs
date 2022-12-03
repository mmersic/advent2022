use std::collections::HashMap;

pub fn go() {
    let mut s1 = HashMap::new();
    s1.insert("A X", 1+3);
    s1.insert("A Y", 2+6);
    s1.insert("A Z", 3+0);

    //PAPER
    s1.insert("B X", 1+0);
    s1.insert("B Y", 2+3);
    s1.insert("B Z", 3+6);

    //SCISSORS
    s1.insert("C X", 1+6);
    s1.insert("C Y", 2+0);
    s1.insert("C Z", 3+3);

    let mut s2 = HashMap::new();
    //ROCK 
    s2.insert("A X", 3+0);
    s2.insert("A Y", 1+3);
    s2.insert("A Z", 2+6);

    //PAPER
    s2.insert("B X", 1+0);
    s2.insert("B Y", 2+3);
    s2.insert("B Z", 3+6);

    //SCISSORS
    s2.insert("C X", 2+0);
    s2.insert("C Y", 3+3);
    s2.insert("C Z", 1+6);
    

    let s: (i32, i32) = include_str!("../inputs/day.02.input")
        .split("\n")
        .map(|x| (*s1.get(x).unwrap(), *s2.get(x).unwrap()))
        .reduce(|x, y| (x.0 + y.0, x.1 + y.1)).unwrap();

    println!("day 2 part 1: {}", s.0);
    println!("day 2 part 2: {}", s.1);
}