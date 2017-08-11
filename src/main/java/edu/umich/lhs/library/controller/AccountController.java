package edu.umich.lhs.library.controller;

import edu.umich.lhs.library.model.libraryUser;
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
import edu.umich.lhs.library.CustomizedUserManager;
import edu.umich.lhs.library.exception.LibraryException;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;




@Controller
public class AccountController {

	@Autowired
	private CustomizedUserManager userDetailService;
	
	
	private static final Logger logger = Logger.getLogger(AccountController.class);
	
	
	@DeleteMapping(value = "/user/{userID}")
	public  ResponseEntity<String>  deleteUser( @PathVariable int userID,  @ModelAttribute("loggedInUser") libraryUser loggedInUser) {
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
	private ResponseEntity<String> checkAccessRights(libraryUser loggedInUser){
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
	public ResponseEntity<List<libraryUser>> getAllUsers( ) {
		ResponseEntity<List<libraryUser>> resultEntity = null;

		List<libraryUser> users = userDetailService.getUsers();
		resultEntity =  new ResponseEntity<List<libraryUser>>( users, HttpStatus.OK) ;

		return resultEntity ; 
	}

	
	// TODO: hasRole or hasAuthority 
	// TODO: Check which HTTPStatus to throw instead of HttpStatus.INTERNAL_SERVER_ERROR
	@PostMapping(value = {"/user","/addUser"}, 
			consumes ={MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('INFORMATICIAN') OR hasAuthority('ADMIN')")
	public  ResponseEntity<libraryUser>  createNewUser(@RequestBody LibraryUserDTO dto ) {
		ResponseEntity<libraryUser> resultEntity = null;
		
		try {
			libraryUser libraryUser = convertLibraryUserDTOToLibraryUser(dto);
			userDetailService.createUser(libraryUser);
			libraryUser result = userDetailService.loadUserByUsername(libraryUser.getUsername());
			resultEntity = new ResponseEntity<libraryUser>( result , HttpStatus.OK) ;
		} catch(LibraryException ex){
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<libraryUser>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		} catch ( DataAccessException ex) {
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<libraryUser>( HttpStatus.INTERNAL_SERVER_ERROR) ;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<libraryUser>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		}

		return resultEntity;
	}
	
	private libraryUser convertLibraryUserDTOToLibraryUser(LibraryUserDTO dto) {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(dto.getRole());
		ArrayList<GrantedAuthority> list =  new ArrayList<GrantedAuthority>();
		list.add(authority);
		libraryUser libraryUser = new libraryUser(dto.getUsername(), dto.getPassword(), list , dto.getProfile());
		return libraryUser;
	}
		
	// TODO: hasRole or hasAuthority 
	@PutMapping(value = "/user/{id}", 
				consumes ={MediaType.APPLICATION_JSON_VALUE}, 
				produces= {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('INFORMATICIAN') OR hasAuthority('ADMIN')")
	public ResponseEntity<libraryUser> updateUser(@RequestBody LibraryUserDTO dto , @PathVariable int id, @ModelAttribute("loggedInUser") libraryUser loggedInUser, HttpSession httpSession) {

		ResponseEntity<libraryUser> resultEntity = null;
		

		try { 

			libraryUser libraryUser = convertLibraryUserDTOToLibraryUser(dto);

			String username = userDetailService.getUsernameById(id);
			if(username != null ) { 
				if(! username.equals(dto.getUsername())) {
					userDetailService.updateUserName(username, dto.getUsername());
				}
				userDetailService.updateUser(libraryUser);
				libraryUser result = userDetailService.loadUserByUsername(libraryUser.getUsername());
				if(loggedInUser.getProfile().getId() == libraryUser.getId()) {
					httpSession.removeAttribute("DBUser");
					httpSession.setAttribute("DBUser", result);
				}
				resultEntity = new ResponseEntity<libraryUser>( result , HttpStatus.OK) ;
			}  else {
				logger.error("User with id "+id+" does not exist. ");
				resultEntity = new ResponseEntity<libraryUser>(  HttpStatus.NOT_FOUND) ;
			}

		} catch(Exception ex) {
			logger.error(ex.getMessage());
			resultEntity = new ResponseEntity<libraryUser>(  HttpStatus.INTERNAL_SERVER_ERROR) ;
		}

		return resultEntity ; 
	}
	
	@GetMapping(value="user/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<libraryUser> getLoggedInUser(){
		
		libraryUser loggedInUser = null ;
		
		Authentication currentUser = SecurityContextHolder.getContext()
				.getAuthentication(); 
		
		loggedInUser =  (libraryUser) currentUser.getPrincipal();
		
		/*if(currentUser != null) {
			if( ! "anonymousUser".equals(currentUser.getName())) {
 
				loggedInUser = userDetailService.loadUserByUsername(currentUser.getName());
			}
		}*/
		
		ResponseEntity<libraryUser> entity = new ResponseEntity<libraryUser> (loggedInUser , HttpStatus.OK);
		
		return entity ; 
				
	}
}
