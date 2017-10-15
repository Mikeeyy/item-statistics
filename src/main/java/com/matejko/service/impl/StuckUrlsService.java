package com.matejko.service.impl;

import com.matejko.model.common.Status;
import com.matejko.model.entity.BaseEntity;
import com.matejko.model.entity.Url;
import io.vavr.collection.Seq;
import javax.inject.Named;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Mikołaj Matejko on 15.10.2017 as part of item-statistics
 */
@Named
@RequiredArgsConstructor
public class StuckUrlsService {
  private static final Logger logger = LoggerFactory.getLogger(StuckUrlsService.class);

  private final UrlService urlService;

  public void freeStuckUrls() {
    final Seq<Url> stuckUrls = urlService.findAllStuckUrls();

    if (stuckUrls.isEmpty()) {
      return;
    }

    urlService.setStatus(stuckUrls, Status.FREE);

    logger.debug(String.format("For following urls status has been set to FREE: [%s]",
        StringUtils.join(stuckUrls.map(BaseEntity::getId))));
  }
}
