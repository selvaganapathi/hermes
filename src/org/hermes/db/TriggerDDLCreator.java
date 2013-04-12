package org.hermes.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TriggerDDLCreator extends DDLCreator {

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
		List<Trigger> sourceTriggers=sourceReader.getTriggers(schema, table);
		List<Trigger> destinationTriggers=destionationReader.getTriggers(schema, table);
		
		
		for(Trigger destTrigger:destinationTriggers)
		{
			
			boolean found=false;
			boolean modify=false;
			
			
			for(Trigger sourceTrigger:sourceTriggers)
			{
				if(destTrigger.getName().equalsIgnoreCase(sourceTrigger.getName()))
				{
					String desc1=null;
					String desc2=null;
					int index1=getIndex(destTrigger.getDescription());
					if(index1!=-1)
					 desc1=destTrigger.getDescription().substring(getIndex(destTrigger.getDescription()));
					else
						desc1=destTrigger.getDescription()	;
					
					if(index1!=-1)
					 desc2=sourceTrigger.getDescription().substring(getIndex(sourceTrigger.getDescription()));
					else
					 desc2=sourceTrigger.getDescription()	;
					
					desc1=desc1.replaceAll("[\n\r ]", "");
					desc2=desc2.replaceAll("[\n\r ]", "");
					String body1=destTrigger.getBody().replaceAll("[\n\r ]", "");
					String body2=sourceTrigger.getBody().replaceAll("[\n\r ]", "");
					
					if(!desc1.equals(desc2) ||
							!body1.equals(body2)	
					)
					modify=true;
					
					found=true;
					break;
						
				}
				
				
			}
			
			if(!found || modify)
			{
				
				 createTriggerDDL( destTrigger) ;
			}
			
		}
	}
	
	private void createTriggerDDL(Trigger destTrigger) throws IOException
	{
		String ddl=Templates.CREATE_TRIGGER_TEMPLATE.replace(Templates.TRIGGER_NAME, destTrigger.getName());
		int index=getIndex(destTrigger.getDescription());
		if(index!=-1)
		ddl=ddl.replace(Templates.TRIGGER_DESCRIPTION, destTrigger.getDescription().substring(index));
		else
			ddl=ddl.replace(Templates.TRIGGER_DESCRIPTION,destTrigger.getDescription());	
		ddl=ddl.replace(Templates.TRIGGER_BODY, destTrigger.getBody());
		write(ddl);
	}
	
	private int getIndex(String desc)
	{
	  int index=	desc.indexOf("after");
	  
	  if(index!=-1)
		  return index;
	  
	   index=	desc.indexOf("before");
	  
	  if(index!=-1)
		  return index;
	  
	  index=	desc.indexOf("instead");
	  
	  if(index!=-1)
		  return index;
	  
	   index=	desc.indexOf("after".toUpperCase());
	  
	  if(index!=-1)
		  return index;
	  
	   index=	desc.indexOf("before".toUpperCase());
	  
	  if(index!=-1)
		  return index;
	  
	  index=	desc.indexOf("instead".toUpperCase());
	  
	  if(index!=-1)
		  return index;
	  
	  return index;
	}

}
