package db;

import java.io.File;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;
import java.util.StringJoiner;

public class QueryParse {
    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+",
            PLUS  = "\\s*\\+\\s*",
            MINUS  = "\\s*\\-\\s*",
            MUL  = "\\s*\\*\\s*",
            DIV  = "\\s*\\/\\s*",
            AS = "\\s+as\\s+",
            EQUAL  = "\\s*\\==\\s*",
            NOTEQUAL  = "\\s*\\!=\\s*",
            GREATER  = "\\s*\\>\\s*",
            GREATEREQ  = "\\s*\\>=\\s*",
            LESSER = "\\s*\\<\\s*",
            LESSEREQ  = "\\s*\\<=\\s*";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD   = Pattern.compile("load " + REST),
            STORE_CMD  = Pattern.compile("store " + REST),
            DROP_CMD   = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD  = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");


    public static void main(String[] args) {

    }

    /** Modifies database according to query parsed */
    static String eval(String query, Database db) {
        try {
            Matcher m;
            if ((m = CREATE_CMD.matcher(query)).matches()) {
                createTable(m.group(1), db);
                return "";
            } else if ((m = LOAD_CMD.matcher(query)).matches()) {
                loadTable(m.group(1), db);
                return "";
            } else if ((m = STORE_CMD.matcher(query)).matches()) {
                storeTable(m.group(1), db);
                return "";
            } else if ((m = DROP_CMD.matcher(query)).matches()) {
                dropTable(m.group(1), db);
                return "";
            } else if ((m = INSERT_CMD.matcher(query)).matches()) {
                insertRow(m.group(1), db);
                return "";
            } else if ((m = PRINT_CMD.matcher(query)).matches()) {
                return printTable(m.group(1), db);
            } else if ((m = SELECT_CMD.matcher(query)).matches()) {
                Table selected = select(m.group(1), db);
                return selected.toString();
            } else {
                throw new RuntimeException("ERROR: Malformed query");
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /** Creates new table and adds to database */
    private static void createTable(String expr, Database db) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).split(COMMA), db);
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4), db);
        } else {
            throw new RuntimeException("ERROR: malformed create");
        }
    }

    /** Table creation based on user input */
    private static void createNewTable(String name, String[] cols, Database db) {
//        StringJoiner joiner = new StringJoiner(", ");
//        for (int i = 0; i < cols.length-1; i++) {
//            joiner.add(cols[i]);
//        }
//
//        String colSentence = joiner.toString() + " and " + cols[cols.length-1];
//        System.out.printf("You are trying to create a table named %s with the columns %s\n", name, colSentence);

        if (db.tables.containsKey(name)) {
            throw new RuntimeException("ERROR: table already exists");
        }
        for (int i = 0; i < cols.length; i++) {
            String[] nameType = cols[i].split("\\s+");
            if (nameType.length != 2) {
                throw new RuntimeException("ERROR: malformed create");
            }
            cols[i] = nameType[0] + " " + nameType[1];
        }
        Table newTable = new Table(cols);
        db.tables.put(name, newTable);

    }

    /** Table creation based on selection of existing tables */
    private static void createSelectedTable(String name, String exprs, String tables, String conds, Database db) {
//        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
//                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
        if (db.tables.containsKey(name)) {
            throw new RuntimeException("ERROR: table already exists");
        }
        Table newTable = select(exprs, tables, conds, db);
        db.tables.put(name, newTable);

    }

    /** Loads .tbl into database */
    // SOURCE: www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example
    private static void loadTable(String name, Database db) {
//        System.out.printf("You are trying to load the table named %s\n", name);
        String filename = name + ".tbl";
        File file = new File(filename);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine());
                fileContents.append("\n");
            }
            String fileString = fileContents.toString();
            String[] rows = fileString.split("\n");
            String colTitles = rows[0];
            String[] cols = colTitles.split(",");
            Table newTable = new Table(cols);
            for (int i = 1; i < rows.length; i++) {
                String line = rows[i];
                String[] lineItems = line.split(",");
//                for (int j = 0; j < lineItems.length; j++) {
//                    lineItems[j] = lineItems[j].replaceAll("'", "");
//                }
                newTable.addRow(lineItems);
            }
            db.tables.put(name, newTable);
        } catch (Exception e) {
            throw new RuntimeException("ERROR: inappropriate load");
        }
    }

    /** Writes table from database into a .tbl file */
    private static void storeTable(String name, Database db) {
//        System.out.printf("You are trying to store the table named %s\n", name);
        if (!db.tables.containsKey(name)) {
            throw new RuntimeException("ERROR: no such table");
        }
        Table toWrite = db.tables.get(name);
        String filename = name + ".tbl";
        FileWriter fw = null;
        BufferedWriter bw = null;
        String error;

        try {
            fw = new FileWriter(filename);
            bw = new BufferedWriter(fw);
            bw.write(toWrite.toString());
        } catch (IOException e) {
            error = e.getMessage();
            throw new RuntimeException("ERROR: " + error);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                error = e.getMessage();
                throw new RuntimeException("ERROR: " + error);
            }
        }

    }

    /** Removes table from database storage */
    private static void dropTable(String name, Database db) {
//        System.out.printf("You are trying to drop the table named %s\n", name);
        if (!db.tables.containsKey(name)) {
            throw new RuntimeException("ERROR: no such table");
        }
        db.tables.remove(name);
    }

    /** Insert row of given values to table in database */
    private static void insertRow(String expr, Database db) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            throw new RuntimeException("ERROR: malformed insert");
        }

//        System.out.printf("You are trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
        String tableName = m.group(1);
        String values = m.group(2).trim();
        if (!db.tables.containsKey(tableName)) {
            throw new RuntimeException("ERROR: no such table");
        }
        String[] vals = values.split(COMMA);
        db.tables.get(tableName).addRow(vals);
    }

    /** Returns string representation of a table in database */
    private static String printTable(String name, Database db) {
//        System.out.printf("You are trying to print the table named %s\n", name);
        if (!db.tables.containsKey(name)) {
            throw new RuntimeException("ERROR: no such table");
        }
        return db.tables.get(name).toString();
    }

    /** Returns table based on select expressions */
    private static Table select(String expr, Database db) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            throw new RuntimeException("ERROR: malformed select");
        }
        return select(m.group(1), m.group(2), m.group(3), db);
    }

    /** Helper method to build selection criteria */
    private static void selectBuilder (ArrayList<ColExpr> exprList, String op, String expr, Table reference) {
        String[] exprTemp;
        if (op.equals("+")) {
            exprTemp = expr.split(PLUS);
        } else if (op.equals("-")) {
            exprTemp = expr.split(MINUS);
        } else if (op.equals("*")) {
            exprTemp = expr.split(MUL);
        } else {
            exprTemp = expr.split(DIV);
        }
        if (exprTemp.length != 2) {
            throw new RuntimeException("ERROR: malformed select");
        }
        String col1name = exprTemp[0];
        ColumnHeader col1 = ColumnHeader.getHeaderByName(reference, col1name).copy();
        String rest = exprTemp[1];
        if (rest.contains("'") || Utils.isNumeric(rest)) {
            String literal = rest.replaceAll("'", "");
            ColExpr nextExpr = new ColExpr(col1, op, literal);
            exprList.add(nextExpr);
        } else if (rest.contains("as")) {
            String[] col2Alias = rest.split(AS);
            if (col2Alias[0].contains(" ") || col2Alias[1].contains(" ") || col2Alias.length != 2) {
                throw new RuntimeException("ERROR: malformed select");
            }
            ColumnHeader col2 = ColumnHeader.getHeaderByName(reference, col2Alias[0]);
            String alias = col2Alias[1];
            ColExpr nextExpr = new ColExpr(col1, op, col2, alias);
            exprList.add(nextExpr);
        } else {
            throw new RuntimeException("ERROR: malformed select");
        }
    }

    /** Helper method to build filter criteria */
    private static void whereBuilder (ArrayList<WhereExpr> condList, String cmp, String expr, Table reference) {
        String[] exprTemp;
        if (cmp.equals("==")) {
            exprTemp = expr.split(EQUAL);
        } else if (cmp.equals("!=")) {
            exprTemp = expr.split(NOTEQUAL);
        } else if (cmp.equals(">=")) {
            exprTemp = expr.split(GREATEREQ);
        } else if (cmp.equals(">")){
            exprTemp = expr.split(GREATER);
        } else if (cmp.equals("<=")){
            exprTemp = expr.split(LESSEREQ);
        } else {
            exprTemp = expr.split(LESSER);
        }
        if (exprTemp.length != 2) {
            throw new RuntimeException("ERROR: malformed where clause");
        }
        String col1name = exprTemp[0];
        ColumnHeader col1 = ColumnHeader.getHeaderByName(reference, col1name);
        String rest = exprTemp[1];
        if (rest.contains("'") || Utils.isNumeric(rest)) {
            String literal = rest.replaceAll("'", "");
            WhereExpr nextExpr = new WhereExpr(col1, cmp, literal);
            condList.add(nextExpr);
        } else if (!rest.contains(" ")) {
            String col2name = rest;
            ColumnHeader col2 = ColumnHeader.getHeaderByName(reference, col2name);
            WhereExpr nextExpr = new WhereExpr(col1, cmp, col2);
            condList.add(nextExpr);
        } else {
            throw new RuntimeException("ERROR: malformed where clause");
        }
    }

    /** Returns new table based on selection criteria */
    private static Table select(String exprs, String tables, String conds, Database db) {
//        System.out.printf("You are trying to select these expressions:" +
//                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
        String[] tableNames = tables.trim().split(COMMA);
        ArrayList<Table> toJoin = new ArrayList<>();
        for (String tName : tableNames) {
            if (!db.tables.containsKey(tName)) {
                throw new RuntimeException("ERROR: no such table");
            }
            toJoin.add(db.tables.get(tName));
        }

        Table joinedTable = Table.joinSeries(toJoin);
        Table selectedTable;

        if (exprs.equals("*")) {
            selectedTable = joinedTable;
        } else {
            ArrayList<ColExpr> selectCriteria = new ArrayList<>();
            String[] exprArray = exprs.split(COMMA);
            for (String expr : exprArray) {

                // For simple column names
                if (!expr.contains(" ")) {
                    ColumnHeader col1 = ColumnHeader.getHeaderByName(joinedTable, expr).copy();
                    selectCriteria.add(new ColExpr(col1));
                }

                // For operations
                else if (expr.contains("+")) {
                    selectBuilder(selectCriteria, "+", expr, joinedTable);
                } else if (expr.contains("-")) {
                    selectBuilder(selectCriteria, "-", expr, joinedTable);
                } else if (expr.contains("*")) {
                    selectBuilder(selectCriteria, "*", expr, joinedTable);
                } else if (expr.contains("/")) {
                    selectBuilder(selectCriteria, "/", expr, joinedTable);
                } else {
                    throw new RuntimeException("ERROR: malformed select");
                }
            }
            selectedTable = Table.selectFrom(joinedTable, selectCriteria);
        }
        if (conds == null) {
            return selectedTable;
        } else {
            ArrayList<WhereExpr> filterConds = new ArrayList<>();
            String[] condsArray = conds.trim().split(AND);
            for (String cond : condsArray) {
                if (cond.contains("==")) {
                    whereBuilder(filterConds, "==", cond, selectedTable);
                } else if (cond.contains("!=")) {
                    whereBuilder(filterConds, "!=", cond, selectedTable);
                } else if (cond.contains(">=")) {
                    whereBuilder(filterConds, ">=", cond, selectedTable);
                } else if (cond.contains("<=")) {
                    whereBuilder(filterConds, "<=", cond, selectedTable);
                } else if (cond.contains(">")) {
                    whereBuilder(filterConds, ">", cond, selectedTable);
                } else if (cond.contains("<")) {
                    whereBuilder(filterConds, "<", cond, selectedTable);
                } else {
                    throw new RuntimeException("ERROR: malformed where clause");
                }
            }
            Table.filterTable(selectedTable, filterConds);
        }
        return selectedTable;
    }
}
