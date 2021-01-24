package com.lezurex.whatsweb.server;

import com.lezurex.whatsweb.server.database.Database;
import com.lezurex.whatsweb.server.database.DatabaseAdapter;
import com.lezurex.whatsweb.server.database.objects.Row;
import com.lezurex.whatsweb.server.database.enums.RowType;
import java.io.IOException;

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
                new Row("admin", RowType.VARCHAR),
                new Row("name", RowType.VARCHAR));

        databaseAdapter.createTable("chats",
                new Row("uuid", RowType.VARCHAR),
                new Row("history", RowType.LONGTEXT));

    }
}
