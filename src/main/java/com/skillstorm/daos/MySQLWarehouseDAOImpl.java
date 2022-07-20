package com.skillstorm.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.skillstorm.builders.WarehouseBuilder;
import com.skillstorm.models.Brand;
import com.skillstorm.models.Category;
import com.skillstorm.models.Product;
import com.skillstorm.models.State;
import com.skillstorm.models.Warehouse;

public class MySQLWarehouseDAOImpl implements WarehouseDAO {
	
	Connection conn;
	WarehouseBuilder builder = new WarehouseBuilder();

	public MySQLWarehouseDAOImpl(Connection connection) {
		this.conn  = connection;
	}
	
	@Override
	public List<Warehouse> findAll() throws SQLException {

		String sql = "{CALL find_all_warehouses}";
		
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			ResultSet rs = stmt.executeQuery();
			LinkedList<Warehouse> warehouses = new LinkedList<>();
			
			while(rs.next()) {
				State state = new State(rs.getString("statename"), rs.getString("statecode"));
				Warehouse warehouse = builder
						.id(rs.getInt("warehouseid"))
						.name(rs.getString("name"))
						.street(rs.getString("street"))
						.city(rs.getString("city"))
						.state(state)
						.zip(rs.getString("zip"))
						.capacity(rs.getInt("capacity"))
						.build();
				warehouses.add(warehouse);
			}
			return warehouses;
		}
	}

	@Override
	public Warehouse findById(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Warehouse> findByName(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Warehouse> findByZip(String zip) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Warehouse> findByCity(String city) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Warehouse> findByState(State state) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Warehouse> findByFilters(String name, int capacity, String street, String city, State state,
			String zip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Warehouse save(Warehouse warehouse) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Warehouse warehouse) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Warehouse warehouse) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int warehouseId) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Warehouse> findByAvailableCapacity(int minimumAvailableCapacity) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
