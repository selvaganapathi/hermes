package org.hermes.db;

import java.util.ArrayList;
import java.util.List;

public class Table {

	String name;
	String schema;
	String tableSpace;
	int initialExtent;
	int nextExtent;
	List<Column> columns;
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public void addCloumn (Column col)
	{
	 if(null==columns)	
	 {
		 columns=new ArrayList<Column>();
	 }
	 columns.add(col);
	}
	public int getInitialExtent() {
		return initialExtent;
	}
	public void setInitialExtent(int initialExtent) {
		this.initialExtent = initialExtent;
	}
	public int getNextExtent() {
		return nextExtent;
	}
	public void setNextExtent(int nextExtent) {
		this.nextExtent = nextExtent;
	}
	public String getTableSpace() {
		return tableSpace;
	}
	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}
}
