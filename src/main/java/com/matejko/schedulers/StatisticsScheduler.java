package com.matejko.schedulers;

import com.matejko.model.common.SchedulerResult;
import com.matejko.model.common.SchedulerType;
import com.matejko.service.SchedulerHistoryService;
import com.matejko.service.impl.StatisticsGatherer;
import com.matejko.service.impl.StuckUrlsService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Mikołaj Matejko on 14.10.2017 as part of item-statistics
 */
@Component
@RequiredArgsConstructor
public class StatisticsScheduler {

  private static final Logger logger = LoggerFactory.getLogger(StatisticsScheduler.class);


  private final StatisticsGatherer statisticsGatherer;
  private final StuckUrlsService stuckUrlsService;
  private final SchedulerHistoryService schedulerHistoryService;

  @Scheduled(fixedRate = 60 * 1000)
  public void reportCurrentTime() {
    try {
      statisticsGatherer.gatherData();
      handeSuccess(SchedulerType.REPORT_CURRENT_TIME);
    } catch (Throwable e) {
      handleException(e, SchedulerType.REPORT_CURRENT_TIME);
    }
  }

  @Scheduled(fixedRate = 60 * 1000)
  public void freeStuckUrls() {
    try {
      stuckUrlsService.freeStuckUrls();
      handeSuccess(SchedulerType.FREE_STUCK_URLS);
    } catch (Throwable e) {
      handleException(e, SchedulerType.FREE_STUCK_URLS);
    }
  }

  private void handeSuccess(final SchedulerType type) {
    logger.debug(String.format("Successfully executed scheduler [%s]", type.toString()));

    schedulerHistoryService.saveHistory(LocalDateTime.now(), type, SchedulerResult.SUCCESS, null);
  }

  private void handleException(final Throwable e, SchedulerType type) {
    logger.error(String.format("Error occured at scheduler [%s]", type.toString()), e);

    schedulerHistoryService.saveHistory(LocalDateTime.now(), type, SchedulerResult.ERROR, e);
  }
}
