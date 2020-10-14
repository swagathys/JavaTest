package com.credit.suisse.javaTest.controller;

import com.credit.suisse.javaTest.dao.LogDAO;
import com.credit.suisse.javaTest.model.EventRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class LogController {

    private LogDAO logDAO;

    public LogController(LogDAO logDAO) {
        this.logDAO = logDAO;
    }

    @GetMapping("/rest/api/logs")
    public ResponseEntity<List<EventRecord>> getEvents() {
        log.info("getAllLogs() invoked");
        return new ResponseEntity<>(logDAO.getAllLogs(), HttpStatus.OK);
    }
}
