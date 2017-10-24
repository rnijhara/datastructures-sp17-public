package db;
import java.util.ArrayList;

/**
 * Created by XWEN on 2/28/2017.
 */

// Note on where expressions
// Pretty much sae as SelectExprs (get a String[] by comma splitting)
// Instantiate ArrayList for WhereExprs
// Iterate through exprs in String[] to get column names, literals, comparison signs
// Issue with above is that there could be no spaces between x>y for example so not as easy as whitespace splitting
// Get copys of ColumnHeaders with ColumnHeaders.getHeaderByName(table, name).copy()
// Create new WhereExprs and add to WhereExprs
// Get resulting table with Table.filterTable(table, whereExprs)

public class WhereExpr {
    boolean isUnary;
    ColumnHeader col1;
    ColumnHeader col2;
    private String sign;
    // typeCheck before passing in string literal
    private String literal;

    /** Unary constructor */
    public WhereExpr(ColumnHeader col1, String sign, String literal) {
        isUnary = true;
        this.col1 = col1;
        if (!sign.equals("==") && !sign.equals(">=") && !sign.equals(">")
                && !sign.equals("<") && !sign.equals("<=") && !sign.equals("!=")) {
            throw new RuntimeException("ERROR: malformed conditional");
        }
        this.sign = sign;
        this.literal = literal;
    }

    /** Binary constructor */
    public WhereExpr(ColumnHeader col1, String sign, ColumnHeader col2) {
        isUnary = false;
        this.col1 = col1;
        if (!sign.equals("==") && !sign.equals(">=") && !sign.equals(">")
                && !sign.equals("<") && !sign.equals("<=") && !sign.equals("!=")) {
            throw new RuntimeException("ERROR: malformed conditional");
        }
        this.sign = sign;
        this.col2 = col2;
    }

    /** Returns true if a row satisfies where clause */
    public boolean evaluateUnaryToRow(ArrayList<Object> row, int index1) {
        if (row.get(index1).equals(SpecialValues.NOVALUE)) {
            return false;
        }

        // String comparison
        if (col1.type.equals(String.class)) {
            String item = (String) row.get(index1);
            String other = (String) literal;
            if (Utils.isNumeric(other)) {
                throw new RuntimeException("ERROR: incompatible types");
            }
            if (sign.equals("==")) {
                return item.compareTo(other) == 0;
            } else if (sign.equals("!=")) {
                return item.compareTo(other) != 0;
            } else if (sign.equals(">=")) {
                return item.compareTo(other) >= 0;
            } else if (sign.equals("<=")) {
                return item.compareTo(other) <= 0;
            } else if (sign.equals(">")) {
                return item.compareTo(other) > 0;
            } else if (sign.equals("<")) {
                return item.compareTo(other) < 0;
            }
        } else if (col1.type.equals(Integer.class)) {
            Integer item = (Integer) row.get(index1);

            // Need to account for parsing errors here
            try {
                if (!Utils.isNumeric(literal)) {
                    throw new RuntimeException("ERROR: incompatible type");
                }
                Integer other = Integer.parseInt(literal);
                if (sign.equals("==")) {
                    return item.compareTo(other) == 0;
                } else if (sign.equals("!=")) {
                    return item.compareTo(other) != 0;
                } else if (sign.equals(">=")) {
                    return item.compareTo(other) >= 0;
                } else if (sign.equals("<=")) {
                    return item.compareTo(other) <= 0;
                } else if (sign.equals(">")) {
                    return item.compareTo(other) > 0;
                } else if (sign.equals("<")) {
                    return item.compareTo(other) < 0;
                }
            } catch (NumberFormatException e) {
                if (!Utils.isNumeric(literal)) {
                    throw new RuntimeException("ERROR: incompatible type");
                }
                Float other = Float.parseFloat(literal);
                Float itemPromo = (float) item;
                if (sign.equals("==")) {
                    return itemPromo.compareTo(other) == 0;
                } else if (sign.equals("!=")) {
                    return itemPromo.compareTo(other) != 0;
                } else if (sign.equals(">=")) {
                    return itemPromo.compareTo(other) >= 0;
                } else if (sign.equals("<=")) {
                    return itemPromo.compareTo(other) <= 0;
                } else if (sign.equals(">")) {
                    return itemPromo.compareTo(other) > 0;
                } else if (sign.equals("<")) {
                    return itemPromo.compareTo(other) < 0;
                }
            }
        } else if (col1.type.equals(Float.class)) {
            Float item = (Float) row.get(index1);
            if (!Utils.isNumeric(literal)) {
                throw new RuntimeException("ERROR: incompatible type");
            }
            Float other = Float.parseFloat(literal);
            if (sign.equals("==")) {
                return item.compareTo(other) == 0;
            } else if (sign.equals("!=")) {
                return item.compareTo(other) != 0;
            } else if (sign.equals(">=")) {
                return item.compareTo(other) >= 0;
            } else if (sign.equals("<=")) {
                return item.compareTo(other) <= 0;
            } else if (sign.equals(">")) {
                return item.compareTo(other) > 0;
            } else if (sign.equals("<")) {
                return item.compareTo(other) < 0;
            }
        }
        return false;
    }

    /** Returns true if a row satisfies a binary where clause */
    public boolean evaluateBinaryToRow(ArrayList<Object> row, int index1, int index2) {
        Object obj1 = row.get(index1);
        Object obj2 = row.get(index2);
        if (obj1.equals(SpecialValues.NOVALUE) || obj2.equals(SpecialValues.NOVALUE)) {
            return false;
        }
        col1.checkCmpBinary(col2, sign);

        // String comparison

        if (col1.type.equals(String.class)) {
            String item1 = (String) obj1;
            String item2 = (String) obj2;
            if (sign.equals("==")) {
                return item1.compareTo(item2) == 0;
            } else if (sign.equals("!=")) {
                return item1.compareTo(item2) != 0;
            } else if (sign.equals(">=")) {
                return item1.compareTo(item2) >= 0;
            } else if (sign.equals("<=")) {
                return item1.compareTo(item2) <= 0;
            } else if (sign.equals(">")) {
                return item1.compareTo(item2) > 0;
            } else if (sign.equals("<")) {
                return item1.compareTo(item2) < 0;
            }
        }

        // Number comparison

        // Accounting for NaN first

        // Obj1 as NaN greater than everything except another NaN, to which it is equal
        if (obj1.equals(SpecialValues.NAN)) {
            if (obj2.equals(SpecialValues.NAN)) {
                if (sign.equals(">=") || sign.equals("==") || sign.equals("<=")) {
                    return true;
                }
                return false;
            }
            if (sign.equals(">=") || sign.equals(">") || sign.equals("!=")) {
                return true;
            }
            return false;
        }

        // Obj2 as NaN, but Obj1 is not
        if (obj2.equals(SpecialValues.NAN)) {
            if (sign.equals("<") || sign.equals("<=") || sign.equals("!=")) {
                return true;
            }
            return false;
        }

        // Both columns hold non-SV integers
        if (col1.getComboType(col2).equals(Integer.class)) {
            Integer item1 = (Integer) obj1;
            Integer item2 = (Integer) obj2;
            if (sign.equals("==")) {
                return item1.compareTo(item2) == 0;
            } else if (sign.equals("!=")) {
                return item1.compareTo(item2) != 0;
            } else if (sign.equals(">=")) {
                return item1.compareTo(item2) >= 0;
            } else if (sign.equals("<=")) {
                return item1.compareTo(item2) <= 0;
            } else if (sign.equals(">")) {
                return item1.compareTo(item2) > 0;
            } else if (sign.equals("<")) {
                return item1.compareTo(item2) < 0;
            }
        }

        // One or both columns hold non-SV floats
        if (col1.type.equals(Integer.class)) {
            Integer item1 = (Integer) obj1;
            Float item1Promo = (float) item1;
            Float item2 = (Float) obj2;
            return NumberOps.floatFloatCmp(item1Promo, item2, sign);
        } else if (col2.type.equals(Integer.class)) {
            Float item1 = (Float) obj1;
            Integer item2 = (Integer) obj2;
            Float item2Promo = (float) item2;
            return NumberOps.floatFloatCmp(item1, item2Promo, sign);
        } else {
            Float item1 = (Float) obj1;
            Float item2 = (Float) obj2;
            return NumberOps.floatFloatCmp(item1, item2, sign);
        }
    }
}
