package org.uofm.ot.dao;


import org.uofm.ot.model.Server_details;

public interface SystemConfigurationDAO {

	public Server_details getFedoraServerConfiguration();
	
	public Server_details getSMTPServerConfiguration();
	
	public Server_details getFusekiServerConfiguration();
	
	public Server_details saveFedoraServerConf(Server_details configuration);
	
	public Server_details saveSMTPServerConf(Server_details configuration);
	
	public Server_details saveFusekiServerConf(Server_details configuration);
	
}
