# Project #3: Experiment with Hashing

* Author: Matt Youngberg
* Class: CS321 Section # 4001
* Semester: Fall 2023

## Overview

This program evaluates Hashtable performance, implementing open addressing and
varying the load factor, using both linear probing and double hashing.

## Reflection

Implementing the core of this project was straightforward, provided that one
read the text and felt comfortable with the concepts. Implementing a Hashtable
using linear probing and double hashing was also clear-cut and, to some extent,
replicable from the book. Reaching the first checkpoint of the project demanded
very little time on my part, so that aspect proceeded well. Writing the test
class was also enjoyable, as the requirement to generate different types of
object inputs for the tests presented a scenario that neatly necessitated a
layer of indirection via an interface.

The complexity of the project for me primarily lay in deciphering the statistics
outputted by the tests, as well as comprehending how they were to be derived
from the requested frequencyCount and probeCount fields. Identifying which part
of the codebase could most succinctly handle the aggregation posed its own
challenges. And then once tests were operational, I had to navigate through
various incorrect interpretations I initially held. Overall, this cost me
significantly more time than the aspects involving a proficient understanding of
Hashtables.

## Compiling and Using

To compile the program, run the following command from the project root:

```
$ javac DoubleHashing.java HashObject.java Hashtable.java HashtableTest.java \
LinearProbing.java TwinPrimeGenerator.java
```

The driver class is `HashtableTest`. See it's usage by running it without 
arguments:

```
$ java HashtableTest 
Usage: java HashtableTest <dataType> <loadFactor> [<debugLevel>]
       <dataSource>: 1 ==> random numbers
                     2 ==> date value as a long
                     3 ==> word list
       <loadFactor>: The ratio of objects to table size, 
                       denoted by alpha = n/m
       <debugLevel>: 0 ==> print summary of experiment
                     1 ==> save the two hash tables to a file at the end
                     2 ==> print debugging output for each insert
```

Then you can run it with the arguments you see fit:

```
$ java HashtableTest 3 0.5 1
```

Run the test suite provided with the project with the following command, after
having compiled the project:

```
$ ./run-tests.sh
```

## Results 

Here are tables that help one intuitively explore how Linearing Probing is
generally outperformed by Double Hashing; the latter avoids clustering and thus
lowers the average amount of probes that have to happen per insert:

### Random Numbers

| Method           | Load Factor | Avg Probes |
|------------------|-------------|------------|
| **Linear Probing**   |             |            |
|                  | 0.50        | 1.51       |
|                  | 0.60        | 1.76       |
|                  | 0.70        | 2.16       |
|                  | 0.80        | 2.95       |
|                  | 0.90        | 5.37       |
|                  | 0.95        | 10.16      |
|                  | 0.99        | 55.56      |
| **Double Hashing**   |             |            |
|                  | 0.50        | 1.39       |
|                  | 0.60        | 1.53       |
|                  | 0.70        | 1.71       |
|                  | 0.80        | 2.00       |
|                  | 0.90        | 2.56       |
|                  | 0.95        | 3.14       |
|                  | 0.99        | 4.75       |

### Random Dates

| Method           | Load Factor | Avg Probes |
|------------------|-------------|------------|
| **Linear Probing**   |             |            |
|                  | 0.50        | 1.08       |
|                  | 0.60        | 1.08       |
|                  | 0.70        | 1.10       |
|                  | 0.80        | 1.22       |
|                  | 0.90        | 1.76       |
|                  | 0.95        | 2.18       |
|                  | 0.99        | 3.71       |
| **Double Hashing**   |             |            |
|                  | 0.50        | 1.12       |
|                  | 0.60        | 1.14       |
|                  | 0.70        | 1.20       |
|                  | 0.80        | 1.39       |
|                  | 0.90        | 2.05       |
|                  | 0.95        | 2.74       |
|                  | 0.99        | 4.27       |

### Word List

| Method           | Load Factor | Avg Probes |
|------------------|-------------|------------|
| **Linear Probing**   |             |            |
|                  | 0.50        | 1.60       |
|                  | 0.60        | 2.15       |
|                  | 0.70        | 3.60       |
|                  | 0.80        | 6.71       |
|                  | 0.90        | 19.81      |
|                  | 0.95        | 110.59     |
|                  | 0.99        | 471.67     |
| **Double Hashing**   |             |            |
|                  | 0.50        | 1.39       |
|                  | 0.60        | 1.53       |
|                  | 0.70        | 1.72       |
|                  | 0.80        | 2.02       |
|                  | 0.90        | 2.57       |
|                  | 0.95        | 3.19       |
|                  | 0.99        | 4.70       |

A careful eye will note that it appears that the Double Hashing hash method 
increases faster for the Random Dates than the same would with Linear Probing.
The best explanation that I can muster is that, given the table capacity
of 95,791, the `hashCode` of Java's `Date` objects provides a distribution that
mitigates the impact of clustering with Linear Probing. Consequently, the chance
is greater that we're simply seeing a statistical anomaly in the data used to
produce these results.

## Sources used

- [Java 8 API Docs](https://docs.oracle.com/javase/8/docs/api/)
- [ChatGPT-4](https://chat.openai.com/), for language in documentation and
assistance in Bash scripting.
- [Github Copilot](https://github.com/features/copilot), as a productivity
booster using small autocompletions of code I could already write without
assistance.
