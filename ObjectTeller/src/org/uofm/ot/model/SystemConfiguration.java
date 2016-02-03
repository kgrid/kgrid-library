package org.uofm.ot.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SystemConfiguration {

	private SETTING_TYPE setting_type;
	
	private String server_name;
	
	@Pattern (regexp= "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", message="Invalid IP Address.")
	private String ip_address;
	
	@NotNull(message="Please enter authorised user name to access server.")
	private String username;
	
	@NotNull(message="Please enter password to access server.")
	private String passwd;
	
	private int id;
	
	public SystemConfiguration(){}
	
	

	public SystemConfiguration(SETTING_TYPE setting_type, String server_name, String ip_address, String username,
			String passwd, int id) {
		super();
		this.setting_type = setting_type;
		this.server_name = server_name;
		this.ip_address = ip_address;
		this.username = username;
		this.passwd = passwd;
		this.id = id;
	}



	public SETTING_TYPE getSetting_type() {
		return setting_type;
	}

	public void setSetting_type(SETTING_TYPE setting_type) {
		this.setting_type = setting_type;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getIp_address() {
		return ip_address;
	}



	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	
	

	@Override
	public String toString() {
		return "SystemConfiguration [setting_type=" + setting_type + ", server_name=" + server_name + ", ip_address="
				+ ip_address + ", username=" + username + ", passwd=" + passwd + ", id=" + id + "]";
	}




	
}
