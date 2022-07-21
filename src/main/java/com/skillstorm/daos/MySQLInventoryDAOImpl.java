package com.skillstorm.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
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

	/**
	 * If an item for the same warehouse and product already exists, it does an update.
	 * @return rowsAffected This will be 1 if an insert is done, and 2 if an update is done (1 for set quantity and 1 set minimum)
	 */
	@Override
	public int save(InventoryItem item) throws SQLException {
		return save(item.getWarehouse().getId(), item.getProduct().getId(), item.getQuantity(), item.getMinimum());
	}
	
	@Override
	public int save(int warehouseId, int productId, int quantity, int minimum) throws SQLException {
		int rowsAffected = 0;
		Boolean isTooManyProducts = exceedsCapacity(warehouseId, productId, quantity);
		
		if (isTooManyProducts == null || isTooManyProducts) 
			return rowsAffected;
		// else insert the new inventory item
		String insert = "{CALL insert_or_update_inventory_item(?, ?, ?, ?)}";
		
		try (CallableStatement insertcall = conn.prepareCall(insert)) {
			insertcall.setInt(1, warehouseId);
			insertcall.setInt(2, productId);
			insertcall.setInt(3, quantity);
			insertcall.setInt(4, minimum);
			rowsAffected = insertcall.executeUpdate();
		}
		
		return rowsAffected;
		
	}
	
	private Boolean exceedsCapacity(int warehouseId, int productId, int quantity) throws SQLException {
		String query = "{CALL check_capacity_needed(?, ?, ?)}";
		
		try (CallableStatement querycall = conn.prepareCall(query)) {
			querycall.setInt(1, warehouseId);
			querycall.setInt(2, productId);
			querycall.setInt(3, quantity);
			ResultSet rs = querycall.executeQuery();
			if (rs.next()) {
				int capacityNeeded = rs.getInt("capacity-needed");
				int capacity = rs.getInt("capacity");
				return (capacityNeeded > capacity);
			}
			
		}
		return null;
	}
	
	@Override
	public int addSomeProduct(Warehouse warehouse, Product product, int unitsAdded) throws SQLException {
		return addSomeProduct(warehouse.getId(), product.getId(), unitsAdded);
	}

	@Override
	public int addSomeProduct(int warehouseId, int productId, int unitsAdded) throws SQLException {
		// Check if inventory item exists
		InventoryItem item;
		int newTotalUnits;
		try {
			item = findByWarehouseIdProductId(warehouseId, productId);
			newTotalUnits = item.getQuantity() + unitsAdded;
		} catch (Exception e) {
			System.out.println("Trying to add additional product, but no record of that product in the inventory exists. So creating entry.");
			item = null;
			newTotalUnits = unitsAdded;
		}
		// save or update
		save(warehouseId, productId, newTotalUnits, 0);
		return newTotalUnits;
	}
	
	@Override
	public int removeSomeProduct(Warehouse warehouse, Product product, int unitsRemoved) throws SQLException {
		return removeSomeProduct(warehouse.getId(), product.getId(), unitsRemoved);
	}
	/**
	 * Decrements the quantity of some warehouse product inventory item by the given amount unitsRemoved
	 * will not update if try to remove more than exits
	 * @return newQuantity the new number of units of this warehouse product (will not update if tried to remove more than exist)
	 */
	@Override
	public int removeSomeProduct(int warehouseId, int productId, int unitsRemoved) throws SQLException {
		// Check if inventory item exists
		InventoryItem item;
		int unitsInStock;
		int minimum;
		try {
			item = findByWarehouseIdProductId(warehouseId, productId);
			unitsInStock = item.getQuantity();
			minimum = item.getMinimum();
		} catch (Exception e) {
			System.out.println("Trying to add additional product, but no record of that product in the inventory exists. So creating entry.");
			item = null;
			unitsInStock = 0;
			minimum = 0;
		}
		int newQuantity = unitsInStock;
		// if it exists, are you trying to remove more units than exist?
		if (unitsRemoved <= unitsInStock) {
			newQuantity = unitsInStock - unitsRemoved;
			update(warehouseId, productId, newQuantity, minimum);
		}
		return newQuantity;
	}
	
	/** 
	 * Returns the number of rows affected (0 if the product exists or 2 if the product inventory was updated (1 quantity update + 1 
	 */
	@Override
	public int removeAllOfOneProduct(Warehouse warehouse, Product product) throws SQLException {
		return removeAllOfOneProduct(warehouse.getId(), product.getId());
	}
	
	@Override
	public int removeAllOfOneProduct(int warehouseId, int productId) throws SQLException {
		InventoryItem item;
		int minimum;
		try {
			item = findByWarehouseIdProductId(warehouseId, productId);
			minimum = item.getMinimum();
		} catch (Exception e) {
			item = null;
			minimum = 0;
		}
		return update(warehouseId, productId, 0, minimum);
	}

	/**
	 * If an item for the this warehouse and product doesn't exists, it will be created.
	 * @return rowsAffected This will be 1 if an insert is done, and 2 if an update is done (1 set quantity + 1 set minimum)
	 */
	@Override
	public int update(InventoryItem item) throws SQLException {
		// save does a save or update
		return save(item);
	}
	
	/**
	 * If an item for the this warehouse and product doesn't exists, it will be created.
	 * @return rowsAffected This will be 1 if an insert is done, and 2 if an update is done (1 set quantity + 1 set minimum)
	 */
	@Override
	public int update(int warehouseId, int productId, int quantity, int minimum) throws SQLException {
		// save does a save or update
		return save(warehouseId, productId, quantity, minimum);
	}

	@Override
	public void delete(InventoryItem item) throws SQLException {
		delete(item.getWarehouse().getId(), item.getProduct().getId());
	}

	@Override
	public void delete(int warehouseId, int productId) throws SQLException {
		String sql = "DELETE FROM inventory WHERE warehouseid = ? AND  productid = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, warehouseId);
			ps.setInt(2, productId);
			ps.executeUpdate();
		}
		
	}

	@Override
	public void deleteMany(int warehouseId, int[] productIds) throws SQLException {
		String sql = "DELETE FROM inventory WHERE warehouseid = ? AND  productid = ?";

		// Set auto-commit to false
		conn.setAutoCommit(false);
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int productId : productIds) {
				ps.setInt(1, warehouseId);
				ps.setInt(2, productId);
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
		}
		
	}

	@Override
	public void deleteMany(int[] warehouseIds, int productId) throws SQLException {
		String sql = "DELETE FROM inventory WHERE warehouseid = ? AND  productid = ?";

		// Set auto-commit to false
		conn.setAutoCommit(false);
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int warehouseId : warehouseIds) {
				ps.setInt(1, warehouseId);
				ps.setInt(2, productId);
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
		}
	}
	
	@Override
	public void deleteMany(int[] warehouseIds, int[] productIds) throws SQLException {
		String sql = "DELETE FROM inventory WHERE warehouseid = ? AND  productid = ?";

		// Set auto-commit to false
		conn.setAutoCommit(false);
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int productId : productIds) {
				for (int warehouseId : warehouseIds) {
					ps.setInt(1, warehouseId);
					ps.setInt(2, productId);
					ps.addBatch();
				}
			}
			ps.executeBatch();
			conn.commit();
		}
	}

	@Override
	public void deleteMany(InventoryItem[] items) throws SQLException {
		HashSet<Integer> warehouseSet = new HashSet<>();
		HashSet<Integer> productSet = new HashSet<>();
		for (InventoryItem item : items) {
			warehouseSet.add(item.getWarehouse().getId());
			productSet.add(item.getProduct().getId());
		}
		int[] warehouseIds = extractIds(warehouseSet);
		int[] productIds = extractIds(productSet);
		deleteMany(warehouseIds, productIds);
	}
	
	/** Takes in a hashset and an empty initialized array of equal size
	 * @param set
	 * @param ids
	 * @return ids the filled array containing the hash set's contents
	 */
	private int[] extractIds(HashSet<Integer> set) {
		int[] ids = new int[set.size()];
		int count = 0;
		for (int id : set) {
			ids[count++] = id;
		}
		return ids;
	}
	
	// For testing purposes only
	public static void main(String[] args) throws SQLException {
		// try with resource closes the connection
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			InventoryDAO dao = new MySQLInventoryDAOImpl(conn);
//			InventoryItem inventory = dao.findByWarehouseIdProductName(2, "Pandora's box");
//			System.out.println(inventory);
//			List<InventoryItem> items = dao.findByProductName("Pandora's box");
//			for (InventoryItem item : items ) {
//				System.out.println(item);
//			}
//			int total = dao.save(1, 14, 1, 1);
//			int[] ids = { 13, 14 };
//			dao.deleteMany(3, ids);
			int total = dao.removeAllOfOneProduct(3, 14);
			System.out.println(total);
		}
	}
	

}
