package org.uofm.ot.knowledgeObject;

public class Payload {
	
	private String content;
	
	private String functionName ;
	
	private String engineType;
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		return "Payload [content=" + content + ", functionName=" + functionName + ", engineType=" + engineType + "]";
	}

	
	
	
	
	
}
