package org.uofm.ot.services;

public enum IDStatus {
	
	PUBLIC("public"),
	RESERVED("reserved"),
	UNAVAILABLE("unavailable");
	
	private String status; 
	
	private IDStatus(String status) {
		this.status = status ;
	}
	
	@Override
	public String toString(){
		return this.status;
	}
}
