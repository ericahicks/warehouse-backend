package com.skillstorm.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.skillstorm.builders.UserBuilder;
import com.skillstorm.models.User;
import com.skillstorm.models.Warehouse;

public class MySQLUserDAOImpl implements UserDAO {
	
	Connection conn;
	UserBuilder builder = new UserBuilder();
	
	public MySQLUserDAOImpl(Connection connection) {
		this.conn  = connection;
	}

	@Override
	public List<User> findAll() throws SQLException {

		String sql = "SELECT userid, username, password, email FROM user";
		
		try (Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			return processResults(rs);
		}
	}

	@Override
	public List<User> findByWarehouse(Warehouse warehouse) throws SQLException {
		return findByWarehouse(warehouse.getId());
	}
	
	@Override
	// TODO currently is not getting the permissions when fetching the users
	public List<User> findByWarehouse(int warehouseid) throws SQLException {
		String sql = "{CALL find_users_by_warehouse(?)}";
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setInt(1, warehouseid);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs);
		}
		// ADD LOOP TO FETCH PERMISSIONS FOR EACH USER HERE!
	}

	@Override
	public User findById(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByUsername(String username) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByEmail(String email) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User save(User user) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(User user) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(User user) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int userid) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	

	/**
	 * Private helper method for processing a set of results
	 * from the database's warehouse table
	 * @param rs A result set returned by the database
	 * @return warehouses A List of warehouses in the result set
	 * @throws SQLException
	 */
	private List<User> processResults(ResultSet rs) throws SQLException {
		LinkedList<User> users = new LinkedList<>();

		while(rs.next()) {
			Set<Integer> permissions = new HashSet<>();
			permissions.add(rs.getInt("warehouseid"));
			User user = builder
					.id(rs.getInt("userid"))
					.username(rs.getString("username"))
					.email(rs.getString("email"))
					.password(rs.getString("password"))
					.permissions(permissions)
					.build();
			users.add(user);
		}
		return users;
	}

	@Override
	public Set<Integer> findPermissions(int userid) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addPermission(int warehouseid) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addPermissions(int[] warehouseids) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
