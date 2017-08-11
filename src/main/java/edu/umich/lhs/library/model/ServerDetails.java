package edu.umich.lhs.library.model;


public class ServerDetails {
	private String name;

	private String username;
	
	private String password;

	private String url;

	public ServerDetails(String name, String username,
			String password) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.url = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}
}
