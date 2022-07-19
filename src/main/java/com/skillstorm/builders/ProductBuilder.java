package com.skillstorm.builders;

import com.skillstorm.models.Brand;
import com.skillstorm.models.Category;
import com.skillstorm.models.Product;

public class ProductBuilder {
     /////////////////////////////////////////////////////////////////
	/////////////////////// Instance Variables ///////////////////////
	//////////////////////////////////////////////////////////////////
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
	
	public ProductBuilder id(int id) {
		this.id = id;
		return this;
	}
	
	public ProductBuilder category(Category category) {
		this.category = category;
		return this;
	}
	
	public ProductBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	public ProductBuilder description(String description) {
		this.description = description;
		return this;
	}
	
	
	public ProductBuilder size(String size) {
		this.size = size;
		return this;
	}
	
	public ProductBuilder brand(Brand brand) {
		this.brand = brand;
		return this;
	}
	
	public ProductBuilder imageURL(String imageURL) {
		this.imageURL = imageURL;
		return this;
	}
	
	public Product build() throws IllegalArgumentException {
		if (isValid()) {
			return new Product(this);
		}
		throw new IllegalArgumentException();
	}
	
	private boolean isValid() {
		if (id < 0) {
			return false;
		}
		if (name == null || name.isEmpty()) {
			return false;
		}
		return true;
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @return the brand
	 */
	public Brand getBrand() {
		return brand;
	}

	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}

	public static void main(String[] args) {
		ProductBuilder builder = new ProductBuilder();
		Product apple = builder.id(1).name("apple").build(); // don't have to keep track of order of properties
		System.out.println(apple);
	}
}
