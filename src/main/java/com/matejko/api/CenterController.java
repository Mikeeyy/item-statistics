package com.matejko.api;

import com.matejko.model.generated.Url;
import com.matejko.service.impl.StatisticsGatherer;
import com.matejko.service.impl.UrlService;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Mikołaj Matejko on 26.09.2017 as part of item-statistics
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CenterController {

  private final StatisticsGatherer statisticsGatherer;
  private final UrlService urlService;

  @PostMapping(value = "/executeManually")
  public void executeManually() {
    statisticsGatherer.gatherData();
  }

  @PostMapping(value = "/url/add")
  public Collection<Url> addUrl(@RequestBody final Collection<Url> urls) {
    return urls.stream().map(urlService::merge).collect(Collectors.toList());
  }

  @PostMapping(value = "/url/delete")
  public Url deleteUrl(@RequestParam final String url) {
    return urlService.delete(url);
  }
}
