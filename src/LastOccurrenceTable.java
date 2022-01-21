import java.util.HashMap;
import java.util.Map;

public class LastOccurrenceTable {
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
}
