package org.uofm.ot.transferobjects;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  

@XmlRootElement 
public class CodeMetadata {
	
	private String functionName;
	
	private EngineType engineType;
	
	private int noOfParams;
	
	private ArrayList<ParamDescription> params;
	
	private DataType returntype;
	
	private String executionDS;
	
	public String getExecutionDS() {
		return executionDS;
	}



	public void setExecutionDS(String executionDS) {
		this.executionDS = executionDS;
	}



	public CodeMetadata(){}

	

	public int getNoOfParams() {
		return noOfParams;
	}



	public void setNoOfParams(int noOfParams) {
		this.noOfParams = noOfParams;
	}



	public ArrayList<ParamDescription> getParams() {
		return params;
	}



	public DataType getReturntype() {
		return returntype;
	}



	public void setReturntype(DataType returntype) {
		this.returntype = returntype;
	}


	public CodeMetadata(String functionName, EngineType engineType, int noOfParams, ArrayList<ParamDescription> params,
			DataType returntype, String executionDS) {
		super();
		this.functionName = functionName;
		this.engineType = engineType;
		this.noOfParams = noOfParams;
		this.params = params;
		this.returntype = returntype;
		this.executionDS = executionDS;
	}



	@Override
	public String toString() {
		return "CodeMetadata [functionName=" + functionName + ", engineType=" + engineType + ", noOfParams="
				+ noOfParams + ", params=" + params + ", returntype=" + returntype + ", executionDS=" + executionDS
				+ "]";
	}



	public void setParams(ArrayList<ParamDescription> params) {
		this.params = params;
	}



	@XmlElement
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}


	@XmlElement
	public EngineType getEngineType() {
		return engineType;
	}

	public void setEngineType(EngineType engineType) {
		this.engineType = engineType;
	}

	

}
