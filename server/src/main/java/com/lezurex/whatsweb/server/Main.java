package com.lezurex.whatsweb.server;

import com.lezurex.whatsweb.server.utils.Database;
import com.lezurex.whatsweb.server.utils.DatabaseAdapter;
import com.lezurex.whatsweb.server.utils.Row;
import com.lezurex.whatsweb.server.utils.RowType;

import java.io.IOException;

public class Main {

    public static Database database;

    public static void main(String[] args) throws IOException {
        initDatabase();
        Server server = new Server(2121);
        server.start();
    }

    public static void initDatabase() {
        database = new Database("localhost", "root", "", "whatsweb", 3306);
        database.connect();

        DatabaseAdapter db = new DatabaseAdapter(database);

        db.createTable("users",
                new Row("uuid", RowType.VARCHAR),
                new Row("username", RowType.VARCHAR),
                new Row("password", RowType.VARCHAR),
                new Row("email", RowType.VARCHAR),
                new Row("friends", RowType.LONGTEXT),
                new Row("groups", RowType.LONGTEXT),
                new Row("lastSeen", RowType.DOUBLE));

        db.createTable("groups",
                new Row("uuid", RowType.VARCHAR),
                new Row("members", RowType.MEDIUMTEXT),
                new Row("history", RowType.LONGTEXT));
    }
}
