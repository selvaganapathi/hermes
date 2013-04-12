package org.hermes.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class IndexDDLCreator extends DDLCreator {

	private String schema;
	private String table;
	private boolean ignoreTableSpace=true;
	
	public void createDDL() throws SQLException, IOException {
		TableReader sourceReader=new TableReader(sourceConnection);
		TableReader destionationReader=new TableReader(destinationConnection);
		List<Index> sourceIndexes=sourceReader.getIndexes(schema, table);
		List<Index> destinationIndexes=destionationReader.getIndexes(schema, table);
		
		for(Index destIndex:destinationIndexes)
		{
			boolean found=false;
			boolean drop=false;
			
			if(destIndex.getName().startsWith("SYS"))
			{
				continue;
			}
			for(Index sourceIndex:sourceIndexes)
			{
				if(destIndex.getName().equalsIgnoreCase(sourceIndex.getName()))
				{
					if(!destIndex.getColumns().containsAll(sourceIndex.getColumns()))
					{
					drop=true;
					}
					found=true;
					break;
				}
				else if(destIndex.getColumns().size()==sourceIndex.getColumns().size() &&
						destIndex.getColumns().containsAll(sourceIndex.getColumns()))
				{
					found=true;
					break;
				}
			}
			
			if(drop)
			{
				dropDDL(destIndex.getName());
				createIndexDDL(destIndex);
			}
			else if(!found)
			{
				createIndexDDL(destIndex);
			}
			
		}
		
	}
	
	private void dropDDL(String index) throws IOException
	{
		String ddl=Templates.DROP_INDEX_TEMPLATE.replaceAll(Templates.INDEX_NAME, index);
		write(ddl);
	}
	
	private void createIndexDDL(Index index) throws IOException
	{
		String ddl=null;
		
		if(index.getType().startsWith("DOMAIN"))
		{
			 ddl=Templates.CREATE_INDEX_DOMAIN_TEMPLATE.replace(Templates.INDEX_NAME, index.getName());
			 ddl= ddl.replace(Templates.INDEX_TYPE, index.getSpecialType());
			 ddl= ddl.replace(Templates.PARAMETERS, index.getParameters());
		}
		else
		{
		 ddl=Templates.CREATE_INDEX_TEMPLATE.replace(Templates.INDEX_NAME, index.getName());
		 if(null!=index.getTableSpace())
				ddl=ddl.replace(Templates.TABLE_SPACE, Templates.CREATE_INDEX_TABLE_SPACE_TEMPLATE.replace(Templates.TABLE_SPACE, index.getTableSpace()));
			else
			ddl=ddl.replace(Templates.TABLE_SPACE,"");
		 ddl=ddl.replace(Templates.NEXT_EXTENT, Utils.getAsString(index.getNextExtend()));
		}
		ddl=ddl.replace(Templates.TABLE_NAME, table);
		List<String> columns=index.getColumns();
		StringBuilder builder=new StringBuilder();
		int size=columns.size();
		for(int i=0;i<size;i++)
		{
			builder.append(columns.get(i));
			if(i!=size-1)
			builder.append(',');
			builder.append("\r\n");
		}
		ddl=ddl.replace(Templates.COLUMNS, builder.toString());
		write(ddl);
	}

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

	
}
