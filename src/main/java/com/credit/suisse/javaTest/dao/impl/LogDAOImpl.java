package com.credit.suisse.javaTest.dao.impl;

import com.credit.suisse.javaTest.dao.LogDAO;
import com.credit.suisse.javaTest.model.EventRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LogDAOImpl implements LogDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LogDAOImpl (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addAlert(List<EventRecord> eventRecords) {
        try {
            jdbcTemplate.batchUpdate("INSERT into LOG (id, duration, type, host, alert) values (?,?,?,?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            EventRecord record = eventRecords.get(i);
                            preparedStatement.setString(1, record.getId());
                            preparedStatement.setString(2, String.valueOf(record.getTotalTime()));
                            preparedStatement.setString(3, record.getType());
                            preparedStatement.setString(4, record.getHost());
                            preparedStatement.setString(5, record.isAlert() ? "true" : "false");
                        }

                        @Override
                        public int getBatchSize() {
                            return eventRecords.size();
                        }
                    });
            return true;
        } catch (DataAccessException ex) {
            log.error("DataAccessException :", ex.getLocalizedMessage());
            return false;
        } catch (Exception ex) {
            log.error("Exception Occured:", ex.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public List<EventRecord> getAllLogs() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM LOG");
        List<EventRecord> eventRecords = new ArrayList<>(rows.size());
        for (Map row : rows) {
            EventRecord record = new EventRecord();
            record.setId((String) row.get("id"));
            record.setTotalTime(Long.valueOf((String) row.get("duration")));
            record.setType(String.valueOf(row.get("type")));
            record.setHost(String.valueOf(row.get("host")));
            record.setAlert(row.get("alert").equals("true") ? true : false);
            eventRecords.add(record);
        }
        return eventRecords;
    }
}
