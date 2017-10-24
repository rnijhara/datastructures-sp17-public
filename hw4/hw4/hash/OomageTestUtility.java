package hw4.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /*
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int[] buckets = new int[M];
        int N = oomages.size();
        for (Oomage o : oomages) {
            int bn = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[bn] = buckets[bn] + 1;
        }
        for (int s : buckets) {
            if (s < N / 50) {
                return false;
            }
            if (s > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
