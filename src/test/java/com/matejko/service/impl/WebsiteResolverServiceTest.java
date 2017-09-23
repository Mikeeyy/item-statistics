package com.matejko.service.impl;

import com.matejko.exceptions.NonExistingWebsiteException;
import com.matejko.model.common.Website;
import com.matejko.model.generated.Url;
import com.matejko.service.interfaces.parsers.WebParserService;
import io.vavr.API;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
@RunWith(Enclosed.class)
public class WebsiteResolverServiceTest {

  public abstract static class BaseTest {
    WebsiteResolverService service;
    WebParserService parserOlx;
    WebParserService parserAllegro;
    Map<Website, WebParserService> parsersMap;

    @Before
    public void setUp() throws Exception {
      parserAllegro = mock(WebParserService.class);
      parserOlx = mock(WebParserService.class);

      when(parserAllegro.serviceType()).thenReturn(Website.ALLEGRO);
      when(parserOlx.serviceType()).thenReturn(Website.OLX);

      service = new WebsiteResolverService(List.of(parserAllegro, parserOlx).asJava());

      parsersMap = API.Map(API.Tuple(Website.ALLEGRO, parserAllegro)).put(API.Tuple(Website.OLX, parserOlx));
    }
  }

  public static class ExceptionsTest extends BaseTest {

    @Test(expected = NonExistingWebsiteException.class)
    public void resolveWebParserNotExistingParser() throws Exception {
      service = new WebsiteResolverService(List.of(parserAllegro).asJava());

      final String url = "https://www.olx.pl/motoryzacja/motocykle-skutery/q-tor%C3%B3wka/?search%5Border%5D=filter_float_price%3Adesc";
      service.resolveWebParser(new Url().withUrl(url));
    }

    @Test(expected = NonExistingWebsiteException.class)
    public void resolveWebsiteInvalidUrl() throws Exception {
      final String url = "www.allexgro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905&p=5";
      service.resolveWebsite(new Url().withUrl(url));
    }

    @Test(expected = NonExistingWebsiteException.class)
    public void resolveWebsiteNotYetImplementedWebsite() throws Exception {
      final String url = "www.ebay.com/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905&p=5";
      service.resolveWebsite(new Url().withUrl(url));
    }
  }

  @RunWith(Parameterized.class)
  public static class WebsiteResolverServiceParametrizedTest extends BaseTest {

    @Parameterized.Parameter
    public String url;
    @Parameterized.Parameter(1)
    public Website website;


    @Parameterized.Parameters
    public static Object[][] params() {
      return new Object[][] {
          {"https://allegro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905", Website.ALLEGRO},
          {"allegro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905", Website.ALLEGRO},
          {"www.allegro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905&p=5", Website.ALLEGRO},
          {"https://www.allegro.pl/kategoria/motocykle-i-quady-5557", Website.ALLEGRO},
          {"https://allegro.pl/listing?string=licytacja&order=m&bmatch=base-relevance-floki-5-nga-hcp-ele-1-3-0905", Website.ALLEGRO},
          {"https://www.olx.pl/motoryzacja/motocykle-skutery/q-tor%C3%B3wka/?search%5Border%5D=filter_float_price%3Adesc", Website.OLX},
          {"www.olx.pl/motoryzacja/motocykle-skutery/q-tor%C3%B3wka/?search%5Border%5D=filter_float_price%3Adesc", Website.OLX},
          {"olx.pl/motoryzacja/motocykle-skutery/q-tor%C3%B3wka/?search%5Border%5D=filter_float_price%3Adesc&page=5", Website.OLX}
      };
    }

    @Test
    public void resolveWebParser() throws Exception {
      Assert.assertEquals("resolved website parser should be as assumed", parsersMap.getOrElse(website, null),
          service.resolveWebParser(new Url().withUrl(url)));
    }

    @Test
    public void resolveWebsite() throws Exception {
      Assert.assertEquals("resolved website should be as assumed", website,
          service.resolveWebsite(new Url().withUrl(url)));
    }
  }
}