package org.uofm.ot.transferobjects;

public class Result {

	private int success;
	
	private Object result;
	
	private String errorMessage;
	
	public Result(){}

	public Result(int success, String result, String errorMessage) {
		super();
		this.success = success;
		this.result = result;
		this.errorMessage = errorMessage;
	}

	
	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "Result [success=" + success + ", result=" + result + ", errorMessage=" + errorMessage + "]";
	}
	
	
}
