package com.skillstorm.test;

import static org.junit.Assert.*;

import org.junit.*;

import com.skillstorm.models.State;

public class StateTest {

	// instance variables refreshed in each test
	private static State state;
	
	public StateTest() { }

	@Test
	public void setInvalidAbbreviationTooLong() {
		assertThrows(IllegalArgumentException.class, () -> {
			State state = new State("California", "Cali");
		});
	}
	
	@Test
	public void setInvalidAbbreviationTooShort() {
		assertThrows(IllegalArgumentException.class, () -> {
			State state = new State("California", "C");
		});
	}
	
	@Test
	public void setInvalidAbbreviationNotLetters() {
		assertThrows(IllegalArgumentException.class, () -> {
			State state = new State("California", "C1");
		});
	}
	

	@Test
	public void setInvalidAbbreviationNonLetters() {
		assertThrows(IllegalArgumentException.class, () -> {
			State state = new State("California", "C_");
		});
	}
	
	@Test
	public void setInvalidAbbreviationNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			State state = new State("California", null);
		});
	}
	

	@Test
	public void setInvalidANameNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			State state = new State(null, "CA");
		});
	}
	
	@Test
	public void setValidAbbreviation() {
		String abbr = "CA";
		State state = new State("California", "CA");
		assertEquals(state.getAbbreviation(), abbr);
	}
	

	@Test
	public void setValidAbbreviationLowercase() {
		String abbr = "CA";
		State state = new State("California", "ca");
		assertEquals(state.getAbbreviation(), abbr.toUpperCase());
	}
	
}
