package org.hermes.db;

import java.util.ArrayList;
import java.util.List;

public class Constraint {

	List<String> columns;
	private String name;
	private String type;
	private String condition;
	private String owner;
	private String table;
	private String rOwner;
	private String rName;
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public void addCloumn (String col)
	{
	 if(null==columns)	
	 {
		 columns=new ArrayList<String>();
	 }
	 columns.add(col);
	}
	public String getRName() {
		return rName;
	}
	public void setRName(String name) {
		rName = name;
	}
	public String getROwner() {
		return rOwner;
	}
	public void setROwner(String owner) {
		rOwner = owner;
	}
}
