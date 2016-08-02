package org.uofm.ot.model;


import javax.validation.constraints.Pattern;

public class Server_details {
	
	private SETTING_TYPE setting_type;
	
	private String svr_name;
	
	@Pattern (regexp= "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", message="Invalid IP Address.")
	private String ip_address;
	
	private String svr_username;
	
	private String svr_passwd;
	
	private int id;
	
	private Integer svr_port;
	
	private String complete_url;
	

	public Server_details() {
	}

	public Server_details(SETTING_TYPE setting_type, String svr_name, String ip_address, String svr_username,
			String svr_passwd, Integer svr_port, String complete_url) {
		this.setting_type = setting_type;
		this.svr_name = svr_name;
		this.ip_address = ip_address;
		this.svr_username = svr_username;
		this.svr_passwd = svr_passwd;
		this.svr_port = svr_port;
		this.complete_url = complete_url;
	}

	@Override
	public String toString() {
		return "Server_details [setting_type=" + setting_type + ", svr_name=" + svr_name + ", ip_address=" + ip_address
				+ ", svr_username=" + svr_username + ", svr_passwd=" + svr_passwd + ", id=" + id + ", svr_port="
				+ svr_port + ", complete_url=" + complete_url + "]";
	}

	public SETTING_TYPE getSetting_type() {
		return setting_type;
	}

	public void setSetting_type(SETTING_TYPE setting_type) {
		this.setting_type = setting_type;
	}

	public String getSvr_name() {
		return svr_name;
	}

	public void setSvr_name(String svr_name) {
		this.svr_name = svr_name;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getSvr_username() {
		return svr_username;
	}

	public void setSvr_username(String svr_username) {
		this.svr_username = svr_username;
	}

	public String getSvr_passwd() {
		return svr_passwd;
	}

	public void setSvr_passwd(String svr_passwd) {
		this.svr_passwd = svr_passwd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getSvr_port() {
		return svr_port;
	}

	public void setSvr_port(Integer svr_port) {
		this.svr_port = svr_port;
	}

	public String getComplete_url() {
		return complete_url;
	}

	public void setComplete_url(String complete_url) {
		this.complete_url = complete_url;
	}
	
	

}
