import java.nio.file.Files
import java.nio.file.Path


fun overlap(x1: Int, x2: Int, y1: Int, y2: Int): Array<Int> {
    val max = Math.max(x2, y2)
    val count : Array<Int> = Array(max+1) { 0 }
    
    for (i in x1 until x2+1) {
        count[i]++
    }
    
    for (i in y1 until y2+1) {
        count[i]++
    }
    
    return count 
}

fun rangeContains(x1: Int, x2: Int, y1: Int, y2: Int): Boolean {
    val count = overlap(x1, x2, y1, y2)
    val range = arrayOf(x1, x2, y1, y2)
    
    next@ for (i in 0 .. 3 step 2) {
        for (j in range[i] until range[i+1]+1) {
            if (count[j] != 2) {
                continue@next
            }
        }
        return true
    }
    return false
}

fun rangeAnyOverlap(x1: Int, x2: Int, y1: Int, y2: Int): Boolean {
    val count = overlap(x1, x2, y1, y2)
    for (c in count) {
        if (c > 1) {
            return true
        }
    }
    return false
}


fun main() {
    val lines = Files.readAllLines(Path.of(Stub().javaClass.getClassLoader().getResource("day.04.input").toURI()))
    
    var overlapCount = 0
    var anyOverlapCount = 0
    
    for (line in lines) {
        val l = line.split(",")
        val l1 = l[0].split("-")
        val l2 = l[1].split("-")
        
        val x1 = Integer.parseInt(l1[0])
        val x2 = Integer.parseInt(l1[1])
        val y1 = Integer.parseInt(l2[0])
        val y2 = Integer.parseInt(l2[1])
        
        if (rangeContains(x1, x2, y1, y2)) {
            overlapCount++
        }        
        
        if (rangeAnyOverlap(x1, x2, y1, y2)) {
            anyOverlapCount++
        }
    }
    
    println("day 4 part 1: $overlapCount")
    println("day 4 part 2: $anyOverlapCount")
}


