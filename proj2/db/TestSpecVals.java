package db;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by XWEN on 2/28/2017.
 */
public class TestSpecVals {

    // Unary tests
    @Test
    public void selectStrUnarySVs() {
        String[] titles1 = {"X string", "Y string"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "herro"});
        t1.addRow(new String[]{"deez", "NOVALUE"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", "NUTS"));
        selectExprs.add(new ColExpr(header2, "+", "WORLD"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add("NUTS");
        expR0.add("herroWORLD");
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add("deezNUTS");
        expR1.add("WORLD");
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
    public void selectIntUnarySVs() {
        String[] titles1 = {"X int", "Y int"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "69"});
        ArrayList<Object> row1 = new ArrayList<>();
        row1.add(420);
        row1.add(SpecialValues.NAN);
        t1.addRow(row1);
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs1 = new ArrayList<>();
        ArrayList<ColExpr> selectExprs2 = new ArrayList<>();
        selectExprs1.add(new ColExpr(header1, "+", "5"));
        selectExprs1.add(new ColExpr(header2, "-", "7"));
        selectExprs2.add(new ColExpr(header1, "*", "5"));
        selectExprs2.add(new ColExpr(header2, "/", "7"));
        Table t2 = Table.selectFrom(t1, selectExprs1);
        Table t3 = Table.selectFrom(t1, selectExprs2);

        ArrayList<Object> exp2R0 = new ArrayList<>();
        ArrayList<Object> exp3R0 = new ArrayList<>();
        exp2R0.add(5);
        exp2R0.add(62);
        exp3R0.add(0);
        exp3R0.add(69 / 7);
        ArrayList<Object> exp2R1 = new ArrayList<>();
        ArrayList<Object> exp3R1 = new ArrayList<>();
        exp2R1.add(425);
        exp2R1.add(SpecialValues.NAN);
        exp3R1.add(420 * 5);
        exp3R1.add(SpecialValues.NAN);
        assertEquals(exp2R0, t2.rowStore.get(0));
        assertEquals(exp2R1, t2.rowStore.get(1));
        assertEquals(exp3R0, t3.rowStore.get(0));
        assertEquals(exp3R1, t3.rowStore.get(1));
        assertEquals(2, t2.rows);
        assertEquals(2, t2.cols);
    }

    @Test
    public void selectIntUnaryZeroDivs() {
        String[] titles1 = {"X int", "Y int"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "69"});
        ArrayList<Object> row1 = new ArrayList<>();
        row1.add(420);
        row1.add(SpecialValues.NAN);
        t1.addRow(row1);
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "/", "0"));
        selectExprs.add(new ColExpr(header2, "/", "0"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(SpecialValues.NAN);
        expR0.add(SpecialValues.NAN);
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add(SpecialValues.NAN);
        expR1.add(SpecialValues.NAN);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(2, t2.rows);
        assertEquals(2, t2.cols);
    }

    @Test
    public void selectFloatUnarySVs() {
        String[] titles1 = {"X float", "Y float"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "69.0"});
        ArrayList<Object> row1 = new ArrayList<>();
        row1.add(420.0f);
        row1.add(SpecialValues.NAN);
        t1.addRow(row1);
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs1 = new ArrayList<>();
        ArrayList<ColExpr> selectExprs2 = new ArrayList<>();
        selectExprs1.add(new ColExpr(header1, "+", "5.0"));
        selectExprs1.add(new ColExpr(header2, "-", "7.0"));
        selectExprs2.add(new ColExpr(header1, "*", "5.0"));
        selectExprs2.add(new ColExpr(header2, "/", "7.0"));
        Table t2 = Table.selectFrom(t1, selectExprs1);
        Table t3 = Table.selectFrom(t1, selectExprs2);

        ArrayList<Object> exp2R0 = new ArrayList<>();
        ArrayList<Object> exp3R0 = new ArrayList<>();
        exp2R0.add(0.0f + 5.0f);
        exp2R0.add(69.0f - 7.0f);
        exp3R0.add(0f * 5.0f);
        exp3R0.add(69.0f / 7.0f);
        ArrayList<Object> exp2R1 = new ArrayList<>();
        ArrayList<Object> exp3R1 = new ArrayList<>();
        exp2R1.add(420.0f + 5.0f);
        exp2R1.add(SpecialValues.NAN);
        exp3R1.add(420f * 5.0f);
        exp3R1.add(SpecialValues.NAN);
        assertEquals(exp2R0, t2.rowStore.get(0));
        assertEquals(exp2R1, t2.rowStore.get(1));
        assertEquals(exp3R0, t3.rowStore.get(0));
        assertEquals(exp3R1, t3.rowStore.get(1));
        assertEquals(2, t2.rows);
        assertEquals(2, t2.cols);
        assertEquals(2, t2.rows);
        assertEquals(2, t2.cols);
    }

    @Test
    public void selectFloatUnaryZeroDivs() {
        String[] titles1 = {"X float", "Y float"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "69.0"});
        ArrayList<Object> row1 = new ArrayList<>();
        row1.add(420.0f);
        row1.add(SpecialValues.NAN);
        t1.addRow(row1);
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "/", "0.000"));
        selectExprs.add(new ColExpr(header2, "/", "0"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(SpecialValues.NAN);
        expR0.add(SpecialValues.NAN);
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add(SpecialValues.NAN);
        expR1.add(SpecialValues.NAN);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(2, t2.rows);
        assertEquals(2, t2.cols);
    }

    // Binary tests
    @Test
    public void selectStrBinarySVs() {
        String[] titles1 = {"X string", "Y string"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "herro"});
        t1.addRow(new String[]{"deez", "NOVALUE"});
        t1.addRow(new String[]{"NOVALUE", "NOVALUE"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "W"));
        Table t2 = Table.selectFrom(t1, selectExprs);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add("herro");
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add("deez");
        ArrayList<Object> expR2 = new ArrayList<>();
        expR2.add(SpecialValues.NOVALUE);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(expR2, t2.rowStore.get(2));
        assertEquals(3, t2.rows);
        assertEquals(1, t2.cols);
    }

    @Test
    public void selectIntBinarySVs1() {
        String[] titles1 = {"X int", "Y int"};
        Table t1 = new Table(titles1);
        ArrayList<Object> row1 = new ArrayList<>();
        row1.add(SpecialValues.NOVALUE);
        row1.add(SpecialValues.NAN);
        t1.rowStore.add(row1);
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "A"));
        selectExprs.add(new ColExpr(header1, "-", header2, "B"));
        selectExprs.add(new ColExpr(header1, "*", header2, "C"));
        selectExprs.add(new ColExpr(header1, "/", header2, "D"));
        selectExprs.add(new ColExpr(header2, "+", header1, "E"));
        selectExprs.add(new ColExpr(header2, "-", header1, "F"));
        selectExprs.add(new ColExpr(header2, "*", header1, "G"));
        selectExprs.add(new ColExpr(header2, "/", header1, "H"));
        Table t2 = Table.selectFrom(t1, selectExprs);
        ArrayList<Object> expR0 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            expR0.add(SpecialValues.NAN);
        }
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(1, t2.rows);
        assertEquals(8, t2.cols);
    }

    @Test
    public void selectIntBinarySVs2() {
        String[] titles1 = {"X int", "Y int"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "0"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "A"));
        selectExprs.add(new ColExpr(header1, "-", header2, "B"));
        selectExprs.add(new ColExpr(header1, "*", header2, "C"));
        selectExprs.add(new ColExpr(header1, "/", header2, "D"));
        selectExprs.add(new ColExpr(header1, "/", header1, "E"));
        selectExprs.add(new ColExpr(header2, "/", header1, "F"));
        Table t2 = Table.selectFrom(t1, selectExprs);
        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(0);
        expR0.add(0);
        expR0.add(0);
        expR0.add(SpecialValues.NAN);
        expR0.add(SpecialValues.NOVALUE);
        expR0.add(SpecialValues.NAN);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(1, t2.rows);
        assertEquals(6, t2.cols);
    }

    @Test
    public void selectIntBinarySVs3() {
        String[] titles1 = {"X int", "Y int"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "0"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "A"));
        selectExprs.add(new ColExpr(header1, "-", header2, "B"));
        selectExprs.add(new ColExpr(header1, "*", header2, "C"));
        selectExprs.add(new ColExpr(header1, "/", header2, "D"));
        Table t2 = Table.selectFrom(t1, selectExprs);
        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(0);
        expR0.add(0);
        expR0.add(0);
        expR0.add(SpecialValues.NAN);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(1, t2.rows);
        assertEquals(4, t2.cols);
        for (int i = 0; i < 3; i++) {
            assertTrue(t2.rowStore.get(0).get(i) instanceof Integer);
        }
    }

    @Test
    public void selectFloatBinarySVs1() {
        String[] titles1 = {"X float", "Y float"};
        Table t1 = new Table(titles1);
        ArrayList<Object> row1 = new ArrayList<>();
        row1.add(SpecialValues.NOVALUE);
        row1.add(SpecialValues.NAN);
        t1.rowStore.add(row1);
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "A"));
        selectExprs.add(new ColExpr(header1, "-", header2, "B"));
        selectExprs.add(new ColExpr(header1, "*", header2, "C"));
        selectExprs.add(new ColExpr(header1, "/", header2, "D"));
        selectExprs.add(new ColExpr(header2, "+", header1, "E"));
        selectExprs.add(new ColExpr(header2, "-", header1, "F"));
        selectExprs.add(new ColExpr(header2, "*", header1, "G"));
        selectExprs.add(new ColExpr(header2, "/", header1, "H"));
        Table t2 = Table.selectFrom(t1, selectExprs);
        ArrayList<Object> expR0 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            expR0.add(SpecialValues.NAN);
        }
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(1, t2.rows);
        assertEquals(8, t2.cols);
    }

    @Test
    public void selectFloatBinarySVs2() {
        String[] titles1 = {"X float", "Y float"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "0.0"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "A"));
        selectExprs.add(new ColExpr(header1, "-", header2, "B"));
        selectExprs.add(new ColExpr(header1, "*", header2, "C"));
        selectExprs.add(new ColExpr(header1, "/", header2, "D"));
        selectExprs.add(new ColExpr(header1, "/", header1, "E"));
        selectExprs.add(new ColExpr(header2, "/", header1, "F"));
        Table t2 = Table.selectFrom(t1, selectExprs);
        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(0.0f);
        expR0.add(0.0f);
        expR0.add(0.0f);
        expR0.add(SpecialValues.NAN);
        expR0.add(SpecialValues.NOVALUE);
        expR0.add(SpecialValues.NAN);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(1, t2.rows);
        assertEquals(6, t2.cols);
        for (int i = 0; i < 3; i++) {
            assertTrue(t2.rowStore.get(0).get(i) instanceof Float);
        }
    }

    @Test
    public void selectFloatBinarySVs3() {
        String[] titles1 = {"X float", "Y float"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "0.0"});
        ColumnHeader header1 = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader header2 = ColumnHeader.getHeaderByName(t1, "Y");
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(header1, "+", header2, "A"));
        selectExprs.add(new ColExpr(header1, "-", header2, "B"));
        selectExprs.add(new ColExpr(header1, "*", header2, "C"));
        selectExprs.add(new ColExpr(header1, "/", header2, "D"));
        Table t2 = Table.selectFrom(t1, selectExprs);
        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add(0.0f);
        expR0.add(0.0f);
        expR0.add(0.0f);
        expR0.add(SpecialValues.NAN);
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(1, t2.rows);
        assertEquals(4, t2.cols);
    }

    @Test
    public void selectIntFloatBinarySVs3() {
        String[] titles1 = {"X int", "Y int", "W float", "Z float"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"NOVALUE", "0", "NOVALUE", "0.0"});
        ColumnHeader x = ColumnHeader.getHeaderByName(t1, "X");
        ColumnHeader y = ColumnHeader.getHeaderByName(t1, "Y");
        ColumnHeader w = ColumnHeader.getHeaderByName(t1, "W");
        ColumnHeader z = ColumnHeader.getHeaderByName(t1, "Z");
        ArrayList<ColExpr> selectExprs1 = new ArrayList<>();
        selectExprs1.add(new ColExpr(x, "+", z, "A"));
        selectExprs1.add(new ColExpr(w, "+", y, "B"));
        selectExprs1.add(new ColExpr(x, "-", z, "C"));
        selectExprs1.add(new ColExpr(w, "-", y, "D"));
        selectExprs1.add(new ColExpr(x, "*", z, "E"));
        selectExprs1.add(new ColExpr(w, "*", y, "F"));
        selectExprs1.add(new ColExpr(x, "/", z, "G"));
        selectExprs1.add(new ColExpr(w, "/", y, "H"));
        Table t2 = Table.selectFrom(t1, selectExprs1);
        ArrayList<Object> exp1R0 = new ArrayList<>();
        for (int i = 0; i < 6; i ++) {
            exp1R0.add(0.0f);
        }
        exp1R0.add(SpecialValues.NAN);
        exp1R0.add(SpecialValues.NAN);
        assertEquals(exp1R0, t2.rowStore.get(0));
        assertEquals(1, t2.rows);
        assertEquals(8, t2.cols);

        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(z, "+", x, "A"));
        selectExprs.add(new ColExpr(y, "+", w, "B"));
        selectExprs.add(new ColExpr(z, "-", x, "C"));
        selectExprs.add(new ColExpr(y, "-", w, "D"));
        selectExprs.add(new ColExpr(z, "*", x, "E"));
        selectExprs.add(new ColExpr(y, "*", w, "F"));
        selectExprs.add(new ColExpr(z, "/", x, "G"));
        selectExprs.add(new ColExpr(y, "/", w, "H"));
        selectExprs.add(new ColExpr(x, "/", w, "I"));
        selectExprs.add(new ColExpr(w, "/", x, "J"));
        Table t3 = Table.selectFrom(t1, selectExprs);
        ArrayList<Object> exp2R0 = new ArrayList<>();
        for (int i = 0; i < 6; i ++) {
            exp2R0.add(0.0f);
        }
        exp2R0.add(SpecialValues.NAN);
        exp2R0.add(SpecialValues.NAN);
        exp2R0.add(SpecialValues.NOVALUE);
        exp2R0.add(SpecialValues.NOVALUE);
        assertEquals(exp2R0, t3.rowStore.get(0));
        assertEquals(1, t3.rows);
        assertEquals(10, t3.cols);
    }
}
