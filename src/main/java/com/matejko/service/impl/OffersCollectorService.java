package com.matejko.service.impl;

import com.matejko.exceptions.Exceptions;
import com.matejko.exceptions.NonExistingWebsiteException;
import com.matejko.exceptions.ServiceException;
import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Page;
import com.matejko.model.generated.Url;
import com.matejko.service.interfaces.parsers.WebParserService;
import com.matejko.utils.CollectionUtils;
import io.vavr.API;
import io.vavr.collection.List;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
@Named
public class OffersCollectorService {
  private static final Logger logger = LoggerFactory.getLogger(OffersCollectorService.class);

  private final WebsiteResolverService typeResolverService;
  private final SingleOfferCollectorService singleOfferCollectorService;

  @Inject
  public OffersCollectorService(final WebsiteResolverService typeResolverService,
                                final SingleOfferCollectorService singleOfferCollectorService) {
    this.typeResolverService = typeResolverService;
    this.singleOfferCollectorService = singleOfferCollectorService;
  }

  /**
   * collecting offers from all pages for given filter url
   *
   * @param providedUrl url
   * @return all offers
   * @throws ServiceException exception
   */
  @Async
  public CompletableFuture<List<Offer>> collectOffers(final Url providedUrl) throws ServiceException {
    final Page page = singleOfferCollectorService.collectPagesInfo(providedUrl);
    final CompletableFuture<List<Offer>> mainUrlOffers = singleOfferCollectorService.collectOffersForUrl(providedUrl);

    if (page.getQuantity() > 1) {
      final List<CompletableFuture<List<Offer>>> completables = List.ofAll(createUrls(providedUrl, page))
          .map(f -> new Url().withUrl(f))
          .map(Exceptions.uncheckedException(singleOfferCollectorService::collectOffersForUrl))
          .prepend(mainUrlOffers);

      CompletableFuture.allOf(CollectionUtils.toArray(completables.asJava(), CompletableFuture.class));

      return CompletableFuture.completedFuture(completables.flatMap(Exceptions.uncheckedException(CompletableFuture::get)));
    } else {
      return mainUrlOffers;
    }
  }

  private List<String> createUrls(final Url providedUrl, final Page page) throws NonExistingWebsiteException {
    final WebParserService parserService = typeResolverService.resolveWebParser(providedUrl);

    return API.For(List.range(page.getCurrentPage(), page.getQuantity()))
        .yield(i -> parserService.createNextPageUrl(providedUrl.getUrl(), i))
        .toList();
  }
}