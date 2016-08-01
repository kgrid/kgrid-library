package org.uofm.ot.dao;

import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.ObjectId;

public interface ObjectIDDAO {
	
	public ObjectId retrieveNewId() throws ObjectTellerException;
	
	public void updateNewId(ObjectId objectId) throws ObjectTellerException;
	
	
	
}
