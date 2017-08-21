package edu.umich.lhs.library.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


/**
 * @author nbahulek
 *
 */
public class LibraryUser extends User {
	
	private UserProfile profile ; 
	
	public LibraryUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
			UserProfile profile) {
		super(username, password, authorities);
		this.profile = profile;
	}

	public UserProfile getProfile() {
		return profile;
	}


	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public LibraryUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public LibraryUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	
	public LibraryUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, UserProfile profile) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.profile = profile ; 
	}
	
	public String getFullName(){
		return profile.getFirst_name()+ " "+ profile.getLast_name() ; 
	}
	
	public String getFirst_name() {
		return profile.getFirst_name();
	}
	
	public String getLast_name() {
		return profile.getLast_name();
	}
	
	public String getRole() {
		GrantedAuthority [] grantedAuthority = {}; 
		grantedAuthority = getAuthorities().toArray(grantedAuthority);
		return grantedAuthority[0].getAuthority();
	}
	
	public boolean isInformatician(){
		boolean success = false;
		GrantedAuthority [] grantedAuthority = {}; 
		grantedAuthority = getAuthorities().toArray(grantedAuthority);
		for (GrantedAuthority auth : grantedAuthority) {
			if(auth.getAuthority().equals(UserRoles.INFORMATICIAN.toString() ) || auth.getAuthority().equals(UserRoles.ADMIN.toString() )) {
				success = true ;
			}
		}
		return success ; 
	}
	
	public boolean isAdmin(){
		boolean success = false;
		GrantedAuthority [] grantedAuthority = {}; 
		grantedAuthority = getAuthorities().toArray(grantedAuthority);
		for (GrantedAuthority auth : grantedAuthority) {
			if(auth.getAuthority().equals(UserRoles.ADMIN.toString() )) {
				success = true ;
			}
		}
		return success;
	}
	
	public int getId() {
		return profile.getId();
	}
}
