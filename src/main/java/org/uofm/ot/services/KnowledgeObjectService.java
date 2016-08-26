package org.uofm.ot.services;

import java.util.List;

import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fusekiAccessLayer.FusekiService;
import org.uofm.ot.knowledgeObject.FedoraObject;


public class KnowledgeObjectService {
	
	private FusekiService fusekiService;
	
	public void setFusekiService(FusekiService fusekiService) {
		this.fusekiService = fusekiService;
	}

	public FedoraObject getKnowledeObject(String uri) throws ObjectTellerException {
		FedoraObject object = new FedoraObject();
		object.setURI(uri);
		object = fusekiService.getKnowledgeObject(object);
		return object;
	}
	
	public FedoraObject addOrEditObject(FedoraObject newObject) throws ObjectTellerException {
		return null;
	}
	
	public FedoraObject deleteObject(String uri) throws ObjectTellerException {
		return null;
	}
	
	public List<FedoraObject> getKnowledeObjects(boolean published) throws ObjectTellerException {
		List<FedoraObject> fedoraObjects = null;
		fedoraObjects = fusekiService.getFedoraObjects(published);
		return fedoraObjects;
	}

}
