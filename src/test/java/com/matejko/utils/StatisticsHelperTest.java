package com.matejko.utils;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by Mikołaj Matejko on 22.09.2017 as part of item-statistics
 */
@RunWith(Enclosed.class)
public class StatisticsHelperTest {
  @Test
  public void calculateStatistics() throws Exception {
  }

  @RunWith(Parameterized.class)
  public static class MedianTest {

    @Parameterized.Parameter()
    public List<? extends Number> numbers;

    @Parameterized.Parameter(1)
    public Option<Double> expectedMedian;

    @Parameterized.Parameters
    public static Object[][] prices() {
      return new Object[][] {
          {null, Option.none()},
          {List.empty(), Option.none()},
          {List.of(3, 13, 7, 5, 21, 23, 39, 23, 40, 23, 14, 12, 56, 23, 29), Option.of(23.0)},
          {List.of(3, 13, 7, 5, 21, 23, 23, 40, 23, 14, 12, 56, 23, 29), Option.of(22.0)},
          {List.of(5.5), Option.of(5.5)},
          {List.of(5.5, 15.6, 7.33), Option.of(7.33)},
          {List.of(5.5, 15.6, 7.33, 8.78), Option.of((7.33 + 8.78) / 2)}
      };
    }

    @Test
    public void medianTest() throws Exception {
      Assert.assertEquals("parsed price should be as expected", expectedMedian, StatisticsHelper.median(numbers));
    }
  }

  @RunWith(Parameterized.class)
  public static class ParsePriceTest {

    @Parameterized.Parameter()
    public String requestPrice;

    @Parameterized.Parameter(1)
    public Double expectedPrice;

    @Parameterized.Parameters
    public static Object[][] prices() {
      return new Object[][] {{"15 000,90 zł", 15000.90}, {"15000,90zł", 15000.90}, {"  1 5000.90   zł  ", 15000.90},
          {"0,9  zł", 0.90}, {" 15.000,90 zł", 15000.90}, {"15000.90zł", 15000.90}};
    }

    @Test
    public void parsePriceTest() throws Exception {
      Assert.assertEquals("parsed price should be as expected", expectedPrice, StatisticsHelper.parsePrice(requestPrice));
    }
  }
}