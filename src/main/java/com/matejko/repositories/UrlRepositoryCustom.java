package com.matejko.repositories;

import com.matejko.model.entity.Url;
import io.vavr.collection.Seq;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
public interface UrlRepositoryCustom {
  Seq<Url> findAllByActiveTrueAndFree();

  Seq<Url> findAllStuck();
}
