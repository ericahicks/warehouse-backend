package com.skillstorm.models;

import java.io.Serializable;

public class InventoryItem implements Serializable, Cloneable {

	/** Serial number for identifying the class type of this inventory instance 
	 * when it is converted into a byte stream.	 
	 */
	private static final long serialVersionUID = -6671709745943555138L;
	/**
	 * The product is the product whose quantity is being described in this
	 * inventory item.
	 */
	private Product product;
	/* 
	 * The quantity is the number or units of the associated product 
	 * that are in stock.
	 */
	private int quantity;
	/** 
	 * The minimum is the minimum quantity of the associated product
	 * should be kept in stock.
	 */
	private int minimum;
	
	/**
	 * Constructor that does not set the fields of this inventory item.
	 */
	public InventoryItem() {
		this(null, 0, 0); // defaults for product, quantity, minimum quantity
	}
	
	/** 
	 * Constructor that sets the product, product quantity,
	 * but not the minimum desired quantity of the product to keep in stock. 
	 * @param product
	 * @param quantity
	 */
	public InventoryItem(Product product, int quantity) {
		this(product, quantity, 0); // minimum defaults to 0
	}

	/**
	 * Constructor that sets the product, product quantity, and minimum
	 * desired quantity of the product for this inventory item.
	 * @param product
	 * @param quantity
	 * @param minimum
	 */
	public InventoryItem(Product product, int quantity, int minimum) {
		super();
		this.product = product;
		setQuantity(quantity);
		setMinimum(minimum);
	}
	
	@Override
	public Object clone() {
	    try {
	        return (InventoryItem) super.clone();
	    } catch (CloneNotSupportedException e) {
	        return new InventoryItem(getProduct(), this.quantity, this.minimum);
	    }
	}

	/**
	 * Gets the product that this inventory item refers to.
	 * Returns a deep copy not a reference to the internal product.
	 * @return the product
	 */
	public Product getProduct() {
	    return (Product) this.product.clone();
	}

	/**
	 * Sets the product that this inventory item refers to.
	 * Uses clone to avoid leaving an available reference to this internal property.
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = (Product) product.clone();
	}

	/**
	 * Gets the quantity in number of units of the product associated with this 
	 * inventory item.
	 * @return the quantity A nonnegative integer number of whole units of this product.
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the number of whole units of the associated product to keep in stock at the warehouse.
	 * @param quantity the number of whole units of a product to keep in stock (nonnegative)
	 */
	public void setQuantity(int quantity) throws IllegalArgumentException {
		if (quantity >= 0) 
			this.quantity = quantity;
		else 
			throw new IllegalArgumentException("Quantity cannot be a negative value.");
	}

	/**
	 * Gets the desired minimum number of whole units of the associated product
	 * to keep in stock at the warehouse this inventory item refers to.
	 * @return the minimum
	 */
	public int getMinimum() {
		return minimum;
	}

	/**
	 * Sets the desired minimum number of whole units of the associate product
	 * to keep in stock at the warehouse that this inventory item refers to. 
	 * @param quantity the minimum quantity of product to keep in stock
	 */
	public void setMinimum(int quantity) throws IllegalArgumentException {
		if (quantity >= 0) 
			this.minimum = quantity;
		else 
			throw new IllegalArgumentException("Quantity cannot be a negative value.");
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
		return "Inventory [product=" + product + ", quantity=" + quantity + ", minimum="
				+ minimum + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + minimum;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + quantity;
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
		InventoryItem other = (InventoryItem) obj;
		if (minimum != other.minimum)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}
	
}
