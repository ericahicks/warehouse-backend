package com.skillstorm.servlets;

import java.io.IOException;
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
import com.skillstorm.daos.MySQLWarehouseDAOImpl;
import com.skillstorm.models.InventoryItem;
import com.skillstorm.models.Warehouse;
import com.skillstorm.services.WarehouseURLParserService;

@WebServlet(urlPatterns = "/warehouse/*")
public class WarehouseServlet  extends HttpServlet  {

	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// Class and Instance Variables /////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	private static final long serialVersionUID = -1255978588645665829L;
	private Connection conn;
	private MySQLWarehouseDAOImpl dao;
	private WarehouseURLParserService urlService = new WarehouseURLParserService();
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
			dao = new MySQLWarehouseDAOImpl(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Warehouse Servlet Created!");
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
		System.out.println("Warehouse Servlet Destroyed!");
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
					new WarehouseAllHandler().getWarehouses(resp);
					break;
				case ID:
					new WarehouseByIdHandler().getWarehouse((int) urlService.getSubDomain1(), resp);
					break;
				case NAME:
					new WarehousesByNameHandler().getWarehouses((String) urlService.getSubDomain2(), resp);
					break;
				case CITY:
					new WarehousesByCityHandler().getWarehouses((String) urlService.getSubDomain2(), resp);
					break;
				case STATE:
					new WarehousesByStateHandler().getWarehouses((String) urlService.getSubDomain2(), resp);
					break;
				case ZIP:
					new WarehousesByZipHandler().getWarehouses((String) urlService.getSubDomain2(), resp);
					break;
				case AVAILABLE:
					new WarehousesByAvailableCapacityHandler().getWarehouses((int) urlService.getSubDomain2(), resp);
					break;
				default:
					resp.setStatus(400);
					resp.getWriter().append("Unrecognized api url requested.");
					break;
				}
			} catch (IOException | SQLException e) {
				System.out.println(e.getMessage());
				resp.setStatus(500);
				resp.getWriter().append("Server unable to fetch warehouse(s).");
			}
	}
	

	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////    HANDLER METHODS    /////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////// 
	///////////////////////////////////////////////////////////////////////////////////////
	 
	/*******************************************************************************************/

	class WarehouseAllHandler {
		
		// GET /warehouse/
		public void getWarehouses(HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			List<Warehouse> warehouse = dao.findAll();
			System.out.println(warehouse);
			if (warehouse == null || warehouse.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No inventory found.");
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(warehouse));
			}
		}
	}
		
	/*******************************************************************************************/

	class WarehouseByIdHandler {
		
		// GET /warehouse/{id}
		public void getWarehouse(int id, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
		
			Warehouse warehouse = dao.findById(id);
			System.out.println(warehouse);
			if (warehouse == null) {
				System.out.println("No warehouse with id " + id);
				resp.setStatus(404);
				resp.getWriter().append("No warehouse with id " + id);
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(warehouse));
			}
		}
	}
	
	/*******************************************************************************************/

	class WarehousesByNameHandler {
		
		// GET /warehouse/name/{name}
		public void getWarehouses(String name, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			List<Warehouse> warehouse = dao.findByName(name);
			System.out.println(warehouse);
			if (warehouse == null || warehouse.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No warehouses found with name " + name);
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(warehouse));
			}
		}
	}
	
	/*******************************************************************************************/

	class WarehousesByCityHandler {
		
		// GET /warehouse/city/{city}
		public void getWarehouses(String city, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			List<Warehouse> warehouse = dao.findByCity(city);
			System.out.println(warehouse);
			if (warehouse == null || warehouse.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No warehouses found in " + city);
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(warehouse));
			}
		}
	}
	

	/*******************************************************************************************/

	class WarehousesByStateHandler {
		
		// GET /warehouse/state/{state}
		public void getWarehouses(String state, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			List<Warehouse> warehouse = dao.findByStateCode(state.trim().toUpperCase());
			System.out.println(warehouse);
			if (warehouse == null || warehouse.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No warehouses found in " + state);
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(warehouse));
			}
		}
	}
	
	/*******************************************************************************************/

	class WarehousesByZipHandler {
		
		// GET /warehouse/zip/{zip}
		public void getWarehouses(String zip, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			List<Warehouse> warehouse = dao.findByZip(zip);
			System.out.println(warehouse);
			if (warehouse == null || warehouse.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No warehouses found in " + zip);
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(warehouse));
			}
		}
	}
	
	/*******************************************************************************************/

	class WarehousesByAvailableCapacityHandler {
		
		// GET /warehouse/available/{units}
		public void getWarehouses(int units, HttpServletResponse resp) throws SQLException, JsonProcessingException, IOException {
			List<Warehouse> warehouse = dao.findByAvailableCapacity(units);
			System.out.println(warehouse);
			if (warehouse == null || warehouse.isEmpty()) {
				resp.setStatus(404);
				resp.getWriter().append("No warehouses found with availabe capacity of " + units + " units");
			} else {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(warehouse));
			}
		}
	}
}
