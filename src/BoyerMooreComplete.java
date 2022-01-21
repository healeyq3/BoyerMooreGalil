import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoyerMooreComplete {
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
        HashMap<Character, Integer> lot = (HashMap<Character, Integer>) LastOccurrenceTable.buildLastTable(pattern);

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

        GoodSuffixPreprocessing.preprocessStrongSuffix(shift, f, pattern, comparator);
        GoodSuffixPreprocessing.preprocessCase2(shift, f, pattern);

        // k is the "periodicity" of the pattern. A check will be implemented below to ensure that k is valid
        // (Only for p = u^k case).
        int k = m - FailureTable.buildFailureTable(pattern, comparator)[m - 1];

        // s is the shift of the pattern
        int s = 0;
        // j will keep track of which element in the pattern we are currently checking
        int j;
        /*
        Instead of checking each character of the pattern (0 <- m), we will instead check (l <- m). While we do
        initialize l to zero here, its value could change later according to the Galil Rule.
         */
        int l = 0;
        // Refactor code to use boolean isGalil? Remove some logic from loop?
        while (s <= n - m) {
            // once again recall that BM checks from right to left.
            j = m - 1;

            // so long as the characters of the pattern and text are matching keeping moving left in the pattern.
            while (j >= l && comparator.compare(pattern.charAt(j), text.charAt(s + j)) == 0) {
                j--;
            }

            /*
            If j < l then we have found a match. If k > 1 and m % (m - ftable[m-1]) == 0 (REMOVED THIS)
            then we can exploit the periodicity of the pattern by shifting k units forward
            and only checking the last k elements to determine if there is another occurrence.
             */
            if (j < l && k > 1) {
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
                int lotShift = j - lot.getOrDefault(text.charAt(s + j), -1);
                // We will shift the text according to the maximum of the good suffix and bad character heuristics.
                s += Math.max(shift[j + 1], lotShift);
            }
        }
        return matches;
    }
}
