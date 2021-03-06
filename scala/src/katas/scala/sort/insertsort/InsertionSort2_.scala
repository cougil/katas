package katas.scala.sort.insertsort

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

class InsertionSort2_ extends AssertionsForJUnit {
  @Test def shouldSortList() {
    assert(sort(List()) === List())
    assert(sort(List(1)) === List(1))
    assert(sort(List(1, 1)) === List(1, 1))
    assert(sort(List(2, 1)) === List(1, 2))
    assert(sort(List(2, 3, 1)) === List(1, 2, 3))
  }

  def sort[a](list: List[a])(implicit orederer: a => Ordered[a]): List[a] =
    list match {
      case List() => List()
      case x :: xs => insert(x, sort(xs))
    }

  def insert[a](value: a, list: List[a])(implicit orderer: a => Ordered[a]): List[a] =
    list match {
      case List() => List(value)
      case x :: xs => if (value > x) x :: insert(value, xs) else value :: list
    }
}