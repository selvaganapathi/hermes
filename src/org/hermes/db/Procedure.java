package org.hermes.db;

import java.util.ArrayList;
import java.util.List;

public class Procedure {

	private String name;
	private String schema;
	private List<String> lines;
	public List<String> getLines() {
		return lines;
	}
	public void setLines(List<String> lines) {
		this.lines = lines;
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
	
	public void addLine (String col)
	{
	 if(null==lines)	
	 {
		 lines=new ArrayList<String>();
	 }
	 lines.add(col);
	}
}
