package org.uofm.ot.controller;

import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.model.OTUser;
import org.uofm.ot.services.KnowledgeObjectService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
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
	public ResponseEntity<KnowledgeObject> createKnowledgeObject(@RequestBody KnowledgeObject KnowledgeObject, @ModelAttribute("loggedInUser") OTUser loggedInUser, HttpServletRequest request ) throws ObjectTellerException, URISyntaxException {

		ResponseEntity<KnowledgeObject> entity = null;

//		loggedInUser = new User("nbahulek@umich.edu", "test", 48 , "Namita", "B.", UserRoles.ADMIN);
		
		if (loggedInUser != null ) {
			String libraryURL = request.getRequestURL().toString();
			KnowledgeObject object= knowledgeObjectService.createKnowledgeObject(KnowledgeObject, loggedInUser, libraryURL);
			String uri = request.getRequestURL()+"/" +object.getURI();
			URI location = new URI(uri);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setLocation(location);
			entity = new ResponseEntity<KnowledgeObject>(object,responseHeaders,HttpStatus.CREATED);
			
		} else {
			
			entity = new ResponseEntity<KnowledgeObject> (HttpStatus.UNAUTHORIZED);	
			
		}
		return entity ; 
	}
	
	@RequestMapping(value="/knowledgeObject", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<KnowledgeObject> geKnowledgeObjects() throws ObjectTellerException {
		return knowledgeObjectService.getKnowledgeObjects(false);
	}


	@RequestMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/ark:/{naan}/{name}"},
			method=RequestMethod.GET,
			produces = {MediaType.TEXT_HTML_VALUE})
	public ResponseEntity<String> redirectToKnowledgeObject(ArkId arkId) throws ObjectTellerException, MalformedURLException {

		URI location = getKoRelativePageUrl(arkId);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.LOCATION, location.toString());

		ResponseEntity<String> responseEntity = new ResponseEntity<>(headers.getLocation().toString(), headers, HttpStatus.PERMANENT_REDIRECT);

		return responseEntity;

	}

	@Autowired
	private ServletContext servletContext;

	private URI getKoRelativePageUrl(ArkId arkId) {
		URI location = null;
		try {
			location = new URIBuilder()
                    .setPath(servletContext.getContextPath() + "/")
                    .setFragment( "/object/" + URLEncoder.encode(arkId.getFedoraPath(), "UTF-8"))
                    .build();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return location;
	}

	@RequestMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/ark:/{naan}/{name}"},
			method=RequestMethod.GET ,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> getKnowledgeObject(ArkId arkId) throws ObjectTellerException  {
		ResponseEntity<KnowledgeObject> entity = null;

		KnowledgeObject knowledgeObject =  knowledgeObjectService.getKnowledgeObject(arkId);

		if(knowledgeObject != null){
			entity = new ResponseEntity<KnowledgeObject>(knowledgeObject,HttpStatus.OK);
		} else {
			entity = new ResponseEntity<KnowledgeObject>(knowledgeObject,HttpStatus.NOT_FOUND);
		}

		return entity ;
	}


	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/complete",
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public  ResponseEntity<KnowledgeObject> getCompleteKnowledgeObjectForArkId( ArkId arkId) throws ObjectTellerException  {
		ResponseEntity<KnowledgeObject> entity = null;
		KnowledgeObject knowledgeObject =  knowledgeObjectService.getCompleteKnowledgeObject(arkId);
		if(knowledgeObject != null){
			entity = new ResponseEntity<KnowledgeObject>(knowledgeObject,HttpStatus.OK);
		} else {
			entity = new ResponseEntity<KnowledgeObject>(knowledgeObject,HttpStatus.NOT_FOUND);
		}

		return entity ;
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/complete",
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public KnowledgeObject getCompleteKnowledgeObject( @PathVariable String objectURI) throws ObjectTellerException  {
		ArkId arkId = new ArkId(objectURI);
		return knowledgeObjectService.getCompleteKnowledgeObject(arkId);
	}
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}",
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public KnowledgeObject updateKnowledgeObjectByArkId(@RequestBody KnowledgeObject knowledgeObject, @ModelAttribute("loggedInUser") OTUser loggedInUser, ArkId arkId, HttpServletRequest request) throws ObjectTellerException, URISyntaxException {

		if (knowledgeObjectService.exists(arkId)) {
			return knowledgeObjectService.editObject(knowledgeObject,arkId);
		} else {
			String libraryURL = request.getRequestURI().toString();
			return knowledgeObjectService.createFromExistingArkId(knowledgeObject, loggedInUser, libraryURL ,arkId);
        }
	}


	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}",
			method=RequestMethod.PUT ,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public KnowledgeObject updateKnowledgeObject(@RequestBody KnowledgeObject knowledgeObject, @ModelAttribute("loggedInUser") OTUser loggedInUser, @PathVariable String objectURI,HttpServletRequest request ) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		return updateKnowledgeObjectByArkId(knowledgeObject, loggedInUser, arkId, request);
	}
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}", 
			method=RequestMethod.DELETE )
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteKnowledgeObjectByArkId(ArkId arkId) throws ObjectTellerException {
		knowledgeObjectService.deleteObject(arkId);
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}", 
			method=RequestMethod.DELETE )
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteKnowledgeObject(@PathVariable String objectURI) throws ObjectTellerException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.deleteObject(arkId);
	}

	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/payload", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdatePayload(@RequestBody Payload payload , @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.editPayload(arkId, payload);
	}
	
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/payload", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdatePayloadByArkId(@RequestBody Payload payload , ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.editPayload(arkId, payload);	
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/metadata", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Metadata getMetadata( @PathVariable String objectURI) throws ObjectTellerException {
		ArkId arkId = new ArkId(objectURI);
		return knowledgeObjectService.getKnowledgeObject(arkId).getMetadata();
	}
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/metadata", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Metadata getMetadataByArkId(ArkId arkId) throws ObjectTellerException {
		return knowledgeObjectService.getKnowledgeObject(arkId).getMetadata();
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/payload", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Payload> getPayload( @PathVariable String objectURI) {
		ResponseEntity<Payload> payload = null;
		try {
			ArkId arkId = new ArkId(objectURI);
			Payload payloadObj = knowledgeObjectService.getPayload(arkId);
			payload = new ResponseEntity<Payload> (payloadObj,HttpStatus.OK);
		} catch (ObjectTellerException e) {
			payload = new ResponseEntity<Payload> (HttpStatus.NOT_FOUND);
		}
		return payload;
	}
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/payload", 
			method=RequestMethod.GET , 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Payload> getPayloadByArkId( ArkId arkId) {
		ResponseEntity<Payload> payload = null;
		try {
			Payload payloadObj = knowledgeObjectService.getPayload(arkId);
			payload = new ResponseEntity<Payload> (payloadObj,HttpStatus.OK);
		} catch (ObjectTellerException e) {
			payload = new ResponseEntity<Payload> (HttpStatus.NOT_FOUND);
		}
		return payload;
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/inputMessage", 
			method=RequestMethod.GET )
	public ResponseEntity<String> getInputMessage( @PathVariable String objectURI)  {
		ResponseEntity<String> inputMessage = null;
		try {
			ArkId arkId = new ArkId(objectURI);
			String content = knowledgeObjectService.getInputMessageContent(arkId);
			inputMessage = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			inputMessage = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return inputMessage ; 
	}
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/inputMessage", 
			method=RequestMethod.GET )
	public ResponseEntity<String> getInputMessageByArkId(ArkId arkId)  {
		ResponseEntity<String> inputMessage = null;
		try {
			String content = knowledgeObjectService.getInputMessageContent(arkId);
			inputMessage = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			inputMessage = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return inputMessage ; 
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/outputMessage", 
			method=RequestMethod.GET)
	public ResponseEntity<String> getOutputMessage( @PathVariable String objectURI) throws ObjectTellerException {
		ResponseEntity<String> outputMessage = null;
		try {
			ArkId arkId = new ArkId(objectURI);
			String content = knowledgeObjectService.getOutputMessageContent(arkId);
			outputMessage = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			outputMessage = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return outputMessage ;
	}
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/outputMessage", 
			method=RequestMethod.GET)
	public ResponseEntity<String> getOutputMessageByArkId(ArkId arkId) throws ObjectTellerException {
		ResponseEntity<String> outputMessage = null;
		try {
			String content = knowledgeObjectService.getOutputMessageContent(arkId);
			outputMessage = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			outputMessage = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return outputMessage ;
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/logData", 
			method=RequestMethod.GET )
	public ResponseEntity<String> getLogData( @PathVariable String objectURI) throws ObjectTellerException {
		ResponseEntity<String> logData = null;
		try {
			ArkId arkId = new ArkId(objectURI);
			String content = knowledgeObjectService.getProvData(arkId);
			logData = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			logData = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return logData ;
	}
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/logData", 
			method=RequestMethod.GET )
	public ResponseEntity<String> getLogDataByArkId( ArkId arkId) throws ObjectTellerException {
		ResponseEntity<String> logData = null;
		try {
			String content = knowledgeObjectService.getProvData(arkId);
			logData = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			logData = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return logData ;
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/metadata", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void updateMetadata(@RequestBody Metadata metadata , @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.addOrEditMetadataToArkId(arkId, metadata);
	}
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/metadata", 
			method=RequestMethod.PUT , 
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void updateMetadataByArkId(@RequestBody Metadata metadata , ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.addOrEditMetadataToArkId(arkId, metadata);
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/inputMessage", 
			method=RequestMethod.PUT)
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateInputMessage(@RequestBody String inputMessage , @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.editInputMessageContent(arkId, inputMessage);
	}
	
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/inputMessage", 
			method=RequestMethod.PUT)
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateInputMessageByArkId(@RequestBody String inputMessage , ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.editInputMessageContent(arkId, inputMessage);
	}
	
	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/outputMessage", 
			method=RequestMethod.PUT)
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateOutputMessage(@RequestBody String outputMessage , @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.editOutputMessageContent(arkId, outputMessage);
	}
	
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}/outputMessage", 
			method=RequestMethod.PUT)
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateOutputMessageByArkId(@RequestBody String outputMessage , ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.editOutputMessageContent(arkId, outputMessage);
	}
	
	@RequestMapping(value="/knowledgeObject/ark:/{naan}/{name}", 
			method=RequestMethod.PATCH,
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void patchKnowledgeObject(@RequestBody KnowledgeObject knowledgeObject, ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.patchKnowledgeObject(knowledgeObject, arkId);
	}

	@PutMapping("/knowledgeObject/ark:/{naan}/{name}/{published:published|unpublished}")
	@ResponseStatus(HttpStatus.OK)
	public String publish(ModelMap map, @ModelAttribute("loggedInUser") OTUser loggedInUser, ArkId arkId, @PathVariable String published) throws ObjectTellerException, URISyntaxException {
		loggedInUser = (OTUser) map.get("loggedInUser");
		
		knowledgeObjectService.publishKnowledgeObject(arkId, "published".equals(published), loggedInUser);

		String name;
		if(loggedInUser == null) {
			name = "anonymous user";
		} else {
			name = loggedInUser.getUsername();
		}
		return String.format("User %s %s ko %s at %s", name, published, arkId.getArkId(), new Date());
	}

	@ExceptionHandler(ObjectTellerException.class)
	public ResponseEntity<ObjectTellerException> handleObjectTellerExceptions(ObjectTellerException e) {

		ResponseEntity<ObjectTellerException> response = new ResponseEntity<ObjectTellerException>(e, HttpStatus.INTERNAL_SERVER_ERROR);

		return response;
	}
}
