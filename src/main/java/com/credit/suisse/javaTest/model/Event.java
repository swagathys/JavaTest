package com.credit.suisse.javaTest.model;

import lombok.Data;

@Data
public class Event {

    private String id;
    private EventState state;
    private String type;
    private String host;
    private String timestamp;
}
