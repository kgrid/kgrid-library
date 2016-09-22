package org.uofm.ot.dao;

import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.ObjectId;
import org.uofm.ot.services.ArkID;

public interface ObjectIDDAO {
	
	public ArkID retrieveNewId() throws ObjectTellerException;
	
	public void updateNewId() throws ObjectTellerException;
	
	
	
}
