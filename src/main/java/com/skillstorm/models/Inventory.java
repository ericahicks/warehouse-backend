package com.skillstorm.models;

import java.io.Serializable;

public class Inventory implements Serializable {

	/** Serial number for identifying the class type of this inventory instance 
	 * when it is converted into a byte stream.	 
	 */
	private static final long serialVersionUID = -6671709745943555138L;
	/**
	 * The warehouse is the where the inventory item/quantity being described is located.
	 */
	private Warehouse warehouse;
	/**
	 * The product is the product whose quantity and location are being described in this
	 * inventory item.
	 */
	private Product product;
	/* 
	 * The quantity is the number or units of the associated product 
	 * in stock at the particular warehouse in this referenced in this inventory item.
	 */
	private int quantity;
	/** 
	 * The minimum is the minimum quantity of the associated product
	 * that the associated warehouse wants to keep in stock
	 * as described in this inventory item.
	 */
	private int minimum;
	
	/**
	 * Constructor that does not set the fields of this inventory item.
	 */
	public Inventory() {
		this(null, null, 0, 0); // defaults for warehouse, product, quantity, minimum quantity
	}
	
	/** 
	 * Constructor that sets the warehouse, product, product quantity,
	 * but not the minimum desired quantity of the product to keep in stock. 
	 * @param warehouse
	 * @param product
	 * @param quantity
	 */
	public Inventory(Warehouse warehouse, Product product, int quantity) {
		this(warehouse, product, quantity, 0); // minimum defaults to 0
	}

	/**
	 * Constructor that sets the warehouse, product, product quantity, and minimum
	 * desired quantity of the product for this inventory item.
	 * @param warehouse
	 * @param product
	 * @param quantity
	 * @param minimum
	 */
	public Inventory(Warehouse warehouse, Product product, int quantity, int minimum) {
		super();
		this.warehouse = warehouse;
		this.product = product;
		setQuantity(quantity);
		setMinimum(minimum);
	}

	/**
	 * Gets the warehouse that this inventory item is associated with.
	 * @return the warehouse
	 */
	public Warehouse getWarehouse() {
		return warehouse;
	}

	/**
	 * Sets the warehouse that his inventory item is associated with.
	 * @param warehouse the warehouse to set
	 */
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	/**
	 * Gets the product that this inventory item refers to.
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * Sets the product that this inventory item refers to.
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
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
		return "Inventory [warehouse=" + warehouse + ", product=" + product + ", quantity=" + quantity + ", minimum="
				+ minimum + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + minimum;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((warehouse == null) ? 0 : warehouse.hashCode());
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
		Inventory other = (Inventory) obj;
		if (minimum != other.minimum)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (quantity != other.quantity)
			return false;
		if (warehouse == null) {
			if (other.warehouse != null)
				return false;
		} else if (!warehouse.equals(other.warehouse))
			return false;
		return true;
	}
	
}
