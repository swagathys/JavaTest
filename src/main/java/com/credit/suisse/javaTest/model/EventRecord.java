package com.credit.suisse.javaTest.model;

import lombok.Data;

@Data
public class EventRecord {

    private String id;
    private Long startTime;
    private Long endTime;
    private Long totalTime;
    private String type;
    private String host;
    private boolean alert;
}
