package com.matejko.repositories;

import com.matejko.model.entity.Url;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

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
    final Query query = em.createQuery("select u " +
        "from Url u " +
        "outer join u.statistics s " +
        "where s.executionDate < :today");
    query.setParameter("today", new Date(), TemporalType.DATE);

    return query.getResultList();
  }
}
