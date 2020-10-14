package com.credit.suisse.javaTest.service;

import com.credit.suisse.javaTest.JavaTestApplication;
import com.credit.suisse.javaTest.dao.LogDAO;
import com.credit.suisse.javaTest.dao.impl.LogDAOImpl;
import com.credit.suisse.javaTest.model.EventRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EventParserTests {

    private LogDAO logDAO;

    private EventParser parser;

    @Before
    public void setup() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
        logDAO = new LogDAOImpl(new JdbcTemplate(db));
        parser = new EventParser(logDAO);
    }

    @Test
    public void testParse() throws IOException {
        File file = new File(EventParserTests.class.getClassLoader().getResource("logfile.txt").getFile());
        parser.parse(file);
        List<EventRecord> allLogs = logDAO.getAllLogs();
        Assert.assertEquals(2, allLogs.size());
    }
}
