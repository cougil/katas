package katas.scala.sort.insertsort

import org.scalatest.Matchers
import katas.scala.sort.SeqSortTest

import scala.reflect.ClassTag


class InsertSort14 extends SeqSortTest with Matchers {
	override def sort[T](seq: Seq[T])(implicit ordered: (T) => Ordered[T], tag: ClassTag[T]): Seq[T] = {
		def insert(sorted: Seq[T], element: T): Seq[T] = {
			if (sorted.isEmpty) Seq(element)
			else if (sorted.head >= element) element +: sorted
			else sorted.head +: insert(sorted.tail, element)
		}
		if (seq.isEmpty) seq
		else insert(sort(seq.tail), seq.head)
	}
}