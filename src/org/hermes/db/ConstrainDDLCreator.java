package org.hermes.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ConstrainDDLCreator  extends DDLCreator {

	private String schema;
	private String table;
	
	public void createDDL() throws SQLException, IOException {
		
		TableReader sourceReader=new TableReader(sourceConnection);
		TableReader destionationReader=new TableReader(destinationConnection);
		List<Constraint> sourceConstraints=sourceReader.getConstraints(schema, table);
		List<Constraint> destinationConstraints=destionationReader.getConstraints(schema, table);
		
		for(Constraint destConstraint:destinationConstraints)
		{
			boolean found=false;
			boolean drop=false;
			
			if(destConstraint.getType().equalsIgnoreCase("C"))
				continue;
			
			if(destConstraint.getType().equalsIgnoreCase("P") && destConstraint.getOwner().equalsIgnoreCase(schema) 
					&& table.equalsIgnoreCase(destConstraint.getTable()))
			{
				
				for(Constraint sourceConstraint:sourceConstraints)
				{
					if(sourceConstraint.getType().equalsIgnoreCase("P")   && sourceConstraint.getName().equalsIgnoreCase(destConstraint.getName())
							&& !sourceConstraint.getColumns().containsAll(destConstraint.getColumns()) )
					{
						drop=true;
						break;
					}
				}
				if(drop)
				{
					dropConstraint(destConstraint.getName() );
					createPrimaryConstraint(((PrimaryKey)destConstraint));
				}
				drop=false;
			}
			else if(destConstraint.getType().equalsIgnoreCase("U"))
			{
				boolean matched=false;
				for(Constraint sourceConstraint:sourceConstraints)
				{
					if(sourceConstraint.getType().equalsIgnoreCase("U")  &&
							sourceConstraint.getName().equalsIgnoreCase(destConstraint.getName())	)
					{
						if(!sourceConstraint.getColumns().containsAll(destConstraint.getColumns()) )
						{
							drop=true;
						}
						else
						{
							matched=true;
						}
						break;
					}
					
				}
				if(drop)
				{
					dropConstraint(destConstraint.getName() );
					createUniqueConstraint(destConstraint);
				}
				else if(!matched)
				{
					createUniqueConstraint(destConstraint);
				}
			}
			else if(destConstraint.getType().equalsIgnoreCase("R"))
			{
				boolean matched=false;
				for(Constraint sourceConstraint:sourceConstraints)
				{
					if(sourceConstraint.getType().equalsIgnoreCase("R")  &&
							sourceConstraint.getName().equalsIgnoreCase(destConstraint.getName())	)
					{
						if(!sourceConstraint.getColumns().containsAll(destConstraint.getColumns()) )
						{
							drop=true;
						}
						else
						{
							matched=true;
						}
						break;
					}
					
				}
				if(drop)
				{
					dropConstraint(destConstraint.getName() );
					createForeignConstraint(destConstraint,destinationConstraints);
				}
				else if(!matched)
				{
					createForeignConstraint(destConstraint,destinationConstraints);
				}
			}
			
			
			
		}
		
		
	}
	
	private void dropConstraint(String constraint ) throws IOException
	{
		String ddl=Templates.DROP_CONSTRAINT_TEMPLATE.replaceAll(Templates.CONSTRAINT_NAME, constraint);
		 ddl=ddl.replace(Templates.TABLE_NAME, table);
		 write(ddl);
	}
	private void createUniqueConstraint(Constraint constraint ) throws IOException
	{
		String ddl=Templates.CREATE_UNIQUE_CONSTRAINT_TEMPLATE.replaceAll(Templates.CONSTRAINT_NAME, constraint.getName());
		 ddl=ddl.replace(Templates.TABLE_NAME, table);
		 List<String> columns=constraint.getColumns();
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
	
	private void createForeignConstraint(Constraint constraint,List<Constraint> destinationConstraints ) throws IOException
	{
		String ddl=Templates.CREATE_FOREIGN_CONSTRAINT_TEMPLATE.replaceAll(Templates.CONSTRAINT_NAME, constraint.getName());
		 ddl=ddl.replace(Templates.TABLE_NAME, table);
		 List<String> columns=constraint.getColumns();
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
			
			String r_name=constraint.getRName();
			
			for(Constraint destConstraint:destinationConstraints)
			{
				if(r_name.equalsIgnoreCase(destConstraint.getName()))
				{
					ddl=ddl.replace(Templates.RELATED_OWNER, destConstraint.getOwner());
					ddl=ddl.replace(Templates.RELATED_TABLE_NAME, destConstraint.getTable());
					
					StringBuilder builder2=new StringBuilder();
					List<String> r_columns=destConstraint.getColumns();
					int r_size=r_columns.size();
					for(int i=0;i<r_size;i++)
					{
						builder2.append(r_columns.get(i));
						if(i!=size-1)
							builder2.append(',');
						builder2.append("\r\n");
					}
					ddl=ddl.replace(Templates.RELATED_COLUMNS, builder2.toString());
					break;
				}
			}
			
		   write(ddl);
	}
	
	private void createPrimaryConstraint(PrimaryKey constraint ) throws IOException
	{
		String ddl=Templates.CREATE_PRIMARY_CONSTRAINT_TEMPLATE.replaceAll(Templates.TABLE_NAME, table);
		 ddl=ddl.replace(Templates.TABLE_NAME, table);
		 List<String> columns=constraint.getColumns();
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
			ddl=ddl.replace(Templates.NEXT_EXTENT, Utils.getAsString(constraint.getNextExtend()));
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
