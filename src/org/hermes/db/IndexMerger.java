package org.hermes.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class IndexMerger extends TableMerger{

	public IndexMerger(Connection sourceConnection, Connection destinationConnection) {
		super(sourceConnection, destinationConnection);
		
	}

	public void merge(String schema,List<String> tables) throws SQLException, IOException
	{
		 
		DDLCreator creator=getDDLCreator(schema, tables);
		creator.createDDL();
		creator.shutdown();
		
		
	}
	
	@Override
	public DDLCreator getDDLCreator(String schema, List<String> tables) throws IOException {
		IndexDDLCreator creator=new IndexDDLCreator();
		
		creator.setSchema(schema);
		creator.setTable(tables.get(0));
		creator.setSourceConnection(getSourceConnection());
		creator.setDestinationConnection(getDestinationConnection());
		creator.setWriter(getWriter(schema));
		return creator;
	}
}
