package org.hermes.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ViewMerger extends TableMerger{

	public ViewMerger(Connection sourceConnection, Connection destinationConnection) {
		super(sourceConnection, destinationConnection);
		
	}

	public void merge(String schema,List<String> views) throws SQLException, IOException
	{
		 
		DDLCreator creator=getDDLCreator(schema, views);
		creator.createDDL();
		creator.shutdown();
		
		
	}
	
	@Override
	public DDLCreator getDDLCreator(String schema, List<String> views) throws IOException {
		ViewDDLCreator creator=new ViewDDLCreator();
		
		creator.setSchema(schema);
		creator.setViews(views);
		creator.setSourceConnection(getSourceConnection());
		creator.setDestinationConnection(getDestinationConnection());
		creator.setWriter(getWriter(schema));
		return creator;
	}
}
