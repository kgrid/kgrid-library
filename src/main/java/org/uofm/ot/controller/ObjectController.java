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
import org.uofm.ot.knowledgeObject.FedoraObject;
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
	public String getObjectForArkId(ArkId arkId, ModelMap model, FedoraObject fedoraObject,  @ModelAttribute("loggedInUser") User loggedInUser) {
		return getObject(arkId.getArkId(), model, fedoraObject, loggedInUser);
	}

		@RequestMapping(value = "/object/{objectURI}", method = RequestMethod.GET)
		public String getObject( @PathVariable String objectURI, ModelMap model, FedoraObject fedoraObject,  @ModelAttribute("loggedInUser") User loggedInUser) {
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
	public ResponseEntity<String> publishObject(ModelMap model, @PathVariable String objectURI, @PathVariable String param, FedoraObject fedoraObject, @ModelAttribute("loggedInUser") User loggedInUser) {
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


		
		FedoraObject object = knowledgeObjectService.getCompleteKnowledgeObject(new ArkId(objectURI));
		model.addAttribute("fedoraObject",object);

		String logData = object.getLogData();
//		model.addAttribute("processedLogData",addEscapeCharsInXML(logData));
		model.addAttribute("processedLogData",logData);

	}

	private String addEscapeCharsInXML(String input) {
		if(input != null) {
			input = input.replaceAll( "<", "&lt;");       
			input = input.replaceAll(">", "&gt;");
			input = input.replaceAll("\n", "<br>");
			return input;
		} else 
			return null;
	}

/*	@RequestMapping(value="/editPayload", method=RequestMethod.POST)
	public String editPayload(ModelMap model, FedoraObject fedoraObject, @ModelAttribute("loggedInUser") User loggedInUser) {
		if(validateUser(loggedInUser)) {
			try {

				String transactionId = null;

				editFedoraObjectService.putBinary(fedoraObject.getPayload().getContent(), fedoraObject.getURI(), ChildType.PAYLOAD.getChildType(),transactionId);
				editFedoraObjectService.editPayloadMetadata(fedoraObject.getPayload(),fedoraObject.getURI());
				try {
					Thread.sleep(1000);                 
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
					// TODO Throw exception here  
				}
				getObject(fedoraObject.getURI(), model );
				model.addAttribute("ActiveTab", "PAYLOAD");

			} catch (ObjectTellerException e) {
				logger.error("Unable to edit payload for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage()); 
				model.addAttribute("ErrorMessage","Unable to edit payload for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage());
			}
		} else {
			logger.error("User needs to login to edit payload of the knowledge object with ID. "+ fedoraObject.getURI()); 
			model.addAttribute("ErrorMessage"," Please login to edit knowledge object" );
		}
		return "objects/ObjectView";
	}

	@RequestMapping(value="/editInputMessage", method=RequestMethod.POST)
	public String editInputMessage(ModelMap model, FedoraObject fedoraObject, @ModelAttribute("loggedInUser") User loggedInUser) {
		if(validateUser(loggedInUser)) {
			try {

				String transactionId = null;

				editFedoraObjectService.putBinary(fedoraObject.getInputMessage(), fedoraObject.getURI(), ChildType.INPUT.getChildType(),transactionId);

				getObject(fedoraObject.getURI(), model );
				model.addAttribute("ActiveTab", "INPUT");


			} catch (ObjectTellerException e) {
				logger.error("Unable to edit Input Message for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage()); 
				model.addAttribute("ErrorMessage","Unable to edit Input Message for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage());
			} 
		} else {
			logger.error("User needs to login to edit input message for knowledge object with ID. "+ fedoraObject.getURI()); 
			model.addAttribute("ErrorMessage"," Please login to edit knowledge object" );
		}

		return "objects/ObjectView";
	}

	@RequestMapping(value="/editOutputMessage", method=RequestMethod.POST)
	public String editOutputMessage(ModelMap model, FedoraObject fedoraObject, @ModelAttribute("loggedInUser") User loggedInUser) {

		if(validateUser(loggedInUser)) {
			try {
				String transactionId = null;
				editFedoraObjectService.putBinary(fedoraObject.getOutputMessage(), fedoraObject.getURI(), ChildType.OUTPUT.getChildType(), transactionId);

				getObject(fedoraObject.getURI(),model);
				model.addAttribute("ActiveTab", "OUTPUT");

			} catch (ObjectTellerException e) {
				logger.error("Unable to edit Output Message for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage()); 
				model.addAttribute("ErrorMessage","Unable to edit Output Message for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage());
			}
		} else {
			logger.error("User needs to login to edit output message for knowledge object with ID. "+ fedoraObject.getURI()  ); 
			model.addAttribute("ErrorMessage"," Please login to edit knowledge object" );
		}

		return "objects/ObjectView";
	}*/

	private boolean validateUser(User loggedInUser) {
		return loggedInUser != null;
	}
	
	private boolean validateAccessToPrivateObject(String objectURI,User loggedInUser) throws ObjectTellerException{

		FedoraObject object = knowledgeObjectService.getKnowledgeObject(new ArkId(objectURI));
		
		if(!object.getMetadata().isPublished() && loggedInUser == null)
			return false;
		else
			return true;
	}
	
	/*@RequestMapping(value="/deleteCitation/{objectURI}/{partA}/{partB}/{partC}/{partD}/{partE}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteCitation( @PathVariable String objectURI, @PathVariable String partA ,  @PathVariable String partB ,   @PathVariable String partC , @PathVariable String partD ,@PathVariable String partE,  @ModelAttribute("loggedInUser") User loggedInUser ) {
		ResponseEntity<String> resultEntity = null; 
		
		String citationID = partA + "/" + partB + "/" + partC + "/" + partD + "/" + partE ;
		if(validateUser(loggedInUser)) {
			try {
				deleteFedoraResourceService.deleteObjectCitation(objectURI, citationID);
				resultEntity =  new ResponseEntity<String>( HttpStatus.OK) ;
			} catch (ObjectTellerException e) {
				logger.error("An exception occured while deleting Object Citation for Object ID "+objectURI+ "Citation ID " +citationID + ". Caused by "+e.getMessage());
				resultEntity =  new ResponseEntity<String>("An exception occured while deleting Object Citation for Object ID "+objectURI+ "Citation ID " +citationID + ". Caused by "+e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR) ; 
			}
		} else {
			resultEntity = new ResponseEntity<String>( "Please login to edit knowledge object", HttpStatus.UNAUTHORIZED) ;
		}	
		return resultEntity ; 
	}*/
	
/*	@RequestMapping(value="/deleteObject.{objectURI}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteObject( @PathVariable String objectURI,   @ModelAttribute("loggedInUser") User loggedInUser ) {
		ResponseEntity<String> resultEntity = null; 
		
		
		if(validateUser(loggedInUser)) {
			try {
				deleteFedoraResourceService.deleteObject(new ArkId(objectURI));
				resultEntity =  new ResponseEntity<String>( HttpStatus.OK) ;
			} catch (ObjectTellerException e) {
				logger.error("An exception occured while deleting Object with Object ID "+objectURI+ ". Caused by "+e.getMessage());
				resultEntity =  new ResponseEntity<String>("An exception occured while deleting Object with Object ID "+objectURI+ ". Caused by "+e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR) ; 
			}
		} else {
			resultEntity = new ResponseEntity<String>( "Please login to edit knowledge object", HttpStatus.UNAUTHORIZED) ;
		}	
		return resultEntity ; 
	}*/
}
