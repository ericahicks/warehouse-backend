package com.skillstorm.test;

import static org.junit.Assert.*;

import org.junit.*;

import com.skillstorm.models.Inventory;

public class InventoryTest {

	// instance variables refreshed in each test
	private static Inventory inventory;
	
	public InventoryTest() { }
	
	// before all my tests - run this setup method
	@BeforeClass // @BeforeAll
	public static void setupBeforeAll() {
		inventory = new Inventory();
	}

	@Test
	public void setInvalidQuantity() {
		assertThrows(IllegalArgumentException.class, () -> {
			inventory.setQuantity(-1);
		});
	}
	
	@Test
	public void setInvalidQuantityInCreate() {
		assertThrows(IllegalArgumentException.class, () -> {
			Inventory inventB = new Inventory(null, null, -1);
		});
	}
	
	@Test
	public void setValidQuantity() {
		int quantity = 3;
		inventory.setQuantity(quantity);
		assertEquals(inventory.getQuantity(), quantity);
	}
	
	@Test
	public void setInvalidMinimum() {
		assertThrows(IllegalArgumentException.class, () -> {
			inventory.setMinimum(-1);
		});
	}
	
	@Test
	public void setInvalidMinimumInCreate() {
		assertThrows(IllegalArgumentException.class, () -> {
			Inventory inventB = new Inventory(null, null, 3, -1);
		});
	}
	
	@Test
	public void setValidMinimum() {
		int quantity = 3;
		inventory.setMinimum(quantity);
		assertEquals(inventory.getMinimum(), quantity);
	}
	
	@Test
	public void setValidMinimumOf0() {
		int quantity = 0;
		inventory.setMinimum(quantity);
		assertEquals(inventory.getMinimum(), quantity);
	}
}
