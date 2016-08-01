package org.uofm.ot.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.model.SETTING_TYPE;
import org.uofm.ot.model.Server_details;

public class SystemConfigurationDAOImpl implements SystemConfigurationDAO {

	private final String TABLE_NAME= "SERVER_DETAILS";
	
	private final String COLUMN_NAME_SETTING= "SETTING_TYPE";
	
	private final String USERNAME_COLUMN= "SVR_USERNAME";
	
	private final String PASSWD_COLUMN= "SVR_PASSWD";
	
	private final String SERVERNAME_COLUMN= "SVR_NAME";
	
	private final String SERVERPORT_COLUMN= "SVR_PORT";
	
	private final String IP_ADDRESS= "IP_ADDRESS";
	
	private final String COMPLETE_URL = "COMPLETE_URL";
	
	private final String ID_COLUMN= "ID";
	
	private final String SELECT_SYSTEM_CONFIGURATIONS_BY_SETTING_TYPE= "SELECT * FROM "+TABLE_NAME+ " WHERE "+COLUMN_NAME_SETTING+" = ? " ;
	
	private final String UPDATE_BY_SETTING_TYPE = "UPDATE "+TABLE_NAME+" SET "+PASSWD_COLUMN+" = ? , "+USERNAME_COLUMN+" = ? , "+SERVERNAME_COLUMN+" = ? , "+IP_ADDRESS+" = ? , "+
			SERVERPORT_COLUMN+" = ? , "+ COMPLETE_URL +"= ? WHERE "+COLUMN_NAME_SETTING+" = ? AND "+ID_COLUMN +" = ?";
	
	private final String INSERT_SYSTEM_CONFIGURATION = "INSERT INTO "+TABLE_NAME+"( "+COLUMN_NAME_SETTING+" , " +IP_ADDRESS+" , "+SERVERNAME_COLUMN+" , "+USERNAME_COLUMN+" , "
	+PASSWD_COLUMN+" , "+SERVERPORT_COLUMN+" , "+COMPLETE_URL +" ) VALUES (?,?,?,?,?,?,?) ";
	
	private JdbcTemplate jdbcTemplate;  

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Server_details getFedoraServerConfiguration() {
		Server_details fedoraConf = null;
		try {
			fedoraConf = jdbcTemplate.queryForObject(SELECT_SYSTEM_CONFIGURATIONS_BY_SETTING_TYPE,new Object[] {SETTING_TYPE.FEDORA_SERVER.toString()},
					new BeanPropertyRowMapper<>(Server_details.class));
		}  catch( EmptyResultDataAccessException e) {
			fedoraConf = null;
		}
		return fedoraConf;
	}

	@Override
	public Server_details getSMTPServerConfiguration() {
		Server_details SMTPConf = null;
		try {
			SMTPConf = jdbcTemplate.queryForObject(SELECT_SYSTEM_CONFIGURATIONS_BY_SETTING_TYPE,new Object[] {SETTING_TYPE.SMTP_SERVER.toString()},new BeanPropertyRowMapper<>(Server_details.class));
		}  catch( EmptyResultDataAccessException e) {
			SMTPConf = null;
		}
		return SMTPConf;
	}

	@Override
	public Server_details saveFedoraServerConf(Server_details configuration) {
		Server_details oldConf = getFedoraServerConfiguration();
		Server_details newConf = null;
		int result ;
		if(oldConf != null) {
			result = jdbcTemplate.update(UPDATE_BY_SETTING_TYPE, configuration.getSvr_passwd(),configuration.getSvr_username(),configuration.getSvr_name(),configuration.getIp_address(),
					configuration.getSvr_port(),configuration.getComplete_url(),SETTING_TYPE.FEDORA_SERVER.toString(),oldConf.getId());		
		} else {
			result = jdbcTemplate.update(  INSERT_SYSTEM_CONFIGURATION,  
					new Object[] { SETTING_TYPE.FEDORA_SERVER.toString(), configuration.getIp_address(), configuration.getSvr_name(), configuration.getSvr_username(), configuration.getSvr_passwd(),
							configuration.getSvr_port(), configuration.getComplete_url()  });  	  			
		}
		if(result > 0 ) {
			newConf = getFedoraServerConfiguration();
		}
		return newConf;
	}

	@Override
	public Server_details saveSMTPServerConf(Server_details configuration) {
		Server_details oldConf = getSMTPServerConfiguration();
		Server_details newConf = null;
		int result ;
		if(oldConf != null){
			result = jdbcTemplate.update(UPDATE_BY_SETTING_TYPE, configuration.getSvr_passwd(),configuration.getSvr_username(),configuration.getSvr_name(),configuration.getIp_address(),
					configuration.getSvr_port(),configuration.getComplete_url(),SETTING_TYPE.SMTP_SERVER.toString(),oldConf.getId());
		} else {
			result = jdbcTemplate.update(  INSERT_SYSTEM_CONFIGURATION,  
					new Object[] { SETTING_TYPE.SMTP_SERVER.toString(), configuration.getIp_address(), configuration.getSvr_name(), configuration.getSvr_username(), configuration.getSvr_passwd(),
							configuration.getSvr_port(), configuration.getComplete_url() });  	  
		}
		if(result > 0 ){
			newConf = getSMTPServerConfiguration();
		}
		return newConf;
	}

	@Override
	public Server_details getFusekiServerConfiguration() {
		Server_details FusekiConf = null;
		try {
			FusekiConf = jdbcTemplate.queryForObject(SELECT_SYSTEM_CONFIGURATIONS_BY_SETTING_TYPE,new Object[] {SETTING_TYPE.FUSEKI_SERVER.toString()},new BeanPropertyRowMapper<>(Server_details.class));
		}  catch( EmptyResultDataAccessException e) {
			FusekiConf = null;
		}
		return FusekiConf;
	}

	@Override
	public Server_details saveFusekiServerConf(Server_details configuration) {
		Server_details oldConf = getFusekiServerConfiguration();
		Server_details newConf = null;
		int result ;
		if(oldConf != null){
			result = jdbcTemplate.update(UPDATE_BY_SETTING_TYPE, configuration.getSvr_passwd(),configuration.getSvr_username(),configuration.getSvr_name(),configuration.getIp_address(),
					configuration.getSvr_port(),configuration.getComplete_url(),SETTING_TYPE.FUSEKI_SERVER.toString(),oldConf.getId());
		} else {
			result = jdbcTemplate.update(  INSERT_SYSTEM_CONFIGURATION,  
					new Object[] { SETTING_TYPE.FUSEKI_SERVER.toString(), configuration.getIp_address(), configuration.getSvr_name(), configuration.getSvr_username(), configuration.getSvr_passwd(),
							configuration.getSvr_port(), configuration.getComplete_url() });  	  
		}
		if(result > 0 ){
			newConf = getFusekiServerConfiguration();
		}
		return newConf;
	}

}
