package com.matejko.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
public class PhantomJsUtils {
  public static Document renderPage(final String url) {
    System.setProperty("phantomjs.binary.path", systemIndependentExecutable());
    final WebDriver ghostDriver = new PhantomJSDriver();
    try {
      ghostDriver.get(url);
      return Jsoup.parse(ghostDriver.getPageSource());
    } finally {
      ghostDriver.quit();
    }
  }

  private static String systemIndependentExecutable() {
    return "src/main/resources/PhantomJsDrivers/phantomjs.exe";//// TODO: 07.09.2017
  }
}
