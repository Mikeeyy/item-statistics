package com.matejko.service.impl.parsers;

import com.matejko.model.common.Website;
import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Page;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import java.util.stream.Collectors;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Created by Mikołaj Matejko on 17.08.2017 as part of offer-lookup
 */
@Named
public class AllegroWebParserService extends BaseWebParserService {

  @Override
  public Website serviceType() {
    return Website.ALLEGRO;
  }

  @Override
  public List<Offer> listOffers(final Document document) {
    final Element element = document.getElementById("opbox-listing");
    final Elements articles = element.select("article");

    return List.ofAll(articles.stream().map(article -> {
      final Element pictureAndText = article.child(0).child(0);
      final Element picture = pictureAndText.child(0);
      final Element text = pictureAndText.child(1);

      final String pictureUrl = picture.select("img").attr("src");
      final String offerUrl = text.select("h2").first().child(0).attr("href");
      final String title = text.select("h2").first().text();
      final String price = text.select("div.ae47445").text();
      final String attributes = parseAttributes(text.select("div.bec3e46")
          .select("dl").stream().flatMap(f -> f.children().stream()).collect(Collectors.toList()));

      return new Offer(attributes, offerUrl, pictureUrl, price, title);
    }))
        .filter(this::revokeEmptyOffers);
  }

  @Override
  public Page parsePagesQuantity(final Document document) {
    final String current = document.select("li.active").get(0).text();
    final String quantity = document.select("li.quantity").get(0).text();
    return new Page(Long.valueOf(current), Long.valueOf(quantity));
  }

  private String parseAttributes(final java.util.List<Element> children) {
    if (children == null || children.isEmpty()) {
      return StringUtils.EMPTY;
    }

    final Map<Boolean, List<Element>> tuple2s = Stream.ofAll(children.stream())
        .toList()
        .zipWithIndex()
        .groupBy(f -> f._2 % 2 == 0)
        .mapValues(f -> f.map(g -> g._1));

    //jeden atrybut
    if (!tuple2s.containsKey(false)) {
      return tuple2s.get(true).transform(attrs -> StringUtils.join(attrs, StringUtils.LF));
    }

    return tuple2s.get(true)
        .getOrElseThrow(() -> new ServiceException("lack of data in the map, key: true"))
        .zipWith(tuple2s.get(false).getOrElseThrow(() -> new ServiceException("lack of data in the map, key: false")),
            (el1, el2) -> el1.text() + ": " + el2.text())
        .transform(attrs -> StringUtils.join(attrs, StringUtils.LF));
  }

  @Override
  protected String queryPageParam() {
    return "p";
  }
}
