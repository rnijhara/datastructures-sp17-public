/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/

    // Credit to Algs4 Princeton LSD Radix Sort
    public static String[] sort(String[] asciis) {
        final int R = 256;
        String[] copy = new String[asciis.length];
        String[] sorted = new String[asciis.length];
        System.arraycopy(asciis, 0, sorted, 0, asciis.length);

        int maxLen = 0;
        for (String a : asciis) {
            if (a.length() > maxLen) {
                maxLen = a.length();
            }
        }

        for (int i = 0; i < sorted.length; i++) {
            int[] counts = new int[R + 1];

            for (int j = 0; j < sorted.length; j++) {
                String str = sorted[j];
                int strLen = str.length();
                if (strLen - 1 - j < 0) {
                    counts[1] += 1;
                } else {
                    counts[(int) str.charAt(strLen - 1 - j) + 1] += 1;
                }
            }

            for (int r = 0; r < R; r++) {
                counts[r + 1] += counts[r];
            }

            for (int j = 0; j < sorted.length; j++) {
                String str = sorted[j];
                int strLen = str.length();
                if (strLen - 1 - j < 0) {
                    copy[counts[1]++] = sorted[j];
                }
            }

            System.arraycopy(copy, 0, sorted, 0, sorted.length);
        }
        return sorted;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     *  destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index)
    {
        //TODO use if you want to
    }
    public static void main(String[] args) {
    }
}
