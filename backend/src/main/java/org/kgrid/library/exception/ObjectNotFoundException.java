package org.kgrid.library.exception;

public class ObjectNotFoundException extends LibraryException {

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
