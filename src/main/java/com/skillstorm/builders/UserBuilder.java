package com.skillstorm.builders;

import java.util.HashSet;
import java.util.Set;

import com.skillstorm.models.User;

public class UserBuilder {

	private int id;
	private String username;
	private String password;
	private String email;
	private Set<Integer> permissions; // warehouseids 
	

	///////////////////////////////////////////////
	////////////////   Methods   //////////////////
	///////////////////////////////////////////////
	public UserBuilder username(String username) {
		this.username = username;
		return this;
	}
	
	public UserBuilder password(String password) {
		this.password = password;
		return this;
	}
	
	public UserBuilder email(String email) {
		this.email = email;
		return this;
	}
	
	public UserBuilder id(int id) {
		this.id = id;
		return this;
	}
	
	public UserBuilder permissions(Set<Integer> permissions) {
		Set<Integer> copy = new HashSet<>();
		copy.addAll(permissions); // shallow copy okay bc Integer is immutable
		this.permissions = copy;
		return this;
	}
	
	public User build() {
		if (isValid()) {
			return new User(this);
		}
		throw new IllegalArgumentException();
	}
	

	private boolean isValid() {
		if (id < 0) {
			return false;
		}
		if (username == null || username.isEmpty()) {
			return false;
		}
		if (email == null || email.isEmpty()) {
			return false;
		}
		return true;
	}
	
	///////////////////////////////////////////////
	////////////////   Getters   //////////////////
	///////////////////////////////////////////////
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the permissions
	 */
	public Set<Integer> getPermissions() {
		return permissions;
	}
	
}
