package org.uofm.ot.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.uofm.ot.dao.ObjectIDDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.ObjectId;
import org.uofm.ot.model.SETTING_TYPE;
import org.uofm.ot.model.Server_details;

public class ObjectIDDAOImpl implements ObjectIDDAO {
	
	private final String TABLE_NAME= "ID_TRACKER";
	
	private final String COLUMN_ID= "ID";
	
	private final String NEW_OBJECT_ID= "newObjectId";
	
	private final String CHECK_ID = "SELECT * FROM "+TABLE_NAME ;
	
	private final String UPDATE_NEW_ID = "UPDATE "+TABLE_NAME +" SET "+NEW_OBJECT_ID+"= ? WHERE "+COLUMN_ID+"=? ";
	
	private JdbcTemplate jdbcTemplate;  

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public ObjectId retrieveNewId() throws ObjectTellerException {
		try {
			ObjectId objectId = jdbcTemplate.queryForObject(CHECK_ID,new BeanPropertyRowMapper<>(ObjectId.class));
			return objectId;
		}  catch( EmptyResultDataAccessException e) {
			return null;
		}

	}

	@Override
	public void updateNewId(ObjectId objectId) throws ObjectTellerException {
		
		int result = jdbcTemplate.update(UPDATE_NEW_ID, objectId.getNewObjectId()+1,objectId.getId());		
		if(result == 0 ) {
			ObjectTellerException exception = new ObjectTellerException("Unable to generate new ID ");
			throw exception;
		}
	}

	

	
	
	
}
