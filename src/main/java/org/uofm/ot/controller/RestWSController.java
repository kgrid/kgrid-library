package org.uofm.ot.controller;


import java.net.URI;
import java.net.URISyntaxException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.uofm.ot.adapter.OWLAdapter;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.ChildType;
import org.uofm.ot.fedoraAccessLayer.FCRepoService;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.services.KnowledgeObjectService;
import org.uofm.ot.transferobjects.Result;



@RestController
@CrossOrigin
public class RestWSController {

	private FCRepoService fcRepoService;

	private static final Logger logger = Logger.getLogger(RestWSController.class);

	@Autowired
	KnowledgeObjectService knowledgeObjectService;

	public void setGetFedoraObjectService(FCRepoService fcRepoService) {
		this.fcRepoService = fcRepoService;
	}

	@RequestMapping(value = "/knowledgeObject/ark:/{naan}/{name}/result", method = RequestMethod.POST,
			consumes = "application/owl+xml",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Result> getOWLResult(@RequestBody String content, ArkId arkId) throws ObjectTellerException {
		
		ResponseEntity<Result> resultEntity = null; 
		
		boolean objectExists = false;
		
		if(arkId != null ) {
			try {
				objectExists = fcRepoService.checkIfObjectExists(
						new URI(fcRepoService.getBaseURI() + "/" + arkId.getFedoraPath()));
				if (objectExists) {
					String payload = fcRepoService
							.getObjectContent(arkId.getFedoraPath(), ChildType.PAYLOAD.getChildType());
					OWLAdapter adapter = new OWLAdapter();
					Result result = adapter.execute(content, payload);
					resultEntity = new ResponseEntity<Result>(result, HttpStatus.OK);
				}
			} catch (URISyntaxException e) {
				logger.error("Error creating URI for checking if OWL object exists at " + fcRepoService.getBaseURI() + "/" + arkId.getFedoraPath());
			}
		}
		return resultEntity;
	}
}
