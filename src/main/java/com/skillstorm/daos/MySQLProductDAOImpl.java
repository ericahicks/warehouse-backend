package com.skillstorm.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.skillstorm.builders.ProductBuilder;
import com.skillstorm.conf.WarehouseDbCreds;
import com.skillstorm.models.Brand;
import com.skillstorm.models.Category;
import com.skillstorm.models.Product;

public class MySQLProductDAOImpl implements ProductDAO {
	
	Connection conn;
	ProductBuilder builder = new ProductBuilder();
	
	public MySQLProductDAOImpl(Connection connection) {
		this.conn  = connection;
	}

	@Override
	public List<Product> findAll() throws SQLException {

		String sql = "{CALL find_all_products}";
		
		try (CallableStatement stmt = conn.prepareCall(sql);) {
			ResultSet rs = stmt.executeQuery();
			LinkedList<Product> products = new LinkedList<>();
			
			while(rs.next()) {
				Category category = new Category(rs.getInt("categoryid"), rs.getString("category"));
				Brand brand = new Brand(rs.getInt("brandid"), rs.getString("brand"));
				Product product = builder
						.id(rs.getInt("productid"))
						.category(category)
						.name(rs.getString("name"))
						.description(rs.getString("description"))
						.size(rs.getString("size"))
						.brand(brand)
						.imageURL(rs.getString("imageURL"))
						.build();
				products.add(product);
			}
			return products;
		}
	}

	@Override
	public Product findById(int id) throws SQLException {
		String sql = "{CALL find_product_by_id(?)}";
		
		try (CallableStatement stmt = conn.prepareCall(sql);) {
			stmt.setInt(1, id);
			
			// Executing the query returns a ResultSet which contains all of the values returned
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// Looping over individual rows of the result set
				Category category = new Category(rs.getInt("categoryid"), rs.getString("category"));
				Brand brand = new Brand(rs.getInt("brandid"), rs.getString("brand"));
				Product product = builder
						.id(rs.getInt("productid"))
						.category(category)
						.name(rs.getString("name"))
						.description(rs.getString("description"))
						.size(rs.getString("size"))
						.brand(brand)
						.imageURL(rs.getString("imageURL"))
						.build();
				return product;
			}
		} 
		return null;
	}

	@Override
	public List<Product> findByName(String name) {
		String sql = "{CALL find_products_by_name(?)}";
		
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setString(1, name);
			
			// Executing the query returns a ResultSet which contains all of the values returned
			ResultSet rs = stmt.executeQuery();
			LinkedList<Product> products = new LinkedList<>();
			
			while(rs.next()) {
				// Looping over individual rows of the result set
				Category category = new Category(rs.getInt("categoryid"), rs.getString("category"));
				Brand brand = new Brand(rs.getInt("brandid"), rs.getString("brand"));
				Product product = builder
						.id(rs.getInt("productid"))
						.category(category)
						.name(rs.getString("name"))
						.description(rs.getString("description"))
						.size(rs.getString("size"))
						.brand(brand)
						.imageURL(rs.getString("imageURL"))
						.build();
				products.add(product);
			}
			
			return products;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Product> findByCategory(String category) {
		String sql = "{CALL find_products_by_category(?)}";
		
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setString(1, category);
			
			// Executing the query returns a ResultSet which contains all of the values returned
			ResultSet rs = stmt.executeQuery();
			LinkedList<Product> products = new LinkedList<>();
			
			while(rs.next()) {
				// Looping over individual rows of the result set
				Category ctgry = new Category(rs.getInt("categoryid"), rs.getString("category"));
				Brand brand = new Brand(rs.getInt("brandid"), rs.getString("brand"));
				Product product = builder
						.id(rs.getInt("productid"))
						.category(ctgry)
						.name(rs.getString("name"))
						.description(rs.getString("description"))
						.size(rs.getString("size"))
						.brand(brand)
						.imageURL(rs.getString("imageURL"))
						.build();
				products.add(product);
			}
			return products;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Product> findByBrand(String brand) throws SQLException {
		String sql = "{CALL find_products_by_brand(?)}";
		
		try (CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.setString(1, brand);
			
			// Executing the query returns a ResultSet which contains all of the values returned
			ResultSet rs = stmt.executeQuery();
			LinkedList<Product> products = new LinkedList<>();
			
			while(rs.next()) {
				// Looping over individual rows of the result set
				Category ctgry = new Category(rs.getInt("categoryid"), rs.getString("category"));
				Brand brnd = new Brand(rs.getInt("brandid"), rs.getString("brand"));
				Product product = builder
						.id(rs.getInt("productid"))
						.category(ctgry)
						.name(rs.getString("name"))
						.description(rs.getString("description"))
						.size(rs.getString("size"))
						.brand(brnd)
						.imageURL(rs.getString("imageURL"))
						.build();
				products.add(product);
			}
			
			return products;
		} 
	}

	@Override
	public Product save(Product product) throws SQLException {
		String sql = "INSERT INTO product (categoryid, name, description, size, brandid, imageurl) VALUES "
				+ "	(?, ?, ?, ?, ?, ?)";
		
		// Start a transaction
		conn.setAutoCommit(false); // Prevents each query from immediately altering the database

		// Obtain auto incremented values with RETURN_GENERATED_KEYS
		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, product.getCategory().getId());
			ps.setString(2, product.getName());
			ps.setString(3, product.getDescription());
			ps.setString(4, product.getSize());
			ps.setInt(5, product.getBrand().getId());
			ps.setString(6, product.getImageURL());
			
			int rowsAffected = ps.executeUpdate(); // If 0 is returned, my data didn't save
			if (rowsAffected != 0) {
				ResultSet keys = ps.getGeneratedKeys();
				if (keys.next()) {
					int key = keys.getInt(1); // Gets the auto generated key
					product.setId(key);
				}
				conn.commit(); // Executes ALL queries in a given transaction
				return product;
			} else {
				conn.rollback(); // Undoes any of the queries. Database pretends those never happened
				return null;
			}
			
		}
	}

	@Override
	public int update(Product product) throws SQLException {
		int rowsAffected = 0;
		String sql = "UPDATE product "
				+ "SET "
				+ "    categoryid = ?, "
				+ "    name = ?, "
				+ "    description = ?, "
				+ "    size = ?, "
				+ "    brandid = ?, "
				+ "    imageURL = ? "
				+ "WHERE "
				+ "    productid = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, product.getCategory().getId());
			ps.setString(2, product.getName());
			ps.setString(3, product.getDescription());
			ps.setString(4, product.getSize());
			ps.setInt(5, product.getBrand().getId());
			ps.setString(6, product.getImageURL());
			ps.setInt(7, product.getId());
			
			rowsAffected = ps.executeUpdate(); // If 0 is returned, the data didn't update
			
		}
		return rowsAffected;
		
	}

	@Override
	public void delete(Product product) throws SQLException {
		delete(product.getId());
	}

	@Override
	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM product WHERE productid = ?";

		// Start a transaction
		conn.setAutoCommit(false); // Prevents each query from immediately altering the database
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			
			int rowsAffected = ps.executeUpdate(); // If 0 is returned, nothing returned
			if (rowsAffected != 0) {
				conn.commit(); // Executes ALL queries in a given transaction
			} else {
				conn.rollback(); // Undoes any of the queries. Database pretends those never happened
			}
			
		}
	}
	
	@Override
	public void deleteMany(Product[] products) throws SQLException {
		int[] ids = new int[products.length];
		deleteMany(ids);
		
	}

	@Override
	public void deleteMany(int[] ids) throws SQLException {
		String sql = "DELETE FROM product WHERE productid = ?";
		
		// Start a transaction
		conn.setAutoCommit(false); // Prevents each query from immediately altering the database
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			int i =0;
			for (; i < ids.length; i++) {
				ps.setInt(1, ids[i]);
				int rowsAffected = ps.executeUpdate(); // If 0 is returned, nothing returned
				if (rowsAffected == 0) {
					conn.rollback();
					break;
				}
			}
			if (i == ids.length) {
				conn.commit();
			}
	    } 
	}
	
	public static void main(String[] args) {
		// try with resource closes the connection
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			ProductDAO dao = new MySQLProductDAOImpl(conn);
			List<Product> prods = dao.findAll();
			for (Product prod: prods)
				System.out.println(prod);
//			Product prod;
//			prod = dao.findById(7);
//			System.out.println(prod);
	//		List<Product> prods = dao.findByName("Aladdin's lamp");
	//		for (Product prod: prods)
	//			System.out.println(prod);
	//		List<Product> prods = dao.findByCategory("lamp");
	//		for (Product prod: prods)
	//			System.out.println(prod);
//			ProductBuilder builder = new ProductBuilder();
//			Product newprod = builder
//								.id(7)
//								.category(new Category(1, "box"))
//								.name("Chewy Box")
//								.description("Empty shipping box for your pet.")
//								.size("Fits large dog.")
//								.brand(new Brand(4, "Chewy"))
//								.imageURL("/resources/assets/chewybox.jpg")
//								.build();
	//		Product prod = dao.save(newprod);
//			dao.update(newprod);
//			System.out.println(newprod);
//			int[] ids = {12, 11};
//			dao.deleteMany(ids);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
