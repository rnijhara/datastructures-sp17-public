package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
//    @Test
    public void addFillTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(7);
        assertEquals(7, arb.capacity());
        assertEquals(0, arb.fillCount());
        for (int i = 0; i < 7; i += 1) {
            arb.enqueue(i);
        }
        for (int i = 0; i < 7; i += 1) {
            assertEquals((Integer) i, arb.peek());
            assertEquals((Integer) i, arb.dequeue());
        }
    }

//    @Test(expected = RuntimeException.class)
    public void testFull() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(7);
        for (int i = 0; i <= 7; i += 1) {
            arb.enqueue(i);
        }
    }

//    @Test(expected = RuntimeException.class)
    public void testEmpty() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(7);
        arb.dequeue();
    }
    /** Calls tests for ArrayRingBuffer. */

    public static void forLoop() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(7);
        for (int i = 0; i < 7; i += 1) {
            arb.enqueue(i);
        }
        arb.dequeue();
        arb.enqueue(7);
        for (int x : arb) {
            for (int y: arb) {
                System.out.println("x: " + x + ", y: " + y);
            }
        }
    }
    public static void main(String[] args) {
//        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
        forLoop();
    }
} 
