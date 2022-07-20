package com.skillstorm.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.skillstorm.models.InventoryItem;
import com.skillstorm.models.Product;

public class MySQLInventoryDAOImpl implements InventoryDAO {
	
	Connection conn;
	
	public MySQLInventoryDAOImpl(Connection connection) {
		this.conn  = connection;
	}

	@Override
	public List<InventoryItem> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InventoryItem findByWarehouseIdProductId(int warehouseId, int productId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InventoryItem> findByWarehouseId(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InventoryItem> findByProductId(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InventoryItem> findByProductName(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product save(InventoryItem item) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(InventoryItem item) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(InventoryItem item) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int warehouseId, int productId) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMany(int warehouseId, int[] productIds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMany(int[] warehouseIds, int productId) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMany(InventoryItem[] items) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
