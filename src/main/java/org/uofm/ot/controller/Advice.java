package org.uofm.ot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.uofm.ot.model.User;

@ControllerAdvice
public class Advice {
	
	
	@ModelAttribute("loggedInUser")
	public User populateUser(HttpSession session) {
		User loggedInUser = (User ) session.getAttribute("DBUser");
		return loggedInUser;
	}

}
