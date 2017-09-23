package com.matejko.exceptions;

import io.vavr.CheckedFunction1;
import java.util.function.Function;

/**
 * Created by Mikołaj Matejko on 08.09.2017 as part of item-statistics
 */
public interface Exceptions {
  static <T, R> Function<T, R> uncheckedException(final CheckedFunction1<T, R> function) {
    return uncheckedException(function, ServiceRuntimeException::new);
  }

  static <T, R> Function<T, R> uncheckedException(final CheckedFunction1<T, R> function,
                                                  final Function<? super Throwable, ? extends RuntimeException> exceptionFunction) {
    return g -> {
      try {
        return function.apply(g);
      } catch (final Throwable e) {
        throw exceptionFunction.apply(e);
      }
    };
  }
}
