package org.uofm.ot.fedoraAccessLayer;

import org.uofm.ot.exception.ObjectTellerException;

public class DeleteFedoraResourceService extends FedoraObjectService {
	
	// TODO: technically this should not be wrapped in arkId 
	public void deleteObjectCitation(String objectURI, String citationID) throws ObjectTellerException {
		String uri = objectURI + "/" + ChildType.CITATIONS.getChildType() + "/" + citationID ; 
		deleteFedoraResource(uri);
	}
	
	public void deleteObject(String uri) throws ObjectTellerException {
		deleteFedoraResource(uri);
	}

}
