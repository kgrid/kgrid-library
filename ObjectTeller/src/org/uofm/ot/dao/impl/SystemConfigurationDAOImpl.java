package org.uofm.ot.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.model.SETTING_TYPE;
import org.uofm.ot.model.SystemConfiguration;

public class SystemConfigurationDAOImpl implements SystemConfigurationDAO {

	private final String TABLE_NAME= "TAB_SETTINGS";
	
	private final String COLUMN_NAME_SETTING= "SETTING_TYPE";
	
	private final String USERNAME_COLUMN= "USERNAME";
	
	private final String PASSWD_COLUMN= "PASSWD";
	
	private final String SERVERNAME_COLUMN= "SERVER_NAME";
	
	private final String IP_ADDRESS= "IP_ADDRESS";
	
	private final String ID_COLUMN= "ID";
	
	private final String SELECT_SYSTEM_CONFIGURATIONS_BY_SETTING_TYPE= "SELECT * FROM "+TABLE_NAME+ " WHERE "+COLUMN_NAME_SETTING+" = ? " ;
	
	private final String UPDATE_BY_SETTING_TYPE = "UPDATE "+TABLE_NAME+" SET "+PASSWD_COLUMN+" = ? , "+USERNAME_COLUMN+" = ? , "+SERVERNAME_COLUMN+" = ? , "+IP_ADDRESS+" = ?  WHERE "+COLUMN_NAME_SETTING+" = ?" ;
	
	private final String INSERT_SYSTEM_CONFIGURATION = "INSERT INTO "+TABLE_NAME+"( "+COLUMN_NAME_SETTING+" , " +IP_ADDRESS+" , "+SERVERNAME_COLUMN+" , "+USERNAME_COLUMN+" , "+PASSWD_COLUMN+" ) VALUES (?,?,?,?,?) ";
	
	private JdbcTemplate jdbcTemplate;  

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public SystemConfiguration getFedoraServerConfiguration() {
		SystemConfiguration fedoraConf = null;
		try {
			fedoraConf = jdbcTemplate.queryForObject(SELECT_SYSTEM_CONFIGURATIONS_BY_SETTING_TYPE,new Object[] {SETTING_TYPE.FEDORA_SERVER.toString()},new BeanPropertyRowMapper<>(SystemConfiguration.class));
		}  catch( EmptyResultDataAccessException e) {
			fedoraConf = null;
		}
		return fedoraConf;
	}

	@Override
	public SystemConfiguration getSMTPServerConfiguration() {
		SystemConfiguration SMTPConf = null;
		try {
			SMTPConf = jdbcTemplate.queryForObject(SELECT_SYSTEM_CONFIGURATIONS_BY_SETTING_TYPE,new Object[] {SETTING_TYPE.SMTP_SERVER.toString()},new BeanPropertyRowMapper<>(SystemConfiguration.class));
		}  catch( EmptyResultDataAccessException e) {
			SMTPConf = null;
		}
		return SMTPConf;
	}

	@Override
	public SystemConfiguration saveFedoraServerConf(SystemConfiguration configuration) {
		SystemConfiguration oldConf = getFedoraServerConfiguration();
		SystemConfiguration newConf = null;
		int result ;
		if(oldConf != null) {
			result = jdbcTemplate.update(UPDATE_BY_SETTING_TYPE, configuration.getPasswd(),configuration.getUsername(),configuration.getServer_name(),configuration.getIp_address(),SETTING_TYPE.FEDORA_SERVER.toString());		
		} else {
			result = jdbcTemplate.update(  INSERT_SYSTEM_CONFIGURATION,  
					new Object[] { SETTING_TYPE.FEDORA_SERVER.toString(), configuration.getIp_address(), configuration.getServer_name(), configuration.getUsername(), configuration.getPasswd()  });  	  			
		}
		if(result > 0 ) {
			newConf = getFedoraServerConfiguration();
		}
		return newConf;
	}

	@Override
	public SystemConfiguration saveSMTPServerConf(SystemConfiguration configuration) {
		SystemConfiguration oldConf = getSMTPServerConfiguration();
		SystemConfiguration newConf = null;
		int result ;
		if(oldConf != null){
			result = jdbcTemplate.update(UPDATE_BY_SETTING_TYPE, configuration.getPasswd(),configuration.getUsername(),configuration.getServer_name(),configuration.getIp_address(),SETTING_TYPE.SMTP_SERVER.toString());
		} else {
			result = jdbcTemplate.update(  INSERT_SYSTEM_CONFIGURATION,  
					new Object[] { SETTING_TYPE.SMTP_SERVER.toString(), configuration.getIp_address(), configuration.getServer_name(), configuration.getUsername(), configuration.getPasswd()  });  	  
		}
		if(result > 0 ){
			newConf = getSMTPServerConfiguration();
		}
		return newConf;
	}

}
