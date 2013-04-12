package org.hermes.db;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DDLCreator  implements Merger{

	Merger merger;
	protected Connection sourceConnection;
	protected Connection destinationConnection;
	protected String ddl;
	private FileWriter writer;
	
	public FileWriter getWriter() {
		return writer;
	}

	public void setWriter(FileWriter writer) {
		this.writer = writer;
	}

	public String getDdl() {
		return ddl;
	}

	public void setDdl(String ddl) {
		this.ddl = ddl;
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

	public DDLCreator (Merger merger)
	{
		this.merger=merger;
	}
	
	public DDLCreator ()
	{
		
	}
	
	public void write(String ddl) throws IOException
	{
		writer.write(ddl);
		writer.flush();
	}
	
	public void shutdown() throws IOException, SQLException
	{
		writer.close();
		//sourceConnection.close();
		//destinationConnection.close();
	
	}
}
