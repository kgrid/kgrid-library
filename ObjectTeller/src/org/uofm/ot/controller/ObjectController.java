package org.uofm.ot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.uofm.ot.fedoraAccessLayer.FedoraObject;
import org.uofm.ot.fedoraAccessLayer.FedoraObjectService;
import org.uofm.ot.ui.util.Menu;

@Controller
public class ObjectController {
	
	private FedoraObjectService fedoraObjectService;

	public void setFedoraObjectService(FedoraObjectService fedoraObjectService) {
		this.fedoraObjectService = fedoraObjectService;
	}
	
	@RequestMapping(value = "/object.{objectURI}", method = RequestMethod.GET)
	public String getObject( @PathVariable String objectURI, ModelMap model) {
		String view = "objects/objectInfo";
		FedoraObject object = fedoraObjectService.getObject(objectURI);
		
		model.addAttribute("fedoraObject",object);
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("ActiveSubTab", Menu.LHSingleObjectMenuOptions.SUMMARY.getName());
		model.addAttribute("PageType", "Single");
		return view;
	}
}
