package com.skillstorm.builders;

import com.skillstorm.models.InventoryItem;
import com.skillstorm.models.Product;
import com.skillstorm.models.Warehouse;

public class InventoryBuilder {

    /////////////////////////////////////////////////////////////////
	/////////////////////// Instance Variables ///////////////////////
	//////////////////////////////////////////////////////////////////
	private Warehouse warehouse;
	
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
	
	//////////////////////////////////////////////////
	///////////////////  Methods  ////////////////////
	//////////////////////////////////////////////////
	public InventoryBuilder warehouse(Warehouse warehouse) {
		this.warehouse = (Warehouse) warehouse.clone();
		return this;
	}
	
	/**
	 * 
	 * @param product
	 * @return this instance of InventoryBuilder
	 */
	public InventoryBuilder product(Product product) {
		this.product = (Product) product.clone();
		return this;
	}
	
	/**
	 * 
	 * @param quantity
	 * @return  this instance of InventoryBuilder
	 */
	public InventoryBuilder quantity(int quantity) {
		this.quantity = quantity;
		return this;
	}
	
	/**
	 * 
	 * @param minimum
	 * @return  this instance of InventoryBuilder
	 */
	public InventoryBuilder minimum(int minimum) {
		this.minimum = minimum;
		return this;
	}
	
	
	public InventoryItem build() throws IllegalArgumentException {
		if (isValid()) {
			return new InventoryItem(this);
		}
		throw new IllegalArgumentException();
	}
	
	private boolean isValid() {
		if (product == null) {
			return false;
		}
		if (quantity < 0) {
			return false;
		}
		if (minimum < 0) {
			return false;
		}
		return true;
	}

	////////////////////////////////////////////////////////
	/////////////////////   Getters   //////////////////////
	////////////////////////////////////////////////////////
	public Warehouse getWarehouse() {
		return (Warehouse) this.warehouse.clone();
	}
	
	/**
	 * @return the product
	 */
	public Product getProduct() {
		return (Product) this.product.clone();
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @return the minimum
	 */
	public int getMinimum() {
		return minimum;
	}

}
