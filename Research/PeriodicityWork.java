class PeriodicityWork {

	public static int[] buildFailureTable(CharSequence pattern, CharacterComparator comparator) {
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
            if (comparison == 0) {
                ftable[j] = i + 1;
                i++;
                j++;
            } else if (comparison != 0 && i == 0) {
                ftable[j] = 0;
                j++;
            } else {
                i = ftable[i - 1];
            }
        }
        return ftable;
	}

	public static void main(String[] args) {
		CharacterComparator comparator = new CharacterComparator();
		
		CharSequence pattern1 = "ababa";
		int m1 = pattern1.length();
		int p1 = m1 - buildFailureTable(pattern1, comparator)[m1 - 1];
		System.out.println("P1k = " +  p1);

		CharSequence pattern2 = "ababab";
		int m2 = pattern2.length();
		int p2 = m2 - buildFailureTable(pattern2, comparator)[m2 - 1];
		System.out.println("P2k = " +  p2);

		CharSequence pattern3 = "abcabca";
		int m3 = pattern3.length();
		int p3 = m3 - buildFailureTable(pattern3, comparator)[m3 - 1];
		System.out.println("P3k = " +  p3);

		CharSequence pattern3 = "abcabca";
		int m3 = pattern3.length();
		int p3 = m3 - buildFailureTable(pattern3, comparator)[m3 - 1];
		System.out.println("P3k = " +  p3);

		CharSequence pattern3 = "abcabca";
		int m3 = pattern3.length();
		int p3 = m3 - buildFailureTable(pattern3, comparator)[m3 - 1];
		System.out.println("P3k = " +  p3);

	}
}