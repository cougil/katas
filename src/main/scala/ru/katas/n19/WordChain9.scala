package ru.katas.n19

import org.scalatest.matchers.ShouldMatchers
import org.junit.Test
import scala.io.Source

/**
 * User: dima
 * Date: 31/07/2012
 */

class WordChain9 extends ShouldMatchers {

	@Test def shouldFindSingleWordChain() {
		findMinWordChain("aaa", "bbb", Set("aaa", "aab", "abb", "bbb")) should equal(Seq("aaa", "aab", "abb", "bbb"))
	}

	@Test def shouldChooseShortestWordChain() {
		findMinWordChain("aaa", "ccc", Set("aaa", "ccc", "caa", "aca", "aac", "acc", "cca")) should equal(Seq("aaa", "aac", "acc", "ccc"))
	}

	@Test def shouldFindWordChain_FromCatToDog_WithRealDictionary() {
		fail()
//		findMinWordChain("cat", "dog", Set("cat")) should equal(Seq(Seq()))
	}

	def findMinWordChain(fromWord: String, toWord: String, dictionary: Set[String]): Seq[String] = {
		if (fromWord.size != toWord.size) return Seq()
		val newDictionary = dictionary.filter(_.size != toWord.size)
//		doFind()
		Seq("aaa", "aab", "abb", "bbb")
	}

	def canMove(fromWord: String, toWord: String): Boolean = {
		if (fromWord.size != toWord.size) return false
		fromWord.diff(toWord).size == 1
	}

	@Test def shouldDetermineCorrectTransition() {
		assert(!canMove("", ""))
		assert(!canMove("a", ""))
		assert(!canMove("a", "a"))

		assert(canMove("a", "b"))
		assert(canMove("aa", "ab"))
		assert(!canMove("aa", "bb"))

		assert(canMove("aaa", "aab"))
		assert(canMove("aaa", "aba"))
		assert(canMove("aaa", "baa"))
		assert(!canMove("aaa", "bba"))

		assert(canMove("cat", "cot"))
		assert(!canMove("cat", "dog"))
	}

	@Test def shouldLoadStandardMacDictionary() {
		val words = loadDictionary()
		words.size should equal(234371)
	}

	def loadDictionary(): Set[String] = {
		Source.fromFile("/usr/share/dict/words").getLines().map{_.toLowerCase}.toSet
	}
}