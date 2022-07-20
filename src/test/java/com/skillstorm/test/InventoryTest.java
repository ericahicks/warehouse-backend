package com.skillstorm.test;

import static org.junit.Assert.*;

import org.junit.*;

import com.skillstorm.models.Brand;
import com.skillstorm.models.Category;
import com.skillstorm.models.InventoryItem;
import com.skillstorm.models.Product;

public class InventoryTest {

	// instance variables refreshed in each test
	private static InventoryItem inventory;
	
	public InventoryTest() { }
	
	// before all my tests - run this setup method
	@BeforeClass // @BeforeAll
	public static void setupBeforeAll() {
		inventory = new InventoryItem();
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
			InventoryItem inventB = new InventoryItem(null, null, -1);
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
			InventoryItem inventB = new InventoryItem(null, null, 3, -1);
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
