package com.skillstorm.test;

import static org.junit.Assert.*;

import org.junit.*;

import com.skillstorm.models.Product;
import com.skillstorm.models.User;
import com.skillstorm.models.Category;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.builders.ProductBuilder;
import com.skillstorm.models.Brand;

public class ProductTest {
	// instance variables refreshed in each test
	private static Brand brand;
	private static Category category;
	private static Product product;
	
	public ProductTest() { }

	// before all my tests - run this setup method
	@BeforeClass // @BeforeAll
	public static void setupBeforeAll() {
		brand = new Brand(1, "Ugly");
		category = new Category(1, "clothes");
		product = new ProductBuilder()
				.id(1)
				.name("sweater")
				.category(category)
				.brand(brand)
				.size("large")
				.imageURL("../assets/img/sweater.png")
				.build();
	}

	@Test
	public void setInvalidAbbreviationTooLong() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(product));
	}

}
