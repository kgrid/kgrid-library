package org.uofm.ot;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.uofm.ot.model.OTUser;
import org.uofm.ot.model.UserProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class CustomizedUserManager extends JdbcUserDetailsManager  {
	
	private static final String SELECT_USER_PROFILE_BY_USERNAME = "select id, first_name, last_name from user_profiles where username= ?";
	
	private static final String CREATE_USER_PROFILE = "insert into user_profiles(username, first_name, last_name) values (?, ?, ?);"; 
	
	private static final String UPDATE_USER_PROFILE = "update user_profiles set first_name = ? , last_name = ? where username = ? ;"; 
	
	private static final String UPDATE_USER_NAME = "update  users set username = ? where username = ? ;";
	
	private static final String GET_ALL_USERS = "select * from users";

	private static final String GET_COUNT_USERS = "select count(*) from users";

	private static final String GET_USERNAME_BY_ID = "select users.username from users, user_profiles where users.username = user_profiles.username and user_profiles.id = ? ;" ; 
	
	
	@Override
	public OTUser loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails uDetails= super.loadUserByUsername(username);
		
		UserProfile profile = getUserProfile(username);
		
		OTUser otUser = new OTUser(uDetails.getUsername(), uDetails.getPassword(), uDetails.getAuthorities(), profile);
		
		return otUser;
	}

	
	
	public void createUser(OTUser user) throws Exception {
		
		super.createUser(user);
		
		createUserProfile(user);
	}

	public void updateUser(OTUser user) throws Exception {
		super.updateUser(user);

		updateUserProfile(user);
	}

    public int ping() {
		return getJdbcTemplate().queryForObject(GET_COUNT_USERS, null, Integer.class);
    }


    private UserProfile getUserProfile(String username) {
		
		return getJdbcTemplate().queryForObject(SELECT_USER_PROFILE_BY_USERNAME, new Object[]{username},new BeanPropertyRowMapper<>(UserProfile.class));
	}
	
	private void createUserProfile(OTUser otUser) throws Exception  {
		
		if(otUser.getProfile() != null ) {
			int success = getJdbcTemplate().update(CREATE_USER_PROFILE, new Object[] {otUser.getUsername(),otUser.getProfile().getFirst_name(),otUser.getProfile().getLast_name()});
			if(success == 0)
				throw new Exception("Unable to add user profile for "+otUser);
		}
	}
	
	
	private void updateUserProfile(OTUser otUser) throws Exception  {
		
		if(otUser.getProfile() != null) {
			int success = getJdbcTemplate().update(UPDATE_USER_PROFILE, new Object[] { otUser.getProfile().getFirst_name(),otUser.getProfile().getLast_name(),otUser.getUsername()});
			if(success == 0)
				throw new Exception("Unable to updtate user profile for "+otUser);
		}
	}

	public List<OTUser> getUsers() {
		return getJdbcTemplate().query(GET_ALL_USERS, new RowMapper<OTUser>() {
			public OTUser mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				String username = rs.getString(1);
				String password = rs.getString(2);
				boolean enabled = rs.getBoolean(3);
				List<GrantedAuthority> authorities = loadUserAuthorities(username);
				UserProfile profile = getUserProfile(username);
				return new OTUser(username, password, enabled, true, true, true,
						authorities,profile);
			}

		});
		
		 
	}
	
	public void updateUserName(String oldUserName, String newUserName) {
		getJdbcTemplate().update(UPDATE_USER_NAME,new Object[]{newUserName,oldUserName});
	}
	
	public String getUsernameById(int id) {
		return getJdbcTemplate().queryForObject(GET_USERNAME_BY_ID,new Object[]{id} ,String.class); 
	}
}

