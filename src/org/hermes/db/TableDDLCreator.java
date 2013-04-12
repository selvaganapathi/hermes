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


public class TableDDLCreator extends DDLCreator {

	private String schema;
	private List<String> tables;
	private boolean ignoreTableSpace=true;
	
	private IndexDDLCreator getIndexDDLCreator (String table)
	{
		IndexDDLCreator creator=new IndexDDLCreator();
		creator.setSchema(schema);
		creator.setTable(table);
		creator.setSourceConnection(getSourceConnection());
		creator.setDestinationConnection(getDestinationConnection());
		creator.setWriter(getWriter());
		
		return creator;
		
	}
	
	
	
	public void createDDL() throws SQLException, IOException {
		
		 TableReader sourceReader=new TableReader(sourceConnection);
		 TableReader destionationReader=new TableReader(destinationConnection);
		 HashMap<String,Table> sourceTables=new  HashMap<String,Table>();
		 HashMap<String,Table> destionationTables=new  HashMap<String,Table>();
		 
		for(String tab:tables)
		{
			 Table table=sourceReader.getTable(schema, tab);
			 if(null!=table)
			 {
			 List<Column> columns=sourceReader.getColumns(schema, tab);
			 table.setColumns(columns);
			 sourceTables.put(tab,table);
			 }
			
		}
		
		for(String tab:tables)
		{
			Table table=destionationReader.getTable(schema, tab);
			if(null!=table)
			 {
			 List<Column> columns=destionationReader.getColumns(schema, tab);
			 table.setColumns(columns);
			 destionationTables.put(tab,table);
			 }
		}
		Iterator<Entry<String, Table>> it= destionationTables.entrySet().iterator();
		
		while(it.hasNext())
		{
			Table destinationTable=it.next().getValue();
			
			Table sourceTable=sourceTables.get(destinationTable.getName());
			
			if(null!=sourceTable)
			{
				write(alterTableDDL(sourceTable, destinationTable));
				
				
			}
			else
			{
				write(CreateTableDDL(destinationTable));
			}
			
			IndexDDLCreator indexCreator=getIndexDDLCreator(destinationTable.getName());
			indexCreator.createDDL();
			
			ConstrainDDLCreator constraintCreator= getConstrainDDLCreator(destinationTable.getName());
			constraintCreator.createDDL();
			
			TriggerDDLCreator triggerDDLCreator= getTriggerDDLCreator(destinationTable.getName());
			triggerDDLCreator.createDDL();
			
			GrantDDLCreator grantDDLCreator=getGrantDDLCreator(destinationTable.getName());
			grantDDLCreator.createDDL();
			
			SynonymDDLCreator synonymDDLCreator=getSynonymDDLCreator(destinationTable.getName());
			synonymDDLCreator.createDDL();
			
		
			
		}
		
		getWriter().close();
		
		
		
	}

	private ConstrainDDLCreator getConstrainDDLCreator (String table)
	{
		ConstrainDDLCreator creator=new ConstrainDDLCreator();
		creator.setSchema(schema);
		creator.setTable(table);
		creator.setSourceConnection(getSourceConnection());
		creator.setDestinationConnection(getDestinationConnection());
		creator.setWriter(getWriter());
		
		return creator;
		
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
	
	private TriggerDDLCreator getTriggerDDLCreator (String table)
	{
		TriggerDDLCreator creator=new TriggerDDLCreator();
		creator.setSchema(schema);
		creator.setTable(table);
		creator.setSourceConnection(getSourceConnection());
		creator.setDestinationConnection(getDestinationConnection());
		creator.setWriter(getWriter());
		
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
	
	
	
	
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<String> getTables() {
		return tables;
	}

	public void setTables(List<String> tables) {
		this.tables = tables;
	}
	
	private String CreateTableDDL(Table destinationTable)
	{
		String ddl=Templates.CREATE_TABLE_TEMPLATE.replace(Templates.TABLE_NAME, destinationTable.getName());
		ddl=ddl.replace(Templates.NEXT_EXTENT, Utils.getAsString(destinationTable.getNextExtent()));
		if(ignoreTableSpace)
		ddl=ddl.replace(Templates.TABLE_SPACE, "");
		else
		{
			String table_space_ddl=Templates.TABLE_SPACE_DEFINATION.replace(Templates.TABLE_SPACE_NAME, destinationTable.getTableSpace());
			ddl=ddl.replace(Templates.TABLE_SPACE, table_space_ddl);
		}
		
		List<Column> cols=destinationTable.getColumns();
		StringBuilder builder=new StringBuilder();
		int size=cols.size();
		for(int i=0;i<size;i++)
		{
			Column column=cols.get(i);
			String columnDDL=Templates.COLUMN_DEFINATION.replace(Templates.COLUMN_NAME, column.getName());
			
			if(column.getType().equalsIgnoreCase("NUMBER"))
			{
				if(column.getPrecision()==0 && column.getDataScale()==0)
				{
					columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE,"INTEGER");
					columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH,"");
				}
				else
				{
				columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE, column.getType());
				columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH, "("+column.getPrecision()+","+column.getDataScale()+")");
				}
			}
			else if(column.getType().equalsIgnoreCase("DATE"))
			{
				 columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH, "");
				 columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE, column.getType());
			}
			else
			{
			    columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH, "("+column.getDataLength()+")");
			    columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE, column.getType());
			}
			columnDDL=columnDDL.replaceAll(Templates.NULL_VALUE, column.isNullable()?Templates.NULL:Templates.NOT_NULL);
			builder.append(columnDDL);
			if(i!=size-1)
			builder.append(',');
			builder.append("\r\n");
		}
		ddl=ddl.replace(Templates.COLUMNS,""+ builder.toString());
		return ddl;
	}
	
	private String alterTableDDL(Table sourceTable,Table destinationTable)
	{
		List<Column> sourceColumns=sourceTable.getColumns();
		List<Column> detinationColumns=destinationTable.getColumns();
		StringBuilder builder=new StringBuilder();
		builder.append(Templates.PROMPT_ALTER_TABLE_ADD_COLUMN_TEMPLATE.replaceAll(Templates.TABLE_NAME, destinationTable.getName()));
		for(Column destColumn:detinationColumns)
		{
			boolean present=false;
			for(Column sourceColumn:sourceColumns)
			{
				if(sourceColumn.getName().equalsIgnoreCase(destColumn.getName()))
				{
					if(!destColumn.getType().equalsIgnoreCase(sourceColumn.getType()))
					{
						String ddl=Templates.MODIFY_TABLE_ADD_COLUMN_TEMPLATE.replaceAll(Templates.TABLE_NAME, destinationTable.getName());
						String columnDDL=Templates.COLUMN_DEFINATION.replaceAll(Templates.COLUMN_NAME, destColumn.getName());
					
						if(destColumn.getType().equalsIgnoreCase("NUMBER"))
						{
							if(destColumn.getPrecision()==0 && destColumn.getDataScale()==0)
							{
								columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE,"INTEGER");
								columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH,"");
							}
							else
							{
							columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE, destColumn.getType());
							columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH, "("+destColumn.getPrecision()+","+destColumn.getDataScale()+")");
							}
						}
						else if(destColumn.getType().equalsIgnoreCase("DATE"))
						{
							 columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH, "");
							 columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE, destColumn.getType());
						}
						else
						{
						    columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH, "("+destColumn.getDataLength()+")");
							columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE, destColumn.getType());
						}
						columnDDL=columnDDL.replaceAll(Templates.NULL_VALUE, destColumn.isNullable()?Templates.NULL:Templates.NOT_NULL);
						ddl=ddl.replaceAll(Templates.COLUMN,columnDDL);
						builder.append(ddl);
						builder.append("\r\n");
					}
					
					present=true;
					break;
				}
				
			}
			
			if(!present)
			{
				String ddl=Templates.ALTER_TABLE_ADD_COLUMN_TEMPLATE.replaceAll(Templates.TABLE_NAME, destinationTable.getName());
				String columnDDL=Templates.COLUMN_DEFINATION.replaceAll(Templates.COLUMN_NAME, destColumn.getName());
				
				if(destColumn.getType().equalsIgnoreCase("NUMBER"))
				{
					if(destColumn.getPrecision()==0 && destColumn.getDataScale()==0)
					{
						columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE,"INTEGER");
						columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH,"");
					}
					else
					{
					columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE, destColumn.getType());
					columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH, "("+destColumn.getPrecision()+","+destColumn.getDataScale()+")");
					}
				}
				else if(destColumn.getType().equalsIgnoreCase("DATE"))
				{
					 columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH, "");
					 columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE, destColumn.getType());
				}
				else
				{
				    columnDDL=columnDDL.replaceAll(Templates.DATA_LENGTH, "("+destColumn.getDataLength()+")");
					columnDDL=columnDDL.replaceAll(Templates.DATA_TYPE, destColumn.getType());
				}
				columnDDL=columnDDL.replaceAll(Templates.NULL_VALUE, destColumn.isNullable()?Templates.NULL:Templates.NOT_NULL);
				ddl=ddl.replaceAll(Templates.COLUMN,columnDDL);
				builder.append(ddl);
				builder.append("\r\n");
				
			}
		}
		return builder.toString();
		
	}
	
	

	public boolean isIgnoreTableSpace() {
		return ignoreTableSpace;
	}

	public void setIgnoreTableSpace(boolean ignoreTableSpace) {
		this.ignoreTableSpace = ignoreTableSpace;
	}

}
