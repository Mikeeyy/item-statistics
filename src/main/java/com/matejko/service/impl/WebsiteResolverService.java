package com.matejko.service.impl;

import com.matejko.exceptions.NonExistingWebsiteException;
import com.matejko.model.common.Website;
import com.matejko.model.generated.Url;
import com.matejko.service.interfaces.parsers.WebParserService;
import io.vavr.collection.Stream;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
@Named
public class WebsiteResolverService {
  private final List<WebParserService> webParsers;

  @Inject
  public WebsiteResolverService(final List<WebParserService> webParsers) {
    this.webParsers = webParsers;
  }

  /**
   * returns a web parser for given website if url satisfied its pattern
   *
   * @param url url
   * @return web parser
   * @throws NonExistingWebsiteException not a signle pattern has been satisfied
   */
  public WebParserService resolveWebParser(final Url url) throws NonExistingWebsiteException {
    final Website website = resolveWebsite(url);

    return Stream.ofAll(webParsers)
        .find(f -> Objects.equals(website, f.serviceType()))
        .getOrElseThrow(() ->
            new NonExistingWebsiteException(String.format("No service website implemented for url [%s]", url)));
  }

  /**
   * checks if url satisfies any pattern and returns an website enum for it
   *
   * @param url url
   * @return website enum if url satisfies a pattern
   * @throws NonExistingWebsiteException not a signle pattern has been satisfied
   */
  public Website resolveWebsite(final Url url) throws NonExistingWebsiteException {
    return Stream.of(Website.values())
        .find(website -> {
          Pattern pattern = Pattern.compile(".*" + website.getValue() + ".*");
          Matcher matcher = pattern.matcher(url.getUrl());
          return matcher.matches();
        })
        .getOrElseThrow(() -> new NonExistingWebsiteException(String.format("No service type implemented for url [%s]", url.getUrl())));
  }
}
