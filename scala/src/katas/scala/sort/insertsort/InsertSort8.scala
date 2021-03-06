package katas.scala.sort.insertsort

import org.scalatest.Matchers
import katas.scala.sort.SeqSortTest

import scala.reflect.ClassTag

/**
 * User: dima
 * Date: 26/06/2012
 */


class InsertSort8 extends Matchers with SeqSortTest {

	override def sort[T](seq: Seq[T])(implicit ordered: (T) => Ordered[T], tag: ClassTag[T]): Seq[T] = {
		def insert(seq: Seq[T], n: T): Seq[T] = {
			if (seq.isEmpty) Seq(n)
			else if (seq.head >= n) n +: seq
			else seq.head +: insert(seq.tail, n)
		}

		if (seq.isEmpty) seq
		else insert(sort(seq.tail), seq.head)
	}
}