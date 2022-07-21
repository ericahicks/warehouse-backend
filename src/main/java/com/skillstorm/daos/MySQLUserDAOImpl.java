package com.skillstorm.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.skillstorm.builders.UserBuilder;
import com.skillstorm.conf.WarehouseDbCreds;
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
	public List<User> findByWarehouse(int warehouseid) throws SQLException {
		String sql = "{CALL find_users_by_warehouse(?)}";
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setInt(1, warehouseid);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs);
		}
	}

	@Override
	public User findById(int id) throws SQLException, IndexOutOfBoundsException {
		String sql = "SELECT userid, username, password, email FROM user WHERE userid = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs).get(0);
		}
	}

	@Override
	public User findByUsername(String username) throws SQLException {
		String sql = "SELECT userid, username, password, email FROM user WHERE username = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs).get(0);
		}
	}

	@Override
	public User findByEmail(String email) throws SQLException {
		String sql = "{CALL find_user_by_email(?)}";
		
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			return processResults(rs).get(0);
		}
	}

	@Override
	public User save(User user) throws SQLException {
		Set<Integer> permissions = user.getPermissions();
		String sql = "INSERT INTO user (username, password, email) " 
				+ " VALUES	(?, ?, ?)";
		String sql2 = "INSERT INTO permission (userid, warehouseid) VALUES (?, ?)";
		// Start a transaction 
		conn.setAutoCommit(false); 
		// Obtain auto incremented value with RETURN_GENERATED_KEYS
		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			  PreparedStatement ps2 = conn.prepareStatement(sql2)) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getEmail());
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			// If success, save permissions using new key
			if (keys.next()) {
				int key = keys.getInt(1); // Gets the auto generated key
				user.setId(key);
				for (int warehouseid : permissions) {
					ps2.setInt(1, key);
					ps2.setInt(2, warehouseid);
					ps2.addBatch();
				}
				ps2.executeBatch();
			}
			conn.commit();
			user.setPermissions(permissions);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			return null;
		}
	}

	@Override
	public User save(User user, Set<Integer> permissions) throws SQLException {
		String sql = "INSERT INTO user (username, password, email) " 
				+ " VALUES	(?, ?, ?)";
		String sql2 = "INSERT INTO permission (userid, warehouseid) VALUES (?, ?)";
		// Start a transaction 
		conn.setAutoCommit(false); // Prevents each query from immediately altering the database
		
		// Obtain auto incremented values with RETURN_GENERATED_KEYS
		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			  PreparedStatement ps2 = conn.prepareStatement(sql2)) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getEmail());
			
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				int key = keys.getInt(1); // Gets the auto generated key
				user.setId(key);
				for (int warehouseid : permissions) {
					ps2.setInt(1, key);
					ps2.setInt(2, warehouseid);
					ps2.addBatch();
				}
				ps2.executeBatch();
			}
			conn.commit();
			user.setPermissions(permissions);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			return null;
		}
	}

	@Override
	public void update(User user) throws SQLException {
		String sql = "UPDATE user SET username = ?, password = ?, email = ? WHERE userid = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getEmail());
			ps.setInt(4, user.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(User user) throws SQLException {
		delete(user.getId());
	}

	@Override
	public void delete(int userid) throws SQLException {
		String sql = "DELETE FROM user WHERE userid = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userid);
			ps.executeUpdate();
		}
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
		try (PreparedStatement stmt = conn.prepareStatement("SELECT warehouseid FROM permission WHERE userid = ?")) {

			while(rs.next()) {
				// Get the user permissions
				Set<Integer> permissions = processPermissions(stmt, rs.getInt("userid"));
				// Create the user
				User user = builder
						.id(rs.getInt("userid"))
						.username(rs.getString("username"))
						.email(rs.getString("email"))
						.password(rs.getString("password"))
						.permissions(permissions)
						.build();
				users.add(user);
			}
			
		}
		return users;
	}
	
	private Set<Integer> processPermissions(PreparedStatement stmt, int userid) throws SQLException {
		Set<Integer> permissions = new HashSet<>();
		stmt.setInt(1, userid);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			permissions.add(rs.getInt("warehouseid"));
		}
		return permissions;
	}

	@Override
	public Set<Integer> findPermissions(int userid) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPermission(int warehouseid, int userid) throws SQLException {
		String sql = "INSERT INTO permission (userid, warehouseid) VALUES (?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, userid);
				ps.setInt(2, warehouseid);
				ps.executeUpdate();
		}
	}

	@Override
	public void addPermissions(Set<Integer> warehouseids, int userid) throws SQLException {
		String sql = "INSERT INTO permission (userid, warehouseid) VALUES (?, ?)";
		// Start a transaction 
		conn.setAutoCommit(false); // Prevents each query from immediately altering the database
		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			for (int warehouseid : warehouseids) {
				ps.setInt(1, userid);
				ps.setInt(2, warehouseid);
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
		}
	}
	
	/** 
	 * For testing purposes only
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// try with resource closes the connection
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			UserDAO dao = new MySQLUserDAOImpl(conn);
//			List<User> users = dao.findByWarehouse(2);
//			for (User user : users) {
//				System.out.println(user);
//			}
			dao.delete(5);
//			int[] p = { 1, 2, 3 };
//			dao.addPermissions(p, 2);
			Set<Integer> pers = new HashSet<>();
			pers.add(1);
			pers.add(2);
			pers.add(3);
			User user = dao.save(new User("gaga", "oolala", "gaga@oolala.com"), pers);
			System.out.println(user);
		}
		
	}

}
