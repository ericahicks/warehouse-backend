package com.skillstorm.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.skillstorm.models.User;
import com.skillstorm.models.Warehouse;

public interface UserDAO {

	// CRUD is Create, Read, Update, Delete
	
	public List<User> findAll() throws SQLException;
	
	public List<User> findByWarehouse(int warehouseid) throws SQLException;
	
	public List<User> findByWarehouse(Warehouse warehouse) throws SQLException;
	
	public User findById(int id) throws SQLException;
	
	public User findByUsername(String username) throws SQLException; // may return more than one

	public User findByEmail(String email) throws SQLException;
	
	public Set<Integer> findPermissions(int userid) throws SQLException;
	
	public User save(User user) throws SQLException;
	
	public int addPermission(int warehouseid) throws SQLException;
	
	public int addPermissions(int[] warehouseids) throws SQLException;
	
	public int update(User user) throws SQLException; 
	
	public void delete(User user) throws SQLException;
	
	public void delete(int userid) throws SQLException;


}
