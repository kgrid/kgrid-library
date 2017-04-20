package org.uofm.ot.model;


public class ServerDetails {
	private String name;

	private String username;
	
	private String password;

	private String url;

	private String prefix;


	public ServerDetails(String name, String username,
			String password, String prefix) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.url = name;
		this.prefix = prefix;
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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
