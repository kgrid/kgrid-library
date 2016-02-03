package org.uofm.ot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.model.SystemConfiguration;
import org.uofm.ot.ui.util.Menu;

@Controller
public class SystemConfController {
	
	private SystemConfigurationDAO sysConfDao;
	
	

	public void setSysConfDao(SystemConfigurationDAO sysConfDao) {
		this.sysConfDao = sysConfDao;
	}



	@RequestMapping(value = "/viewSystemConf", method = RequestMethod.GET)
	public String view(  ModelMap model) {	
	
		SystemConfiguration fedoraConf = sysConfDao.getFedoraServerConfiguration();
		if(fedoraConf == null){
			fedoraConf = new SystemConfiguration();
			fedoraConf.setPasswd("Not Configured");
			fedoraConf.setUsername("Not Configured");
			fedoraConf.setServer_name("Not Configured");
			fedoraConf.setIp_address("Not Configured");
		}
		
		SystemConfiguration SMTPConf = sysConfDao.getSMTPServerConfiguration();
		if(SMTPConf == null){
			SMTPConf = new SystemConfiguration();
			SMTPConf.setPasswd("Not Configured");
			SMTPConf.setUsername("Not Configured");
			SMTPConf.setServer_name("Not Configured");
			SMTPConf.setIp_address("Not Configured");
		}
		
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.SYSTEM.getName());
		model.addAttribute("ActiveSubTab", Menu.LHSystemMenuOptions.VIEW.getName());
		model.addAttribute("fedoraConf",fedoraConf);
		model.addAttribute("SMTPConf",SMTPConf);
		
		
		
		return "system/viewConfiguration";
	}
	
	
	@RequestMapping(value = "/editSystemConf", method = RequestMethod.GET)
	public String edit( SystemConfiguration fedoraConf, BindingResult bindingResult , ModelMap model) {	
	
		SystemConfiguration FEDORAConf = sysConfDao.getFedoraServerConfiguration();
		if(FEDORAConf == null){
			FEDORAConf = new SystemConfiguration();
			FEDORAConf.setPasswd("Not Configured");
			FEDORAConf.setUsername("Not Configured");
			FEDORAConf.setServer_name("Not Configured");
			FEDORAConf.setIp_address("Not Configured");
		}
		
		SystemConfiguration SMTPConf = sysConfDao.getSMTPServerConfiguration();
		if(SMTPConf == null){
			SMTPConf = new SystemConfiguration();
			SMTPConf.setPasswd("Not Configured");
			SMTPConf.setUsername("Not Configured");
			SMTPConf.setServer_name("Not Configured");
			SMTPConf.setIp_address("Not Configured");
		}
		
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.SYSTEM.getName());
		model.addAttribute("ActiveSubTab", Menu.LHSystemMenuOptions.EDIT.getName());
		model.addAttribute("fedoraConf",FEDORAConf);
		model.addAttribute("SMTPConf",SMTPConf);
		
		
		
		return "system/editConfiguration";
	}
	
	
	@RequestMapping(value = "/saveFedoraServerConf", method = RequestMethod.POST)
	public String saveFedoraServerConf( SystemConfiguration fedoraConf, BindingResult bindingResult ,  ModelMap model) {
		
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.SYSTEM.getName());
		model.addAttribute("ActiveSubTab", Menu.LHSystemMenuOptions.EDIT.getName());
		
		SystemConfiguration newFedoraConf  = sysConfDao.saveFedoraServerConf(fedoraConf);
		model.addAttribute("fedoraConf",newFedoraConf);
		
		SystemConfiguration SMTPConf = sysConfDao.getSMTPServerConfiguration();
		if(SMTPConf == null){
			SMTPConf = new SystemConfiguration();
			SMTPConf.setPasswd("Not Configured");
			SMTPConf.setUsername("Not Configured");
			SMTPConf.setServer_name("Not Configured");
			SMTPConf.setIp_address("Not Configured");
		}
		model.addAttribute("SMTPConf",SMTPConf);

		return "system/editConfiguration";
	}
	
	@RequestMapping(value = "/saveSMTPServerConf", method = RequestMethod.POST)
	public String saveSMTPServerConf( SystemConfiguration SMTPConf, BindingResult bindingResult ,  ModelMap model) {
		
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.SYSTEM.getName());
		model.addAttribute("ActiveSubTab", Menu.LHSystemMenuOptions.EDIT.getName());
		
		SystemConfiguration newSMTPConf  = sysConfDao.saveSMTPServerConf(SMTPConf);
		model.addAttribute("SMTPConf",newSMTPConf);
		
		SystemConfiguration fedoraConf = sysConfDao.getFedoraServerConfiguration();
		if(fedoraConf == null){
			fedoraConf = new SystemConfiguration();
			fedoraConf.setPasswd("Not Configured");
			fedoraConf.setUsername("Not Configured");
			fedoraConf.setServer_name("Not Configured");
			fedoraConf.setIp_address("Not Configured");
		}
		model.addAttribute("fedoraConf",fedoraConf);

		return "system/editConfiguration";
	}
}
