package org.uofm.ot.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.ChildType;
import org.uofm.ot.fedoraAccessLayer.CreateFedoraObjectService;
import org.uofm.ot.fedoraAccessLayer.EditFedoraObjectService;
import org.uofm.ot.fedoraAccessLayer.FedoraObject;
import org.uofm.ot.fedoraAccessLayer.GetFedoraObjectService;
import org.uofm.ot.fedoraAccessLayer.PayloadDescriptor;
import org.uofm.ot.fusekiAccessLayer.FusekiService;
import org.uofm.ot.model.User;

import com.google.gson.Gson; 

@Controller
public class ObjectController {

	private EditFedoraObjectService editFedoraObjectService;

	private FusekiService fusekiService;

	private CreateFedoraObjectService createFedoraObjectService;

	private GetFedoraObjectService getFedoraObjectService;

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


	@RequestMapping(value = "/object.{objectURI}", method = RequestMethod.GET)
	public String getObject( @PathVariable String objectURI, ModelMap model, FedoraObject fedoraObject) {
		String view = "objects/ObjectView";

		try {
			getObject(objectURI, model);
			model.addAttribute("ActiveTab", "METADATA");
		} catch (ObjectTellerException e) { 
			logger.error("Unable to retrieve the object  "+objectURI+ ". Exception occured "+ e.getErrormessage()); 
			model.addAttribute("ErrorMessage","Unable to retrieve the object  "+objectURI+ ". Exception occured "+ e.getErrormessage());
		}
		return view;
	}

	@RequestMapping(value="/editMetadata", method=RequestMethod.POST)
	public String editObjectHEMetadata(ModelMap model, FedoraObject fedoraObject) {
		try {
			editFedoraObjectService.editObjectMetadata(fedoraObject);
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

		return "objects/ObjectView";
	}

	@RequestMapping(value="/publishObject.{objectURI}/{param}", method=RequestMethod.GET)
	public ResponseEntity<String> publishObject(ModelMap model, @PathVariable String objectURI, @PathVariable String param, FedoraObject fedoraObject) {

		try {
			editFedoraObjectService.toggleObject(objectURI, param);
			getObject(objectURI, model);
			model.addAttribute("ActiveTab", "METADATA");

		} catch (ObjectTellerException e) {
			logger.error("Unable to edit published property of the object "+ objectURI);
			return new ResponseEntity<String>( "Unable to edit published property of the object "+ objectURI, HttpStatus.INTERNAL_SERVER_ERROR) ;
		}

		return new ResponseEntity<String>(  HttpStatus.OK) ;
	}

	private void getObject(String objectURI,ModelMap model) throws ObjectTellerException {
		FedoraObject object = new FedoraObject();
		object.setURI(objectURI);

		object = fusekiService.getObjectProperties(object);

		PayloadDescriptor payloadD = fusekiService.getPayloadProperties(objectURI);

		object.setPayloadDescriptor(payloadD);

		String provDataPart1 = fusekiService.getObjectProvProperties(objectURI);

		String provDataPart2 = fusekiService.getObjectProvProperties(objectURI+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType());

		object.setLogData(provDataPart1+provDataPart2);

		object.setPayload(getFedoraObjectService.getObjectContent(objectURI, ChildType.PAYLOAD.getChildType()));

		object.setInputMessage(getFedoraObjectService.getObjectContent(objectURI, ChildType.INPUT.getChildType()));

		object.setOutputMessage(getFedoraObjectService.getObjectContent(objectURI, ChildType.OUTPUT.getChildType()));

		model.addAttribute("fedoraObject",object);

		String inputMessage = object.getInputMessage();
		model.addAttribute("processedStringInput",addEscapeCharsInXML(inputMessage));

		String outputMessage = object.getOutputMessage();
		model.addAttribute("processedStringOutput",addEscapeCharsInXML(outputMessage));

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
	public String editPayload(ModelMap model, FedoraObject fedoraObject) {
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
			model.addAttribute("ErrorMeassage","Unable to edit payload for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage());
		}

		return "objects/ObjectView";
	}

	@RequestMapping(value="/editInputMessage", method=RequestMethod.POST)
	public String editInputMessage(ModelMap model, FedoraObject fedoraObject) {
		try {

			String transactionId = null;
			
			editFedoraObjectService.putBinary(fedoraObject.getInputMessage(), fedoraObject.getURI(), ChildType.INPUT.getChildType(),transactionId);

			getObject(fedoraObject.getURI(), model );
			model.addAttribute("ActiveTab", "INPUT");


		} catch (ObjectTellerException e) {
			logger.error("Unable to edit Input Message for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage()); 
			model.addAttribute("ErrorMeassage","Unable to edit Input Message for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage());
		}

		return "objects/ObjectView";
	}

	@RequestMapping(value="/editOutputMessage", method=RequestMethod.POST)
	public String editOutputMessage(ModelMap model, FedoraObject fedoraObject) {

		try {
			String transactionId = null;
			editFedoraObjectService.putBinary(fedoraObject.getOutputMessage(), fedoraObject.getURI(), ChildType.OUTPUT.getChildType(), transactionId);

			getObject(fedoraObject.getURI(),model);
			model.addAttribute("ActiveTab", "OUTPUT");

		} catch (ObjectTellerException e) {
			logger.error("Unable to edit Output Message for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage()); 
			model.addAttribute("ErrorMeassage","Unable to edit Output Message for the object "+ fedoraObject + ". Exception occured "+ e.getErrormessage());
		}

		return "objects/ObjectView";
	}

	@RequestMapping(value="/createNewObjectTest", method=RequestMethod.POST , consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> createNewObject(@RequestBody String string, HttpSession httpSession ) {		
		

		String result ;

		Gson gson = new Gson();
		FedoraObject fedoraObject = gson.fromJson(string, FedoraObject.class);

		try {
			User loggedInUser = (User) httpSession.getAttribute("DBUser");
			FedoraObject newObject = createFedoraObjectService.createObject(fedoraObject,loggedInUser);

			result =  "{  \"uri\": \""+newObject.getURI()+"\"  }";

		} catch (ObjectTellerException e) {
			logger.error("An exception occured while creating new object "+e.getMessage()+" caused by "+e.getCause());
			return new ResponseEntity<String>("An exception occured while creating new object "+e.getMessage()+" caused by "+e.getCause() , HttpStatus.INTERNAL_SERVER_ERROR) ; 
		}

		return new ResponseEntity<String>( result, HttpStatus.CREATED) ; 

	}
}
