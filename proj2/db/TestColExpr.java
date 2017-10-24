package db;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 * Created by XWEN on 2/27/2017.
 */
public class TestColExpr {

    // Unary tests
    @Test
    public void testStringUnary() {
        ColumnHeader z = new ColumnHeader("Z string");
        ColExpr zPlusWorld = new ColExpr(z, "+", "World");
        ArrayList<Object> row = new ArrayList<>();
        row.add("hello");
        row.add("deez");
        assertEquals("helloWorld", zPlusWorld.applyUnaryToRow(row, 0));
        assertEquals("deezWorld", zPlusWorld.applyUnaryToRow(row, 1));
    }

    @Test
    public void testStringCopy() {
        ColumnHeader z = new ColumnHeader("Z string");
        ColExpr zPlusWorld = new ColExpr(z);
        ArrayList<Object> row = new ArrayList<>();
        row.add("hello");
        row.add("deez");
        assertEquals("hello", zPlusWorld.applyUnaryToRow(row, 0));
        assertEquals("deez", zPlusWorld.applyUnaryToRow(row, 1));
    }

    @Test(expected=Exception.class)
    public void testStringError() {
        ColumnHeader z = new ColumnHeader("Z string");
        ColExpr zPlusWorld = new ColExpr(z, "-", "o");
        ArrayList<Object> row = new ArrayList<>();
        row.add("hello");
        row.add("deez");
        assertEquals("hello", zPlusWorld.applyUnaryToRow(row, 0));
        assertEquals("deez", zPlusWorld.applyUnaryToRow(row, 1));
    }

    @Test
    public void testIntUnary() {
        ColumnHeader z = new ColumnHeader("Z int");
        ColExpr zPlus = new ColExpr(z, "+", "6");
        ColExpr zMin = new ColExpr(z, "-", "6");
        ColExpr zMul = new ColExpr(z, "*", "6");
        ColExpr zDiv = new ColExpr(z, "/", "6");
        ArrayList<Object> row = new ArrayList<>();
        row.add(12);
        row.add(6);
        assertEquals(18, zPlus.applyUnaryToRow(row, 0));
        assertEquals(12, zPlus.applyUnaryToRow(row, 1));
        assertEquals(6, zMin.applyUnaryToRow(row, 0));
        assertEquals(0, zMin.applyUnaryToRow(row, 1));
        assertEquals(72, zMul.applyUnaryToRow(row, 0));
        assertEquals(36, zMul.applyUnaryToRow(row, 1));
        assertEquals(2, zDiv.applyUnaryToRow(row, 0));
        assertEquals(1, zDiv.applyUnaryToRow(row, 1));

    }

    // Binary tests

    @Test
    public void testStringBinary() {
        ColumnHeader z = new ColumnHeader("Z string");
        ColumnHeader w = new ColumnHeader("W string");
        ColExpr zPlusW = new ColExpr(z, "+", w, "T");
        ArrayList<Object> row = new ArrayList<>();
        row.add("hello");
        row.add("World");
        assertEquals("helloWorld", zPlusW.applyBinaryToRow(row, 0, 1));
    }

    @Test(expected=Exception.class)
    public void testStringOpError() {
        ColumnHeader z = new ColumnHeader("Z string");
        ColumnHeader w = new ColumnHeader("W string");
        ColExpr zPlusW = new ColExpr(z, "-", w, "T");
        ArrayList<Object> row = new ArrayList<>();
        row.add("hello");
        row.add("World");
        assertEquals("helloWorld", zPlusW.applyBinaryToRow(row, 0, 1));
    }

    @Test(expected=Exception.class)
    public void testStringIntError() {
        ColumnHeader z = new ColumnHeader("Z string");
        ColumnHeader w = new ColumnHeader("W int");
        ColExpr zPlusW = new ColExpr(z, "+", w, "T");
        ArrayList<Object> row = new ArrayList<>();
        row.add("hello");
        row.add(6);
        assertEquals("hello6", zPlusW.applyBinaryToRow(row, 0, 1));
    }

    @Test(expected=Exception.class)
    public void testStringFloatError() {
        ColumnHeader z = new ColumnHeader("Z string");
        ColumnHeader w = new ColumnHeader("W float");
        ColExpr zPlusW = new ColExpr(z, "+", w, "T");
        ArrayList<Object> row = new ArrayList<>();
        row.add("hello");
        row.add(6.0f);
        assertEquals("hello6.0", zPlusW.applyBinaryToRow(row, 0, 1));
    }

    @Test
    public void testIntBinary() {
        ColumnHeader z = new ColumnHeader("Z int");
        ColumnHeader w = new ColumnHeader("W int");
        ColExpr zPlusW = new ColExpr(z, "+", w, "T");
        ColExpr zMinW = new ColExpr(z, "-", w, "T");
        ColExpr zMulW = new ColExpr(z, "*", w, "T");
        ColExpr zDivW = new ColExpr(z, "/", w, "T");
        ArrayList<Object> row = new ArrayList<>();
        row.add(6);
        row.add(3);
        assertEquals(9, zPlusW.applyBinaryToRow(row, 0, 1));
        assertEquals(3, zMinW.applyBinaryToRow(row, 0, 1));
        assertEquals(18, zMulW.applyBinaryToRow(row, 0, 1));
        assertEquals(2, zDivW.applyBinaryToRow(row, 0, 1));
    }
}
