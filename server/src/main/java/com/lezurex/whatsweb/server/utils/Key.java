package com.lezurex.whatsweb.server.utils;

public class Key {

    private String row;
    private String keyWord;

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
