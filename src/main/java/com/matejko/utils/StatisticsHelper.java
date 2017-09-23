package com.matejko.utils;

import com.matejko.model.generated.Offer;
import com.matejko.model.generated.Statistics;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mikołaj Matejko on 22.09.2017 as part of item-statistics
 */
public class StatisticsHelper {
  /**
   * calculates statistics for given offers
   *
   * @param offers offers
   * @return calculated statistics
   */
  public static Statistics calculateStatistics(final List<Offer> offers) {
    return offers.transform(f -> new Statistics()
        .withExecutionDate(LocalDate.now())
        .withQuantity((long) f.size())
        .withAveragePrice(f.map(g -> parsePrice(g.getPrice())).average().getOrElse(0.0))
        .withMedianPrice(median(f.map(g -> parsePrice(g.getPrice()))).getOrElse(0.0))
        .withHighestPrice(f.map(g -> parsePrice(g.getPrice())).max().getOrElse(0.0))
        .withLowestPrice(f.map(g -> parsePrice(g.getPrice())).min().getOrElse(0.0))
    );
  }

  /**
   * parsing price from website
   *
   * @param price price
   * @return parsed price to double
   */
  public static Double parsePrice(final String price) {
    final String partiallyParsedPrice = Stream.ofAll(price.chars().mapToObj(f -> (char) f))
        .map(String::valueOf)
        .filter(f -> f.matches("[0-9\\.,]"))
        .transform(elements -> StringUtils.join(elements.asJava(), StringUtils.EMPTY));

    if (partiallyParsedPrice.contains(".") && partiallyParsedPrice.contains(",")) {
      return Double.valueOf(partiallyParsedPrice.replace(".", "").replace(",", "."));
    }

    return Double.valueOf(partiallyParsedPrice.replaceAll("[,\\.]", "."));
  }

  /**
   * calculating median
   *
   * @param numbers numbers
   * @param <T>     type of numbers
   * @return median as double
   */
  public static <T extends Number> Option<Double> median(final List<T> numbers) {
    return Option.of(numbers)
        .map(g -> g
            .map(Number::doubleValue)
            .sorted()
            .transform(f -> {
              if (f.isEmpty()) {
                return null;
              } else if (f.size() % 2 == 0) {
                return (f.get(f.size() / 2) + f.get(f.size() / 2 - 1)) / 2;
              } else {
                return f.get(f.size() / 2);
              }
            }))
        .flatMap(Option::of);
  }
}
