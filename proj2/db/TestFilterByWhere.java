package db;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by XWEN on 3/1/2017.
 */
public class TestFilterByWhere {

    @Test
    public void testTeamsTable() {
        String[] teamTableTitles = {"TeamName string", "City string",
                "Sport string", "YearEstablished int", "Mascot string",
                "Stadium string"};
        Table teamExample = new Table(teamTableTitles);
        teamExample.addRow(new String[]{"Mets", "New York", "MLB Baseball", "1962", "Mr. Met", "Citi Field"});
        teamExample.addRow(new String[]{"Steelers", "Pittsburgh", "NFL Football", "1933", "Steely McBeam", "Heinz Field"});
        teamExample.addRow(new String[]{"Patriots", "New England", "NFL Football", "1960", "Pat Patriot", "Gillette Stadium"});
        teamExample.addRow(new String[]{"Cloud9", "Los Angeles", "eSports", "2012", "NOVALUE", "NOVALUE"});
        teamExample.addRow(new String[]{"EnVyUs", "Charlotte", "eSports", "2007", "NOVALUE", "NOVALUE"});
        teamExample.addRow(new String[]{"Golden Bears", "Berkeley", "NCAA Football", "1886", "Oski", "Memorial Stadium"});

        ColumnHeader m = ColumnHeader.getHeaderByName(teamExample, "Mascot").copy();
        ColumnHeader yE = ColumnHeader.getHeaderByName(teamExample, "YearEstablished").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(m));
        selectExprs.add(new ColExpr(yE));
        ArrayList<WhereExpr> conds = new ArrayList<>();
        conds.add(new WhereExpr(yE, ">", "1942"));
        Table t2 = Table.selectFrom(teamExample, selectExprs);
        Table.filterTable(t2, conds);

        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add("Mr. Met");
        expR0.add(1962);
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add("Pat Patriot");
        expR1.add(1960);
        ArrayList<Object> expR2 = new ArrayList<>();
        expR2.add(SpecialValues.NOVALUE);
        expR2.add(2012);
        ArrayList<Object> expR3 = new ArrayList<>();
        expR3.add(SpecialValues.NOVALUE);
        expR3.add(2007);

        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(expR2, t2.rowStore.get(2));
        assertEquals(expR3, t2.rowStore.get(3));

        assertTrue(expR0.get(1) instanceof Integer);
        for (int i = 0; i < t2.rows; i++) {
            assertTrue(t2.rowStore.get(i).get(1) instanceof Integer);
        }

        assertEquals(2, t2.cols);
        assertEquals(4, t2.rows);
    }

    @Test
    public void testRecordsTable() {
        String[] teamTableTitles = {"TeamName string", "City string",
                "Sport string", "YearEstablished int", "Mascot string",
                "Stadium string"};
        Table teamExample = new Table(teamTableTitles);
        teamExample.addRow(new String[]{"Mets", "New York", "MLB Baseball", "1962", "Mr. Met", "Citi Field"});
        teamExample.addRow(new String[]{"Steelers", "Pittsburgh", "NFL Football", "1933", "Steely McBeam", "Heinz Field"});
        teamExample.addRow(new String[]{"Patriots", "New England", "NFL Football", "1960", "Pat Patriot", "Gillette Stadium"});
        teamExample.addRow(new String[]{"Cloud9", "Los Angeles", "eSports", "2012", "NOVALUE", "NOVALUE"});
        teamExample.addRow(new String[]{"EnVyUs", "Charlotte", "eSports", "2007", "NOVALUE", "NOVALUE"});
        teamExample.addRow(new String[]{"Golden Bears", "Berkeley", "NCAA Football", "1886", "Oski", "Memorial Stadium"});

        String[] recordsTitles = {"TeamName string", "Season int", "Wins int", "Losses int", "Ties int"};
        Table records = new Table(recordsTitles);
        records.addRow(new String[]{"Golden Bears", "2016", "5", "7", "0"});
        records.addRow(new String[]{"Golden Bears", "2015", "8", "5", "0"});
        records.addRow(new String[]{"Golden Bears", "2014", "5", "7", "0"});
        records.addRow(new String[]{"Steelers", "2015", "10", "6", "0"});
        records.addRow(new String[]{"Steelers", "2014", "11", "5", "0"});
        records.addRow(new String[]{"Steelers", "2013", "8", "8", "0"});
        records.addRow(new String[]{"Mets", "2015", "90", "72", "0"});
        records.addRow(new String[]{"Mets", "2014", "79", "83", "0"});
        records.addRow(new String[]{"Mets", "2013", "74", "88", "0"});
        records.addRow(new String[]{"Patriots", "2015", "12", "4", "0"});
        records.addRow(new String[]{"Patriots", "2014", "12", "4", "0"});
        records.addRow(new String[]{"Patriots", "2013", "12", "4", "0"});

        Table t2 = Table.join(teamExample, records);

        assertEquals(10, t2.cols);
        ColumnHeader city = ColumnHeader.getHeaderByName(t2, "City").copy();
        ColumnHeader season = ColumnHeader.getHeaderByName(t2, "Season").copy();
        ColumnHeader wins = ColumnHeader.getHeaderByName(t2, "Wins").copy();
        ColumnHeader losses = ColumnHeader.getHeaderByName(t2, "Losses").copy();
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(new ColExpr(city));
        selectExprs.add(new ColExpr(season));
        selectExprs.add(new ColExpr(wins, "/", losses, "Ratio"));
        Table t3 = Table.selectFrom(t2, selectExprs);

        for (int i = 0; i < t3.rows; i++) {
            assertTrue(t3.rowStore.get(i).get(0) instanceof String);
            assertTrue(t3.rowStore.get(i).get(1) instanceof Integer);
            assertTrue(t3.rowStore.get(i).get(2) instanceof Integer);
        }
        assertEquals(3, t3.cols);
        assertEquals(12, t3.rows);

        ColumnHeader city2 = ColumnHeader.getHeaderByName(t3, "City").copy();
        ColumnHeader season2 = ColumnHeader.getHeaderByName(t3, "Season").copy();
        ColumnHeader ratio = ColumnHeader.getHeaderByName(t3, "Ratio").copy();
        ArrayList<ColExpr> select3 = new ArrayList<>();
        select3.add(new ColExpr(city2));
        select3.add(new ColExpr(season2));
        select3.add(new ColExpr(ratio));
        Table t4 = Table.selectFrom(t3, select3);
        ColumnHeader ratio2 = ColumnHeader.getHeaderByName(t4, "Ratio").copy();
        WhereExpr underOne = new WhereExpr(ratio2, "<", "1");
        ArrayList<WhereExpr> wheres = new ArrayList<>();
        wheres.add(underOne);
        Table.filterTable(t4, wheres);

        for (int i = 0; i < t4.rows; i++) {
            assertTrue(t4.rowStore.get(i).get(0) instanceof String);
            assertTrue(t4.rowStore.get(i).get(1) instanceof Integer);
            assertTrue(t4.rowStore.get(i).get(2) instanceof Integer);
        }
        assertEquals(3, t4.cols);
        assertEquals(4, t4.rows);
        System.out.println(t4);
    }

    @Test
    public void testFansTable() {
        String[] titles1 = {"Lastname string", "Firstname string", "TeamName string"};
        Table t1 = new Table(titles1);
        t1.addRow(new String[]{"Lee", "Maurice", "Mets"});
        t1.addRow(new String[]{"Lee", "Maurice", "Steelers"});
        t1.addRow(new String[]{"Ray", "Mitas", "Patriots"});
        t1.addRow(new String[]{"Hwang", "Alex", "Cloud9"});
        t1.addRow(new String[]{"Rulison", "Jared", "EnVyUs"});
        t1.addRow(new String[]{"Fang", "Vivian", "Golden Bears"});
        ColumnHeader fname = ColumnHeader.getHeaderByName(t1, "Firstname");
        ColumnHeader lname = ColumnHeader.getHeaderByName(t1, "Lastname");
        ColumnHeader tname = ColumnHeader.getHeaderByName(t1, "TeamName");
        ColExpr fnExpr = new ColExpr(fname);
        ColExpr lnExpr = new ColExpr(lname);
        ColExpr tnExpr = new ColExpr(tname);
        ArrayList<ColExpr> selectExprs = new ArrayList<>();
        selectExprs.add(fnExpr);
        selectExprs.add(lnExpr);
        selectExprs.add(tnExpr);
        Table t2 = Table.selectFrom(t1, selectExprs);
        WhereExpr afterLee = new WhereExpr(lname, ">=", "Lee");
        ArrayList<WhereExpr> conds = new ArrayList<>();
        conds.add(afterLee);
        Table.filterTable(t2, conds);
        ArrayList<Object> expR0 = new ArrayList<>();
        expR0.add("Maurice");
        expR0.add("Lee");
        expR0.add("Mets");
        ArrayList<Object> expR1 = new ArrayList<>();
        expR1.add("Maurice");
        expR1.add("Lee");
        expR1.add("Steelers");
        ArrayList<Object> expR2 = new ArrayList<>();
        expR2.add("Mitas");
        expR2.add("Ray");
        expR2.add("Patriots");
        ArrayList<Object> expR3 = new ArrayList<>();
        expR3.add("Jared");
        expR3.add("Rulison");
        expR3.add("EnVyUs");
        assertEquals(expR0, t2.rowStore.get(0));
        assertEquals(expR1, t2.rowStore.get(1));
        assertEquals(expR2, t2.rowStore.get(2));
        assertEquals(expR3, t2.rowStore.get(3));
        assertEquals(3, t2.cols);
        assertEquals(4, t2.rows);
    }
}
