# PDP

Lab 2
Divide a simple task between threads. 
The task can easily be divided in sub-tasks requiring no cooperation at all. 
See the effects of false sharing, and the costs of creating threads and of switching between threads.

Requirement: write two problems: one for computing the sum of two matrices, the other for computing the product of two matrices.

Divide the task between a configured number of threads (going from 1 to the number of elements in the resulting matrix).
See the effects on the execution time.


Lab 4

Goal
The goal of this lab is to put together a more complex scenario using threads, mutual exclusion, and producere-consumer synchronization.

The programs to be written will demonstrate:

- the usage of threads to divide the work;
- the usage of mutexes to protect the invariants on the shared data
- the usage of condition variables to signal, from the producer side, of the availability of data for the consumer side.

Requirement
Parallelize the multiplication of 3 matrices.
Use a configurable number of threads to do one matrix multiplication.
Then, use another set of threads to do the second multiplication.
The threads in the second set should start as soon as they start having data from the first multiplication result.

Lab 6 - Parallelizing techniques

Goal
The goal of this lab is to implement a simple but non-trivial parallel algorithm.

Requirement
Perform the multiplication of 2 polynomials. Use both the regular O(n2) algorithm and the Karatsuba algorithm, and each in both the sequencial form and a parallelized form. Compare the 4 variants.

Lab 7 - Parallelizing techniques (2)

Goal
The goal of this lab is to implement a simple but non-trivial parallel algorithm.

Requirement
Solve both problems below:

1. Given a sequence of n numbers, compute the sums of the first k numbers, for each k between 1 and n. Parallelize the computations, to optimize for low latency on a large number of processors. Use at most 2*n additions, but no more than 2*log(n) additions on each computation path from inputs to an output. Example: if the input sequence is 1 5 2 4, then the output should be 1 6 8 12.

2. Add n big numbers. We want the result to be obtained digit by digit, starting with the least significant one, and as soon as possible. For this reason, you should use n-1 threads, each adding two numbers. Each thread should pass the result to the next thread. Arrange the threads in a binary tree. Each thread should pass the sum to the next thread through a queue, digit by digit.


Lab 8 - Parallelizing techniques (3 - parallel explore)

Goal
The goal of this lab is to implement a simple but non-trivial parallel algorithm.

Requirement
Solve the problem below:

Given a directed graph, find a Hamiltonean cycle, if one exists. Use multiple threads to parallelize the search.
