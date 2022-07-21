package com.skillstorm.models;

import java.io.Serializable;
import java.util.regex.*;
/**
 * Represents a state in the United States with an official name
 * and postal code abbreviation.
 */
public final class State implements Serializable {
	

	///////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////  Class and Instance Variables    ////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////// 
	 
	/** Serial number for identifying the class type of this state instance 
	 * when it is converted into a byte stream.	 
	 */
	private static final long serialVersionUID = -6085875777723920784L;

	/** 
	 * Official name of this state in the United States of America.
	 */
	private final String name;
	
	/** 
	 * Two letter abbreviation for this state.
	 * Matches the official postal code for this state.
	 */
	private final String abbreviation;
	
	///////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// Constructors  /////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////// 
	public State() {
		this.name = "Pennsylvania";
		this.abbreviation = "PA";
	}
	
	/** 
	 * Constructor that sets the fields of this state object.
	 * May throw an IllegalArgumentException if the abbreviation is not
	 * two letters.
	 * @param name Official name of state in the US
	 * @param abbreviation Valid US Postal Code 2-letter state abbreviation
	 */
	public State(String name, String abbreviation) {
		if (name == null)
			throw new IllegalArgumentException("State name cannot be null");
		this.name = name;
		
		if (abbreviation == null)
			throw new IllegalArgumentException("State abbreviation cannot be null");
		
		if (abbreviation.length() != 2 
				|| !Pattern.matches("[a-zA-Z]{2}", abbreviation)) {
			throw new IllegalArgumentException("State abbreviation must be 2 letters.");
		} else {
			this.abbreviation = abbreviation.toUpperCase();
		}
	}
	

	///////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// Getters and Setters //////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////// 
	
	/**
	 * Gets the name of this state
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the abbreviation for this state used by the official US Postal Service
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}


	/**
	* The serial number used to identify the class type of instances of the class
	* when represented as a byte stream. 
	* @return serialVersionUID
	*/
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	///////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////// Methods  /////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////// 

	@Override
	public String toString() {
		return "State [name=" + name + ", abbreviation=" + abbreviation + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abbreviation == null) ? 0 : abbreviation.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		State other = (State) obj;
		if (abbreviation == null) {
			if (other.abbreviation != null)
				return false;
		} else if (!abbreviation.equals(other.abbreviation))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
