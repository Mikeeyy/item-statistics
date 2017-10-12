package com.matejko.repositories;

import com.matejko.model.entity.Statistics;
import com.matejko.model.entity.Url;
import io.vavr.collection.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UrlRepositoryTest {
  @Autowired
  private TestEntityManager em;

  @Inject
  private UrlRepository urlRepository;

  private List<String> shouldBeReturnedIds;
  private List<Url> urls;

  @Before
  public void setUp() throws Exception {
    final Url emptyUrl = createSimpleUrl();

    final Url urlWithOldStats = createSimpleUrl();
    urlWithOldStats.getStatistics().add(createSimpleStatistics(LocalDate.now().minusDays(1)));

    final Url urlWithVeryOldStats = createSimpleUrl();
    urlWithVeryOldStats.getStatistics().add(createSimpleStatistics(LocalDate.now().minusDays(10)));

    final Url urlWithTodaysStats = createSimpleUrl();
    urlWithTodaysStats.getStatistics().add(createSimpleStatistics(LocalDate.now()));

    final Url urlWithOldAndTodaysStats = createSimpleUrl();
    urlWithOldAndTodaysStats.getStatistics().add(createSimpleStatistics(LocalDate.now().minusDays(1)));
    urlWithOldAndTodaysStats.getStatistics().add(createSimpleStatistics(LocalDate.now()));

    urls = List.of(emptyUrl, urlWithOldStats, urlWithVeryOldStats,
        urlWithTodaysStats, urlWithOldAndTodaysStats);
    shouldBeReturnedIds = List.of(emptyUrl.getUrl(), urlWithOldStats.getUrl(), urlWithVeryOldStats.getUrl());

    urls.forEach(em::persist);
    em.flush();
  }

  private Statistics createSimpleStatistics(final LocalDate localDate) {
    Statistics statistics = new Statistics();
    statistics.setExecutionDate(localDate);
    statistics.setQuantity(5L);
    statistics.setCreationDate(LocalDateTime.now());
    return statistics;
  }

  private Url createSimpleUrl() {
    Url url = new Url();
    url.setUrl("www.allegro.pl/" + UUID.randomUUID().toString());
    url.setDescription(UUID.randomUUID().toString());
    url.setActive(true);
    url.setCreationDate(LocalDateTime.now());
    return url;
  }

  @Test
  public void findAllByActiveTrueAndNotCheckedTodayTest() throws Exception {
    final List<Url> urls = List.ofAll(urlRepository.findAllByActiveTrueAndNotCheckedToday());

    Assert.assertEquals("number of returned elements should be equal to assumed number of elements",
        shouldBeReturnedIds.size(), urls.size());

    urls.forEach(url -> Assert.assertTrue("each returned url should be included into assumed ones",
        shouldBeReturnedIds.contains(url.getUrl())));
  }

  @Test
  public void findAllTest() throws Exception {
    final java.util.List<Url> urls = urlRepository.findAll();

    Assert.assertEquals("number of saved records should be equal to number of fetched records",
        this.urls.size(), urls.size());

    List.ofAll(urls)
        .zipWithIndex()
        .forEach(f -> {
          if (f._2 == 0) {
            Assert.assertEquals("number of saved statistics should be equal to 0",
                0, f._1.getStatistics().size());
          } else {
            Assert.assertTrue("number of stats should be greater than zero",
                f._1.getStatistics().size() > 0);
          }
        });
  }


}