package db;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by XWEN on 2/22/2017.
 */
public class TestRow {

    @Test
    public void testNOVAL() {
        String[] titles1 = {"X int", "Y float", "Z string"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "5.0", "hello"});
        t1.addRow(new String[]{"8", "NOVALUE", "world"});
        t1.addRow(new String[]{"13", "7.0", "NOVALUE"});
        assertTrue(t1.rowStore.get(0).get(0) instanceof SpecialValues);
        assertTrue(t1.rowStore.get(1).get(1) instanceof SpecialValues);
        assertTrue(t1.rowStore.get(2).get(2) instanceof SpecialValues);
    }

    @Test
    public void testJoin1() {
        String[] titles1 = {"X int", "Y int"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"2", "5"});
        t1.addRow(new String[]{"8", "3"});
        t1.addRow(new String[]{"13", "7"});
        String[] titles2 = {"X int", "Z int"};
        Table t2 = new Table(titles2);
        t2.addRow(new String[]{"2", "4"});
        t2.addRow(new String[]{"8", "9"});
        t2.addRow(new String[]{"10", "11"});
        t2.addRow(new String[]{"11", "1"});
        ArrayList<Table> toJoin = new ArrayList<>();
        toJoin.add(t1);
        toJoin.add(t2);
        Table t3 = Table.joinSeries(toJoin);
        ArrayList<Object> t3r0 = new ArrayList<>();
        t3r0.add(2);
        t3r0.add(5);
        t3r0.add(4);
        assertEquals(t3r0, t3.rowStore.get(0));
    }

    @Test
    public void testJoin2() {
        Table t1 = new Table(new String[]{"X int", "Y int"});
        t1.addRow(new String[]{"1", "4"});
        t1.addRow(new String[]{"2", "5"});
        t1.addRow(new String[]{"3", "6"});
        Table t2 = new Table(new String[]{"X int", "Z int"});
        t2.addRow(new String[]{"1", "7"});
        t2.addRow(new String[]{"7", "7"});
        t2.addRow(new String[]{"1", "9"});
        t2.addRow(new String[]{"1", "11"});
        ArrayList<Table> toJoin = new ArrayList<>();
        toJoin.add(t1);
        toJoin.add(t2);
        Table t3 = Table.joinSeries(toJoin);
        Integer[] row0 = {1, 4, 7};
        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.addAll(Arrays.asList(row0));
        Integer[] row1 = {1, 4, 9};
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.addAll(Arrays.asList(row1));
        Integer[] row2 = {1, 4, 11};
        ArrayList<Object> expR2 = new ArrayList<>();
        expR2.addAll(Arrays.asList(row2));
        assertEquals(expR0, t3.rowStore.get(0));
        assertEquals(expR1, t3.rowStore.get(1));
        assertEquals(expR2, t3.rowStore.get(2));
    }

    @Test
    public void testCartJoin() {
        Table t1 = new Table(new String[]{"X int", "Y int", "Z int"});
        t1.addRow(new String[]{"2", "5", "4"});
        t1.addRow(new String[]{"8", "3", "9"});
        Table t2 = new Table(new String[]{"A int", "B int"});
        t2.addRow(new String[]{"7", "0"});
        t2.addRow(new String[]{"2", "8"});
        Table t3cj = Table.cartJoin(t1, t2);
        Table t3j = Table.join(t1, t2);
        int[] row0 = {2, 5, 4, 7, 0};
        ArrayList<Object> expR0 = new ArrayList<>();
        for (int i : row0) {
            expR0.add(i);
        }
        assertEquals(expR0, t3j.rowStore.get(0));
        assertEquals(expR0, t3cj.rowStore.get(0));
    }

    @Test
    public void testCartJoinCopy() {
        Table t1 = new Table(new String[]{"X int", "Y int", "Z int"});
        t1.addRow(new String[]{"2", "5", "4"});
        t1.addRow(new String[]{"8", "3", "9"});
        Table t2 = new Table(new String[]{"A int", "B int"});
        t2.addRow(new String[]{"7", "0"});
        t2.addRow(new String[]{"2", "8"});
        Table t3cj = Table.cartJoin(t1, t2);
        Table t3j = Table.join(t1, t2);
        Table t3copyJ = Table.copyTable(t3j);
        Table t3copyCJ = Table.copyTable(t3cj);
        int[] row0 = {2, 5, 4, 7, 0};
        ArrayList<Object> expR0 = new ArrayList<>();
        for (int i : row0) {
            expR0.add(i);
        }
        assertEquals(expR0, t3copyJ.rowStore.get(0));
        assertEquals(expR0, t3copyCJ.rowStore.get(0));
        assertEquals(4, t3copyCJ.rows);
        assertEquals(4, t3copyJ.rows);
        assertEquals(5, t3copyCJ.cols);
        assertEquals(5, t3copyJ.cols);
    }

    @Test
    public void noJoin() {
        Table t1 = new Table(new String[]{"X int", "Y int"});
        t1.addRow(new String[]{"1", "7"});
        t1.addRow(new String[]{"7", "7"});
        t1.addRow(new String[]{"1", "9"});
        Table t2 = new Table(new String[]{"X int", "Z int"});
        t2.addRow(new String[]{"3", "8"});
        t2.addRow(new String[]{"4", "9"});
        t2.addRow(new String[]{"5", "10"});
        Table t3 = Table.join(t1, t2);
        assertTrue(t3.rowStore.isEmpty());
    }

    @Test
    public void testJoinSeries() {
        Table t1 = new Table(new String[]{"X int", "Y int", "Z int"});
        t1.addRow(new String[]{"2", "5", "4"});
        t1.addRow(new String[]{"8", "3", "9"});
        Table t2 = new Table(new String[]{"A int", "B int"});
        t2.addRow(new String[]{"7", "0"});
        t2.addRow(new String[]{"2", "8"});
        Table t3 = new Table(new String[]{"A int", "W int"});
        t3.addRow(new String[]{"7", "99"});
        t3.addRow(new String[]{"7", "12"});
        ArrayList<Table> tables = new ArrayList<>();
        tables.add(t1);
        tables.add(t2);
        tables.add(t3);
        Table t4 = Table.joinSeries(tables);
        Integer[] r0 = new Integer[]{7, 2, 5, 4, 0, 99};
        Integer[] r1 = new Integer[]{7, 2, 5, 4, 0, 12};
        Integer[] r2 = new Integer[]{7, 8, 3, 9, 0, 99};
        Integer[] r3 = new Integer[]{7, 8, 3, 9, 0, 12};
        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.addAll(Arrays.asList(r0));
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.addAll(Arrays.asList(r1));
        ArrayList<Object> expR2 = new ArrayList<>();
        expR2.addAll(Arrays.asList(r2));
        ArrayList<Object> expR3 = new ArrayList<>();
        expR3.addAll(Arrays.asList(r3));
        assertEquals(expR0, t4.rowStore.get(0));
        assertEquals(expR1, t4.rowStore.get(1));
        assertEquals(expR2, t4.rowStore.get(2));
        assertEquals(expR3, t4.rowStore.get(3));
        assertEquals(6, t4.cols);
        assertEquals(4, t4.rows);
        System.out.println(t4);


    }

    public static void main(String[] args) {
        ArrayList<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.remove((Integer)2);
        for (Integer i : test) {
            System.out.println(i);
        }
    }
}
