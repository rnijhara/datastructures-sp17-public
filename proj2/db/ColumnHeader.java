package db;

/**
 * Created by XWEN on 2/22/2017.
 */

/** Custom object for overhead storage of column details in a table */
public class ColumnHeader {
    String name;
    String stringrep;
    Class type;
    boolean intPromoted = false;

    /** Returns appropriate object (data) type to be set for the column */
    private static Class checkType(String typeString) {
        if (typeString.equals("int")) {
            return Integer.class;
        } else if (typeString.equals("float")) {
            return Float.class;
        } else if (typeString.equals("string")) {
            return String.class;
        }
        else {
            throw new RuntimeException("ERROR: wrong type");
        }
    }

    /** Constructs ColumnHeader with given String, ex "x int" */
    public ColumnHeader(String headerInput) {
        String[] nameType = headerInput.split("\\s+");
        if (nameType.length != 2) {
            throw new RuntimeException("ERROR: malformed create");
        }
        name = nameType[0];
        stringrep = nameType[0] + " " + nameType[1];
        type = checkType(nameType[1]);
    }

    /** Constructs ColumnHeader with given String[] */
    public ColumnHeader(String[] headerInput) {
        name = headerInput[0];
        stringrep = headerInput[0] + " " + headerInput[1];
        type = checkType(headerInput[1]);
    }

    /** Constructor for selects */
    public ColumnHeader(String name, Class type) {
        this.name = name;
        this.type = type;
        if (type.equals(Integer.class)) {
            stringrep = name + " " + "int";
        } else if (type.equals(Float.class)) {
            stringrep = name + " " + "float";
        } else {
            stringrep = name + " " + "string";
        }
    }
    /** Converts value to data type appropriate for items under ColumnHeader */
    // Note: literal 6 cannot be parsed into 6.0f according to staff solution
    Object fromString(String val) {
        if (val.equals("NOVALUE")) {
            return SpecialValues.NOVALUE;
        }

        // If cannot be parsed correctly from string, it is an error
        try {
            if (type.equals(Integer.class)) {
                return Integer.parseInt(val);
            } else if (type.equals(Float.class)) {
                if (!val.contains(".")) {
                    throw new RuntimeException("ERROR: row does not match table");
                }
                return Float.parseFloat(val);
            }
        } catch (Exception e) {
            throw new RuntimeException("ERROR: row does not match table");
        }
        if (!val.contains("'")) {
            throw new RuntimeException("ERROR: row does not match table");
        }
//        if (Utils.isNumeric(val)) {
//            throw new RuntimeException("ERROR: row does not match table");
//        }
        return val.replaceAll("'", "");
    }

    /** String representation of ColumnHeader for printing */
    public String toString() {
        return stringrep;
    }

    /** Check if name matches given string, for selects */
    public boolean nameMatch(String givenName) {
        return name.equals(givenName);
    }

    /** Returns ColumnHeader in Table matching Name, or null if no match*/
    public static ColumnHeader getHeaderByName(Table t, String name) {
        for (ColumnHeader column : t.colHeaders) {
            if (column.nameMatch(name)) {
                return column;
            }
        }
        throw new RuntimeException("ERROR: no such column: " + name);
    }

    /** Returns index of ColumnHeader in given Table, or -1 if not in table */
    public int getIndexInTable(Table t) {
        for (int i = 0; i < t.cols; i++) {
            if (equals(t.colHeaders.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /** Returns resulting column type after performing operation */
    Class getComboType(ColumnHeader other) {
        if (type.equals(String.class) && other.type.equals(String.class)) {
            return String.class;
        } else if (type.equals(Float.class) || other.type.equals(Float.class)) {
            return Float.class;
        }
        return Integer.class;
    }

    /** Checks if operation is valid between two column types */
    boolean checkOpUnary(String op) {
        boolean opCheck = false;
        if (type.equals(String.class) && op.equals("+")) {
            opCheck = true;
        } else if (!type.equals(String.class) && (op.equals("+") ||
                op.equals("-") || op.equals("/") || op.equals("*"))) {
            opCheck = true;
        }
        if (!opCheck) {
            throw new RuntimeException("ERROR: incompatible types");
        }
        return true;
    }

    /** Checks if operation is valid between two column types */
    boolean checkOpBinary(ColumnHeader other, String op) {
        boolean typeCheck = false;
        if (type.equals(other.type)) {
            typeCheck = true;
        } else if (type.equals(Integer.class) && other.type.equals(Float.class)) {
            typeCheck = true;
        } else if (type.equals(Float.class) && other.type.equals(Integer.class)) {
            typeCheck = true;
        }
        if (!typeCheck) {
            throw new RuntimeException("ERROR: incompatible types");
        }

        //Already checked matching types, so can check unary;
        return checkOpUnary(op);
    }

    /** Checks if comparison is valid between two column types */
    boolean checkCmpBinary(ColumnHeader other, String cmp) {
        boolean typeCheck = false;
        if (type.equals(other.type)) {
            typeCheck = true;
        } else if (type.equals(Integer.class) && other.type.equals(Float.class)) {
            typeCheck = true;
        } else if (type.equals(Float.class) && other.type.equals(Integer.class)) {
            typeCheck = true;
        }
        if (!typeCheck) {
            throw new RuntimeException("ERROR: incompatible types");
        }
        return true;
    }

    /** Returns a copy of ColumnHeader for selects to prevent mutation of source table */
    // Copy ColumnHeader before selecting from tables!
    ColumnHeader copy() {
        return new ColumnHeader(stringrep);
    }

    void intPromoteFloat() {
        if (intPromoted) {
            type = Float.class;
            stringrep = name + " float";
        }
    }

    /** Checking for equality if another ColumnHeader has same name and type */
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;
        ColumnHeader cmp = (ColumnHeader) other;
        if (!this.stringrep.equals(cmp.stringrep)) return false;
        if (!this.type.equals(cmp.type)) return false;
        return true;
    }

}
