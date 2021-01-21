package com.lezurex.whatsweb.server.database.objects;

public class Key {

    private final String row;
    private final String keyWord;

    public Key(String row, String keyWord) {
        this.row = row;
        this.keyWord = keyWord;
    }

    public String getRow() {
        return row;
    }

    public String getKeyWord() {
        return keyWord;
    }

}
