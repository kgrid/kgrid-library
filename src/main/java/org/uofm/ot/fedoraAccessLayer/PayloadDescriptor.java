package org.uofm.ot.fedoraAccessLayer;



public class PayloadDescriptor {

	private String functionName ;
	
	private String engineType;
	
	public PayloadDescriptor(){}

	public PayloadDescriptor(String functionName, String engineType) {
		super();
		this.functionName = functionName;
		this.engineType = engineType;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	@Override
	public String toString() {
		return "PayloadDescriptor [functionName=" + functionName + ", engineType=" + engineType + "]";
	}
	
}
