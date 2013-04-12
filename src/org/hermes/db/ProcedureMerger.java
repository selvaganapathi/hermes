package org.hermes.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProcedureMerger  extends TableMerger{

	
	public ProcedureMerger(Connection sourceConnection, Connection destinationConnection) {
		super(sourceConnection, destinationConnection);
		
	}
	
	public void merge(String schema,List<String> procedures) throws SQLException, IOException
	{
		 
		ProcedureDDLCreator creator=getDDLCreator(schema);
		
		for(String procedure:procedures)
		{
			creator.setName(procedure);
			creator.createDDL();
		}
		
		creator.shutdown();
		
		
	}
	
	public ProcedureDDLCreator getDDLCreator(String schema) throws IOException
	{
		ProcedureDDLCreator creator=new ProcedureDDLCreator();
		creator.setSchema(schema);
		creator.setSourceConnection(getSourceConnection());
		creator.setDestinationConnection(getDestinationConnection());
		creator.setWriter(getWriter(schema));
		
		return creator;
	}

}
