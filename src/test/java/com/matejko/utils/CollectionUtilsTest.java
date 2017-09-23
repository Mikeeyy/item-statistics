package com.matejko.utils;

import io.vavr.collection.List;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static org.junit.Assert.assertEquals;

/**
 * Created by Mikołaj Matejko on 08.09.2017 as part of item-statistics
 */
@RunWith(Parameterized.class)
public class CollectionUtilsTest<T> {

  private final List<T> list;
  private final Class<T> clazz;

  public CollectionUtilsTest(final List<T> list, final Class<T> clazz) {
    this.list = list;
    this.clazz = clazz;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {List.of(1, 5, 8, 15), Integer.class},
        {List.of("1", "5", "8", "15"), String.class},
        {List.of(new Generic<Integer>(), new Generic<String>(), new Generic<Double>(), new Generic<Integer>()), Generic.class}
    });
  }

  @Test
  public void integerListShouldBeCorrectlyTransformerToList() throws Exception {
    testToArray(list, clazz);
  }

  private void testToArray(final List<T> list, final Class<T> clazz) {
    final T[] array = CollectionUtils.toArray(list.asJava(), clazz);

    assertEquals("List size should be equal to array size", list.size(), array.length);

    final List<T> sortedList = list.sorted();
    final List<T> sortedArray = List.of(array).sorted();

    for (int i = 0; i < sortedList.size(); i++) {
      assertEquals("Objects on certain indexes should be identical",
          sortedList.get(i),
          sortedArray.get(i));
    }
  }
}

class Generic<G> implements Comparable<Generic<G>> {
  private Integer i = 0;

  void doSmth(final G obj) {

  }

  @Override
  public int compareTo(final Generic<G> o) {
    return i.compareTo(o.i);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Generic<?> generic = (Generic<?>) o;

    return i != null ? i.equals(generic.i) : generic.i == null;
  }

  @Override
  public int hashCode() {
    return i != null ? i.hashCode() : 0;
  }
}