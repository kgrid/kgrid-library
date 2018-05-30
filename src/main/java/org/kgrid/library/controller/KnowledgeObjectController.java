package org.kgrid.library.controller;

import org.kgrid.library.knowledgeObject.ArkId;
import org.kgrid.library.knowledgeObject.KnowledgeObject;
import org.kgrid.library.services.KnowledgeObjectService;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.kgrid.library.exception.LibraryException;
import org.kgrid.library.exception.ObjectNotFoundException;
import org.kgrid.library.model.ErrorInfo;
import org.kgrid.library.model.LibraryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.kgrid.library.knowledgeObject.Metadata;
import org.kgrid.library.knowledgeObject.Payload;
import org.kgrid.library.knowledgeObject.Version;

@RestController
public class KnowledgeObjectController {
	
	@Autowired
	private KnowledgeObjectService knowledgeObjectService;

	@Autowired
	private ServletContext servletContext;
	
	private static final Logger logger = Logger.getLogger(KnowledgeObjectController.class);

	/**
	 * Creates a new knowledge object with a new ark id by parsing the body of the request
	 * @param knowledgeObject
	 * @param loggedInUser
	 * @param request
	 * @return
	 * @throws LibraryException
	 * @throws URISyntaxException
	 */
	@PostMapping(value="/knowledgeObject",
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> createKnowledgeObject(@RequestBody KnowledgeObject knowledgeObject, @ModelAttribute("loggedInUser") LibraryUser loggedInUser, HttpServletRequest request ) throws LibraryException, URISyntaxException {

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
		return entity;
	}

	/**
	 * Creates a new knowledge object with a new ark id by copying the local knowledge object referenced by the ark id in the request body
	 * @param localArkId
	 * @param loggedInUser
	 * @param request
	 * @return
	 * @throws LibraryException
	 * @throws URISyntaxException
	 */
	@PostMapping(value="/knowledgeObject",
		consumes = MediaType.TEXT_PLAIN_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<KnowledgeObject> cloneKnowledgeObject(@RequestBody String localArkId, @ModelAttribute("loggedInUser") LibraryUser loggedInUser, HttpServletRequest request) throws LibraryException, URISyntaxException {
		ArkId objectRef = new ArkId(localArkId);
		KnowledgeObject ko = knowledgeObjectService.getCompleteKnowledgeObject(objectRef);
		return createKnowledgeObject(ko, loggedInUser, request);
	}

	/**
	 * Gets the list of knowledge objects in the library
	 * @param onlyPublished if this is false or not supplied will return the full list of knowledge objects,
	 * 		if true will return only published knowledge objects
	 * @return a list of knowledge objects in json format
	 * @throws LibraryException
	 */
	@GetMapping(value="/knowledgeObject",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<KnowledgeObject>> getKnowledgeObjects(@RequestParam(value="published", required = false) boolean onlyPublished) throws LibraryException {
			return new ResponseEntity<>(knowledgeObjectService.getKnowledgeObjects(onlyPublished), HttpStatus.OK);
	}

	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}", "/ark:/{naan}/{name}", "/knowledgeObject/{naan}-{name}", "/{naan}-{name}"},
			produces = {MediaType.TEXT_HTML_VALUE})
	public ResponseEntity<String> redirectToKnowledgeObject(ArkId arkId) throws LibraryException, MalformedURLException {

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
	public ResponseEntity<KnowledgeObject> getKnowledgeObject(ArkId arkId, @RequestHeader(value = "Prefer", required = false) String prefer) throws LibraryException, URISyntaxException  {

		return getKnowledgeObject(arkId, null, prefer);
	}

	@GetMapping(value={"/ko/ark:/{naan}/{name}"},
		produces = {"application/ld+json"})
	public ResponseEntity<String> getKnowldgeObject(ArkId arkId) throws LibraryException {
		return new ResponseEntity(knowledgeObjectService.getKnowledgeObjectJSON(arkId), HttpStatus.OK);
	}


	@Deprecated // use the above method
	@GetMapping(value="/knowledgeObject/ark:/{naan}/{name}/complete",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public  ResponseEntity<KnowledgeObject> getCompleteKnowledgeObjectForArkId( ArkId arkId) throws LibraryException, URISyntaxException {
		ResponseEntity<KnowledgeObject> entity = null;
		KnowledgeObject knowledgeObject =  knowledgeObjectService.getCompleteKnowledgeObject(arkId);
		if(knowledgeObject != null){
			knowledgeObject.setArkId(arkId);
			entity = new ResponseEntity<>(knowledgeObject,HttpStatus.OK);
		} else {
			entity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return entity ;
	}

	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/versions","/ark:/{naan}/{name}/versions","/knowledgeObject/{naan}-{name}/versions","/{naan}-{name}/versions"},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<String>> getKnowledgeObjectVersions(ArkId arkId) throws LibraryException, URISyntaxException  {

		List<String> versionList = knowledgeObjectService.getVersions(arkId);

		if(versionList != null){
			return new ResponseEntity<>(versionList, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/{version:.+}", "/ark:/{naan}/{name}/{version:.+}","/knowledgeObject/{naan}-{name}/{version:.+}","/{naan}-{name}/{version:.+}"},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> getKnowledgeObject(ArkId arkId, @PathVariable Version version, @RequestHeader(value = "Prefer", required = false) String prefer) throws LibraryException, URISyntaxException  {

		KnowledgeObject knowledgeObject;
		boolean complete = prefer == null || !prefer.matches(".*return\\s*=\\s*minimal.*");
		if(version != null) {
			knowledgeObject = knowledgeObjectService.getVersionSnapshot(arkId, version, complete);
		} else {
			if (complete)
				knowledgeObject = knowledgeObjectService.getCompleteKnowledgeObject(arkId);
			else
				knowledgeObject = knowledgeObjectService.getKnowledgeObject(arkId);
		}

		return new ResponseEntity<>(knowledgeObject, HttpStatus.OK);
	}

	/**
	 * Creates a new version of an existing knowledge object
	 * @param arkId the knowledge object to be versioned
	 * @param loggedInUser the user who is creating the new version
	 * @param version the version name, provided in a slug header
	 * @return the new version of the knowledge object
	 * @throws LibraryException
	 * @throws URISyntaxException
	 */
	@PostMapping(value={"/knowledgeObject/ark:/{naan}/{name}", "/knowledgeObject/{naan}-{name}"},
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<KnowledgeObject> versionKnowledgeObjectByRef(ArkId arkId, @ModelAttribute("loggedInUser") LibraryUser loggedInUser, @RequestHeader(value = "Slug", required = false) String version) throws LibraryException, URISyntaxException {

		KnowledgeObject ko;
		try {
			ko = knowledgeObjectService.createVersion(arkId, version);
		} catch (IllegalArgumentException e) {
			throw new LibraryException("Illegal version id " + e);
		}
		return new ResponseEntity<>(ko, HttpStatus.CREATED);
	}

	@PatchMapping(value={"/knowledgeObject/ark:/{naan}/{name}/{version}", "/ark:/{naan}/{name}/{version}","/knowledgeObject/{naan}-{name}/{version}","/{naan}-{name}/{version}"})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void revertToPriorVersion(ArkId arkId, Version version) throws LibraryException, URISyntaxException{
		knowledgeObjectService.rollbackToPriorVersionSnapshot(arkId, version);
	}

	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/knowledgeObject/{naan}-{name}"},
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> createOrUpdateKnowledgeObjectByArkId(@RequestBody KnowledgeObject knowledgeObject, @ModelAttribute("loggedInUser") LibraryUser loggedInUser, ArkId arkId, HttpServletRequest request) throws LibraryException, URISyntaxException {

		if (loggedInUser != null ) {
			if (knowledgeObjectService.exists(arkId)) {
				KnowledgeObject editedObject = knowledgeObjectService.editObject(knowledgeObject, arkId);
				return new ResponseEntity<>(editedObject,HttpStatus.OK);
			} else {
				knowledgeObject.setArkId(arkId);
				KnowledgeObject createdObject = knowledgeObjectService.copyKnowledgeObject(knowledgeObject, loggedInUser);
				return new ResponseEntity<>(createdObject,HttpStatus.CREATED);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/knowledgeObject/{naan}-{name}"},
			consumes = {MediaType.TEXT_PLAIN_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<KnowledgeObject> createOrUpdateKnowledgeObjectFromURL(@RequestBody String koURL, @ModelAttribute("loggedInUser") LibraryUser loggedInUser, ArkId arkId) throws LibraryException, URISyntaxException {
		if (loggedInUser != null ) {
			KnowledgeObject ko = getRemoteKO(koURL);
			if(knowledgeObjectService.exists(new ArkId(ko.getMetadata().getArkId()))) {
				throw new IllegalStateException("Object with ark id " + arkId + " already exists in this library.");
			}
			ko.setArkId(arkId);
			KnowledgeObject object = knowledgeObjectService.copyKnowledgeObject(ko, loggedInUser);
			return new ResponseEntity<>(object, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	private KnowledgeObject getRemoteKO (String koUrl) throws LibraryException {

		RestTemplate rt = new RestTemplate();
		ResponseEntity<KnowledgeObject> response;
		try {
			response = rt.getForEntity(koUrl, KnowledgeObject.class);
		} catch (HttpClientErrorException e) {
			throw new ObjectNotFoundException("Cannot find remote object at URL " + koUrl);
		}
		return response.getBody();
	}
	
	@DeleteMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/knowledgeObject/{naan}-{name}"})
	public ResponseEntity<String> deleteKnowledgeObjectByArkId(ArkId arkId) {
		try {
			knowledgeObjectService.deleteObject(arkId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (LibraryException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = {"/knowledgeObject/ark:/{naan}/{name}/{version}","/knowledgeObject/{naan}-{name}/{version}"})
	public ResponseEntity<String> deleteKnowledgeObjectVersion(ArkId arkId, Version version) throws ObjectNotFoundException {
		try {
			knowledgeObjectService.deleteVersion(arkId, version);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (LibraryException | URISyntaxException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}/payload","/knowledgeObject/{naan}-{name}/payload"},
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdatePayloadByArkId(@RequestBody Payload payload , ArkId arkId) throws LibraryException, URISyntaxException {
		knowledgeObjectService.editPayload(arkId, payload);	
	}

	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/metadata","/knowledgeObject/{naan}-{name}/metadata"},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Metadata getMetadataByArkId(ArkId arkId) throws LibraryException, URISyntaxException {
		return knowledgeObjectService.getKnowledgeObject(arkId).getMetadata();
	}
	
	@RequestMapping(value={"/knowledgeObject/ark:/{naan}/{name}/payload","/knowledgeObject/{naan}-{name}/payload"},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Payload> getPayloadByArkId( ArkId arkId) {
		ResponseEntity<Payload> payload = null;
		try {
			Payload payloadObj = knowledgeObjectService.getPayload(arkId);
			payload = new ResponseEntity<> (payloadObj,HttpStatus.OK);
		} catch (LibraryException e) {
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
		} catch (LibraryException exception){
			inputMessage = new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return inputMessage;
	}
	
	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/outputMessage","/knowledgeObject/{naan}-{name}/outputMessage"})
	public ResponseEntity<String> getOutputMessageByArkId(ArkId arkId) throws LibraryException {
		ResponseEntity<String> outputMessage = null;
		try {
			String content = knowledgeObjectService.getOutputMessageContent(arkId);
			outputMessage = new ResponseEntity<String>(content, HttpStatus.OK);
		} catch (LibraryException exception){
			outputMessage = new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
		}
		return outputMessage ;
	}
	
	@GetMapping(value={"/knowledgeObject/ark:/{naan}/{name}/logData","/knowledgeObject/{naan}-{name}/logData"})
	public ResponseEntity<String> getLogDataByArkId( ArkId arkId) throws LibraryException, URISyntaxException {
		String content = knowledgeObjectService.getProvData(arkId).toString();
		return new ResponseEntity<>(content, HttpStatus.OK);
	}
	
	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}/metadata","/knowledgeObject/{naan}-{name}/metadata"},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void updateMetadataByArkId(@RequestBody Metadata metadata , ArkId arkId) throws LibraryException, URISyntaxException {
		knowledgeObjectService.addOrEditMetadataToArkId(arkId, metadata);
	}

	@PutMapping(value="/knowledgeObject/ark:/{naan}/{name}/inputMessage")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateInputMessageByArkId(@RequestBody String inputMessage , ArkId arkId) throws LibraryException, URISyntaxException {
		knowledgeObjectService.editInputMessageContent(arkId, inputMessage);
	}
	
	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}/outputMessage","/knowledgeObject/{naan}-{name}/outputMessage"})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void addOrUpdateOutputMessageByArkId(@RequestBody String outputMessage , ArkId arkId) throws LibraryException, URISyntaxException {
		knowledgeObjectService.editOutputMessageContent(arkId, outputMessage);
	}

	@Deprecated // Need to make a more resource-oriented version
	@PatchMapping(value={"/knowledgeObject/ark:/{naan}/{name}","/knowledgeObject/{naan}-{name}"},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void patchKnowledgeObjectPublicationStatus(@RequestBody KnowledgeObject knowledgeObject, ArkId arkId) throws LibraryException, URISyntaxException {
		knowledgeObjectService.patchKnowledgeObject(knowledgeObject, arkId);
	}

	@PutMapping(value={"/knowledgeObject/ark:/{naan}/{name}/{published:published|unpublished}","/knowledgeObject/{naan}-{name}/{published:published|unpublished}"})
	@ResponseStatus(HttpStatus.OK)
	public String publish(ModelMap map, @ModelAttribute("loggedInUser") LibraryUser loggedInUser, ArkId arkId, @PathVariable String published) throws LibraryException, URISyntaxException {
		loggedInUser = (LibraryUser) map.get("loggedInUser");
		
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

	@ExceptionHandler(LibraryException.class)
	public ResponseEntity<ErrorInfo> handleLibraryExceptions(LibraryException e, WebRequest request) {
		return new ResponseEntity<>(new ErrorInfo(e.getMessage(), request.getDescription(false), new Date().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleObjectNotFoundExceptions(ObjectNotFoundException e, WebRequest request) {
		return new ResponseEntity<>(new ErrorInfo(e.getMessage(), request.getDescription(false), new Date().toString()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(URISyntaxException.class)
	public ResponseEntity<ErrorInfo> handleURISyntaxExceptions(URISyntaxException e, WebRequest request){
		return new ResponseEntity<>(new ErrorInfo(e.getMessage(), request.getDescription(false), new Date().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorInfo> handleIllegalStateExceptions(IllegalStateException e, WebRequest request){
		return new ResponseEntity<>(new ErrorInfo(e.getMessage(), request.getDescription(false), new Date().toString()), HttpStatus.CONFLICT);
	}
}
