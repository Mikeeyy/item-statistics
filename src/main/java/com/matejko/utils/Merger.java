package com.matejko.utils;

import com.matejko.model.entity.Url;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by Mikołaj Matejko on 12.10.2017 as part of item-statistics
 */
public class Merger {
  public static Url merge(Url destination, Url old) {
    destination.setModificationDate(LocalDateTime.now());

    Optional.ofNullable(old.getActive()).ifPresent(destination::setActive);
    Optional.ofNullable(old.getDescription()).ifPresent(destination::setDescription);
    Optional.ofNullable(old.getUrl()).ifPresent(destination::setUrl);

    return destination;
  }
}
