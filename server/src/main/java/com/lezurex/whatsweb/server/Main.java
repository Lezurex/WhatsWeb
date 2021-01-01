package com.lezurex.whatsweb.server;

import com.lezurex.whatsweb.server.utils.Database;
import com.lezurex.whatsweb.server.utils.DatabaseAdapter;
import com.lezurex.whatsweb.server.utils.Row;
import com.lezurex.whatsweb.server.utils.RowType;

import java.io.IOException;
import java.util.UUID;

public class Main {

    public static Database database;
    public static DatabaseAdapter databaseAdapter;

    public static void main(String[] args) throws IOException {
        initDatabase();
        Server server = new Server(2121);
        server.start();
    }

    public static void initDatabase() {
        System.out.println("Connecting to database...");
        database = new Database("localhost", "root", "", "whatsweb", 3306);
        database.connect();

        databaseAdapter = new DatabaseAdapter(database);

        System.out.println("Initializing tables...");

        databaseAdapter.createTable("users",
                new Row("uuid", RowType.VARCHAR),
                new Row("username", RowType.VARCHAR),
                new Row("password", RowType.VARCHAR),
                new Row("email", RowType.VARCHAR),
                new Row("friends", RowType.LONGTEXT),
                new Row("groups", RowType.LONGTEXT),
                new Row("lastSeen", RowType.DOUBLE),
                new Row("token", RowType.VARCHAR));

        databaseAdapter.createTable("groups",
                new Row("uuid", RowType.VARCHAR),
                new Row("members", RowType.MEDIUMTEXT),
                new Row("history", RowType.LONGTEXT));
    }
}
