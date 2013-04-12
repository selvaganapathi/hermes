package org.hermes.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GrantDDLCreator extends DDLCreator {

	private String schema;
	private String table;
	
	
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
	
	public void createDDL() throws SQLException, IOException {
		
		TableReader sourceReader=new TableReader(sourceConnection);
		TableReader destionationReader=new TableReader(destinationConnection);
		List<Grant> sourceGrants= sourceReader.getGrants(schema, table);
		List<Grant> destGrants= destionationReader.getGrants(schema, table);
		List<String> users=	sourceReader.getUsers();
		
		for(Grant desgrant:destGrants)
		{
			boolean found=false;
			for(Grant sourcegGrant:sourceGrants)
			{
				if(sourcegGrant.getGrantee().equalsIgnoreCase(desgrant.getGrantee()) )
				{
					found=true;
					 if( sourcegGrant.getPrivileges().size()< desgrant.getPrivileges().size() )
					 {
						 
						 writeGrants(desgrant);
					 }
					 break;
						 
				}
			}
			
			if(!found)
			{
				
				 for(String user:users)
				 {
					 if(user.equalsIgnoreCase(desgrant.getGrantee()))
						 writeGrants(desgrant);
				 }
			
			}
			
		}
		
		
		
		
		
	}
	
	
	
	private void writeGrants(Grant grant) throws IOException
	{
		 String ddl=Templates.GRANT_TEMPLATE.replace(Templates.TABLE_NAME, table);
		 List<String> privileges=grant.getPrivileges();
			StringBuilder builder=new StringBuilder();
			int size=privileges.size();
			for(int i=0;i<size;i++)
			{
				String column=privileges.get(i);
				builder.append(column);
				if(i!=size-1)
				builder.append(',');
			}
		 ddl=ddl.replace(Templates.PRIVILEGES,builder.toString() );
		 ddl=ddl.replace(Templates.GRANTEE, grant.getGrantee());
		 write(ddl);
	}

}
