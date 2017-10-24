public class LinkedListDeque<Item> {
    private class MortyNode {
        private Item item;
        private MortyNode next;
        private MortyNode prev;

        private MortyNode(MortyNode p, Item i, MortyNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private int size;
    private MortyNode sent;

    /** Constructs an empty LinkedListDeque */
    public LinkedListDeque() {
        size = 0;
        sent = new MortyNode(null, null, null);
        sent.prev = sent;
        sent.next = sent;
    }

    /** Adds an item to the beginning of the LinkedListDeque */
    public void addFirst(Item item) {
        size += 1;
        MortyNode first = new MortyNode(sent, item, sent.next);
        sent.next.prev = first;
        sent.next = first;
    }

    /** Adds an item to the end of the LinkedListDeque */
    public void addLast(Item item) {
        size += 1;
        MortyNode last = new MortyNode(sent.prev, item, sent);
        sent.prev.next = last;
        sent.prev = last;
    }

    /** Returns true if the LinkedListDeque is empty */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the LinkedListDeque*/
    public int size() {
        return size;
    }

    /** Prints the items of the LinkedListDeque separated by spaces */
    public void printDeque() {
        MortyNode tracker = sent.next;
        while (tracker != sent) {
            System.out.print((tracker.item) + " ");
            tracker = tracker.next;
        }
        System.out.println();
    }

    /** Removes and returns the first item of the LinkedListDeque */
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        MortyNode first = sent.next;
        sent.next.next.prev = sent;
        sent.next = sent.next.next;
        return first.item;
    }

    /** Removes and returns the last item of the LinkedListDeque */
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        MortyNode last = sent.prev;
        sent.prev.prev.next = sent;
        sent.prev = sent.prev.prev;
        return last.item;
    }

    /** Iterative indexing method for a LinkedListDeque */
    public Item get(int index) {
        MortyNode current = sent;
        while (index >= 0) {
            current = current.next;
            if (current == sent) {
                return null;
            }
            index -= 1;
        }
        return current.item;
    }

    /** Helper method for recursive get */
    private Item getHelper(MortyNode start, int i) {
        if (start == sent) {
            return null;
        } else if (i == 0) {
            return start.item;
        }
        return getHelper(start.next, i - 1);
    }

    /** Recursive indexing method for a LinkedListDeque */
    public Item getRecursive(int index) {
        return getHelper(sent.next, index);
    }
}
