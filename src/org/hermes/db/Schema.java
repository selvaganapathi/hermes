package org.hermes.db;

import java.util.ArrayList;
import java.util.List;

public class Schema {

	private List<Table> tables;
	private List<View> views;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Table> getTables() {
		return tables;
	}
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	public List<View> getViews() {
		return views;
	}
	public void setViews(List<View> views) {
		this.views = views;
	}
	
	public void addTable(Table tab)
	{
	 if(null==tables)	
	 {
		 tables=new ArrayList<Table>();
	 }
	 tables.add(tab);
	}
	
	public void addViews(View tab)
	{
	 if(null==views)	
	 {
		 views=new ArrayList<View>();
	 }
	 views.add(tab);
	}
}
