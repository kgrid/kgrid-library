package org.uofm.ot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.model.LibrarySetting;
import org.uofm.ot.model.SETTING_TYPE;
import org.uofm.ot.model.Server_details;
import org.uofm.ot.model.User;
import org.uofm.ot.model.UserRoles;

import com.google.gson.Gson;

import java.util.Properties;

@RestController
public class SystemConfController {
	
	private SystemConfigurationDAO sysConfDao;
	
	

	public void setSysConfDao(SystemConfigurationDAO sysConfDao) {
		this.sysConfDao = sysConfDao;
	}

	
	@RequestMapping(value = "/getLibSettings", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> getLibSettings( @ModelAttribute("loggedInUser") User loggedInUser) {
		ResponseEntity<String> resultEntity = null;

		if(loggedInUser != null ) {

			if(loggedInUser.getRole() == UserRoles.ADMIN) {
				Server_details fedoraConf = sysConfDao.getFedoraServerConfiguration();
				if(fedoraConf == null){
					fedoraConf = new Server_details();
					fedoraConf.setSvr_passwd("Not Configured");
					fedoraConf.setSvr_username("Not Configured");
					fedoraConf.setComplete_url("Not Configured");
					fedoraConf.setIp_address("Not Configured");
				}

				Server_details SMTPConf = sysConfDao.getSMTPServerConfiguration();
				if(SMTPConf == null){
					SMTPConf = new Server_details();
					SMTPConf.setSvr_passwd("Not Configured");
					SMTPConf.setSvr_username("Not Configured");
					SMTPConf.setSvr_name("Not Configured");
					SMTPConf.setIp_address("Not Configured");
				}

				Server_details fusekiConf = sysConfDao.getFusekiServerConfiguration();
				if(fusekiConf == null){
					fusekiConf = new Server_details();
					fusekiConf.setComplete_url("Not Configured");
				}

				LibrarySetting result = new LibrarySetting();
				result.setFedoraConfig(fedoraConf);
				result.setFusekiConfig(fusekiConf);
				result.setSMTPConfig(SMTPConf);

				Gson gson = new Gson();
				String json = gson.toJson(result);

				resultEntity = new ResponseEntity<String>(json, HttpStatus.OK);
			} else {
				resultEntity = new ResponseEntity<String>( "You do not have permission to access server settings. Please contact your system administrator.", HttpStatus.FORBIDDEN);
			} 
		} else {
			resultEntity = new ResponseEntity<String>( "Please log in to access server settings", HttpStatus.UNAUTHORIZED);
		} 
		
		return resultEntity;
	}
	
	@RequestMapping(value = "/saveLibSettings", method = RequestMethod.POST,consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> saveLibSettings(@RequestBody String content, @ModelAttribute("loggedInUser") User loggedInUser) {
		ResponseEntity<String> resultEntity = null ;
		if(loggedInUser != null) {
			if(loggedInUser.isAdmin() ) {
				Gson gson = new Gson();

				LibrarySetting newSettings= gson.fromJson(content, LibrarySetting.class);


				Server_details fedoraConf = newSettings.getFedoraConfig();
				fedoraConf.setSetting_type(SETTING_TYPE.FEDORA_SERVER);
				Server_details newFedoraConf  = sysConfDao.saveFedoraServerConf(fedoraConf);


				Server_details fusekiConf = newSettings.getFusekiConfig();
				fusekiConf.setSetting_type(SETTING_TYPE.FUSEKI_SERVER);
				Server_details newFusekiCof = sysConfDao.saveFusekiServerConf(fusekiConf);


				Server_details SMTPConf = newSettings.getSMTPConfig();
				SMTPConf.setSetting_type(SETTING_TYPE.SMTP_SERVER);
				Server_details newSMTPConf = sysConfDao.saveSMTPServerConf(SMTPConf);



				LibrarySetting result = new LibrarySetting();
				result.setFedoraConfig(newFedoraConf);
				result.setFusekiConfig(newFusekiCof);
				result.setSMTPConfig(newSMTPConf);


				String json = gson.toJson(result);

				resultEntity = new ResponseEntity<String>(json, HttpStatus.OK);
			} else 
				resultEntity = new ResponseEntity<String>("You do not have permission to save server settings. Please contact your system administrator.", HttpStatus.FORBIDDEN);
		} else {
			resultEntity = new ResponseEntity<String>("Please log in to save server settings", HttpStatus.UNAUTHORIZED);
		}
		return resultEntity ;
	}

	@Value("${spring.profiles.active}")
	private String spring_profiles_active;

	@GetMapping("/config")
	public Properties getConfigurationProps() {

		Properties properties = new Properties();

		properties.setProperty("spring.profiles.active", spring_profiles_active);

		return properties;
	}




}
	

