public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private static int RFACTOR = 2;
    private int nextFirst;
    private int nextLast;
    private static double usageThreshold = 0.25;
    private static int MINSIZE = 16;

    /** Constructs empty ArrayDeque */
    public ArrayDeque() {
        size = 0;
        items = (Item[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
    }

    /** Modifies capacity of ArrayDeque to be appropriate for current size */
    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        int firstIndex = plusOne(nextFirst);
        int lastIndex = minusOne(nextLast);
        if (firstIndex < lastIndex) {
            System.arraycopy(items, firstIndex, newItems, 0, size);
        } else {
            System.arraycopy(items, firstIndex, newItems, 0, items.length - firstIndex);
            System.arraycopy(items, 0, newItems, items.length - firstIndex, lastIndex + 1);
        }
        items = newItems;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    private int plusOne(int i) {
        return (i + 1) % items.length;
    }

    private int minusOne(int i) {
        if (i - 1 < 0) {
            return items.length - 1;
        }
        return i - 1;
    }

    /** Returns ratio of ArrayDeque size to items capacity */
    private double usage() {
        double cap = items.length;
        return size / cap;
    }

    private boolean checkShrink() {
        return ((items.length >= MINSIZE) && (usage() < usageThreshold));
    }

    /** Adds an item to the beginning of the ArrayDeque */
    public void addFirst(Item item) {
        /* Checks if resize is necessary before adding */
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        /* Adds item to beginning, updates size and nextFirst */
        items[nextFirst] = item;
        size += 1;
        nextFirst = minusOne(nextFirst);
    }

    /** Adds an item to the end of the ArrayDeque */
    public void addLast(Item item) {
        /* Checks if resize is necessary before adding */
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        /* Adds items to end, updates size and nextLast */
        items[nextLast] = item;
        size += 1;
        nextLast = plusOne(nextLast);
    }

    /** Returns true if the ArrayDeque holds no items */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items held by the ArrayDeque */
    public int size() {
        return size;
    }

    /** Prints each item held by the ArrayDeque separated by a space */
    public void printDeque() {
        int current = plusOne(nextFirst);
        int last = minusOne(nextLast);
        while (current != last) {
            System.out.print((items[current]) + " ");
            current = plusOne(current);
        }
        System.out.println(items[current]);
    }

    /** Removes the first item of the ArrayDeque */
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        /* Removes first item, adjusting size and nextFirst */
        int firstIndex = plusOne(nextFirst);
        Item first = items[firstIndex];
        items[firstIndex] = null;
        size -= 1;
        nextFirst = firstIndex;
        /* If below usage threshold, then capacity shrinks */
        if (checkShrink()) {
            resize(items.length / RFACTOR);
        }
        return first;

    }

    /** Removes the last item of the ArrayDeque */
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        /* Removes last item, adjusting size and nextLast */
        int lastIndex = minusOne(nextLast);
        Item last = items[lastIndex];
        items[lastIndex] = null;
        size -= 1;
        nextLast = lastIndex;
        /* If below usage threshold, then capacity shrinks */
        if (checkShrink()) {
            resize(items.length / RFACTOR);
        }
        return last;
    }

    /** Returns item of ArrayDeque at given index */
    public Item get(int index) {
        int adjustedIndex = plusOne(nextFirst) + index;
        if (adjustedIndex < items.length) {
            return items[adjustedIndex];
        }
        return items[adjustedIndex - items.length];
    }
}
