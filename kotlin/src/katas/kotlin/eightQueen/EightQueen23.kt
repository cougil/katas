package katas.kotlin.eightQueen

import katas.kotlin.shouldEqual
import org.junit.Test
import kotlin.coroutines.experimental.buildSequence

class EightQueen23 {
    @Test fun `find positions of queens on board in which they don't attack each other`() {
        Board(size = 0).doFindQueenPosition() shouldEqual listOf(Board(size = 0))
        Board(size = 1).doFindQueenPosition() shouldEqual listOf(Board(size = 1, queens = listOf(Queen(0, 0))))
        Board(size = 2).doFindQueenPosition() shouldEqual listOf()
        Board(size = 3).doFindQueenPosition() shouldEqual listOf()
        Board(size = 4).doFindQueenPosition() shouldEqual listOf(
            Board(size = 4, queens = listOf(Queen(x = 0, y = 2), Queen(x = 1, y = 0), Queen(x = 2, y = 3), Queen(x = 3, y = 1))),
            Board(size = 4, queens = listOf(Queen(x = 0, y = 1), Queen(x = 1, y = 3), Queen(x = 2, y = 0), Queen(x = 3, y = 2)))
        )
        Board(size = 8).doFindQueenPosition().size shouldEqual 92
        Board(size = 10).doFindQueenPosition().size shouldEqual 724

        // Board(size = 20).findQueenPosition().take(1).toList() shouldEqual 724
    }

    private fun Board.doFindQueenPosition(): List<Board> = findQueenPosition().toList()

    private fun Board.findQueenPosition(): Sequence<Board> =
        generateBoards().filter { it.size == it.queens.size }

    private fun Board.generateBoards(): Sequence<Board> =
        if (size == 0) sequenceOf(Board(size = 0))
        else buildSequence {
            var boards = listOf(Board(size = size))
            0.until(size).forEach { x ->
                boards = 0.until(size).flatMap { y ->
                    boards
                        .mapNotNull { it.add(Queen(x, y)) }
                        .filter { it.queens.size == x + 1 }
                        .onEach { yield(it) }
                }
            }
        }

    private fun Board.add(queen: Queen): Board? =
        if (isValidMove(queen)) copy(queens = queens + queen) else null

    private fun Board.isValidMove(queen: Queen) =
        queens.size < size &&
            queens.none { it.x == queen.x || it.y == queen.y } &&
            queens.none { Math.abs(it.x - queen.x) == Math.abs(it.y - queen.y) }

    private data class Board(val size: Int, val queens: List<Queen> = emptyList())

    private data class Queen(val x: Int, val y: Int)
}


