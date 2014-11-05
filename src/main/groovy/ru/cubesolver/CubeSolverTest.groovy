package ru.cubesolver
import org.junit.Test

import static ru.cubesolver.CubeAssembler.assembleAsCube
import static ru.cubesolver.Surface.*

class CubeSolverTest {

  @Test void "surface has sides"() {
    def surface = surface(
            [x, x, _],
            [_, x, x],
            [_, x, x]
    )
    assert surface.topSide() == [x, x, _]
    assert surface.rightSide() == [_, x, x]
    assert surface.bottomSide() == [_, x, x]
    assert surface.leftSide() == [x, _, _]
  }

  @Test void "surface sides can be connectible"() {
    assert !areConnectible([_, _, x], [x, x, x])
    assert !areConnectible([_, _, x], [x, _, _])
    assert areConnectible([_, x, x], [_, _, _])
    assert areConnectible([_, _, x], [x, x, _])
  }

  @Test void "surface can be converted to string"() {
    def surface = surface(
            [x, x, _],
            [_, x, x],
            [_, x, _]
    )
    assert surface.toString() == """
      |xx-
      |-xx
      |-x-
    """.stripMargin("|").trim()
  }

  @Test void "surface can be constructed from string"() {
    def surface = surface("""
      xx-
      -xx
      -x-
    """)
    assert surface.toString() == """
      |xx-
      |-xx
      |-x-
    """.stripMargin("|").trim()
  }

  @Test void "surfaces can have different size"() {
    assert surface("""
          ---
          -x-
          ---
    """).size() == 3
    assert surface("""
          -----
          -xx--
          -xx--
          -----
          -----
    """).size() == 5
  }

  @Test void "surface can be rotated"() {
    assert surface(
            [x, x, _],
            [_, x, _],
            [_, x, _],
    ).rotateRight() == surface(
            [_, _, x],
            [x, x, x],
            [_, _, _],
    )
    assert surface(
            [_, _, x],
            [x, x, x],
            [_, _, _],
    ).rotateRight() == surface(
            [_, x, _],
            [_, x, _],
            [_, x, x],
    )
  }

  @Test void "surface can be horizontally flipped"() {
    assert surface(
            [x, x, _],
            [_, x, _],
            [_, x, _],
    ).horizontalFlip() == surface(
            [_, x, x],
            [_, x, _],
            [_, x, _],
    )
  }

  @Test void "all possible rotations of a surface"() {
    def rotations = surface(
            [x, x, _],
            [_, x, _],
            [_, x, _],
    ).rotations()

    assert rotations.size() == 8
    assert rotations[4] == surface(
            [_, x, x],
            [_, x, _],
            [_, x, _],
    )
    assert rotations[7] == surface(
            [x, _, _],
            [x, x, x],
            [_, _, _],
    )
  }


  @Test void "all possible valid combinations of 3x3 surfaces"() {
    def combinations = surface("""
        ---
        -x-
        ---
    """).combinations()

    combinations.each {
      println("=====")
      println(it)
    }
    assert combinations.size() == 160
  }

  @Test void "all possible valid combinations of 4x4 surfaces"() {
    def combinations = surface("""
        ----
        -xx-
        -xx-
        ----
    """).combinations()

    def size = combinations.size()
    assert size == 2400
  }

  @Test void "all possible valid combinations of 5x5 surfaces"() {
    def combinations = surface("""
        -----
        -xxx-
        -xxx-
        -xxx-
        -----
    """).combinations()

    def size = combinations.size()
    assert size == 38415
  }


  @Test void "can assemble some 3x3 surfaces"() {
    def allSurfaces = surface("""
          ---
          -x-
          ---
    """).combinations()

    def cube = assembleAsCube(allSurfaces.toList().take(30))
    cube.each { key, value ->
      println("===${key}===")
      println(value)
    }

    assert cube.front == surface("""
          -x-
          -x-
          ---
    """)
    assert cube.top == surface("""
          xx-
          -x-
          ---
    """)
    assert cube.right == surface("""
          xxx
          xx-
          ---
    """)
    assert cube.bottom == surface("""
          xxx
          xxx
          ---
    """)
    assert cube.left == surface("""
          -xx
          xxx
          ---
    """)
    assert cube.back == surface("""
          ---
          -xx
          xxx
    """)
  }

  @Test void "can assemble some 5x5 surfaces"() {
    def allSurfaces = surface("""
          -----
          -xxx-
          -xxx-
          -xxx-
          -----
    """).combinations()

    def cube = assembleAsCube(allSurfaces.toList().take(100))
    cube.each { key, value ->
      println("===${key}===")
      println(value)
    }

    assert cube.front == surface("""
       -x---
       -xxx-
       -xxx-
       -xxx-
       -----
    """)
    assert cube.top == surface("""
       -----
       -xxx-
       -xxx-
       -xxx-
       --xxx
    """)
    assert cube.right == surface("""
       --xx-
       xxxxx
       xxxxx
       xxxxx
       xxxxx
    """)
    assert cube.bottom == surface("""
       xxx--
       xxxx-
       xxxx-
       xxxx-
       xxx--
    """)
    assert cube.left == surface("""
       xxxxx
       xxxxx
       xxxxx
       xxxx-
       -----
    """)
    assert cube.back == surface("""
       -xxxx
       -xxx-
       -xxx-
       -xxx-
       -----
    """)
  }
}
