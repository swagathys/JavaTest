package com.credit.suisse.javaTest.service;

import com.credit.suisse.javaTest.dao.LogDAO;
import com.credit.suisse.javaTest.model.Event;
import com.credit.suisse.javaTest.model.EventRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.credit.suisse.javaTest.model.EventState.STARTED;

@Slf4j
@Component
public class EventParser {

    private LogDAO logDAO;

    @Autowired
    public EventParser(LogDAO logDAO) {
        this.logDAO = logDAO;
    }

    public void parse(File file) throws IOException {
        log.info("Starting to parse");
        Scanner sc = new Scanner(file);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, EventRecord> recordMap = new HashMap<>();
        generateEventMap(sc, mapper, recordMap);
        //filter by alert and then add it to table
        logDAO.addAlert(recordMap.values().stream().filter(r -> r.isAlert()).collect(Collectors.toList()));
    }

    private void generateEventMap(Scanner sc, ObjectMapper mapper, Map<String, EventRecord> recordMap) throws IOException {
        while (sc.hasNextLine()) {
            Event event = mapper.readValue(sc.nextLine(), Event.class);
            EventRecord record = recordMap.get(event.getId());
            if (record == null) {
                record = new EventRecord();
                record.setId(event.getId());
                recordMap.put(event.getId(), record);
            }
            if (STARTED.equals(event.getState())) {
                record.setStartTime(Long.valueOf((event.getTimestamp())));
            } else {
                record.setEndTime(Long.valueOf((event.getTimestamp())));
            }
            if (record.getStartTime() != null && record.getEndTime() != null) {
                record.setTotalTime(record.getEndTime() - record.getStartTime());
                record.setAlert(record.getTotalTime() > 4);
            }
            record.setHost(event.getHost());
            record.setType(event.getType());
        }
    }
}
