package com.matejko.service.impl;

import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Page;
import com.matejko.model.generated.Url;
import com.matejko.service.impl.parsers.AllegroWebParserService;
import io.vavr.API;
import io.vavr.collection.List;
import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Mikołaj Matejko on 22.09.2017 as part of item-statistics
 */
@RunWith(Parameterized.class)
public class OffersCollectorServiceTest {

  @Parameterized.Parameter
  public String url;
  @Parameterized.Parameter(1)
  public Long currentPage;
  @Parameterized.Parameter(2)
  public Long pagesQuantity;
  @Parameterized.Parameter(3)
  public Integer mainPageOffers;
  @Parameterized.Parameter(4)
  public Integer otherPageOffers;
  private OffersCollectorService service;
  private WebsiteResolverService resolverService;
  private SingleOfferCollectorService collectorService;
  private AllegroWebParserService parser;

  @Parameterized.Parameters
  public static Object[][] parameters() {
    return new Object[][] {
        {"https://allegro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905",
            1L, 5L, 25, 25},
        {"allegro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905",
            1L, 8L, 25, 25},
        {"https://allegro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905&p=5",
            5L, 9L, 25, 25},
        {"https://allegro.pl/kategoria/motocykle-i-quady-5557",
            2L, 33L, 0, 0},
        {"https://allegro.pl/listing?string=licytacja&order=m&bmatch=base-relevance-floki-5-nga-hcp-ele-1-3-0905",
            1L, 1L, 25, 25}
    };
  }

  @Before
  public void init() {
    resolverService = mock(WebsiteResolverService.class);
    collectorService = mock(SingleOfferCollectorService.class);
    parser = mock(AllegroWebParserService.class);

    service = new OffersCollectorService(resolverService, collectorService);
  }

  @Test
  public void collectOffers() throws Exception {
    final Url mainUrl = new Url().withUrl(this.url);

    when(collectorService.collectPagesInfo(mainUrl)).thenReturn(new Page().withCurrentPage(currentPage).withQuantity(pagesQuantity));
    when(collectorService.collectOffersForUrl(mainUrl)).thenReturn(mockedCompletableOffersList(mainPageOffers));
    when(resolverService.resolveWebParser(mainUrl)).thenReturn(parser);
    when(parser.createNextPageUrl(eq(mainUrl.getUrl()), any(Long.class))).thenCallRealMethod();
    when(collectorService.collectOffersForUrl(any(Url.class))).thenReturn(mockedCompletableOffersList(otherPageOffers));

    final CompletableFuture<List<Offer>> completableFuture = service.collectOffers(mainUrl);

    assertNotNull("result of method shouldn't be null", completableFuture);

    final List<Offer> offerList = completableFuture.get();
    assertNotNull("list inside should not be null", offerList);
    assertEquals("number of offers should be equal to: Number of offer from main page + other pages quantity * number of offers per other page",
        mainPageOffers + (pagesQuantity - currentPage) * otherPageOffers, offerList.size());


  }

  private CompletableFuture<List<Offer>> mockedCompletableOffersList(final int quantity) {
    final List<Offer> offers = API.For(List.range(0, quantity))
        .yield(f -> mock(Offer.class))
        .toList();

    return CompletableFuture.completedFuture(offers);
  }

}