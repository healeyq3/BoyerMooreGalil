import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Grading util for PatternMatching.
 *
 * DO NOT SHARE WITH STUDENTS.
 *
 * @author David Wang
 */
class PatternMatchingTestUtil {

    //*************************************************************************
    //***********************    INSTANCE VARIABLES    ************************
    //*************************************************************************

    private List<Integer> expected;
    private List<Integer> actual;
    private int[] expectedFailureTable;
    private int[] actualFailureTable;
    private Map<Character, Integer> expectedLastTable;
    private Map<Character, Integer> actualLastTable;
    private CharacterComparator comparator;

    //*************************************************************************
    //************************    AUXILIARY METHODS    ************************
    //*************************************************************************

    // HELPER METHODS

    /**
     * Creates a specific - non ascii - string used to create equal rolling
     * hashing without being equal strings
     *
     * @param charsValues int values of each char
     * @return the specified string
     */
    String createSpecificString(int... charsValues) {
        StringBuilder result = new StringBuilder();
        for (int x : charsValues) {
            result.append((char) x);
        }
        return result.toString();
    }

    /**
     * Converts the expected and actual lists to a string
     *
     * @return the stringified version of the lists
     */
    private String getContent() {
        String result = "Expected: " + expected;
        result += "\n";
        result += "Actual: " + actual;
        return result;
    }

    /**
     * Converts the expected and actual failure tables to a string
     *
     * @return the stringified version of the failure tables
     */
    private String getFailureTableContent() {
        String result = "Expected: " + Arrays.toString(expectedFailureTable);
        result += "\n";
        result += "Actual: " + Arrays.toString(actualFailureTable);
        return result;
    }

    /**
     * Converts the expected and actual last tables to a string
     *
     * @return the stringified version of the last tables
     */
    private String getLastTableContent() {
        String result = "Expected: " + expectedLastTable;
        result += "\n";
        result += "Actual: " + actualLastTable;
        return result;
    }

    /**
     * Runs a search algorithm on a pattern and text
     *
     * @param search  name of the search
     * @param pattern pattern to search for
     * @param text    text to search in
     */
    private void search(PatternMatchingTestUtil.Algorithm search,
                        String pattern, String text) {
        switch (search) {
        case KMP:
            actual = PatternMatching.kmp(pattern, text, comparator);
            break;
        case BOYERMOORE:
            actual = PatternMatching.boyerMoore(pattern, text, comparator);
            break;
        case RABINKARP:
            actual = PatternMatching.rabinKarp(pattern, text, comparator);
            break;
        default:
            throw new IllegalArgumentException("Search type is incorrect.");
        }
    }

    // EXCEPTION METHODS

    /**
     * Tests pattern matching with an invalid pattern
     *
     * @param search name of search to run
     */
    void patternException(PatternMatchingTestUtil.Algorithm search) {
        comparator = new CharacterComparator();
        try {
            // Null pattern
            search(search, null, "text");
        } catch (IllegalArgumentException e1) {
            assertEquals(IllegalArgumentException.class, e1.getClass());
            assertComparisons(0);
            try {
                // Empty pattern
                search(search, "", "text");
            } catch (IllegalArgumentException e2) {
                assertEquals(IllegalArgumentException.class, e2.getClass());
                assertComparisons(0);
                throw e2;
            }
        }
    }

    /**
     * Tests pattern matching with a null text
     *
     * @param search name of search to run
     */
    void nullText(PatternMatchingTestUtil.Algorithm search) {
        comparator = new CharacterComparator();
        try {
            // Null text
            search(search, "pattern", null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertComparisons(0);
            throw e;
        }
    }

    /**
     * Tests pattern matching with a null comparator
     *
     * @param search name of search to run
     */
    void nullComparator(PatternMatchingTestUtil.Algorithm search) {
        comparator = null;
        try {
            // Null comparator
            search(search, "a", "aaa");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            throw e;
        }
    }

    /**
     * Tests last table with null pattern
     */
    void failureTableNullPattern() {
        comparator = new CharacterComparator();
        try {
            // Null pattern
            PatternMatching.buildFailureTable(null, new CharacterComparator());
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertComparisons(0);
            throw e;
        }
    }

    /**
     * Tests last table with null comparator
     */
    void failureTableNullComparator() {
        comparator = new CharacterComparator();
        try {
            // Null comparator
            PatternMatching.buildFailureTable("pattern", null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertComparisons(0);
            throw e;
        }
    }

    /**
     * Tests last table with null pattern
     */
    void lastTableNullPattern() {
        try {
            // Null pattern
            PatternMatching.buildLastTable(null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            throw e;
        }
    }


    // ASSERT METHODS

    /**
     * Checks that the number of times the comparator has been used is valid
     *
     * @param expectedCount expected number of usage
     */
    private void assertComparisons(Integer... expectedCount) {
        int count = comparator.getComparisonCount();
        List<Integer> list = Arrays.asList(expectedCount);
        if (!list.contains(0)) {
            assertTrue("Comparator was not used", count > 0);
        }
        String message = "\n" + "Acceptable comparisons: "
            + Arrays.toString(expectedCount) + "\n" + "Actual comparisons: "
            + count + "\n";
        assertTrue("Number of comparator usage was different than expected"
                + message, list.contains(count));
    }

    /**
     * Checks that the expected indices are valid
     */
    private void assertActualEquals() {
        if (expected == null) {
            throw new IllegalStateException("Expected cannot be null");
        } else if (actual == null) {
            throw new IllegalStateException("Actual cannot be null");
        }
        String content = "\n" + getContent() + "\n";
        assertEquals("Unexpected array size", expected.size(),
            actual.size());
        assertEquals("Unexpected array content" + content, expected,
            actual);
    }

    /**
     * Checks that the failure table is valid
     */
    private void assertFailureTableEquals() {
        if (expectedFailureTable == null) {
            throw new IllegalStateException("Expected failure table cannot be "
                + "null");
        } else if (actualFailureTable == null) {
            throw new IllegalStateException("Actual failure table cannot be "
                + "null");
        }
        String content = "\n" + getFailureTableContent() + "\n";
        assertEquals("Unexpected table length", expectedFailureTable.length,
            actualFailureTable.length);
        assertArrayEquals("Unexpected table content" + content,
            expectedFailureTable, actualFailureTable);
    }

    /**
     * Checks that the last table is valid
     */
    private void assertLastTableEquals() {
        if (expectedLastTable == null) {
            throw new IllegalStateException("Expected last table cannot be "
                + "null");
        } else if (actualLastTable == null) {
            throw new IllegalStateException("Actual last table cannot be "
                + "null");
        }
        String content = "\n" + getLastTableContent() + "\n";
        assertEquals("Unexpected table size", expectedLastTable.size(),
            actualLastTable.size());
        assertEquals("Unexpected table content" + content,
            expectedLastTable, actualLastTable);
    }

    // RUNNER METHODS

    /**
     * Runner for the searching tests to reduce code redundancy.
     *
     * @param search          name of search to perform
     * @param pattern         pattern to search for
     * @param text            text to search in
     * @param expectedAnswers expected indices of pattern in text
     * @param expectedCount   expected number of usage of the comparator
     */
    void searchRunner(PatternMatchingTestUtil.Algorithm search, String pattern,
                      String text, Integer[] expectedAnswers,
                      Integer... expectedCount) {
        comparator = new CharacterComparator();
        search(search, pattern, text);
        expected = Arrays.asList(expectedAnswers);

        assertActualEquals();
        assertComparisons(expectedCount);
    }

    /**
     * Runner for the failure table tests to reduce code redundancy.
     *
     * @param pattern       pattern to generate failure table from
     * @param expectedTable expected failure table
     * @param expectedCount expected number of usage of the comparator
     */
    void failureTableRunner(String pattern, int[] expectedTable,
                            int expectedCount) {
        comparator = new CharacterComparator();
        actualFailureTable = PatternMatching.buildFailureTable(pattern,
            comparator);
        expectedFailureTable = expectedTable;

        assertFailureTableEquals();
        assertComparisons(expectedCount);
    }

    /**
     * Runner for the last table tests to reduce code redundancy.
     *
     * @param pattern        pattern to generate last table from
     * @param expectedKeys   expected last table keys
     * @param expectedValues expected last table values
     */
    void lastTableRunner(String pattern, char[] expectedKeys,
                         int[] expectedValues) {
        if (expectedKeys.length != expectedValues.length) {
            throw new IllegalArgumentException("Number of expected keys "
                + "should equal the number of expected values");
        }
        comparator = new CharacterComparator();
        actualLastTable = PatternMatching.buildLastTable(pattern);
        expectedLastTable = new HashMap<>();
        for (int i = 0; i < expectedKeys.length; i++) {
            expectedLastTable.put(expectedKeys[i], expectedValues[i]);
        }

        assertLastTableEquals();
    }

    //*************************************************************************
    //************************   AUXILIARY CLASSES    *************************
    //*************************************************************************

    enum Algorithm {
        KMP {
            @Override
            public String toString() {
                return "kmp";
            }
        },
        BOYERMOORE {
            @Override
            public String toString() {
                return "boyerMoore";
            }
        },
        RABINKARP {
            @Override
            public String toString() {
                return "rabinKarp";
            }
        },
    }
}