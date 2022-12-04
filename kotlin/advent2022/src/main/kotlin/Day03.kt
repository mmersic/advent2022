import java.nio.file.Files
import java.nio.file.Path

fun score(c : Char): Int {
    if (c <= 'Z') {
        return c - 'A' + 27
    } else {
        return c - 'a' + 1
    }
}

fun main() {
    val lines = Files.readAllLines(Path.of(Stub().javaClass.getClassLoader().getResource("day.03.input").toURI()))
    
    var totalPriority = 0
    for (line in lines) {
        val l1 = line.substring(0, line.length/2)
        val l2 = line.substring(line.length/2)
        
        val s1 = HashSet<Char>()
        val s2 = HashSet<Char>()
        s1.addAll(l1.asSequence())
        s2.addAll(l2.asSequence())
        val s3 = s1.intersect(s2)
        totalPriority += s3.stream().map { x -> score(x) }.reduce(0) { acc, x -> acc + x }
    }
    
    var totalPriority2 = 0
    for (i in lines.indices step 3) {
        val s1 = HashSet<Char>(); s1.addAll(lines[i].asSequence())
        val s2 = HashSet<Char>(); s2.addAll(lines[i+1].asSequence())
        val s3 = HashSet<Char>(); s3.addAll(lines[i+2].asSequence())
        val s4 = s1.intersect(s2).intersect(s3)
        totalPriority2 += s4.stream().map { x -> score(x) }.reduce(0) { acc, x -> acc + x }
    }
    
    
    println("day 3 part 1: $totalPriority")
    println("day 3 part 2: $totalPriority2")
}