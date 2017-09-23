package com.matejko.service.impl.parsers;

import com.matejko.service.interfaces.parsers.WebParserService;
import java.util.Arrays;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by Mikołaj Matejko on 22.09.2017 as part of item-statistics
 */
@RunWith(Parameterized.class)
public class OlxWebParserServiceTest extends BaseParserServiceTest {

  public OlxWebParserServiceTest(final String url) {
    super(url);
  }

  @Parameterized.Parameters
  public static Iterable<String> urls() {
    return Arrays.asList("https://www.olx.pl/motoryzacja/motocykle-skutery/q-tor%C3%B3wka/?search%5Border%5D=filter_float_price%3Adesc",
        "www.olx.pl/motoryzacja/motocykle-skutery/q-tor%C3%B3wka/?search%5Border%5D=filter_float_price%3Adesc",
        "https://www.olx.pl/motoryzacja/motocykle-skutery/q-tor%C3%B3wka/?search%5Border%5D=filter_float_price%3Adesc&page=5");
  }

  @Override
  protected String queryPageParam() {
    return "page";
  }

  @Override
  protected WebParserService service() {
    return new OlxWebParserService();
  }
}
