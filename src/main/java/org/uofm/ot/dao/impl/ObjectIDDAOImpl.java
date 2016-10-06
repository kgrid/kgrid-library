package org.uofm.ot.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.uofm.ot.dao.ObjectIDDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.ObjectId;
import org.uofm.ot.knowledgeObject.ArkId;

public class ObjectIDDAOImpl implements ObjectIDDAO {
	
	private final String TABLE_NAME= "ID_TRACKER";
	
	private final String COLUMN_ID= "ID";
	
	private final String NEW_OBJECT_ID= "newObjectId";
	
	private final String CHECK_ID = "SELECT * FROM "+TABLE_NAME ;
	
	private final String UPDATE_NEW_ID = "UPDATE "+TABLE_NAME +" SET "+NEW_OBJECT_ID +"= "+NEW_OBJECT_ID +" + 1  WHERE "+COLUMN_ID+"= 1 ";
	
	private JdbcTemplate jdbcTemplate;  
	
	private static final String ID_PREFIX = "OT";

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	


	@Override
	public ArkId retrieveNewId() throws ObjectTellerException {
		try {
			ObjectId objectId = jdbcTemplate.queryForObject(CHECK_ID,new BeanPropertyRowMapper<>(ObjectId.class));
			String name = ID_PREFIX + objectId.getNewObjectId();
			ArkId arkId = new ArkId("ark:/99999/" + name);
			return arkId;
		}  catch( EmptyResultDataAccessException e) {
			return null;
		}

	}

	@Override
	public void updateNewId() throws ObjectTellerException {
		
		int result = jdbcTemplate.update(UPDATE_NEW_ID);		
		if(result == 0 ) {
			ObjectTellerException exception = new ObjectTellerException("Unable to generate new ID ");
			throw exception;
		}
	}

	

	
	
	
}
