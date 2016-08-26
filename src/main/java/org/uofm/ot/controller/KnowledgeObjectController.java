package org.uofm.ot.controller;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject getKnowledgeObject( @PathVariable String objectURI)  {
		FedoraObject fedoraObject = null;
		try {
			fedoraObject = knowledgeObjectService.getKnowledeObject(objectURI);
		} catch (ObjectTellerException e) {
			logger.error("Exception occured while retrieving Object with id "+objectURI+". Exception "+e.getMessage());
		}
		return fedoraObject;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public FedoraObject updateKnowledgeObject(@PathVariable String uri,FedoraObject knowledgeObject) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject", 
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
	public FedoraObject getMetadata( @PathVariable String objectURI) {
		return null;
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
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Payload updateMetadta(Metadata metadata , @PathVariable String objectURI) {
		return null;
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
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/citations", 
			method=RequestMethod.GET, 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Citation> getObjectCitations(@PathVariable String objectURI) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/citation/{citationID}", 
			method=RequestMethod.DELETE)
	public List<Citation> deleteObjectCitation(@PathVariable String objectURI,@PathVariable String citationID ) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/citations/", 
			method=RequestMethod.PUT, 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Citation> updateObjectCitations(@PathVariable String objectURI,List<Citation> citations) {
		return null;
	}
	
	@RequestMapping(value="/knowledgeObject/{objectURI}/citation/{citationID}", 
			method=RequestMethod.PUT, 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Citation> updateObjectCitation(@PathVariable String objectURI,@PathVariable String citationID, List<Citation> citations) {
		return null;
	}
}
