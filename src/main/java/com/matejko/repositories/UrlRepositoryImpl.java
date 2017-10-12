package com.matejko.repositories;

import com.matejko.model.entity.Url;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
public class UrlRepositoryImpl implements UrlRepositoryCustom {

  private final EntityManager em;

  @Inject
  public UrlRepositoryImpl(final EntityManager em) {
    this.em = em;
  }

  @Override
  public List<Url> findAllByActiveTrueAndNotCheckedToday() {
    return em.createQuery("select u from Url u where u.active = true", Url.class)
        .getResultList()
        .stream().filter(res -> res.getStatistics().stream()
            .noneMatch(stat -> stat.getExecutionDate().equals(LocalDate.now())))
        .collect(Collectors.toList());
  }
}
