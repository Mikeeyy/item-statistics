package com.matejko.service.impl;

import com.matejko.exceptions.ServiceException;
import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Page;
import com.matejko.model.generated.Url;
import com.matejko.service.interfaces.parsers.WebParserService;
import io.vavr.collection.List;
import java.util.concurrent.CompletableFuture;
import javax.inject.Named;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
@Named
@RequiredArgsConstructor
public class SingleOfferCollectorService {
  private static final Logger logger = LoggerFactory.getLogger(SingleOfferCollectorService.class);

  private final WebsiteResolverService typeResolverService;

  /**
   * Collecting offers for url
   *
   * @param url url
   * @return all offers
   * @throws ServiceException exception
   */
  @Async
  public CompletableFuture<List<Offer>> collectOffersForUrl(final Url url) throws ServiceException {
    final WebParserService parserService = typeResolverService.resolveWebParser(url);
    final Document document = parserService.navigate(url.getUrl());

    if (!parserService.offersAvailable(document)) {
      return CompletableFuture.completedFuture(List.empty());
    }

    return CompletableFuture.completedFuture(parserService.listOffers(document));
  }

  /**
   * Collecting number of pages, actual index
   *
   * @param url url
   * @return number of pages, actual index
   * @throws ServiceException exception
   */
  public Page collectPagesInfo(final Url url) throws ServiceException {
    final WebParserService parserService = typeResolverService.resolveWebParser(url);
    final Document document = parserService.navigate(url.getUrl());

    return parserService.parsePagesQuantity(document);
  }
}
