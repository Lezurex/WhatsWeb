package com.lezurex.whatsweb.server.database.objects;

import com.lezurex.whatsweb.server.database.enums.RowType;

public class Row {

    private String name;
    private RowType type;

    public Row(String name, RowType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public RowType getType() {
        return type;
    }

}
