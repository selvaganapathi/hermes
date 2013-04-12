package org.hermes.db;

import java.util.ArrayList;
import java.util.List;

public class Index {

	private String name;
	private String tableSpace;
	private List<String> columns;
	private long nextExtend;
	private long initialExtend;
	private String type;
	private String table;
	private String schema;
	private String parameters;
	private String specialType;
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getSpecialType() {
		return specialType;
	}
	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public long getNextExtend() {
		return nextExtend;
	}
	public void setNextExtend(long nextExtend) {
		this.nextExtend = nextExtend;
	}
	public String getTableSpace() {
		return tableSpace;
	}
	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}
	public long getInitialExtend() {
		return initialExtend;
	}
	public void setInitialExtend(long initialExtend) {
		this.initialExtend = initialExtend;
	}
	
	public void addCloumn (String col)
	{
		 if(null==columns)	
		 {
			 columns=new ArrayList<String>();
		 }
		   columns.add(col);
	}
}
