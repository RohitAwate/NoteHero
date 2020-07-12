---
notehero:
    sudo: true
    categories: 'Algorithms > Sorting > Fast'
---

### Wikipedia
# Tim Sort

Timsort is a hybrid stable sorting algorithm, derived from merge sort and insertion sort, designed to perform well on many kinds of real-world data. It was implemented by Tim Peters in 2002 for use in the Python programming language. The algorithm finds subsequences of the data that are already ordered (runs) and uses them to sort the remainder more efficiently. This is done by merging runs until certain criteria are fulfilled. Timsort has been Python's standard sorting algorithm since version 2.3. It is also used to sort arrays of non-primitive type in Java SE 7, on the Android platform, in GNU Octave, on V8, and Swift.

It uses techniques from Peter McIlroy's 1993 paper "Optimistic Sorting and Information Theoretic Complexity". 

## Operation

Timsort was designed to take advantage of runs of consecutive ordered elements that already exist in most real-world data, natural runs. It iterates over the data collecting elements into runs and simultaneously putting those runs in a stack. Whenever the runs on the top of the stack match a merge criterion, they are merged together. This goes on until all data is traversed; then, all runs are merged two at a time and only one sorted run remains. The advantage of merging ordered runs instead of merging fixed size sub-lists (as done by traditional mergesort) is that it decreases the total number of comparisons needed to sort the entire list.

Each run has a minimum size, which is based on the size of the input and it is defined at the start of the algorithm. If a run is smaller than this minimum run size, insertion sort is used to add more elements to the run until the minimum run size is reached. 

### Merge space overhead
To merge, Timsort copies the elements of the smaller array (X in this illustration) to temporary memory, then sorts and fills elements in final order into the combined space of X and Y.

The original merge sort implementation is not in-place and it has a space overhead of N (data size). In-place merge sort implementations exist, but have a high time overhead. In order to achieve a middle term, Timsort performs a merge sort with a small time overhead and smaller space overhead than N.

First, Timsort performs a binary search to find the location where the first element of the second run would be inserted in the first ordered run, keeping it ordered. Then, it performs the same algorithm to find the location where the last element of the first run would be inserted in the second ordered run, keeping it ordered. Elements before and after these locations are already in their correct place and do not need to be merged. Then, the smaller of the remaining elements of the two runs is copied into temporary memory, and elements are merged with the larger run into the now free space. If the first run is smaller, the merge starts at the beginning; if the second is smaller, the merge starts at the end. This optimization reduces the number of required element movements, the running time and the temporary space overhead in the general case.

Example: two runs [1, 2, 3, 6, 10] and [4, 5, 7, 9, 12, 14, 17] must be merged. Note that both runs are already sorted individually. The smallest element of the second run is 4 and it would have to be added at the 4th position of the first run in order to preserve its order (assuming that the first position of a run is 1). The largest element of the first run is 10 and it would have to be added at the 5th position of the second run in order to preserve its order. Therefore, [1, 2, 3] and [12, 14, 17] are already in their final positions and the runs in which elements movements are required are [6, 10] and [4, 5, 7, 9]. With this knowledge, we only need to allocate a temporary buffer of size 2 instead of 5. 

## Analysis

In the worst case, Timsort takes $ O(n\log{n}) comparisons to sort an array of n elements. In the best case, which occurs when the input is already sorted, it runs in linear time, meaning that it is an adaptive sorting algorithm.

It is advantageous over Quicksort for sorting object references or pointers because these require expensive memory indirection to access data and perform comparisons and Quicksort's cache coherence benefits are greatly reduced. 