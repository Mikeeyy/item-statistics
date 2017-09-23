package com.matejko.service.impl;

import com.matejko.exceptions.Exceptions;
import com.matejko.mappers.StatisticsMapper;
import com.matejko.mappers.UrlMapper;
import com.matejko.model.entity.Url;
import com.matejko.model.generated.Offer;
import com.matejko.repositories.UrlRepository;
import com.matejko.utils.CollectionUtils;
import com.matejko.utils.StatisticsHelper;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Mikołaj Matejko on 20.09.2017 as part of item-statistics
 */
@Named
public class StatisticsGatherer {

  private final UrlRepository urlRepository;
  private final OffersCollectorService offersCollectorService;

  private final UrlMapper urlMapper;
  private final StatisticsMapper statisticsMapper;

  /**
   * constructor
   *
   * @param urlRepository          url repository
   * @param offersCollectorService offers collector service
   * @param urlMapper              url mapper
   * @param statisticsMapper       statistics mapper
   */
  @Inject
  public StatisticsGatherer(final UrlRepository urlRepository,
                            final OffersCollectorService offersCollectorService,
                            final UrlMapper urlMapper,
                            final StatisticsMapper statisticsMapper) {
    this.urlRepository = urlRepository;
    this.offersCollectorService = offersCollectorService;
    this.urlMapper = urlMapper;
    this.statisticsMapper = statisticsMapper;
  }

  /**
   * collects data from active urls
   */
  public void gatherData() {
    final List<Tuple2<Url, CompletableFuture<List<Offer>>>> tuples = List.ofAll(urlRepository.findAllByActiveTrueAndNotCheckedToday())
        .map(Exceptions.uncheckedException(url -> Tuple.of(url,
            offersCollectorService.collectOffers(urlMapper.entityToModel(url)))));

    if (tuples.isEmpty()) {
      return;
    }

    final List<CompletableFuture<List<Offer>>> completables = tuples.map(f -> f._2);
    CompletableFuture.allOf(CollectionUtils.toArray(completables.asJava(), CompletableFuture.class));

    tuples
        .map(Exceptions.uncheckedException(f -> Tuple.of(f._1, f._2.get())))
        .map(f -> Tuple.of(f._1, StatisticsHelper.calculateStatistics(f._2)))
        .map(f -> {
          f._1.getStatistics().add(statisticsMapper.map(f._2));
          return f._1;
        })
        .transform(urlRepository::save);
  }
}
