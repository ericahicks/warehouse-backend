package com.skillstorm.models;

import java.io.Serializable;
import java.util.Set;
import java.util.regex.Pattern;

import com.skillstorm.builders.UserBuilder;

/** 
 * Represents a user that wants to login to the warehouse
 * inventory system. 
 */
public class User implements Serializable {

	/** Serial number for identifying the class type of this user instance 
	 * when it is converted into a byte stream.	 
	 */
	private static final long serialVersionUID = -1322462729309865637L;
	
	/** A unique userid number. (This will be generated not set manually.)*/
	private int id;
	/** A unique username not their email or legal name. */
	private String username;
	/** A password for authentication purposes. */
	private String password;
	/** A unique email for password reset purposes. 
	 * (There cannot be multiple accounts created with the same email.) */
	private String email;
	/** A list of warehouse ids used for authorization. 
	 * Specifies which warehouse data the user has access to. */
	private Set<Integer> permissions;
	
	/** Constructor that leaves fields uninitialized. */
	public User() {
		super();
	}

	/** Constructor that takes in name, password, and email. 
	 * @param name The unique username of the user.
	 * @param password The password associated with the user account.
	 * @param email The unique email associated with the user account.
	 * */
	public User(String name, String password, String email) {
		super();
		this.username = name;
		this.password = password;
		setEmail(email);
	}
	
	/**
	 * Constructor that takes in the username, password, email, and permissions list.
	 * @param username
	 * @param password
	 * @param email
	 * @param permissions List of warehouse ids used for authorization.
	 */
	public User(String username, String password, String email, Set<Integer> permissions) {
		super();
		this.username = username;
		this.password = password;
		setEmail(email);
		this.permissions = permissions;
	}

	/** 
	 * Constructor that takes in all the fields
	 * @param id
	 * @param username
	 * @param password
	 * @param email
	 * @param permissions List of warehouse ids that the user has access to.
	 */
	public User(int id, String username, String password, String email, Set<Integer> permissions) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		setEmail(email);
		this.permissions = permissions;
	}
	
	public User(UserBuilder builder) {
		this.id = builder.getId();
		this.username = builder.getUsername();
		this.password = builder.getPassword();
		setEmail(builder.getEmail());
		this.permissions = builder.getPermissions();
	}

	/**
	 * Gets the unique userid that was generated when the user account is created.
	 * @return id The unique userid generated when the user account is created.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the userid associated with the user account. 
	 * This value should be unique among all the user accounts.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the username associated with this user account.
	 * @return username The unique username chosen by the user when creating a user account.
	 */
	public String getUsername() {
		return username;
	}

	/** 
	 * Sets the username of this user account.
	 * @param name A unique username to identify the user by.
	 */
	public void setUsername(String name) {
		this.username = name;
	}

	/**
	 * Gets the user account password. 
	 * @return password The password used for authentication of this user account.
	 */
	public String getPassword() {
		return password;
	}

	/** 
	 * Sets the password used for authentication of this user account.
	 * @param password The password used for authentication.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/** 
	 * Gets the email associated with this user account.
	 * The email is unique among all user accounts.
	 * @return email A unique email for communications with the user of the account and authentication.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email associated with this user account.
	 * The email is used for communication with the user and for authentication purposes.
	 * @param email The unique email associated with this user account.
	 */
	public void setEmail(String email) {
		// RFC 5322 compliant regex from http://emailregex.com/
		String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		//if (email == null || !Pattern.matches(regex, email))
		//	throw new IllegalArgumentException("Invalid email provided.");
		this.email = email;
	}
	
	/**
	* The serial number used to identify the class type of instances of the class
	* when represented as a byte stream. 
	* @return serialVersionUID
	*/
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets a list of warehouse ids that describe which warehouse data
	 * this user account has access to.
	 * @return permissions A list of warehouse ids this user account has access to.
	 */
	public Set<Integer> getPermissions() {
		return permissions;
	}

	/** 
	 * Sets which warehouses' data this user has access to. 
	 * @param permissions List of warehouse ids this user has access to.
	 */
	public void setPermissions(Set<Integer> permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * Adds a warehouse id so that this user will have access 
	 * to that warehouse's data.
	 * @param permission The id of a warehouse this user will now have access to.
	 */
	public void addPermission(Integer permission) {
		this.permissions.add(permission);
	}
	
	/**
	 * Removes the given warehouse id from the permissions list
	 * so this user no longer has acess to that warehouse's data.
	 * @param permission A warehouse id number.
	 */
	public void removePermission(Integer permission) {
		this.permissions.remove(permission);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", permissions="
				+ permissions + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + id;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
	
	
	
}
