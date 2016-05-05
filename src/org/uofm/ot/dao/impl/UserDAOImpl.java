package org.uofm.ot.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.uofm.ot.dao.UserDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.User;

public class UserDAOImpl implements UserDAO {
	
	private final String TABLE_NAME= "USER_DETAILS";
	
	private final String USERNAME_COLUMN= "USERNAME";
	
	private final String PASSWD_COLUMN= "PASSWD";
	
	private final String ROLE_COLUMN= "ROLE";
	
	private final String SELECT_USER_BY_USERNAME_PASSWORD = "SELECT * FROM "+TABLE_NAME+" WHERE "+USERNAME_COLUMN+" = ? AND "+PASSWD_COLUMN+" = ?" ;
	
	private final String UPDATE_USER_PASSWORD = "UPDATE "+TABLE_NAME+" SET "+PASSWD_COLUMN+" = ? WHERE "+USERNAME_COLUMN+" = ?" ;
	
	private final String SELECT_USER_BY_USERNAME= "SELECT * FROM "+TABLE_NAME+" WHERE "+USERNAME_COLUMN+" = ? " ;
	
	private JdbcTemplate jdbcTemplate;  


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	public User getUserByUsernameAndPassword(String userName, String password) {
		User dbUser = null;
		try {
			dbUser =  jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME_PASSWORD,
					new Object[] {userName,password},new BeanPropertyRowMapper<>(User.class));
		} catch( EmptyResultDataAccessException e) {
			dbUser = null;
		}		
		return dbUser; 
	}


	@Override
	public User updatePassword(String userName, String password)  {
		User dbUser = null;
		int result = jdbcTemplate.update(UPDATE_USER_PASSWORD, password,userName);
		if(result > 0 ) {
			dbUser =  jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME,
					new Object[] {userName},new BeanPropertyRowMapper<>(User.class));
		}
		return dbUser;
	}
	
}
