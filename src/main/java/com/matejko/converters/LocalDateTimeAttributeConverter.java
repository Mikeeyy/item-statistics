package com.matejko.converters;

import io.vavr.control.Option;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(final LocalDateTime locDateTime) {
    return Option.of(locDateTime).map(Timestamp::valueOf).getOrNull();
  }

  @Override
  public LocalDateTime convertToEntityAttribute(final Timestamp sqlTimestamp) {
    return Option.of(sqlTimestamp).map(Timestamp::toLocalDateTime).getOrNull();
  }
}
