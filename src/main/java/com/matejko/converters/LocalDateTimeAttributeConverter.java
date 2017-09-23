package com.matejko.converters;

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
    return (locDateTime == null ? null : Timestamp.valueOf(locDateTime));
  }

  @Override
  public LocalDateTime convertToEntityAttribute(final Timestamp sqlTimestamp) {
    return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
  }
}
