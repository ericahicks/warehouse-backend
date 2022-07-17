package com.skillstorm.test;

import static org.junit.Assert.*;

import org.junit.*;

import com.skillstorm.models.Warehouse;

public class WarehouseTest {
	
	// instance variables refreshed in each test
	private static Warehouse warehouse;
	
	public WarehouseTest() { }
	
	// before all my tests - run this setup method
	@BeforeClass // @BeforeAll
	public static void setupBeforeAll() {
		warehouse = new Warehouse();
	}
	
	@Test
	public void setInvalidZipcodeTooShort() {
		assertThrows(IllegalArgumentException.class, () -> {
			warehouse.setZip("1234");
		});
	}
	
	@Test
	public void setInvalidZipcodeTooLong() {
		assertThrows(IllegalArgumentException.class, () -> {
			warehouse.setZip("123456789");
		});
	}
	
	@Test
	public void setInvalidZipcodeNoDash() {
		assertThrows(IllegalArgumentException.class, () -> {
			warehouse.setZip("12345 6789");
		});
	}
	
	@Test
	public void setValidZipcode() {
		String zip = "12345";
		warehouse.setZip(zip);
		assertEquals(warehouse.getZip(), zip);
	}
	
	@Test
	public void setValidZipcodeWithStarting0() {
		String zip = "02345";
		warehouse.setZip(zip);
		assertEquals(warehouse.getZip(), zip);
	}
	
	@Test
	public void setValidZipcodeWithDash() {
		String zip = "12345-6789";
		warehouse.setZip(zip);
		assertEquals(warehouse.getZip(), zip);
	}
	
	@Test
	public void setInvalidZipcodeWithDash() {
		assertThrows(IllegalArgumentException.class, () -> {
			warehouse.setZip("12345-");
		});
	}
	
	@Test
	public void setInvalidCapacity() {
		assertThrows(IllegalArgumentException.class, () -> {
			warehouse.setCapacity(-1);
		});
	}
	
	@Test
	public void setValidCapacity() {
		int capacity = 10;
		warehouse.setCapacity(capacity);
		assertEquals(warehouse.getCapacity(), capacity);
	}
	
	@Test
	public void setValidCapacityOf0() {
		int capacity = 0;
		warehouse.setCapacity(capacity);
		assertEquals(warehouse.getCapacity(), capacity);
	}
}
