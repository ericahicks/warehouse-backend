package com.skillstorm.models;

import java.io.Serializable;

/** 
 * Represents a category of products that are stored in the warehouses.
 * Used to provide sorting for the inventory system. 
 */
public final class Category implements Serializable, Cloneable {

	/** Serial number for identifying the class type of this category instance 
	 * when it is converted into a byte stream.	 
	 */
	private static final long serialVersionUID = -7837858182385772283L;
	/** 
	 * Unique id number that identifies this category from all the other categories.
	 */
	private final int id;
	/** 
	 * Unique descriptive name that identifies this category from all the other categories.
	 */
	private final String name;
	
	public Category() { 
		this.id = 0;
		this.name = "";
	}
	
	/** 
	 * Constructor that sets the name and the id of this category.
	 * @param id
	 * @param name
	 */
	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public Object clone() {
	    try {
	        return (Category) super.clone();
	    } catch (CloneNotSupportedException e) {
	        return new Category(id, name);
	    }
	}

	/**
	 * Gets the unique identification number that represents this category.
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the unique descriptive name of this category.
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	* The serial number used to identify the class type of instances of this class
	* when represented as a byte stream. 
	* @return serialVersionUID
	*/
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Category other = (Category) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
