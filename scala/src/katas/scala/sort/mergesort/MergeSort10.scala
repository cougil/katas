package katas.scala.sort.mergesort

import org.junit.Test
import org.specs2.matcher.ShouldMatchers

/**
 * User: dima
 * Date: 30/04/2012
 */
class MergeSort10 extends ShouldMatchers {
  @Test def shouldSortList() {
    sort(List[Int]()) should equalTo(List())
    sort(List(1)) should equalTo(List(1))
    sort(List(1, 2)) should equalTo(List(1, 2))
    sort(List(2, 1)) should equalTo(List(1, 2))
    sort(List(2, 3, 1)) should equalTo(List(1, 2, 3))

	  List(1, 2, 3, 4).permutations.foreach { list =>
			  sort(list) should equalTo(List(1, 2, 3, 4))
	  }

	  sort(List("b", "a")) should equalTo(List("a", "b"))
  }

  def sort[T](list: List[T])(implicit o: T => Ordered[T]): List[T] = {
    if (list.size <= 1) return list
    val (part1, part2) = list.splitAt(list.size / 2)
    merge(sort(part1), sort(part2))
  }

  def merge[T](list1: List[T], list2: List[T])(implicit o: T => Ordered[T]): List[T] = {
    if (list1.isEmpty) return list2
    if (list2.isEmpty) return list1
    if (list1(0) < list2(0))
      list1(0) :: merge(list1.tail, list2)
    else
      list2(0) :: merge(list1, list2.tail)
  }
}