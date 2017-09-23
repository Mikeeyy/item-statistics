package com.matejko.service.impl;

import com.matejko.mappers.StatisticsMapper;
import com.matejko.mappers.StatisticsMapperImpl;
import com.matejko.mappers.UrlMapper;
import com.matejko.mappers.UrlMapperImpl;
import com.matejko.model.entity.Url;
import com.matejko.model.generated.Offer;
import com.matejko.repositories.UrlRepository;
import io.vavr.API;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
public class StatisticsGathererTest {

  private StatisticsGatherer service;
  private UrlRepository urlRepository;
  private OffersCollectorService offersCollectorService;
  private UrlMapper urlMapper;
  private StatisticsMapper statisticsMapper;
  private List<Url> mockedUrls;

  @Before
  public void setUp() throws Exception {
    urlRepository = mock(UrlRepository.class);
    offersCollectorService = mock(OffersCollectorService.class);
    urlMapper = new UrlMapperImpl();
    statisticsMapper = new StatisticsMapperImpl();
    mockedUrls = mockedDbUrls();

    when(urlRepository.findAllByActiveTrueAndNotCheckedToday()).thenReturn(mockedUrls);
    when(urlRepository.save(mockedUrls)).thenReturn(mockedUrls);

    for (final Url url : mockedUrls) {
      final com.matejko.model.generated.Url mappedUrl = urlMapper.entityToModel(url);
      when(offersCollectorService.collectOffers(mappedUrl))
          .thenReturn(CompletableFuture.completedFuture(offersForUrl(mappedUrl)));
    }
    service = new StatisticsGatherer(urlRepository, offersCollectorService, urlMapper, statisticsMapper);
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

  private List<Url> mockedDbUrls() {
    return API.For(io.vavr.collection.List.range(0, 15))
        .yield(f -> {
          final Url url = new Url();
          url.setActive(true);
          url.setDescription(UUID.randomUUID().toString());
          url.setUrl("https://www.allegro.pl/" + UUID.randomUUID().toString());
          return url;
        })
        .toJavaList();
  }

  @Test
  public void gatherData() throws Exception {
    service.gatherData();
  }

}