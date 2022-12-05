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
        
        for (c in l1) {
            if (l2.contains(c)) {
                totalPriority += score(c)
                break
            }
        }
    }
    
    var totalPriority2 = 0
    for (i in lines.indices step 3) {
        val l1 = lines[i]
        val l2 = lines[i+1]
        val l3 = lines[i+2]
        
        for (c in l1) {
            if (l2.contains(c) && l3.contains(c)) {
                totalPriority2 += score(c)
                break
            }
        }
    }
    
    println("day 3 part 1: $totalPriority")
    println("day 3 part 2: $totalPriority2")
}