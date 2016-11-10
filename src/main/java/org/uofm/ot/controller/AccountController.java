package org.uofm.ot.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import  org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.uofm.ot.AppSecurityConfig;
import org.uofm.ot.CustomizedUserManager;
import org.uofm.ot.dao.UserDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.OTUser;
import org.uofm.ot.model.User;

import com.google.gson.Gson;




@Controller
public class AccountController {

	private UserDAO userDao;
	
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}
	


	@Autowired
	private CustomizedUserManager userDetailService;
	
	
	private static final Logger logger = Logger.getLogger(AccountController.class);
	
	
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST,consumes ={MediaType.APPLICATION_JSON_VALUE}, produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> saveUser( @RequestBody String string ,  @ModelAttribute("loggedInUser") OTUser loggedInUser , HttpSession httpSession) {	
		
		ResponseEntity<String> resultEntity = checkAccessRights(loggedInUser); 

		if(resultEntity == null) {
		
				Gson gson = new Gson();
				User user = gson.fromJson(string, User.class);

				User updatedUser = userDao.updateUser(user);

				if(updatedUser != null ) {				
					String result = gson.toJson(updatedUser);
					if(loggedInUser.getProfile().getId() == user.getId()) {
						httpSession.removeAttribute("DBUser");
						httpSession.setAttribute("DBUser", updatedUser);
					}
					resultEntity =  new ResponseEntity<String>( result , HttpStatus.OK) ;
				} else {
					resultEntity = new ResponseEntity<String>("Unable to update user with ID "+user.getId() , HttpStatus.INTERNAL_SERVER_ERROR) ;
				} 
		}

		return resultEntity ; 
	}
	
	/*@RequestMapping(value = "/getAllUsers", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<User>> getAllUsers( @ModelAttribute("loggedInUser") OTUser loggedInUser) {
		ResponseEntity<List<User>> resultEntity = null;
		if(loggedInUser != null) {
			if(loggedInUser.isInformatician() ) {
				List<User> users = userDao.getAllUsers();
				resultEntity =  new ResponseEntity<List<User>>( users, HttpStatus.OK) ;
			} else {
				resultEntity = new ResponseEntity<List<User>> ( HttpStatus.FORBIDDEN) ;
			}
		} else {
			resultEntity = new ResponseEntity<List<User>> ( HttpStatus.UNAUTHORIZED) ;
		}
		return resultEntity ; 
	}*/
	
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes ={MediaType.APPLICATION_JSON_VALUE}, produces= {MediaType.APPLICATION_JSON_VALUE})
	public  ResponseEntity<String>  addUser(@RequestBody String string ,  @ModelAttribute("loggedInUser") OTUser loggedInUser) {
		ResponseEntity<String> resultEntity = checkAccessRights(loggedInUser); 

		if(resultEntity == null) {
			Gson gson = new Gson();
			User user = gson.fromJson(string, User.class);

			try {
				User savedUser = userDao.addNewUser(user);
				String result = gson.toJson(savedUser);
				resultEntity = new ResponseEntity<String>( result , HttpStatus.OK) ;
			} catch(ObjectTellerException ex){
				logger.error(ex.getMessage());
				resultEntity = new ResponseEntity<String>(  ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) ;
			} catch ( DataAccessException ex) {
				logger.error(ex.getMessage());
				resultEntity = new ResponseEntity<String>(  ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) ;
			}
		}

		return resultEntity ; 
	}
	
	@RequestMapping(value = "/deleteUser/{userID}", method = RequestMethod.DELETE )
	public  ResponseEntity<String>  deleteUser( @PathVariable int userID,  @ModelAttribute("loggedInUser") OTUser loggedInUser) {
		ResponseEntity<String> resultEntity = checkAccessRights(loggedInUser); 

		if(resultEntity == null) { 
			if(userID != loggedInUser.getProfile().getId()){
				try {
					userDao.deleteUser(userID);
					resultEntity = new ResponseEntity<String>( HttpStatus.OK) ;
				} catch(ObjectTellerException ex){
					logger.error(ex.getMessage());
					resultEntity = new ResponseEntity<String>(  ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) ;
				}
			} else {
				resultEntity = new ResponseEntity<String>("Please ask system administrator to delete your account. You can not delete it yourself. ", HttpStatus.FORBIDDEN) ;
			}
			
		}

		return resultEntity ; 
	}
	
	/**
	 * @param loggedInUser
	 * @return returns null if user has required access rights 
	 */
	private ResponseEntity<String> checkAccessRights(OTUser loggedInUser){
		ResponseEntity<String> resultEntity = null; 
		if(loggedInUser != null) {
			if(loggedInUser.isInformatician() ) {
				
			} else {
				resultEntity = new ResponseEntity<String> ( "You do not have permission to update user information. Please contact your system administrator.", HttpStatus.FORBIDDEN) ;
			}
		} else {
			resultEntity = new ResponseEntity<String> ( "Please log in to update user information.", HttpStatus.UNAUTHORIZED) ; 
		}
		
		return resultEntity ; 
	}
	

	// TODO: hasRole or hasAuthority 
	@RequestMapping(value = {"/user","/getAllUsers"}, method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<OTUser>> getAllUsers( ) {
		ResponseEntity<List<OTUser>> resultEntity = null;

		List<OTUser> users = userDetailService.getUsers();
		resultEntity =  new ResponseEntity<List<OTUser>>( users, HttpStatus.OK) ;

		return resultEntity ; 
	}
}
