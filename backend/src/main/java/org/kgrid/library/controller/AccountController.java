package org.kgrid.library.controller;

import org.kgrid.library.model.LibraryUser;
import org.apache.log4j.Logger;
import org.kgrid.library.CustomizedUserManager;
import org.kgrid.library.exception.LibraryException;
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

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;




@Controller
public class AccountController {

	@Autowired
	private CustomizedUserManager userDetailService;
	
	
	private static final Logger logger = Logger.getLogger(AccountController.class);
	
	
	@DeleteMapping(value = "/user/{userID}")
	public  ResponseEntity<String>  deleteUser( @PathVariable int userID,  @ModelAttribute("loggedInUser") LibraryUser loggedInUser) {
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
	private ResponseEntity<String> checkAccessRights(LibraryUser loggedInUser){
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
	public ResponseEntity<List<LibraryUser>> getAllUsers( ) {
		ResponseEntity<List<LibraryUser>> resultEntity = null;

		List<LibraryUser> users = userDetailService.getUsers();
		resultEntity =  new ResponseEntity<List<LibraryUser>>( users, HttpStatus.OK) ;

		return resultEntity ; 
	}

	
	// TODO: hasRole or hasAuthority 
	// TODO: Check which HTTPStatus to throw instead of HttpStatus.INTERNAL_SERVER_ERROR
	@PostMapping(value = {"/user","/addUser"}, 
			consumes ={MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('INFORMATICIAN') OR hasAuthority('ADMIN')")
	public  ResponseEntity<LibraryUser>  createNewUser(@RequestBody LibraryUserDTO dto ) {
		ResponseEntity<LibraryUser> resultEntity = null;
		
		try {
			LibraryUser libraryUser = convertLibraryUserDTOToLibraryUser(dto);
			userDetailService.createUser(libraryUser);
			LibraryUser result = userDetailService.loadUserByUsername(libraryUser.getUsername());
			resultEntity = new ResponseEntity<LibraryUser>( result , HttpStatus.OK) ;
		} catch(LibraryException ex){
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<LibraryUser>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		} catch ( DataAccessException ex) {
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<LibraryUser>( HttpStatus.INTERNAL_SERVER_ERROR) ;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<LibraryUser>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		}

		return resultEntity;
	}
	
	private LibraryUser convertLibraryUserDTOToLibraryUser(LibraryUserDTO dto) {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(dto.getRole());
		ArrayList<GrantedAuthority> list =  new ArrayList<GrantedAuthority>();
		list.add(authority);
		LibraryUser libraryUser = new LibraryUser(dto.getUsername(), dto.getPassword(), list , dto.getProfile());
		return libraryUser;
	}
		
	// TODO: hasRole or hasAuthority 
	@PutMapping(value = "/user/{id}", 
				consumes ={MediaType.APPLICATION_JSON_VALUE}, 
				produces= {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('INFORMATICIAN') OR hasAuthority('ADMIN')")
	public ResponseEntity<LibraryUser> updateUser(@RequestBody LibraryUserDTO dto , @PathVariable int id, @ModelAttribute("loggedInUser") LibraryUser loggedInUser, HttpSession httpSession) {

		ResponseEntity<LibraryUser> resultEntity = null;
		

		try {

			LibraryUser libraryUser = convertLibraryUserDTOToLibraryUser(dto);

			String username = userDetailService.getUsernameById(id);
			if(username != null ) { 
				if(! username.equals(dto.getUsername())) {
					userDetailService.updateUserName(username, dto.getUsername());
				}
				userDetailService.updateUser(libraryUser);
				LibraryUser result = userDetailService.loadUserByUsername(libraryUser.getUsername());
				if(loggedInUser.getProfile().getId() == libraryUser.getId()) {
					httpSession.removeAttribute("DBUser");
					httpSession.setAttribute("DBUser", result);
				}
				resultEntity = new ResponseEntity<LibraryUser>( result , HttpStatus.OK) ;
			}  else {
				logger.error("User with id "+id+" does not exist. ");
				resultEntity = new ResponseEntity<LibraryUser>(  HttpStatus.NOT_FOUND) ;
			}

		} catch(Exception ex) {
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<LibraryUser>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		}

		return resultEntity ; 
	}
	
	@GetMapping(value="user/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<LibraryUser> getLoggedInUser(){

		LibraryUser loggedInUser = null ;
		
		Authentication currentUser = SecurityContextHolder.getContext()
				.getAuthentication(); 
		
		loggedInUser =  (LibraryUser) currentUser.getPrincipal();
		
		/*if(currentUser != null) {
			if( ! "anonymousUser".equals(currentUser.getName())) {
 
				loggedInUser = userDetailService.loadUserByUsername(currentUser.getName());
			}
		}*/
		
		ResponseEntity<LibraryUser> entity = new ResponseEntity<LibraryUser> (loggedInUser , HttpStatus.OK);
		
		return entity ; 
				
	}
}
