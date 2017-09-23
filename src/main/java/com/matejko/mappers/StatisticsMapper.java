package com.matejko.mappers;

import com.matejko.model.entity.Statistics;
import org.mapstruct.Mapper;

/**
 * Created by Mikołaj Matejko on 22.09.2017 as part of item-statistics
 */
@Mapper
public interface StatisticsMapper {
  /**
   * generated model to entity
   *
   * @param model generated model
   * @return entity
   */
  Statistics map(com.matejko.model.generated.Statistics model);

  /**
   * entity to generated model
   *
   * @param entity entity
   * @return generated model
   */
  com.matejko.model.generated.Statistics map(Statistics entity);
}
