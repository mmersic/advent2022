import java.nio.file.Files
import java.nio.file.Path

private val SCORE_P1 = mapOf(
    "A X" to 1+3, "A Y" to 2+6, "A Z" to 3+0,
    "B X" to 1+0, "B Y" to 2+3, "B Z" to 3+6,
    "C X" to 1+6, "C Y" to 2+0, "C Z" to 3+3,
)

private val SCORE_P2 = mapOf(
    "A X" to 3+0, "A Y" to 1+3, "A Z" to 2+6,
    "B X" to 1+0, "B Y" to 2+3, "B Z" to 3+6,
    "C X" to 2+0, "C Y" to 3+3, "C Z" to 1+6,
)


fun main() {
    val scores = Files.readAllLines(Path.of(Stub().javaClass.getClassLoader().getResource("day.02.input").toURI())).stream()
        .map { x -> Pair(SCORE_P1[x], SCORE_P2[x]) }
        .reduce(Pair(0, 0)) { acc, x -> Pair(acc.first!! + x.first!!, acc.second!! + x.second!!) }

    println("day 2 part 1: " + scores.first)
    println("day 2 part 2: " + scores.second)
}