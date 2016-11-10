package org.uofm.ot.controller;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.dao.UserDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fusekiAccessLayer.FusekiService;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.model.Server_details;
import org.uofm.ot.model.User;
import org.uofm.ot.services.KnowledgeObjectService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author nbahulek
 *
 */
@Controller
public class LoginController {

	private static final Logger logger = Logger.getLogger(LoginController.class);
	
	private UserDAO userDao;
	
	private FusekiService fusekiService;
	
	@Autowired
	private KnowledgeObjectService objectService;
	
	private SystemConfigurationDAO sysConfDao;
	
	public void setSysConfDao(SystemConfigurationDAO sysConfDao) {
		this.sysConfDao = sysConfDao;
	}
		
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

	public void setFusekiService(FusekiService fusekiService) {
		this.fusekiService = fusekiService;
	}
	
	private String getObjectPage( ModelMap model) {
		try {
			List<KnowledgeObject> list = objectService.getKnowledgeObjects(false);
			Integer publishedObjects = objectService.getNumberOfPublishedObjects();
			model.addAttribute("objects", list);
			model.addAttribute("TotalObjects", list.size());
			if(publishedObjects != null)
				model.addAttribute("PublishedObjects",publishedObjects);
			Server_details fedoraServer = sysConfDao.getFedoraServerConfiguration();
			if(fedoraServer != null)
				model.addAttribute("ServerURL", fedoraServer.getComplete_url());
			else
				model.addAttribute("ServerURL", "Not Configured");
			
			if(fedoraServer != null)
				model.addAttribute("FedoraIpAddress", fedoraServer.getIp_address());
			else
				model.addAttribute("FedoraIpAddress", "Not Configured");

			
			model.addAttribute("LibraryName", fusekiService.getLibraryName());
		} catch(ObjectTellerException ex){
			logger.error("Exception occured while retrieving objects "+ex.getCause());
			model.addAttribute("ErrorMessage","Exception occured while retrieving objects "+ex.getCause());
		}
		return "login/home";
	}
	
//	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout( ModelMap model, HttpSession session ){
		session.removeAttribute("DBUser");
		return newLogin(model,session) ; 
	}
	
//	@RequestMapping(value="/login", method=RequestMethod.POST , consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> onlyLogin(@RequestBody String string, HttpSession httpSession) {		
		
		ResponseEntity<String> resultEntity = null;

		Gson gson = new Gson();
		User userObject = gson.fromJson(string, User.class);

		User resUser = userDao.getUserByUsernameAndPassword(userObject.getUsername(), userObject.getPasswd());
		if(resUser != null) {
			httpSession.setAttribute("DBUser", resUser);
			String result = gson.toJson(resUser);

			resultEntity =  new ResponseEntity<String>( result, HttpStatus.OK) ;
		} else {
			resultEntity =  new ResponseEntity<String>( "Unable to login. Please check username and password. ", HttpStatus.UNAUTHORIZED) ;
		}
		
		return resultEntity ; 
	}
	
	/**
	 * @param model
	 * @param httpSession
	 * @return View to render home page
	 */
	@RequestMapping(value={"/home","/"})
	public String newLogin(ModelMap model,  HttpSession httpSession ) {		
	

		if(httpSession.getAttribute("DBUser") == null){

			List<KnowledgeObject> list;
			try {
				list = objectService.getKnowledgeObjects(true);
				model.addAttribute("objects", list);
			} catch (ObjectTellerException ex) {
				logger.error("Exception occured while retrieving objects "+ex.getCause());
				model.addAttribute("ErrorMessage","Exception occured while retrieving objects "+ex.getCause());
			}
			model.addAttribute("loggedInUser", null);

		} else {
			getObjectPage(model);
		}

		return "login/home" ;			
	}
	

	
}
