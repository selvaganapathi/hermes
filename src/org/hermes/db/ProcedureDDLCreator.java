package org.hermes.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.hermes.db.util.Logger;


public class ProcedureDDLCreator  extends DDLCreator {

	private String schema;
	private String name;

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

	public void createDDL() throws SQLException, IOException {
		
		TableReader sourceReader=new TableReader(sourceConnection);
		TableReader destionationReader=new TableReader(destinationConnection);
		Procedure sourceProcedure=sourceReader.getProcedure(schema,name);
		Procedure destProcedure=destionationReader.getProcedure(schema,name);
		List<String> sourceLines=	sourceProcedure.getLines();
		List<String> destLines=	destProcedure.getLines();
		
		int destSize=null!=destLines?destLines.size():0;
		int sourceSize=null!=sourceLines?sourceLines.size():0;
		
		if(sourceSize==0)
		{
			procedureDDL(destProcedure);
			return ;
		}
		boolean modify=false;
		for(int i=0;i<destSize;i++)
		{
			String destLine=destLines.get(i);
			destLine=destLine.replaceAll("[\n\r ]", "");
			if(i==0)
				continue;
			if(destLine.length()>1 )
			{
				if(sourceSize<=i)
				{
					Logger.log("first line no"+i);
					modify=true;
					break;
				}
				else
				{
				  String sourceLine=sourceLines.get(i).replaceAll("[\n\r ]", "");
				  if(!destLine.equalsIgnoreCase(sourceLine))
				  {
					  Logger.log("line no"+i);
					  Logger.log("line "+destLine);
					  Logger.log("line "+sourceLine);
					  modify=true; 
					  break;
				  }
				}
			}
		}
		
		if(modify)
		{
			procedureDDL(destProcedure);
		}
		
	}
	
	private void procedureDDL(Procedure destProcedure) throws IOException
	{
		 String ddl=Templates.PROCEDURE_TEMPLATE.replace(Templates.PROCEDURE_NAME, destProcedure.getName());
		 List<String> destLines=	destProcedure.getLines();
		 StringBuilder builder=new StringBuilder();
		 for(String line:destLines)
		 {
			 builder.append(line);
			// builder.append("\r\n");
		 }
		 ddl=ddl.replace(Templates.PROCEDURE_DETAILS, builder.toString());
		 write(ddl);
		
	}

}
