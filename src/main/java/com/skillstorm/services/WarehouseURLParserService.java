package com.skillstorm.services;

import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * Class for parsing a URL that is used for an API call
 * to Create/Read/Update/Delete a warehouse.
 * 
 * GET all is
 *  /warehouse or /warehouse/ or warehouse/
 *  
 * GET doesn't have a body, so the URL to get by different specifiers is:
 *  /warehouse/{warehouseid}
 *  /warehouse/name/{name}
 *  /warehouse/city/{city}
 *  /warehouse/state/{statecode}
 *  /warehouse/zip/{zipcode}
 *  /warehouse/available/{available-capacity-units}
 */

public class WarehouseURLParserService implements URLParserService {
	

	public static enum Type {
		UNDEFINED,
		ALL,
		ID,
		NAME,
		CITY,
		STATE,
		ZIP,
		AVAILABLE
	}
	
	private String url;
	private Type type;
	private Object subDomain1;
	private Object subDomain2;

	////////////////////////////////////////////////////
	///////////////// Constructors   //////////////////
	///////////////////////////////////////////////////

	public WarehouseURLParserService() { }
	
	public WarehouseURLParserService(String url) { }

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
		case 2:
			try {
				subDomain1 = Integer.valueOf(urlParts[1]);
				type = Type.ID;
			} catch (IllegalArgumentException e) {
				type = Type.UNDEFINED;
			}
			break;
		case 3:
			extractValues(urlParts);
			break;
		default:
			type = Type.UNDEFINED;
		}
	}
	
	private void extractValues(String[] urlParts) {
		subDomain1 = extractValue(urlParts[1]);
		subDomain2 = extractValue(urlParts[2]);
		this.type = Enum.valueOf(Type.class, 
				                  urlParts[1].trim().toUpperCase());
		if (type == Type.AVAILABLE) {
			try {
				subDomain2 = Integer.valueOf(urlParts[2]);
			} catch (IllegalArgumentException e) {
				type = Type.UNDEFINED;
			}
		}
	}
	
	public Object extractValue(String value) {
		value = URLDecoder.decode(value, Charset.defaultCharset());
		return value;
			
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
