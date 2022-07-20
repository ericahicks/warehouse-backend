package com.skillstorm.builders;

import com.skillstorm.models.State;

public class WarehouseQueryBuilder {
	private String sql = "SELECT w.warehouseid, w.name, w.capacity, " 
		    + "w.street, w.city, w.state as statecode, s.name as statename, zip "
			+ "FROM warehouse w "
			+ "  LEFT JOIN state s "
			+ "  ON w.state = s.abbreviation ";
	private boolean hasWhere = false;
	private boolean needsAnd = false;
	
	public WarehouseQueryBuilder() {
		
	}
	
	public WarehouseQueryBuilder(String name, int capacity, 
			String street, String city, State state,
			String zip) {
		if (name != null && !name.isEmpty())
			name();
		if (street != null && !street.isEmpty())
			street();
		if (city != null && !city.isEmpty())
			city();
		if (state != null)
			state();
		if (zip != null)
			zip();
		if (capacity >= 0) 
			capacity();
	}
	
	public WarehouseQueryBuilder name() {
		append(); // joining syntax needed to add name clause
		sql += " w.name = ? ";
		return this;
	}
	
	public WarehouseQueryBuilder capacity() {
		append(); // joining syntax needed to add name clause
		sql += " w.capacity >= ? ";
		return this;
	}
	
	public WarehouseQueryBuilder street() {
		append(); // joining syntax needed to add name clause
		sql += " w.street LIKE ? ";
		return this;
	}
	
	public WarehouseQueryBuilder city() {
		append(); // joining syntax needed to add name clause
		sql += " w.city >= ? ";
		return this;
	}
	
	public WarehouseQueryBuilder state() {
		append(); // joining syntax needed to add name clause
		sql += " s.abbreviation = ? ";
		return this;
	}
	
	public WarehouseQueryBuilder zip() {
		append(); // joining syntax needed to add name clause
		sql += " w.zip = ? ";
		return this;
	}
	
	public void append() {
		if (!hasWhere) {
			sql += " WHERE ";
			hasWhere = true;
			needsAnd = true;
		} else if (needsAnd) {
			sql += " AND ";
		}
	}
	
	public String build() {
		return sql;
	}

}
