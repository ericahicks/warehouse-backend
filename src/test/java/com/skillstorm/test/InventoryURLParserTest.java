package com.skillstorm.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.skillstorm.services.InventoryURLParserService;
import com.skillstorm.services.InventoryURLParserService.Type;

public class InventoryURLParserTest {
	// instance variables refreshed in each test
	private static InventoryURLParserService parser;
	
	public InventoryURLParserTest() { }
	
	// before all my tests - run this setup method
	@BeforeClass // @BeforeAll
	public static void setupBeforeAll() {
		parser = new InventoryURLParserService();
	}

	@Test
	public void splitSimpleURLStartSlash() {
		String url = "/inventory";
		String[] parts = parser.splitURL(url);
		assertEquals(1, parts.length);
	}
	
	@Test
	public void splitSimpleURLStartAndEndSlash() {
		String url = "/inventory/";
		String[] parts = parser.splitURL(url);
		assertEquals(1, parts.length);
	}
	
	@Test
	public void splitSimpleURLEndSlash() {
		String url = "inventory/";
		String[] parts = parser.splitURL(url);
		assertEquals(1, parts.length);
	}
	
	@Test
	public void split2Domain() {
		String url = "inventory/warehouse/1";
		String[] parts = parser.splitURL(url);
		assertEquals(3, parts.length);
	}
	
	@Test
	public void split2DomainProduct() {
		String url = "inventory/product/1";
		String[] parts = parser.splitURL(url);
		assertEquals(3, parts.length);
	}
	
	@Test
	public void split2DomainTypeProduct() {
		String url = "inventory/product/1";
		parser.setURL(url);
		parser.extractURL();
		assertEquals(Type.PRODUCT, parser.getType());
		assertEquals(1, parser.getSubDomain2());
	}
	
	@Test
	public void split2DomainTypeWarehouse() {
		String url = "inventory/warehouse/1";
		parser.setURL(url);
		parser.extractURL();
		assertEquals(Type.WAREHOUSE, parser.getType());
		assertEquals("warehouse", parser.getSubDomain1());
		assertEquals(1, parser.getSubDomain2());
	}
	
	@Test
	public void split2DomainBoth() {
		String url = "inventory/12346/1";
		parser.setURL(url);
		parser.extractURL();
		assertEquals(Type.BOTH, parser.getType());
		assertEquals(12346, parser.getSubDomain1());
		assertEquals(1, parser.getSubDomain2());
	}
	

}
