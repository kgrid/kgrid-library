package org.uofm.ot.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.uofm.ot.dao.UserDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.User;
import org.uofm.ot.model.UserRoles;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class UserDAOImpl implements UserDAO {
	
	private final String TABLE_NAME= "USER_DETAILS";
	
	private final String USERNAME_COLUMN= "USERNAME";
	
	private final String PASSWD_COLUMN= "PASSWD";
	
	private final String ROLE_COLUMN= "ROLE";
	
	private final String FIRST_NAME_COLUMN = "FIRST_NAME";
	
	private final String LAST_NAME_COLUMN = "LAST_NAME";
	
	private final String ID_COLUMN = "ID";
	
	private final String SELECT_USER_BY_USERNAME_PASSWORD = "SELECT * FROM "+TABLE_NAME+" WHERE "+USERNAME_COLUMN+" = ? AND "+PASSWD_COLUMN+" = ?" ;
	
	private final String UPDATE_USER = "UPDATE "+TABLE_NAME+" SET "+ USERNAME_COLUMN+" = ? , " + FIRST_NAME_COLUMN + " = ? , "+ LAST_NAME_COLUMN + " = ? ," + ROLE_COLUMN + " = ? WHERE "+ID_COLUMN+" = ? " ;
	
	private final String SELECT_USER_BY_USERNAME= "SELECT * FROM "+TABLE_NAME+" WHERE "+USERNAME_COLUMN+" = ? " ;
	
	private final String SELECT_ALL_USER = "SELECT * FROM "+TABLE_NAME ;
	
	private final String ADD_NEW_USER = "INSERT INTO "+TABLE_NAME+" ( "+ USERNAME_COLUMN + " , " + PASSWD_COLUMN + " , "+ ROLE_COLUMN + " , " + FIRST_NAME_COLUMN +" , " + LAST_NAME_COLUMN + " ) values (?, ?, ?, ?, ?); ";

	private final String DELETE_USER_BY_ID = "DELETE FROM "+TABLE_NAME+" WHERE "+ID_COLUMN+" = ? ";
	
	private final String SELECT_USER_BY_ID = "SELECT * FROM "+TABLE_NAME+" WHERE "+ID_COLUMN+" = ? ";
	
	private final String COUNT_USERS_BY_ROLES = "SELECT count(*) FROM "+TABLE_NAME+" WHERE " + ROLE_COLUMN +" = ?";
	
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
	public User updateUser(User user)  {
		User dbUser = null;
		int result = jdbcTemplate.update(UPDATE_USER,user.getUsername(),user.getFirst_name(),user.getLast_name(),user.getRole().toString() ,user.getId());
		if(result > 0 ) {
			dbUser =  jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME,
					new Object[] {user.getUsername()},new BeanPropertyRowMapper<>(User.class));
		}
		return dbUser;
	}


	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User> ();
		List<Map<String,Object>> rows =  jdbcTemplate.queryForList(SELECT_ALL_USER );
		for (Map row : rows) {
			User user = new User();
			user.setFirst_name((String)row.get("first_name"));
			user.setLast_name((String)row.get("last_name"));
			user.setId((Integer)row.get("id"));
			user.setPasswd((String)row.get("passwd"));
			String role = (String)row.get("role");
			user.setRole(UserRoles.valueOf(role));
			user.setUsername((String)row.get("username"));
			users.add(user);
		}
		return users;
	}


	@Override
	public User addNewUser(User user) throws ObjectTellerException {
		User dbUser = null; 
		int success = jdbcTemplate.update(ADD_NEW_USER, new Object[] { user.getUsername(), user.getPasswd() , user.getRole().toString() , user.getFirst_name() , user.getLast_name()
		});

		if(success == 0)
			throw new ObjectTellerException("Unable to add user "+user);

		if(success > 0 ) {
			dbUser =  jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME,
					new Object[] {user.getUsername()},new BeanPropertyRowMapper<>(User.class));
		}
		
		return dbUser ; 
	}


	@Override
	public void deleteUser(int userId) throws ObjectTellerException {

		User toBeDeleted =  jdbcTemplate.queryForObject(SELECT_USER_BY_ID,
				new Object[] {userId},new BeanPropertyRowMapper<>(User.class));

		if(toBeDeleted != null && toBeDeleted.getRole() == UserRoles.ADMIN ) {
			
			int admins = jdbcTemplate.queryForObject(COUNT_USERS_BY_ROLES, new Object[] { UserRoles.ADMIN.toString() },Integer.class);
			
			if(admins > 1) {

				int success = jdbcTemplate.update(DELETE_USER_BY_ID, userId);

				if(success == 0 || success < 0 ) {
					throw new ObjectTellerException("Unable to delete user with id: "+userId);

				}
			} else {
				throw new ObjectTellerException("This is the last Admin user. You need to create a new Admin user to delete this user .");
			}
		}
	}
	
	
}
