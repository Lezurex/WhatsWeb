package com.lezurex.whatsweb.server.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String host;
    private final String user;
    private final String password;
    private final int port;
    private final String database;
    private Connection connection;

    public Database(String host, String username, String password, String database, int port) {
        this.host = host;
        this.user = username;
        this.password = password;
        this.port = port;
        this.database = database;
    }

    /**
     * Connects to the database
     */
    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
            System.out.println("Successfully connected to the database");
        } catch (SQLException exception) {
            System.out.println("An error occurred while connecting to the database");
            exception.printStackTrace();
        }
    }

    /**
     * Disconnects from the database
     */
    public void disconnect() {
        if(isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return Returns if the connection has been established
     */
    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }
}
