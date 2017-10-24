package db;

/**
 * Created by XWEN on 2/27/2017.
 */

// Utility class for numerical operations
public class NumberOps {

    public static Float floatFloat(Float item1, Float item2, String op) {
        if (op.equals("+")) {
            return item1 + item2;
        } else if (op.equals("-")) {
            return item1 - item2;
        } else if (op.equals("*")) {
            return item1 * item2;
        } else if (op.equals("/")) {
            return item1 / item2;
        }
        throw new RuntimeException(("ERROR: malformed column expression"));
    }

    public static Integer intInt(Integer item1, Integer item2, String op) {
        if (op.equals("+")) {
            return item1 + item2;
        } else if (op.equals("-")) {
            return item1 - item2;
        } else if (op.equals("*")) {
            return item1 * item2;
        } else if (op.equals("/")) {
            return item1 / item2;
        }
        throw new RuntimeException(("ERROR: malformed column expression"));
    }

    public static Float intFloat(Integer item1, Float item2, String op) {
        if (op.equals("+")) {
            return item1 + item2;
        } else if (op.equals("-")) {
            return item1 - item2;
        } else if (op.equals("*")) {
            return item1 * item2;
        } else if (op.equals("/")) {
            return item1 / item2;
        }
        throw new RuntimeException(("ERROR: malformed column expression"));
    }

    public static Float floatInt(Float item1, Integer item2, String op) {
        if (op.equals("+")) {
            return item1 + item2;
        } else if (op.equals("-")) {
            return item1 - item2;
        } else if (op.equals("*")) {
            return item1 * item2;
        } else if (op.equals("/")) {
            return item1 / item2;
        }
        throw new RuntimeException(("ERROR: malformed column expression"));
    }

    public static boolean floatFloatCmp(Float item1, Float item2, String sign) {
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
        throw new RuntimeException("ERROR: malformed conditional");
    }
}
