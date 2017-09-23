package com.matejko.exceptions;

import java.io.IOException;
import java.util.Optional;
import org.junit.Test;


import static com.matejko.exceptions.Exceptions.uncheckedException;

/**
 * Created by Mikołaj Matejko on 08.09.2017 as part of item-statistics
 */
public class ExceptionsTest {
  private String throwServiceException() throws ServiceException {
    throw new ServiceException("");
  }

  private String throwIOException() throws IOException {
    throw new IOException("");
  }

  private String throwException() throws Exception {
    throw new Exception("");
  }

  @Test(expected = ServiceRuntimeException.class)
  public void serviceExceptionInLambdaShouldBeThrownAsRuntimeException() throws Exception {
    Optional.of("StringValue")
        .map(uncheckedException(f -> throwServiceException()));
  }

  @Test(expected = ServiceRuntimeException.class)
  public void ioExceptionInLambdaShouldBeThrownAsRuntimeException() throws Exception {
    Optional.of("StringValue")
        .map(uncheckedException(f -> throwIOException()));
  }

  @Test(expected = ServiceRuntimeException.class)
  public void exceptionInLambdaShouldBeThrownAsRuntimeException() throws Exception {
    Optional.of("StringValue")
        .map(uncheckedException(f -> throwException()));
  }

  @Test
  public void uncheckedException1() throws Exception {
  }

}