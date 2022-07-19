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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.conf.WarehouseDbCreds;
import com.skillstorm.daos.MySQLProductDAOImpl;
import com.skillstorm.daos.ProductDAO;
import com.skillstorm.models.Product;
import com.skillstorm.services.URLParserService;

@WebServlet(urlPatterns = "/product/*")
public class ProductServlet extends HttpServlet {

	/*
	 * Servlet Lifecycle
	 * 
	 * init - A method called when the web server first creates our servlet
	 * service - method called before EVERY request
	 * destroy - method called when the web server is stopped/servlet terminates
	 */
	
	@Override
	public void init() throws ServletException {
		// This allows us to write code that is run right as the servlet is created
		// You can establish any connections
		try {
			conn = WarehouseDbCreds.getInstance().getConnection();
			dao = new MySQLProductDAOImpl(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ProductServlet Created!");
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
		System.out.println("ProductServlet Destroyed!");
		super.destroy();
	}
	
	// I would prefer filters to this
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// This method activates on ALL HTTP requests to this servlet
		System.out.println("Servicing request!");
		super.service(req, resp); // Keep this line
	}
	private static final long serialVersionUID = -1005346930881540665L;
	Connection conn;
	ProductDAO dao;
	ObjectMapper mapper = new ObjectMapper();
	URLParserService urlService = new URLParserService();
	
    // Returns product(s)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int id = urlService.extractIdFromURL(req.getPathInfo());
			// This means they want a specific product. Find that product
			Product product = dao.findById(id);
			if (product != null) {
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(product));
			} else {
				resp.setStatus(404);
				resp.getWriter().append("No product with the provided Id found");
			}
		} catch (Exception e) {
			try {
				// Means that there wasn't an id in the URL. Fetch all artists instead
				List<Product> products = dao.findAll();
				System.out.println(products);
				resp.setContentType("application/json");
				resp.getWriter().print(mapper.writeValueAsString(products));
			} catch (SQLException ex) {
				resp.setStatus(500);
				resp.getWriter().append("Unable to fetch products");
			}
		}
		
	}
	
}
