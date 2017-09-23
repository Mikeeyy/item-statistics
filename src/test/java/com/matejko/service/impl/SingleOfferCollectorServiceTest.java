package com.matejko.service.impl;

import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Page;
import com.matejko.model.generated.Url;
import com.matejko.service.interfaces.parsers.WebParserService;
import io.vavr.collection.List;
import java.util.concurrent.CompletableFuture;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Mikołaj Matejko on 21.09.2017 as part of item-statistics
 */
public class SingleOfferCollectorServiceTest {

  private SingleOfferCollectorService service;

  private List<Offer> offersList = List.of(mock(Offer.class), mock(Offer.class), mock(Offer.class));
  private Page page = mock(Page.class);

  @Before
  public void setUp() throws Exception {
    final WebsiteResolverService resolver = mock(WebsiteResolverService.class);
    final WebParserService parser = mock(WebParserService.class);

    when(resolver.resolveWebParser(any(Url.class))).thenReturn(parser);

    final Document document = mock(Document.class);
    when(parser.navigate(any(String.class))).thenReturn(document);
    when(parser.listOffers(document)).thenReturn(offersList);
    when(parser.parsePagesQuantity(document)).thenReturn(page);

    service = new SingleOfferCollectorService(resolver);
  }

  @Test
  public void collectOffersForUrl() throws Exception {
    final CompletableFuture<List<Offer>> completableFuture = service.collectOffersForUrl(mock(Url.class));
    final List<Offer> list = completableFuture.get();

    Assert.assertTrue("both lists should be the same", offersList.containsAll(list));
  }

  @Test
  public void collectPagesInfo() throws Exception {
    final Page page = service.collectPagesInfo(mock(Url.class));

    Assert.assertEquals("both page objects should be equal", this.page, page);
  }

}