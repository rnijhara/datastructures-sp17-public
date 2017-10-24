import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDeque1B {

    @Test
    public void integerDequeTests() {
        Integer stud, sol;
        int maxCount;

        for (int i = 1; i <= 50; i++) {
            OperationSequence resize = new OperationSequence();
            ArrayDeque<Integer> studentResize = new ArrayDeque<Integer>();
            ArrayDequeSolution<Integer> expResize = new ArrayDequeSolution<Integer>();
            maxCount = i * 8;

            /* Fills list */
            for (int k = 0; k < maxCount; k++) {
                double opsRNG = StdRandom.uniform();
                int itemRNG = StdRandom.uniform(100);

                if (opsRNG < 0.5) {
                    resize.quickAdd("addFirst", itemRNG);
                    studentResize.addFirst(itemRNG);
                    expResize.addFirst(itemRNG);
                } else {
                    resize.quickAdd("addLast", itemRNG);
                    studentResize.addLast(itemRNG);
                    expResize.addLast(itemRNG);
                }
            }

            /* Removes all from list */
            for (int k = 0; k < maxCount; k++) {
                if (i % 2 == 0) {
                    resize.quickAdd("removeFirst");
                    stud = studentResize.removeFirst();
                    sol = expResize.removeFirst();
                    assertEquals(resize.toString(), sol, stud);
                } else {
                    resize.quickAdd("removeLast");
                    stud = studentResize.removeLast();
                    sol = expResize.removeLast();
                    assertEquals(resize.toString(), sol, stud);
                }
            }

        }


    }
}
