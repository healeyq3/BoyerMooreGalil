public class FailureTable {

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
}
