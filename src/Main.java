public class Main {
    public static void main(String[] args) {
        // CharacterComparator comparator = new CharacterComparator();
        // CharSequence pattern = "abcdeabcd";
        // CharSequence text = "abcdeabcdabcdeabcdabcdeabcd";
        // System.out.println(BoyerMooreBCGalil.boyerMooreBCGalil(pattern, text, comparator).toString());
        // System.out.println(String.format("total comparisions: %d", comparator.getComparisonCount()));

        CharacterComparator comparator = new CharacterComparator();
        CharSequence pattern = "abaaaa";
        CharSequence text = "abaaaabaaaaabaaaabaaaa";
        System.out.println(BoyerMooreBCGalil.boyerMooreBCGalil(pattern, text, comparator).toString());
        System.out.println(String.format("total comparisions: %d", comparator.getComparisonCount()));
    }
}