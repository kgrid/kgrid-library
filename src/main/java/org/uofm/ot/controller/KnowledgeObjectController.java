package org.uofm.ot.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.knowledgeObject.FedoraObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.model.User;
import org.uofm.ot.services.KnowledgeObjectService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class KnowledgeObjectController {
	
	@Autowired
	private KnowledgeObjectService knowledgeObjectService ;
	
	private static final Logger logger = Logger.getLogger(KnowledgeObjectController.class);

	@RequestMapping(value="/knowledgeObject", 
			method=RequestMethod.POST , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<FedoraObject> createKnowledgeObject(@RequestBody FedoraObject KnowledgeObject,@ModelAttribute("loggedInUser") User loggedInUser, HttpServletRequest request ) throws ObjectTellerException, URISyntaxException {

		ResponseEntity<FedoraObject> entity = null;
		
		if (loggedInUser != null) {
			
			FedoraObject object= knowledgeObjectService.createKnowledgeObject(KnowledgeObject, loggedInUser);
			String uri = request.getRequestURL()+"/" +object.getURI();
			URI location = new URI(uri);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setLocation(location);
			entity = new ResponseEntity<FedoraObject>(object,responseHeaders,HttpStatus.CREATED);
			
		} else {
			
			entity = new ResponseEntity<FedoraObject> (HttpStatus.UNAUTHORIZED);	
			
		}
		return entity ; 
	}
	
	@RequestMapping(value="/knowledgeObject", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<FedoraObject> geKnowledgeObjects() throws ObjectTellerException {
		return knowledgeObjectService.getKnowledgeObjects(false);
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<FedoraObject> getKnowledgeObject( @PathVariable String objectURI) throws ObjectTellerException  {
		ResponseEntity<FedoraObject> entity = null;
		
		FedoraObject fedoraObject =  knowledgeObjectService.getKnowledgeObject(objectURI);
		
		if(fedoraObject != null){
			entity = new ResponseEntity<FedoraObject>(fedoraObject,HttpStatus.OK);			
		} else {
			entity = new ResponseEntity<FedoraObject>(fedoraObject,HttpStatus.NOT_FOUND);
		}
		
		return entity ; 
	}
	
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/complete", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject getCompleteKnowledgeObject( @PathVariable String objectURI) throws ObjectTellerException  {
		return knowledgeObjectService.getCompleteKnowledgeObject(objectURI);
	}
	
	@RequestMapping(value="/knowledgeObject", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject updateKnowledgeObject(@RequestBody FedoraObject knowledgeObject) throws ObjectTellerException {
		return knowledgeObjectService.editObject(knowledgeObject);
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}", 
			method=RequestMethod.DELETE )
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteKnowledgeObject(@PathVariable String objectURI) throws ObjectTellerException {
		knowledgeObjectService.deleteObject(objectURI);
	}

	@RequestMapping(value="/knowledgeObject/{objectURI}/payload", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdatePayload(@RequestBody Payload payload , @PathVariable String objectURI) throws ObjectTellerException {
		knowledgeObjectService.editPayload(objectURI, payload);	
	}
	
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/metadata", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Metadata getMetadata( @PathVariable String objectURI) throws ObjectTellerException {
		return knowledgeObjectService.getKnowledgeObject(objectURI).getMetadata();
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/payload", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Payload> getPayload( @PathVariable String objectURI) {
		ResponseEntity<Payload> payload = null;
		try {
			Payload payloadObj = knowledgeObjectService.getPayload(objectURI);
			payload = new ResponseEntity<Payload> (payloadObj,HttpStatus.OK);
		} catch (ObjectTellerException e) {
			payload = new ResponseEntity<Payload> (HttpStatus.NOT_FOUND);
		}
		return payload;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/inputMessage", 
			method=RequestMethod.GET )
	public ResponseEntity<String> getInputMessage( @PathVariable String objectURI)  {
		ResponseEntity<String> inputMessage = null;
		try {
			String content = knowledgeObjectService.getInputMessageContent(objectURI);
			inputMessage = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			inputMessage = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return inputMessage ; 
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/outputMessage", 
			method=RequestMethod.GET)
	public ResponseEntity<String> getOutputMessage( @PathVariable String objectURI) throws ObjectTellerException {
		ResponseEntity<String> outputMessage = null;
		try {
			String content = knowledgeObjectService.getOutputMessageContent(objectURI);
			outputMessage = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			outputMessage = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return outputMessage ;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/logData", 
			method=RequestMethod.GET )
	public ResponseEntity<String> getLogData( @PathVariable String objectURI) throws ObjectTellerException {
		ResponseEntity<String> logData = null;
		try {
			String content = knowledgeObjectService.getProvData(objectURI);
			logData = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			logData = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return logData ;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/metadata", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void updateMetadata(@RequestBody Metadata metadata , @PathVariable String objectURI) throws ObjectTellerException {
		 knowledgeObjectService.addOrEditMetadata(objectURI, metadata);
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/inputMessage", 
			method=RequestMethod.PUT)
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateInputMessage(@RequestBody String inputMessage , @PathVariable String objectURI) throws ObjectTellerException {
		knowledgeObjectService.editInputMessageContent(objectURI, inputMessage);
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/outputMessage", 
			method=RequestMethod.PUT)
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateOutputMessage(@RequestBody String outputMessage , @PathVariable String objectURI) throws ObjectTellerException {
		knowledgeObjectService.editOutputMessageContent(objectURI, outputMessage);
	}
	
}
