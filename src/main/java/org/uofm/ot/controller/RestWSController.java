package org.uofm.ot.controller;


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
import org.uofm.ot.fedoraAccessLayer.GetFedoraObjectService;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.services.KnowledgeObjectService;
import org.uofm.ot.transferobjects.Result;



@RestController
@CrossOrigin
public class RestWSController {

	private GetFedoraObjectService getFedoraObjectService;

	@Autowired
	KnowledgeObjectService knowledgeObjectService;

	public void setGetFedoraObjectService(GetFedoraObjectService getFedoraObjectService) {
		this.getFedoraObjectService = getFedoraObjectService;
	}

	@RequestMapping(value = "/knowledgeObject/ark:/{naan}/{name}/result", method = RequestMethod.POST,
			consumes = "application/owl+xml",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Result> getOWLResult(@RequestBody String content, ArkId arkId) throws ObjectTellerException {
		
		ResponseEntity<Result> resultEntity = null; 
		
		boolean objectExists = false;
		
		if(arkId != null ) {
			objectExists= getFedoraObjectService.checkIfObjectExists(arkId.getFedoraPath());
			if ( objectExists ) {
				String payload = getFedoraObjectService.getObjectContent(arkId.getFedoraPath(), ChildType.PAYLOAD.getChildType());
				OWLAdapter adapter = new OWLAdapter();
				Result result = adapter.execute(content, payload);				
				resultEntity = new ResponseEntity<Result> (result, HttpStatus.OK);
			}
		}
		return resultEntity;
	}
}
