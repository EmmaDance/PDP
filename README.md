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
