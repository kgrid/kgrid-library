package org.uofm.ot.model;

import java.io.Serializable;

public class UserProfile implements Serializable {
	
	private int id;
	
	private String first_name;
	
	private String last_name;
	
	public UserProfile(){}
	
	public UserProfile(String first_name, String last_name) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
