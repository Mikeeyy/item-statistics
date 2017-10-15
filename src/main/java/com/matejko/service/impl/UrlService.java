package com.matejko.service.impl;

import com.matejko.mappers.UrlMapper;
import com.matejko.model.common.Status;
import com.matejko.model.generated.Url;
import com.matejko.repositories.UrlRepository;
import com.matejko.utils.Merger;
import io.vavr.API;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import java.time.LocalDateTime;
import javax.inject.Named;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Created by Mikołaj Matejko on 12.10.2017 as part of item-statistics
 */
@Named
@RequiredArgsConstructor
@Transactional
public class UrlService {
  private final UrlRepository urlRepository;
  private final UrlMapper urlMapper;

  public Seq<com.matejko.model.entity.Url> findAllByActiveTrueAndFree() {
    return urlRepository.findAllByActiveTrueAndFree();
  }

  public Url merge(Url url) {
    final com.matejko.model.entity.Url newEntity = urlMapper.modelToEntity(url);

    final com.matejko.model.entity.Url entityToSave = urlRepository.findByUrl(url.getUrl())
        .map(f -> Merger.merge(f, newEntity))
        .orElse(init(newEntity));

    entityToSave.setStatus(Status.FREE);

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

  public java.util.List<com.matejko.model.entity.Url> save(final Seq<com.matejko.model.entity.Url> urls) {
    return urlRepository.save(urls);
  }

  public Seq<com.matejko.model.entity.Url> setStatus(final Seq<com.matejko.model.entity.Url> dbUrls, final Status status) {
    return List.ofAll(urlRepository.save(dbUrls.map(f -> {
      f.setStatus(status);
      f.setModified();
      return f;
    })));
  }

  public Seq<com.matejko.model.entity.Url> findAllStuckUrls() {
    return urlRepository.findAllStuck();
  }

  public Seq<Url> generateAndSave(final String description, final String url, final Integer yearFrom, final Integer yearTo) {
    final List<Url> urls = API.For(List.rangeClosed(yearFrom, yearTo))
        .yield(f -> new Url(url.replace("YEAR_FROM", f.toString())
            .replace("YEAR_TO", f.toString()), description.replace("YEAR", f.toString()),
            true))
        .toList();

    urlRepository.save(urls.map(f -> {
      final com.matejko.model.entity.Url entity = urlMapper.modelToEntity(f);
      entity.setCreationDate(LocalDateTime.now());
      entity.setStatus(Status.FREE);
      return entity;
    }));

    return urls;
  }
}
