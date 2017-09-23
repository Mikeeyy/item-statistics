package com.matejko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ItemStatisticsApplication {

  public static void main(final String[] args) {
    final ConfigurableApplicationContext ctx = SpringApplication.run(ItemStatisticsApplication.class, args);
    // final StatisticsGatherer statisticsGatherer = ctx.getBean(StatisticsGatherer.class);
    // statisticsGatherer.gatherData();
  }
}
