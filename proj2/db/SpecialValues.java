package db;

/**
 * Created by XWEN on 2/25/2017.
 */
public enum SpecialValues {
    NOVALUE("NOVALUE"),
    NAN("NaN");

    protected final String stringrep;

    SpecialValues(String stringrep) {
        this.stringrep = stringrep;
    }

    public String toString() {
        return stringrep;
    }

    public static Object binaryNumOpsSV(Object obj1, Object obj2,
                                  ColumnHeader col1, ColumnHeader col2, String op) {
        if (obj1.equals(SpecialValues.NAN) || obj2.equals(SpecialValues.NAN)) {
            return SpecialValues.NAN;
        }

        // Two NOVALUEs cannot create a value
        if ((obj1.equals(SpecialValues.NOVALUE) && obj2.equals(SpecialValues.NOVALUE))) {
            return SpecialValues.NOVALUE;
        }

        // Zero Division
        if ((obj2.equals(SpecialValues.NOVALUE) || obj2.equals(0) || obj2.equals(0.0f))
                && op.equals("/")) {
            return SpecialValues.NAN;
        }

        Class newType = col1.getComboType(col2);
        Class type1 = col1.type;
        Class type2 = col2.type;

        // Object1 is NOVALUE
        if (obj1.equals(SpecialValues.NOVALUE)) {

            // Object2 also NOVALUE -> NOVALUE, except for division already covered (this is redundant now)
            if (obj2.equals(SpecialValues.NOVALUE)) {
                return SpecialValues.NOVALUE;
            }
            // Object2 is valued Float or Integer

            // Integer as new class: Integer + Integer
            if (newType.equals(Integer.class)) {
                return NumberOps.intInt(0, (Integer) obj2, op);
            }

            // Redundant check since Strings are already accounted for in ColExpr
            if (newType.equals(Float.class)) {
                if (type1.equals(Integer.class)) {
                    return NumberOps.intFloat(0, (Float) obj2, op);
                } else if (type2.equals(Integer.class)) {
                    return NumberOps.floatInt(0.0f, (Integer) obj2, op);
                }
                return NumberOps.floatFloat(0.0f, (Float) obj2, op);
            }
        } else if (obj2.equals(SpecialValues.NOVALUE)) {
            // Only Object2 is NOVALUE
            if (newType.equals(Integer.class)) {
                return NumberOps.intInt((Integer) obj1, 0, op);
            }

            if (newType.equals(Float.class)) {
                if (type1.equals(Integer.class)) {
                    return NumberOps.intFloat((Integer) obj1, 0.0f, op);
                } else if (type2.equals(Integer.class)) {
                    return NumberOps.floatInt((Float) obj1, 0, op);
                }
                return NumberOps.floatFloat((Float) obj1, 0.0f, op);
            }
        }
        if (newType.equals(Integer.class)) {
            return NumberOps.intInt((Integer) obj1, (Integer) obj2, op);
        }
        if (type1.equals(Integer.class)) {
            return NumberOps.intFloat((Integer) obj1, (Float) obj2, op);
        } else if (type2.equals(Integer.class)) {
            return NumberOps.floatInt((Float) obj1, (Integer) obj2, op);
        }
        return NumberOps.floatFloat((Float) obj1, (Float) obj2, op);
    }
}
