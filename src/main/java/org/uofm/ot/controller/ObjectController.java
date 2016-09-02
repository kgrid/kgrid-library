package org.uofm.ot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.python.bouncycastle.crypto.modes.OldCTSBlockCipher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.ChildType;
import org.uofm.ot.fedoraAccessLayer.CreateFedoraObjectService;
import org.uofm.ot.fedoraAccessLayer.DeleteFedoraResourceService;
import org.uofm.ot.fedoraAccessLayer.EditFedoraObjectService;
import org.uofm.ot.fedoraAccessLayer.GetFedoraObjectService;
import org.uofm.ot.fusekiAccessLayer.FusekiService;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.FedoraObject;
import org.uofm.ot.knowledgeObject.PayloadDescriptor;
import org.uofm.ot.model.User;
import org.uofm.ot.services.KnowledgeObjectService;

import com.google.gson.Gson; 

@Controller
public class ObjectController {

	private EditFedoraObjectService editFedoraObjectService;

	private FusekiService fusekiService;

	private CreateFedoraObjectService createFedoraObjectService;

	private GetFedoraObjectService getFedoraObjectService;
	
	private DeleteFedoraResourceService deleteFedoraResourceService;
	
	private KnowledgeObjectService objectService;

	private static final Logger logger = Logger.getLogger(ObjectController.class);


	public void setEditFedoraObjectService(EditFedoraObjectService editFedoraObjectService) {
		this.editFedoraObjectService = editFedoraObjectService;
	}


	public void setGetFedoraObjectService(GetFedoraObjectService getFedoraObjectService) {
		this.getFedoraObjectService = getFedoraObjectService;
	}


	public void setFusekiService(FusekiService fusekiService) {
		this.fusekiService = fusekiService;
	}


	public void setCreateFedoraObjectService(CreateFedoraObjectService createFedoraObjectService) {
		this.createFedoraObjectService = createFedoraObjectService;
	}


	public void setDeleteFedoraResourceService(DeleteFedoraResourceService deleteFedoraResourceService) {
		this.deleteFedoraResourceService = deleteFedoraResourceService;
	}


	public void setObjectService(KnowledgeObjectService objectService) {
		this.objectService = objectService;
	}


	@RequestMapping(value = "/object.{objectURI}", method = RequestMethod.GET)
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

	@RequestMapping(value="/editMetadata", method=RequestMethod.POST)
	public String editObjectHEMetadata(ModelMap model, FedoraObject fedoraObject,  @ModelAttribute("loggedInUser") User loggedInUser) {

		if(validateUser(loggedInUser)) {
			try {
				
				List<Citation> oldCitations = fusekiService.getObjectCitations(fedoraObject.getURI());
				
				editFedoraObjectService.editObjectMetadata(fedoraObject.getMetadata(),fedoraObject.getURI());
				
				
				if(fedoraObject.getMetadata() != null && fedoraObject.getMetadata().getCitations() != null) {
					List<Citation> editCitations = new ArrayList<Citation>();

					boolean firstCitation = true ; 
					// To add new citations
					for (Citation citation : fedoraObject.getMetadata().getCitations()) {
						if(citation.getCitation_id() == null && citation.getCitation_title() != null && citation.getCitation_at() != null ) {

							if(firstCitation) {
								boolean citationParentExist = createFedoraObjectService.checkIfObjectExists(fedoraObject.getURI()+"/"+ChildType.CITATIONS.getChildType());

								if(!citationParentExist) 
									createFedoraObjectService.createContainer(fedoraObject.getURI()+"/"+ChildType.CITATIONS.getChildType(), null);

								firstCitation = false ;

							}
							
							String location = createFedoraObjectService.createContainerWithAutoGeneratedName(fedoraObject.getURI()+"/"+ChildType.CITATIONS.getChildType(), null);

							citation.setCitation_id(location);

							createFedoraObjectService.addCitationProperties(citation, fedoraObject.getURI()+"/"+ChildType.CITATIONS.getChildType()+"/"+location, null);
						} else {
							if(citation.getCitation_id() != null )
								editCitations.add(citation);
						}
					}
					
					editFedoraObjectService.editCitations(fedoraObject.getURI(),editCitations);
					oldCitations.removeAll(editCitations);
				}
				
				if(oldCitations.isEmpty() == false){
					for (Citation citation : oldCitations) {
						deleteFedoraResourceService.deleteObjectCitation(fedoraObject.getURI(), citation.getCitation_id());
					}		
				}
				
				try {
					Thread.sleep(1000);                 
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt(); 
				}
				getObject(fedoraObject.getURI(), model);
				
				model.addAttribute("ActiveTab", "METADATA");


			} catch (ObjectTellerException e) {
				logger.error("Unable to edit human entered metadata for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage()); 
				model.addAttribute("ErrorMessage","Unable to edit human entered metadata for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage());
			}
		} else {
			logger.error("User needs to login to edit payload of the knowledge object with ID. "+ fedoraObject.getURI()); 
			model.addAttribute("ErrorMessage"," Please login to edit knowledge object" );
		}

		return "objects/ObjectView";
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
		
		FedoraObject object = objectService.getCompleteKnowledgeObject(objectURI);
		model.addAttribute("fedoraObject",object);

		String logData = object.getLogData();
		model.addAttribute("processedLogData",addEscapeCharsInXML(logData));

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

	@RequestMapping(value="/editPayload", method=RequestMethod.POST)
	public String editPayload(ModelMap model, FedoraObject fedoraObject, @ModelAttribute("loggedInUser") User loggedInUser) {
		if(validateUser(loggedInUser)) {
			try {

				String transactionId = null;

				editFedoraObjectService.putBinary(fedoraObject.getPayload(), fedoraObject.getURI(), ChildType.PAYLOAD.getChildType(),transactionId);
				editFedoraObjectService.editPayloadMetadata(fedoraObject);
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
	}

	@RequestMapping(value="/createNewObject", method=RequestMethod.POST , consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> createNewObject(@RequestBody String string,  @ModelAttribute("loggedInUser") User loggedInUser ) {		
		ResponseEntity<String> resultEntity = null; 

		String result ; 
		
		if(validateUser(loggedInUser)) {
			Gson gson = new Gson();
			FedoraObject fedoraObject = gson.fromJson(string, FedoraObject.class);		
			
			try {
				FedoraObject newObject = createFedoraObjectService.createObject(fedoraObject,loggedInUser);
			
				result =  "{  \"uri\": \""+newObject.getURI()+"\"  }";
				resultEntity =  new ResponseEntity<String>( result, HttpStatus.CREATED) ; 
			} catch (ObjectTellerException e) {
				logger.error("An exception occured while creating new object "+e.getMessage()+" caused by "+e.getCause());
				resultEntity =  new ResponseEntity<String>("An exception occured while creating new object "+e.getMessage()+" caused by "+e.getCause() , HttpStatus.INTERNAL_SERVER_ERROR) ; 
			}
		
		} else {
			resultEntity = new ResponseEntity<String>("Please log in to create new object.", HttpStatus.UNAUTHORIZED );
		}

		return resultEntity ; 
	}
	
	private boolean validateUser(User loggedInUser) {
		return loggedInUser != null;
	}
	
	private boolean validateAccessToPrivateObject(String objectURI,User loggedInUser) throws ObjectTellerException{
		FedoraObject object = new FedoraObject();
		object.setURI(objectURI);

		object = fusekiService.getKnowledgeObject(objectURI);
		
		if(!object.getMetadata().isPublished() && loggedInUser == null)
			return false;
		else
			return true;
	}
	
	@RequestMapping(value="/deleteCitation/{objectURI}/{partA}/{partB}/{partC}/{partD}/{partE}", method=RequestMethod.DELETE)
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
	}
	
	@RequestMapping(value="/deleteObject.{objectURI}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteObject( @PathVariable String objectURI,   @ModelAttribute("loggedInUser") User loggedInUser ) {
		ResponseEntity<String> resultEntity = null; 
		
		
		if(validateUser(loggedInUser)) {
			try {
				deleteFedoraResourceService.deleteObject(objectURI);
				resultEntity =  new ResponseEntity<String>( HttpStatus.OK) ;
			} catch (ObjectTellerException e) {
				logger.error("An exception occured while deleting Object with Object ID "+objectURI+ ". Caused by "+e.getMessage());
				resultEntity =  new ResponseEntity<String>("An exception occured while deleting Object with Object ID "+objectURI+ ". Caused by "+e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR) ; 
			}
		} else {
			resultEntity = new ResponseEntity<String>( "Please login to edit knowledge object", HttpStatus.UNAUTHORIZED) ;
		}	
		return resultEntity ; 
	}
}
