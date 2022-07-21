package com.skillstorm.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.skillstorm.builders.InventoryBuilder;
import com.skillstorm.builders.ProductBuilder;
import com.skillstorm.builders.WarehouseBuilder;
import com.skillstorm.conf.WarehouseDbCreds;
import com.skillstorm.models.Brand;
import com.skillstorm.models.Category;
import com.skillstorm.models.InventoryItem;
import com.skillstorm.models.Product;
import com.skillstorm.models.State;
import com.skillstorm.models.Warehouse;

public class MySQLInventoryDAOImpl implements InventoryDAO {
	
	Connection conn;
	
	public MySQLInventoryDAOImpl(Connection connection) {
		this.conn  = connection;
	}
	
	private List<InventoryItem> processResults(ResultSet rs) throws IllegalArgumentException, SQLException {

		List<InventoryItem> inventory = new ArrayList<InventoryItem>();
		while (rs.next()) {
			State state;
			Brand brand;
			Category category;
			try {
				state = new State(rs.getString("statename"), rs.getString("statecode"));
			} catch (IllegalArgumentException e) {
				state = null;
			}
			try {
				brand = new Brand(rs.getInt("brandid"), rs.getString("brandname"));
			} catch (IllegalArgumentException e) {
				brand = null;
			}
			try {
				category = new Category(rs.getInt("categoryid"), rs.getString("categoryname"));
			} catch (IllegalArgumentException e) {
				category = null;
			}
			Product prod = new ProductBuilder()
					.id(rs.getInt("productid"))
					.name(rs.getString("productname"))
					.description(rs.getString("description"))
					.size(rs.getString("size"))
					.imageURL(rs.getString("imageurl"))
					.brand(brand)
					.category(category)
					.build();
			Warehouse warehouse = new WarehouseBuilder()
					.id(rs.getInt("warehouseid"))
					.name(rs.getString("warehousename"))
					.capacity(rs.getInt("capacity"))
					.street(rs.getString("street"))
					.city(rs.getString("city"))
					.state(state)
					.zip(rs.getString("zip"))
					.build();
			InventoryItem item = new InventoryBuilder()
					.product(prod)
					.warehouse(warehouse)
					.quantity(rs.getInt("quantity"))
					.minimum(rs.getInt("minimum"))
					.build();
			inventory.add(item);
		}
		return inventory;
	}

	@Override
	public List<InventoryItem> findAll() throws SQLException {
		String sql = "{CALL find_all_inventory()}";
		
		try (CallableStatement cs = conn.prepareCall(sql)) {
			ResultSet rs = cs.executeQuery();
			return processResults(rs);
		}
	}

	@Override
	public InventoryItem findByWarehouseIdProductId(int warehouseId, int productId) throws SQLException {
		String sql = "{CALL find_all_inventory_by_warehouse_and_product(?, ?)}";
		
		try (CallableStatement cs = conn.prepareCall(sql)) {
			cs.setInt(1, warehouseId);
			cs.setInt(2, productId);
			ResultSet rs = cs.executeQuery();
			return processResults(rs).get(0); // ArrayOutOfBoundsException if no results were found
		}
	}
	

	@Override
	public InventoryItem findByWarehouseIdProductName(int warehouseId, String productName) throws SQLException {
		String sql = "{CALL find_all_inventory_by_warehouse_and_product_name(?, ?)}";
		
		try (CallableStatement cs = conn.prepareCall(sql)) {
			cs.setInt(1, warehouseId);
			cs.setString(2, productName);
			ResultSet rs = cs.executeQuery();
			return processResults(rs).get(0); // ArrayOutOfBoundsException if no results were found
		}
	}

	@Override
	public List<InventoryItem> findByWarehouseId(int id) throws SQLException {
		String sql = "{CALL find_all_inventory_by_warehouse(?)}";
		
		try (CallableStatement cs = conn.prepareCall(sql)) {
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			return processResults(rs);
		}
	}

	@Override
	public List<InventoryItem> findByProductId(int id) throws SQLException {
		String sql = "{CALL find_all_inventory_by_product(?)}";
		
		try (CallableStatement cs = conn.prepareCall(sql)) {
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			return processResults(rs);
		}
	}

	@Override
	public List<InventoryItem> findByProductName(String name) throws SQLException {
		String sql = "{CALL find_all_inventory_by_product_name(?)}";
		
		try (CallableStatement cs = conn.prepareCall(sql)) {
			cs.setString(1, name);
			ResultSet rs = cs.executeQuery();
			return processResults(rs);
		}
	}
	
	public int findInventoryTotal() throws SQLException {
		String sql = "SELECT SUM(quantity) AS total FROM inventory";
		int total = 0;
		try (Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				total = rs.getInt("total");
			}
		}
		return total;
	}
	
	public int findInventoryTotalByWarehouse(int warehouseId) throws SQLException{
		String sql = "SELECT SUM(quantity) as total FROM inventory WHERE warehouseid = ?";
		int total = 0;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, warehouseId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt("total");
			}
		}
		return total;
	}
	
	public int findInventoryTotalByProduct(int productId) throws SQLException {
		String sql = "SELECT SUM(quantity) as total FROM inventory WHERE productid = ?";
		int total = 0;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, productId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt("total");
			}
		}
		return total;
	}

	public int findInventoryTotalByProduct(String productName) throws SQLException {
		String sql = "SELECT SUM(quantity) as total FROM inventory INNER JOIN product USING (productid) WHERE product.name = ?";
		int total = 0;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, productName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt("total");
			}
		}
		return total;
	}
	
	public int findInventoryTotalByWarehouseProduct(int warehouseId, int productId) throws SQLException {
		String sql = "SELECT SUM(quantity) as total FROM inventory WHERE warehouseid = ? AND productid = ?";
		int total = 0;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, warehouseId);
			stmt.setInt(2, productId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt("total");
			}
		}
		return total;
	}
	
	public int findInventoryTotalByWarehouseProduct(int warehouseId, String productName) throws SQLException {
		String sql = "SELECT SUM(quantity) as total FROM inventory INNER JOIN product USING (productid) WHERE warehouseid = ? AND  product.name = ?";
		int total = 0;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, warehouseId);
			stmt.setString(2, productName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt("total");
			}
		}
		return total;
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
	
	// For testing purposes only
	public static void main(String[] args) throws SQLException {
		// try with resource closes the connection
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			InventoryDAO dao = new MySQLInventoryDAOImpl(conn);
			InventoryItem inventory = dao.findByWarehouseIdProductName(2, "Pandora's box");
			System.out.println(inventory);
//			for (InventoryItem item : inventory ) {
//				System.out.println(item);
//			}
		}
	}

}
