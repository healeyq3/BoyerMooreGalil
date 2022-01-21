public class GoodSuffixPreprocessing {
    /**
     * For each suffix t, beginning with the arbitrary index j and length 1 <= k < m within the pattern,
     * this helper method looks for the nearest, leftward matching substring, beginning at arbitrary index i with
     * length k where p[i - 1] != p[j - 1]. If said substring exists, then the shift array at index j will be set
     * to j - i.
     *
     * These computations are reliant on a border array f. A border is a substring which is both a proper suffix and
     * a proper prefix. See the ReadMe file for a more in depth description of this
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
     * @throws IllegalArgumentException    if the pattern is null valued
     */
    public static void preprocessCase2(int[] shift, int[] f, CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Your pattern cannot be a null value. Please pass in a valid pattern"
                    + " parameter argument.");
        }
        if (shift == null) {
            throw new IllegalArgumentException("Your shift array cannot be a null value. Please pass in a valid"
                    + " shift array parameter argument.");
        }
        if (f == null) {
            throw new IllegalArgumentException("Your border array cannot be a null value. Please pass in a valid"
                    + " border array parameter argument.");
        }
        int m = pattern.length();
        if (shift.length != m + 1) {
            throw new IllegalArgumentException("Your shift array has to be equal to the length of the pattern + 1."
                    + " Please pass in an appropriately sized shift array.");
        }
        if (f.length != m + 1) {
            throw new IllegalArgumentException("Your border array has to be equal to the length of the pattern + 1."
                    + " Please pass in an appropriately sized border array.");
        }
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
}
