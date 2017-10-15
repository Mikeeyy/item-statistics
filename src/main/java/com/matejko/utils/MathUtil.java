package com.matejko.utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.stat.StatUtils;

public class MathUtil {

  /**
   * default significance level
   */
  public static final double DEFAULT_SIGNIFICANCE_LEVEL = 0.85;
  /**
   * static instance
   */
  private static final MathUtil instance = new MathUtil();

  public static MathUtil getInstance() {
    return instance;
  }

  public <T> OutlierRemover<T> removeAllOutliers(final io.vavr.collection.List<T> values,
                                                 final Function<T, Double> converter) {
    io.vavr.collection.List<T> result = values;
    io.vavr.collection.List<T> outliers = io.vavr.collection.List.empty();
    T outlier;

    while ((outlier = getOutlier(result.asJava(), converter)) != null) {
      result = result.remove(outlier);
      outliers = outliers.append(outlier);
    }

    return new OutlierRemover<>(result, outliers);
  }

  /**
   * Returns a statistical outlier with the default significance level (0.85),
   * or null if no such outlier exists..
   */
  public <T> T getOutlier(List<T> values, Function<T, Double> converter) {
    return getOutlier(values, converter, DEFAULT_SIGNIFICANCE_LEVEL);
  }

  public <T> T getOutlier(List<T> values, Function<T, Double> converter, double significanceLevel) {
    AtomicReference<T> outlier = new AtomicReference<>();
    double grubbs = getGrubbsTestStatistic(values, converter, outlier);
    double size = values.size();
    if (size < 3) {
      return null;
    }
    TDistributionImpl t = new TDistributionImpl(size - 2.0);
    try {
      double criticalValue = t.inverseCumulativeProbability((1.0 - significanceLevel) / (2.0 * size));
      double criticalValueSquare = criticalValue * criticalValue;
      double grubbsCompareValue = ((size - 1) / Math.sqrt(size)) *
          Math.sqrt((criticalValueSquare) / (size - 2.0 + criticalValueSquare));
      System.out.println("critical value: " + grubbs + " - " + grubbsCompareValue);
      if (grubbs > grubbsCompareValue) {
        return outlier.get();
      } else {
        return null;
      }
    } catch (MathException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> double getGrubbsTestStatistic(List<T> values, Function<T, Double> converter, AtomicReference<T> outlier) {
    double[] array = toArray(values.stream().map(converter).collect(Collectors.toList()));
    double mean = StatUtils.mean(array);
    double stddev = stdDev(values.stream().map(converter).collect(Collectors.toList()));
    double maxDev = 0;
    for (T o : values) {
      double d = converter.apply(o);
      if (Math.abs(mean - d) > maxDev) {
        maxDev = Math.abs(mean - d);
        outlier.set(o);
      }
    }
    double grubbs = maxDev / stddev;
    System.out.println("mean/stddev/maxDev/grubbs: " + mean + " - " + stddev + " - " + maxDev + " - " + grubbs);
    return grubbs;
  }

  public Double stdDev(List<?> values) {
    return stdDev(toArray(values));
  }

  public Double stdDev(double[] values) {
    return Math.sqrt(StatUtils.variance(values));
  }

  public double[] toArray(List<?> values) {
    double[] d = new double[values.size()];
    int count = 0;
    for (Object o : values) {
      double val = o instanceof Double ? (Double) o : Double.parseDouble("" + o);
      d[count++] = val;
    }
    return d;
  }

  @Data
  @AllArgsConstructor
  public class OutlierRemover<T> {
    public io.vavr.collection.List<T> data;
    public io.vavr.collection.List<T> outliers;
  }
}