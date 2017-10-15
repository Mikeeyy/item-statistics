package com.matejko.service.impl;

import com.matejko.mappers.StatisticsMapper;
import com.matejko.mappers.StatisticsMapperImpl;
import com.matejko.mappers.UrlMapper;
import com.matejko.mappers.UrlMapperImpl;
import com.matejko.model.common.Status;
import com.matejko.model.entity.Url;
import com.matejko.model.generated.Offer;
import io.vavr.API;
import io.vavr.collection.Seq;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import static org.mockito.Mockito.when;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
@RunWith(MockitoJUnitRunner.class)
public class StatisticsGathererTest {

  private StatisticsGatherer service;

  @Mock
  private UrlService urlService;
  @Mock
  private OffersCollectorService offersCollectorService;

  private UrlMapper urlMapper;
  private StatisticsMapper statisticsMapper;
  private Seq<Url> mockedUrls;

  @Before
  public void setUp() throws Exception {
    urlMapper = new UrlMapperImpl();
    statisticsMapper = new StatisticsMapperImpl();
    mockedUrls = mockedDbUrls();

    when(urlService.findAllByActiveTrueAndFree()).thenReturn(mockedUrls);
    when(urlService.save(mockedUrls)).thenReturn(mockedUrls.asJava());
    when(urlService.setStatus(mockedUrls, Status.PROCESSING)).thenReturn(mockedUrls.map(f -> {
      f.setStatus(Status.PROCESSING);
      return f;
    }));

    for (final Url url : mockedUrls) {
      final com.matejko.model.generated.Url mappedUrl = urlMapper.entityToModel(url);
      when(offersCollectorService.collectOffers(mappedUrl))
          .thenReturn(CompletableFuture.completedFuture(offersForUrl(mappedUrl)));
    }
    service = new StatisticsGatherer(urlService, offersCollectorService, urlMapper, statisticsMapper);
  }

  private io.vavr.collection.List<Offer> offersForUrl(final com.matejko.model.generated.Url mappedUrl) {
    return API.For(io.vavr.collection.List.range(0, 25))
        .yield(f -> new Offer()
            .withAttributes(UUID.randomUUID().toString())
            .withPrice("15 000 zł")
            .withTitle(UUID.randomUUID().toString())
            .withPictureUrl(UUID.randomUUID().toString())
            .withOfferUrl(mappedUrl.getUrl()))
        .toList();
  }

  private Seq<Url> mockedDbUrls() {
    return API.For(io.vavr.collection.List.range(0, 15))
        .yield(f -> {
          final Url url = new Url();
          url.setActive(true);
          url.setDescription(UUID.randomUUID().toString());
          url.setUrl("https://www.allegro.pl/" + UUID.randomUUID().toString());
          return url;
        }).toList();
  }

  @Test
  public void gatherData() throws Exception {
    service.gatherData();
  }

}