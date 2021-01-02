package com.lezurex.whatsweb.server.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatElement {

    private User author;
    private String content;
    private double timestamp;
    private UUID uuid;



}
