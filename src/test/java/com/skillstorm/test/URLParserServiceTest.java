package com.skillstorm.test;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.skillstorm.services.ProductURLParserService;
import com.skillstorm.services.ProductURLParserService.Type;

public class URLParserServiceTest {

	// instance variables refreshed in each test
	private static ProductURLParserService parser;
	
	public URLParserServiceTest() { }
	
	// before all my tests - run this setup method
	@BeforeClass // @BeforeAll
	public static void setupBeforeAll() {
		parser = new ProductURLParserService();
	}

	@Test
	public void splitSimpleURLStartSlash() {
		String url = "/product";
		String[] parts = parser.splitURL(url);
		assertEquals(parts.length, 1);
	}
	
	@Test
	public void splitSimpleURLStartAndEndSlash() {
		String url = "/product/";
		String[] parts = parser.splitURL(url);
		assertEquals(parts.length, 1);
	}
	
	@Test
	public void splitSimpleURLEndSlash() {
		String url = "product/";
		String[] parts = parser.splitURL(url);
		assertEquals(parts.length, 1);
	}
	
	@Test
	public void splitSimpleURLEndSlashSpaces() {
		String url = "product/   ";
		String[] parts = parser.splitURL(url);
		assertEquals(parts.length, 1);
	}
	
	@Test
	public void splitSimpleURLEndSlashwithSpaces() {
		String url = "  product/   ";
		String[] parts = parser.splitURL(url);
		assertEquals(parts.length, 1);
		assertEquals(parts[0], "product");
	}
	
	@Test
	public void testEnumValueOf0() {
		ProductURLParserService.Type type = Type.UNDEFINED;
		assertEquals(type.ordinal(), 0);
	}
	
	@Test
	public void getTypeAndValue() {
		String url = "/product/";
		parser.setUrl(url);
		parser.extractURL();
		assertEquals(parser.getType(), Type.ALL);
		assertEquals(parser.getValue(), "product");
	}
	
	@Test
	public void getTypeAndValueId() {
		String url = "/product/10";
		parser.setUrl(url);
		parser.extractURL();
		assertEquals(parser.getType(), Type.ID);
		assertEquals((int) parser.getValue(), 10);
	}
	

	@Test
	public void getTypeAndValueName() {
		String url = "/product/happy";
		parser.setUrl(url);
		parser.extractURL();
		assertEquals(parser.getType(), Type.NAME);
		assertEquals(parser.getValue(), "happy");
	}
	

	@Test
	public void getTypeAndValueCategory() {
		String url = "/product/category/food";
		parser.setUrl(url);
		parser.extractURL();
		assertEquals(Type.CATEGORY, parser.getType());
		assertEquals("food", parser.getValue());
	}
	

	@Test
	public void getTypeAndValueBrand() {
		String url = "/product/brand/Kirkland";
		parser.setUrl(url);
		parser.extractURL();
		assertEquals(parser.getType(), Type.BRAND);
		assertEquals(parser.getValue(), "Kirkland");
	}
}
