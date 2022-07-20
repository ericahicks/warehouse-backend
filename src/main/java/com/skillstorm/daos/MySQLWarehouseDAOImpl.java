package com.skillstorm.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.skillstorm.builders.WarehouseBuilder;
import com.skillstorm.builders.WarehouseQueryBuilder;
import com.skillstorm.conf.WarehouseDbCreds;
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
			return processResults(rs);
		}
	}

	@Override
	public Warehouse findById(int id) throws SQLException {
		String sql = "{CALL find_warehouse_by_id(?)}";
		
		try (CallableStatement stmt = conn.prepareCall(sql);) {
			stmt.setInt(1, id);
			
			// Executing the query returns a ResultSet which contains all of the values returned
			ResultSet rs = stmt.executeQuery();
			try {
				return processResults(rs).get(0);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * Private helper method that returns the state if a valid
	 * name and code are given, else returns null and prints an
	 * error message specifying which location is missing a state.
	 * @param location The warehouseid (used for error handling only)
	 * @param statename State name
	 * @param statecode Two letter state abbreviation
	 * @return
	 */
	private State getState(int location, String statename, String statecode) {
		State state;
		try {
			state = new State(statename, statecode);
		} catch (IllegalArgumentException e) {
			System.out.println("No valid state specified for warehouse " + location + " location:");
			System.out.println("    State Code: " + statecode);
			System.out.println("    State Name: " + statename);
			state = null; 
		}
		return state;
	}

	@Override
	public List<Warehouse> findByName(String name) throws SQLException {
		String sql = "{CALL find_warehouses_by_name(?)}";

		
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs);
		}
	}

	@Override
	public List<Warehouse> findByZip(String zip) throws SQLException {
		String sql = "{CALL find_warehouses_by_zip(?)}";

		
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setString(1, zip);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs);
		}
	}
	
	@Override
	public List<Warehouse> findByStreet(String street) throws SQLException {
		String sql = "{CALL find_warehouses_by_street(?)}";

		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setString(1, street);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs);
		}
	}
	
	/**
	 * Private helper method for processing a set of results
	 * from the database's warehouse table
	 * @param rs A result set returned by the database
	 * @return warehouses A List of warehouses in the result set
	 * @throws SQLException
	 */
	private List<Warehouse> processResults(ResultSet rs) throws SQLException {
		LinkedList<Warehouse> warehouses = new LinkedList<>();

		while(rs.next()) {
			State state = getState(rs.getInt("warehouseid"), 
		               rs.getString("statename"), 
		               rs.getString("statecode"));
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

	@Override
	public List<Warehouse> findByCity(String city) throws SQLException {
		String sql = "{CALL find_warehouses_by_city(?)}";

		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setString(1, city);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs);
		}
	}

	@Override
	public List<Warehouse> findByState(State state) throws SQLException {
		String sql = "{CALL find_warehouses_by_state(?)}";

		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setString(1, state.getAbbreviation());
			ResultSet rs = stmt.executeQuery();
			return processResults(rs);
		}
	}

	@Override
	public List<Warehouse> findByAvailableCapacity(int minimumAvailableCapacity) throws SQLException {
		String sql = "{CALL find_warehouses_by_available_capacity(?)}";

		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setInt(1, minimumAvailableCapacity);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs);
		}
	}

	@Override
	public List<Warehouse> findByFilters(String name, int capacity, String street, String city, State state,
			String zip) throws SQLException {
		String sql = buildQueryString(name, capacity, street, city, state, zip);
		System.out.println(sql);
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			// NOTE THIS IF-CHAIN MUST BE IN THE SAME ORDER
			// AS THE IF-CHAIN IN THE WAREHOUSE QUERY BUILDER
			int i = 1;
			if (name != null && !name.isEmpty()) {
				stmt.setString(i++, name); }
			if (street != null && !street.isEmpty()) {
				stmt.setString(i++, "%" + street + "%"); }
			if (city != null && !city.isEmpty()) {
				stmt.setString(i++, city); }
			if (state != null) {
				stmt.setString(i++, state.getAbbreviation()); }
			if (zip != null) {
				stmt.setString(i++, zip); }
			if (capacity >= 0) {
				stmt.setInt(i++, capacity); }
			ResultSet rs = stmt.executeQuery();
			return processResults(rs);
		}
	}
	
	private String buildQueryString(String name, int capacity, 
			String street, String city, State state,
			String zip) {
		String sql = new WarehouseQueryBuilder(name, capacity, 
				street, city, state, zip).build();
		return sql;
	}

	@Override
	public Warehouse save(Warehouse warehouse) throws SQLException {
		String sql = "INSERT INTO warehouse (name, capacity, " 
				+ " street, city, state, zip) VALUES "
				+ "	(?, ?, ?, ?, ?, ?)";
		
		// Start a transaction
		conn.setAutoCommit(false); // Prevents each query from immediately altering the database

		// Obtain auto incremented values with RETURN_GENERATED_KEYS
		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, warehouse.getName());
			ps.setInt(2, warehouse.getCapacity());
			ps.setString(3, warehouse.getStreet());
			ps.setString(4, warehouse.getCity());
			ps.setString(5, warehouse.getState().getAbbreviation());
			ps.setString(6, warehouse.getZip());
			
			int rowsAffected = ps.executeUpdate(); // If 0 is returned, my data didn't save
			if (rowsAffected != 0) {
				ResultSet keys = ps.getGeneratedKeys();
				if (keys.next()) {
					int key = keys.getInt(1); // Gets the auto generated key
					warehouse.setId(key);
				}
				System.out.println("Committing the save changes");
				conn.commit(); // Executes ALL queries in a given transaction
				return warehouse;
			} else {
				System.out.println("Rolling back the save changes");
				conn.rollback(); // Undoes any of the queries. Database pretends those never happened
				return null;
			}
			
		}
		
	}

	@Override
	public int update(Warehouse warehouse) throws SQLException {
		String sql = "UPDATE warehouse "
				+ "SET "
				+ "    name = ?, "
				+ "    capacity = ?, "
				+ "    street = ?, "
				+ "    city = ?, "
				+ "    state = ?, "
				+ "    zip = ? "
				+ "WHERE "
				+ "    warehouseid = ?";
		
		// Start a transaction
		conn.setAutoCommit(false); // Prevents each query from immediately altering the database
					
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, warehouse.getName());
			ps.setInt(2, warehouse.getCapacity());
			ps.setString(3, warehouse.getStreet());
			ps.setString(4, warehouse.getCity());
			ps.setString(5, warehouse.getState().getAbbreviation());
			ps.setString(6, warehouse.getZip());
			ps.setInt(7, warehouse.getId());
			
			int rowsAffected = ps.executeUpdate(); // If 0 is returned, the data didn't update
			if (rowsAffected != 0) {
				conn.commit(); // Executes ALL queries in a given transaction
			} else {
				conn.rollback(); // Undoes any of the queries. Database pretends those never happened
			}
			return rowsAffected;
			
		}
		
	}

	@Override
	public void delete(Warehouse warehouse) throws SQLException {
		delete(warehouse.getId());
		
	}

	@Override
	public void delete(int warehouseId) throws SQLException {
		String sql = "DELETE FROM warehouse WHERE warehouseid = ?";

		// Start a transaction
		conn.setAutoCommit(false); // Prevents each query from immediately altering the database
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, warehouseId);
			
			int rowsAffected = ps.executeUpdate(); // If 0 is returned, nothing returned
			if (rowsAffected != 0) {
				conn.commit(); // Executes ALL queries in a given transaction
			} else {
				conn.rollback(); // Undoes any of the queries. Database pretends those never happened
			}
			
		}
		
	}
	
	/** 
	 * For testing purposes only, not placed in automated tests
	 * because automated tests should be independent and repeatable
	 * and should not make a connection to an actual db which is slow
	 * @param args
	 */
	public static void main(String[] args) {
		// try with resource closes the connection
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			WarehouseDAO dao = new MySQLWarehouseDAOImpl(conn);
//			List<Warehouse> warehouses = dao.findByFilters(null, 90, "main", null, null, "19355");
//			List<Warehouse> warehouses = dao.findByAvailableCapacity(3);
//			for (Warehouse ware: warehouses)
//				System.out.println(ware);
			// (String name, int capacity, String street, 
			// String city, State state, String zip)
//			System.out.println(dao.findByFilters(null, 90, null, null, null, null));
//			Warehouse w = dao.save(new Warehouse("Big Warehouse", 5000, "111 First St", "Philadelphia", new State("Penn", "PA"), "19355"));
			Warehouse w1 = new Warehouse("Big Warehouse", 5000, "111 First St", "Philadelphia", new State("Penn", "PA"), "19355");
//			w.setName("Really Really Big Warehouse");
//			w.setCapacity(1000000);
			w1.setId(8);
//			System.out.println("Save returned a " + w);
			dao.delete(8);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

}
