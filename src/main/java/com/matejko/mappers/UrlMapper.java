package com.matejko.mappers;

import com.matejko.model.generated.Url;
import org.mapstruct.Mapper;

/**
 * Created by Mikołaj Matejko on 20.09.2017 as part of item-statistics
 */
@Mapper
public interface UrlMapper {
  Url entityToModel(com.matejko.model.entity.Url entity);

  com.matejko.model.entity.Url modelToEntity(Url model);
}
