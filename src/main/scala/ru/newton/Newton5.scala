package ru.newton

import org.junit.Test
import org.scalatest.matchers.{MatchResult, Matcher, ShouldMatchers}
import org.scalatest.Matchers

/**
 * User: dima
 * Date: 20/04/2013
 */

class Newton5 extends Matchers {
	@Test def `should find square root of a number using Newton method`() {
		squareRootOf(1) should beCloseTo(1.0)
		squareRootOf(4) should beCloseTo(2.0)
		squareRootOf(10) should beCloseTo(3.16227)
	}

	private def squareRootOf(n: Double, guess: Double = 1.0, threshold: Double = 0.0001): Double = {
		def goodEnough = {guess: Double => (n - guess * guess).abs < threshold}
		def improve = {guess: Double => guess - ((guess * guess - n) / (2 * guess))}
		if (goodEnough(guess)) guess
		else squareRootOf(n, improve(guess))
	}

	private def beCloseTo(expected: Double, threshold: Double = 0.0001) = {
		new Matcher[Double]() {
			def apply(actual: Double) =
				MatchResult(
					(expected - actual).abs < threshold,
					actual + " should be close to" + expected,
					actual + " is not close to" + expected
				)
		}
	}
}