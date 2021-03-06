package katas.java.hackerank;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static java.util.Collections.shuffle;
import static katas.java.hackerank.SelfBalancingTree.Node.hiddenInsert;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * https://www.hackerrank.com/challenges/self-balancing-tree
 */
public class SelfBalancingTree {
    @Test public void treeHeight() {
        Node tree =
            node('a',
                node('b'),
                node('c',
                    node('d'),
                    node('e')));

        assertThat(height(tree), equalTo(2));
        assertThat(height(tree.left), equalTo(0));
        assertThat(height(tree.right), equalTo(1));
    }

    @Test public void insertingValue() {
        Node tree =
            node(3,
                node(1),
                node(5));

        assertThat(insert(tree, 4), equalTo(
            node(3,
                node(1),
                node(5,
                    node(4),
                    null))
        ));
    }

    @Test public void heightAfterInsertion() {
        Node node = node(5);
        assertThat(node.ht, equalTo(0));

        node = insert(node, node(4));
        assertThat(node.ht, equalTo(1));

        node = insert(node, node(6));
        assertThat(node.ht, equalTo(1));

        node = insert(node, node(7));
        assertThat(node.ht, equalTo(2));

        node = insert(node, node(8));
        assertThat(node.ht, equalTo(3));
    }

    @Test public void rotateRight() {
        Node tree =
            node(5,
                node(4,
                    node(3), null),
            null);

        assertThat(rotateRight(tree.left, tree), equalTo(
            node(4,
                node(3),
                node(5))
        ));
    }

    @Test public void heightAfterBalancedInsertion() {
        Node tree = node(5);
        assertThat(tree.ht, equalTo(0));

        tree = insertAndBalance(tree, node(6));
        assertThat(tree.ht, equalTo(1));

        tree = insertAndBalance(tree, node(7));
        assertThat(tree.ht, equalTo(1));

        tree = insertAndBalance(tree, node(8));
        assertThat(tree.ht, equalTo(2));

        tree = insertAndBalance(tree, node(9));
        assertThat(tree.ht, equalTo(2));
    }

    @Test public void exampleOfBalancingFactor() {
        Node node = node(3);
        node = hiddenInsert(node, 2);
        node = hiddenInsert(node, 4);
        node = hiddenInsert(node, 5);
        node = insert(node, 6);

        assertThat(balanceFactorOf(node), lessThan(2));
    }

    @Test public void shuffledInput() {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            integers.add(i);
        }
        shuffle(integers, new Random(123));

        Node node = node(-1);
        for (Integer i : integers) {
            node = insert(node, i);
        }
        System.out.println(node);
        assertThat(node.ht, equalTo(3));
    }

    private static Node insert(Node node, int value) {
        return insertAndBalance(node, node(value));
    }

    private static Node insertAndBalance(Node node, Node newNode) {
        return insert(node, newNode, true);
    }

    private static Node insert(Node node, Node newNode) {
        return insert(node, newNode, false);
    }

    private static Node insert(Node node, Node newNode, boolean rebalance) {
        if (node == null) return newNode;
        if (newNode.val <= node.val) {
            node.left = insert(node.left, newNode, rebalance);
        } else {
            node.right = insert(node.right, newNode, rebalance);
        }
        updateHeight(node);

        return rebalance ? balance(node) : node;
    }

    private static Node balance(Node node) {
        int leftHeight = heightOf(node.left);
        int rightHeight = heightOf(node.right);
        if (Math.abs(leftHeight - rightHeight) <= 1) return node;

        int leftLeftHeight = leftHeight == -1 ? -1 : heightOf(node.left.left);
        int leftRightHeight = leftHeight == -1 ? -1 : heightOf(node.left.right);
        int rightLeftHeight = rightHeight == -1 ? -1 : heightOf(node.right.left);
        int rightRightHeight = rightHeight == -1 ? -1 : heightOf(node.right.right);

        if (leftHeight > rightHeight) {
            if (leftRightHeight > leftLeftHeight) {
                node.left = rotateLeft(node.left.right, node.left);
            }
            return rotateRight(node.left, node);
        } else {
            if (rightLeftHeight > rightRightHeight) {
                node.right = rotateRight(node.right.left, node.right);
            }
            return rotateLeft(node.right, node);
        }
    }

    private static Node updateHeight(Node node) {
        node.ht = 1 + max(heightOf(node.left), heightOf(node.right));
        return node;
    }

    private static Node rotateRight(Node node, Node parentNode) {
        parentNode.left = node.right;
        node.right = parentNode;

        updateHeight(parentNode);
        updateHeight(node);

        return node;
    }

    private static Node rotateLeft(Node node, Node parentNode) {
        parentNode.right = node.left;
        node.left = parentNode;

        updateHeight(parentNode);
        updateHeight(node);

        return node;
    }

    private static int heightOf(Node node) {
        return node == null ? -1 : node.ht;
    }

    private static Node node(int value) {
        return node(value, null, null);
    }

    private static Node node(int value, Node left, Node right) {
        Node node = new Node();
        node.val = value;
        node.left = left;
        node.right = right;
        node.ht = 0;
        return node;
    }

    private static int balanceFactorOf(Node node) {
        return heightOf(node.left) - heightOf(node.right);
    }

    private static void printInOrderBalanceFactors(Node node) {
        if (node == null) return;
        printInOrderBalanceFactors(node.left);
        System.out.print(node.val + ":" + balanceFactorOf(node) + " ");
        printInOrderBalanceFactors(node.right);
    }

    private static void printPreOrderBalanceFactors(Node node) {
        if (node == null) return;
        System.out.print(node.val + ":" + balanceFactorOf(node) + " ");
        printPreOrderBalanceFactors(node.left);
        printPreOrderBalanceFactors(node.right);
    }

    private static int height(Node node) {
        if (node == null) {
            return -1;
        } else {
            return 1 + max(height(node.left), height(node.right));
        }
    }

    private static String asString(Node node) {
        if (node == null) return "{}";
        String s = "{val:" + node.val + "/" + node.ht;
        if (node.left != null || node.right != null) {
            s += ",left:" + asString(node.left);
            s += ",right:" + asString(node.right);
        }
        return s + '}';
    }


    static class Node {
        int val;
        int ht;
        Node left;
        Node right;

        Node() {
        }

        static Node newNode(int value) {
            Node node = new Node();
            node.val = value;
            node.left = null;
            node.right = null;
            return node;
        }

        static Node hiddenInsert(Node node, int value) {
            if (node == null) {
                node = newNode(value);
            } else if (value > node.val) {
                node.right = hiddenInsert(node.right, value);
                if (BF_hidden(node) == -2) {
                    if (value > node.right.val) {
                        node = RR_hidden(node);
                    } else {
                        node = RL_hidden(node);
                    }
                }
            } else if (value < node.val) {
                node.left = hiddenInsert(node.left, value);
                if (BF_hidden(node) == 2) {
                    if (value < node.left.val) {
                        node = LL_hidden(node);
                    } else {
                        node = LR_hidden(node);
                    }
                }
            }

            node.ht = ht_hidden(node);
            return node;
        }

        static int ht_hidden(Node node) {
            if (node == null) {
                return 0;
            } else {
                int var1 = node.left == null ? 0 : 1 + node.left.ht;
                int var2 = node.right == null ? 0 : 1 + node.right.ht;
                return var1 > var2 ? var1 : var2;
            }
        }

        static Node rotate_right_hidden(Node node) {
            Node nodeLeft = node.left;
            node.left = nodeLeft.right;
            nodeLeft.right = node;
            node.ht = ht_hidden(node);
            nodeLeft.ht = ht_hidden(nodeLeft);
            return nodeLeft;
        }

        static Node rotate_left_hidden(Node node) {
            Node nodeRight = node.right;
            node.right = nodeRight.left;
            nodeRight.left = node;
            node.ht = ht_hidden(node);
            nodeRight.ht = ht_hidden(nodeRight);
            return nodeRight;
        }

        static Node RR_hidden(Node node) {
            node = rotate_left_hidden(node);
            return node;
        }

        static Node LL_hidden(Node node) {
            node = rotate_right_hidden(node);
            return node;
        }

        static Node LR_hidden(Node node) {
            node.left = rotate_left_hidden(node.left);
            node = rotate_right_hidden(node);
            return node;
        }

        static Node RL_hidden(Node node) {
            node.right = rotate_right_hidden(node.right);
            node = rotate_left_hidden(node);
            return node;
        }

        static int BF_hidden(Node node) {
            if (node == null) {
                return 0;
            } else {
                int leftHt = node.left == null ? 0 : 1 + node.left.ht;
                int rightHt = node.right == null ? 0 : 1 + node.right.ht;
                return leftHt - rightHt;
            }
        }

        @Override public String toString() {
            return asString(this);
        }

        @SuppressWarnings("SimplifiableIfStatement")
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (val != node.val) return false;
            if (left != null ? !left.equals(node.left) : node.left != null) return false;
            return right != null ? right.equals(node.right) : node.right == null;
        }

        @Override public int hashCode() {
            int result = val;
            result = 31 * result + (left != null ? left.hashCode() : 0);
            result = 31 * result + (right != null ? right.hashCode() : 0);
            return result;
        }
    }
}
