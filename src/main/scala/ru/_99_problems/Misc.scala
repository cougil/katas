package ru._99_problems

import org.scalatest.matchers.ShouldMatchers
import org.junit.Test


class Misc extends ShouldMatchers {
	private case class Point(x: Int, y: Int) {
		def knightMoves: Seq[Point] = {
			Seq()
		}

		def isOnBoard(boardSize: Int): Boolean = {
			x < boardSize && x >= 0 && y < boardSize && y >= 0
		}
	}

	@Test def `P91 (**) Knight's tour.`() {
		findKnightMoves(boardSize = 5, Point(0, 0), Seq(Point(0, 0))) should equal(Seq(Point(0, 0)))
	}

	private def findKnightMoves(boardSize: Int, startPoint: Point, moves: Seq[Point]): Seq[Point] = {
		if (moves.size == boardSize * boardSize) return moves

		val nextMoves = startPoint.knightMoves.filter(_.isOnBoard(boardSize)).filter(!moves.contains(_))
		if (nextMoves.isEmpty) return Seq()

		nextMoves.foldLeft[Option[Seq[Point]]](None) { (result, nextMove) =>
			if (result != None) result
			else {
				val subMoves = findKnightMoves(boardSize, nextMove, nextMove +: moves)
				if (subMoves.nonEmpty) Some(subMoves) else None
			}
		}.getOrElse(Seq())
	}


	@Test def `P90 (**) Eight queens problem`() {
		solveEightQueen(2) should equal(Seq())
		solveEightQueen(3) should equal(Seq())
		solveEightQueen(4).size should equal(2)
		solveEightQueen(5).size should equal(10)
		solveEightQueen(8).size should equal(92)
	}


	private case class Queen(col: Int, row: Int) {
		def attacks(that: Queen): Boolean = {
			col == that.col || row == that.row || (col - that.col).abs == (row - that.row).abs
		}
	}
	private type Solution = Seq[Queen]

	private def solveEightQueen(boardSize: Int, startCol: Int = 0): Seq[Solution] = {
		if (boardSize < 3) return Seq()
		if (startCol >= boardSize) return Seq(Seq())

		val solutions = solveEightQueen(boardSize, startCol + 1)
		Range(0, boardSize).flatMap{ row =>
			val queen = Queen(startCol, row)
			solutions.filter(isValidWith(queen, _)).map(queen +: _)
		}
	}

	private def isValidWith(queen: Queen, solution: Solution) = solution.forall(!queen.attacks(_))

	private def asBoard(solution: Solution, boardSize: Int): String = {
		val result = Array.fill[Char](boardSize, boardSize){ '-' }
		solution.foreach{ queen => result(queen.row)(queen.col) = 'Q' }
		result.map{ row => row.mkString("") }.mkString("\n")
	}

	@Test def `presenting solution as a board`() {
		asBoard(Seq(Queen(0,0), Queen(2, 2)), 3) should equal(
			"""
			  |Q--
			  |---
			  |--Q
			""".trim.stripMargin)
	}

	@Test def `amount of queens in solution should equal board size`() {
		val solutions = solveEightQueen(8)
		solutions.size should be(92)
		solutions.foreach{ solution =>
			solution.length should equal(8)
		}
	}

	@Test def `queen is on the same row, column or diagonal`() {
		Queen(0, 0).attacks(Queen(0, 2)) should be(true)
		Queen(0, 0).attacks(Queen(2, 0)) should be(true)
		Queen(0, 0).attacks(Queen(2, 2)) should be(true)
		Queen(0, 0).attacks(Queen(2, 1)) should be(false)
	}
}