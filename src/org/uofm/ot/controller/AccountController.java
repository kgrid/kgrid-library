package org.uofm.ot.controller;

import java.util.List;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.uofm.ot.dao.UserDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.User;
import org.uofm.ot.ui.util.Menu;

import com.google.gson.Gson;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;




@Controller
public class AccountController {

	private UserDAO userDao;
	
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	private static final Logger logger = Logger.getLogger(AccountController.class);
	
	/*@RequestMapping(value = "/viewAccount", method = RequestMethod.GET)
	public String view(  ModelMap model) {	
	
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.ACCOUNT.getName());
		model.addAttribute("ActiveSubTab", Menu.LHAccountMenuOptions.VIEW.getName());
		
		return "account/viewAccount";
	}
	
	@RequestMapping(value = "/editAccount", method = RequestMethod.GET)
	public String editAccount( @Valid User user, BindingResult bindingResult ,HttpSession session,ModelMap model ) {	
		
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.ACCOUNT.getName());
		model.addAttribute("ActiveSubTab", Menu.LHAccountMenuOptions.EDIT.getName());
		User dBUser = (User) session.getAttribute("DBUser");
		model.addAttribute("user",dBUser);
		
		return "account/editAccount";
	}*/
	
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST,consumes ={MediaType.APPLICATION_JSON_VALUE}, produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> saveUser( @RequestBody String string ) {	

		Gson gson = new Gson();
		User user = gson.fromJson(string, User.class);

		User updatedUser = userDao.updateUser(user);

		if(updatedUser != null ) {				
			String result = gson.toJson(updatedUser);
			return new ResponseEntity<String>( result , HttpStatus.OK) ;
		} else {
			return new ResponseEntity<String>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
		
	}
	
	@RequestMapping(value = "/getAllUsers", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<User>> getAllUsers() {
		
		List<User> users = userDao.getAllUsers();
		return new ResponseEntity<List<User>>( users, HttpStatus.OK) ;
	}
	
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes ={MediaType.APPLICATION_JSON_VALUE}, produces= {MediaType.APPLICATION_JSON_VALUE})
	public  ResponseEntity<String>  addUser(@RequestBody String string) {
		System.out.println("Inside add user "+string);
		Gson gson = new Gson();
		User user = gson.fromJson(string, User.class);
		
		try {
			User savedUser = userDao.addNewUser(user);
			String result = gson.toJson(savedUser);
			return new ResponseEntity<String>( result , HttpStatus.OK) ;
		} catch(ObjectTellerException ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<String>(  ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) ;
		} catch (MySQLIntegrityConstraintViolationException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<String>(  ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
	
	@RequestMapping(value = "/deleteUser/{userID}", method = RequestMethod.DELETE )
	public  ResponseEntity<String>  deleteUser( @PathVariable int userID) {
		System.out.println("Inside delete user "+userID);
		
		
		try {
			 userDao.deleteUser(userID);
			return new ResponseEntity<String>( HttpStatus.OK) ;
		} catch(ObjectTellerException ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<String>(  ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) ;
		}
	}
}
