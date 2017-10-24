package db;

import java.util.HashMap;

public class Database {
    HashMap<String, Table> tables;

    public Database() {
        tables = new HashMap<>();
    }

    public String transact(String query) {
        return QueryParse.eval(query, this);

    }
}
