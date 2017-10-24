package synthesizer;
import java.util.Iterator;

/**
 * Created by XWEN on 2/17/2017.
 */
public interface BoundedQueue<T> extends Iterable<T> {

    /** Returns size of the buffer */
    int capacity();

    /** Return number of items currently in the buffer */
    int fillCount();

    /** Adds item x to the end */
    void enqueue(T x);

    /** Delete and return item from the current */
    T dequeue();

    /** Returns, but does not delete, item from the front */
    T peek();

    /** Returns iterator object */
    Iterator<T> iterator();

    /** Returns true is the buffer is empty */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /** Returns true if the buffer is full */
    default boolean isFull() {
        return fillCount() == capacity();
    }
}
