import Test.HUnit
import Data.List(sortBy, find, findIndex)
import System.Random (RandomGen, next, mkStdGen, newStdGen)

last' :: [a] -> a
last' [] = error "Can't get last element of empty list"
last' [x] = x
last' (_:xs) = last' xs
-- from https://wiki.haskell.org/99_questions/Solutions/1
myLast' = foldr1 (const id)
myLast'' = foldr1 (flip const)
myLast''' = head . reverse
myLast'''' = foldl1 (curry snd)
myLast''''' [] = error "No end for empty lists!"
myLast''''' x = x !! (length x -1)


penultimate :: [a] -> a
penultimate [] = error "Can't get penultimate element"
penultimate [_] = error "Can't get penultimate element"
penultimate [x, _] = x
penultimate (_:xs) = penultimate(xs)
-- from https://wiki.haskell.org/99_questions/Solutions/2
myButLast :: [a] -> a
myButLast = last . init
myButLast' x = reverse x !! 1
myButLast'' [x,_]  = x
myButLast'' (_:xs) = myButLast'' xs
myButLast''' (x:(_:[])) = x
myButLast''' (_:xs) = myButLast''' xs
myButLast'''' = head . tail . reverse
lastbut1 = fst . foldl (\(a,b) x -> (b,x)) (err1,err2)
  where
    err1 = error "lastbut1: Empty list"
    err2 = error "lastbut1: Singleton"
lastbut1safe = fst . foldl (\(a,b) x -> (b,Just x)) (Nothing,Nothing)


kth :: Int -> [a] -> a
--kth n xs = xs !! n
kth _ [] = error "Can't get element of empty list"
kth n (x:xs) = if n == 0 then x else kth (n - 1) xs


length' :: [a] -> Int
length' [] = 0
length' (_:xs) = 1 + length' xs


reverse' :: [a] -> [a]
reverse' [] = []
reverse' (x:xs) = (reverse' xs) ++ [x]


isPalindrome :: (Eq a) => [a] -> Bool
isPalindrome xs = xs == (reverse xs)


-- http://stackoverflow.com/questions/6479444/is-there-a-type-any-in-haskell
data AList a = AList [AList a] | Value a
aList :: [a] -> AList a
aList [] = AList []
aList xs = AList ((\x -> Value x) `map` xs)


flatten :: [AList a] -> [a]
flatten [] = []
flatten (x:xs) = case x of
        Value it -> it : flatten xs
        AList it -> flatten it ++ flatten xs


compress :: [Char] -> [Char]
compress [] = []
compress (x:xs) = x : compress (consumeHead x xs)
    where
        consumeHead _ [] = []
        consumeHead char ys =
            if (head ys) /= char then ys
            else consumeHead char (tail ys)


pack :: (Eq a) => [a] -> [[a]]
pack' :: (Eq a) => [a] -> [a] -> [[a]]
pack xs = pack' xs []
pack' [] [] = []
pack' [] list = [list]
pack' (x:xs) list =
    if (length list == 0) || x == (head list) then pack' xs (x:list)
    else list : (pack' xs [x])


encode :: (Eq a) => [a] -> [(Int, a)]
encode list = (\x -> (length x, head x)) `map` (pack list)


decode :: [(Int, a)] -> [a]
decode [] = []
decode ((n, c):xs) = (nCopiesOf c n) ++ decode xs


encodeDirect :: (Eq a) => [a] -> [(Int, a)]
encodeDirect [] = []
encodeDirect list = (countHeadIn list, head list) : encodeDirect (dropHeadIn list)
    where
        countHeadIn xs = length (takeWhile (== head xs) xs)
        dropHeadIn xs = dropWhile (== head xs) xs


duplicate :: (Eq a) => [a] -> [a]
duplicate [] = []
duplicate (x:xs) = [x, x] ++ duplicate xs


duplicateN :: (Eq a) => Int -> [a] -> [a]
duplicateN _ [] = []
duplicateN n (x:xs) = nCopiesOf x n ++ duplicateN n xs


dropEveryNth :: Int -> [a] -> [a]
dropEveryNth _ [] = []
dropEveryNth amount list = drop' amount amount list
    where
        drop' n counter xs
            | n < 2 || null xs = []
            | counter == 1 = drop' n n (tail xs)
            | otherwise = (head xs) : (drop' n (counter - 1) (tail xs))


split :: Int -> [a] -> ([a], [a])
split index list = split' index [] list
    where
        split' 0 xs ys = (xs, ys)
        split' _ xs [] = (xs, [])
        split' n xs (y:ys) = split' (n - 1) (xs ++ [y]) ys


slice :: Int -> Int -> [a] -> [a]
slice _ _ [] = []
slice from to list = fst (split (to - from) (snd (split from list)))


rotate :: Int -> [a] -> [a]
rotate _ [] = []
rotate shift list
    | shift < 0 = rotate (shift + length list) list
    | shift == 0 = []
    | shift > (length list) = rotate (shift `mod` length list) list
    | otherwise = (snd tuple) ++ (fst tuple)
        where tuple = split shift list


removeAt :: Int -> [a] -> ([a], a)
removeAt _ [] = error "Cannot remove element from empty list"
removeAt n list =
    let tuple = split n list
        newList = (fst tuple) ++ (tail (snd tuple))
        removeElement = (head (snd tuple))
    in (newList, removeElement)


insertAt :: Int -> a -> [a] -> [a]
insertAt n value [] = [value]
insertAt n value list = (fst s) ++ [value] ++ (snd s)
    where s = split n list


range :: Int -> Int -> [Int]
range from to
    | from == to = [to]
    | from < to = from : range (from + 1) to
    | otherwise = []


randomSelect :: Int -> [a] -> IO [a]
randomSelect amount list = do
    g <- newStdGen
    return $ randomSelect' g amount list

randomSelect' :: (RandomGen g) => g -> Int -> [a] -> [a]
randomSelect' _ _ [] = []
randomSelect' _ 0 _ = []
randomSelect' randomGen amount list = element : (randomSelect' newGenerator (amount - 1) updatedList)
    where (randomInt, newGenerator) = next randomGen
          index = randomInt `mod` (length list)
          element = kth index list
          updatedList = fst (removeAt index list)


lotto :: Int -> Int -> IO [Int]
lotto amount maxNumber = do
    g <- newStdGen
    return $ lotto' g amount maxNumber

lotto' :: (RandomGen g) => g -> Int -> Int -> [Int]
lotto' randomGen amount maxNumber = randomSelect' randomGen amount (range 1 maxNumber)


randomPermute :: [a] -> IO [a]
randomPermute list =
    newStdGen >>= (\ g -> return $ randomPermute' g list)

randomPermute' :: (RandomGen g) => g -> [a] -> [a]
randomPermute' _ [] = []
randomPermute' randomGen list = randomSelect' randomGen (length list) list


combinations :: Int -> [a] -> [[a]]
combinations amount list
    | amount == 0 = [[]]
    | amount > (length list) = []
    | otherwise =
        (\ subCombination -> head list : subCombination) `map` (combinations (amount - 1) (tail list)) ++
        (combinations amount $ tail list)


group3 :: (Eq a) => [a] -> [[[a]]]
group3 list
    | length list /= 9 = error ("Expected group size to be 9 but was " ++ show (length list))
    | otherwise =
        (combinations 2 list) >>= (\comb2 ->
            (combinations 3 $ exclude comb2 list) >>= (\comb3 ->
                (combinations 4 $ exclude comb3 $ exclude comb2 list) >>= (\comb4 -> [[comb2, comb3, comb4]])
            ))
        where
            exclude comb xs = filter (\it -> notElem it comb) xs


group :: (Eq a) => [Int] -> [a] -> [[[a]]]
group sizes list
    | sizes == [] = [[]]
    | otherwise =
        (combinations (head sizes) list) >>=
            (\combination -> (\it -> combination : it) `map` (group (tail sizes) (exclude combination list)))
        where
            exclude comb xs = filter (\it -> notElem it comb) xs


lsort :: [[a]] -> [[a]]
lsort listOfLists = sortBy (\a b -> compare (length a) (length b)) listOfLists


lsortFreq :: [[a]] -> [[a]]
lsortFreq listOfLists = sortBy (\a b -> compare (lengthFreqOf a) (lengthFreqOf b)) listOfLists
    where lengthFreqOf list = length (filter (\it -> (length it) == (length list)) listOfLists)


isPrime :: Int -> Bool
isPrime n
    | n < 2 = False
    | otherwise = notDivisable n (n - 1)
notDivisable :: Int -> Int -> Bool
notDivisable n n2
    | n2 < 2 = True
    | otherwise = ((n `rem` n2) /= 0) && (notDivisable n (n2 - 1))


gcd' :: Int -> Int -> Int
gcd' a b
    | a < b = gcd' b a
    | a `rem` b == 0 = b
    | otherwise = gcd b (a - b)


isCoprimeTo :: Int -> Int -> Bool
isCoprimeTo a b = (gcd' a b) == 1


totient :: Int -> Int
totient n = amountOfComprimes [1..n]
    where amountOfComprimes = length . filter (isCoprimeTo n)


primeFactors :: Int -> [Int]
primeFactors n = case firstPrimeOf n of
        Just value -> value : primeFactors (n `div` value)
        Nothing -> []
    where firstPrimeOf number = find (\it -> (isPrime it) && (number `rem` it == 0)) [2..n]


primeFactorsMultiplicity :: Int -> [(Int, Int)]
primeFactorsMultiplicity n = case firstPrimeOf n of
        Just value -> add value (primeFactorsMultiplicity (n `div` value))
        Nothing -> []
    where
        firstPrimeOf number = find (\it -> (isPrime it) && (number `rem` it == 0)) [2..n]
        add value list = case findIndex (\it -> (fst it) == value) list of
            Just index -> let entry = (list !! index) in
                (fst entry, (snd entry) + 1) : (filter (\it -> (fst it) /= value) list)
            Nothing -> (value, 1) : list


-- private
nCopiesOf :: a -> Int -> [a]
nCopiesOf _ 0 = []
nCopiesOf value amount = value : nCopiesOf value (amount - 1)



main :: IO Counts
main =
    do
        runTestTT $ TestCase $ assertEqual "P01" 8 (last' [1, 1, 2, 3, 5, 8])
        runTestTT $ TestCase $ assertEqual "P02" 5 (penultimate [1, 1, 2, 3, 5, 8])
        runTestTT $ TestCase $ assertEqual "P03" 2 (kth 2 [1, 1, 2, 3, 5, 8])
        runTestTT $ TestCase $ assertEqual "P04" 6 (length' [1, 1, 2, 3, 5, 8])
        runTestTT $ TestCase $ assertEqual "P05" [8, 5, 3, 2, 1, 1] (reverse' [1, 1, 2, 3, 5, 8])
        runTestTT $ TestCase $ assertEqual "P06" False (isPalindrome [1, 2, 3, 4, 5])
        runTestTT $ TestCase $ assertEqual "P06" True (isPalindrome [1, 2, 3, 2, 1])
        runTestTT $ TestCase $ assertEqual "P07" [1, 1, 2] (flatten [aList([1, 1]), Value 2])
        runTestTT $ TestCase $ assertEqual "P08" "abcade" (compress "aaaabccaadeeee")
        runTestTT $ TestCase $ assertEqual "P09" ["aaaa", "b", "cc", "aa", "d", "eeee"] (pack "aaaabccaadeeee")
        runTestTT $ TestCase $ assertEqual "P10" [(4, 'a'), (1, 'b'), (2, 'c'), (2, 'a'), (1, 'd'), (4, 'e')] (encode "aaaabccaadeeee")
        runTestTT $ TestCase $ assertEqual "P11" "" "" -- not implemented because it's cumbersome to do in type syst
        runTestTT $ TestCase $ assertEqual "P12" "aaaabccaadeeee" (decode [(4, 'a'), (1, 'b'), (2, 'c'), (2, 'a'), (1, 'd'), (4, 'e')])
        runTestTT $ TestCase $ assertEqual "P13" [(4, 'a'), (1, 'b'), (2, 'c'), (2, 'a'), (1, 'd'), (4, 'e')] (encodeDirect "aaaabccaadeeee")
        runTestTT $ TestCase $ assertEqual "P14" "aabbccccdd" (duplicate "abccd")
        runTestTT $ TestCase $ assertEqual "P15" "aaabbbccccccddd" (duplicateN 3 "abccd")
        runTestTT $ TestCase $ assertEqual "P16" "abdeghjk" (dropEveryNth 3 "abcdefghijk")
        runTestTT $ TestCase $ assertEqual "P17" ("abc", "defghijk") (split 3 "abcdefghijk")
        runTestTT $ TestCase $ assertEqual "P18" "defg" (slice 3 7 "abcdefghijk")
        runTestTT $ TestCase $ assertEqual "P19" "defghijkabc" (rotate 3 "abcdefghijk")
        runTestTT $ TestCase $ assertEqual "P19" "defghijkabc" (rotate 14 "abcdefghijk")
        runTestTT $ TestCase $ assertEqual "P19" "jkabcdefghi" (rotate (-2) "abcdefghijk")
        runTestTT $ TestCase $ assertEqual "P20" ("acd", 'b') (removeAt 1 "abcd")
        runTestTT $ TestCase $ assertEqual "P21" ("a!bcd") (insertAt 1 '!' "abcd")
        runTestTT $ TestCase $ assertEqual "P22" [4, 5, 6, 7, 8, 9] (range 4 9)
        runTestTT $ TestCase $ assertEqual "P22" [] (range 9 4)
        runTestTT $ TestCase $ assertEqual "P23" "hgc" (randomSelect' (mkStdGen 123) 3 "abcdefghijk")
        runTestTT $ TestCase $ assertEqual "P24" [24,23,18,4,13,25] (lotto' (mkStdGen 123) 6 49)
        runTestTT $ TestCase $ assertEqual "P25" "acbdfe" (randomPermute' (mkStdGen 123) "abcdef")
        runTestTT $ TestCase $ assertEqual "P26" [""] (combinations 0 "a")
        runTestTT $ TestCase $ assertEqual "P26" ["a"] (combinations 1 "a")
        runTestTT $ TestCase $ assertEqual "P26" [] (combinations 2 "a")
        runTestTT $ TestCase $ assertEqual "P26" ["ab", "ac", "bc"] (combinations 2 "abc")
        runTestTT $ TestCase $ assertEqual "P26" ["abc"] (combinations 3 "abc")
        runTestTT $ TestCase $ assertEqual "P26"
                ["abc","abd","abe","abf","acd","ace","acf","ade","adf","aef","bcd","bce","bcf","bde","bdf","bef","cde","cdf","cef","def"]
                (combinations 3 "abcdef")
        runTestTT (TestCase (assertEqual "P26" 220 $ (length . combinations 3) "abcdef123456"))
        runTestTT $ TestCase $ assertEqual "P27a" 1260 (length $ group3 ["Aldo", "Beat", "Carla", "David", "Evi", "Flip", "Gary", "Hugo", "Ida"])
        runTestTT $ TestCase $ assertEqual "P27b" 1260 (length $ group [2, 3, 4] ["Aldo", "Beat", "Carla", "David", "Evi", "Flip", "Gary", "Hugo", "Ida"])
        runTestTT $ TestCase $ assertEqual "P28a" ["o", "de", "de", "mn", "abc", "fgh", "ijkl"] (lsort ["abc", "de", "fgh", "de", "ijkl", "mn", "o"])
        runTestTT $ TestCase $ assertEqual "P28b" ["ijkl", "o", "abc", "fgh", "de", "de", "mn"] (lsortFreq ["abc", "de", "fgh", "de", "ijkl", "mn", "o"])

        runTestTT $ TestCase $ assertEqual "P31" False (isPrime 6)
        runTestTT $ TestCase $ assertEqual "P31" True (isPrime 7)
        runTestTT $ TestCase $ assertEqual "P32" 9 (gcd' 36 63)
        runTestTT $ TestCase $ assertEqual "P33" True (35 `isCoprimeTo` 64)
        runTestTT $ TestCase $ assertEqual "P33" False (36 `isCoprimeTo` 64)
        runTestTT $ TestCase $ assertEqual "P34" 4 (totient 10)
        runTestTT $ TestCase $ assertEqual "P35" [] (primeFactors 1)
        runTestTT $ TestCase $ assertEqual "P35" [2, 5] (primeFactors 10)
        runTestTT $ TestCase $ assertEqual "P35" [3, 3, 5, 7] (primeFactors 315)
        runTestTT $ TestCase $ assertEqual "P36" [(3, 2)] (primeFactorsMultiplicity 9)
        runTestTT $ TestCase $ assertEqual "P36" [(3, 2), (5, 1), (7, 1)] (primeFactorsMultiplicity 315)
