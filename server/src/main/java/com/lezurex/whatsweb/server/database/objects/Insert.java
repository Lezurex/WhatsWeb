package com.lezurex.whatsweb.server.database.objects;

public class Insert {

    private final String row;
    private final String value;

    public Insert(String row, String value) {
        this.row = row;
        this.value = value;
    }

    public String getRow() {
        return row;
    }

    public String getValue() {
        return value;
    }

}
