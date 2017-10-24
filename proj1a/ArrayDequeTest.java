public class ArrayDequeTest {

    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out size checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method to print out position of a next item to be added */
    public static boolean checkgetIndex(int expected, int actual) {
        if (expected != actual) {
            System.out.println("getFirst/LastIndex returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out indexing checks for an ArrayDeque holding integers. */
    public static boolean checkIntItem(int expected, int actual) {
        if (expected != actual) {
            System.out.println("Int item returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /** Adds to list, tests isEmpty() and size() and prints ArrayDeque */
    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");

        ArrayDeque<String> adq = new ArrayDeque<String>();

        boolean passed = checkEmpty(true, adq.isEmpty());

        adq.addFirst("front");

        passed = checkSize(1, adq.size()) && passed;
        passed = checkEmpty(false, adq.isEmpty()) && passed;

        adq.addLast("middle");
        passed = checkSize(2, adq.size()) && passed;

        adq.addLast("back");
        passed = checkSize(3, adq.size()) && passed;

        adq.addFirst("fronter");
        passed = checkSize(4, adq.size()) && passed;
        passed = checkgetIndex(2, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(5, adq.getLastIndex()) && passed;

        System.out.println("Printing out deque: ");
        adq.printDeque();

        printTestStatus(passed);
    }

    /**Adds to back and tests to cycling */
    public static void testAddBackCycling() {
        System.out.println("Running back cycling test.");

        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        for (int i = 1; i <= 5; i++) {
            adq.addLast(i);
        }
        System.out.println("Printing out deque: ");
        adq.printDeque();

        boolean passed = checkgetIndex(0, adq.getLastIndex());
        passed = checkgetIndex(4, adq.getFirstIndex()) && passed;
        System.out.println("Get(0): " + adq.get(0) + ", Get(4): " + adq.get(4));

        System.out.println("Capacity: " + adq.capacity());
        printTestStatus(passed);
    }

    public static void testAddFrontCycling() {
        System.out.println("Running front cycling test");

        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        for (int i = 6; i > 0; i--) {
            adq.addFirst(i);
        }
        System.out.println("Printing out deque: ");
        adq.printDeque();

        boolean passed = checkgetIndex(3, adq.getLastIndex());
        passed = checkgetIndex(6, adq.getFirstIndex()) && passed;
        System.out.println("Get(0): " + adq.get(0) + ", Get(5): " + adq.get(5));

        System.out.println("Capacity: " + adq.capacity());
        printTestStatus(passed);
    }

    public static void addFirstRemoveLastTest() {
        System.out.println("Running add remove tests.");
        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        boolean passed = checkEmpty(true, adq.isEmpty());
        for (int i = 5; i > 0; i --) {
            adq.addFirst(i);
        }
        System.out.print("Deque after adding items: ");
        adq.printDeque();
        passed = checkSize(5, adq.size()) && passed;
        passed = checkgetIndex(7, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(3, adq.getLastIndex()) && passed;

        passed = checkIntItem(5, adq.removeLast()) && passed;
        passed = checkgetIndex(7, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(2, adq.getLastIndex()) && passed;
        passed = checkSize(4, adq.size()) && passed;

        passed = checkIntItem(4, adq.removeLast()) && passed;
        passed = checkIntItem(3, adq.removeLast()) && passed;
        passed = checkIntItem(2, adq.removeLast()) && passed;
        passed = checkgetIndex(7, adq.getLastIndex()) && passed;
        passed = checkgetIndex(7, adq.getFirstIndex()) && passed;
        passed = checkIntItem(1, adq.get(0)) && passed;
        passed = checkIntItem(1, adq.removeLast()) && passed;

        System.out.println("Removing first from empty: " + adq.removeFirst());
        System.out.println("Removing last from empty: " + adq.removeLast());
        passed = checkSize(0, adq.size()) && passed;
        passed = checkEmpty(true, adq.isEmpty()) && passed;
        printTestStatus(passed);
        System.out.println("Capacity: " + adq.capacity());
    }

    public static void addLastRemoveFirstTest() {
        System.out.println("Running addLast removeFirst tests.");
        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        boolean passed = checkEmpty(true, adq.isEmpty());
        for (int i = 1; i <= 5; i++) {
            adq.addLast(i);
        }
        System.out.print("Deque after adding items: ");
        adq.printDeque();
        passed = checkSize(5, adq.size()) && passed;
        passed = checkgetIndex(4, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(0, adq.getLastIndex()) && passed;

        passed = checkIntItem(1, adq.removeFirst()) && passed;
        passed = checkgetIndex(5, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(0, adq.getLastIndex()) && passed;
        passed = checkSize(4, adq.size()) && passed;

        passed = checkIntItem(2, adq.removeFirst()) && passed;
        passed = checkIntItem(3, adq.removeFirst()) && passed;
        passed = checkIntItem(4, adq.removeFirst()) && passed;
        passed = checkgetIndex(0, adq.getLastIndex()) && passed;
        passed = checkgetIndex(0, adq.getFirstIndex()) && passed;
        passed = checkIntItem(5, adq.get(0)) && passed;
        passed = checkIntItem(5, adq.removeFirst()) && passed;

        System.out.println("Removing first from empty: " + adq.removeFirst());
        System.out.println("Removing last from empty: " + adq.removeLast());
        passed = checkSize(0, adq.size()) && passed;
        passed = checkEmpty(true, adq.isEmpty()) && passed;
        printTestStatus(passed);
        System.out.println("Capacity: " + adq.capacity());
    }

    public static void addResizeTest() {
        System.out.println("Running addFirst addLast Resize tests.");
        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        boolean passed = checkSize(0, adq.size());
        passed = checkEmpty(true, adq.isEmpty()) && passed;

        for (int i = 4; i > 0; i--) {
            adq.addFirst(i);
        }
        for (int i = 5; i < 9; i++ ) {
            adq.addLast(i);
        }
        passed = checkSize(8, adq.size()) && passed;
        passed = checkgetIndex(0, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(7, adq.getLastIndex()) && passed;
        passed = checkIntItem(1, adq.get(0)) && passed;
        passed = checkIntItem(8, adq.get(7)) && passed;
        System.out.print("Deque after adding items: ");
        adq.printDeque();
        System.out.println("Capacity: " + adq.capacity());

        adq.addFirst(0);
        passed = checkgetIndex(15, adq.getFirstIndex()) && passed;
        passed = checkIntItem(0, adq.get(0)) && passed;
        passed = checkSize(9, adq.size()) && passed;
        System.out.print("Deque after adding and resizing: ");
        adq.printDeque();
        System.out.println("Capacity: " + adq.capacity());

        for (int i = 9; i < 16; i++) {
            adq.addLast(i);
        }
        passed = checkgetIndex(15, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(14, adq.getLastIndex()) && passed;
        passed = checkIntItem(0, adq.get(0)) && passed;
        passed = checkIntItem(15, adq.get(15)) && passed;
        System.out.print("Deque after adding more: ");
        adq.printDeque();
        System.out.println("Capacity: " + adq.capacity());

        adq.addLast(16);
        passed = checkSize(17, adq.size()) && passed;
        passed = checkgetIndex(0, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(16, adq.getLastIndex()) && passed;
        passed = checkIntItem(16, adq.get(16)) && passed;

        adq.addFirst(-1);
        passed = checkSize(18, adq.size()) && passed;
        passed = checkgetIndex(31, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(16, adq.getLastIndex()) && passed;
        passed = checkIntItem(-1, adq.get(0)) && passed;

        System.out.print("Deque after adding and resizing again: ");
        adq.printDeque();
        System.out.println("Capacity: " + adq.capacity());
        printTestStatus(passed);

    }

    public static void basicAddRmTest() {
        System.out.println("Running basic add & remove tests.");
        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        boolean passed = checkSize(0, adq.size());
        passed = checkEmpty(true, adq.isEmpty()) && passed;

        adq.addFirst(0);
        adq.addLast(1);
        adq.addLast(2);
        adq.addLast(3);

        passed = checkIntItem(0, adq.get(0)) && passed;
        passed = checkIntItem(3, adq.get(3)) && passed;
        passed = checkgetIndex(3, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(6, adq.getLastIndex()) && passed;

        passed = checkIntItem(0, adq.removeFirst()) && passed;
        passed = checkIntItem(1, adq.removeFirst()) && passed;
        passed = checkIntItem(3, adq.removeLast()) && passed;
        passed = checkIntItem(2, adq.removeLast()) && passed;

        System.out.println("removeFirst (null): " + adq.removeFirst());
        System.out.println("removeLast (null): " + adq.removeLast());
        passed = checkSize(0, adq.size()) && passed;
        passed = checkEmpty(true, adq.isEmpty()) && passed;
        System.out.println("Capacity: " + adq.capacity());
        printTestStatus(passed);
    }

    public static void addRemoveDownsizeTest() {
        System.out.println("Running add, remove, and downsizing tests.");
        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        boolean passed = checkSize(0, adq.size());
        passed = checkEmpty(true, adq.isEmpty()) && passed;

        for (int i = 5; i > 0; i--) {
            adq.addFirst(i);
        }
        adq.addLast(6);
        adq.addLast(7);
        passed = checkSize(7, adq.size()) && passed;
        passed = checkgetIndex(7, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(5, adq.getLastIndex()) && passed;
        adq.addLast(8);
        adq.addLast(9);
        passed = checkSize(9, adq.size()) && passed;
        passed = checkgetIndex(0, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(8, adq.getLastIndex()) && passed;

        System.out.print("Deque after upsizing: ");
        adq.printDeque();
        System.out.println("Capacity: " + adq.capacity());

        for (int i = 1; i <= 5; i++) {
            passed = checkIntItem(i, adq.removeFirst()) && passed;
        }
        passed = checkSize(4, adq.size()) && passed;
        passed = checkgetIndex(5, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(8, adq.getLastIndex()) && passed;

        System.out.print("Deque before downsizing: ");
        adq.printDeque();
        System.out.println("Capacity: " + adq.capacity());

        passed = checkIntItem(6, adq.removeFirst()) && passed;

        System.out.print("Deque after downsizing: ");
        adq.printDeque();
        System.out.println("Capacity: " + adq.capacity());

        passed = checkgetIndex(0, adq.getFirstIndex()) && passed;
        passed = checkgetIndex(2, adq.getLastIndex()) && passed;
        printTestStatus(passed);
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
//        addIsEmptySizeTest();
//        testAddBackCycling();
//        testAddFrontCycling();
//        addFirstRemoveLastTest();
//        addLastRemoveFirstTest();
        addResizeTest();
//        basicAddRmTest();
        addRemoveDownsizeTest();
    }
}
