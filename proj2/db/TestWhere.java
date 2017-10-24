package db;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by XWEN on 2/28/2017.
 */
public class TestWhere {

    @Test
    public void stringUnaryCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add("hello");
        row.add("world");
        row.add("cs61b");
        row.add("hug");
        ColumnHeader y = new ColumnHeader("Y string");
        WhereExpr w1 = new WhereExpr(y, "==", "hello");
        assertTrue(w1.evaluateUnaryToRow(row, 0));
        assertFalse(w1.evaluateUnaryToRow(row, 1));
        assertFalse(w1.evaluateUnaryToRow(row, 2));
        assertFalse(w1.evaluateUnaryToRow(row, 3));

        WhereExpr w2 = new WhereExpr(y, ">=", "hello");
        assertTrue(w2.evaluateUnaryToRow(row, 0));
        assertTrue(w2.evaluateUnaryToRow(row, 1));
        assertFalse(w2.evaluateUnaryToRow(row, 2));

        WhereExpr w3 = new WhereExpr(y, ">", "hello");
        assertFalse(w3.evaluateUnaryToRow(row, 0));
        assertTrue(w3.evaluateUnaryToRow(row, 1));

        WhereExpr w4 = new WhereExpr(y, "<=", "hello");
        assertTrue(w4.evaluateUnaryToRow(row, 0));
        assertTrue(w4.evaluateUnaryToRow(row, 2));

        WhereExpr w5 = new WhereExpr(y, "!=", "hello");
        assertFalse(w5.evaluateUnaryToRow(row, 0));
        assertTrue(w5.evaluateUnaryToRow(row, 1));

        WhereExpr w6 = new WhereExpr(y, "<", "hello");
        assertFalse(w6.evaluateUnaryToRow(row, 0));
        assertTrue(w6.evaluateUnaryToRow(row, 2));
    }

    @Test
    public void numUnaryCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add(6.0f);
        row.add(50);
        row.add(69);
        row.add(420.0f);
        ColumnHeader x = new ColumnHeader("X float");
        ColumnHeader y = new ColumnHeader("Y int");
        ColumnHeader z = new ColumnHeader("Z int");
        ColumnHeader w = new ColumnHeader("W float");
        WhereExpr y1 = new WhereExpr(y, "==", "69");
        assertTrue(y1.evaluateUnaryToRow(row, 2));
        assertFalse(y1.evaluateUnaryToRow(row, 1));

        WhereExpr x1 = new WhereExpr(x, "<=", "69.0");
        assertTrue(x1.evaluateUnaryToRow(row, 0));
        assertFalse(x1.evaluateUnaryToRow(row, 3));

        WhereExpr z1 = new WhereExpr(z, ">=", "68.0");
        assertTrue(z1.evaluateUnaryToRow(row, 2));
        assertFalse(z1.evaluateUnaryToRow(row, 1));

        WhereExpr x2 = new WhereExpr(x, ">", "68");
        assertFalse(x2.evaluateUnaryToRow(row, 0));
        assertTrue(x2.evaluateUnaryToRow(row, 3));

        WhereExpr w1 = new WhereExpr(w, "!=", "420.0");
        assertTrue(w1.evaluateUnaryToRow(row, 0));
        assertFalse(w1.evaluateUnaryToRow(row, 3));

        WhereExpr w2 = new WhereExpr(w, "<", "420");
        assertTrue(w2.evaluateUnaryToRow(row, 0));
        assertFalse(w2.evaluateUnaryToRow(row, 3));
    }

    // Binary tests
    @Test
    public void stringBinaryCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add("hello");
        row.add("world");
        row.add(SpecialValues.NOVALUE);
        ColumnHeader x = new ColumnHeader("X string");
        ColumnHeader y = new ColumnHeader("Y string");
        ColumnHeader z = new ColumnHeader("Z string");

        WhereExpr y1 = new WhereExpr(y, "==", y);
        assertFalse(y1.evaluateBinaryToRow(row, 1, 0));
        assertTrue(y1.evaluateBinaryToRow(row, 1, 1));

        WhereExpr z1 = new WhereExpr(z, "!=", y);
        assertTrue(z1.evaluateBinaryToRow(row, 1, 0));
        assertFalse(z1.evaluateBinaryToRow(row, 2, 1));
        assertFalse(z1.evaluateBinaryToRow(row, 2, 0));

        WhereExpr x1 = new WhereExpr(x, "<=", y);
        assertTrue(x1.evaluateBinaryToRow(row, 0, 1));
        assertFalse(x1.evaluateBinaryToRow(row, 0, 2));

        WhereExpr x2 = new WhereExpr(x, ">=", y);
        assertTrue(x2.evaluateBinaryToRow(row, 0, 0));
        assertFalse(x2.evaluateBinaryToRow(row, 0, 1));
        assertFalse(x2.evaluateBinaryToRow(row, 0, 2));

        WhereExpr y2 = new WhereExpr(y, ">", x);
        assertTrue(y2.evaluateBinaryToRow(row, 1, 0));
        assertFalse(y2.evaluateBinaryToRow(row, 1, 2));
        assertFalse(y2.evaluateBinaryToRow(row, 1, 1));

        WhereExpr y3 = new WhereExpr(y, "<", x);
        assertFalse(y3.evaluateBinaryToRow(row, 1, 0));
        assertFalse(y3.evaluateBinaryToRow(row, 1, 1));
        assertFalse(y3.evaluateBinaryToRow(row, 1, 2));
    }

    @Test
    public void intBinaryCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add(69);
        row.add(420);
        row.add(SpecialValues.NAN);
        ColumnHeader x = new ColumnHeader("X int");
        ColumnHeader y = new ColumnHeader("Y int");
        ColumnHeader z = new ColumnHeader("Z int");

        WhereExpr y1 = new WhereExpr(y, "==", y);
        assertFalse(y1.evaluateBinaryToRow(row, 1, 0));
        assertTrue(y1.evaluateBinaryToRow(row, 1, 1));
        assertTrue(y1.evaluateBinaryToRow(row, 2, 2));

        WhereExpr z1 = new WhereExpr(z, "!=", y);
        assertTrue(z1.evaluateBinaryToRow(row, 2, 1));
        assertTrue(z1.evaluateBinaryToRow(row, 2, 0));
        assertTrue(z1.evaluateBinaryToRow(row, 1, 2));
        assertFalse(z1.evaluateBinaryToRow(row, 2, 2));
        assertFalse(z1.evaluateBinaryToRow(row, 1, 1));

        WhereExpr x1 = new WhereExpr(x, "<=", y);
        assertTrue(x1.evaluateBinaryToRow(row, 0, 1));
        assertTrue(x1.evaluateBinaryToRow(row, 0, 2));;
        assertTrue(x1.evaluateBinaryToRow(row, 2, 2));

        WhereExpr x2 = new WhereExpr(x, ">=", y);
        assertTrue(x2.evaluateBinaryToRow(row, 0, 0));
        assertFalse(x2.evaluateBinaryToRow(row, 0, 1));
        assertFalse(x2.evaluateBinaryToRow(row, 0, 2));
        assertTrue(x2.evaluateBinaryToRow(row, 2, 2));
        assertTrue(x2.evaluateBinaryToRow(row, 2, 1));

        WhereExpr y2 = new WhereExpr(y, ">", x);
        assertTrue(y2.evaluateBinaryToRow(row, 1, 0));
        assertFalse(y2.evaluateBinaryToRow(row, 1, 2));
        assertFalse(y2.evaluateBinaryToRow(row, 1, 1));
        assertFalse(y2.evaluateBinaryToRow(row, 2, 2));
        assertTrue(y2.evaluateBinaryToRow(row, 2, 1));

        WhereExpr y3 = new WhereExpr(y, "<", x);
        assertTrue(y3.evaluateBinaryToRow(row, 0, 1));
        assertFalse(y3.evaluateBinaryToRow(row, 1, 0));
        assertFalse(y3.evaluateBinaryToRow(row, 1, 1));
        assertTrue(y3.evaluateBinaryToRow(row, 1, 2));
        assertFalse(y3.evaluateBinaryToRow(row, 2, 2));
    }

    @Test
    public void intFloatBinaryEqualsCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add(69);
        row.add(70.0f);
        row.add(85.0f);
        row.add(420);
        row.add(SpecialValues.NAN);
        row.add(SpecialValues.NAN);
        row.add(70);
        row.add(69.0f);
        ColumnHeader x = new ColumnHeader("X int");
        ColumnHeader y = new ColumnHeader("Y float");
        ColumnHeader z = new ColumnHeader("Z float");
        ColumnHeader w = new ColumnHeader("W int");
        ColumnHeader a = new ColumnHeader("A int");
        ColumnHeader b = new ColumnHeader("B float");

        // Col1 int, col2 float, ==
        WhereExpr xy1 = new WhereExpr(x, "==", y);
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 1));
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 4));
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 5));
        assertTrue(xy1.evaluateBinaryToRow(row,4, 5));
        assertTrue(xy1.evaluateBinaryToRow(row,0, 7));

        // Col1 float, col2 float, ==
        WhereExpr yz1 = new WhereExpr(y, "==", z);
        assertTrue(yz1.evaluateBinaryToRow(row, 1, 1));
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 2));
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 5));
        assertFalse(yz1.evaluateBinaryToRow(row, 5, 1));
        assertTrue(yz1.evaluateBinaryToRow(row, 5, 5));

        // Col1 float, col2 int, ==
        WhereExpr zw1 = new WhereExpr(z, "==", w);
        assertTrue(zw1.evaluateBinaryToRow(row, 1, 6));
        assertFalse(zw1.evaluateBinaryToRow(row, 3, 4));
        assertFalse(zw1.evaluateBinaryToRow(row, 3, 5));
        assertFalse(zw1.evaluateBinaryToRow(row, 5, 3));
        assertTrue(zw1.evaluateBinaryToRow(row, 1, 6));
    }

    @Test
    public void intFloatBinaryNotEqualsCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add(69);
        row.add(70.0f);
        row.add(85.0f);
        row.add(420);
        row.add(SpecialValues.NAN);
        row.add(SpecialValues.NAN);
        row.add(70);
        row.add(69.0f);
        ColumnHeader x = new ColumnHeader("X int");
        ColumnHeader y = new ColumnHeader("Y float");
        ColumnHeader z = new ColumnHeader("Z float");
        ColumnHeader w = new ColumnHeader("W int");
        ColumnHeader a = new ColumnHeader("A int");
        ColumnHeader b = new ColumnHeader("B float");

        // Col1 int, col2 float, !=
        WhereExpr xy1 = new WhereExpr(x, "!=", y);
        assertTrue(xy1.evaluateBinaryToRow(row, 0, 1));
        assertTrue(xy1.evaluateBinaryToRow(row, 0, 4));
        assertTrue(xy1.evaluateBinaryToRow(row, 0, 5));
        assertFalse(xy1.evaluateBinaryToRow(row,4, 5));
        assertFalse(xy1.evaluateBinaryToRow(row,0, 7));

        // Col1 float, col2 float, !=
        WhereExpr yz1 = new WhereExpr(y, "!=", z);
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 1));
        assertTrue(yz1.evaluateBinaryToRow(row, 1, 2));
        assertTrue(yz1.evaluateBinaryToRow(row, 1, 5));
        assertTrue(yz1.evaluateBinaryToRow(row, 5, 1));
        assertFalse(yz1.evaluateBinaryToRow(row, 5, 5));

        // Col1 float, col2 int, ==
        WhereExpr zw1 = new WhereExpr(z, "!=", w);
        assertFalse(zw1.evaluateBinaryToRow(row, 1, 6));
        assertTrue(zw1.evaluateBinaryToRow(row, 3, 4));
        assertTrue(zw1.evaluateBinaryToRow(row, 3, 5));
        assertTrue(zw1.evaluateBinaryToRow(row, 5, 3));
        assertFalse(zw1.evaluateBinaryToRow(row, 1, 6));
    }

    @Test
    public void intFloatBinaryGEqualsCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add(69);
        row.add(70.0f);
        row.add(85.0f);
        row.add(420);
        row.add(SpecialValues.NAN);
        row.add(SpecialValues.NAN);
        row.add(70);
        row.add(69.0f);
        ColumnHeader x = new ColumnHeader("X int");
        ColumnHeader y = new ColumnHeader("Y float");
        ColumnHeader z = new ColumnHeader("Z float");
        ColumnHeader w = new ColumnHeader("W int");
        ColumnHeader a = new ColumnHeader("A int");
        ColumnHeader b = new ColumnHeader("B float");

        // Col1 int, col2 float, >=
        WhereExpr xy1 = new WhereExpr(x, ">=", y);
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 1));
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 4));
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 5));
        assertTrue(xy1.evaluateBinaryToRow(row, 3, 2));
        assertTrue(xy1.evaluateBinaryToRow(row, 4, 5));
        assertTrue(xy1.evaluateBinaryToRow(row, 5, 4));
        assertTrue(xy1.evaluateBinaryToRow(row, 0, 7));

        // Col1 float, col2 float, >=
        WhereExpr yz1 = new WhereExpr(y, ">=", z);
        assertTrue(yz1.evaluateBinaryToRow(row, 1, 1));
        assertTrue(yz1.evaluateBinaryToRow(row, 1, 7));
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 2));
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 5));
        assertTrue(yz1.evaluateBinaryToRow(row, 5, 1));
        assertTrue(yz1.evaluateBinaryToRow(row, 5, 5));

        // Col1 float, col2 int, >=
        WhereExpr zw1 = new WhereExpr(z, ">=", w);
        assertTrue(zw1.evaluateBinaryToRow(row, 1, 0));
        assertFalse(zw1.evaluateBinaryToRow(row, 1, 3));
        assertFalse(zw1.evaluateBinaryToRow(row, 1, 5));
        assertTrue(zw1.evaluateBinaryToRow(row, 5, 3));
        assertTrue(zw1.evaluateBinaryToRow(row, 5, 4));
        assertTrue(zw1.evaluateBinaryToRow(row, 4, 5));
    }

    @Test
    public void intFloatBinaryGreaterCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add(69);
        row.add(70.0f);
        row.add(85.0f);
        row.add(420);
        row.add(SpecialValues.NAN);
        row.add(SpecialValues.NAN);
        row.add(70);
        row.add(69.0f);
        ColumnHeader x = new ColumnHeader("X int");
        ColumnHeader y = new ColumnHeader("Y float");
        ColumnHeader z = new ColumnHeader("Z float");
        ColumnHeader w = new ColumnHeader("W int");
        ColumnHeader a = new ColumnHeader("A int");
        ColumnHeader b = new ColumnHeader("B float");

        // Col1 int, col2 float, >
        WhereExpr xy1 = new WhereExpr(x, ">", y);
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 1));
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 4));
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 5));
        assertTrue(xy1.evaluateBinaryToRow(row, 3, 2));
        assertFalse(xy1.evaluateBinaryToRow(row, 4, 5));
        assertFalse(xy1.evaluateBinaryToRow(row, 5, 4));
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 7));
        assertTrue(xy1.evaluateBinaryToRow(row, 3, 7));

        // Col1 float, col2 float, >
        WhereExpr yz1 = new WhereExpr(y, ">", z);
        assertTrue(yz1.evaluateBinaryToRow(row, 1, 7));
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 2));
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 5));
        assertTrue(yz1.evaluateBinaryToRow(row, 5, 1));
        assertFalse(yz1.evaluateBinaryToRow(row, 5, 5));

        // Col1 float, col2 int, >
        WhereExpr zw1 = new WhereExpr(z, ">", w);
        assertTrue(zw1.evaluateBinaryToRow(row, 1, 0));
        assertFalse(zw1.evaluateBinaryToRow(row, 1, 3));
        assertFalse(zw1.evaluateBinaryToRow(row, 1, 5));
        assertTrue(zw1.evaluateBinaryToRow(row, 5, 3));
        assertFalse(zw1.evaluateBinaryToRow(row, 5, 4));
        assertFalse(zw1.evaluateBinaryToRow(row, 4, 5));
    }

    @Test
    public void intFloatBinaryLEqualsCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add(69);
        row.add(70.0f);
        row.add(85.0f);
        row.add(420);
        row.add(SpecialValues.NAN);
        row.add(SpecialValues.NAN);
        row.add(70);
        row.add(69.0f);
        ColumnHeader x = new ColumnHeader("X int");
        ColumnHeader y = new ColumnHeader("Y float");
        ColumnHeader z = new ColumnHeader("Z float");
        ColumnHeader w = new ColumnHeader("W int");
        ColumnHeader a = new ColumnHeader("A int");
        ColumnHeader b = new ColumnHeader("B float");

        // Col1 int, col2 float, <=
        WhereExpr xy1 = new WhereExpr(x, "<=", y);
        assertTrue(xy1.evaluateBinaryToRow(row, 0, 1));
        assertTrue(xy1.evaluateBinaryToRow(row, 0, 7));
        assertFalse(xy1.evaluateBinaryToRow(row, 3, 2));
        assertTrue(xy1.evaluateBinaryToRow(row, 0, 5));
        assertTrue(xy1.evaluateBinaryToRow(row, 4, 5));
        assertTrue(xy1.evaluateBinaryToRow(row, 5, 4));

        // Col1 float, col2 float, <=
        WhereExpr yz1 = new WhereExpr(y, "<=", z);
        assertTrue(yz1.evaluateBinaryToRow(row, 1, 1));
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 7));
        assertTrue(yz1.evaluateBinaryToRow(row, 1, 2));
        assertTrue(yz1.evaluateBinaryToRow(row, 4, 5));
        assertTrue(yz1.evaluateBinaryToRow(row, 5, 4));
        assertFalse(yz1.evaluateBinaryToRow(row, 2, 1));
        assertFalse(yz1.evaluateBinaryToRow(row, 2, 7));

        // Col1 float, col2 int, <=
        WhereExpr zw1 = new WhereExpr(z, "<=", w);
        assertTrue(zw1.evaluateBinaryToRow(row, 1, 3));
        assertTrue(zw1.evaluateBinaryToRow(row, 1, 5));
        assertTrue(zw1.evaluateBinaryToRow(row, 1, 6));
        assertTrue(zw1.evaluateBinaryToRow(row, 4, 5));
        assertTrue(zw1.evaluateBinaryToRow(row, 5, 4));
        assertFalse(zw1.evaluateBinaryToRow(row, 1, 0));
    }

    @Test
    public void intFloatBinaryLesserCmp() {
        ArrayList<Object> row = new ArrayList<>();
        row.add(69);
        row.add(70.0f);
        row.add(85.0f);
        row.add(420);
        row.add(SpecialValues.NAN);
        row.add(SpecialValues.NAN);
        row.add(70);
        row.add(69.0f);
        ColumnHeader x = new ColumnHeader("X int");
        ColumnHeader y = new ColumnHeader("Y float");
        ColumnHeader z = new ColumnHeader("Z float");
        ColumnHeader w = new ColumnHeader("W int");
        ColumnHeader a = new ColumnHeader("A int");
        ColumnHeader b = new ColumnHeader("B float");

        // Col1 int, col2 float, <
        WhereExpr xy1 = new WhereExpr(x, "<", y);
        assertTrue(xy1.evaluateBinaryToRow(row, 0, 1));
        assertFalse(xy1.evaluateBinaryToRow(row, 0, 7));
        assertFalse(xy1.evaluateBinaryToRow(row, 3, 2));
        assertTrue(xy1.evaluateBinaryToRow(row, 0, 5));
        assertFalse(xy1.evaluateBinaryToRow(row, 4, 5));
        assertFalse(xy1.evaluateBinaryToRow(row, 5, 4));

        // Col1 float, col2 float, <
        WhereExpr yz1 = new WhereExpr(y, "<", z);
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 1));
        assertFalse(yz1.evaluateBinaryToRow(row, 1, 7));
        assertTrue(yz1.evaluateBinaryToRow(row, 1, 2));
        assertFalse(yz1.evaluateBinaryToRow(row, 4, 5));
        assertFalse(yz1.evaluateBinaryToRow(row, 5, 4));
        assertFalse(yz1.evaluateBinaryToRow(row, 2, 1));
        assertFalse(yz1.evaluateBinaryToRow(row, 2, 7));

        // Col1 float, col2 int, <
        WhereExpr zw1 = new WhereExpr(z, "<", w);
        assertTrue(zw1.evaluateBinaryToRow(row, 1, 3));
        assertTrue(zw1.evaluateBinaryToRow(row, 1, 5));
        assertFalse(zw1.evaluateBinaryToRow(row, 4, 5));
        assertFalse(zw1.evaluateBinaryToRow(row, 5, 4));
        assertFalse(zw1.evaluateBinaryToRow(row, 1, 0));
    }
}
