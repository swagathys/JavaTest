package com.credit.suisse.javaTest.dao;

import com.credit.suisse.javaTest.dao.impl.LogDAOImpl;
import com.credit.suisse.javaTest.model.EventRecord;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

public class LogDAOTests {

    private LogDAO logDAO;

    @Before
    public void setUp() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
        logDAO = new LogDAOImpl(new JdbcTemplate(db));
    }

    private EventRecord getEventRecord(String id, boolean alert, Long duration) {
        EventRecord record1 = new EventRecord();
        record1.setId(id);
        record1.setAlert(alert);
        record1.setHost("host");
        record1.setType("application");
        record1.setTotalTime(duration);
        return record1;
    }

    @Test
    public void testAddAlert() {
        EventRecord record1 = getEventRecord("12345", true, 6L);
        EventRecord record2 = getEventRecord("54321", true, 7L);
        boolean alert = logDAO.addAlert(Arrays.asList(record1, record2));
        Assert.assertTrue(alert);
    }

    @Test
    public void testGetAlerts() {
        List<EventRecord> allLogs = logDAO.getAllLogs();
        Assert.assertEquals(0, allLogs.size());
        EventRecord record1 = getEventRecord("12345", true, 6L);
        EventRecord record2 = getEventRecord("54321", true, 7L);
        boolean alert = logDAO.addAlert(Arrays.asList(record1, record2));
        Assert.assertTrue(alert);
        allLogs = logDAO.getAllLogs();
        Assert.assertEquals(2, allLogs.size());
    }
}
