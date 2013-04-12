package org.hermes.db;

import java.util.ArrayList;
import java.util.List;

public class Grant {

	private String grantee;
	private String table;
	private String schema;
	private List<String> privileges;
	public String getGrantee() {
		return grantee;
	}
	public void setGrantee(String grantee) {
		this.grantee = grantee;
	}
	public List<String> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
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
	
	public void addPrivilege (String privilege)
	{
		 if(null==privileges)	
		 {
			 privileges=new ArrayList<String>();
		 }
		 privileges.add(privilege);
	}
}
