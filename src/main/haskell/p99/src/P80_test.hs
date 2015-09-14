import Test.HUnit
import P70_Test(expectEqual, testList)
import P80
import qualified Data.Map.Strict as Map

p80 = testList "P80" [
    expectEqual
        (Graph [
            (Edge 'b' 'c' ()), (Edge 'f' 'c' ()), (Edge 'g' 'h' ()),
            (Edge 'd' 'd' ()), (Edge 'f' 'b' ()), (Edge 'k' 'f' ()),
            (Edge 'h' 'g' ())
        ])
        (graphFromString "[b-c, f-c, g-h, d, f-b, k-f, h-g]"),

    expectEqual
        (Graph [
            (Edge 'b' 'c' 1), (Edge 'f' 'c' 2), (Edge 'g' 'h' 3),
            (Edge 'd' 'd' 0), (Edge 'f' 'b' 4), (Edge 'k' 'f' 5),
            (Edge 'h' 'g' 6)
        ])
        (graphFromStringLabel "[b-c/1, f-c/2, g-h/3, d, f-b/4, k-f/5, h-g/6]"),

    expectEqual
        (Digraph [
            (Edge 'b' 'c' ()), (Edge 'f' 'c' ()), (Edge 'g' 'h' ()),
            (Edge 'd' 'd' ()), (Edge 'f' 'b' ()), (Edge 'k' 'f' ()),
            (Edge 'h' 'g' ())
        ])
        (digraphFromString "[b>c, f>c, g>h, d, f>b, k>f, h>g]"),

    expectEqual
        (Digraph [
            (Edge 'b' 'c' 1), (Edge 'f' 'c' 2), (Edge 'g' 'h' 3),
            (Edge 'd' 'd' 0), (Edge 'f' 'b' 4), (Edge 'k' 'f' 5),
            (Edge 'h' 'g' 6)
        ])
        (digraphFromStringLabel "[b>c/1, f>c/2, g>h/3, d, f>b/4, k>f/5, h>g/6]"),

    expectEqual
        ("bcfghdk", [
            (Edge 'b' 'c' ()), (Edge 'f' 'c' ()), (Edge 'g' 'h' ()),
            (Edge 'f' 'b' ()), (Edge 'k' 'f' ()), (Edge 'h' 'g' ())
        ])
        (graphToTermForm $ graphFromString "[b-c, f-c, g-h, d, f-b, k-f, h-g]"),

    expectEqual
        [('b', "cf"), ('c',"bf"), ('f',"cbk"), ('g',"h"), ('h',"g"), ('d',""), ('k',"f")]
        (graphToAdjacentForm $ graphFromString "[b-c, f-c, g-h, d, f-b, k-f, h-g]"),

    expectEqual
        ("bcfghdk", [
            (Edge 'b' 'c' ()), (Edge 'f' 'c' ()), (Edge 'g' 'h' ()),
            (Edge 'f' 'b' ()), (Edge 'k' 'f' ()), (Edge 'h' 'g' ())
        ])
        (digraphToTermForm $ digraphFromString "[b>c, f>c, g>h, d, f>b, k>f, h>g]"),

    expectEqual
        [('b', "cf"), ('c',"bf"), ('f',"cbk"), ('g',"h"), ('h',"g"), ('d',""), ('k',"f")]
        (digraphToAdjacentForm $ digraphFromString "[b>c, f>c, g>h, d, f>b, k>f, h>g]")
 ]

p81 = testList "P81" [
    expectEqual
        ["pq", "pmq"]
        (graphFindPaths 'p' 'q' $ graphFromStringLabel "[p-q/9, m-q/7, k, p-m/5]"),
    expectEqual
        ["pq", "pmq"]
        (digraphFindPaths 'p' 'q' $ digraphFromStringLabel "[p>q/9, m>q/7, k, p>m/5]"),
    expectEqual
        []
        (digraphFindPaths 'p' 'k' $ digraphFromStringLabel "[p>q/9, m>q/7, k, p>m/5]")
 ]

p82 = testList "P82" [
    expectEqual
        ["fcbf", "fbcf"]
        (graphFindCycles 'f' $ graphFromString "[b-c, f-c, g-h, d, f-b, k-f, h-g]"),
    expectEqual
        ["abca"]
        (digraphFindCycles 'a' $ digraphFromString "[a>b, b>c, c>a, d]")
 ]

p83 = testList "P83" [
    expectEqual
        (graphFromString `map` ["[a-a]"])
        (spanningTrees $ graphFromString "[a-a]"),
    expectEqual
        (graphFromString `map` ["[a-b]"])
        (spanningTrees $ graphFromString "[a-b]"),
    expectEqual
        (graphFromString `map` ["[a-b, b-c, b-d]"])
        (spanningTrees $ graphFromString "[a-b, b-c, b-d]"),
    expectEqual
        (graphFromString `map` ["[a-b, b-c]", "[a-b, a-c]", "[a-c, b-c]"])
        (spanningTrees $ graphFromString "[a-b, b-c, a-c]"),
    expectEqual
        (graphFromString `map` [])
        (spanningTrees $ graphFromString "[a-b, d]"),
    expectEqual
        (graphFromString `map` [
            "[a-b, a-d, b-c, b-e, d-f, d-g, e-h]",
            "[a-b, a-d, b-c, b-e, d-f, d-g, g-h]",
            "[a-b, a-d, b-c, b-e, d-f, e-h, f-g]",
            "[a-b, a-d, b-c, b-e, d-f, e-h, g-h]",
            "[a-b, a-d, b-c, b-e, d-f, f-g, g-h]",
            "[a-b, a-d, b-c, b-e, d-g, e-h, f-g]",
            "[a-b, a-d, b-c, b-e, d-g, f-g, g-h]",
            "[a-b, a-d, b-c, b-e, e-h, f-g, g-h]",
            "[a-b, a-d, b-c, c-e, d-f, d-g, e-h]",
            "[a-b, a-d, b-c, c-e, d-f, d-g, g-h]",
            "[a-b, a-d, b-c, c-e, d-f, e-h, f-g]",
            "[a-b, a-d, b-c, c-e, d-f, e-h, g-h]",
            "[a-b, a-d, b-c, c-e, d-f, f-g, g-h]",
            "[a-b, a-d, b-c, c-e, d-g, e-h, f-g]",
            "[a-b, a-d, b-c, c-e, d-g, f-g, g-h]",
            "[a-b, a-d, b-c, c-e, e-h, f-g, g-h]",
            "[a-b, a-d, b-c, d-e, d-f, d-g, e-h]",
            "[a-b, a-d, b-c, d-e, d-f, d-g, g-h]",
            "[a-b, a-d, b-c, d-e, d-f, e-h, f-g]",
            "[a-b, a-d, b-c, d-e, d-f, e-h, g-h]",
            "[a-b, a-d, b-c, d-e, d-f, f-g, g-h]",
            "[a-b, a-d, b-c, d-e, d-g, e-h, f-g]",
            "[a-b, a-d, b-c, d-e, d-g, f-g, g-h]",
            "[a-b, a-d, b-c, d-e, e-h, f-g, g-h]",
            "[a-b, a-d, b-c, d-f, d-g, e-h, g-h]",
            "[a-b, a-d, b-c, d-f, e-h, f-g, g-h]",
            "[a-b, a-d, b-c, d-g, e-h, f-g, g-h]",
            "[a-b, a-d, b-e, c-e, d-f, d-g, e-h]",
            "[a-b, a-d, b-e, c-e, d-f, d-g, g-h]",
            "[a-b, a-d, b-e, c-e, d-f, e-h, f-g]",
            "[a-b, a-d, b-e, c-e, d-f, e-h, g-h]",
            "[a-b, a-d, b-e, c-e, d-f, f-g, g-h]",
            "[a-b, a-d, b-e, c-e, d-g, e-h, f-g]",
            "[a-b, a-d, b-e, c-e, d-g, f-g, g-h]",
            "[a-b, a-d, b-e, c-e, e-h, f-g, g-h]",
            "[a-b, a-d, c-e, d-e, d-f, d-g, e-h]",
            "[a-b, a-d, c-e, d-e, d-f, d-g, g-h]",
            "[a-b, a-d, c-e, d-e, d-f, e-h, f-g]",
            "[a-b, a-d, c-e, d-e, d-f, e-h, g-h]",
            "[a-b, a-d, c-e, d-e, d-f, f-g, g-h]",
            "[a-b, a-d, c-e, d-e, d-g, e-h, f-g]",
            "[a-b, a-d, c-e, d-e, d-g, f-g, g-h]",
            "[a-b, a-d, c-e, d-e, e-h, f-g, g-h]",
            "[a-b, a-d, c-e, d-f, d-g, e-h, g-h]",
            "[a-b, a-d, c-e, d-f, e-h, f-g, g-h]",
            "[a-b, a-d, c-e, d-g, e-h, f-g, g-h]",
            "[a-b, b-c, b-e, d-e, d-f, d-g, e-h]",
            "[a-b, b-c, b-e, d-e, d-f, d-g, g-h]",
            "[a-b, b-c, b-e, d-e, d-f, e-h, f-g]",
            "[a-b, b-c, b-e, d-e, d-f, e-h, g-h]",
            "[a-b, b-c, b-e, d-e, d-f, f-g, g-h]",
            "[a-b, b-c, b-e, d-e, d-g, e-h, f-g]",
            "[a-b, b-c, b-e, d-e, d-g, f-g, g-h]",
            "[a-b, b-c, b-e, d-e, e-h, f-g, g-h]",
            "[a-b, b-c, b-e, d-f, d-g, e-h, g-h]",
            "[a-b, b-c, b-e, d-g, e-h, f-g, g-h]",
            "[a-b, b-c, b-e, d-f, e-h, f-g, g-h]",
            "[a-b, b-c, c-e, d-e, d-f, d-g, e-h]",
            "[a-b, b-c, c-e, d-e, d-f, d-g, g-h]",
            "[a-b, b-c, c-e, d-e, d-f, e-h, f-g]",
            "[a-b, b-c, c-e, d-e, d-f, e-h, g-h]",
            "[a-b, b-c, c-e, d-e, d-f, f-g, g-h]",
            "[a-b, b-c, c-e, d-e, d-g, e-h, f-g]",
            "[a-b, b-c, c-e, d-e, d-g, f-g, g-h]",
            "[a-b, b-c, c-e, d-e, e-h, f-g, g-h]",
            "[a-b, b-c, c-e, d-f, d-g, e-h, g-h]",
            "[a-b, b-c, c-e, d-g, e-h, f-g, g-h]",
            "[a-b, b-c, c-e, d-f, e-h, f-g, g-h]",
            "[a-b, b-e, c-e, d-e, d-f, d-g, e-h]",
            "[a-b, b-e, c-e, d-e, d-f, d-g, g-h]",
            "[a-b, b-e, c-e, d-e, d-f, e-h, f-g]",
            "[a-b, b-e, c-e, d-e, d-f, e-h, g-h]",
            "[a-b, b-e, c-e, d-e, d-f, f-g, g-h]",
            "[a-b, b-e, c-e, d-e, d-g, e-h, f-g]",
            "[a-b, b-e, c-e, d-e, d-g, f-g, g-h]",
            "[a-b, b-e, c-e, d-e, e-h, f-g, g-h]",
            "[a-b, b-e, c-e, d-f, d-g, e-h, g-h]",
            "[a-b, b-e, c-e, d-g, e-h, f-g, g-h]",
            "[a-b, b-e, c-e, d-f, e-h, f-g, g-h]",
            "[a-d, b-c, b-e, d-e, d-f, d-g, e-h]",
            "[a-d, b-c, b-e, d-e, d-f, d-g, g-h]",
            "[a-d, b-c, b-e, d-e, d-f, e-h, f-g]",
            "[a-d, b-c, b-e, d-e, d-f, e-h, g-h]",
            "[a-d, b-c, b-e, d-e, d-f, f-g, g-h]",
            "[a-d, b-c, b-e, d-e, d-g, e-h, f-g]",
            "[a-d, b-c, b-e, d-e, d-g, f-g, g-h]",
            "[a-d, b-c, b-e, d-e, e-h, f-g, g-h]",
            "[a-d, b-e, c-e, d-e, d-f, d-g, e-h]",
            "[a-d, b-e, c-e, d-e, d-f, d-g, g-h]",
            "[a-d, b-e, c-e, d-e, d-f, e-h, f-g]",
            "[a-d, b-e, c-e, d-e, d-f, e-h, g-h]",
            "[a-d, b-e, c-e, d-e, d-f, f-g, g-h]",
            "[a-d, b-e, c-e, d-e, d-g, e-h, f-g]",
            "[a-d, b-e, c-e, d-e, d-g, f-g, g-h]",
            "[a-d, b-e, c-e, d-e, e-h, f-g, g-h]",
            "[a-d, b-c, c-e, d-e, d-f, d-g, e-h]",
            "[a-d, b-c, c-e, d-e, d-f, d-g, g-h]",
            "[a-d, b-c, c-e, d-e, d-f, e-h, f-g]",
            "[a-d, b-c, c-e, d-e, d-f, e-h, g-h]",
            "[a-d, b-c, c-e, d-e, d-f, f-g, g-h]",
            "[a-d, b-c, c-e, d-e, d-g, e-h, f-g]",
            "[a-d, b-c, c-e, d-e, d-g, f-g, g-h]",
            "[a-d, b-c, c-e, d-e, e-h, f-g, g-h]",
            "[a-d, b-c, b-e, d-f, d-g, e-h, g-h]",
            "[a-d, b-e, c-e, d-f, d-g, e-h, g-h]",
            "[a-d, b-c, c-e, d-f, d-g, e-h, g-h]",
            "[a-d, b-c, b-e, d-f, e-h, f-g, g-h]",
            "[a-d, b-e, c-e, d-f, e-h, f-g, g-h]",
            "[a-d, b-c, c-e, d-f, e-h, f-g, g-h]",
            "[a-d, b-c, b-e, d-g, e-h, f-g, g-h]",
            "[a-d, b-e, c-e, d-g, e-h, f-g, g-h]",
            "[a-d, b-c, c-e, d-g, e-h, f-g, g-h]"
        ])
        (spanningTrees $ graphFromString
                "[a-b, a-d, b-c, b-e, c-e, d-e, d-f, d-g, e-h, f-g, g-h]")
 ]

p84 = testList "P84" [
    expectEqual
        (graphFromString "[a-a]")
        (minimalSpanningTree $ graphFromString "[a-a]"),
    expectEqual
        (graphFromStringLabel "[a-b/1, b-c/2]")
        (minimalSpanningTree $ graphFromStringLabel "[a-b/1, b-c/2, a-c/3]"),
    expectEqual
        (graphFromStringLabel "[a-d/3, d-g/3, g-h/1, d-f/4, a-b/5, b-c/2, b-e/4]")
        (minimalSpanningTree $ graphFromStringLabel
            "[a-b/5, a-d/3, b-c/2, b-e/4, c-e/6, d-e/7, d-f/4, d-g/3, e-h/5, f-g/4, g-h/1]")
 ]

p85 = testList "P85" [
    expectEqual (Just(Map.fromList [('a','1')]))
        (isomorphicMapping
        (graphFromString "[a-a]") (graphFromString "[1-1]")),
    expectEqual True (areIsomorphic
        (graphFromString "[a-a]") (graphFromString "[1-1]")),

    expectEqual (Just(Map.fromList [('a','1'), ('b','2')]))
        (isomorphicMapping
        (graphFromString "[a-b]") (graphFromString "[1-2]")),
    expectEqual True (areIsomorphic
        (graphFromString "[a-b]") (graphFromString "[1-2]")),

    expectEqual (Just(Map.fromList [('a','1'), ('b','2'), ('b', '2'), ('c', '3'), ('d', '4')]))
        (isomorphicMapping
        (graphFromString "[a-b, b-c, c-a, c-d]") (graphFromString "[1-2, 2-3, 3-1, 3-4]")),
    expectEqual True (areIsomorphic
        (graphFromString "[a-b, b-c, c-a, c-d]") (graphFromString "[1-2, 2-3, 3-1, 3-4]"))
 ]

p86 = testList "P86" [
    expectEqual 0 (nodeDegree (graphFromString "[a-a]") 'a'),
    expectEqual 1 (nodeDegree (graphFromString "[a-b]") 'a'),
    expectEqual "a" (nodesByDegree (graphFromString "[a-a]")),
    expectEqual [('a', 1)] (colorNodes (graphFromString "[a-a]")),

    expectEqual 3 (nodeDegree (graphFromString "[a-b, b-c, c-a, a-d]") 'a'),
    expectEqual 2 (nodeDegree (graphFromString "[a-b, b-c, c-a, a-d]") 'b'),
    expectEqual 2 (nodeDegree (graphFromString "[a-b, b-c, c-a, a-d]") 'c'),
    expectEqual 1 (nodeDegree (graphFromString "[a-b, b-c, c-a, a-d]") 'd'),
    expectEqual "acbd" (nodesByDegree (graphFromString "[a-b, b-c, c-a, a-d]")),
    expectEqual [('a', 1), ('d', 2), ('c', 2), ('b', 3)]
        (colorNodes (graphFromString "[a-b, b-c, c-a, a-d]"))
 ]

p87 = testList "P87" [
    expectEqual "a" (nodesByDepthFrom 'a' (graphFromString "[a-a]")),
    expectEqual "cba" (nodesByDepthFrom 'a' (graphFromString "[a-b, b-c, a-c]")),
    expectEqual "cab" (nodesByDepthFrom 'b' (graphFromString "[a-b, b-c, a-c]")),
    expectEqual "abc" (nodesByDepthFrom 'c' (graphFromString "[a-b, b-c, a-c]")),
        expectEqual "cbad" (nodesByDepthFrom 'd' (graphFromString "[a-b, b-c, e-e, a-c, a-d]"))
 ]

main :: IO Counts
main = runTestTT $ TestList [p80, p81, p82, p83, p84, p85, p86, p87]
