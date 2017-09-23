package com.matejko.service.impl.parsers;

import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Page;
import com.matejko.service.interfaces.parsers.WebParserService;
import io.vavr.collection.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by Mikołaj Matejko on 22.09.2017 as part of item-statistics
 */
public abstract class BaseParserServiceTest {

  private final String url;
  private WebParserService service;

  BaseParserServiceTest(final String url) {
    this.url = url;
  }

  protected abstract String queryPageParam();

  protected abstract WebParserService service();

  @Before
  public void setUp() {
    service = service();
  }

  @Test
  public void serviceType() throws Exception {
    Assert.assertNotNull("type cannot be null", service.serviceType());
  }

  @Test
  public void listOffers() throws Exception {
    final Document document = service.navigate(url);
    final List<Offer> offers = service.listOffers(document);

    Assert.assertNotNull("offers cannot be null", offers);
    Assert.assertTrue("list of offers should not be empty", !offers.isEmpty());

    Assert.assertTrue("for all offers basic data should be fulfilled",
        offers.forAll(offer -> isNotBlank(offer.getTitle()) && isNotBlank(offer.getPrice())
            && isNotBlank(offer.getOfferUrl())));
  }


  @Test
  public void parsePagesQuantity() throws Exception {
    final Document document = service.navigate(url);
    final Page page = service.parsePagesQuantity(document);

    Assert.assertNotNull("page object cannot be null", page);
    Assert.assertNotNull("current page cannot be null", page.getCurrentPage());
    Assert.assertNotNull("quantity cannot be null", page.getQuantity());

    Assert.assertTrue("current page should be higher than zero", page.getCurrentPage() > 0);
    Assert.assertTrue("quantity should be higher than zero", page.getQuantity() > 0);
    Assert.assertTrue("current page should be lower or equal to quantity",
        page.getCurrentPage() <= page.getQuantity());
  }

  @Test
  public void createNextPageUrl() throws Exception {
    final String nextPageUrl = service.createNextPageUrl(url, 3L);

    Assert.assertTrue("each created link should contain ? in url",
        nextPageUrl.contains("?"));

    final Pattern pattern = Pattern.compile(".*[?&]" + queryPageParam() + "=[0-9]+.*");
    final Matcher matcher = pattern.matcher(nextPageUrl);

    Assert.assertTrue("each created link for next page should contain page index in query",
        matcher.matches());

    final Matcher matcher2 = pattern.matcher(nextPageUrl);
    int count = 0;
    while (matcher2.find()) {
      count++;
    }

    Assert.assertEquals("number of page index in query always should be 1", 1, count);
  }

  @Test
  public void navigate() throws Exception {
    final Document document = service.navigate(url);

    Assert.assertNotNull("page should be correctly fetched and saved as jsoup Document object",
        document);
  }
}
