package com.skillstorm.daos;

import java.sql.SQLException;
import java.util.List;

import com.skillstorm.models.InventoryItem;
import com.skillstorm.models.Product;

public interface InventoryDAO {

	// CRUD is Create, Read, Update, Delete
	
	public List<InventoryItem> findAll() throws SQLException;
	
	public InventoryItem findByWarehouseIdProductId(int warehouseId, int productId) throws SQLException;
	
	public InventoryItem findByWarehouseIdProductName(int warehouseId, String productname) throws SQLException;
	
	public List<InventoryItem> findByWarehouseId(int id) throws SQLException;
	
	public List<InventoryItem> findByProductId(int id) throws SQLException; // may return more than one

	public List<InventoryItem> findByProductName(String name) throws SQLException;
	
	public int findInventoryTotal() throws SQLException;
	
	public int findInventoryTotalByWarehouse(int warehouseId) throws SQLException;
	
	public int findInventoryTotalByProduct(int productId) throws SQLException;

	public int findInventoryTotalByProduct(String productName) throws SQLException;
	
	public int findInventoryTotalByWarehouseProduct(int warehouseId, int productId) throws SQLException;
	
	public int findInventoryTotalByWarehouseProduct(int warehouseId, String productname) throws SQLException;
	
	public Product save(InventoryItem item) throws SQLException;
	
	public void update(InventoryItem item) throws SQLException; 
	
	public void delete(InventoryItem item) throws SQLException;
	
	public void delete(int warehouseId, int productId) throws SQLException;
	
	public void deleteMany(int warehouseId, int[] productIds) throws SQLException;
	
	public void deleteMany(int[] warehouseIds, int productId) throws SQLException;

	public void deleteMany(InventoryItem[] items) throws SQLException;

}
