package com.skillstorm.test;


import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.skillstorm.services.WarehouseURLParserService;
import com.skillstorm.services.WarehouseURLParserService.Type;

public class WarehouseURLParserTest {
	

	// instance variables refreshed in each test
	private static WarehouseURLParserService parser;
	
	public WarehouseURLParserTest() { }
	
	// before all my tests - run this setup method
	@BeforeClass // @BeforeAll
	public static void setupBeforeAll() {
		parser = new WarehouseURLParserService();
	}

	@Test
	public void splitSimpleURLStartSlash() {
		String url = "/warehouse";
		String[] parts = parser.splitURL(url);
		assertEquals(1, parts.length);
	}
	

	@Test
	public void splitSimpleURLStartAndEndSlash() {
		String url = "/warehouse/";
		String[] parts = parser.splitURL(url);
		assertEquals(1, parts.length);
	}
	
	@Test
	public void splitSimpleURLEndSlash() {
		String url = "warehouse/";
		String[] parts = parser.splitURL(url);
		assertEquals(1, parts.length);
	}
	
	@Test
	public void split1Domain() {
		String url = "warehouse/1";
		String[] parts = parser.splitURL(url);
		assertEquals(2, parts.length);
	}
	
	@Test
	public void split2Domain() {
		String url = "warehouse/available/1";
		String[] parts = parser.splitURL(url);
		assertEquals(3, parts.length);
	}
	
	@Test
	public void split2DomainName() {
		String url = "warehouse/name/sweater";
		String[] parts = parser.splitURL(url);
		assertEquals(3, parts.length);
	}
	
	@Test
	public void split1DomainTypeId() {
		String url = "warehouse/1";
		parser.setURL(url);
		parser.extractURL();
		assertEquals(Type.ID, parser.getType());
		assertEquals(1, parser.getSubDomain1());
	}
	
	@Test
	public void split1DomainTypeName() {
		String url = "warehouse/name/Her%20House";
		parser.setURL(url);
		parser.extractURL();
		assertEquals(Type.NAME, parser.getType());
		assertEquals("name", parser.getSubDomain1());
		assertEquals("Her House", parser.getSubDomain2());
	}
	
	@Test
	public void split2DomainTypeAvailable() {
		String url = "warehouse/available/100";
		parser.setURL(url);
		parser.extractURL();
		assertEquals(Type.AVAILABLE, parser.getType());
		assertEquals("available", parser.getSubDomain1());
		assertEquals(100, parser.getSubDomain2());
	}
	
	@Test
	public void split2DomainState() {
		String url = "warehouse/state/PA";
		parser.setURL(url);
		parser.extractURL();
		assertEquals(Type.STATE, parser.getType());
		assertEquals("state", parser.getSubDomain1());
		assertEquals("PA", parser.getSubDomain2());
	}
	
	@Test
	public void split2Domainzip() {
		String url = "warehouse/zip/19355";
		parser.setURL(url);
		parser.extractURL();
		assertEquals(Type.ZIP, parser.getType());
		assertEquals("zip", parser.getSubDomain1());
		assertEquals("19355", parser.getSubDomain2());
	}
	
	@Test
	public void split2DomainCity() {
		String url = "warehouse/city/Malvern";
		parser.setURL(url);
		parser.extractURL();
		assertEquals(Type.CITY, parser.getType());
		assertEquals("city", parser.getSubDomain1());
		assertEquals("Malvern", parser.getSubDomain2());
	}

}
