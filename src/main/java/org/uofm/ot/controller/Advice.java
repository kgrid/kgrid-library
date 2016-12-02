package org.uofm.ot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.uofm.ot.CustomizedUserManager;
import org.uofm.ot.model.OTUser;

@ControllerAdvice
public class Advice {
	
	@Autowired
	private CustomizedUserManager userDetailService;
	
	@ModelAttribute("loggedInUser")
	public OTUser populateUser(HttpSession session) {
		Authentication currentUser = SecurityContextHolder.getContext()
				.getAuthentication(); 
		
		OTUser loggedInUser = null;
		if(currentUser != null) {
			if( ! "anonymousUser".equals(currentUser.getName())) {
 
				loggedInUser = userDetailService.loadUserByUsername(currentUser.getName());
				
				session.setAttribute("DBUser", loggedInUser);

			}
		}
		
		return loggedInUser;
	}

}
