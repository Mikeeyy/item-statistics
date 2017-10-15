package com.matejko.repositories;

import com.matejko.model.entity.SchedulerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mikołaj Matejko on 15.10.2017 as part of item-statistics
 */
public interface SchedulerHistoryRepository extends JpaRepository<SchedulerHistory, Long> {
}
