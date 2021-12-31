import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * A class containing both the original Boyer Moore algorithm (employing the good suffix and bad character heuristics),
 * as well as the BM algorithm with the Galil Rule implemented as well.
 *
 * @author Quill Healey
 * @version 2.0
 *
 * Collaborators:
 * GeeksForGeeks article: https://www.geeksforgeeks.org/boyer-moore-algorithm-for-pattern-searching/
 * GeeksForGeeks article: https://www.geeksforgeeks.org/boyer-moore-algorithm-good-suffix-heuristic/
 * GaTech CS 1332 TAs (Wrote the CharacterComparator Class)
 */
public class BoyerMoore {

    /**
     * A Boyer Moore algorithm implementation that relies on the bad character rule and the good suffix heuristic.
     *
     * @param pattern    the pattern a user is searching for in a body of text (the needle).
     * @param text    the body of text where a user searches for a pattern (the haystack).
     * @param comparator    an external class used by the algorithm to check if two characters are equal.
     * @return     a list containing the starting index for each match found.
     * @throws java.lang.IllegalArgumentException   if the pattern, text, or comparator is null.
     * @throws java.lang.IllegalArgumentException   if the pattern has length 0;
     */
    public static List<Integer> boyerMoore(CharSequence pattern,
                                           CharSequence text,
                                           CharacterComparator comparator) {
        if (pattern == null) {
            throw new IllegalArgumentException("Your pattern cannot be a null value. Please pass in a valid pattern"
                    + " parameter argument.");
        }
        if (pattern.length() == 0) {
            throw new IllegalArgumentException("Your pattern cannot be of length 0. Please call this method with"
                    + " a non-zero length pattern");
        }
        if (text == null) {
            throw new IllegalArgumentException("The text parameter cannot be null. Please try again with a "
                    + " non-null text parameter.");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Your comparator cannot be null. Please try again with a "
                    + " non-null comparator parameter.");
        }

        // matches will store the starting indices of each match found
        ArrayList<Integer> matches = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return matches;
        }

        int n = text.length();
        int m = pattern.length();

        // lot is the last occurrence table built for the passed in pattern as specified by the bad character heuristic.
        HashMap<Character, Integer> lot = (HashMap<Character, Integer>) buildLastTable(pattern);

        /*
        the integer array f is a border position array. Each entry in f contains the starting index of the border for
        the suffix starting at index i for the passed in pattern.
        each entry in the shift array contains the distance the pattern will shift by if there is a mismatch at index
        i - 1.
        Note that both array are set to the length of the pattern + 1 so that if there is a mismatch at index m our
        call shift[m + 1] will not throw an IndexOutOfBoundsException.
        See the preprocessing methods below for more details.
         */
        int[] f = new int[m + 1];
        int[] shift = new int[m + 1];

        for (int i = 0; i < m + 1; i++) {
            shift[i] = 0;
        }

        preprocessStrongSuffix(shift, f, pattern, comparator);
        preprocessCase2(shift, f, pattern);

        // s is the shift of the pattern
        int s = 0;
        // j will keep track of which element in the pattern we are currently checking
        int j;

        while (s <= n - m) {
            // once again recall that BM checks from right to left.
            j = m - 1;

            // so long as the characters of the pattern and text are matching keeping moving left in the pattern.
            while (j >= 0 && comparator.compare(pattern.charAt(j), text.charAt(s + j)) == 0) {
                j--;
            }

            // If j < 0 then we have found a match
            if (j < 0) {
                matches.add(s);
                s++;
            } else {
                int lotShift = lot.getOrDefault(text.charAt(s + j), -1);
                // We will shift the text according to the maximum of the good suffix and bad character heuristics.
                s += Math.max(shift[j + 1], j - lotShift);
            }
        }

        return matches;
    }

    /**
     * A Boyer Moore algorithm implementation that relies on the bad character rule, good suffix heuristic,
     * and the Galil Rule.
     *
     * Note that this code will be nearly identical to the algorithm implemented immediately above. The difference
     * can be found when there is a match between the pattern and text.
     *
     * @param pattern   the pattern a user is searching for in a body of text (the needle).
     * @param text  the body of text where a user searches for a pattern (the haystack).
     * @param comparator    an external class used by the algorithm to check if two characters are equal.
     * @return  a list containing the starting index for each match found.
     * @throws java.lang.IllegalArgumentException   if the pattern, text, or comparator is null.
     * @throws java.lang.IllegalArgumentException   if the pattern has length 0;
     */
    public static List<Integer> boyerMooreGalil(CharSequence pattern,
                                           CharSequence text,
                                           CharacterComparator comparator) {
        if (pattern == null) {
            throw new IllegalArgumentException("Your pattern cannot be a null value. Please pass in a valid pattern"
                    + " parameter argument.");
        }
        if (pattern.length() == 0) {
            throw new IllegalArgumentException("Your pattern cannot be of length 0. Please call this method with"
                    + " a non-zero length pattern");
        }
        if (text == null) {
            throw new IllegalArgumentException("The text parameter cannot be null. Please try again with a "
                    + " non-null text parameter.");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Your comparator cannot be null. Please try again with a "
                    + " non-null comparator parameter.");
        }
        // matches will store the starting indices of each match found
        ArrayList<Integer> matches = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return matches;
        }

        int n = text.length();
        int m = pattern.length();

        // lot is the last occurrence table built for the passed in pattern as specified by the bad character heuristic.
        HashMap<Character, Integer> lot = (HashMap<Character, Integer>) buildLastTable(pattern);

        /*
        the integer array f is a border position array. Each entry in f contains the starting index of the border for
        the suffix starting at index i for the passed in pattern.
        each entry in the shift array contains the distance the pattern will shift by if there is a mismatch at index
        i - 1.
        Note that both array are set to the length of the pattern + 1 so that if there is a mismatch at index m our
        call shift[m + 1] will not throw an IndexOutOfBoundsException.
        * See the preprocessing methods below for more details.
         */
        int[] f = new int[m + 1];
        int[] shift = new int[m + 1];

        for (int i = 0; i < m + 1; i++) {
            shift[i] = 0;
        }

        preprocessStrongSuffix(shift, f, pattern, comparator);
        preprocessCase2(shift, f, pattern);

        // k is the periodicity of the pattern. A check will be implemented below to ensure that k is valid.
        int k = m - buildFailureTable(pattern, comparator)[m - 1];

        // s is the shift of the pattern
        int s = 0;
        // j will keep track of which element in the pattern we are currently checking
        int j;
        /*
        Instead of checking each character of the pattern (0 <- m), we will instead check (l <- m). While we do
        initialize l to zero here, its value could change later according to the Galil Rule.
         */
        int l = 0;

        while (s <= n - m) {
            // once again recall that BM checks from right to left.
            j = m - 1;

            // so long as the characters of the pattern and text are matching keeping moving left in the pattern.
            while (j >= l && comparator.compare(pattern.charAt(j), text.charAt(s + j)) == 0) {
                j--;
            }

            /*
            If j < l then we have found a match. If k > 1 and m % (m - ftable[m-1]) == 0 then we
            can exploit the periodicity of the pattern by shifting k units forward and only checking the
            last k elements to determine if there is another occurrence.
             */
            if (j < l && k > 1 && m % k == 0) {
                matches.add(s);
                l = m - k;
                s += k;
            } else if (j < 0) {
                // if the pattern does not have a period we revert to the usual BM shifting scheme.
                matches.add(s);
                s += shift[0];
            } else {
                if (l != 0) {
                    /* if we run into a mismatch after previously exploiting the Galil rule, we must return to checking
                    all the text and pattern comparisons.
                     */
                    l = 0;
                }
                int lotShift = lot.getOrDefault(text.charAt(s + j), -1);
                // We will shift the text according to the maximum of the good suffix and bad character heuristics.
                s += Math.max(shift[j + 1], j - lotShift);
            }
        }
        return matches;
    }

    /**
     * Builds a last occurrence table, as specified by the bad character rule, that will be used to run both the BM
     * and BM-Galil algorithms. Note that the dictionary's size will be equivalent to the size of the pattern's
     * alphabet.
     *
     * If the pattern is empty an empty map will be returned.
     *
     * @param pattern    a pattern that the last occurrence table is built for.
     * @return  a Map with keys of all the characters in the pattern mapping to their occurrence in the pattern.
     * @throws java.lang.IllegalArgumentException if the pattern is null
     */
    public static Map<Character, Integer> buildLastTable(CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Your pattern cannot be a null value. Please pass in a valid pattern"
                    + " parameter argument");
        }
        int m = pattern.length();
        HashMap<Character, Integer> lot = new HashMap<>(); // The last occurrence table that will be returned
        int i = 0;
        while (i < m) {
            lot.put(pattern.charAt(i), i);
            i++;
        }
        return lot;
    }

    /**
     * For each suffix t, beginning with the arbitrary index j and length 1 <= k < m within the pattern,
     * this helper method looks for the nearest, leftward matching substring, beginning at arbitrary index i with
     * length k where p[i - 1] != p[j - 1]. If said substring exists, then the shift array at index j will be set
     * to j - i.
     *
     * These computations are reliant on a border array f. A border is a substring which is both a proper suffix and
     * a proper prefix. See the ReadMe file and linked explanation videos for a more in depth description of this
     * process.
     *
     * @param shift    an integer array whose entries contain the distance the pattern needs to shift if a mismatch
     *                 occurs at position i - 1. Note that the shift array is initialized with size m + 1.
     * @param f    an integer array whose entries contain the starting index of the border for the suffix starting
     *             at index i in the given pattern. This border array will also be useful when preprocessing case 2.
     *             Note that the border array is initialized with size m + 1.
     * @param pattern    a pattern that we are preprocessing a shift table for.
     * @param comparator    an instance of CharacterComparator that is needed to compare characters in the pattern.
     * @throws java.lang.IllegalArgumentException    if the pattern or comparator is null.
     */
    public static void preprocessStrongSuffix(int[] shift, int[] f, CharSequence pattern,
                                                CharacterComparator comparator) {
        if (pattern == null) {
            throw new IllegalArgumentException("Your pattern cannot be a null value. Please pass in a valid pattern"
                    + " parameter argument.");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Your comparator cannot be null. Please try again with a "
                    + " non-null comparator parameter.");
        }
        int m = pattern.length();
        // Much like how the Boyer-Moore search algorithm moves right to left, so will our preprocessing
        int i = m;
        int j = m + 1;
        // The last element in the border array is set to value m + 1 since it cannot have a border.
        f[i] = j;

        while (i > 0) {
            /*
            This while loop handles two key pieces of logic.
            1. If the character at position i - 1 is not equivalent to the character at position j - 1,
            then continue searching to the right for a border.
            2. Recall that when pat[i - 1] != pat[j - 1] we shift the pattern from i to j. After an additional check
            (explained below) the code within this while loop computes this logic.
             */
            while (j <= m && comparator.compare(pattern.charAt(i - 1), pattern.charAt(j - 1)) != 0) {
                // Ensuring that the shift computed for element i is the nearest mismatch
                if (shift[j] == 0) {
                    /*
                    When there is a mismatch between the pattern and text at element i, the pattern will be shifted
                    j - i elements to the right.
                     */
                    shift[j] = j - i;
                }
                /*
                Here lies an advantage of moving right to left in our pattern. At this point we know that p[i-1] and
                p[j-1] are unequal. Therefore, either there exists a border starting with element p[i-1] in the
                pattern and it is further to the right in the pattern than element j, or there is not a border in the
                pattern starting with element p[i-1]. Fortunately, we can use our knowledge of the border at index j
                instead of performing this search manually. f[j] will either kick the j pointer outside of length m,
                thus implying that the border starting with p[i-1] does not exist, or it will find another location
                within the pattern where the second element of the border follows p[i] = p[j] (and perhaps onwards).
                 */
                j = f[j];
            }
            /*
            p[i-1] matched with p[j - 1], meaning that we found a border. Now store the starting position of
            this border.
             */
            i--;
            j--;
            f[i] = j;
        }
    }

    /**
     * The preprocessCase2 helper method is run on the shift and f (border) arrays after we call the
     * preprocessStrongSuffix method. This algorithm uses the previously computed borders to account for the case when
     * there is a prefix of P that matches with a suffix of the suffix t (above). In other words, there is
     * no guarantee that for the suffix t there will be a match for t earlier in the pattern. However, there is still
     * a chance that some prefix of P will match with some suffix of t.
     *
     * @param shift    The same shift array used in the strong good suffix preprocessing.
     * @param f    The same border array used in the strong good suffix preprocessing.
     * @param pattern    a pattern that we are preprocessing a shift table for (same as above).
     */
    public static void preprocessCase2(int[] shift, int[] f, CharSequence pattern) {
        int m = pattern.length();
        // f[0] is the widest-border from the starting position
        int j = f[0];

        for (int i = 0; i <= m; i++) {
            /*
            For all free entries in the shift array we replace them with the distance of the widest border.
             */
            if (shift[i] == 0) {
                shift[i] = j;
            }
            /*
            When the suffix of the pattern becomes shorter than f[0] the algorithm uses the next widest border in the
            shift array.
             */
            if (i == j) {
                j = f[j];
            }
        }
    }

    /**
     * Builds failure table that will be used to calculate the periodicity of the pattern
     *
     * The table built should be the length of the input pattern.
     *
     * Note that a given index i will contain the length of the largest prefix
     * of the pattern indices [0..i] that is also a proper suffix of the pattern
     * indices [1..i]. This means that index 0 of the returned table will always
     * be equal to 0
     *
     * Ex. pattern = ababac
     *
     * table[0] = 0
     * table[1] = 0
     * table[2] = 1
     * table[3] = 2
     * table[4] = 3
     * table[5] = 0
     *
     * If the pattern is empty, return an empty array.
     *
     * @param pattern    a pattern you're building a failure table for
     * @param comparator used to check if two characters are equal
     * @return integer array holding your failure table
     * @throws java.lang.IllegalArgumentException if the pattern or comparator
     *                                            is null
     */
    public static int[] buildFailureTable(CharSequence pattern,
                                          CharacterComparator comparator) {
        if (pattern == null) {
            throw new IllegalArgumentException("Your pattern cannot be a null value. Please pass in a valid pattern"
                    + " parameter argument.");
        }
        if (pattern.length() == 0) {
            return new int[0];
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Your comparator cannot be a null value. Please pass in a valid"
                    + " comparator");
        }

        int m = pattern.length();

        int[] ftable = new int[m];
        ftable[0] = 0;

        int i = 0;
        int j = 1;

        while (j < pattern.length()) {
            int comparison = comparator.compare(pattern.charAt(i), pattern.charAt(j));
            // If the two characters are equal
            if (comparison == 0) {
                ftable[j] = i + 1;
                i++;
                j++;
            } else if (i == 0) {
                ftable[j] = 0;
                j++;
            } else {
                i = ftable[i - 1];
            }
        }
        return ftable;
    }

    /**
     * A private helper I created for debugging.
     *
     * @param arr An array whose elements you want to print out.
     */
    private static void printArr(ArrayList<Integer> arr) {
        for (int a : arr) {
            System.out.print(a + ", ");
        }
        System.out.println();
    }
}
