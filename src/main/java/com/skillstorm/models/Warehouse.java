package com.skillstorm.models;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Represents a Warehouse name, location, and capacity. 
 * This class is used by the inventory and User classes in the Warehouse db.
 */
public class Warehouse implements Serializable {
	
	/** Serial number for identifying the class type of this warehouse instance 
	 * when it is converted into a byte stream.	 
	 */
	private static final long serialVersionUID = -4171652953127041774L;
	/**
	 * A unique identifiaction number to represent the warehouse instance
	 */
	private int id;
	/** 
	 * The name of the warehouse (does not need to be unique)
	 */
	private String name;
	/**
	 * Capacity of this warehouse specified simply in the number of
	 * units of a generic product that would fit in this warehouse.
	 */
	private int capacity;
	/** 
	 * The street portion of the address where this warehouse is located.
	 * The street may contain multiple lines with apt or suite number.
	 */
	private String street;
	/** 
	 * The city this warehouse is located in. 
	 */
	private String city;
	/**
	 * The state this warehouse is located in.
	 */
	private State state;
	/**
	 * The zip code this warehouse is located in.
	 * Stored as a string because it may be a 5-digit or 9-digit zip code
	 * For example: 94040 or 94040-1234
	 */
	private String zip;
	
	/**
	 * Constructor that does not set the properties of this warehouse.
	 */
	public Warehouse() { }
	
	/**
	 * Constructor that only sets the name of this warehouse instance
	 * and sets the capacity to the default of 0.
	 * @param name
	 */
	public Warehouse(String name) {
		this(name, 0);
	}
	
	public Warehouse(String name, int capacity) {
		this.name = name;
		setCapacity(capacity);
	}
	
	/**
	 * Constructor that sets all the fields except the unique identification number
	 * for this warehouse.
	 * @param name
	 * @param capacity
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Warehouse(String name, int capacity, String street, String city, State state, String zip) {
		super();
		this.name = name;
		setCapacity(capacity);
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}


	/**
	 * Constructor that sets all the properties of this warehouse.
	 * @param id
	 * @param name
	 * @param capacity
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Warehouse(int id, String name, int capacity, String street, String city, State state, String zip) {
		super();
		this.id = id;
		this.name = name;
		setCapacity(capacity);
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
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
	 * Gets the unique identification number of this Warehouse.
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identification number of this warehouse.
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the name of this warehouse
	 * @return name The name of the warehouse (not necessarily unique)
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this warehouse. It does not need to be unique.
	 * @param name The name of the warehouse.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the capacity of the warehouse as a simple integral number
	 * whose values represents the maximum number of a generic product that 
	 * can all be stored in this warehouse.
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Sets the capacity of this warehouse.
	 * Throws an IllegalArgumentException if the capacity is less than 0.
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Capacity must be nonnegative.");
		}
		this.capacity = capacity;
	}

	/**
	 * Gets the street address of this warehouse.
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Sets the street address of this warehouse. May include one or two
	 * lines if there is an apartment or suite number specified.
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Gets the name of the city this warehouse is located in.
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city that this warehouse is located in.
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the state that this warehouse is located in.
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets the state that this warehouse is located in.
	 * @param state the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Gets the zip code of the location this warehouse is located in.
	 * @return zip the zipcode of the warehouse
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * Sets the zip code of this warehouse. It may be 5 or 9 digits.
	 * Throws illegal argument exception if it is not a vaild zipcode format.
	 * @param zip The zipcode in a valid form 12345 or 12345-6789
	 */
	public void setZip(String zip) {
		if (zip == null || Pattern.matches("^\\d{5}(?:-\\d{4})?$", zip))
			this.zip = zip;
		else
			throw new IllegalArgumentException("Invalid zipcode.");
	}

	@Override
	public String toString() {
		return "Warehouse [id=" + id + ", name=" + name + ", capacity=" + capacity + ", street=" + street + ", city="
				+ city + ", state=" + state + ", zip=" + zip + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		Warehouse other = (Warehouse) obj;
		if (capacity != other.capacity)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}
	
	
	
}
