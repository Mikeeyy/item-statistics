package com.matejko.repositories;

import com.matejko.model.entity.Url;
import java.util.List;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
public interface UrlRepositoryCustom {
  List<Url> findAllByActiveTrueAndNotCheckedToday();
}
