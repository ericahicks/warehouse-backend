package com.skillstorm.models;

import java.io.Serializable;

/**
 * Represents a company's brand name for a line of products.
 */
public final class Brand implements Serializable, Cloneable {
	/** Serial number for identifying the class type of this brand instance 
	 * when it is converted into a byte stream.	 
	 */
	private static final long serialVersionUID = -5594426839189200866L;
	/**
	 * A unique identification number for distinguishing this brand
	 * from other brands.
	 */
	private final int id;
	/**
	 * A unique name that this brand identifies by.
	 */
	private final String name;

	/**
	 * Constructor that sets the name and unique identification number
	 * of this brand.
	 * @param id
	 * @param name
	 */
	public Brand(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	@Override
	public Object clone() {
	    try {
	        return (Brand) super.clone();
	    } catch (CloneNotSupportedException e) {
	        return new Brand(id, name);
	    }
	}

	/**
	 * Gets the unique identification number that identifies this brand.
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the name that his brand identifies by.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	* The serial number used to identify the class type of instances of the class
	* when represented as a byte stream. 
	* @return serialVersionUID
	*/
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + "]";
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
		Brand other = (Brand) obj;
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
