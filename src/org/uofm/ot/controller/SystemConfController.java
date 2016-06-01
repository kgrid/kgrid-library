package org.uofm.ot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.LibrarySetting;
import org.uofm.ot.model.SETTING_TYPE;
import org.uofm.ot.model.Server_details;
import org.uofm.ot.transferobjects.InputObject;
import org.uofm.ot.transferobjects.Result;
import org.uofm.ot.ui.util.Menu;

import com.google.gson.Gson;

@RestController
public class SystemConfController {
	
	private SystemConfigurationDAO sysConfDao;
	
	

	public void setSysConfDao(SystemConfigurationDAO sysConfDao) {
		this.sysConfDao = sysConfDao;
	}

	
	@RequestMapping(value = "/getLibSettings", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> testMethod() {
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

		
		
		return new ResponseEntity<String>(json, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveLibSettings", method = RequestMethod.POST,consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> saveLibSettings(@RequestBody String content) {
		
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

		return new ResponseEntity<String>(json, HttpStatus.OK);
	}
}
