package org.uofm.ot.exception;

public class ObjectNotFoundException extends ObjectTellerException {

  public ObjectNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ObjectNotFoundException(String message) {
    super(message);
  }

  public ObjectNotFoundException(Throwable cause) {
    super(cause);
  }

}
