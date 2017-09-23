package com.matejko.model.common;

/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
public enum Website {
  ALLEGRO("allegro.pl"), OLX("olx.pl");

  private final String url;

  Website(final String url) {
    this.url = url;
  }

  public String getValue() {
    return this.url;
  }
}
