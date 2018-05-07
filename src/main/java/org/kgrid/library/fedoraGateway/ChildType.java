package org.kgrid.library.fedoraGateway;

public enum ChildType {
	PAYLOAD ("Payload"),
	INPUT ("Input"),
	OUTPUT ("Output"),
	LOG("Log"),
	CREATEACTIVITY("CreateActivity"),
	CITATIONS("Citations");
	
	private String childType;

	public String getChildType() {
		return childType;
	}

	private ChildType(String childType) {
		this.childType = childType;
	}

	
	
	
}
