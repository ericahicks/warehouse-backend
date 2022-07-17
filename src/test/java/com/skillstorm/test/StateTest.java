package com.skillstorm.test;

import static org.junit.Assert.*;

import org.junit.*;

import com.skillstorm.models.State;

public class StateTest {

	// instance variables refreshed in each test
	private static State state;
	
	public StateTest() { }
	
	// before all my tests - run this setup method
	@BeforeClass // @BeforeAll
	public static void setupBeforeAll() {
		state = new State();
	}
	

	@Test
	public void setInvalidAbbreviationTooLong() {
		assertThrows(IllegalArgumentException.class, () -> {
			state.setAbbreviation("ABC");
		});
	}
	
	@Test
	public void setInvalidAbbreviationTooShort() {
		assertThrows(IllegalArgumentException.class, () -> {
			state.setAbbreviation("A");
		});
	}
	
	@Test
	public void setInvalidAbbreviationNotLetters() {
		assertThrows(IllegalArgumentException.class, () -> {
			state.setAbbreviation("A1");
		});
	}
	
	@Test
	public void setInvalidAbbreviationNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			state.setAbbreviation(null);
		});
	}
	
	@Test
	public void setValidAbbreviation() {
		String abbr = "CA";
		state.setAbbreviation(abbr);
		assertEquals(state.getAbbreviation(), abbr);
	}
	
}
