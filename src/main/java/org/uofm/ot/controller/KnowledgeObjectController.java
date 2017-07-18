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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.uofm.ot.exception.ObjectNotFoundException;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.knowledgeObject.Version;
import org.uofm.ot.model.ErrorInfo;
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
	private KnowledgeObjectService knowledgeObjectService;

	@Autowired
	private ServletContext servletContext;
	
	private static final Logger logger = Logger.getLogger(KnowledgeObjectController.class);

	/**
	 * Creates a new knowlege object with a new ark id by parsing the body of the request
	 * @param knowledgeObject
	 * @param loggedInUser
	 * @param request
	 * @return
	 * @throws ObjectTellerException
	 * @throws URISyntaxException
	 */
	@PostMapping(value="/knowledgeObject",
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> createKnowledgeObject(@RequestBody KnowledgeObject knowledgeObject, @ModelAttribute("loggedInUser") OTUser loggedInUser, HttpServletRequest request ) throws ObjectTellerException, URISyntaxException {

		ResponseEntity<KnowledgeObject> entity;
		
		if (loggedInUser != null ) {
			KnowledgeObject object = knowledgeObjectService.createNewKnowledgeObject(knowledgeObject, loggedInUser);
			String uri = request.getRequestURL() + "/" + object.getURI();
			URI location = new URI(uri);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setLocation(location);
			entity = new ResponseEntity<>(object,responseHeaders,HttpStatus.CREATED);
			
		} else {
			entity = new ResponseEntity<> (HttpStatus.UNAUTHORIZED);
		}
		return entity ; 
	}

	/**
	 * Creates a new knowledge object with a new ark id by copying the local knowledge object referenced by the ark id in the request body
	 * @param localArkId
	 * @param loggedInUser
	 * @param request
	 * @return
	 * @throws ObjectTellerException
	 * @throws URISyntaxException
	 */
	@PostMapping(value="/knowledgeObject",
		consumes = MediaType.TEXT_PLAIN_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<KnowledgeObject> cloneKnowledgeObject(@RequestBody String localArkId, @ModelAttribute("loggedInUser") OTUser loggedInUser, HttpServletRequest request) throws ObjectTellerException, URISyntaxException {
		ArkId objectRef = new ArkId(localArkId);
		KnowledgeObject ko = knowledgeObjectService.getCompleteKnowledgeObject(objectRef);
		return createKnowledgeObject(ko, loggedInUser, request);
	}

	/**
	 * Gets the list of knowledge objects in the library
	 * @param onlyPublished if this is false or not supplied will return the full list of knowledge objects,
	 * 		if true will return only published knowledge objects
	 * @return a list of knowledge objects in json format
	 * @throws ObjectTellerException if
	 */
	@GetMapping(value="/knowledgeObject",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<KnowledgeObject>> getKnowledgeObjects(@RequestParam(value="published", required = false) boolean onlyPublished) throws ObjectTellerException, ObjectNotFoundException {
			return new ResponseEntity<>(knowledgeObjectService.getKnowledgeObjects(onlyPublished), HttpStatus.OK);
	}

	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}", "/ark:/{naan}/{name}", "/knowledgeObject/{naan}-{name}", "/{naan}-{name}"},
			produces = {MediaType.TEXT_HTML_VALUE})
	public ResponseEntity<String> redirectToKnowledgeObject(ArkId arkId) throws ObjectTellerException, MalformedURLException {

		URI location = getKoRelativePageUrl(arkId);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.LOCATION, location.toString());

		return new ResponseEntity<>(headers.getLocation().toString(), headers, HttpStatus.PERMANENT_REDIRECT);
	}

	private URI getKoRelativePageUrl(ArkId arkId) {
		URI location = null;
		try {
			location = new URIBuilder()
                    .setPath(servletContext.getContextPath() + "/")
                    .setFragment( "/object/" + URLEncoder.encode(arkId.getFedoraPath(), "UTF-8"))
                    .build();
		} catch (UnsupportedEncodingException | URISyntaxException e ) {
			e.printStackTrace();
		}
		return location;
	}

	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/ark:/{naan}/{name}","/knowledgeObject/{naan}-{name}","/{naan}-{name}"},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> getKnowledgeObject(ArkId arkId, @RequestParam(value="complete", required=false) boolean complete) throws ObjectTellerException, URISyntaxException  {

		KnowledgeObject knowledgeObject;
		if(complete) {
			knowledgeObject = knowledgeObjectService.getCompleteKnowledgeObject(arkId);
		} else {
			knowledgeObject =  knowledgeObjectService.getKnowledgeObject(arkId);
		}

		if(knowledgeObject != null){
			return new ResponseEntity<>(knowledgeObject, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Deprecated // use the above method
	@GetMapping(value="/knowledgeObject/ark:/{naan}/{name}/complete",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public  ResponseEntity<KnowledgeObject> getCompleteKnowledgeObjectForArkId( ArkId arkId) throws ObjectTellerException, URISyntaxException {
		ResponseEntity<KnowledgeObject> entity = null;
		KnowledgeObject knowledgeObject =  knowledgeObjectService.getCompleteKnowledgeObject(arkId);
		if(knowledgeObject != null){
			entity = new ResponseEntity<>(knowledgeObject,HttpStatus.OK);
		} else {
			entity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return entity ;
	}

	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/versions","/ark:/{naan}/{name}/versions","/knowledgeObject/{naan}-{name}/versions","/{naan}-{name}/versions"},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<String>> getKnowledgeObjectVersions(ArkId arkId) throws ObjectTellerException, URISyntaxException  {

		List<String> versionList = knowledgeObjectService.getVersions(arkId);

		if(versionList != null){
			return new ResponseEntity<>(versionList, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/{version:.+}", "/ark:/{naan}/{name}/{version:.+}","/knowledgeObject/{naan}-{name}/{version:.+}","/{naan}-{name}/{version:.+}"},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> getKnowledgeObject(ArkId arkId, @PathVariable Version version) throws ObjectTellerException, URISyntaxException  {

		KnowledgeObject knowledgeObject = knowledgeObjectService.getVersionSnapshot(arkId, version);

		if(knowledgeObject != null){
			return new ResponseEntity<>(knowledgeObject, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping(value={"/knowledgeObject/ark:/{naan}/{name}", "/knowledgeObject/{naan}-{name}"},
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<KnowledgeObject> versionKnowledgeObjectByRef(ArkId arkId, @ModelAttribute("loggedInUser") OTUser loggedInUser, @RequestParam(name="version", required=false) String version) throws ObjectTellerException, URISyntaxException {

		KnowledgeObject ko;
		try {
			ko = knowledgeObjectService.createVersion(arkId, version);
		} catch (IllegalArgumentException e) {
			throw new ObjectTellerException("Illegal version id " + e);
		}
		return new ResponseEntity<>(ko, HttpStatus.OK);
	}

	@PatchMapping(value={"/knowledgeObject/ark:/{naan}/{name}/{version}", "/ark:/{naan}/{name}/{version}","/knowledgeObject/{naan}-{name}/{version}","/{naan}-{name}/{version}"})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void revertToPriorVersion(ArkId arkId, Version version) throws  ObjectTellerException, URISyntaxException{
		knowledgeObjectService.rollbackToPriorVersionSnapshot(arkId, version);
	}

	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/knowledgeObject/{naan}-{name}"},
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> createOrUpdateKnowledgeObjectByArkId(@RequestBody KnowledgeObject knowledgeObject, @ModelAttribute("loggedInUser") OTUser loggedInUser, ArkId arkId, HttpServletRequest request) throws ObjectTellerException, URISyntaxException {

		if (loggedInUser != null ) {
			if (knowledgeObjectService.exists(arkId)) {
				KnowledgeObject editedObject = knowledgeObjectService.editObject(knowledgeObject, arkId);
				return new ResponseEntity<>(editedObject,HttpStatus.OK);
			} else {
				KnowledgeObject createdObject = knowledgeObjectService.createNewKnowledgeObject(knowledgeObject, loggedInUser);
				return new ResponseEntity<>(createdObject,HttpStatus.CREATED);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/knowledgeObject/{naan}-{name}"},
			consumes = {MediaType.TEXT_PLAIN_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> createOrUpdateKnowledgeObjectFromURL(@RequestBody String koURL, @ModelAttribute("loggedInUser") OTUser loggedInUser, ArkId arkId, HttpServletRequest request) throws ObjectTellerException, URISyntaxException {
		if (loggedInUser != null ) {
			KnowledgeObject ko = getRemoteKO(koURL);
			KnowledgeObject object = knowledgeObjectService.editObject(ko, arkId);
			return new ResponseEntity<>(object, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	private KnowledgeObject getRemoteKO (String koUrl) {
		RestTemplate rt = new RestTemplate();

		ResponseEntity<KnowledgeObject> response = rt.getForEntity(koUrl, KnowledgeObject.class);

		return response.getBody();
	}
	
	@DeleteMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/knowledgeObject/{naan}-{name}"})
	public ResponseEntity<String> deleteKnowledgeObjectByArkId(ArkId arkId) {
		try {
			knowledgeObjectService.deleteObject(arkId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (ObjectTellerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@DeleteMapping(value = {"/knowledgeObject/ark:/{naan}/{name}/{version}","/knowledgeObject/{naan}-{name}/{version}"})
	public ResponseEntity<String> deleteKnowledgeObjectVersion(ArkId arkId, Version version) throws ObjectNotFoundException {
		try {
			knowledgeObjectService.deleteVersion(arkId, version);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (ObjectTellerException | URISyntaxException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}/payload","/knowledgeObject/{naan}-{name}/payload"},
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdatePayloadByArkId(@RequestBody Payload payload , ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.editPayload(arkId, payload);	
	}

	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/metadata","/knowledgeObject/{naan}-{name}/metadata"},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Metadata getMetadataByArkId(ArkId arkId) throws ObjectTellerException, URISyntaxException {
		return knowledgeObjectService.getKnowledgeObject(arkId).getMetadata();
	}
	
	@RequestMapping(value={"/knowledgeObject/ark:/{naan}/{name}/payload","/knowledgeObject/{naan}-{name}/payload"},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Payload> getPayloadByArkId( ArkId arkId) {
		ResponseEntity<Payload> payload = null;
		try {
			Payload payloadObj = knowledgeObjectService.getPayload(arkId);
			payload = new ResponseEntity<> (payloadObj,HttpStatus.OK);
		} catch (ObjectTellerException e) {
			payload = new ResponseEntity<> (HttpStatus.NOT_FOUND);
		}
		return payload;
	}
	
	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/inputMessage","/knowledgeObject/{naan}-{name}/inputMessage"})
	public ResponseEntity<String> getInputMessageByArkId(ArkId arkId)  {
		ResponseEntity<String> inputMessage = null;
		try {
			String content = knowledgeObjectService.getInputMessageContent(arkId);
			inputMessage = new ResponseEntity<>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			inputMessage = new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return inputMessage;
	}
	
	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/outputMessage","/knowledgeObject/{naan}-{name}/outputMessage"})
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
	
	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/logData","/knowledgeObject/ark:/{naan}/{name}/logData"})
	public ResponseEntity<String> getLogDataByArkId( ArkId arkId) throws ObjectTellerException, URISyntaxException {
		String content = knowledgeObjectService.getProvData(arkId).toString();
		return new ResponseEntity<>(content, HttpStatus.OK);
	}
	
	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}/metadata","/knowledgeObject/{naan}-{name}/metadata"},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void updateMetadataByArkId(@RequestBody Metadata metadata , ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.addOrEditMetadataToArkId(arkId, metadata);
	}

	@PutMapping(value="/knowledgeObject/ark:/{naan}/{name}/inputMessage")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateInputMessageByArkId(@RequestBody String inputMessage , ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.editInputMessageContent(arkId, inputMessage);
	}
	
	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}/outputMessage","/knowledgeObject/{naan}-{name}/outputMessage"})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateOutputMessageByArkId(@RequestBody String outputMessage , ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.editOutputMessageContent(arkId, outputMessage);
	}

	@Deprecated // Need to make a more resource-oriented version
	@PatchMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/knowledgeObject/{naan}-{name}"},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void patchKnowledgeObjectPublicationStatus(@RequestBody KnowledgeObject knowledgeObject, ArkId arkId) throws ObjectTellerException, URISyntaxException {
		knowledgeObjectService.patchKnowledgeObject(knowledgeObject, arkId);
	}

	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}/{published:published|unpublished}","/knowledgeObject/{naan}-{name}/{published:published|unpublished}"})
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

	//Exception handling:

	@ExceptionHandler(ObjectTellerException.class)
	public ResponseEntity<ErrorInfo> handleObjectTellerExceptions(ObjectTellerException e, WebRequest request) {
		return new ResponseEntity<>(new ErrorInfo(e.getMessage(), request.getContextPath(), new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleObjectNotFoundExceptions(ObjectNotFoundException e, WebRequest request) {
		return new ResponseEntity<>(new ErrorInfo(e.getMessage(), request.getContextPath(), new Date()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(URISyntaxException.class)
	public ResponseEntity<ErrorInfo> handleURISyntaxExceptions(URISyntaxException e, WebRequest request){
		return new ResponseEntity<>(new ErrorInfo(e.getMessage(), request.getContextPath(), new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	//-----------------------------------Outdated Methods-------------------------------------------//

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
	@RequestMapping(value="/knowledgeObject/{objectURI}/complete",
			method=RequestMethod.GET ,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public KnowledgeObject getCompleteKnowledgeObject( @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		return knowledgeObjectService.getCompleteKnowledgeObject(arkId);
	}

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
	@RequestMapping(value="/knowledgeObject/{objectURI}",
			method=RequestMethod.PUT ,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> updateKnowledgeObject(@RequestBody KnowledgeObject knowledgeObject, @ModelAttribute("loggedInUser") OTUser loggedInUser, @PathVariable String objectURI,HttpServletRequest request ) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		return createOrUpdateKnowledgeObjectByArkId(knowledgeObject, loggedInUser, arkId, request);
	}

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
	@RequestMapping(value="/knowledgeObject/{objectURI}",
			method=RequestMethod.DELETE )
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteKnowledgeObject(@PathVariable String objectURI) throws ObjectTellerException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.deleteObject(arkId);
	}

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
	@RequestMapping(value="/knowledgeObject/{objectURI}/payload",
			method=RequestMethod.PUT ,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdatePayload(@RequestBody Payload payload , @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.editPayload(arkId, payload);
	}

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
	@RequestMapping(value="/knowledgeObject/{objectURI}/metadata",
			method=RequestMethod.GET ,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Metadata getMetadata( @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		return knowledgeObjectService.getKnowledgeObject(arkId).getMetadata();
	}

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
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

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
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

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
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

	// TODO: Remove this method after UI switched it to other API
	@RequestMapping(value="/knowledgeObject/{objectURI}/logData",
			method=RequestMethod.GET )
	public ResponseEntity<String> getLogData( @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ResponseEntity<String> logData = null;
		try {
			ArkId arkId = new ArkId(objectURI);
			String content = knowledgeObjectService.getProvData(arkId).toString();
			logData = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (ObjectTellerException exception){
			logData = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return logData ;
	}

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
	@RequestMapping(value="/knowledgeObject/{objectURI}/metadata",
			method=RequestMethod.PUT ,
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void updateMetadata(@RequestBody Metadata metadata , @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.addOrEditMetadataToArkId(arkId, metadata);
	}

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
	@RequestMapping(value="/knowledgeObject/{objectURI}/inputMessage",
			method=RequestMethod.PUT)
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateInputMessage(@RequestBody String inputMessage , @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.editInputMessageContent(arkId, inputMessage);
	}

	// TODO: Remove this method after UI switched it to other API
	@Deprecated
	@RequestMapping(value="/knowledgeObject/{objectURI}/outputMessage",
			method=RequestMethod.PUT)
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateOutputMessage(@RequestBody String outputMessage , @PathVariable String objectURI) throws ObjectTellerException, URISyntaxException {
		ArkId arkId = new ArkId(objectURI);
		knowledgeObjectService.editOutputMessageContent(arkId, outputMessage);
	}
}
