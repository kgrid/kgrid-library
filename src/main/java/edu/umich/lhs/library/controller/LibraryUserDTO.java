package edu.umich.lhs.library.controller;

import edu.umich.lhs.library.model.UserProfile;

public class LibraryUserDTO {
	
	private String username ; 
	
	private String password ; 
	
	private String role; 
	
	private UserProfile profile ; 

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	

}
