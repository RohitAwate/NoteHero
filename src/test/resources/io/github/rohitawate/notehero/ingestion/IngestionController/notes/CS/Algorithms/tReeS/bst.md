---
notehero:
    title: 'Binary Search Tree'
    sudo: false
---

### Wikipedia
# Binary Search Tree

In computer science, a binary search tree (BST), also called an ordered or sorted binary tree, is a rooted binary tree whose internal nodes each store a key greater than all the keys in the node's left subtree and less than those in its right subtree. A binary tree is a type of data structure for storing data such as numbers in an organized way. Binary search trees allow binary search for fast lookup, addition and removal of data items, and can be used to implement dynamic sets and lookup tables. The order of nodes in a BST means that each comparison skips about half of the remaining tree, so the whole lookup takes time proportional to the binary logarithm of the number of items stored in the tree. This is much better than the linear time required to find items by key in an (unsorted) array, but slower than the corresponding operations on hash tables. Several variants of the binary search tree have been studied. 

## Definition

A binary search tree is a rooted binary tree, whose internal nodes each store a key (and optionally, an associated value), and each has two distinguished sub-trees, commonly denoted left and right. The tree additionally satisfies the binary search property: the key in each node is greater than or equal to any key stored in the left sub-tree, and less than or equal to any key stored in the right sub-tree.:287 The leaves (final nodes) of the tree contain no key and have no structure to distinguish them from one another.

Often, the information represented by each node is a record rather than a single data element. However, for sequencing purposes, nodes are compared according to their keys rather than any part of their associated records. The major advantage of binary search trees over other data structures is that the related sorting algorithms and search algorithms such as in-order traversal can be very efficient; they are also easy to code.

Binary search trees are a fundamental data structure used to construct more abstract data structures such as sets, multisets, and associative arrays.

- When inserting or searching for an element in a binary search tree, the key of each visited node has to be compared with the key of the element to be inserted or found.
- The shape of the binary search tree depends entirely on the order of insertions and deletions and can become degenerate.
- After a long intermixed sequence of random insertion and deletion, the expected height of the tree approaches square root of the number of keys, √n, which grows much faster than log n.
- There has been a lot of research to prevent degeneration of the tree resulting in worst case time complexity of O(n) (for details see section Types).

### Order relation

Binary search requires an order relation by which every element (item) can be compared with every other element in the sense of a total preorder. The part of the element which effectively takes place in the comparison is called its key. Whether duplicates, i. e. different elements with the same key, shall be allowed in the tree or not, does not depend on the order relation, but on the application only. For a search function supporting and handling duplicates in a tree, see section Searching with duplicates allowed.

In the context of binary search trees, a total preorder is realized most flexibly by means of a three-way comparison subroutine.

```python

def search_recursively(key, node):

    if node == None or node.key == key:

        return node

    if key < node.key:

        return search_recursively(key, node.left)

    if key > node.key:

        return search_recursively(key, node.right)
```
---

## Verification

Sometimes we already have a binary tree, and we need to determine whether it is a BST. This problem has a simple recursive solution.

The BST property—every node on the right subtree has to be larger than the current node and every node on the left subtree has to be smaller than the current node—is the key to figuring out whether a tree is a BST or not. The greedy algorithm—simply traverse the tree, at every node check whether the node contains a value larger than the value at the left child and smaller than the value on the right child—does not work for all cases. Consider the following tree: 

```
     20
    /  \
  10    30
       /  \
      5    40
```