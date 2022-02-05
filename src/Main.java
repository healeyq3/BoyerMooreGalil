public class Main {
    public static void main(String[] args) {
        CharacterComparator comparator = new CharacterComparator();
        CharSequence pattern = "abcd";
        CharSequence text = "babcdababcdb";
        System.out.println(BoyerMooreBCGalil.boyerMooreBCGalil(pattern, text, comparator).toString());
        System.out.println(String.format("total comparisions: %d", comparator.getComparisonCount()));
    }
}