package org.uofm.ot.fedoraAccessLayer;

import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.knowledgeObject.ArkId;

public class DeleteFedoraResourceService extends FedoraObjectService {
	
	// TODO: technically this should not be wrapped in arkId 
	public void deleteObjectCitation(String objectURI, String citationID) throws ObjectTellerException {
		String uri = objectURI + "/" + ChildType.CITATIONS.getChildType() + "/" + citationID ; 
		deleteFedoraResource(new ArkId(uri));
	}
	
	public void deleteObject(ArkId arkId) throws ObjectTellerException {
		deleteFedoraResource(arkId);
	}

}
