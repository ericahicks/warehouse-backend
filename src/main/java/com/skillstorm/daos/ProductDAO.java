package com.skillstorm.daos;

import java.sql.SQLException;
import java.util.List;
import com.skillstorm.models.Product;

public interface ProductDAO {

	// CRUD is Create, Read, Update, Delete
	
	public List<Product> findAll() throws SQLException;
	
	public Product findById(int id) throws SQLException;
	
	public List<Product> findByName(String name) throws SQLException; // may return more than one

	public List<Product> findByCategory(String category) throws SQLException;
	
	public List<Product> findByBrand(String brand) throws SQLException;
	
	public Product save(Product product) throws SQLException;
	
	public int update(Product product) throws SQLException; 
	
	public void delete(Product product) throws SQLException;
	
	public void delete(int id) throws SQLException;
	
	public void deleteMany(int[] ids) throws SQLException;

	void deleteMany(Product[] products) throws SQLException;
}
