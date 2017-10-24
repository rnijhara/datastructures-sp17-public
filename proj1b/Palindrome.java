public class Palindrome {

    /* Converts a given String into a Deque holding its characters */
    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new ArrayDeque<Character>();
        for (int i = 0, n = word.length(); i < n; i++) {
            wordDeque.addLast(word.charAt(i));
        }
        return wordDeque;
    }

    /* Helper method for isPalindrome */
    private static boolean helperPalindrome(Deque<Character> longDeque) {
        if (longDeque.size() <= 1) {
            return true;
        } else {
            return ((longDeque.removeFirst() == longDeque.removeLast())
                    && helperPalindrome(longDeque));
        }
    }

    /* Checks whether or not a given word is palindrome */
    public static boolean isPalindrome(String word) {
        Deque<Character> wordDeque = wordToDeque(word);
        return helperPalindrome(wordDeque);
    }

    /* Generalized helper method for a palindrome test using a CharacterComparator */
    private static boolean helperGeneralized(Deque<Character> longDeque, CharacterComparator cc) {
        if (longDeque.size() <= 1) {
            return true;
        } else {
            char first = longDeque.removeFirst();
            char last = longDeque.removeLast();
            return (cc.equalChars(first, last) && helperGeneralized(longDeque, cc));
        }
    }

    /* Generalized palindrome checker using a CharacterComparator */
    public static boolean isPalindrome(String word, CharacterComparator cc) {
        word = word.toLowerCase();
        Deque<Character> wordDeque = wordToDeque(word);
        return helperGeneralized(wordDeque, cc);
    }
}
