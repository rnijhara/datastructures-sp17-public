package db;
import java.util.ArrayList;

/**
 * Created by XWEN on 2/26/2017.
 */


public class ColExpr {
    private boolean isCopy;
    boolean isUnary;
    boolean intPromoted;
    ColumnHeader col1;
    ColumnHeader col2;
    private String operation;
    // Possibly use Object literal instead of String
    // Using string means that parsing methods would need to typeCheck columns first
    private String literal;
    String alias;

    /** Copy column ColExpr constructor */
    public ColExpr(ColumnHeader col1) {
        isCopy = true;
        isUnary = true;
        this.col1 = col1;
    }

    /** Unary operation ColExpr constructor */
    public ColExpr(ColumnHeader col1, String operation, String literal) {
        isCopy = false;
        isUnary = true;
        this.col1 = col1;
        this.operation = operation;
        this.literal = literal;
    }

    /** Binary operation ColExpr constructor */
    public ColExpr(ColumnHeader col1, String operation, ColumnHeader col2, String alias) {
        isCopy = false;
        isUnary = false;
        this.col1 = col1;
        this.col2 = col2;
        this.operation = operation;
        this.alias = alias;
    }

    /** Returns new data object after applying operation to single column and literal */
    public Object applyUnaryToRow(ArrayList<Object> row, int index1) {
        if (isCopy) {
            return row.get(index1);
        }
        // catch would be malformed column expression
        col1.checkOpUnary(operation);
        Class type1 = col1.type;
        if (type1.equals(String.class)) {
            String other = literal;
            if (Utils.isNumeric(other)) {
                throw new RuntimeException("ERROR: incompatible types");
            }
            if (row.get(index1).equals(SpecialValues.NOVALUE)) {
                return other;
            }
            String item1 = (String) row.get(index1);
            return item1 + other;
        }
        if (type1.equals(Float.class)) {
            Float other = Float.parseFloat(literal);
            Object obj1 = row.get(index1);
            if (obj1.equals(SpecialValues.NAN)) {
                return SpecialValues.NAN;
            }
            if (other.equals(0.0f) && operation.equals("/")) {
                return SpecialValues.NAN;
            }
            if (obj1.equals(SpecialValues.NOVALUE)) {
                return NumberOps.floatFloat(0.0f, other, operation);
            }
            Float item1 = (Float) row.get(index1);
            return NumberOps.floatFloat(item1, other, operation);
        } else {
            Object obj1 = row.get(index1);
            try {
                Integer other = Integer.parseInt(literal);
                if (obj1.equals(SpecialValues.NAN)) {
                    return SpecialValues.NAN;
                }
                if (other.equals(0) && operation.equals("/")) {
                    return SpecialValues.NAN;
                }
                if (obj1.equals(SpecialValues.NOVALUE)) {
                    return NumberOps.intInt(0, other, operation);
                }
                Integer item1 = (Integer) row.get(index1);
                return NumberOps.intInt(item1, other, operation);
            } catch (NumberFormatException e) {
                Float other = Float.parseFloat(literal);
                col1.intPromoted = true;
                if (obj1.equals(SpecialValues.NAN)) {
                    return SpecialValues.NAN;
                }
                if (other.equals(0.0f) && operation.equals("/")) {
                    return SpecialValues.NAN;
                }
                if (obj1.equals(SpecialValues.NOVALUE)) {
                    return NumberOps.intFloat(0, other, operation);
                }
                Integer item1 = (Integer) row.get(index1);
                return NumberOps.intFloat(item1, other, operation);
            }
        }
    }

    /** Returns new data object after applying operation to two columns */
    // Incorporating special values
    public Object applyBinaryToRow(ArrayList<Object> row, int index1, int index2) {
        col1.checkOpBinary(col2, operation);
        Class type1 = col1.type;
        Class type2 = col2.type;

        // String concatenation, operation already checked
        if (type1.equals(String.class)) {

            if (row.get(index1).equals(SpecialValues.NOVALUE)) {
                return row.get(index2);
            }
            if (row.get(index2).equals(SpecialValues.NOVALUE)) {
                return row.get(index1);
            }

            String item1 = (String) row.get(index1);
            String item2 = (String) row.get(index2);
            return item1 + item2;
        }

        Object obj1 = row.get(index1);
        Object obj2 = row.get(index2);
        return SpecialValues.binaryNumOpsSV(obj1, obj2, col1, col2, operation);
    }
}
