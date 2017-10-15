package com.matejko.service.impl;

import com.matejko.exceptions.Exceptions;
import com.matejko.mappers.StatisticsMapper;
import com.matejko.mappers.UrlMapper;
import com.matejko.model.common.Status;
import com.matejko.model.entity.Statistics;
import com.matejko.model.entity.Url;
import com.matejko.model.generated.Offer;
import com.matejko.utils.CollectionUtils;
import com.matejko.utils.MathUtil;
import com.matejko.utils.StatisticsHelper;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import javax.inject.Named;
import lombok.RequiredArgsConstructor;

/**
 * Created by Mikołaj Matejko on 20.09.2017 as part of item-statistics
 */
@Named
@RequiredArgsConstructor
public class StatisticsGatherer {

  private final UrlService urlService;
  private final OffersCollectorService offersCollectorService;

  private final UrlMapper urlMapper;
  private final StatisticsMapper statisticsMapper;

  /**
   * collects data from active urls
   */
  public void gatherData() {
    Seq<Url> dbUrls = urlService.findAllByActiveTrueAndFree();

    if (dbUrls.isEmpty()) {
      return;
    }

    dbUrls = urlService.setStatus(dbUrls, Status.PROCESSING);

    final List<Tuple2<Url, CompletableFuture<List<Offer>>>> tuples = dbUrls
        .map(Exceptions.uncheckedException(url -> Tuple.of(url,
            offersCollectorService.collectOffers(urlMapper.entityToModel(url)))))
        .toList();

    final List<CompletableFuture<List<Offer>>> completables = tuples.map(f -> f._2);
    CompletableFuture.allOf(CollectionUtils.toArray(completables.asJava(), CompletableFuture.class));

    tuples
        .map(Exceptions.uncheckedException(f -> Tuple.of(f._1, f._2.get().filter(z -> StatisticsHelper.containsNumber(z.getPrice())))))
        .map(f -> {
          final MathUtil.OutlierRemover<Offer> outlierRemover = MathUtil.getInstance().removeAllOutliers(f._2,
              g -> StatisticsHelper.parsePrice(g.getPrice()));

          final com.matejko.model.generated.Statistics genStats = StatisticsHelper
              .calculateStatistics(outlierRemover.getData());
          final String json = StatisticsHelper.statisticsJson(outlierRemover.getData());
          final String outliers = StatisticsHelper.statisticsJson(outlierRemover.getOutliers());

          final Statistics statistics = statisticsMapper.map(genStats);
          statistics.setCreationDate(LocalDateTime.now());
          statistics.setUrl(f._1);
          statistics.setData(json);
          statistics.setOutliers(outliers);
          statistics.setOutliersQuantity((long) outlierRemover.getOutliers().size());
          f._1.getStatistics().add(statistics);
          f._1.setStatus(Status.FREE);
          f._1.setModified();
          return f._1;
        })
        .transform(urls -> {
          if (urls != null && !urls.isEmpty()) {
            return urlService.save(urls);
          }

          return List.empty();
        });
  }
}
