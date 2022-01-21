import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        ArrayList<Integer> matches = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return matches;
        }

        int n = text.length();
        int m = pattern.length();

        // lot is the last occurrence table built for the passed in pattern as specified by the bad character heuristic.
        HashMap<Character, Integer> lot = (HashMap<Character, Integer>) LastOccurrenceTable.buildLastTable(pattern);

        /*
        s is the shift of the pattern with respect to the text. Or rather, it is the element in the text that we
        are comparing pattern.charAt(0) with.
         */
        int s = 0;
        // j will keep track of which element in the pattern we are currently checking
        int j;

        /*
        Instead of checking each character of the pattern (0 <- m), we will instead check (l <- m). While we do
        initialize l to zero, its value could change later according to the Galil rule.
         */
        int l = 0;

        // k is the "periodicity" of the pattern
        int k = m - FailureTable.buildFailureTable(pattern, comparator)[m - 1];

        /*
        We will use this "galil" flag to indicate whether we can use Zvi Galil's optimization technique upon
        finding a match.
         */
        boolean galil = false;
        if (k >= 1) {
            galil = true;
        }

        while (s <= n - m) {
            // once again recall that BM checks from right to left
            j = m - 1;

            // so long as the characters of the pattern and text are matching keep moving left in the pattern
            while (j >= l && comparator.compare(pattern.charAt(j), text.charAt(s + j)) == 0) {
                j--;
            }

            // If j < l then we have found a match
            if (j < l) {
                matches.add(s);
                /*
                If we have determined that our pattern is periodic (or potentially periodic) then we can exploit its
                periodicity by shifting k elements forward and then only checking the last k to determine if there is
                another occurrence. This shifting scheme is the one detailed in Galil's white paper.
                 */
                if (galil) {
                    l = m - k; // Recall we check the elements in the pattern from p[m-1] -> p[l]
                    s += k;
                } else {
                    s += 1;
                }
            } else {
                // Note to self: potentially fewer operations to just always set l = 0?
                if (l != 0) {
                    /*
                    If we have been exploiting Galil's rule, and we suddenly have a mismatch, it no longer suffices to
                    only check the last k elements. To find another match we now must check them all.
                     */
                    l = 0;
                }
                // Typical mismatch shifting scheme as detailed by the bad character heuristic.
                int shift = lot.getOrDefault(text.charAt(s + j), -1);
                if (shift < j) {
                    s = s + j - shift;
                } else {
                    s += 1;
                }
            }
        }
        return matches;
    }
}
