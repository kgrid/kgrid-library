package org.uofm.ot.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.DeleteFedoraResourceService;
import org.uofm.ot.fedoraAccessLayer.EditFedoraObjectService;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.model.OTUser;
import org.uofm.ot.model.User;
import org.uofm.ot.services.KnowledgeObjectService;

@Controller
public class ObjectController {

	private EditFedoraObjectService editFedoraObjectService;

	private DeleteFedoraResourceService deleteFedoraResourceService;
	
	@Autowired
	private KnowledgeObjectService knowledgeObjectService;

	private static final Logger logger = Logger.getLogger(ObjectController.class);


	public void setEditFedoraObjectService(EditFedoraObjectService editFedoraObjectService) {
		this.editFedoraObjectService = editFedoraObjectService;
	}


	public void setDeleteFedoraResourceService(DeleteFedoraResourceService deleteFedoraResourceService) {
		this.deleteFedoraResourceService = deleteFedoraResourceService;
	}


	@RequestMapping(value = "/object/ark:/{naan}/{name}", method = RequestMethod.GET)
	public String getObjectForArkId(ArkId arkId, ModelMap model, KnowledgeObject knowledgeObject,  @ModelAttribute("loggedInUser") OTUser loggedInUser) {
		return getObject(arkId.getArkId(), model, knowledgeObject, loggedInUser);
	}

		@RequestMapping(value = "/object/{objectURI}", method = RequestMethod.GET)
		public String getObject( @PathVariable String objectURI, ModelMap model, KnowledgeObject knowledgeObject,  @ModelAttribute("loggedInUser") OTUser loggedInUser) {
		String view = "objects/ObjectView";
		try {
			if(validateAccessToPrivateObject(objectURI, loggedInUser)) {
				getObject(objectURI, model);
				model.addAttribute("ActiveTab", "METADATA");
			} else {
				logger.error("Private objects can not be viewed by unauthorized users"); 
				model.addAttribute("ErrorMessage","Please log in to view private objects");
			}
		} catch (ObjectTellerException e) { 
			logger.error("Unable to retrieve the object  "+objectURI+ ". Exception occured "+ e.getErrormessage()); 
			model.addAttribute("ErrorMessage","Unable to retrieve the object  "+objectURI+ ". Exception occured "+ e.getErrormessage());
		}
		
		
		return view;
	}
	
	@RequestMapping(value="/publishObject.{objectURI}/{param}", method=RequestMethod.GET)
	public ResponseEntity<String> publishObject(ModelMap model, @PathVariable String objectURI, @PathVariable String param, KnowledgeObject knowledgeObject, @ModelAttribute("loggedInUser") OTUser loggedInUser) {
		ResponseEntity<String> resultEntity = null;

		if(validateUser(loggedInUser)) {
			try {
				editFedoraObjectService.toggleObject(objectURI, param);
				getObject(objectURI, model);
				model.addAttribute("ActiveTab", "METADATA");
				resultEntity = new ResponseEntity<String>(  HttpStatus.OK);

			} catch (ObjectTellerException e) {
				logger.error("Unable to edit published property of the object "+ objectURI);
				resultEntity = new ResponseEntity<String>( "Unable to edit published property of the object "+ objectURI, HttpStatus.INTERNAL_SERVER_ERROR) ;
			} 
		} else {
			resultEntity = new ResponseEntity<String>( "Please login to edit knowledge object", HttpStatus.UNAUTHORIZED) ;
		}

		return resultEntity ;
	}

	private void getObject(String objectURI,ModelMap model) throws ObjectTellerException {

		KnowledgeObject object = knowledgeObjectService.getCompleteKnowledgeObject(new ArkId(objectURI));
		model.addAttribute("fedoraObject",object);

		String logData = object.getLogData();
		model.addAttribute("processedLogData",logData);

	}


	private boolean validateUser(OTUser loggedInUser) {
		return loggedInUser != null;
	}
	
	private boolean validateAccessToPrivateObject(String objectURI,OTUser loggedInUser) throws ObjectTellerException{

		KnowledgeObject object = knowledgeObjectService.getKnowledgeObject(new ArkId(objectURI));
		
		if(!object.getMetadata().isPublished() && loggedInUser == null)
			return false;
		else
			return true;
	}
	
}
