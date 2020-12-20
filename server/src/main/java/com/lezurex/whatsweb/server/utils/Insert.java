package com.lezurex.whatsweb.server.utils;

public class Insert {

    private String row;
    private String value;

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
