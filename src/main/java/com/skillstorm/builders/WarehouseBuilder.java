package com.skillstorm.builders;

import java.util.ArrayList;

import com.skillstorm.models.InventoryItem;
import com.skillstorm.models.State;
import com.skillstorm.models.Warehouse;

public class WarehouseBuilder {
	////////////////////////////////////////////
	//////////// Instance Variables ////////////
	////////////////////////////////////////////
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
	 * A list of inventory items that together describe what is 
	 * currently in stock at this warehouse.
	 */
	private ArrayList<InventoryItem> inventory;
	/**
	 * Constructor that does not set the properties of this warehouse.
	 */
	

	///////////////////////////////////////////////////
	//////////////////  Methods  //////////////////////
	///////////////////////////////////////////////////
	
	public WarehouseBuilder id(int id) {
		this.id = id;
		return this;
	}
	
	public WarehouseBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	public WarehouseBuilder capacity(int capacity) {
		this.capacity = capacity;
		return this;
	}
	
	public WarehouseBuilder street(String street) {
		this.street = street;
		return this;
	}
	
	public WarehouseBuilder city(String city) {
		this.city = city;
		return this;
	}
	
	public WarehouseBuilder state(State state) {
		this.state = state;
		return this;
	}
	
	public WarehouseBuilder zip(String zip) {
		this.zip = zip;
		return this;
	}

	public WarehouseBuilder inventory(ArrayList<InventoryItem> items) {
		this.inventory = new ArrayList<>();
		for (InventoryItem item : items) {
			this.inventory.add((InventoryItem) item.clone());
		}
		return this;
	}
	
	public Warehouse build() {
		if (isValid()) {
			return new Warehouse(this);
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
		if (capacity < 0) {
			return false;
		}
		return true;
	}
	
	///////////////////////////////////////////////////
	//////////////////  Getters  //////////////////////
	///////////////////////////////////////////////////
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @return the inventory
	 */
	public ArrayList<InventoryItem> getInventory() {
		ArrayList<InventoryItem> deepCopy = new ArrayList<>();
		for (InventoryItem item : this.inventory) {
			deepCopy.add((InventoryItem) item.clone());
		}
		return deepCopy;
	}
}
