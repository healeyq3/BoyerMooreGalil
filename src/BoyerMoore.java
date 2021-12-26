import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * A class containing both the original Boyer Moore algorithm (employing the good suffix and bad character heuristics),
 * as well as the BM algorithm with the Galil Rule implemented as well.
 *
 * @author Quill Healey
 * @version 1.0
 *
 * Collaborators:
 * GeeksForGeeks article: https://www.geeksforgeeks.org/boyer-moore-algorithm-for-pattern-searching/
 * GeeksForGeeks article: https://www.geeksforgeeks.org/boyer-moore-algorithm-good-suffix-heuristic/
 */
public class BoyerMoore {

    /**
     * A Boyer Moore algorithm implementation that relies on the bad character rule and the good suffix heuristic.
     *
     * @param pattern   the pattern a user is searching for in a body of text (the needle).
     * @param text  the body of text where a user searches for a pattern (the haystack).
     * @param comparator    an external class used by the algorithm to check if two characters are equal.
     * @return  a list containing the starting index for each match found.
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

        ArrayList<Integer> matches = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return matches;
        }

        int n = text.length();
        int m = pattern.length();

        HashMap<Character, Integer> lot = (HashMap<Character, Integer>) buildLastTable(pattern);

        int[] f = new int[m + 1];
        int[] shift = new int[m + 1];

        for (int i = 0; i < m + 1; i++) {
            shift[i] = 0;
        }

        preprocess_strong_suffix(shift, f, pattern, m, comparator);
        preprocess_case2(shift, f, pattern, m);

        int s = 0, j;

        while (s <= n - m) {
            j = m - 1;

            while (j >= 0 && comparator.compare(pattern.charAt(j), text.charAt(s + j)) == 0) {
                j--;
            }

            if (j < 0) {
                matches.add(s);
                s++;
            } else {
                int lotShift = lot.getOrDefault(text.charAt(s + j), -1);
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
        ArrayList<Integer> matches = new ArrayList<>();
        if (pattern.length() > text.length()) {
            return matches;
        }
        return matches;
    }

    /**
     * Builds a last occurrence table, as specified by the bad character rule, that will be used to run both the BM
     * and BM-Galil algorithms.
     *
     * If the pattern is empty an empty mapped will be returned.
     *
     * @param pattern   a pattern that the last occurrence table is built for.
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


    public static void preprocess_strong_suffix(int[] shift, int[] f, CharSequence pattern, int m,
                                                CharacterComparator comparator) {
        int i = m, j = m + 1;
        f[i] = j;

        while (i > 0) {
            while (j <= m && comparator.compare(pattern.charAt(i - 1), pattern.charAt(j - 1)) != 0) {
                if (shift[j] == 0) {
                    shift[j] = j - i;
                }

                j = f[j];
            }
            i--; j--;
            f[i] = j;
        }
    }

    public static void preprocess_case2(int[] shift, int[] f, CharSequence pattern, int m) {
        int i, j;
        j = f[0];
        for (i = 0; i <= m; i++) {
            if (shift[i] == 0) {
                shift[i] = j;
            }
            if (i == j) {
                j = f[j];
            }
        }
    }

}