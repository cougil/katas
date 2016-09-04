package p99

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

fun <T> isPalindrome(list: List<T>): Boolean =
    if (list.size <= 1) true
    else if (list.first() != list.last()) false
    else isPalindrome(list.drop(1).dropLast(1))

class P06Test {
    @Test fun `find out whether a list is a palindrome`() {
        assertThat(isPalindrome(listOf<Int>()), equalTo(true))
        assertThat(isPalindrome(listOf(1)), equalTo(true))
        assertThat(isPalindrome(listOf(1, 2)), equalTo(false))
        assertThat(isPalindrome(listOf(1, 2, 1)), equalTo(true))
        assertThat(isPalindrome(listOf(1, 2, 2, 1)), equalTo(true))
    }
}