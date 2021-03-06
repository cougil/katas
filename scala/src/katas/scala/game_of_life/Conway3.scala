package katas.scala.game_of_life

import org.junit.Test
import org.scalatest.Matchers

/**
 * User: dima
 * Date: 23/10/2012
 */

class Conway3 extends Matchers {
	@Test def whenAllCellsAreDeadNothingHappens() {
		new Field(
			"""
			  |---
			  |---
			  |---
			""").next() should equal(new Field("""
        |---
        |---
        |---
      """))
	}

	@Test def lonelyCellDies() {
		new Field(
			"""
			  |---
			  |-0-
			  |---
			""").next() should equal(new Field("""
        |---
        |---
        |---
      """))
	}

	@Test def cellWithEnoughNeighboursBecomesAlive() {
		new Field(
			"""
			  |----
			  |-00-
			  |----
			  |----
			""").next() should equal(new Field("""
				|-00-
				|----
				|-00-
				|----
			"""))
	}

	@Test def overPopulatedCellDies() {
		new Field(
			"""
			  |-0-
			  |000
			  |-0-
			""").next().cellAt(1, 1).toString should equal("-")
	}

	@Test def gettingCellShouldWrapAroundFieldBorder() {
		val field = new Field(
			"""
			  |--0
			  |-0-
			  |0--
			""")
		field.cellAt(0, 0) should equal(DeadCell)
		field.cellAt(0, 1) should equal(DeadCell)
		field.cellAt(0, 2) should equal(LivingCell)
		field.cellAt(-1, 0) should equal(LivingCell)
		field.cellAt(-2, 0) should equal(DeadCell)
		field.cellAt(-3, 0) should equal(DeadCell)

		field.cellAt(-1, -1) should equal(DeadCell)
		field.cellAt(-2, -2) should equal(LivingCell)
		field.cellAt(0, 5) should equal(LivingCell)
	}

	case class Cell(c: Char) {
		override def toString: String = c.toString
	}
	object LivingCell extends Cell('0')
	object DeadCell extends Cell('-')
	object UndefinedCell extends Cell(' ')

	class Field(private val data: List[List[Cell]]) {

		def this(s: String) {
			this(s.stripMargin.trim.split("\n").map{_.toList.map{ Cell }}.toList)
		}

		def next(): Field = {
			var newData: List[List[Cell]] = List.fill(data.size){ List.fill(data.size){ UndefinedCell } }
			for (row <- data.indices; col <- data.indices) {
				val liveCellsAround = cellsAround(row, col).count{_ == LivingCell}
				val newCellState =
					if (liveCellsAround < 2 || liveCellsAround > 3) DeadCell
					else LivingCell
				newData = newData.updated(row, newData(row).updated(col, newCellState))
			}
			new Field(newData)
		}

		def cellAt(row: Int, col: Int): Cell = {
			def wrap(n: Int) = (n + data.size) % data.size
			data(wrap(row))(wrap(col))
		}

		private def cellsAround(row: Int, col: Int): Seq[Cell] = {
			List((-1, 0), (-1, -1), (0, -1), (1, -1), (1, 0), (1, 1), (0, 1), (-1, 1)).map{ point =>
				cellAt(row + point._1, col + point._2)
			}
		}

		override def toString: String = "\n" + data.map{_.mkString}.mkString("\n") + "\n"

		override def equals(that: Any): Boolean = that.isInstanceOf[Field] && that.asInstanceOf[Field].data == data
	}
}