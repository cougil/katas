package ru.sort.insertsort

import org.scalatest.matchers.ShouldMatchers
import ru.sort.SeqSortTest
import collection.mutable.ArrayBuffer

/**
 * User: dima
 * Date: 06/07/2012
 */

class InsertSort10 extends SeqSortTest with ShouldMatchers {

	def sort[T](seq: Seq[T])(implicit ordered: T => Ordered[T]): Seq[T] = {
		var array = ArrayBuffer(seq: _*)

		1.until(array.size).foreach{ i =>
			0.to(i).reverse.foreach{ j =>
				if (array(j) > array(i)) {
					swap(array, i, j)
				}
			}
		}

		array.toSeq
	}

	def swap[T](buffer: ArrayBuffer[T], i1: Int, i2: Int) {

	}

	def sort_[T](seq: Seq[T])(implicit ordered: T => Ordered[T]): Seq[T] = {
		if (seq.isEmpty) seq
		else insert(seq.head, sort(seq.tail))
	}
	
	private def insert[T](value: T, seq: Seq[T])(implicit ordered: T => Ordered[T]): Seq[T] = {
		if (seq.isEmpty) Seq(value)
		else if (value <= seq.head) value +: seq
		else if (value > seq.head) seq.head +: insert(value, seq.tail)
		else throw new IllegalStateException()
	}
}