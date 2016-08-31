package org.uofm.ot.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.FedoraObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.services.KnowledgeObjectService;

import java.util.List;

@RestController
public class KnowledgeObjectController {
	
	private KnowledgeObjectService knowledgeObjectService ;
	
	private static final Logger logger = Logger.getLogger(KnowledgeObjectController.class);

	public void setKnowledgeObjectService(KnowledgeObjectService knowledgeObjectService) {
		this.knowledgeObjectService = knowledgeObjectService;
	}

	@RequestMapping(value="/knowledgeObject", 
			method=RequestMethod.POST , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject createKnowledgeObject(FedoraObject KnowledgeObject) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<FedoraObject> geKnowledgeObjects(FedoraObject KnowledgeObject) throws ObjectTellerException {
		return knowledgeObjectService.getKnowledgeObjects(false);
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject getKnowledgeObject( @PathVariable String objectURI) throws ObjectTellerException  {
		return knowledgeObjectService.getKnowledgeObject(objectURI);				
	}
	
	
	@RequestMapping(value="/knowledgeObject-complete/{objectURI}/", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject getCompleteKnowledgeObject( @PathVariable String objectURI) throws ObjectTellerException  {
		return knowledgeObjectService.getCompleteKnowledgeObject(objectURI);
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject updateKnowledgeObject(@PathVariable String uri,FedoraObject knowledgeObject) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/", 
			method=RequestMethod.DELETE , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject deleteKnowledgeObject(@PathVariable String uri) {
		return null;
	}

	@RequestMapping(value="/knowledgeObject/{objectURI}/payload", 
			method=RequestMethod.POST , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Payload addOrUpdatePayload(Payload payload , @PathVariable String objectURI) {
		return null;
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
	public Payload getPayload( @PathVariable String objectURI) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/inputMessage", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject getInputMessage( @PathVariable String objectURI) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/outputMessage", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject getOutputMessage( @PathVariable String objectURI) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/logData", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject getLogData( @PathVariable String objectURI) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/metadata", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void updateMetadata(@RequestBody Metadata metadata , @PathVariable String objectURI) throws ObjectTellerException {
		 knowledgeObjectService.addOrEditMetadata(objectURI, metadata);
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/inputMessage", 
			method=RequestMethod.POST)
	public Payload addOrUpdateInputMessage(String inputMessage , @PathVariable String objectURI) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/outputMessage", 
			method=RequestMethod.POST)
	public Payload addOrUpdateOutputMessage(String outputMessage , @PathVariable String objectURI) {
		return null;
	}
	
}