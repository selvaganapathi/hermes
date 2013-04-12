package org.hermes.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hermes.db.util.Logger;


public class TableMerger {
	
	private Connection sourceConnection;
	private Connection destinationConnection;
	private boolean append=true;
	private String sourceUsername;
	private String destinationUsername;
	
	
	public String getDestinationUsername() {
		return destinationUsername;
	}
	public void setDestinationUsername(String destinationUsername) {
		this.destinationUsername = destinationUsername;
	}
	public String getSourceUsername() {
		return sourceUsername;
	}
	public void setSourceUsername(String sourceUsername) {
		this.sourceUsername = sourceUsername;
	}
	public boolean isAppend() {
		return append;
	}
	public void setAppend(boolean append) {
		this.append = append;
	}
	public TableMerger(Connection sourceConnection,Connection destinationConnection)
	{
		this.sourceConnection=sourceConnection;
		this.destinationConnection=destinationConnection;
	}
	public void merge(String schema,List<String> tables) throws SQLException, IOException
	{
		 
		DDLCreator creator=getDDLCreator(schema, tables);
		creator.createDDL();
		creator.shutdown();
		
		
	}

	public DDLCreator getDDLCreator(String schema,List<String> tables) throws IOException
	{
		TableDDLCreator creator=new TableDDLCreator();
		
		creator.setSchema(schema);
		creator.setTables(tables);
		creator.setSourceConnection(sourceConnection);
		creator.setDestinationConnection(destinationConnection);
		creator.setWriter(getWriter(schema));
		
		return creator;
	}
	
	
	
	public FileWriter getWriter (String schema) throws IOException
	{
		
		File file=new File(schema+".SQL"+"."+DBMerger.count.getAndIncrement());
		
		FileWriter writer=new FileWriter(file,append);
		Logger.log("File path: "+file.getAbsolutePath());
		return writer;
	}
	public Connection getDestinationConnection() {
		return destinationConnection;
	}
	public void setDestinationConnection(Connection destinationConnection) {
		this.destinationConnection = destinationConnection;
	}
	public Connection getSourceConnection() {
		return sourceConnection;
	}
	public void setSourceConnection(Connection sourceConnection) {
		this.sourceConnection = sourceConnection;
	}
}
