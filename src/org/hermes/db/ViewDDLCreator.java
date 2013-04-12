package org.hermes.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.hermes.db.util.Logger;


public class ViewDDLCreator extends DDLCreator {

	private String schema;
	private List<String> views;
	
	public void createDDL() throws SQLException, IOException {
		TableReader sourceReader=new TableReader(sourceConnection);
		 TableReader destionationReader=new TableReader(destinationConnection);
		 HashMap<String,View> sourceTables=new  HashMap<String,View>();
		 HashMap<String,View> destionationTables=new  HashMap<String,View>();
		 
		for(String tab:views)
		{
			View table=sourceReader.getView(schema, tab);
			 if(null!=table)
			 {
			 sourceTables.put(tab,table);
			 }
			
		}
		
		for(String tab:views)
		{
			View table=destionationReader.getView(schema, tab);
			if(null!=table)
			 {
			  destionationTables.put(tab,table);
			}
		}
		Iterator<Entry<String, View>> it= destionationTables.entrySet().iterator();
		
		while(it.hasNext())
		{
			View destinationTable=it.next().getValue();
			
			View sourceTable=sourceTables.get(destinationTable.getName());
			boolean modified=true;
			if(null!=sourceTable)
			{
				modified=isChanged(sourceTable, destinationTable);
				
			}
			if(modified)
			{
			write(CreateViewDDL(destinationTable));
			
			GrantDDLCreator grantDDLCreator=getGrantDDLCreator(destinationTable.getName());
			grantDDLCreator.createDDL();
			
			SynonymDDLCreator synonymDDLCreator=getSynonymDDLCreator(destinationTable.getName());
			synonymDDLCreator.createDDL();
			
			}
			
		}
		
	}
	
	private SynonymDDLCreator getSynonymDDLCreator (String table) throws IOException
	{
		SynonymDDLCreator creator=new SynonymDDLCreator();
		creator.setSchema(schema);
		creator.setTable(table);
		creator.setSourceConnection(getSourceConnection());
		creator.setDestinationConnection(getDestinationConnection());
		
		File file=new File("COMMON.SQL");
		FileWriter writer=new FileWriter(file,true);
		Logger.log("File path: "+file.getAbsolutePath());
		
		creator.setWriter(writer);
		
		return creator;
		
	}
	
	
	
	private GrantDDLCreator getGrantDDLCreator (String table)
	{
		GrantDDLCreator creator=new GrantDDLCreator();
		creator.setSchema(schema);
		creator.setTable(table);
		creator.setSourceConnection(getSourceConnection());
		creator.setDestinationConnection(getDestinationConnection());
		creator.setWriter(getWriter());
		
		return creator;
		
	}
	
	private String CreateViewDDL(View destinationTable)
	{
		
		String ddl=Templates.CREATE_VIEW_TEMPLATE.replaceAll(Templates.VIEW_NAME, destinationTable.getName());
		
		List<Column> cols=destinationTable.getColumns();
		StringBuilder builder=new StringBuilder();
		int size=cols.size();
		for(int i=0;i<size;i++)
		{
			Column column=cols.get(i);
			
			builder.append(column.getName());
			if(i!=size-1)
			builder.append(',');
			builder.append("\r\n");
		}
		ddl=ddl.replace(Templates.COLUMNS, builder.toString());
		
		ddl=ddl.replace(Templates.SELECT_CRITERIA, destinationTable.getText());
		
		return ddl;
	}
	
	private boolean isChanged(View sourceTable, View destinationTable )
	{
		boolean changed=false;
		
		List<Column> sourceCols=sourceTable.getColumns();
		List<Column> destinationCols=destinationTable.getColumns();
		
		for(Column destCol:destinationCols)
		{
			boolean found=false;
			for(Column sourcecol:sourceCols)
			{
				if(destCol.getName().equalsIgnoreCase(sourcecol.getName()))
				{
				found=true;	
				break;
				}
			}
			
			if(!found)
			{
				changed=true;
				break;
			}
		}
		Logger.log("changed :"+changed);
		if(!sourceTable.getText().equalsIgnoreCase(destinationTable.getText()))
			changed=true;
		
		return changed;
		
		
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<String> getViews() {
		return views;
	}

	public void setViews(List<String> views) {
		this.views = views;
	}
}
