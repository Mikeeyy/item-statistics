package com.matejko.model.entity;

import com.matejko.model.common.SchedulerResult;
import com.matejko.model.common.SchedulerType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by Mikołaj Matejko on 15.10.2017 as part of item-statistics
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SchedulerHistory extends BaseEntity {
  @Column(nullable = false)
  private LocalDateTime callTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SchedulerType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SchedulerResult result;

  @Lob
  private String stacktrace;
}
