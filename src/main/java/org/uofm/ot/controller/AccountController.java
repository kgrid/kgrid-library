package org.uofm.ot.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.uofm.ot.CustomizedUserManager;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.OTUser;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;




@Controller
public class AccountController {

	@Autowired
	private CustomizedUserManager userDetailService;
	
	
	private static final Logger logger = Logger.getLogger(AccountController.class);
	
	
	@DeleteMapping(value = "/user/{userID}")
	public  ResponseEntity<String>  deleteUser( @PathVariable int userID,  @ModelAttribute("loggedInUser") OTUser loggedInUser) {
		ResponseEntity<String> resultEntity = checkAccessRights(loggedInUser); 

		if(resultEntity == null) { 
			if(userID != loggedInUser.getProfile().getId()){
				String username = userDetailService.getUsernameById(userID);
				userDetailService.deleteUser(username);
				resultEntity = new ResponseEntity<String>( HttpStatus.OK) ;
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
	

	// TODO: Add INFORMATICIAN in hasAuthority 
	@PreAuthorize("hasAuthority('INFORMATICIAN') OR hasAuthority('ADMIN')")
	@GetMapping(value = {"/user"}
		,produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<OTUser>> getAllUsers( ) {
		ResponseEntity<List<OTUser>> resultEntity = null;

		List<OTUser> users = userDetailService.getUsers();
		resultEntity =  new ResponseEntity<List<OTUser>>( users, HttpStatus.OK) ;

		return resultEntity ; 
	}

	
	// TODO: hasRole or hasAuthority 
	// TODO: Check which HTTPStatus to throw instead of HttpStatus.INTERNAL_SERVER_ERROR
	@PostMapping(value = {"/user","/addUser"}, 
			consumes ={MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('INFORMATICIAN') OR hasAuthority('ADMIN')")
	public  ResponseEntity<OTUser>  createNewUser(@RequestBody OTUserDTO dto ) {
		ResponseEntity<OTUser> resultEntity = null;
		
		try {
			OTUser otUser = convertOTUserDTOToOTUser(dto); 
			userDetailService.createUser(otUser);
			OTUser result = userDetailService.loadUserByUsername(otUser.getUsername());
			resultEntity = new ResponseEntity<OTUser>( result , HttpStatus.OK) ;
		} catch(ObjectTellerException ex){
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<OTUser>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		} catch ( DataAccessException ex) {
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<OTUser>( HttpStatus.INTERNAL_SERVER_ERROR) ;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<OTUser>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		}

		return resultEntity;
	}
	
	private OTUser convertOTUserDTOToOTUser (OTUserDTO dto) {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(dto.getRole());
		ArrayList<GrantedAuthority> list =  new ArrayList<GrantedAuthority>();
		list.add(authority);
		OTUser otUser = new OTUser(dto.getUsername(), dto.getPassword(), list , dto.getProfile());
		return otUser;
	}
		
	// TODO: hasRole or hasAuthority 
	@PutMapping(value = "/user/{id}", 
				consumes ={MediaType.APPLICATION_JSON_VALUE}, 
				produces= {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('INFORMATICIAN') OR hasAuthority('ADMIN')")
	public ResponseEntity<OTUser> updateUser(@RequestBody OTUserDTO dto , @PathVariable int id, @ModelAttribute("loggedInUser") OTUser loggedInUser, HttpSession httpSession) {	

		ResponseEntity<OTUser> resultEntity = null;
		

		try { 

			OTUser otUser = convertOTUserDTOToOTUser(dto); 

			String username = userDetailService.getUsernameById(id);
			if(username != null ) { 
				if(! username.equals(dto.getUsername())) {
					userDetailService.updateUserName(username, dto.getUsername());
				}
				userDetailService.updateUser(otUser);
				OTUser result = userDetailService.loadUserByUsername(otUser.getUsername());
				if(loggedInUser.getProfile().getId() == otUser.getId()) {
					httpSession.removeAttribute("DBUser");
					httpSession.setAttribute("DBUser", result);
				}
				resultEntity = new ResponseEntity<OTUser>( result , HttpStatus.OK) ;
			}  else {
				logger.error("User with id "+id+" does not exist. ");
				resultEntity = new ResponseEntity<OTUser>(  HttpStatus.NOT_FOUND) ;
			}

		} catch(Exception ex) {
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<OTUser>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		}

		return resultEntity ; 
	}
	
	@GetMapping(value="user/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<OTUser> getLoggedInUser(){
		
		OTUser loggedInUser = null ; 
		
		Authentication currentUser = SecurityContextHolder.getContext()
				.getAuthentication(); 
		
		loggedInUser =  (OTUser) currentUser.getPrincipal();
		
		/*if(currentUser != null) {
			if( ! "anonymousUser".equals(currentUser.getName())) {
 
				loggedInUser = userDetailService.loadUserByUsername(currentUser.getName());
			}
		}*/
		
		ResponseEntity<OTUser> entity = new ResponseEntity<OTUser> (loggedInUser , HttpStatus.OK);
		
		return entity ; 
				
	}
}
