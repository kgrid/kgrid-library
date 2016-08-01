package org.uofm.ot.model;

public class LibrarySetting {
	
	private Server_details fusekiConfig;
	
	private Server_details fedoraConfig;
	
	private Server_details SMTPConfig;

	public Server_details getFusekiConfig() {
		return fusekiConfig;
	}

	public void setFusekiConfig(Server_details fusekiConfig) {
		this.fusekiConfig = fusekiConfig;
	}

	public Server_details getFedoraConfig() {
		return fedoraConfig;
	}

	public void setFedoraConfig(Server_details fedoraConfig) {
		this.fedoraConfig = fedoraConfig;
	}

	public Server_details getSMTPConfig() {
		return SMTPConfig;
	}

	public void setSMTPConfig(Server_details sMTPConfig) {
		SMTPConfig = sMTPConfig;
	}

	
}
