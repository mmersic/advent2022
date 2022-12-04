import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class Stub {
    
}
fun main() {
    val stub = Stub()
    val cals = Files.readAllLines(Path.of(stub.javaClass.getClassLoader().getResource("day.01.input").toURI())).stream()
        .collect(Collectors.joining(",")).split(",,").stream()
        .map { x -> x.split(",")
                     .map { x -> Integer.parseInt(x) }
                     .reduce { acc, i -> acc + i } }
        .sorted(Comparator.reverseOrder()).toList()
    
    println("day 1 part 1: " + cals[0])
    println("day 1 part 2: " + (cals[0] + cals[1] + cals[2]))
}