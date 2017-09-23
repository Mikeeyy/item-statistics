package com.matejko.exceptions;

/**
 * Created by Mikołaj Matejko on 08.09.2017 as part of item-statistics
 */
public class ServiceRuntimeException extends RuntimeException {
  public ServiceRuntimeException() {
  }

  public ServiceRuntimeException(final String message) {
    super(message);
  }

  public ServiceRuntimeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ServiceRuntimeException(final Throwable cause) {
    super(cause);
  }

  public ServiceRuntimeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
