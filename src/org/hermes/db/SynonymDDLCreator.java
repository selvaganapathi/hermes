package org.hermes.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SynonymDDLCreator extends DDLCreator {

	private String schema;
	private String table;
	
	
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
	public void createDDL() throws SQLException, IOException {

		TableReader destionationReader=new TableReader(destinationConnection);
		TableReader sourceReader=new TableReader(sourceConnection);
		List<Synonym> sourceSynonyms= sourceReader.getSynonyms(schema, table);
		List<Synonym> destSynonyms= destionationReader.getSynonyms(schema, table);
		
		for(Synonym destSynonym:destSynonyms)
		{
			boolean found=false;
			for(Synonym sourceSynonym:sourceSynonyms)
			{
				if(sourceSynonym.getName().equalsIgnoreCase(destSynonym.getName()) )
				{
					found=true;
					 break;
						 
				}
			}
			
			if(!found)
			synonymDDL( destSynonym);
		}
		
	}
	private void synonymDDL(Synonym synonym) throws IOException
	{
		 String ddl=Templates.SYNONYM_TEMPLATE.replace(Templates.TABLE_NAME, synonym.getTable());
		 ddl=ddl.replace(Templates.RELATED_OWNER, synonym.getTableSchema());
		 ddl=ddl.replace(Templates.RELATED_TABLE_NAME, synonym.getTable());
		 ddl=ddl.replaceAll(Templates.OWNER,  synonym.getSchema());
		
		 write(ddl);
	}

}
