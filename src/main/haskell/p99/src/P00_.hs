module P00_ (
    myLast', myLast'', myLast''', myLast'''', myLast''''',
    myButLast, myButLast', myButLast'', myButLast''', myButLast'''', lastbut1, lastbut1safe,
    elementAt, elementAt', elementAt'', elementAt''', elementAt_w'pf
) where

import Data.List(sortBy, find, findIndex)
import Data.Foldable(Foldable)


-- solutions from https://wiki.haskell.org/99_questions

-- P01
myLast' = foldr1 (const id)
myLast'' = foldr1 (flip const)
myLast''' = head . reverse
myLast'''' = foldl1 (curry snd)
myLast''''' [] = error "No end for empty lists!"
myLast''''' x = x !! (length x - 1)


-- P02
myButLast :: [a] -> a
myButLast = last . init
myButLast' x = reverse x !! 1
myButLast'' [x,_]  = x
myButLast'' (_:xs) = myButLast'' xs
myButLast''' (x:(_:[])) = x
myButLast''' (_:xs) = myButLast''' xs
myButLast'''' = head . tail . reverse
--lastbut1 :: Foldable f => f a -> a
lastbut1 = fst . foldl (\(_,b) x -> (b,x)) (err1,err2)
  where
    err1 = error "lastbut1: Empty list"
    err2 = error "lastbut1: Singleton"
--lastbut1safe :: Foldable f => f a -> Maybe a
lastbut1safe = fst . foldl (\(_,b) x -> (b,Just x)) (Nothing,Nothing)


-- P03
elementAt :: Int -> [a] -> a
elementAt i list = list !! i

elementAt' :: Int -> [a] -> a
elementAt' 0 (x:_)  = x
elementAt' i (_:xs) = elementAt' (i - 1) xs
elementAt' _ _      = error "Index out of bounds"

elementAt'' n xs
        | length xs < n = error "Index out of bounds"
        | otherwise = fst . last $ zip xs [0..n]

elementAt''' n xs = head $ foldr ($) xs
                         $ replicate (n - 1) tail

elementAt_w'pf = (last .) . take . (+ 1)
