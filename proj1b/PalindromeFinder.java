/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {
    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("words.txt");
        int maxLength = 0;
        String maxWord = "NONE-FOUND";
        int currLength;

        while (!in.isEmpty()) {
            OffByN obn = new OffByN(7);
            String word = in.readString();
            currLength = word.length();
            if (currLength >= minLength && Palindrome.isPalindrome(word, obn)) {
                if (currLength >= maxLength) {
                    maxLength = currLength;
                    maxWord = word;
                }
//                System.out.println(word);
            }
        }
        System.out.println(maxWord);
    }
} 
