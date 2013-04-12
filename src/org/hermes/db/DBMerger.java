package org.hermes.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.hermes.db.util.Logger;


public class DBMerger {

	private Connection destinationConnection;
	private Connection sourceConnection;
	private String sourceUsername;
	private String sourcePassword;
	private String sourceUrl;
	private String destinationUsername;
	private String destinationPassword;
	private String destinationUrl;
	private int connectionsSize=5;
	private ArrayBlockingQueue<Connection> destinationConnections=new ArrayBlockingQueue<Connection>(connectionsSize); 
	private ArrayBlockingQueue<Connection> sourceConnections=new ArrayBlockingQueue<Connection>(connectionsSize);
	
	public  static final AtomicInteger count=new AtomicInteger(0);
	
	public void init() throws ClassNotFoundException, SQLException 
	{
		String driverName = "oracle.jdbc.driver.OracleDriver";
	    Class.forName(driverName);
	    try
	    {
	    	for(int i=0;i<connectionsSize;i++)
	    	{
	    		 destinationConnections.add(DriverManager.getConnection(destinationUrl, destinationUsername, destinationPassword));
	          System.out.println("dest connection");
	         }
	    }
	    catch (SQLException e) {
			throw new RuntimeException("Not able to connect to Destination DB",e);
		}
	    
	    try
	    {
	    	for(int i=0;i<connectionsSize;i++)
	    	sourceConnections.add(DriverManager.getConnection(sourceUrl, sourceUsername, sourcePassword));
	    	
	    }
	    catch (SQLException e) {
			throw new RuntimeException("Not able to connect to Source DB",e);
		}
	    
	   
	    	File list=new File(System.getProperty("user.dir"));
	    	Logger.log("Working Directory = " +
	                System.getProperty("user.dir"));
	    	
	    	File[] files=list.listFiles();
	    	
	    	for(File file: files)
	    	{
	    		if(file.getName().endsWith("sql") || file.getName().endsWith("SQL")
	    						
	    		)
	    		{
	    			file.delete();
	    		}
	    	}
	    System.out.println("DB Merger is initialized");
	   
	}
	
	public void read() throws SQLException
	{
		Connection con=null;
		try
		{
			con=destinationConnections.take();
			TableReader reader=new TableReader(con);
			reader.setUsername(sourceUsername);
			reader.getDetails();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(con);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<Schema> getSchemas() throws SQLException
	{
		Connection con=null;
		try
		{
			System.out.println("scema connection is goung");
			con=destinationConnections.take();
			System.out.println("scema connection got");
			TableReader reader=new TableReader(con);
			reader.setUsername(sourceUsername);
			return reader.getSchemas();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(con);
				System.out.println("scema connection given");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public List<Table> getTables(String schema) throws SQLException
	{
		Connection con=null;
		try
		{
			con=destinationConnections.take();
		TableReader reader=new TableReader(con);
		reader.setUsername(sourceUsername);
		return reader.getTables(schema);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(con);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	public List<View> getViewes(String schema) throws SQLException
	{
		Connection con=null;
		try
		{
			con=destinationConnections.take();
		TableReader reader=new TableReader(con);
		reader.setUsername(sourceUsername);
		return reader.getViews(schema);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(con);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public List<Procedure> getProcedures(String schema) throws SQLException, IOException
	{
		Connection con=null;
		try
		{
			con=destinationConnections.take();
		TableReader reader=new TableReader(con);
		reader.setUsername(sourceUsername);
		return reader.getProcedures(schema);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(con);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	public void mergeTable(String schema ,List<String> tables) throws SQLException, IOException
	{
		Connection sourceCon=null;
		Connection destCon=null;
		try
		{
			sourceCon=sourceConnections.take();
			destCon=destinationConnections.take();
		TableMerger merger=new TableMerger(sourceCon,destCon);
		merger.setSourceUsername(sourceUsername);
		merger.setDestinationUsername(destinationUsername);
		merger.merge(schema, tables);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(destCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sourceConnections.put(sourceCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void mergeTable(String schema ,String name) throws SQLException, IOException
	{
		Connection sourceCon=null;
		Connection destCon=null;
		try
		{
			sourceCon=sourceConnections.take();
			destCon=destinationConnections.take();
			
		TableMerger merger=new TableMerger(sourceCon,destCon);
		merger.setSourceUsername(sourceUsername);
		merger.setDestinationUsername(destinationUsername);
		List<String> list= new ArrayList<String>();
		list.add(name);
		merger.merge(schema,list );
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(destCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sourceConnections.put(sourceCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void mergeView(String schema ,List<String> views) throws SQLException, IOException
	{
		Connection sourceCon=null;
		Connection destCon=null;
		try
		{
			sourceCon=sourceConnections.take();
			destCon=destinationConnections.take();
		
		ViewMerger merger=new ViewMerger(sourceCon,destCon);
		merger.setSourceUsername(sourceUsername);
		merger.setDestinationUsername(destinationUsername);
		merger.merge(schema, views);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(destCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sourceConnections.put(sourceCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void mergeFiles(String schema) throws IOException
	{
		int count=this.count.get();
		
		File baseFile=new File(schema+".SQL");
		 OutputStream out =null;

		for(int i=0;i<count;i++)
		{
			File file=new File(schema+".SQL"+"."+i);
			Logger.log("Files merger"+file.getAbsolutePath());
			
			if(file.exists() && file.length()>0 )
			{
				 out = new FileOutputStream(baseFile,true);
				InputStream in = new FileInputStream(file);
				
				 byte[] buf = new byte[1024];
		         int len;
		         while ((len = in.read(buf)) > 0){
		           out.write(buf, 0, len);
		         }
		         
		         in.close();
		         out.flush();
		         out.close();
				
			}
			Logger.log("File deleted"+file.delete());
		}
		this.count.set(0);
		
		
	}
	
	public void mergeView(String schema ,String name) throws SQLException, IOException
	{
	
		Connection sourceCon=null;
		Connection destCon=null;
		try
		{
			sourceCon=sourceConnections.take();
			destCon=destinationConnections.take();
			
		ViewMerger merger=new ViewMerger(sourceCon,destCon);
		List<String> list= new ArrayList<String>();
		list.add(name);
		merger.merge(schema, list);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(destCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sourceConnections.put(sourceCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void mergeProcedures(String schema ,List<String> procedures) throws SQLException, IOException
	{
		Connection sourceCon=null;
		Connection destCon=null;
		try
		{
			sourceCon=sourceConnections.take();
			destCon=destinationConnections.take();
			
		ProcedureMerger merger=new ProcedureMerger(sourceCon,destCon);
		merger.merge(schema, procedures);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(destCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sourceConnections.put(sourceCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void mergeProcedure(String schema ,String name) throws SQLException, IOException
	{
		Connection sourceCon=null;
		Connection destCon=null;
		try
		{
			sourceCon=sourceConnections.take();
			destCon=destinationConnections.take();
		ProcedureMerger merger=new ProcedureMerger(sourceCon,destCon);
		List<String> list= new ArrayList<String>();
		list.add(name);
		merger.merge(schema, list);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				destinationConnections.put(destCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sourceConnections.put(sourceCon);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	public void shutdown() throws SQLException
	{
		if(null!=destinationConnection)
		{
			if(!destinationConnection.isClosed())
			{
			//destinationConnection.commit();
			
			destinationConnection.close();
			}
		}
		
		if(null!=sourceConnection)
		{
			if(!sourceConnection.isClosed())
			{
			//sourceConnection.commit();
			sourceConnection.close();
			}
		}
		
		for(int i=0;i<destinationConnections.size();i++)
    	{
			try {
				destinationConnections.take().close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		
		for(int i=0;i<sourceConnections.size();i++)
    	{
			try {
				sourceConnections.take().close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		
		DBMerger merger=new DBMerger();
		merger.setDestinationUrl("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=orac5n2-vip.oasis.dev.wdh.intuit.com)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=orac5n2-vip.oasis.dev.wdh.intuit.com)(PORT=1521)))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=oasisdev1_b)))");
		merger.setDestinationUsername("OASIS_APP");
		merger.setDestinationPassword("m44ptnn4_oa");
		
		merger.setSourceUrl("jdbc:oracle:thin:@oracle:1522:OASIS1");
		merger.setSourceUsername("oasis_owner");
		merger.setSourcePassword("o3ngin33r");
		
		merger.init();
		
		//merger.read();
		//merger.getSchemas();
		//merger.getTables(schema);
		List<String> table=new ArrayList<String>();
		//table.add("MERCHANTBANK_ENC".toUpperCase());
		//table.add("merchantpcipricing".toUpperCase());
		//table.add("address_vw".toUpperCase());
		table.add("import003authorizations".toUpperCase());
		merger.mergeProcedures("OASIS_OWNER", table);
		//merger.mergeTable("OASIS_OWNER", table);
		//merger.mergeView("OASIS_OWNER", table);
		merger.shutdown();
	}

	public Connection getDestinationConnection() {
		return destinationConnection;
	}

	public void setDestinationConnection(Connection destinationConnection) {
		this.destinationConnection = destinationConnection;
	}

	public String getDestinationPassword() {
		return destinationPassword;
	}

	public void setDestinationPassword(String destinationPassword) {
		this.destinationPassword = destinationPassword;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public void setDestinationUrl(String destinationUrl) {
		this.destinationUrl = destinationUrl;
	}

	public String getDestinationUsername() {
		return destinationUsername;
	}

	public void setDestinationUsername(String destinationUsername) {
		this.destinationUsername = destinationUsername;
	}

	public Connection getSourceConnection() {
		return sourceConnection;
	}

	public void setSourceConnection(Connection sourceConnection) {
		this.sourceConnection = sourceConnection;
	}

	public String getSourcePassword() {
		return sourcePassword;
	}

	public void setSourcePassword(String sourcePassword) {
		this.sourcePassword = sourcePassword;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceUsername() {
		return sourceUsername;
	}

	public void setSourceUsername(String sourceUsername) {
		this.sourceUsername = sourceUsername;
	}
	
}
