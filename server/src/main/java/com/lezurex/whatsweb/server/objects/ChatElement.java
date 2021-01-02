package com.lezurex.whatsweb.server.objects;

import lombok.Getter;

@Getter
public class ChatElement {

    private User author;
    private String content;
    private double timestamp;

}
