package org.uofm.ot.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.uofm.ot.dao.UserDAO;
import org.uofm.ot.model.User;
import org.uofm.ot.ui.util.Menu;



@Controller
public class AccountController {

	private UserDAO userDao;
	
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@RequestMapping(value = "/viewAccount", method = RequestMethod.GET)
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
	}
	
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public String saveUser( @Valid  User user,  BindingResult bindingResult, HttpSession session, ModelMap model ) {	
	
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.ACCOUNT.getName());
		model.addAttribute("ActiveSubTab", Menu.LHAccountMenuOptions.EDIT.getName());
		
		if(!bindingResult.hasErrors()) {
			User updatedUser = userDao.updatePassword(user.getUsername(), user.getPasswd());
			
			if(updatedUser != null ) {				
				session.setAttribute("DBUser", updatedUser);
				model.addAttribute("UserSaved",true);
				model.addAttribute("SuccessMessage","Successfully changed the password.");
			}
		}
		
		return "account/editAccount";
	}
}
