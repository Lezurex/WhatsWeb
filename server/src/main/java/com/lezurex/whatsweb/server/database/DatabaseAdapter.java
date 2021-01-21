package com.lezurex.whatsweb.server.database;

import com.google.common.collect.Lists;
import com.lezurex.whatsweb.server.database.objects.Insert;
import com.lezurex.whatsweb.server.database.objects.Key;
import com.lezurex.whatsweb.server.database.objects.Row;
import com.lezurex.whatsweb.server.database.enums.RowType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseAdapter {

    private final Database database;

    public DatabaseAdapter(Database database) {
        this.database = database;
    }

    /**
     * Create table in database
     *
     * @param name Name of table
     * @param row  Requires a row array
     */
    public void createTable(String name, Row... row) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS " + name + " (");

        int length = row.length;

        for (Row rows : row) {
            stringBuilder.append(rows.getName() + " ");
            if (length == 1) {
                if (rows.getType() == RowType.VARCHAR) {
                    stringBuilder.append("varchar(250)");
                }
                if (rows.getType() == RowType.INTEGER) {
                    stringBuilder.append("bigint(250)");
                }
                if (rows.getType() == RowType.TEXT) {
                    stringBuilder.append("text");
                }
                if (rows.getType() == RowType.MEDIUMTEXT) {
                    stringBuilder.append("mediumtext");
                }
                if (rows.getType() == RowType.LONGTEXT) {
                    stringBuilder.append("longtext");
                }
                if (rows.getType() == RowType.DOUBLE) {
                    stringBuilder.append("double");
                }
            } else {
                if (rows.getType() == RowType.VARCHAR) {
                    stringBuilder.append("varchar(250), ");
                }
                if (rows.getType() == RowType.INTEGER) {
                    stringBuilder.append("bigint(250), ");
                }
                if (rows.getType() == RowType.TEXT) {
                    stringBuilder.append("text, ");
                }
                if (rows.getType() == RowType.MEDIUMTEXT) {
                    stringBuilder.append("mediumtext, ");
                }
                if (rows.getType() == RowType.LONGTEXT) {
                    stringBuilder.append("longtext, ");
                }
                if (rows.getType() == RowType.DOUBLE) {
                    stringBuilder.append("double, ");
                }
                length--;
            }
        }
        stringBuilder.append(");");
        executeCommand(stringBuilder.toString());
    }

    /**
     * Sends a command to the database
     *
     * @param query Requires a query String
     */
    public void executeCommand(String query) {
        try {
            database.getConnection().prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a string from a table
     *
     * @param tablename Requires a table name
     * @param row       Requires the row of target string
     * @param key       (Optional) key
     * @return Returns the string from the table
     */
    public String getStringFromTable(String tablename, String row, Key... key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT " + row + " FROM " + tablename + " ");
        int length = key.length;
        if (length != 0) {
            for (Key keys : key) {
                if (length == 1) {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "'");
                } else {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "' AND ");
                    length--;
                }
            }
        }
        try {
            ResultSet resultSet = database.getConnection().prepareStatement(stringBuilder.toString()).executeQuery();
            while (resultSet.next()) {
                return resultSet.getString(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a stringlist from a table
     *
     * @param tablename Requires a table name
     * @param row       Requires the row of target stringlist
     * @param key       (Optional) key
     * @return Returns the stringlist from the table
     */
    public List<String> getStringListFromTable(String tablename, String row, Key... key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT " + row + " FROM " + tablename + " ");
        int length = key.length;
        if (length != 0) {
            for (Key keys : key) {
                if (length == 1) {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "'");
                } else {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "' AND ");
                    length--;
                }
            }
        }
        try {
            ResultSet resultSet = database.getConnection().prepareStatement(stringBuilder.toString()).executeQuery();
            List<String> results = Lists.newCopyOnWriteArrayList();
            while (resultSet.next()) {
                results.add(resultSet.getString(row));
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets an integer from a table
     *
     * @param tablename Requires a table name
     * @param row       Requires the row of target integer
     * @param key       (Optional) key
     * @return Returns the integer from the table
     */
    public int getIntegerFromTable(String tablename, String row, Key... key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT " + row + " FROM " + tablename + " ");
        int length = key.length;
        if (length != 0) {
            for (Key keys : key) {
                if (length == 1) {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "'");
                } else {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "' AND ");
                    length--;
                }
            }
        }
        try {
            ResultSet resultSet = database.getConnection().prepareStatement(stringBuilder.toString()).executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets a ResultSet
     *
     * @param tablename Requires a tablename
     * @param key       (Optional)key
     * @return Returns the ResultSet from the database
     */
    public ResultSet getResultSet(String tablename, Key... key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM " + tablename + " ");
        int length = key.length;
        if (length != 0) {
            for (Key keys : key) {
                if (length == 1) {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "'");
                } else {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "' AND ");
                    length--;
                }
            }
        }
        try {
            return database.getConnection().prepareStatement(stringBuilder.toString()).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a ResultSet from the database
     *
     * @param query Requires a query String
     * @return Returns the ResultSet from the database
     */
    public ResultSet getResultSet(String query) {
        try {
            return database.getConnection().prepareStatement(query).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts one or more things into the database
     *
     * @param tablename Requires a tablename
     * @param insert    Requires one or more Insert objects
     */
    public void insertIntoTable(String tablename, Insert... insert) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO " + tablename + " (");

        int length = insert.length;

        for (Insert inserts : insert) {
            if (length != 1) {
                stringBuilder.append(inserts.getRow() + ", ");
                length--;
            } else {
                stringBuilder.append(inserts.getRow());
            }
        }

        stringBuilder.append(") VALUES (");
        length = insert.length;

        for (Insert inserts : insert) {
            if (length != 1) {
                stringBuilder.append("'" + inserts.getValue() + "', ");
                length--;
            } else {
                stringBuilder.append("'" + inserts.getValue() + "'");
            }
        }
        stringBuilder.append(");");
        executeCommand(stringBuilder.toString());
    }

    /**
     * Updates a value in a table
     *
     * @param tablename Requires a tablename
     * @param row       Requires the name of the row which should be updated
     * @param newValue  Requires the new value
     * @param key       (Optional) key(s)
     */
    public void updateValue(String tablename, String row, String newValue, Key... key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE " + tablename + " SET " + row + " = '" + newValue + "' ");

        int length = key.length;
        if (length != 0) {
            for (Key keys : key) {
                if (length == 1) {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "'");
                } else {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "' AND ");
                    length--;
                }
            }
        }
        executeCommand(stringBuilder.toString());
    }

    /**
     * Updates one or more values in a table
     *
     * @param tablename Requires the tablename
     * @param key       Requires a key
     * @param insert    Requires the new values as Insert objects
     */
    public void updateValues(String tablename, Key key, Insert... insert) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE " + tablename + " SET ");

        int length = insert.length;
        if (length == 0) return;
        for (Insert inserts : insert) {
            if (length == 1) {
                stringBuilder.append(inserts.getRow());
                stringBuilder.append(" = '");
                stringBuilder.append(inserts.getValue());
                stringBuilder.append("' ");
            } else {
                stringBuilder.append(inserts.getRow());
                stringBuilder.append(" = '");
                stringBuilder.append(inserts.getValue());
                stringBuilder.append("', ");
                length--;
            }
        }
        stringBuilder.append(" WHERE ");
        stringBuilder.append(key.getRow() + " = '");
        stringBuilder.append(key.getKeyWord() + "'");

        executeCommand(stringBuilder.toString());
    }

    /**
     * If no key is provided, the method checks if the table is empty
     *
     * @param tablename Requires the tablename
     * @param key       (Optional) key(s)
     * @return Returns if the table does contain an entry
     */
    public boolean containsEntry(String tablename, Key... key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM " + tablename + " ");
        int length = key.length;
        if (length != 0) {
            for (Key keys : key) {
                if (length == 1) {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "'");
                } else {
                    stringBuilder.append("WHERE " + keys.getRow() + " = '" + keys.getKeyWord() + "' AND ");
                    length--;
                }
            }
        }
        try {
            return database.getConnection().prepareStatement(stringBuilder.toString()).executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes an entry from a table
     *
     * @param tablename Requires the tablename
     * @param key       (Optional) key(s)
     */
    public void deleteFromTable(String tablename, Key... key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM ");
        stringBuilder.append(tablename + " WHERE");

        if (key.length != 0) {
            int length = key.length;
            for (Key keys : key) {
                if (length == 1) {
                    stringBuilder.append(" " + keys.getRow() + " = '" + keys.getKeyWord() + "'");
                } else {
                    stringBuilder.append(" " + keys.getRow() + " = '" + keys.getKeyWord() + "' AND ");
                    length--;
                }
            }
        }
        executeCommand(stringBuilder.toString());
    }

}