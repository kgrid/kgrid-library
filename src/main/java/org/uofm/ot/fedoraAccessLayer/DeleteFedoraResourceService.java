package org.uofm.ot.fedoraAccessLayer;

import org.uofm.ot.exception.ObjectTellerException;

public class DeleteFedoraResourceService extends FedoraObjectService {
	
	public void deleteObjectCitation(String objectURI, String citationID) throws ObjectTellerException {
		String uri = objectURI + "/" + ChildType.CITATIONS.getChildType() + "/" + citationID ; 
		deleteFedoraResource(uri);
	}

}
