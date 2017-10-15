package com.matejko.repositories;

import com.matejko.model.common.Status;
import com.matejko.model.entity.Url;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
@RequiredArgsConstructor
public class UrlRepositoryImpl implements UrlRepositoryCustom {

  private static final String ACTIVE_AND_FREE = "select u " +
      "from Url u " +
      "where u.active = true " +
      "and u.status = :status " +
      "order by u.description asc";
  private static final String STUCK = "select u " +
      "from Url u " +
      "where u.active = true " +
      "and u.status = :status " +
      "and u.modificationDate <= :maxDate";
  private final EntityManager em;
  @Value("${settings.URLS_PER_INVOCATION}")
  private int URLS_PER_INVOCATION;
  @Value("${settings.MAX_MINUTES_OF_PROCESSING}")
  private int MAX_MINUTES_OF_PROCESSING;

  @Override
  public Seq<Url> findAllByActiveTrueAndFree() {
    return List.ofAll(em.createQuery(ACTIVE_AND_FREE, Url.class)
        .setParameter("status", Status.FREE).getResultList())
        .filter(res -> res.getStatistics().stream()
            .noneMatch(stat -> stat.getExecutionDate().equals(LocalDate.now())))
        .take(URLS_PER_INVOCATION);
  }

  @Override
  public Seq<Url> findAllStuck() {
    return List.ofAll(em.createQuery(STUCK, Url.class)
        .setParameter("status", Status.PROCESSING)
        .setParameter("maxDate", LocalDateTime.now().minusMinutes(MAX_MINUTES_OF_PROCESSING))
        .getResultList());
  }
}
