package db;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by XWEN on 2/27/2017.
 */
public class TestSelect {

    @Test
    public void colHeaderCopyEquals() {
        ColumnHeader c1 = new ColumnHeader("h string");
        ColumnHeader c2 = c1.copy();
        assertEquals(c1, c2);
        assertTrue(c1.equals(c2));
        assertFalse(c1 == c2);
    }
    // First test getting index from table
    @Test
    public void getColIndex() {
        String[] titles1 = {"X int", "Y int"};
        Table t1 = new Table(titles1);
        ColumnHeader xi = new ColumnHeader("X", Integer.class);
        ColumnHeader yi = new ColumnHeader("Y", Integer.class);
        assertEquals(0, xi.getIndexInTable(t1));
        assertEquals(1, yi.getIndexInTable(t1));
    }

    // Test if resulting column from two columns is correct
    @Test
    public void getComboTypes() {
        ColumnHeader xInt = new ColumnHeader("X int");
        ColumnHeader aInt = new ColumnHeader("A int");
        ColumnHeader zFloat = new ColumnHeader("Z float");
        ColumnHeader bFloat = new ColumnHeader("B float");
        ColumnHeader yString = new ColumnHeader("Y string");
        ColumnHeader wString = new ColumnHeader("W string");
        assertEquals(String.class, yString.getComboType(wString));
        assertEquals(Integer.class, xInt.getComboType(aInt));
        assertEquals(Float.class, xInt.getComboType(zFloat));
        assertEquals(Float.class, bFloat.getComboType(aInt));
        assertEquals(Float.class, zFloat.getComboType(bFloat));
    }

    // Simple copy column select
    @Test
    public void selectSimpleColumns() {
        String[] titles1 = {"X int", "Y int", "Z int"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"2", "5", "8"});
        t1.addRow(new String[]{"8", "3", "13"});
        t1.addRow(new String[]{"13", "7", "1"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X").copy();
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Z").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1));
        selectExprs.add(new ColExpr(header2));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(2);
        expR0.add(8);
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add(8);
        expR1.add(13);
        ArrayList<Object> expR2 = new ArrayList<>();
        expR2.add(13);
        expR2.add(1);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(expR2, t2.rowStore.get(2));
        assertEquals(3, t2.rows);
        assertEquals(2, t2.cols);
    }

    // Unary tests
    @Test
    public void selectStrUnary() {
        String[] titles1 = {"X string", "Y string"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"hello", "herro"});
        t1.addRow(new String[]{"deez", "dis"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X").copy();
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", "NUTS"));
        selectExprs.add(new ColExpr(header2, "+", "WORLD"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add("helloNUTS");
        expR0.add("herroWORLD");
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add("deezNUTS");
        expR1.add("disWORLD");
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(2, t2.rows);
        assertEquals(2, t2.cols);
        for (int r = 0; r < t2.rows; r++) {
            for (int c = 0; c < t2.cols; c++) {
                assertTrue(t2.rowStore.get(r).get(c) instanceof String);
            }
        }
    }

    @Test
    public void selectIntUnary() {
        String[] titles1 = {"X int", "Y int", "Z int"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"2", "5", "8"});
        t1.addRow(new String[]{"8", "3", "13"});
        t1.addRow(new String[]{"13", "7", "1"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X").copy();
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Z").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", "5"));
        selectExprs.add(new ColExpr(header2, "-", "1"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(7);
        expR0.add(7);
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add(13);
        expR1.add(12);
        ArrayList<Object> expR2 = new ArrayList<>();
        expR2.add(18);
        expR2.add(0);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(expR2, t2.rowStore.get(2));
        assertEquals(3, t2.rows);
        assertEquals(2, t2.cols);
        for (int r = 0; r < t2.rows; r++) {
            for (int c = 0; c < t2.cols; c++) {
                assertTrue(t2.rowStore.get(r).get(c) instanceof Integer);
            }
        }
    }

    @Test
    public void selectFloatUnary() {
        String[] titles1 = {"A float", "X float", "Y float", "Z float"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"2.0", "5.0", "8.0", "32.0"});
        t1.addRow(new String[]{"8.0", "3.0", "13.0", "16.0"});
        t1.addRow(new String[]{"13.0", "7.0", "1.0", "8.0"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "A").copy();
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "X").copy();
        ColumnHeader header3 = ColumnHeader.getHeaderByName(t1, "Y").copy();
        ColumnHeader header4 = ColumnHeader.getHeaderByName(t1, "Z").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", "5.0"));
        selectExprs.add(new ColExpr(header2, "-", "1.0"));
        selectExprs.add(new ColExpr(header3, "*", "2.0"));
        selectExprs.add(new ColExpr(header4, "/", "4.0"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(2.0f + 5.0f);
        expR0.add(5.0f - 1.0f);
        expR0.add(8.0f * 2.0f);
        expR0.add(32.0f / 4.0f);
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add(8.0f + 5.0f);
        expR1.add(3.0f - 1.0f);
        expR1.add(13.0f * 2.0f);
        expR1.add(16.0f / 4.0f);
        ArrayList<Object> expR2 = new ArrayList<>();
        expR2.add(13.0f + 5.0f);
        expR2.add(7.0f - 1.0f);
        expR2.add(1.0f * 2.0f);
        expR2.add(8.0f / 4.0f);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(expR2, t2.rowStore.get(2));
        assertEquals(3, t2.rows);
        assertEquals(4, t2.cols);
        for (int r = 0; r < t2.rows; r++) {
            for (int c = 0; c < t2.cols; c++) {
                assertTrue(t2.rowStore.get(r).get(c) instanceof Float);
            }
        }
    }


    @Test
    public void selectStrBinary() {
        String[] titles1 = {"X string", "Y string"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"hello", "World"});
        t1.addRow(new String[]{"deez", "Nuts"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X").copy();
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "combo"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add("helloWorld");
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add("deezNuts");
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(2, t2.rows);
        assertEquals(1, t2.cols);
        for (int r = 0; r < t2.rows; r++) {
            for (int c = 0; c < t2.cols; c++) {
                assertTrue(t2.rowStore.get(r).get(c) instanceof String);
            }
        }
    }

    @Test
    public void selectIntBinary() {
        String[] titles1 = {"X int", "Y int"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"2", "5"});
        t1.addRow(new String[]{"8", "3"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X").copy();
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "A"));
        selectExprs.add(new ColExpr(header1, "-", header2, "B"));
        selectExprs.add(new ColExpr(header1, "*", header2, "C"));
        selectExprs.add(new ColExpr(header1, "/", header2, "D"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(7);
        expR0.add(-3);
        expR0.add(10);
        expR0.add(0);
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add(11);
        expR1.add(5);
        expR1.add(24);
        expR1.add(2);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(2, t2.rows);
        assertEquals(4, t2.cols);
        for (int r = 0; r < t2.rows; r++) {
            for (int c = 0; c < t2.cols; c++) {
                assertTrue(t2.rowStore.get(r).get(c) instanceof Integer);
            }
        }
    }

    @Test
    public void selectFloatBinary() {
        String[] titles1 = {"X float", "Y float"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"2.0", "5.0"});
        t1.addRow(new String[]{"8.0", "3.0"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X").copy();
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "A"));
        selectExprs.add(new ColExpr(header1, "-", header2, "B"));
        selectExprs.add(new ColExpr(header1, "*", header2, "C"));
        selectExprs.add(new ColExpr(header1, "/", header2, "D"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(2.0f + 5.0f);
        expR0.add(2.0f - 5.0f);
        expR0.add(2.0f * 5.0f);
        expR0.add(2.0f / 5.0f);
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add(8.0f + 3.0f);
        expR1.add(8.0f - 3.0f);
        expR1.add(8.0f * 3.0f);
        expR1.add(8.0f / 3.0f);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(2, t2.rows);
        assertEquals(4, t2.cols);
        for (int r = 0; r < t2.rows; r++) {
            for (int c = 0; c < t2.cols; c++) {
                assertTrue(t2.rowStore.get(r).get(c) instanceof Float);
            }
        }
    }

    @Test
    public void selectIntFloatBinary() {
        String[] titles1 = {"X int", "Y float"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"2", "5.0"});
        t1.addRow(new String[]{"8", "3.0"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X").copy();
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "A"));
        selectExprs.add(new ColExpr(header1, "-", header2, "B"));
        selectExprs.add(new ColExpr(header2, "*", header1, "C"));
        selectExprs.add(new ColExpr(header2, "/", header1, "D"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(2 + 5.0f);
        expR0.add(2 - 5.0f);
        expR0.add(5.0f * 2);
        expR0.add(5.0f / 2);
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add(8 + 3.0f);
        expR1.add(8 - 3.0f);
        expR1.add(3.0f * 8);
        expR1.add(3.0f / 8);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(2, t2.rows);
        assertEquals(4, t2.cols);
        for (int r = 0; r < t2.rows; r++) {
            for (int c = 0; c < t2.cols; c++) {
                assertTrue(t2.rowStore.get(r).get(c) instanceof Float);
            }
        }
    }
}
