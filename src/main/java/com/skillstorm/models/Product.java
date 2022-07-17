package com.skillstorm.models;

import java.io.Serializable;

public class Product implements Serializable {

	/** Serial number for identifying the class type of this user instance 
	 * when it is converted into a byte stream.	 
	 */
	private static final long serialVersionUID = 7387881080397535995L;
	
	/**
	 * A unique identification number that represents this product.
	 */
	private int id;
	/**
	 * The category that this product falls under.
	 */
	private Category category;
	/**
	 * The name of this product. (Does not need to include brand/size/description)
	 */
	private String name;
	/** 
	 * The description of this product. (Does not need to include size/brand/image)
	 */
	private String description;
	/**
	 * Size description of this product. Units and method of describing size will
	 * vary depending on the product.
	 */
	private String size;
	/** 
	 * Brand that the product is made by.
	 */
	private Brand brand;
	/**
	 * A path or url to an image of the product.
	 */
	// TODO future versions should use this for a main image 
	// in addition to a separate list of images for front/back/side views etc.
	private String imageURL;
	/**
	 * Constructor that does not set the fields of this product.
	 */
	public Product() { }
	
	/**
	 * Constructor that only sets the name of the product.
	 * @param name The name of this product.
	 */
	public Product(String name) { 
		this.name = name;
	}

	/** 
	 * Constructor that sets all but the unique id number of this product.
	 * @param category
	 * @param name
	 * @param description
	 * @param size
	 * @param brand
	 * @param imageURL
	 */
	public Product(Category category, String name, String description, String size, Brand brand, String imageURL) {
		super();
		this.category = category;
		this.name = name;
		this.description = description;
		this.size = size;
		this.brand = brand;
		this.imageURL = imageURL;
	}

	/** 
	 * Constructor that sets all the fields of this product.
	 * @param id
	 * @param category
	 * @param name
	 * @param description
	 * @param size
	 * @param brand
	 * @param imageURL
	 */
	public Product(int id, Category category, 
			String name, String description, 
			String size, Brand brand,
			String imageURL) {
		super();
		this.id = id;
		this.category = category;
		this.name = name;
		this.description = description;
		this.size = size;
		this.brand = brand;
		this.imageURL = imageURL;
	}

	/**
	 * Gets the unique identification number of this product.
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identification number of this product.
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the category that this product belongs to.
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Sets the category that this product belongs to.
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * Gets the name of this product. 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the product. (Does not need to indicate Brand or size)
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description of this product.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this product. (Does not need to specify brand/size/image but can)
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets a description of the size of the product. The method of
	 * specifying the size of the product depends on the product type
	 * so cannot be compared.
	 * @return the size
	 */
	// TODO future version should create a Size<T> interface that extends
	// Comparable<T> in the Category class so that each category when created
	// is passed in an anonymous class that specifies how to compare sizes.
	public String getSize() {
		return size;
	}

	/**
	 * Sets the size of the product to a descriptive String that specifies
	 * the size in an appropriate manner for the category of product
	 * this product falls under.
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Gets the brand type of this product.
	 * @return the brand
	 */
	public Brand getBrand() {
		return brand;
	}

	/**
	 * Sets the brand type of this product.
	 * @param brand the brand to set
	 */
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	/**
	 * Gets the path or url to an image of the product.
	 * @return the imageURL
	 */
	public String getImareURL() {
		return imageURL;
	}

	/**
	 * Sets the url or path to the image file containing an image of the product.
	 * @param imageURL the imageURL to set
	 */
	public void setImareURL(String imageURL) {
		this.imageURL = imageURL;
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
		return "Product [id=" + id + ", category=" + category + ", name=" + name + ", description=" + description
				+ ", size=" + size + ", brand=" + brand + ", imageURL=" + imageURL + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((imageURL == null) ? 0 : imageURL.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		Product other = (Product) obj;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (imageURL == null) {
			if (other.imageURL != null)
				return false;
		} else if (!imageURL.equals(other.imageURL))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}
	
	
}
