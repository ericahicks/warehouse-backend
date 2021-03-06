package com.skillstorm.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.conf.WarehouseDbCreds;
import com.skillstorm.daos.MySQLProductDAOImpl;
import com.skillstorm.daos.ProductDAO;
import com.skillstorm.models.Product;
import com.skillstorm.services.ProductURLParserService;

@WebServlet(urlPatterns = "/product/*")
public class ProductServlet extends HttpServlet {

	///////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////// Life Cycle Methods ////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////// 
	@Override
	public void init() throws ServletException {
		// This allows us to write code that is run right as the servlet is created
		// You can establish any connections
		try {
			conn = WarehouseDbCreds.getInstance().getConnection();
			dao = new MySQLProductDAOImpl(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Product Servlet Created!");
		super.init();
	}

	@Override
	public void destroy() {
		// If any connections were established in init
		// Terminate those connections here
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Product Servlet Destroyed!");
		super.destroy();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// This method activates on ALL HTTP requests to this servlet
		System.out.println("Servicing request!");
		super.service(req, resp); // Keep this line
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// Class and Instance Variables /////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	private static final long serialVersionUID = -1005346930881540665L;
	Connection conn;
	ProductDAO dao;
	ObjectMapper mapper = new ObjectMapper();
	ProductURLParserService urlService = new ProductURLParserService();

	
	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// HTTP Methods /////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	// Returns product(s)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			urlService.setUrl(req.getRequestURI());
			urlService.extractURL();
			try {
				switch (urlService.getType()) {
				case ALL:
					new ProductsHandler().getProducts(resp);
					break;
				case ID:
					new ProductByIdHandler().getProduct((int) urlService.getValue(), resp);
					break;
				case NAME:
					new ProductByNameHandler().getProduct((String) urlService.getValue(), resp);
					break;
				case CATEGORY:
					new ProductsByCategoryHandler().getProducts((String) urlService.getValue(), resp);
					break;
				case BRAND:
					new ProductsByBrandHandler().getProducts((String) urlService.getValue(), resp);
					break;
				default:
					resp.setStatus(400);
					resp.getWriter().append("Unrecognized api url requested.");
					break;
				}
			} catch (IOException | SQLException e) {
				e.printStackTrace();
				resp.setStatus(500);
				resp.getWriter().append("Server unable to fetch product(s).");
			}
	}
	
	// Saves the product(s) and returns the saved product
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			urlService.setUrl(req.getRequestURI());
			urlService.extractURL();
			try {
				switch (urlService.getType()) {
				case ALL:
					new ProductsHandler().putProduct(req, resp);
					break;
				default:
					resp.setStatus(400);
					resp.getWriter().append("Unrecognized api url post requested.");
					break;
				}
			} catch (IOException | SQLException e) {
				e.printStackTrace();
				resp.setStatus(500);
				resp.getWriter().append("Server unable to save product.");
			}
	}
	
	// Updates the product(s) and returns the saved product
		@Override
		protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				urlService.setUrl(req.getRequestURI());
				urlService.extractURL();
				try {
					switch (urlService.getType()) {
					case ID:
						new ProductByIdHandler().putProduct((int) urlService.getValue(), req, resp);
						break;
					case ALL:
						new ProductsHandler().putProduct(req, resp);
						break;
					default:
						resp.setStatus(400);
						resp.getWriter().append("Unrecognized api url put requested.");
						break;
					}
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					resp.setStatus(500);
					resp.getWriter().append("Server unable to update product.");
				}
		}
		
		// Deletes product(s)
		@Override
		protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				urlService.setUrl(req.getRequestURI());
				urlService.extractURL();
				try {
					switch (urlService.getType()) {
					case ID:
						new ProductByIdHandler().removeProduct((int) urlService.getValue(), resp);
						break;
					default:
						resp.setStatus(400);
						resp.getWriter().append("Unrecognized api url for delete requested.");
						break;
					}
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					resp.setStatus(500);
					resp.getWriter().append("Server unable to delete product(s).");
				}
		}

	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////    HANDLER METHODS    /////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////// 
	 
	/*******************************************************************************************/

	class ProductByIdHandler {
		
		// GET /product/{ID}
		public void getProduct(int id, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			
			System.out.println("Product by Id handler method");Product product = dao.findById(id);
			if (product != null) {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(product));
			} else {
				resp.setStatus(404);
				resp.getWriter().append("No product with the provided Id \"" + id + "\" found");
			}
		}
		
		// PUT /product/{id}
		public void putProduct(int id, HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {

			InputStream reqBody = req.getInputStream();
			Product newProduct = mapper.readValue(reqBody, Product.class);
//					validatorService.validate(newArtist); // Could be a service
			newProduct.setId(id);
			dao.update(newProduct); 
			resp.setStatus(200);
			resp.getWriter().append("Product update successful.");
		}
		
		// DELETE /product/{id}
		public void removeProduct(int id, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {

			System.out.println("Product by Id handler method");
			dao.delete(id);
			resp.setStatus(204);
			resp.getWriter().append("Removed product successfully.");
		
		}
		
	}
    /*******************************************************************************************/

	class ProductsHandler {

		// GET /product/
		public void getProducts(HttpServletResponse resp) throws JsonProcessingException, IOException, SQLException {
			System.out.println("Products handler method");
			// Means that there wasn't an id in the URL. Fetch all artists instead
			List<Product> products = dao.findAll();
			System.out.println(products);
			if (products == null || products.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No products found.");
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(products));
			}
		}
		
		// PUT /product/
		public void putProduct(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {

			InputStream reqBody = req.getInputStream();
			Product newProduct = mapper.readValue(reqBody, Product.class);
//					validatorService.validate(newArtist); // Could be a service
			dao.update(newProduct); 
			resp.setStatus(200);
			resp.getWriter().append("Product update successful.");
		}
		
	}
	
    /*******************************************************************************************/
	
	class ProductByNameHandler {

		// GET /product/{name}
		public void getProduct(String name, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			System.out.println("Product by Name handler method");
			List<Product> products = dao.findByName(name);
			System.out.println("Products = ");
			for (Product product : products) 
				System.out.println("    " + product + ", ");
			if (products != null && !products.isEmpty()) {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(products));
			} else {
				resp.setStatus(404);
				resp.getWriter().append("No product(s) with the provided name \"" + name + "\" found");
			}
		}
	}
    
    /*******************************************************************************************/

	class ProductsByCategoryHandler {

		// GET /product/category/{name}
		public void getProducts(String categoryName, HttpServletResponse resp) throws JsonProcessingException, IOException, SQLException {
			System.out.println("Products by Category handler method");
			List<Product> products = dao.findByCategory(categoryName);
			System.out.println("Products = ");
			for (Product product : products) 
				System.out.println("    " + product + ", ");
			if (products == null || products.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No products found for the given category \"" + categoryName + "\".");
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(products));
			}
		}

	}
	
    /*******************************************************************************************/

	class ProductsByBrandHandler {

		// GET /product/brand/{name}
		public void getProducts(String brandName, HttpServletResponse resp) throws JsonProcessingException, IOException, SQLException {
			System.out.println("Products by Category handler method");
			List<Product> products = dao.findByBrand(brandName);
			System.out.println("Products = ");
			for (Product product : products) 
				System.out.println("    " + product + ", ");
			if (products == null || products.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No products found of the given brand \"" + brandName + "\".");
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(products));
			}
		}
	}

}
