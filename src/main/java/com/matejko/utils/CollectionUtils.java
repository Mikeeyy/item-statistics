package com.matejko.utils;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Created by Mikołaj Matejko on 08.09.2017 as part of item-statistics
 */
public interface CollectionUtils {
  static <T> T[] toArray(final Collection<T> collection, final Class<?> clazz) {
    return collection.toArray((T[]) Array.newInstance(clazz, collection.size()));
  }
}
