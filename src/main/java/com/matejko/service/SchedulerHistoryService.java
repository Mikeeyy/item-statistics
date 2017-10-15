package com.matejko.service;

import com.matejko.model.common.SchedulerResult;
import com.matejko.model.common.SchedulerType;
import com.matejko.model.entity.SchedulerHistory;
import com.matejko.repositories.SchedulerHistoryRepository;
import java.time.LocalDateTime;
import javax.inject.Named;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Created by Mikołaj Matejko on 15.10.2017 as part of item-statistics
 */
@Named
@RequiredArgsConstructor
@Transactional
public class SchedulerHistoryService {
  private final SchedulerHistoryRepository repository;

  public void saveHistory(LocalDateTime callTime, SchedulerType type, SchedulerResult result, Throwable exception) {
    SchedulerHistory history = new SchedulerHistory();
    history.setCreationDate(LocalDateTime.now());
    history.setCallTime(callTime);
    history.setType(type);
    history.setResult(result);

    if (SchedulerResult.ERROR == result) {
      history.setStacktrace(exception.getMessage());
    }

    repository.save(history);
  }
}
