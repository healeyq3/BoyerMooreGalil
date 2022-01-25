import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the Boyer Moore pattern matching algorithm which uses the Bad Character Heuristic
 * with Zvi Galil's optimization technique.
 *
 * This is the main class in the BoyerMooreGalil repository.
 *
 * @author Quill Healey
 * @version 1.0
 *
 * Collaborators:
 * GaTech CS 1332 TAs (wrote the CharacterComparator class)
 *
 */
public class BoyerMooreBCGalil {

    public static List<Integer> searchAlgorithm(CharSequence pattern, CharSequence text,
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
        List<Integer> matches = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            return matches;
        }

        // lot is the last occurrence table built for the passed in pattern as specified by the bad character heuristic.
        Map<Character, Integer> lot = LastOccurrenceTable.buildLastTable(pattern);

        /*
        i is the shift of the pattern with respect to the text. Or rather, it is the element in the text that we
        are comparing pattern.charAt(0) with.
         */
        int i = 0;

        /*
        Instead of checking each character of the pattern (0 <- m), we will instead check (l <- m). While we do
        initialize l to zero, its value could change later according to the Galil rule.
         */
        int l = 0;

        // k is the "periodicity" of the pattern
        int period = m - FailureTable.buildFailureTable(pattern, comparator)[m - 1];

        /*
        We will use this "galil" flag to indicate whether we can use Zvi Galil's optimization technique upon
        finding a match. Recall that we can use the galil rule if the period of the pattern is at least 1.
         */
        boolean useGalilRule = period >= 1;

        while (i <= n - m) {
            // once again recall that BM checks from right to left
            // j will keep track of which element in the pattern we are currently checking
            int j = m - 1;

            // so long as the characters of the pattern and text are matching keep moving left in the pattern
            while (j >= l && comparator.compare(pattern.charAt(j), text.charAt(i + j)) == 0) {
                j--;
            }

            // If j < l then we have found a match
            if (j < l) {
                matches.add(i);
                /*
                If we have determined that our pattern is periodic (or potentially periodic) then we can exploit its
                periodicity by shifting k elements forward and then only checking the last k to determine if there is
                another occurrence. This shifting scheme is the one detailed in Galil's white paper.
                 */
                if (useGalilRule) {
                    l = m - period; // Recall we check the elements in the pattern from p[m - 1] -> p[l]
                    i += period;
                } else {
                    i++;
                }
            } else {
                /*
                If we have been exploiting Galil's rule, and we suddenly have a mismatch, it no longer suffices to
                only check the last k elements. To find another match we now must check them all.
                    */
                l = 0;

                // Typical mismatch shifting scheme as detailed by the bad character heuristic.
                int shift = lot.getOrDefault(text.charAt(i + j), -1);
                if (shift < j) {
                    i = i + j - shift;
                } else {
                    i++;
                }
            }
        }
        return matches;
    }
}
