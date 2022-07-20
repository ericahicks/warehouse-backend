package com.skillstorm.daos;

import java.sql.SQLException;
import java.util.List;

import com.skillstorm.models.Warehouse;
import com.skillstorm.models.State;

public interface WarehouseDAO {

	// CRUD is Create, Read, Update, Delete
	
	public List<Warehouse> findAll() throws SQLException;
	
	public Warehouse findById(int id) throws SQLException;
	
	public List<Warehouse> findByName(String name) throws SQLException; // may return more than one

	public List<Warehouse> findByStreet(String street) throws SQLException;
	
	public List<Warehouse> findByZip(String zip) throws SQLException;
	
	public List<Warehouse> findByCity(String city) throws SQLException;
	
	public List<Warehouse> findByState(State state) throws SQLException;
	
	public List<Warehouse> findByAvailableCapacity(int minimumAvailableCapacity) throws SQLException;
	
	public List<Warehouse> findByFilters(String name, int capacity, String street, String city, State state, String zip) throws SQLException;
	
	public Warehouse save(Warehouse warehouse) throws SQLException;
	
	public int update(Warehouse warehouse) throws SQLException; 
	
	public void delete(Warehouse warehouse) throws SQLException;
	
	public void delete(int warehouseId) throws SQLException;

}
