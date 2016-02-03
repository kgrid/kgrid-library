package org.uofm.ot.dao;

import org.uofm.ot.model.User;

public interface UserDAO {
	
	public User getUserByUsernameAndPassword(String userName, String password) ;
	
	public User updatePassword(String userName,String password) ;
}
