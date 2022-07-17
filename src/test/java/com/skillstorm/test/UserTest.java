package com.skillstorm.test;

import static org.junit.Assert.*;

import org.junit.*;

import com.skillstorm.models.User;

public class UserTest {


	// instance variables refreshed in each test
	private static User user;
	
	public UserTest() { }
	
	// before all my tests - run this setup method
	@BeforeClass // @BeforeAll
	public static void setupBeforeAll() {
		user = new User();
	}

	@Test
	public void setInvalidEmail() {
		assertThrows(IllegalArgumentException.class, () -> {
			user.setEmail("hello");
		});
	}
	
	
	@Test
	public void setValidEmail() {
		String email = "mail4elh@hotmail.com";
		user.setEmail(email);
		assertEquals(user.getEmail(), email);
	}
	
}
