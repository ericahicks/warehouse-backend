package com.skillstorm.services;

import java.net.URLDecoder;
import java.nio.charset.Charset;

public class ProductURLParserService implements URLParserService {
	
	public static enum Type {
		UNDEFINED,
		ALL,
		ID,
		NAME,
		CATEGORY,
		BRAND
	}
	
	private String url;
	private Type type;
	private Object value;
	
	////////////////////////////////////////////////////
	///////////////// Constructors   //////////////////
	///////////////////////////////////////////////////
	public ProductURLParserService() { }
	
	public ProductURLParserService(String url) { }

	////////////////////////////////////////////////////
	///////////////////// Methods  ////////////////////
	///////////////////////////////////////////////////
	
	public void extractURL() {
		if (url == null) 
			throw new IllegalArgumentException("URL cannot be null. Call parser.setURL(str).");
		String[] splitURL = splitURL(this.url);
		extractType(splitURL);
		extractValue(splitURL[splitURL.length - 1]);
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
	
	public void extractValue(String value) {
		value = URLDecoder.decode(value, Charset.defaultCharset());
		if (value.matches("\\d+")) {
			this.value = Integer.valueOf(value);
		} else {
			this.value = value;
		}
			
	}
	
	/**
	 * Given the the parts of a url /product/category/food
	 * as a String[] array ['product','category','food']
	 * Sets this url parser's type to be one of the following:
	 *     Type.ALL if the url is /product (no modifier)
	 *     Type.ID if the url is /product/{integer} (numeric modifier indicates id given)
	 *     Type.NAME if the url is /product/{string} (string modifer indicates name given)
	 *     Type.CATEGORY if the url is /product/category/{string} (extra specifier category indicates searching by category not id or name)
	 *     Type.BRAND if the url is /product/brand/{string} (extra specifier brand indicates searching by brand not id or name)
	 * @param URLparts the parts of a url, for example, /product/category/food given
	 *     as a String[] array, for example, ['product','category','food']
	 */
	public void extractType(String[] URLparts) {
		switch (URLparts.length) {
		case 1:
			type = Type.ALL;
			break;
		case 2:
			extractValueType(URLparts);
			break;
		case 3:
			extractSubType(URLparts);
			break;
		default:
			type = Type.UNDEFINED;
		}
	}
	
	/**
	 * Given a URL of the format /product/1 or /product/swiffer
	 * will set this type property as Type.ID or Type.NAME
	 * depending on whether the String after the second / can
	 * be parsed as an Integer
	 */
	private void extractValueType(String[] URLparts) {
		String value = URLparts[1];
		if (value.matches("\\d*")) {
			type = Type.ID;
		} else {
			type = Type.NAME;
		}
	}
	
	/**
	 * Given a url of the format /product/category/shoes
	 * sets this type property as Type.CATEGORY or Type.BRAND
	 * or Type.UNDEFINED if not a recognized subcategory
	 */
	private void extractSubType(String[] URLparts) {
		String searchOnValue = URLparts[1];
		System.out.println("SubType is " + searchOnValue);
		switch (searchOnValue) {
		case "category":
			type = Type.CATEGORY;
			break;
		case "brand":
			type = Type.BRAND;
			break;
		default:
			type = Type.UNDEFINED;
			break;
		}
	}

	////////////////////////////////////////////////////
	/////////////// Getters and Setters  //////////////
	///////////////////////////////////////////////////
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
}