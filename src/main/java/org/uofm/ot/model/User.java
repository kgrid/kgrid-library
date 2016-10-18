package org.uofm.ot.model;

import org.hibernate.validator.constraints.NotEmpty;

public class User {

	@NotEmpty(message = "Please enter your login ID.")
	private String username; 
	
	@NotEmpty(message = "Please enter your password.")
	private String passwd;
	
	private int id;
	
	private String first_name;
	
	private String last_name;
	
	private UserRoles role;


	public User(String username, String passwd, int id, String first_name, String last_name, UserRoles role) {
		super();
		this.username = username;
		this.passwd = passwd;
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;
	}

	public User(){}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public UserRoles getRole() {
		return role;
	}

	public void setRole(UserRoles role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", passwd=" + passwd + ", id=" + id + ", first_name=" + first_name
				+ ", last_name=" + last_name + ", role=" + role + "]";
	}

	public boolean isAdmin(){
		return this.role == UserRoles.ADMIN ;
	}
	
	public boolean isInformatician(){
		return this.role == UserRoles.INFORMATICIAN ||  this.role == UserRoles.ADMIN  ;
	}
	
	public String getFullName(){
		return first_name+ " "+ last_name ; 
	}
}
