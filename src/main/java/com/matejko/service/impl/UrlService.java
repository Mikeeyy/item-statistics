package com.matejko.service.impl;

import com.matejko.mappers.UrlMapper;
import com.matejko.model.generated.Url;
import com.matejko.repositories.UrlRepository;
import com.matejko.utils.Merger;
import java.time.LocalDateTime;
import javax.inject.Named;
import lombok.RequiredArgsConstructor;

/**
 * Created by Mikołaj Matejko on 12.10.2017 as part of item-statistics
 */
@Named
@RequiredArgsConstructor
public class UrlService {
  private final UrlRepository urlRepository;
  private final UrlMapper urlMapper;

  public Url merge(Url url) {
    final com.matejko.model.entity.Url newEntity = urlMapper.modelToEntity(url);

    final com.matejko.model.entity.Url entityToSave = urlRepository.findByUrl(url.getUrl())
        .map(f -> Merger.merge(f, newEntity))
        .orElse(init(newEntity));

    final com.matejko.model.entity.Url saved = urlRepository.save(entityToSave);

    return urlMapper.entityToModel(saved);
  }

  public Url delete(final String url) {
    return urlRepository.findByUrl(url)
        .map(f -> {
          urlRepository.delete(f);
          return urlMapper.entityToModel(f);
        })
        .orElseThrow(() -> new RuntimeException(String.format("No url [%s] has been found", url)));
  }

  private com.matejko.model.entity.Url init(final com.matejko.model.entity.Url newEntity) {
    newEntity.setCreationDate(LocalDateTime.now());
    return newEntity;
  }
}
