package com.matejko.service.interfaces.parsers;

import com.matejko.exceptions.ServiceException;
import com.matejko.model.common.Website;
import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Page;
import io.vavr.collection.List;
import org.jsoup.nodes.Document;


/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
public interface WebParserService {
  Website serviceType();

  Document navigate(String url) throws ServiceException;

  List<Offer> listOffers(final Document document);

  Page parsePagesQuantity(final Document document);

  String createNextPageUrl(final String providedUrl, final Long currentPage);
}
