package com.matejko.converters;

import io.vavr.control.Option;
import java.sql.Date;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by Mikołaj Matejko on 23.09.2017 as part of item-statistics
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(final LocalDate locDate) {
    return Option.of(locDate).map(Date::valueOf).getOrNull();
  }

  @Override
  public LocalDate convertToEntityAttribute(final Date sqlDate) {
    return Option.of(sqlDate).map(Date::toLocalDate).getOrNull();
  }
}
