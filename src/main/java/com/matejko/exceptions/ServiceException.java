package com.matejko.exceptions;

/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
public class ServiceException extends Exception {
  public ServiceException() {
  }

  public ServiceException(final String message) {
    super(message);
  }

  public ServiceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ServiceException(final Throwable cause) {
    super(cause);
  }

  public ServiceException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
