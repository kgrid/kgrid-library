package edu.umich.lhs.library.controller;

import edu.umich.lhs.library.model.LibraryUser;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import edu.umich.lhs.library.CustomizedUserManager;

@ControllerAdvice
public class Advice {
	
	@Autowired
	private CustomizedUserManager userDetailService;
	
	@ModelAttribute("loggedInUser")
	public LibraryUser populateUser(HttpSession session) {
		Authentication currentUser = SecurityContextHolder.getContext()
				.getAuthentication();

		LibraryUser loggedInUser = null;
		if(currentUser != null) {
			if( ! "anonymousUser".equals(currentUser.getName())) {
 
				loggedInUser = userDetailService.loadUserByUsername(currentUser.getName());
				
				session.setAttribute("DBUser", loggedInUser);

			}
		}
		
		return loggedInUser;
	}

}
