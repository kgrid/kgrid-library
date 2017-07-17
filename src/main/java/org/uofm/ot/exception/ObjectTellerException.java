package org.uofm.ot.exception;

public class ObjectTellerException extends Exception{

	public ObjectTellerException() {
		super();
	}

	public ObjectTellerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ObjectTellerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectTellerException(String message) {
		super(message);
	}

	public ObjectTellerException(Throwable cause) {
		super(cause);
	}
	
}
