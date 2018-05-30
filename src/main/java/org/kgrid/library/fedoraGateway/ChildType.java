package org.kgrid.library.fedoraGateway;

public enum ChildType {
	LOG("Log"),
	CREATEACTIVITY("CreateActivity");
	
	private String childType;

	public String getChildType() {
		return childType;
	}

	private ChildType(String childType) {
		this.childType = childType;
	}

	
	
	
}
