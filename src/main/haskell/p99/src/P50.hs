module P50 (
    Tree(..), node, leafNode, t, t_, e,
    sizeOf, isBalanced,
    cBalanced,
    isMirrorOf, isSymmetric,
    addValue, fromList,
    symmetricBalancedTrees
) where

data Tree a = Node { value :: a, left :: Tree a, right :: Tree a } | End
              deriving (Eq)
leafNode :: a -> Tree a
leafNode value = Node value End End
node :: a -> Tree a -> Tree a -> Tree a
node value left right = Node value left right

-- shortcut names for tree construction in code
t = Node
t_ value = Node value End End
e = End


instance (Show a) => Show (Tree a) where
    show End = "e"
    show (Node value End End) = "(t_ " ++ (show value) ++ ")"
    show (Node value left right) =
            "(t " ++ (show value) ++ " " ++
            (show left) ++ " " ++ (show right) ++ ")"

-- P55
cBalanced :: Int -> a -> [Tree a]
cBalanced 0 _ = []
cBalanced 1 value = [leafNode value]
cBalanced 2 value = [(Node value (leafNode value) End), (Node value End (leafNode value))]
cBalanced 3 value = [(Node value (leafNode value) (leafNode value))]
cBalanced n value =
    if ((n - 1) `rem` 2 == 0) then
        nodeVariations balancedHalf balancedHalf
    else
        nodeVariations balancedHalf1 balancedHalf ++
        nodeVariations balancedHalf balancedHalf1
    where nodeVariations nodes1 nodes2 =
            (\left -> (\right ->
                    (Node value left right)
                ) `map` nodes1
            ) `concatMap` nodes2
          balancedHalf = cBalanced (n `div` 2) value
          balancedHalf1 = cBalanced ((n `div` 2) - 1) value

sizeOf :: Tree a -> Int
sizeOf End = 0
sizeOf (Node _ left right) = 1 + (sizeOf left) + (sizeOf right)

isBalanced :: Tree a -> Bool
isBalanced End = True
isBalanced (Node _ left right) = (abs (sizeOf(left) - sizeOf(right))) <= 1


-- P56
isMirrorOf :: Tree a -> Tree a -> Bool
isMirrorOf End End = True
isMirrorOf End (Node _ _ _) = False
isMirrorOf (Node _ _ _) End = False
isMirrorOf (Node _ left1 right1) (Node _ left2 right2) =
    (left1 `isMirrorOf` right2) && (right1 `isMirrorOf` left2)

isSymmetric :: Tree a -> Bool
isSymmetric End = True
isSymmetric (Node _ left right) = left `isMirrorOf` right


-- P57
addValue :: Ord a => Tree a -> a -> Tree a
addValue End value = Node value End End
addValue (Node value left right) newValue =
    if (newValue <= value) then Node value (addValue left newValue) right
    else Node value left (addValue right newValue)

fromList :: Ord a => [a] -> Tree a
fromList list = foldl (\tree value -> addValue tree value) End list


-- P58
symmetricBalancedTrees :: Int -> a -> [Tree a]
symmetricBalancedTrees size value = filter isSymmetric (cBalanced size value)

