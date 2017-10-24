public interface Deque<Item> {
    /* Adds item to front of the Deque */
    void addFirst(Item i);

    /* Adds item to back of the Deque */
    void addLast(Item i);

    /* Returns true if the deque is empty, false otherwise */
    boolean isEmpty();

    /* Returns number of items in the Deque */
    int size();

    /* Prints space-separated items held by the Deque */
    void printDeque();

    /* Removes and returns the front item of the Deque or null if empty */
    Item removeFirst();

    /* Removes and returns the last item of the Deque or null if empty */
    Item removeLast();

    /* Gets item at the given index or null if no item exists */
    Item get(int index);
}
