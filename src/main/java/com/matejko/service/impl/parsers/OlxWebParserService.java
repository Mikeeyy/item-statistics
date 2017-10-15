package com.matejko.service.impl.parsers;

import com.matejko.model.common.Website;
import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Page;
import io.vavr.collection.List;
import javax.inject.Named;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Mikołaj Matejko on 19.08.2017 as part of offer-lookup
 */
@Named
public class OlxWebParserService extends BaseWebParserService {
  @Override
  public Website serviceType() {
    return Website.OLX;
  }

  @Override
  public List<Offer> listOffers(final Document document) {
    final Elements elements = document.select("td.offer");

    return List.ofAll(elements.stream().map(article -> {
      final String pictureUrl = article.select("img[src]").attr("src");
      final String offerUrl = article.select("a[href]").select("a.link").attr("href");
      final String title = article.select("td[valign=top]").select("h3").text();
      final String price = article.select("td[valign=top]").select("p").select("p.price").text();

      return new Offer("", offerUrl, pictureUrl, price, title);
    }))
        .filter(this::revokeEmptyOffers);
  }

  @Override
  public Page parsePagesQuantity(final Document document) {
    final Elements elements = document.select("span.item");

    if (elements.isEmpty()) {
      return new Page(1L, 1L);
    }

    final String current = elements.select("span.current").text();

    final String quantity = elements.get(elements.size() - 1).text();
    return new Page(Long.valueOf(current), Long.valueOf(quantity));
  }

  @Override
  public boolean offersAvailable(final Document document) {
    final Elements elements = document.select("div.wrapper").select("div.content").select("div.emptynew");

    return elements == null || elements.size() == 0;
  }

  @Override
  protected String queryPageParam() {
    return "page";
  }
}
