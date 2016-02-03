package org.uofm.ot.dao;


import org.uofm.ot.model.SystemConfiguration;

public interface SystemConfigurationDAO {

	public SystemConfiguration getFedoraServerConfiguration();
	
	public SystemConfiguration getSMTPServerConfiguration();
	
	public SystemConfiguration saveFedoraServerConf(SystemConfiguration configuration);
	
	public SystemConfiguration saveSMTPServerConf(SystemConfiguration configuration);
}
