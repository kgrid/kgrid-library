package org.uofm.ot.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.uofm.ot.dao.UserDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.FedoraObject;
import org.uofm.ot.fedoraAccessLayer.FedoraObjectService;
import org.uofm.ot.model.User;
import org.uofm.ot.ui.util.Menu;




@Controller
public class LoginController {

	private UserDAO userDao;
	
	private FedoraObjectService fedoraObjectService;
	
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

	
	public void setFedoraObjectService(FedoraObjectService fedoraObjectService) {
		this.fedoraObjectService = fedoraObjectService;
	}


	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(  ModelMap model) {
		model.addAttribute("library", "lhs.medicine.umich.edu/sparkproject/library1");
		model.addAttribute("user",new User());
		
		return "login/login";
	}

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String verifyLogin(@Valid User user, BindingResult bindingResult , HttpSession session, ModelMap model) throws ObjectTellerException {
		String view = null;
		if(bindingResult.hasErrors()){
			view = "login/login";
		} else{
			User resUser = userDao.getUserByUsernameAndPassword(user.getUsername(), user.getPasswd());
			if(resUser == null) {
				model.addAttribute("IncorrectLoginErrorMessage", "Incorrect Username or Password. Please try again.");
				view = "login/login";
			}
			else {
				session.setAttribute("DBUser", resUser);
				getObjectPage(model);
				view = "login/home";
			}
		}
		return view;
	}

	@RequestMapping(value = "/objects", method = RequestMethod.GET)
	public String getObjectPage( ModelMap model) {
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("PageType", "List");
					
		ArrayList<FedoraObject> queryObjects = fedoraObjectService.getQueryObjects();
		model.addAttribute("QueryObjects", queryObjects.size());
		
		ArrayList<FedoraObject> resultObjects = fedoraObjectService.getResultObjects();	
		model.addAttribute("ResultObjects", resultObjects.size());
		
		ArrayList<FedoraObject> knowledgeObjects = fedoraObjectService.getKnowledgeObjects();	
		model.addAttribute("KnowledgeObjects", knowledgeObjects.size());
		
		ArrayList<FedoraObject> tailoringObjects = fedoraObjectService.getTailoringObjects();	
		model.addAttribute("TailoringObjects", tailoringObjects.size());
		
		ArrayList<FedoraObject> objects = new ArrayList<FedoraObject>();
		objects.addAll(queryObjects); 
		objects.addAll(resultObjects);
		objects.addAll(knowledgeObjects);
		objects.addAll(tailoringObjects);
		
		model.addAttribute("objects",objects);
		model.addAttribute("TotalObjects", objects.size());
		
		return "login/home";
	}
	
}
