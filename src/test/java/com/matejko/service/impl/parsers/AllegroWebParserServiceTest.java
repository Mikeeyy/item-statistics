package com.matejko.service.impl.parsers;

import com.matejko.service.interfaces.parsers.WebParserService;
import java.util.Arrays;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by Mikołaj Matejko on 21.09.2017 as part of item-statistics
 */
@RunWith(Parameterized.class)
public class AllegroWebParserServiceTest extends BaseParserServiceTest {

  public AllegroWebParserServiceTest(final String url) {
    super(url);
  }

  @Parameterized.Parameters
  public static Iterable<String> urls() {
    return Arrays.asList("https://allegro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905",
        "allegro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905",
        "https://allegro.pl/kategoria/motocykle-i-quady-5557?string=tor%C3%B3wka&order=dd&bmatch=base-relevance-floki-5-nga-hcp-aut-1-3-0905&p=5",
        "https://allegro.pl/kategoria/motocykle-i-quady-5557",
        "https://allegro.pl/listing?string=licytacja&order=m&bmatch=base-relevance-floki-5-nga-hcp-ele-1-3-0905");
  }

  @Override
  protected String queryPageParam() {
    return "p";
  }

  @Override
  protected WebParserService service() {
    return new AllegroWebParserService();
  }
}