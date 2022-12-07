import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val lines = Files.readAllLines(Path.of(Stub().javaClass.getClassLoader().getResource("day.05.input").toURI()))
    
    val stacks = mutableListOf<MutableList<Char>>()
    
    val l1 = lines.get(0)
    for (i in 0 until (l1.length+1)/4) {
        stacks.add(mutableListOf())
    }
    
    var i = 0
    while (i < l1.length) {
        val line = lines.get(i)
        if (!line.contains("[")) {
            break
        }
        var index = -1
        var last = -1
        while (true) {
            index = line.indexOf("[", last+1)
            if (index == -1) { break }
            val c = line.substring(index+1, index+2).get(0)
            stacks.get(index/4).add(c)
            last = index + 1
        }
        i++
    }
    
    i+=2

    val stacks2: MutableList<MutableList<Char>> = ArrayList()
    for (s in stacks) {
        stacks2.add(ArrayList(s))
    }

    val starting = i
    while (i < lines.size) {
        val line: String = lines.get(i)
        val p = line.split(" ")
        val count = p[1].toInt()
        val from: MutableList<Char> = stacks[p[3].toInt() - 1]
        val to: MutableList<Char> = stacks[p[5].toInt() - 1]
        for (j in 0 until count) {
            val c: Char = from.removeAt(0)
            to.add(0, c)
        }
        i++
    }

    i = starting
    while (i < lines.size) {
        val line: String = lines.get(i)
        val p = line.split(" ")
        val count = p[1].toInt()
        val from: MutableList<Char> = stacks2[p[3].toInt() - 1]
        val to: MutableList<Char> = stacks2[p[5].toInt() - 1]
        val temp: MutableList<Char> = java.util.ArrayList()
        for (j in 0 until count) {
            val c: Char = from.removeAt(0)
            temp.add(c)
        }
        for (j in temp.indices.reversed()) {
            to.add(0, temp[j])
        }
        i++
    }

    var result = ""
    for (s in stacks) {
        result += s[0]
    }

    var result2 = ""
    for (s in stacks2) {
        result2 += s[0]
    }

    println("day 5 part 1: $result")

    println("day 5 part 2: $result2")
}