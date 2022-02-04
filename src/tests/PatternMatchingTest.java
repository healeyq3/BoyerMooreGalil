import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Grading JUnits for Pattern Matching.
 *
 * DO NOT SHARE WITH STUDENTS.
 *
 * @author Robert Turko
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PatternMatchingTest {

    //*************************************************************************
    //***********************    INSTANCE VARIABLES    ************************
    //*************************************************************************

    private static final long TIMEOUT = 1000;
    private static int points;
    private static PatternMatchingTestUtil util;


    //*************************************************************************
    //************************    AUXILIARY METHODS    ************************
    //*************************************************************************

    @Before
    public void setup() {
        util = new PatternMatchingTestUtil();
    }

    @AfterClass
    public static void printResults() {
        System.out.println("Total Points: " + points);
    }

    //*************************************************************************
    //***********************    kmp (15 points)   ****************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void kmp01PatternException() {
        try {
            util.patternException(PatternMatchingTestUtil.Algorithm.KMP);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void kmp02TextException() {
        try {
            util.nullText(PatternMatchingTestUtil.Algorithm.KMP);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void kmp03ComparatorException() {
        try {
            util.nullComparator(PatternMatchingTestUtil.Algorithm.KMP);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp04TextEmpty() {
        /*
            pattern: somePattern
            text: -
            indices: -
            expected total comparisons: 0
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP,
            "somePattern", "", new Integer[] {}, 0);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp05PatternLong() {
        /*
            pattern: somePattern
            text: someText
            indices: -
            expected total comparisons: 0
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP,
            "somePattern", "someText", new Integer[] {}, 0);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp06NoMatch() {
        /*
            pattern: abcd
            text: baadbaccd
            indices: -
            expected total comparisons: 12
         */
        /*
            failure table: [0, 0, 0, 0]
            comparisons: 3
         */
        /*
        b | a | a | d | b | a | c | c | d
        --+---+---+---+---+---+---+---+---
        a | b | c | d |   |   |   |   |
        - |   |   |   |   |   |   |   |             comparisons: 1
          | a | b | c | d |   |   |   |
          | - | - |   |   |   |   |   |             comparisons: 2
          |   | a | b | c | d |   |   |
          |   | - | - |   |   |   |   |             comparisons: 2
          |   |   | a | b | c | d |   |
          |   |   | - |   |   |   |   |             comparisons: 1
          |   |   |   | a | b | c | d |
          |   |   |   | - |   |   |   |             comparisons: 1
          |   |   |   |   | a | b | c | d
          |   |   |   |   | - | - |   |             comparisons: 2
        total comparisons: 9
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP,
            "abcd", "baadbaccd", new Integer[] {}, 12);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp07NoMatchPatternAndTextDifferentAlphabet() {
        /*
            pattern: xyz
            text: abcdefg
            indices: -
            expected total comparisons: 7
         */
        /*
            failure table: [0, 0, 0]
            comparisons: 2
         */
        /*
        a | b | c | d | e | f | g
        --+---+---+---+---+---+---
        x | y | z |   |   |   |
        - |   |   |   |   |   |     comparisons: 1
          | x | y | z |   |   |
          | - |   |   |   |   |     comparisons: 1
          |   | x | y | z |   |
          |   | - |   |   |   |     comparisons: 1
          |   |   | x | y | z |
          |   |   | - |   |   |     comparisons: 1
          |   |   |   | x | y | z
          |   |   |   | - |   |     comparisons: 1
        comparisons: 5
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP,
            "xyz", "abcdefg", new Integer[] {}, 7);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp08AlphabetLengthOnePatternLengthOne() {
        /*
            pattern: a
            text: aaaaaaaa
            indices: 0, 1, 2, 3, 4, 5, 6, 7
            expected total comparisons: 8
         */
        /*
            failure table: [0]
            comparisons: 0
         */
        /*
         a | a | a | a | a | a | a | a
        ---+---+---+---+---+---+---+---
         a |   |   |   |   |   |   |
         - |   |   |   |   |   |   |         comparisons: 1
           | a |   |   |   |   |   |
           | - |   |   |   |   |   |         comparisons: 1
           |   | a |   |   |   |   |
           |   | - |   |   |   |   |         comparisons: 1
           |   |   | a |   |   |   |
           |   |   | - |   |   |   |         comparisons: 1
           |   |   |   | a |   |   |
           |   |   |   | - |   |   |         comparisons: 1
           |   |   |   |   | a |   |
           |   |   |   |   | - |   |         comparisons: 1
           |   |   |   |   |   | a |
           |   |   |   |   |   | - |         comparisons: 1
           |   |   |   |   |   |   | a
           |   |   |   |   |   |   | -       comparisons: 1
        comparisons: 8
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP, "a",
                "aaaaaaaa", new Integer[] {0, 1, 2, 3, 4, 5, 6, 7}, 8);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp09AlphabetLengthOnePatternLengthTwo() {
        /*
            pattern: aa
            text: aaaaaaaa
            indices: 0, 1, 2, 3, 4, 5, 6
            expected total comparisons: 9
         */
        /*
            failure table: [0, 1]
            comparisons: 1
         */
        /*
         a | a | a | a | a | a | a | a
        ---+---+---+---+---+---+---+---
         a | a |   |   |   |   |   |
         - | - |   |   |   |   |   |         comparisons: 2
           | a | a |   |   |   |   |
           |   | - |   |   |   |   |         comparisons: 1
           |   | a | a |   |   |   |
           |   |   | - |   |   |   |         comparisons: 1
           |   |   | a | a |   |   |
           |   |   |   | - |   |   |         comparisons: 1
           |   |   |   | a | a |   |
           |   |   |   |   | - |   |         comparisons: 1
           |   |   |   |   | a | a |
           |   |   |   |   |   | - |         comparisons: 1
           |   |   |   |   |   | a | a
           |   |   |   |   |   |   | -       comparisons: 1
        comparisons: 8
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP, "aa",
                "aaaaaaaa", new Integer[] {0, 1, 2, 3, 4, 5, 6}, 9);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp10SingleMatchBeginning() {
        /*
            pattern: com
            text: computerscience
            indices: 0
            expected total comparisons: 16
         */
        /*
            failure table: [0, 0, 0]
            comparisons: 2
         */
        /*
    c | o | m | p | u | t | e | r | s | c | i | e | n | c | e
    --+---+---+---+---+---+---+---+---+---+---+---+---+---+---
    c | o | m |   |   |   |   |   |   |   |   |   |   |   |
    - | - | - |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 3
      |   |   | c | o | m |   |   |   |   |   |   |   |   |
      |   |   | - |   |   |   |   |   |   |   |   |   |   |     comparisons: 1
      |   |   |   | c | o | m |   |   |   |   |   |   |   |
      |   |   |   | - |   |   |   |   |   |   |   |   |   |     comparisons: 1
      |   |   |   |   | c | o | m |   |   |   |   |   |   |
      |   |   |   |   | - |   |   |   |   |   |   |   |   |     comparisons: 1
      |   |   |   |   |   | c | o | m |   |   |   |   |   |
      |   |   |   |   |   | - |   |   |   |   |   |   |   |     comparisons: 1
      |   |   |   |   |   |   | c | o | m |   |   |   |   |
      |   |   |   |   |   |   | - |   |   |   |   |   |   |     comparisons: 1
      |   |   |   |   |   |   |   | c | o | m |   |   |   |
      |   |   |   |   |   |   |   | - |   |   |   |   |   |     comparisons: 1
      |   |   |   |   |   |   |   |   | c | o | m |   |   |
      |   |   |   |   |   |   |   |   | - | - |   |   |   |     comparisons: 2
      |   |   |   |   |   |   |   |   |   | c | o | m |   |
      |   |   |   |   |   |   |   |   |   | - |   |   |   |     comparisons: 1
      |   |   |   |   |   |   |   |   |   |   | c | o | m |
      |   |   |   |   |   |   |   |   |   |   | - |   |   |     comparisons: 1
      |   |   |   |   |   |   |   |   |   |   |   | c | o | m
      |   |   |   |   |   |   |   |   |   |   |   | - |   |     comparisons: 1
        comparisons: 14
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP, "com",
                "computerscience", new Integer[] {0}, 16);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp11SingleMatchEnding() {
        /*
            pattern: nce
            text: computerscience
            indices: 12
            expected total comparisons: 17
         */
        /*
            failure table: [0, 0, 0]
            comparisons: 2
         */
        /*
    c | o | m | p | u | t | e | r | s | c | i | e | n | c | e
    --+---+---+---+---+---+---+---+---+---+---+---+---+---+---
    n | c | e |   |   |   |   |   |   |   |   |   |   |   |
    - |   |   |   |   |   |   |   |   |   |   |   |   |   |    comparisons: 1
      | n | c | e |   |   |   |   |   |   |   |   |   |   |
      | - |   |   |   |   |   |   |   |   |   |   |   |   |    comparisons: 1
      |   | n | c | e |   |   |   |   |   |   |   |   |   |
      |   | - |   |   |   |   |   |   |   |   |   |   |   |    comparisons: 1
      |   |   | n | c | e |   |   |   |   |   |   |   |   |
      |   |   | - |   |   |   |   |   |   |   |   |   |   |    comparisons: 1
      |   |   |   | n | c | e |   |   |   |   |   |   |   |
      |   |   |   | - |   |   |   |   |   |   |   |   |   |    comparisons: 1
      |   |   |   |   | n | c | e |   |   |   |   |   |   |
      |   |   |   |   | - |   |   |   |   |   |   |   |   |    comparisons: 1
      |   |   |   |   |   | n | c | e |   |   |   |   |   |
      |   |   |   |   |   | - |   |   |   |   |   |   |   |    comparisons: 1
      |   |   |   |   |   |   | n | c | e |   |   |   |   |
      |   |   |   |   |   |   | - |   |   |   |   |   |   |    comparisons: 1
      |   |   |   |   |   |   |   | n | c | e |   |   |   |
      |   |   |   |   |   |   |   | - |   |   |   |   |   |    comparisons: 1
      |   |   |   |   |   |   |   |   | n | c | e |   |   |
      |   |   |   |   |   |   |   |   | - |   |   |   |   |    comparisons: 1
      |   |   |   |   |   |   |   |   |   | n | c | e |   |
      |   |   |   |   |   |   |   |   |   | - |   |   |   |    comparisons: 1
      |   |   |   |   |   |   |   |   |   |   | n | c | e |
      |   |   |   |   |   |   |   |   |   |   | - |   |   |    comparisons: 1
      |   |   |   |   |   |   |   |   |   |   |   | n | c | e
      |   |   |   |   |   |   |   |   |   |   |   | - | - | -  comparisons: 3
        comparisons: 15
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP, "nce",
                "computerscience", new Integer[] {12}, 17);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp12PatternEqualTest() {
        /*
            pattern: word
            text: word
            indices: 0
            expected total comparisons: 4 or 7 (depending on if failure table
                                        build)
         */
        /*
            failure table: [0, 0, 0, 0]
            comparisons: 3
         */
        /*
         w | o | r | d
        ---+---+---+---
         w | o | r | d
         - | - | - | -      comparisons: 4
         comparisons: 4
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP, "word",
            "word", new Integer[] {0}, 4, 7);

        /*
            pattern: worz
            text: word
            indices: 0
            expected total comparisons: 4 or 7 (depending on if failure table
                                        build)
         */
        /*
            failure table: [0, 0, 0, 0]
            comparisons: 3
         */
        /*
         w | o | r | d
        ---+---+---+---
         w | o | r | z
         - | - | - | -      comparisons: 4
         comparisons: 4
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP, "worz",
                "word", new Integer[] {}, 4, 7);

        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp13MultipleMatches() {
        /*
            pattern: abcd
            text: babcdababcdb
            indices: 1, 7
            expected total comparisons: 15
         */
        /*
            failure table: [0, 0, 0, 0]
            comparisons: 3
         */
        /*
        b | a | b | c | d | a | b | a | b | c | d | b
        --+---+---+---+---+---+---+---+---+---+---+---
        a | b | c | d |   |   |   |   |   |   |   |
        - |   |   |   |   |   |   |   |   |   |   |         comparisons: 1
          | a | b | c | d |   |   |   |   |   |   |
          | - | - | - | - |   |   |   |   |   |   |         comparisons: 4
          |   |   |   |   | a | b | c | d |   |   |
          |   |   |   |   | - | - | - |   |   |   |         comparisons: 3
          |   |   |   |   |   |   | a | b | c | d |
          |   |   |   |   |   |   | - | - | - | - |         comparisons: 4
        comparisons: 12
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP, "abcd",
                "babcdababcdb", new Integer[] {1, 7},
                15);

        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void kmp14OverlappingPattern() {
        /*
            pattern: abab
            text: babababa
            indices: 1, 3
            expected total comparisons: 10
         */
        /*
            failure table: [0, 0, 1, 2]
            comparisons: 3
         */
        /*
        b | a | b | a | b | a | b | a
        --+---+---+---+---+---+---+---
        a | b | a | b |   |   |   |
        - |   |   |   |   |   |   |     comparisons: 1
          | a | b | a | b |   |   |
          | - | - | - | - |   |   |     comparisons: 4
          |   |   | a | b | a | b |
          |   |   |   |   | - | - |     comparisons: 2
        comparisons: 7
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.KMP, "abab",
                "babababa", new Integer[] {1, 3}, 10);

        points += 1;
    }

    //*************************************************************************
    //****************   buildFailureTable (10 points)   **********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void failureTable01PatternException() {
        try {
            util.failureTableNullPattern();
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void failureTable02ComparatorException() {
        try {
            util.failureTableNullComparator();
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void failureTable03PatternEmpty() {
        /*
            pattern: -
            failure table: []
         */
        util.failureTableRunner("", new int[] {}, 0);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void failureTable04PatternLengthOne() {
        /*
            pattern: a
            failure table: [0]
            comparisons: 0
         */
        util.failureTableRunner("a", new int[] {0}, 0);

        points += 1;
    }
    // 1 point(s)

    @Test(timeout = TIMEOUT)
    public void failureTable05PatternUnique() {
        /*
            pattern: abc
            failure table: [0, 0, 0]
            comparisons: 2
         */
        util.failureTableRunner("abc", new int[] {0, 0, 0}, 2);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void failureTable06AlphabetLengthOne() {
        /*
            pattern: aaaaa
            failure table: [0, 1, 2, 3, 4]
            comparisons: 4
         */
        util.failureTableRunner("aaaaa", new int[] {0, 1, 2, 3, 4}, 4);

        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void failureTable07StrictlyIncreasing() {
        /*
            pattern: aabcabbaabe
            failure table: [0, 1, 0, 0, 1, 0, 0, 1, 2, 3, 0]
            comparisons: 13
         */
        util.failureTableRunner("aabcabbaabe",
                new int[] {0, 1, 0, 0, 1, 0, 0, 1, 2, 3, 0}, 13);

        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void failureTable08NotStrictlyIncreasing() {
        /*
            pattern: ababcababab
            failure table: [0, 0, 1, 2, 0, 1, 2, 3, 4, 3, 4]
            comparisons: 12
         */
        util.failureTableRunner("ababcababab",
                new int[] {0, 0, 1, 2, 0, 1, 2, 3, 4, 3, 4}, 12);

        points += 2;
    }

    //*************************************************************************
    //*******************   boyerMoore (15 points)   **************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void boyerMoore01PatternException() {
        try {
            util.patternException(PatternMatchingTestUtil.Algorithm.BOYERMOORE);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void boyerMoore02TextException() {
        try {
            util.nullText(PatternMatchingTestUtil.Algorithm.BOYERMOORE);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void boyerMoore03ComparatorException() {
        try {
            util.nullComparator(PatternMatchingTestUtil.Algorithm.BOYERMOORE);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore04TextEmpty() {
        /*
            pattern: somePattern
            text: -
            indices: -
            expected total comparisons: 0
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE,
                "somePattern", "", new Integer[] {}, 0);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore05PatternLong() {
        /*
            pattern: somePattern
            text: someText
            indices: -
            expected total comparisons: 0
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE,
                "somePattern", "someText", new Integer[] {}, 0);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore06NoMatch() {
        /*
            pattern: abcd
            text: baadbaccd
            indices: -
            expected total comparisons: 6
         */
        /*
            last table: {a : 0, b : 1, c : 2}
         */
        /*
        b | a | a | d | b | a | c | c | d
        --+---+---+---+---+---+---+---+---
        a | b | c | d |   |   |   |   |
          |   | - | - |   |   |   |   |         comparisons: 2
          |   | a | b | c | d |   |   |
          |   |   |   |   | - |   |   |         comparisons: 1
          |   |   |   |   | a | b | c | d
          |   |   |   |   |   | - | - | -       comparisons: 3
        comparisons: 6
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE,
                "abcd", "baadbaccd", new Integer[] {}, 6);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore07NoMatchPatternAndTextDifferentAlphabet() {
        /*
            pattern: xyz
            text: abcdefg
            indices: -
            expected total comparisons: 2
         */
        /*
            last table: {x : 0, y : 1, z : 2}
         */
        /*
        a | b | c | d | e | f | g
        --+---+---+---+---+---+---
        x | y | z |   |   |   |
          |   | - |   |   |   |     comparisons: 1
          |   |   | x | y | z |
          |   |   |   |   | - |     comparisons: 1
        comparisons: 2
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE,
                "xyz", "abcdefg", new Integer[] {}, 2);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore08AlphabetLengthOnePatternLengthOne() {
        /*
            pattern: a
            text: aaaaaaaa
            indices: 0, 1, 2, 3, 4, 5, 6, 7
            expected total comparisons: 8
         */
        /*
            last table: {a : 0}
         */
        /*
         a | a | a | a | a | a | a | a
        ---+---+---+---+---+---+---+---
         a |   |   |   |   |   |   |
         - |   |   |   |   |   |   |         comparisons: 1
           | a |   |   |   |   |   |
           | - |   |   |   |   |   |         comparisons: 1
           |   | a |   |   |   |   |
           |   | - |   |   |   |   |         comparisons: 1
           |   |   | a |   |   |   |
           |   |   | - |   |   |   |         comparisons: 1
           |   |   |   | a |   |   |
           |   |   |   | - |   |   |         comparisons: 1
           |   |   |   |   | a |   |
           |   |   |   |   | - |   |         comparisons: 1
           |   |   |   |   |   | a |
           |   |   |   |   |   | - |         comparisons: 1
           |   |   |   |   |   |   | a
           |   |   |   |   |   |   | -       comparisons: 1
        comparisons: 8
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE, "a",
                "aaaaaaaa", new Integer[] {0, 1, 2, 3, 4, 5, 6, 7}, 8);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore09AlphabetLengthOnePatternLengthTwo() {
        /*
            pattern: aa
            text: aaaaaaaa
            indices: 0, 1, 2, 3, 4, 5, 6
            expected total comparisons: 14
         */
        /*
            last table: {a : 1}
         */
        /*
         a | a | a | a | a | a | a | a
        ---+---+---+---+---+---+---+---
         a | a |   |   |   |   |   |
         - | - |   |   |   |   |   |         comparisons: 2
           | a | a |   |   |   |   |
           | - | - |   |   |   |   |         comparisons: 2
           |   | a | a |   |   |   |
           |   | - | - |   |   |   |         comparisons: 2
           |   |   | a | a |   |   |
           |   |   | - | - |   |   |         comparisons: 2
           |   |   |   | a | a |   |
           |   |   |   | - | - |   |         comparisons: 2
           |   |   |   |   | a | a |
           |   |   |   |   | - | - |         comparisons: 2
           |   |   |   |   |   | a | a
           |   |   |   |   |   | - | -       comparisons: 2
        comparisons: 14
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE, "aa",
                "aaaaaaaa", new Integer[] {0, 1, 2, 3, 4, 5, 6}, 14);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore10SingleMatchBeginning() {
        /*
            pattern: com
            text: computerscience
            indices: 0
            expected total comparisons: 8
         */
        /*
            last table: {c : 0, o : 1, m : 2}
         */
        /*
    c | o | m | p | u | t | e | r | s | c | i | e | n | c | e
    --+---+---+---+---+---+---+---+---+---+---+---+---+---+---
    c | o | m |   |   |   |   |   |   |   |   |   |   |   |
    - | - | - |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 3
      | c | o | m |   |   |   |   |   |   |   |   |   |   |
      |   |   | - |   |   |   |   |   |   |   |   |   |   |     comparisons: 1
      |   |   |   | c | o | m |   |   |   |   |   |   |   |
      |   |   |   |   |   | - |   |   |   |   |   |   |   |     comparisons: 1
      |   |   |   |   |   |   | c | o | m |   |   |   |   |
      |   |   |   |   |   |   |   |   | - |   |   |   |   |     comparisons: 1
      |   |   |   |   |   |   |   |   | c | o | m |   |   |
      |   |   |   |   |   |   |   |   |   |   | - |   |   |     comparisons: 1
      |   |   |   |   |   |   |   |   |   |   |   | c | o | m
      |   |   |   |   |   |   |   |   |   |   |   |   |   | -   comparisons: 1
        comparisons: 8
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE, "com",
                "computerscience", new Integer[] {0}, 8);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore11SingleMatchEnding() {
        /*
            pattern: nce
            text: computerscience
            indices: 12
            expected total comparisons: 9
         */
        /*
            last table: {n : 0, c : 1, e : 2}
         */
        /*
    c | o | m | p | u | t | e | r | s | c | i | e | n | c | e
    --+---+---+---+---+---+---+---+---+---+---+---+---+---+---
    n | c | e |   |   |   |   |   |   |   |   |   |   |   |
      |   | - |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 1
      |   |   | n | c | e |   |   |   |   |   |   |   |   |
      |   |   |   |   | - |   |   |   |   |   |   |   |   |     comparisons: 1
      |   |   |   |   |   | n | c | e |   |   |   |   |   |
      |   |   |   |   |   |   |   | - |   |   |   |   |   |     comparisons: 1
      |   |   |   |   |   |   |   |   | n | c | e |   |   |
      |   |   |   |   |   |   |   |   |   | - | - |   |   |     comparisons: 2
      |   |   |   |   |   |   |   |   |   |   | n | c | e |
      |   |   |   |   |   |   |   |   |   |   |   |   | - |     comparisons: 1
      |   |   |   |   |   |   |   |   |   |   |   | n | c | e
      |   |   |   |   |   |   |   |   |   |   |   | - | - | -   comparisons: 3
        comparisons: 9
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE, "nce",
                "computerscience", new Integer[] {12}, 9);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore12PatternEqualTest() {
        /*
            pattern: word
            text: word
            indices: 0
            expected total comparisons: 4
         */
        /*
            last table: {w : 0, o : 1, r : 2, d : 3}
         */
        /*
         w | o | r | d
        ---+---+---+---
         w | o | r | d
         - | - | - | -      comparisons: 4
         comparisons: 4
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE, "word",
                "word", new Integer[] {0}, 4);

        /*
            pattern: worz
            text: word
            indices: 0
            expected total comparisons: 4
         */
        /*
            last table: {w : 0, o : 1, r : 2, d : 3}
         */
        /*
         w | o | r | d
        ---+---+---+---
         w | o | r | z
           |   |   | -      comparisons: 1 or 4 (brute force)
         comparisons: 4
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE, "worz",
                "word", new Integer[] {}, 1, 4);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore13MultipleMatches() {
        /*
            pattern: abcd
            text: babcdababcdb
            indices: 1, 7
            expected total comparisons: 12
         */
        /*
            last table: {a : 0, b : 1, c : 2, d : 3}
         */
        /*
        b | a | b | c | d | a | b | a | b | c | d | b
        --+---+---+---+---+---+---+---+---+---+---+---
        a | b | c | d |   |   |   |   |   |   |   |
          |   |   | - |   |   |   |   |   |   |   |         comparisons: 1
          | a | b | c | d |   |   |   |   |   |   |
          | - | - | - | - |   |   |   |   |   |   |         comparisons: 4
          |   | a | b | c | d |   |   |   |   |   |
          |   |   |   |   | - |   |   |   |   |   |         comparisons: 1
          |   |   |   |   | a | b | c | d |   |   |
          |   |   |   |   |   |   |   | - |   |   |         comparisons: 1
          |   |   |   |   |   |   | a | b | c | d |
          |   |   |   |   |   |   | - | - | - | - |         comparisons: 4
          |   |   |   |   |   |   |   | a | b | c | d
          |   |   |   |   |   |   |   |   |   |   | -       comparisons: 1
        comparisons: 13
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE, "abcd",
                "babcdababcdb", new Integer[] {1, 7}, 12);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore14OverlappingPattern() {
        /*
            pattern: abab
            text: babababa
            indices: 1, 3
            expected total comparisons: 11
         */
        /*
            last table: {a : 2, b : 3}
         */
        /*
        b | a | b | a | b | a | b | a
        --+---+---+---+---+---+---+---
        a | b | a | b |   |   |   |
          |   |   | - |   |   |   |     comparisons: 1
          | a | b | a | b |   |   |
          | - | - | - | - |   |   |     comparisons: 4
          |   | a | b | a | b |   |
          |   |   |   |   | - |   |     comparisons: 1
          |   |   | a | b | a | b |
          |   |   | - | - | - | - |     comparisons: 4
          |   |   |   | a | b | a | b
          |   |   |   |   |   |   | -   comparisons: 1
        comparisons: 11
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE, "abab",
                "babababa", new Integer[] {1, 3}, 11);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void boyerMoore15BackwardsShift() {
        /*
            pattern: abcd
            text: babadcdabcda
            indices: 7
            expected total comparisons: 10
         */
        /*
            last table: {a : 0, b : 1, c : 2, d : 3}
         */
        /*
        b | a | b | a | d | c | d | a | b | c | d | a
        --+---+---+---+---+---+---+---+---+---+---+---
        a | b | c | d |   |   |   |   |   |   |   |
          |   |   | - |   |   |   |   |   |   |   |     comparisons: 1
          |   |   | a | b | c | d |   |   |   |   |
          |   |   |   | - | - | - |   |   |   |   |     comparisons: 3
          |   |   |   | a | b | c | d |   |   |   |
          |   |   |   |   |   |   | - |   |   |   |     comparisons: 1
          |   |   |   |   |   |   | a | b | c | d |
          |   |   |   |   |   |   | - | - | - | - |     comparisons: 4
          |   |   |   |   |   |   |   | a | b | c | d
          |   |   |   |   |   |   |   |   |   |   | -   comparisons: 1
        comparisons: 10
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.BOYERMOORE, "abcd",
                "babadcdabcda", new Integer[] {7}, 10);

        points += 1;
    }

    //*************************************************************************
    //*******************   buildLastTable (10 points)   **********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void lastTable01NullException() {
        try {
            util.lastTableNullPattern();
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void lastTable02PatternEmpty() {
        /*
            pattern: -
            last table: {}
         */
        util.lastTableRunner("", new char[] {}, new int[] {});

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void lastTable03PatternLengthOne() {
        /*
            pattern: a
            last table: {a : 0}
         */
        util.lastTableRunner("a", new char[] {'a'}, new int[] {0});

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void lastTable04PatternUnique() {
        /*
            pattern: abc
            last table: {a : 0, b : 1, c : 2}
         */
        util.lastTableRunner("abc", new char[] {'a', 'b', 'c'},
                new int[] {0, 1, 2});

        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void lastTable05AlphabetLengthOne() {
        /*
            pattern: aaaaa
            last table: {a : 4}
         */
        util.lastTableRunner("aaaaa", new char[] {'a'}, new int[] {4});

        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void lastTable06DuplicatePattern() {
        /*
            pattern: abacb
            last table: {a : 2, b : 4, c : 3}
         */
        util.lastTableRunner("abacb", new char[] {'a', 'b', 'c'},
                new int[] {2, 4, 3});

        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void lastTable07MultipleDuplicatePattern() {
        /*
            pattern: computerscience
            last table: {c : 13, o : 1, m : 2, p : 3, u : 4, t : 5, e : 14,
                r : 7, s : 8, i : 10, n : 12}
         */
        util.lastTableRunner("computerscience",
                new char[] {'c', 'o', 'm', 'p', 'u', 't', 'e', 'r', 's', 'i',
                            'n'},
                new int[] {13, 1, 2, 3, 4, 5, 14, 7, 8, 10, 12});

        points += 2;
    }

    //*************************************************************************
    //********************   rabinKarp (25 points)   **************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void rabinKarp01PatternException() {
        try {
            util.patternException(PatternMatchingTestUtil.Algorithm.RABINKARP);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void rabinKarp02TextException() {
        try {
            util.nullText(PatternMatchingTestUtil.Algorithm.RABINKARP);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void rabinKarp03ComparatorException() {
        try {
            util.nullComparator(PatternMatchingTestUtil.Algorithm.RABINKARP);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp04EmptyText() {
        /*
            pattern: somePattern
            text: -
            indices: -
            expected total comparisons: 0
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP,
                "somePattern", "", new Integer[] {}, 0);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp05LongPattern() {
        /*
            pattern: somePattern
            text: someText
            indices: -
            expected total comparisons: 0
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP,
                "somePattern", "someText", new Integer[] {}, 0);

        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp06NoMatch() {
        /*
            pattern: abcd
            text: baadbaccd
            indices: -
            expected total comparisons: 0
         */
        /*
            pattern hash: 141223658
         */
        /*
        b | a | a | d | b | a | c | c | d
        --+---+---+---+---+---+---+---+---
        a | b | c | d |   |   |   |   |         hash: 142653560
          |   |   |   |   |   |   |   |         comparisons: 0
          | a | b | c | d |   |   |   |         hash: 141211000
          |   |   |   |   |   |   |   |         comparisons: 0
          |   | a | b | c | d |   |   |         hash: 141249080
          |   |   |   |   |   |   |   |         comparisons: 0
          |   |   | a | b | c | d |   |         hash: 145552122
          |   |   |   |   |   |   |   |         comparisons: 0
          |   |   |   | a | b | c | d |         hash: 142653785
          |   |   |   |   |   |   |   |         comparisons: 0
          |   |   |   |   | a | b | c | d       hash: 141236427
          |   |   |   |   |   |   |   |         comparisons: 0
        comparisons: 0
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP, "abcd",
                "baadbaccd", new Integer[] {}, 0);

        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp07NoMatchPatternAndTextDifferentAlphabet() {
        /*
            pattern: xyz
            text: abcdefg
            indices: -
            expected total comparisons: 0
         */
        /*
            pattern hash: 1546075
         */
        /*
        a | b | c | d | e | f | g
        --+---+---+---+---+---+---
        x | y | z |   |   |   |     hash: 1249766
          |   |   |   |   |   |     comparisons: 0
          | x | y | z |   |   |     hash: 1262649
          |   |   |   |   |   |     comparisons: 0
          |   | x | y | z |   |     hash: 1275532
          |   |   |   |   |   |     comparisons: 0
          |   |   | x | y | z |     hash: 1288415
          |   |   |   |   |   |     comparisons: 0
          |   |   |   | x | y | z   hash: 1301298
          |   |   |   |   |   |     comparisons: 0
        comparisons: 0
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP, "xyz",
                "abcdefg", new Integer[] {}, 0);

        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp08AlphabetLengthOnePatternLengthOne() {
        /*
            pattern: a
            text: aaaaaaaa
            indices: 0, 1, 2, 3, 4, 5, 6, 7
            expected total comparisons: 8
         */
        /*
            pattern hash: 97
         */
        /*
         a | a | a | a | a | a | a | a
        ---+---+---+---+---+---+---+---
         a |   |   |   |   |   |   |        hash: 97
         - |   |   |   |   |   |   |        comparisons: 1
           | a |   |   |   |   |   |        hash: 97
           | - |   |   |   |   |   |        comparisons: 1
           |   | a |   |   |   |   |        hash: 97
           |   | - |   |   |   |   |        comparisons: 1
           |   |   | a |   |   |   |        hash: 97
           |   |   | - |   |   |   |        comparisons: 1
           |   |   |   | a |   |   |        hash: 97
           |   |   |   | - |   |   |        comparisons: 1
           |   |   |   |   | a |   |        hash: 97
           |   |   |   |   | - |   |        comparisons: 1
           |   |   |   |   |   | a |        hash: 97
           |   |   |   |   |   | - |        comparisons: 1
           |   |   |   |   |   |   | a      hash: 97
           |   |   |   |   |   |   | -      comparisons: 1
        comparisons: 8
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP, "a",
                "aaaaaaaa", new Integer[] {0, 1, 2, 3, 4, 5, 6, 7}, 8);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp09AlphabetLengthOnePatternLengthTwo() {
        /*
            pattern: aa
            text: aaaaaaaa
            indices: 0, 1, 2, 3, 4, 5, 6
            expected total comparisons: 14
         */
        /*
            pattern hash: 11058
         */
        /*
         a | a | a | a | a | a | a | a
        ---+---+---+---+---+---+---+---
         a | a |   |   |   |   |   |        hash: 11058
         - | - |   |   |   |   |   |        comparisons: 2
           | a | a |   |   |   |   |        hash: 11058
           | - | - |   |   |   |   |        comparisons: 2
           |   | a | a |   |   |   |        hash: 11058
           |   | - | - |   |   |   |        comparisons: 2
           |   |   | a | a |   |   |        hash: 11058
           |   |   | - | - |   |   |        comparisons: 2
           |   |   |   | a | a |   |        hash: 11058
           |   |   |   | - | - |   |        comparisons: 2
           |   |   |   |   | a | a |        hash: 11058
           |   |   |   |   | - | - |        comparisons: 2
           |   |   |   |   |   | a | a      hash: 11058
           |   |   |   |   |   | - | -      comparisons: 2
        comparisons: 14
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP, "aa",
                "aaaaaaaa", new Integer[] {0, 1, 2, 3, 4, 5, 6}, 14);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp10SingleMatchBeginning() {
        /*
            pattern: com
            text: computerscience
            indices: 0
            expected total comparisons: 3
         */
        /*
            pattern hash: 1276783
         */
        /*
    c | o | m | p | u | t | e | r | s | c | i | e | n | c | e
    --+---+---+---+---+---+---+---+---+---+---+---+---+---+---
    c | o | m |   |   |   |   |   |   |   |   |   |   |   |     hash: 1276783
    - | - | - |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 3
      | c | o | m |   |   |   |   |   |   |   |   |   |   |     hash: 1429788
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   | c | o | m |   |   |   |   |   |   |   |   |   |     hash: 1404594
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   | c | o | m |   |   |   |   |   |   |   |   |     hash: 1443465
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   | c | o | m |   |   |   |   |   |   |   |     hash: 1507182
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   | c | o | m |   |   |   |   |   |   |     hash: 1492731
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   | c | o | m |   |   |   |   |   |     hash: 1302666
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   | c | o | m |   |   |   |   |     hash: 1468760
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   | c | o | m |   |   |   |     hash: 1479727
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   |   | c | o | m |   |   |     hash: 1276097
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   |   |   | c | o | m |   |     hash: 1352268
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   |   |   |   | c | o | m |     hash: 1302198
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   |   |   |   |   | c | o | m   hash: 1415878
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
        comparisons: 3
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP, "com",
                "computerscience", new Integer[] {0}, 3);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp11SingleMatchEnding() {
        /*
            pattern: nce
            text: computerscience
            indices: 12
            expected total comparisons: 3
         */
        /*
            pattern hash: 1415878
         */
        /*
    c | o | m | p | u | t | e | r | s | c | i | e | n | c | e
    --+---+---+---+---+---+---+---+---+---+---+---+---+---+---
    n | c | e |   |   |   |   |   |   |   |   |   |   |   |     hash: 1276783
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      | n | c | e |   |   |   |   |   |   |   |   |   |   |     hash: 1429788
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   | n | c | e |   |   |   |   |   |   |   |   |   |     hash: 1404594
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   | n | c | e |   |   |   |   |   |   |   |   |     hash: 1443465
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   | n | c | e |   |   |   |   |   |   |   |     hash: 1507182
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   | n | c | e |   |   |   |   |   |   |     hash: 1492731
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   | n | c | e |   |   |   |   |   |     hash: 1302666
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   | n | c | e |   |   |   |   |     hash: 1468760
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   | n | c | e |   |   |   |     hash: 1479727
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   |   | n | c | e |   |   |     hash: 1276097
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   |   |   | n | c | e |   |     hash: 1352268
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   |   |   |   | n | c | e |     hash: 1302198
      |   |   |   |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
      |   |   |   |   |   |   |   |   |   |   |   | n | c | e   hash: 1415878
      |   |   |   |   |   |   |   |   |   |   |   | - | - | -   comparisons: 3
        comparisons: 3
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP, "nce",
                "computerscience", new Integer[] {12}, 3);

        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp12PatternEqualTest() {
        /*
            pattern: word
            text: word
            indices: 0
            expected total comparisons: 4
         */
        /*
            pattern hash: 173135084
         */
        /*
         w | o | r | d
        ---+---+---+---
         w | o | r | d      hash: 173135084
         - | - | - | -      comparisons: 4
         comparisons: 4
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP, "word",
                "word", new Integer[] {0}, 4);

        /*
            pattern (int values): 1, 0, 113
            text (int values): 0, 113, 113
            indices: -
            expected total comparisons: 1
         */
        /*
            pattern hash: 12882
         */
        /*
          0  | 113 | 113 |
        -----+-----+-------
          1  |  0  | 113 |        hash: 12882
          -  |     |     |        comparisons: 1
        comparisons: 1
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP,
                util.createSpecificString(1, 0, 113),
                util.createSpecificString(0, 113, 113),
                new Integer[] {}, 1);

        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp13MultipleMatches() {
        /*
            pattern: abcd
            text: babcdababcdb
            indices: 1, 7
            expected total comparisons: 8
         */
        /*
            pattern hash: 141223658
         */
        /*
        b | a | b | c | d | a | b | a | b | c | d | b
        --+---+---+---+---+---+---+---+---+---+---+---
        a | b | c | d |   |   |   |   |   |   |   |     hash: 142653672
          |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
          | a | b | c | d |   |   |   |   |   |   |     hash: 141223658
          | - | - | - | - |   |   |   |   |   |   |     comparisons: 4
          |   | a | b | c | d |   |   |   |   |   |     hash: 142679434
          |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
          |   |   | a | b | c | d |   |   |   |   |     hash: 144134762
          |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
          |   |   |   | a | b | c | d |   |   |   |     hash: 145539464
          |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
          |   |   |   |   | a | b | c | d |   |   |     hash: 141223430
          |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
          |   |   |   |   |   | a | b | c | d |   |     hash: 142653672
          |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
          |   |   |   |   |   |   | a | b | c | d |     hash: 141223658
          |   |   |   |   |   |   | - | - | - | - |     comparisons: 4
          |   |   |   |   |   |   |   | a | b | c | d   hash: 142679435
          |   |   |   |   |   |   |   |   |   |   |     comparisons: 0
        comparisons: 8
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP, "abcd",
                "babcdababcdb", new Integer[] {1, 7}, 8);

        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp14OverlappingPattern() {
        /*
            pattern: abab
            text: babababa
            indices: 1, 3
            expected total comparisons: 8
         */
        /*
            pattern hash: 141223430
         */
        /*
        b | a | b | a | b | a | b | a
        --+---+---+---+---+---+---+---
        a | b | a | b |   |   |   |     hash: 142653670
          |   |   |   |   |   |   |     comparisons: 0
          | a | b | a | b |   |   |     hash: 141223430
          | - | - | - | - |   |   |     comparisons: 4
          |   | a | b | a | b |   |     hash: 142653670
          |   |   |   |   |   |   |     comparisons: 0
          |   |   | a | b | a | b |     hash: 141223430
          |   |   | - | - | - | - |     comparisons: 4
          |   |   |   | a | b | a | b   hash: 142653670
          |   |   |   |   |   |   |     comparisons: 0
        comparisons: 8
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP, "abab",
                "babababa", new Integer[] {1, 3}, 8);

        points += 2;
    }

    // 4 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp15EqualHashNonEqualString() {
        /*
            pattern (int values): 1, 0, 113
            text (int values): 0, 0, 113, 113, 0
            indices: -
            expected total comparisons: 1
         */
        /*
            pattern hash: 12882
         */
        /*
          0  |  0  | 113 | 113 |  0
        -----+-----+-----+-----+-----
          1  |  0  | 113 |     |        hash: 113
             |     |     |     |        comparisons: 0
             |  1  |  0  | 113 |        hash: 12882
             |  -  |     |     |        comparisons: 1
             |     |  1  |  0  | 113    hash: 1455666
             |     |     |     |        comparisons: 0
        comparisons: 1
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP,
                util.createSpecificString(1, 0, 113),
                util.createSpecificString(0, 0, 113, 113, 0),
                new Integer[] {}, 1);

        points += 4;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void rabinKarp16BasePowerOverflow() {
        /*
            pattern (int values): 1, 1, 1, 1, 1
            text (int values): 113, 1, 1, 1, 1, 1
            indices: 1
            expected total comparisons: 5

            BASE^m overflows when pattern is length 5 (113^5 > 2147483647)
            Tests if BASE^(m-1) is calculated correctly when BASE^m overflows
            (i.e. BASE^(m-1) NOT calculated with BASE^m/BASE)
         */
        /*
            pattern hash = 164503141
         */
        /*
         113 |  1  |  1  |  1  |  1  |  1
        -----+-----+-----+-----+-----+-----
          1  |  1  |  1  |  1  |  1  |      hash: 1245938389
             |     |     |     |     |      comparisons: 0
             |  1  |  1  |  1  |  1  |  1   hash: 164503141
             |  -  |  -  |  -  |  -  |  -   comparisons: 5

        comparisons: 5
         */
        util.searchRunner(PatternMatchingTestUtil.Algorithm.RABINKARP,
                util.createSpecificString(1, 1, 1, 1, 1),
                util.createSpecificString(113, 1, 1, 1, 1, 1),
                new Integer[] {1}, 5);

        points += 2;
    }
}