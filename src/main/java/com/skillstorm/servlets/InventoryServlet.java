package com.skillstorm.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.conf.WarehouseDbCreds;
import com.skillstorm.daos.MySQLInventoryDAOImpl;
import com.skillstorm.models.InventoryItem;
import com.skillstorm.services.InventoryURLParserService;

@WebServlet(urlPatterns = "/inventory/*")
public class InventoryServlet extends HttpServlet {
	

	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// Class and Instance Variables /////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	private static final long serialVersionUID = 7823247666694084912L;
	private Connection conn;
	private MySQLInventoryDAOImpl dao;
	private InventoryURLParserService urlService = new InventoryURLParserService();
	ObjectMapper mapper = new ObjectMapper();

	///////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////// Life Cycle Methods  ///////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void init() throws ServletException {
		// This allows us to write code that is run right as the servlet is created
		// You can establish any connections
		try {
			conn = WarehouseDbCreds.getInstance().getConnection();
			dao = new MySQLInventoryDAOImpl(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Inventory Servlet Created!");
		super.init();
	}
	

	@Override
	public void destroy() {
		// If any connections were established in init
		// Terminate those connections here
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Inventory Servlet Destroyed!");
		super.destroy();
	}


	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// This method activates on ALL HTTP requests to this servlet
		System.out.println("Servicing request!");
		super.service(req, resp); // Keep this line
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// HTTP Methods /////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	// Returns Inventory Item(s)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			urlService.setURL(req.getRequestURI());
			urlService.extractURL();
			try {
				switch (urlService.getType()) {
				case ALL:
					new InventoryAllHandler().getInventory(resp);
					break;
				case WAREHOUSE:
					new InventoryByWarehouseHandler().getInventory((int) urlService.getSubDomain2(), resp);
					break;
				case PRODUCT:
					new InventoryByProductHandler().getInventory((int) urlService.getSubDomain2(), resp);
					break;
				case BOTH:
					new InventoryByWarehouseProductHandler()
					        .getInventory((int) urlService.getSubDomain2(), 
							             (int) urlService.getSubDomain1(),
							             resp);
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
	

	// Saves the inventory item
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			urlService.setURL(req.getRequestURI());
			urlService.extractURL();
			try {
				switch (urlService.getType()) {
				case ALL:
					new InventoryAllHandler().putInventory(req, resp);
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
	

	// Updates the inventory item(s)
		@Override
		protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				urlService.setURL(req.getRequestURI());
				urlService.extractURL();
				try {
					switch (urlService.getType()) {
					case ALL:
						new InventoryAllHandler().putInventory(req, resp);
						break;
					default:
						resp.setStatus(400);
						resp.getWriter().append("Unrecognized api url put requested.");
						break;
					}
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					resp.setStatus(500);
					resp.getWriter().append("Server unable to update inventory item.");
				}
		}
		

		// Deletes an inventory item
		@Override
		protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				urlService.setURL(req.getRequestURI());
				urlService.extractURL();
				try {
					switch (urlService.getType()) {
					case BOTH:
						new InventoryByWarehouseProductHandler().removeProduct((int) urlService.getSubDomain1(), 
								                               (int) urlService.getSubDomain2(), resp);
						break;
					default:
						resp.setStatus(400);
						resp.getWriter().append("Unrecognized api url for delete requested.");
						break;
					}
				} catch (IOException | SQLException e) {
					e.printStackTrace();
					resp.setStatus(500);
					resp.getWriter().append("Server unable to delete inventory item.");
				}
		}
	


	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////    HANDLER METHODS    /////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////// 
	///////////////////////////////////////////////////////////////////////////////////////
	 
	/*******************************************************************************************/

	class InventoryAllHandler {
		
		// GET /inventory/
		public void getInventory(HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			System.out.println("Inventory handler method");
			// Means that there wasn't an id in the URL. Fetch all artists instead
			List<InventoryItem> inventory = dao.findAll();
			System.out.println(inventory);
			if (inventory == null || inventory.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No inventory found.");
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(inventory));
			}
		}
		

		// PUT /inventory/
		public void putInventory(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {

			InputStream reqBody = req.getInputStream();
			InventoryItem newInventoryItem = mapper.readValue(reqBody, InventoryItem.class);
//					validatorService.validate(newArtist); // Could be a service
			int rowsAffected = dao.update(newInventoryItem); // 1 is a put, 2 is a update
			switch (rowsAffected) {
			case 1:
				resp.getWriter().append("Inventory Item created successfully");
				resp.setStatus(201);
				break;
			case 2:
				resp.getWriter().append("Inventory Item updated successfully");
				resp.setStatus(200);
				break;
			case 0:
				resp.getWriter().append("Unable to create Inventory Item.");
				resp.setStatus(400);
				break;
			default:
				resp.getWriter().append("Unrecognized response from server during creation of inventory item.");
				resp.setStatus(500);
				break;
			}
		}
	}
	
	/*******************************************************************************************/

	class InventoryByWarehouseHandler {
		
		// GET /inventory/warehouse/{warehouseid}
		public void getInventory(int warehouseid, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			
			try {
				List<InventoryItem> inventory = dao.findByWarehouseId(warehouseid);
				System.out.println(inventory);
				if (inventory == null || inventory.isEmpty())
					throw new IllegalArgumentException();
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(inventory));
			} catch (IndexOutOfBoundsException  | IllegalArgumentException e) {
				System.out.println("No inventory item for warehouse " + warehouseid);
				resp.setStatus(404);
				resp.getWriter().append("No inventory item with the provided warehouseId " + warehouseid);
			}
		}
	}

	/*******************************************************************************************/

	class InventoryByProductHandler {
		
		// GET /inventory/product/{productid}
		public void getInventory(int productid, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			try {
				List<InventoryItem> inventory = dao.findByProductId(productid);
				System.out.println(inventory);
				if (inventory == null || inventory.isEmpty())
					throw new IllegalArgumentException();
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(inventory));
			} catch (IndexOutOfBoundsException | IllegalArgumentException ex) {
				System.out.println("No inventory items for product " + productid);
				resp.setStatus(404);
				resp.getWriter().append("No inventory items for the provided product " + productid);
			}
		}
	}

	/*******************************************************************************************/

	class InventoryByWarehouseProductHandler {
		
		// GET /inventory/{warehouseid}/{productid}
		public void getInventory(int warehouseid, int productid, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			try {
				InventoryItem inventory = dao.findByWarehouseIdProductId(warehouseid, productid);
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(inventory));
			} catch (IndexOutOfBoundsException e) {
				System.out.println("No inventory item for warehouse " + warehouseid + " and product " + productid);
				resp.setStatus(404);
				resp.getWriter().append("No inventory item with the provided warehouseId " + warehouseid + " "
						+ "productid " + productid + " found");
			}
		}
		

		// DELETE /product/{id}
		public void removeProduct(int warehouseid, int productid, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {

			int rowsAffected = dao.delete(warehouseid, productid);
			if (rowsAffected > 0) {
				resp.setStatus(204);
				resp.getWriter().append("Removed inventory item successfully.");
			} else {
				resp.setStatus(204);
				resp.getWriter().append("Nothing to delete.");
			}
			
		}
		
	}
	

}
