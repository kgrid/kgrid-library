package org.uofm.ot.exception;

public class ObjectTellerException extends Exception{

	private String errormessage;
	
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

	public String getErrormessage() {
		return errormessage;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}
	
	
}
