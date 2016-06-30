package org.uofm.ot.dao;

import java.util.List;

import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.User;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public interface UserDAO {
	
	public User getUserByUsernameAndPassword(String userName, String password) ;
	
	public User updateUser(User user) ;
	
	public List<User> getAllUsers();
	
	public User addNewUser(User user) throws ObjectTellerException,  MySQLIntegrityConstraintViolationException;
	
	public void deleteUser(int userID) throws ObjectTellerException;
}
