package db;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.HashSet;

/**
 * Created by XWEN on 2/21/2017.
 */


// Classes currently using: ColumnHeader, ColExpr, NumberOps, SpecialValues, WhereExpr
//
public class Table {
    int rows;
    int cols;
    ArrayList<ArrayList<Object>> rowStore;
    ArrayList<ColumnHeader> colHeaders;
    HashSet<String> colNames;

    /** Creates table with given headers */
    public Table(String[] args) {
        colHeaders = new ArrayList<>();
        rowStore = new ArrayList<>();
        colNames = new HashSet<>();
        cols = args.length;
        rows = 0;
        for (String header : args) {
            int prevSize = colNames.size();
            ColumnHeader newHeader = new ColumnHeader(header);
            colHeaders.add(newHeader);
            colNames.add(newHeader.name);
            if (colNames.size() == prevSize) {
                throw new RuntimeException("ERROR: duplicate column name: " + newHeader.name);
            }
        }
    }

    /** Creates empty table */
    public Table() {
        cols = 0;
        rows = 0;
        colHeaders = new ArrayList<>();
        rowStore = new ArrayList<>();
        colNames = new HashSet<>();
    }

    /** Adds given String[] values as a new row */
    void addRow(String[] values) {
        ArrayList<Object> newRow = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            // If literal cannot be parsed correctly, error runs
//            try {
                Object item = colHeaders.get(i).fromString(values[i]);
                newRow.add(item);
//            } catch (Exception e) {
//                System.out.println("ERROR: row does not match table");
//                return;
//            }
        }
        addRow(newRow);
    }

    /** Adds given ArrayList as a new row */
    void addRow(ArrayList<Object> newRow) {
        if (newRow.size() != cols) {
            throw new RuntimeException("ERROR: row does not match table");
        }
        rowStore.add(newRow);
        rows += 1;
    }

    /** Returns new row as product of inner join with shared columns
     *  first, then unshared ones from r1 and then r2 */
    private static ArrayList<Object> joinRow(List<Object> r1, List<Object> r2,
                                            List<Integer> add1, List<Integer> com2,
                                            List<Integer> uni2) {
        ArrayList<Object> newRow = new ArrayList<>();
        boolean matched = true;
        for (int i = 0; (i < com2.size() && matched); i++) {
            matched = r1.get(add1.get(i)).equals(r2.get(com2.get(i)));
        }
        if (matched) {
            for (int index : add1) {
                newRow.add(r1.get(index));
            }
            for (int index : uni2) {
                newRow.add(r2.get(index));
            }
        }
        return newRow;
    }

    /** Joins Tables t1 and t2 */
    public static Table join(Table t1, Table t2) {
        ArrayList<ColumnHeader> newHeaders = new ArrayList<>();
        ArrayList<Integer> t1Commons = new ArrayList<>();
        ArrayList<Integer> t2Commons = new ArrayList<>();
        for (int i = 0; i < t1.cols; i++) {
            ColumnHeader t1Header = t1.colHeaders.get(i);
            for (int j = 0; j < t2.cols; j++) {
                ColumnHeader t2Header = t2.colHeaders.get(j);
                if (t1Header.equals(t2Header)) {
                    newHeaders.add(t1Header);
                    t1Commons.add(i);
                    t2Commons.add(j);
                }
            }
        }

        if (t2Commons.isEmpty()) {
            return cartJoin(t1, t2);
        }

        ArrayList<Integer> t1Uniques = Utils.uniqueIndices(t1.cols, t1Commons);
        ArrayList<Integer> t2Uniques = Utils.uniqueIndices(t2.cols, t2Commons);
        Utils.updateHeaders(newHeaders, t1Uniques, t1);
        Utils.updateHeaders(newHeaders, t2Uniques, t2);

//        String[] newTableHeaders = Utils.headersToArray(newHeaders);
//        Table newTable = new Table(newTableHeaders);
        Table newTable = new Table();
        newTable.colHeaders = newHeaders;
        newTable.cols = newTable.colHeaders.size();
        t1Commons.addAll(t1Uniques);

        for (int i = 0; i < t1.rows; i++) {
            ArrayList<Object> r1 = t1.rowStore.get(i);
            for (int j = 0; j < t2.rows; j++) {
                ArrayList<Object> r2 = t2.rowStore.get(j);
                ArrayList<Object> combinedRow = joinRow(r1, r2,
                        t1Commons, t2Commons, t2Uniques);
                if (!combinedRow.isEmpty()) {
                    newTable.addRow(combinedRow);
                }
            }
        }
        return newTable;
    }

    /** Cartesian join for Tables t1 and t2 when inner join is not possible */
    public static Table cartJoin(Table t1, Table t2) {
        ArrayList<ColumnHeader> newHeaders = new ArrayList<>();
        newHeaders.addAll(t1.colHeaders);
        newHeaders.addAll(t2.colHeaders);
//        Table newTable = new Table(Utils.headersToArray(newHeaders));
        Table newTable = new Table();
        newTable.colHeaders = newHeaders;
        newTable.cols = newTable.colHeaders.size();
        for (int i = 0; i < t1.rows; i++) {
            for (int j = 0; j < t2.rows; j++) {
                ArrayList<Object> newRow = new ArrayList<>();
                newRow.addAll(t1.rowStore.get(i));
                newRow.addAll(t2.rowStore.get(j));
                newTable.addRow(newRow);
            }
        }
        return newTable;
    }

    /** Joins a series of tables (... from t1, t2, t3, etc) */
    public static Table joinSeries(List<Table> tables) {
        if (tables.size() == 1) {
            return Table.copyTable(tables.get(0));
        }
        ListIterator<Table> tableSeries = tables.listIterator();
        Table t1 = tableSeries.next();
        Table t2 = tableSeries.next();
        Table joined = join(t1, t2);
        while (tableSeries.hasNext()) {
            Table nextTable = tableSeries.next();
            joined = join(joined, nextTable);
        }
        return joined;
    }

    /** Selects columns from given table as new table */
    public static Table selectFrom(Table t, List<ColExpr> exprs) {

        // exprs for indices, iterate rows using LLIterator
        Table newTable = new Table();
        LinkedList<Integer> indexList = new LinkedList<>();
        ArrayList<ColumnHeader> newHeaders = new ArrayList<>();

        // Gets indices for columns selected for; collects new headers
        for (ColExpr expr : exprs) {
            ColumnHeader header1 = expr.col1;
            int index1 = header1.getIndexInTable(t);
            if (index1 == -1) {
                throw new RuntimeException("ERROR: no such column with name: " + header1.name);
            }
            indexList.add(index1);
            int prevSize = newTable.colNames.size();
            if (expr.isUnary) {
                newHeaders.add(header1);
                newTable.colNames.add(header1.name);
                if (newTable.colNames.size() == prevSize) {
                    throw new RuntimeException("ERROR: duplicate column name: " + header1.name);
                }
            } else {
                ColumnHeader header2 = expr.col2;
                int index2 = header2.getIndexInTable(t);
                if (index2 == -1) {
                    throw new RuntimeException("ERROR: no such column with name: " + header2.name);
                }
                indexList.add(index2);
                Class newType = header1.getComboType(header2);
                ColumnHeader aliasHeader = new ColumnHeader(expr.alias, newType);
                newTable.colNames.add(aliasHeader.name);
                newHeaders.add(aliasHeader);
                if (newTable.colNames.size() == prevSize) {
                    throw new RuntimeException("ERROR: duplicate column name: " + aliasHeader.name);
                }
            }
        }

        newTable.colHeaders.addAll(newHeaders);
        newTable.cols = newHeaders.size();
        // Iteration through each row of Table t, selects according to expressions
        for (ArrayList<Object> row : t.rowStore) {
            ArrayList<Object> newRow = new ArrayList<>();
            ListIterator<Integer> indexIterator = indexList.listIterator();
            for (ColExpr expr : exprs) {
                int index1 = indexIterator.next();
                if (expr.isUnary) {
                    newRow.add(expr.applyUnaryToRow(row, index1));
                } else {
                    int index2 = indexIterator.next();
                    newRow.add(expr.applyBinaryToRow(row, index1, index2));
                }
            }
            newTable.addRow(newRow);
        }

        for (ColumnHeader col : newTable.colHeaders) {
            if (col.intPromoted) {
                col.intPromoteFloat();
            }
        }

        return newTable;
    }

    /** Applies where conditions */
    public static void filterTable(Table t, List<WhereExpr> conds) {
        LinkedList<Integer> indexList = new LinkedList<>();

        // Get indices corresponding to where clauses
        for (WhereExpr clause : conds) {
            ColumnHeader header1 = clause.col1;
            int index1 = header1.getIndexInTable(t);
            if (index1 == -1) {
                throw new RuntimeException("ERROR: no such column with name: " + header1.name);
            }
            indexList.add(index1);
            if (!clause.isUnary) {
                ColumnHeader header2 = clause.col2;
                int index2 = header2.getIndexInTable(t);
                if (index2 == -1) {
                    throw new RuntimeException("ERROR: no such column with name: " + header2.name);
                }
                indexList.add(index2);
            }
        }

        ArrayList<ArrayList<Object>> rowsToRemove = new ArrayList<>();

        for (ArrayList<Object> row : t.rowStore) {
            ListIterator<Integer> indexIterator = indexList.listIterator();
            boolean passed = true;
            for (WhereExpr clause : conds) {
                if (!passed) {
                    continue;
                }
                int index1 = indexIterator.next();
                if (clause.isUnary) {
                    if (!clause.evaluateUnaryToRow(row, index1)) {
                        rowsToRemove.add(row);
                        passed = false;
                    }
                } else {
                    int index2 = indexIterator.next();
                    if (!clause.evaluateBinaryToRow(row, index1, index2)) {
                        rowsToRemove.add(row);
                        passed = false;
                    }
                }
            }
        }

        for (ArrayList<Object> row : rowsToRemove) {
            t.rows -= 1;
            t.rowStore.remove(row);
        }
    }

    /** Return a copy of the given Table t, mainly for select * */
    public static Table copyTable(Table t) {
        Table copy = new Table();
        copy.colHeaders.addAll(t.colHeaders);
        copy.rowStore.addAll(t.rowStore);
        copy.colNames.addAll(t.colNames);
        copy.rows = t.rows;
        copy.cols = t.cols;
        return copy;
    }

    /** Prints table */
    // Needs to be updated for proper representation
    // Obsolete now
    public static void print(Table t1) {
        System.out.println(t1.colHeaders);
        for (ArrayList<Object> row : t1.rowStore) {
            System.out.println(row);
        }
    }

    @Override
    public String toString() {
        StringBuilder tString = new StringBuilder();
        for (int i = 0; i < cols; i++) {
            tString.append(colHeaders.get(i).toString());
            if (i == cols - 1) {
                tString.append("\n");
            } else {
                tString.append(",");
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object item = rowStore.get(i).get(j);
                if (item instanceof String) {
                    tString.append("'");
                    tString.append(item.toString());
                    tString.append("'");
                } else if (item instanceof Float) {
                    String floatTrunc = String.format("%.03f", (Float) item);
                    tString.append(floatTrunc);
                } else {
                    tString.append(item.toString());
                }
                if (j == cols - 1) {
                    if (i != rows -1) {
                        tString.append("\n");
                    }
                } else {
                    tString.append(",");
                }
            }
        }
        return tString.toString();
    }

    public static void main(String[] args) {
//        Table t1 = new Table(new String[]{"X int", "Y int", "Z int"});
//        t1.addRow(new String[]{"2", "5", "4"});
//        t1.addRow(new String[]{"8", "3", "9"});
//        Table t2 = new Table(new String[]{"A int", "B int"});
//        t2.addRow(new String[]{"7", "0"});
//        t2.addRow(new String[]{"2", "8"});
//        Table t3 = new Table(new String[]{"A int", "W int"});
//        t3.addRow(new String[]{"7", "99"});
//        t3.addRow(new String[]{"7", "12"});
//        ArrayList<Table> tables = new ArrayList<>();
//        tables.add(t1);
//        tables.add(t2);
//        tables.add(t3);
//        Table t4 = Table.joinSeries(tables);
//        Object testString = "hello";
//        System.out.println(t4);
//        System.out.println(testString.toString());
        String test = "6";
        System.out.println(test.contains(" "));
        System.out.println(Utils.isNumeric(test));
        Float f = Float.parseFloat(test);
        System.out.println(f);
        String m1 = "Mets";
        String m2 = "MLB Baseball";
        System.out.println(m1.compareTo(m2));
        System.out.println(Utils.isNumeric("'5.0'"));
    }
}
