Notation:

p: represents the pattern string of length m. I will index the pattern with the notation "p[i]" for a single element or "p[i, j]" for the inclusive range of elements from index i to index j. Note that 0 <= i < j < m.

k: the "period" of a string; |k| >= 1

t: represents the text string of length n. I will index the text with the notation "t[x]" for a single element or "t[x, y]" for the inclusive range of elements from index x to index y. Note that 0 <= x < y < n.

f: the array returned by the "FailureTableClass" as detailed in the Knuth-Morris-Pratt algorithm (briefly explained below).

**Please note that the indices i, j and x, z in this README have no relation to any indices i, j or x, y in the code**

The BoyerMooreGalil Repository contains two primary folders. The first is titled "Research," where I have uploaded the respective research papers, as well as my notes on the algorithm(s). The second folder is simply "src," where the code is housed. This README file will focus on the latter.

Within the src folder there are eight .java files. The "CharacterComparator.java" file consists of the CharacterComparator class that the GaTech CS 1332 TAs wrote to enable direct comparisons between character elements. "BoyerMooreTests.java" currently contains a variety of JUnit tests, most of which were written by the CS 1332 TAs or Ruston Shome. I have made some modifications to the tests, however, this file requires the most work. My previous "main," so to speak, implementation used the bad character heuristic, good suffix heurisitc, and Galil rule, requiring that the tests adjust for a differing (mostly fewer) number of character comparisons. However, as I will explain below, my new "main" implementation involving Galil's optimization technique uses just the bad character heuristic upon mismathes. 

The "FailureTable.java," "GoodSuffixPreprocessing.java," and "LastOccurrenceTable.java" files are the three respective preprocessing techniques that the varying implementations of the BM algorithm use. Because we are no longer concerned with the good suffix rule the GoodSuffixPreprocessing class can be ignored. The FailureTable class contains a build method which returns an array whose ith element is the length of the maximum matching proper prefix which is also a suffix of the sub-pattern p[0, i]. This "failure table" will be crucial in implementing the Galil rule. The LastOccurrenceTable class, likewise, contains a build method which will return a Map whose key belongs to the Character class and value belongs to the Integer class. If you wish to learn more about this map see the bad character heurisitic. I will assume knowledge of it as a prerequisite.

Also in the src folder are two alternate implementations of the BM algorithm. The implementation in "BoyerMooreGoodSuffix.java" uses both the bad character heuristic as well the good suffix heuristic. However, it does not contain the Galil rule. "BoyerMooreComplete.java" has an implementation which uses all three techniques. Please note that since we are no longer concered with the good suffix heuristic these files are deprecated, so to speak. I have not concerned myself with updating their comments or tidying up the code.  

"BoyerMooreBCGalil.java" is the heart of this project. "BC" stands for "bad character" and "Galil" obviously implies the use of Zvi's optimization technique. Again, because I'm assuming the reader is already familiar with the bad character heuristic, I will not explain how the search algorithm's mismatch shifting scheme works. Instead I will focus on how we now handle a match. (Note that I will briefly explain the "failure table," however the reader can see the Knuth-Morris-Pratt pattern matching algorithm for more details).

Now, what is Zvi Galil's optimization technique? In his paper, Galil discusses how the time-complexity of the Boyer Moore algorithm is worst case nonlinear when a pattern matches with a substring of the text and the pattern itself is periodic. Or defined properly, when a pattern is the prefix of a repeating string. In order to reduce the time complexity to worst case linear, Galil suggests that when there is a match between t[x, y] and p[i, j], given that p is periodic with period |k|, the search algorithm shifts the pattern forward by |k| indices and then checks only the last |k| (recall that the BM algorithm checks characters from right to left).

Moving onto the implementation itself, there are two main facets of Galil's modification. The first is determining whether or not a pattern is periodic. The second is adjusting the shift framework so that when a pattern p matches with text t we exploit p's periodicity to reduce the number of comparisons between p and t. We will determine p's period, k, with the failure table. As stated above, the failure table is an array whose entry f[i] is the length of the maximum matching proper prefix which is also a suffix of the sub-pattern p[0, i]. As an example take the pattern:

p[0, m - 1] = [a, b, c, d, e, f, a, b, c]. Its failure table would be

f[0, m - 1] = [0, 0, 0, 0, 0, 0, 1, 2, 3]. 

To understand this result observe that p[0, 2] = p[m - 3, m - 1]. This is a range of three elements. Therefore f[m - 1] = 3.
Now, recall that Galil's shifting scheme works when our pattern is a prefix of a repeating string (periodic). Let us define two sub-cases:
1. k, the period, or rather the shortest prefix of the repeating string, is completed (and repeated) within the pattern. For example: p = [a, b, a, b, a]; k = [a, b] => |k| = 2.
2. k is not completed within the pattern. For example, take p = [a, b, c, d, e, f, a, b, c]. Now the only elements that repeated are [a, b, c], however, k = [a, b, c, d, e, f] => |k| = 6.
Observing these two cases it becomes clear that f[m - 1] = m - k. Therefore, to get the period, simply use the equation k = m - f[m - 1] = m - (m - k) = k. (Credit should be given to the CS 1332 TAs for this neat formula).

After determing the value of |k| the remaining code is simple. Therefore, I will not go into excruciating detail here. The reader will find that the code itself is well documented. However, just to frame the reader's thinking: recall that the BM algorithm checks for character matches from right to left, or rather from index m - 1 to index 0. However, upon matches with a periodic pattern p, we only need to check the last |k| elements. To make this adjustment we simply have to create an end comparison index "l" and flag variable "Galil." The loop variable will be the index to which the algorithm searches to, and the flag variable will be used to determine which index to set "l" to and how far to shift our pattern by upon matching. 

I hope that this overview of the repository and main algorithms gives the reader enough background to now foray into the code itself. Best of luck!


