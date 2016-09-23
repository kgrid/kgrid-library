package org.uofm.ot.dao;

import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.knowledgeObject.ArkId;

public interface ObjectIDDAO {
	
	public ArkId retrieveNewId() throws ObjectTellerException;
	
	public void updateNewId() throws ObjectTellerException;
	
	
	
}
