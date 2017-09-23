package com.matejko.exceptions;

/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
public class NonExistingWebsiteException extends ServiceException {
  public NonExistingWebsiteException(final String message) {
    super(message);
  }
}
