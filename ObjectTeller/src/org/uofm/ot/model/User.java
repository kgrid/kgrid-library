package org.uofm.ot.model;

import org.hibernate.validator.constraints.NotEmpty;

public class User {

	@NotEmpty(message = "Please enter your login ID.")
	private String username; 
	
	@NotEmpty(message = "Please enter your password.")
	private String passwd;
	

	private String role;

	public User(String username, String password, String role) {
		super();
		this.username = username;
		this.passwd = password;
		this.role = role;
	} 
	
	public User(){}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + passwd + ", role=" + role + "]";
	}
	
	
}
