package com.credit.suisse.javaTest.dao;

import com.credit.suisse.javaTest.model.EventRecord;
import java.util.List;

public interface LogDAO {

    boolean addAlert(List<EventRecord> eventRecords);

    List<EventRecord> getAllLogs();
}
