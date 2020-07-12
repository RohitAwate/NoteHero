### Wikipedia
# Red-Black Tree

In computer science, a red–black tree is a kind of self-balancing binary search tree. Each node stores an extra bit representing color, used to ensure that the tree remains approximately balanced during insertions and deletions.

When the tree is modified, the new tree is rearranged and repainted to restore the coloring properties that constrain how unbalanced the tree can become in the worst case. The properties are designed such that this rearranging and recoloring can be performed efficiently.

The re-balancing is not perfect, but guarantees searching in O(log n) time, where n is the number of nodes of the tree. The insertion and deletion operations, along with the tree rearrangement and recoloring, are also performed in O(log n) time.

Tracking the color of each node requires only 1 bit of information per node because there are only two colors. The tree does not contain any other data specific to its being a red–black tree so its memory footprint is almost identical to a classic (uncolored) binary search tree. In many cases, the additional bit of information can be stored at no additional memory cost. 

## History

In 1972, Rudolf Bayer invented a data structure that was a special order-4 case of a B-tree. These trees maintained all paths from root to leaf with the same number of nodes, creating perfectly balanced trees. However, they were not binary search trees. Bayer called them a "symmetric binary B-tree" in his paper and later they became popular as 2-3-4 trees or just 2-4 trees.

In a 1978 paper, "A Dichromatic Framework for Balanced Trees", Leonidas J. Guibas and Robert Sedgewick derived the red-black tree from the symmetric binary B-tree. The color "red" was chosen because it was the best-looking color produced by the color laser printer available to the authors while working at Xerox PARC. Another response from Guibas states that it was because of the red and black pens available to them to draw the trees.

In 1993, Arne Andersson introduced the idea of right leaning tree to simplify insert and delete operations.

In 1999, Chris Okasaki showed how to make the insert operation purely functional. Its balance function needed to take care of only 4 unbalanced cases and one default balanced case.

The original algorithm used 8 unbalanced cases, but Cormen et al. (2001) reduced that to 6 unbalanced cases. Sedgewick showed that the insert operation can be implemented in just 46 lines of Java code. In 2008, Sedgewick proposed the left-leaning red–black tree, leveraging Andersson's idea that simplified algorithms. Sedgewick originally allowed nodes whose two children are red, making his trees more like 2-3-4 trees, but later this restriction was added, making new trees more like 2-3 trees. Sedgewick implemented the insert algorithm in just 33 lines, significantly shortening his original 46 lines of code. 

## Properties
Diagram of binary tree. The black root node has two red children and four black grandchildren. The child nodes of the grandchildren are black nil pointers or red nodes with black nil pointers.
An example of a red–black tree

In addition to the requirements imposed on a binary search tree the following must be satisfied by a red–black tree:

1. Each node is either red or black.
2. The root is black. This rule is sometimes omitted. Since the root can always be changed from red to black, but not necessarily vice versa, this rule has little effect on analysis.
3. All leaves (NIL) are black.
4. If a node is red, then both its children are black.
5. Every path from a given node to any of its descendant NIL nodes goes through the same number of black nodes.

The only constraint on the children of black nodes is (5). In particular, a black node (like a leaf node) can have a black parent; for example, every perfect binary tree that consists only of black nodes is a red-black tree.

The black depth of a node is defined as the number of black nodes from the root to that node (i.e. the number of black ancestors). The black height of a red–black tree is the number of black nodes in any path from the root to the leaves, which, by property 5, is constant (alternatively, it could be defined as the black depth of any leaf node).

These constraints enforce a critical property of red–black trees: the path from the root to the farthest leaf is no more than twice as long as the path from the root to the nearest leaf. The result is that the tree is roughly height-balanced. Since operations such as inserting, deleting, and finding values require worst-case time proportional to the height of the tree, this theoretical upper bound on the height allows red–black trees to be efficient in the worst case, unlike ordinary binary search trees.

To see why this is guaranteed, consider a red–black tree having b black nodes in property 5. The shortest path from the root to any leaf has b black nodes and 0 red nodes. We can insert at most one red node between each to black nodes (property 4). Therefore, the longest possible path (ignoring the NIL leaf nodes) has of 2*b nodes, alternating black and red.

```cpp
enum color_t { BLACK, RED };

struct Node {
  Node* parent;
  Node* left;
  Node* right;
  enum color_t color;
  int key;
};

// Helper functions:

Node* GetParent(Node* n) {
  // Note that parent is set to null for the root node.
  return n == nullptr ? nullptr : n->parent;
}

Node* GetGrandParent(Node* n) {
  // Note that it will return nullptr if this is root or child of root
  return GetParent(GetParent(n));
}

Node* GetSibling(Node* n) {
  Node* p = GetParent(n);

  // No parent means no sibling.
  if (p == nullptr) {
    return nullptr;
  }

  if (n == p->left) {
    return p->right;
  } else {
    return p->left;
  }
}

Node* GetUncle(Node* n) {
  Node* p = GetParent(n);

  // No parent means no uncle
  return GetSibling(p);
}

void RotateLeft(Node* n) {
  Node* nnew = n->right;
  Node* p = GetParent(n);
  assert(nnew != nullptr);  // Since the leaves of a red-black tree are empty,
                            // they cannot become internal nodes.
  n->right = nnew->left;
  nnew->left = n;
  n->parent = nnew;
  // Handle other child/parent pointers.
  if (n->right != nullptr) {
    n->right->parent = n;
  }

  // Initially n could be the root.
  if (p != nullptr) {
    if (n == p->left) {
      p->left = nnew;
    } else if (n == p->right) {
      p->right = nnew;
    }
  }
  nnew->parent = p;
}

void RotateRight(Node* n) {
  Node* nnew = n->left;
  Node* p = GetParent(n);
  assert(nnew != nullptr);  // Since the leaves of a red-black tree are empty,
                            // they cannot become internal nodes.

  n->left = nnew->right;
  nnew->right = n;
  n->parent = nnew;

  // Handle other child/parent pointers.
  if (n->left != nullptr) {
    n->left->parent = n;
  }

  // Initially n could be the root.
  if (p != nullptr) {
    if (n == p->left) {
      p->left = nnew;
    } else if (n == p->right) {
      p->right = nnew;
    }
  }
  nnew->parent = p;
}
```