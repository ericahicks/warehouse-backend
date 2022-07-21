package com.skillstorm.services;

import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * Class for parsing a URL that is used for an API call
 * to Create/Read/Update/Delete a product.
 * 
 * GET all is
 *  /inventory or /inventory/ or inventory/
 *  
 * GET doesn't have a body, so the URL to get by warehouse and id is
 *  /inventory/{warehouseid}/{productid}
 *  
 *  To get all the products from a warehouse do
 *  /inventory/warehouse/{warehouseid}
 *  
 *  To get all the inventory of a certain product
 *  /inventory/product/{productid}
 *  
 *  PUT can pass the key or just put it in the body
 *  /inventory/{warehouseid}/{productid} 
 *  /inventory
 */
public class InventoryURLParserService {
	
	public static enum Type {
		UNDEFINED,
		ALL,
		WAREHOUSE,
		PRODUCT,
		BOTH
	}
	
	private String url;
	private Type type;
	private Object subDomain1;
	private Object subDomain2;
	
	
	////////////////////////////////////////////////////
	///////////////// Constructors   //////////////////
	///////////////////////////////////////////////////

	public InventoryURLParserService() { }
	
	public InventoryURLParserService(String url) { }

	////////////////////////////////////////////////////
	///////////////////// Methods  ////////////////////
	///////////////////////////////////////////////////
	
	public void extractURL() {
		String[] urlParts = splitURL(this.url);
		determineURLType(urlParts);
	}
	

	public String[] splitURL(String url) {
		if (url == null) 
			throw new IllegalArgumentException("URL cannot be null.");
		// remove white space
		url = url.trim();
		// trim starting and ending /
		if (url.startsWith("/")) {
			url = url.substring(1);
		}
		return url.split("/");
	}
	
	/** 
	 * Sets the type instance variable
	 */
	private void determineURLType(String[] urlParts) {
		switch (urlParts.length) {
		case 1:
			type = Type.ALL;
			break;
		case 3:
			extractValues(urlParts);
			break;
		case 2:
		default:
			type = Type.UNDEFINED;
		}
	}
	
	private void extractValues(String[] urlParts) {
		subDomain1 = extractValue(urlParts[1]);
		subDomain2 = extractValue(urlParts[2]);
		if (subDomain1.toString().matches("\\d+")) {
			this.type = Type.BOTH;
		} else {
			this.type = Enum.valueOf(Type.class, 
					                  urlParts[1].trim().toUpperCase());
		}
		
		
	}
	
	public Object extractValue(String value) {
		value = URLDecoder.decode(value, Charset.defaultCharset());
		if (value.matches("\\d+")) {
			return Integer.valueOf(value);
		} else {
			return value;
		}
			
	}

	////////////////////////////////////////////////////
	///////////////// Getters and Setters  ////////////
	///////////////////////////////////////////////////

	/**
	 * @return the subDomain1
	 */
	public Object getSubDomain1() {
		return subDomain1;
	}

	/**
	 * @return the subDoman2
	 */
	public Object getSubDomain2() {
		return subDomain2;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
}
