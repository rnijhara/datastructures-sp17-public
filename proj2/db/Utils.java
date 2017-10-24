package db;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by XWEN on 2/22/2017.
 */
public class Utils {

    /** Used for joins, returns indices of unshared columns */
    public static ArrayList<Integer> uniqueIndices(int max, List<Integer> old) {
        ArrayList<Integer> uniques = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            if (!old.contains(i)) {
                uniques.add(i);
            }
        }
        return uniques;
    }

    /** Used for joins, adds ColumnHeaders of unshared columns
     *  to a previous List of ColumnHeaders of shared columns */
    public static void updateHeaders(List<ColumnHeader> prev, List<Integer> indices,
                                     Table t) {
        for (int i : indices) {
            ColumnHeader nextHeader = t.colHeaders.get(i);
            prev.add(nextHeader);
        }
    }

    /** Likely obsolete now with empty table constructor */
    public static String[] headersToArray(List<ColumnHeader> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i).stringrep;
        }
        return array;
    }

    /** Returns true if a string is numeric */
    // SOURCE: stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }



}
