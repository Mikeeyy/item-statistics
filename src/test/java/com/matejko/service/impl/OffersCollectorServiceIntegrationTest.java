package com.matejko.service.impl;

import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Url;
import io.vavr.collection.List;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OffersCollectorServiceIntegrationTest {

  @Inject
  private OffersCollectorService service;

  public static List<String> urls() {
    return List.of("https://allegro.pl/kategoria/motocykle-i-quady-5557?string=yamaha%20r6&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905",
        "https://www.olx.pl/oferty/q-yamaha-r6-rj/?page=2");
  }

  @Test
  public void collectOffers() throws Exception {
    for (final String url : urls()) {
      final CompletableFuture<List<Offer>> completableFuture = service.collectOffers(new Url().withUrl(url));

      Assert.assertNotNull("future should not be null", completableFuture);

      final List<Offer> offerList = completableFuture.get();

      Assert.assertNotNull("offer list should not be null", offerList);
      Assert.assertTrue("offer list should not be empty", offerList.size() > 0);
    }
  }

}