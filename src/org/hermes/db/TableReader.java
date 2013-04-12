package org.hermes.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hermes.db.util.Logger;

import oracle.jdbc.OracleDatabaseMetaData;


public class TableReader {

	private Connection connection;

	private List<String> catalogs;

	private List<Schema> schemas;

	private Map<Short, String> types;
	
	private static List<String> users;
	
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public TableReader(Connection connection) {
		this.connection = connection;
		
		schemas=null;
		users=null;
	}

	public void getDetails() throws SQLException {
		OracleDatabaseMetaData dbmd = (OracleDatabaseMetaData) connection
				.getMetaData();
		// catalogs(dbmd);
		// types(dbmd);
		schemas(dbmd);

		String[] types = { "TABLE" };
		// ResultSet resultSet = dbmd.getTables(null, null, "%", types);

		/*
		 * while (resultSet.next()) { String tableName = resultSet.getString(3);
		 * 
		 * String tableCatalog = resultSet.getString(1); String tableSchema =
		 * resultSet.getString(2);
		 *  // Logger.log(tableName);
		 *  // Logger.log(tableCatalog);
		 *  // Logger.log(tableSchema); }
		 */
	}

	private void types(OracleDatabaseMetaData dbmd) throws SQLException {

		ResultSet resultSet = dbmd.getTypeInfo();
		types = new HashMap<Short, String>();
		Logger.log("------------");
		Logger.log("Types");
		while (resultSet.next()) {
			String name = resultSet.getString(1);
			short type = resultSet.getShort(2);
			types.put(type, name);
		}
		resultSet.close();

		Logger.log("------------");
	}

	

	public List<Schema> getSchemas() throws SQLException {
		OracleDatabaseMetaData dbmd = (OracleDatabaseMetaData) connection
				.getMetaData();
		ResultSet resultSet = dbmd.getSchemas();
		schemas = new ArrayList<Schema>();
		Logger.log("------------");
		Logger.log("Schemas");
		while (resultSet.next()) {
			String name = resultSet.getString(1);
			Schema schema = new Schema();
			schema.setName(name);
			schemas.add(schema);

		}
		resultSet.close();

		Logger.log("------------");

		return schemas;
	}

	
	
	private void schemas(OracleDatabaseMetaData dbmd) throws SQLException {

		ResultSet resultSet = dbmd.getSchemas();
		ArrayList<Schema> schemas = new ArrayList<Schema>();
		Logger.log("------------");
		Logger.log("Schemas");
		while (resultSet.next()) {
			String name = resultSet.getString(1);
			Schema schema = new Schema();
			schema.setName(name);
			schemas.add(schema);

		}
		resultSet.close();
		for (Schema schema : schemas) {
			Logger.log(schema.getName());
			tables(schema, dbmd);
			// views(schema,dbmd);
			// alias(schema,dbmd);
			// synonym(schema,dbmd);
		}
		Logger.log("------------");
	}
	
	public  List<String> getUsers()
	throws SQLException {

			if(null==users)
			{
				users = new ArrayList<String>();
			}
			else
				return users;
			
			PreparedStatement getTables = connection
					.prepareStatement("SELECT USERNAME FROM          sys.all_users");
			
			ResultSet resultSet = getTables.executeQuery();
			//
			Logger.log("------------");
			Logger.log("users");
			
		
			while (resultSet.next()) {
				String name = resultSet.getString("USERNAME");
				
				users.add(name);
				Logger.log(name);
			}
			getTables.close();
			resultSet.close();
			// con.close();
			Logger.log("------------");
			
			return users;
			
	}

	public  List<Table> getTables(String schema)
	throws SQLException {

			
			PreparedStatement getTables = connection
					.prepareStatement("SELECT /*+RULE*/ owner,table_name,Decode(iot_type, NULL,(Decode(TEMPORARY, 'Y', 'TEMPORARY', 'REGULAR')), 'INDEX') table_type,"
							+ "tablespace_name,initial_extent,next_extent,pct_increase,max_extents,pct_free,pct_used,cluster_name FROM sys.all_tables t "
							+ "WHERE owner = ? AND iot_name IS NULL");
			getTables.setString(1, schema);
			ResultSet resultSet = getTables.executeQuery();
			//
			Logger.log("------------");
			Logger.log("Tables");
			List<Table> tables = new ArrayList<Table>();
			while (resultSet.next()) {
				String name = resultSet.getString("table_name");
				Table tab = new Table();
				tab.setName(name);
				tab.setSchema(schema);
				tab.setTableSpace(resultSet.getString("tablespace_name"));
				tab.setInitialExtent(resultSet.getInt("initial_extent"));
				tab.setNextExtent(resultSet.getInt("next_extent"));
				tables.add(tab);
				Logger.log(name);
			}
			getTables.close();
			resultSet.close();
			// con.close();
			Logger.log("------------");
			
			return tables;
			
	}
	
	public  List<Index> getIndexes(String schema,String table)
	throws SQLException {

			
			PreparedStatement getTables = connection
					.prepareStatement("SELECT /*+RULE*/ 1,owner,index_name,index_type,table_owner,table_name,table_type,uniqueness,tablespace_name,initial_extent,next_extent,\r\n" + 
							"   min_extents,max_extents,pct_increase,Nvl(freelists, 1),Nvl(freelist_groups, 1),pct_free,ini_trans,max_trans,temporary,generated,logging,\r\n" + 
							"   compression,prefix_length,\r\n" + 
							"   ityp_owner,ityp_name,parameters,partitioned,buffer_pool,degree,instances FROM sys.all_indexes WHERE table_owner =? AND \r\n" + 
							"   table_name = ?");
			getTables.setString(1, schema);
			getTables.setString(2, table);
			ResultSet resultSet = getTables.executeQuery();
			//
			Logger.log("------------");
			Logger.log("Indexes");
			List<Index> indexes = new ArrayList<Index>();
			HashMap<String,Index> map=new HashMap<String, Index>();
			
			while (resultSet.next()) {
				String name = resultSet.getString("index_name");
				Index index=new Index();
				index.setName(name);
				index.setType(resultSet.getString("index_type"));
				index.setTable(table);
				index.setSchema(schema);
				index.setTableSpace(resultSet.getString("tablespace_name"));
				index.setInitialExtend(resultSet.getInt("initial_extent"));
				index.setNextExtend(resultSet.getInt("next_extent"));
				index.setParameters(resultSet.getString("parameters"));
				index.setSpecialType(resultSet.getString("ityp_owner")+"."+resultSet.getString("ityp_name"));
				indexes.add(index);
				Logger.log(index.getName());
				map.put(index.getName(), index);
			}
			getTables.close();
			resultSet.close();
			
			PreparedStatement getColumns = connection
			.prepareStatement(" SELECT v2.index_owner,v2.index_name,v2.table_owner,v2.table_name,v2.column_name,\r\n" + 
					"    v2.column_position FROM sys.all_indexes v1, sys.all_ind_columns v2 WHERE\r\n" + 
					"     v2.index_owner = v1.owner AND v2.index_name = v1.index_name AND\r\n" + 
					"    v1.table_owner = ? AND v1.table_name = ? ORDER BY index_name,column_position  \r\n"  );
			getColumns.setString(1, schema);
			getColumns.setString(2, table);
			ResultSet resultSet2 = getColumns.executeQuery();
			
			while (resultSet2.next()) {
				map.get(resultSet2.getString("index_name")).addCloumn(resultSet2.getString("column_name"));
			}
			getColumns.close();
			resultSet2.close();
			
			for(Index index:indexes)
			{
				if(index.getType().startsWith("FUNCTION"))
				{
					PreparedStatement getFunctions = connection
					.prepareStatement(" SELECT column_position, column_expression FROM sys.all_ind_expressions WHERE \r\n" + 
							"    index_owner = ? AND index_name = ?"  );
					getFunctions.setString(1, schema);
					getFunctions.setString(2, index.getName());
					ResultSet resultSet3 = getFunctions.executeQuery();
					
					while (resultSet3.next()) {
						map.get(index.getName()).getColumns().set(resultSet3.getInt("column_position")-1, resultSet3.getString("column_expression"));
					}
					getFunctions.close();
					resultSet3.close();
				}
			}
			
			
			
			// con.close();
			Logger.log("------------");
			
			return indexes;
			
	}
	
	public  List<Grant> getGrants(String schema,String table)
	throws SQLException, IOException {
		
		PreparedStatement getTables = connection
		.prepareStatement("SELECT /*+NO_MERGE RULE*/ grantee,table_schema,table_name,NULL,privilege,grantable FROM sys.all_tab_privs\r\n" + 
				"  WHERE table_schema = ? AND table_name = ? UNION ALL \r\n" + 
				"  SELECT /*+NO_MERGE RULE*/ grantee,table_schema,table_name,column_name,privilege,grantable FROM sys.all_col_privs \r\n" + 
				"  WHERE table_schema = ? AND table_name =  ? \r\n" + 
				"");
		getTables.setString(1, schema);
		getTables.setString(2, table);
		getTables.setString(3, schema);
		getTables.setString(4, table);
		Logger.log("------------");
		Logger.log("Grants");
		ResultSet resultSet = getTables.executeQuery();
		List<Grant> grants = new ArrayList<Grant>();
		Map<String,Grant> grantsMap = new HashMap<String,Grant>();
		while (resultSet.next()) {
			Grant grant=grantsMap.get(resultSet.getString("grantee"));
			if(null==grant)
			{
			 grant=new Grant();
			 grant.setGrantee(resultSet.getString("grantee"));
			 grant.setSchema(schema);
			 grant.setTable(table);
			 grantsMap.put(grant.getGrantee(), grant);
			 grants.add(grant);
			}
			
			grant.addPrivilege(resultSet.getString("privilege"));
			
		}
		
		getTables.close();
		resultSet.close();
		
		return grants;
	}
	
	public  List<Procedure> getProcedures(String schema)
	throws SQLException, IOException {
		
		PreparedStatement getTables = connection
		.prepareStatement("SELECT * FROM (  SELECT    owner,    object_name,    status,    created,    last_ddl_time, \r\n" + 
				"      Row_Number() OVER (PARTITION BY owner, object_name, object_type ORDER BY last_ddl_time DESC) rn  FROM sys.all_objects\r\n" + 
				"          WHERE owner = ?      AND object_type = 'PROCEDURE') \r\n" + 
				"          WHERE rn = 1");
		getTables.setString(1, schema);
		
		Logger.log("------------");
		Logger.log("Procedures");
		ResultSet resultSet = getTables.executeQuery();
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		while (resultSet.next()) {
			Procedure procedure=new Procedure();
			procedure.setName(resultSet.getString("object_name"));
			procedure.setSchema(schema);
			procedures.add(procedure);
		}
		
		getTables.close();
		resultSet.close();
		return procedures;
	}
	
	public  Procedure getProcedure(String schema,String name)
	throws SQLException, IOException {
		
		PreparedStatement getTables = connection
		.prepareStatement(" SELECT /*+RULE*/ line,text FROM sys.all_source WHERE \r\n" + 
				"          owner = ?  AND name = ? AND type = 'PROCEDURE' ORDER BY  line");
		getTables.setString(1, schema);
		getTables.setString(2, name);
		
		Logger.log("------------");
		Logger.log("Procedure");
		ResultSet resultSet = getTables.executeQuery();
		
		Procedure procedure=new Procedure();
		
		procedure.setName(name);
		procedure.setSchema(schema);
		
		while (resultSet.next()) {
			
			procedure.addLine(resultSet.getString("text"));
		}
		
		getTables.close();
		resultSet.close();
		
		return procedure;
	}
	public  List<Trigger> getTriggers(String schema,String table)
	throws SQLException, IOException {
		
		PreparedStatement getTables = connection
		.prepareStatement("SELECT /*+RULE*/ owner,trigger_name,table_owner,table_name,referencing_names,when_clause,description,trigger_body,status,action_type,\r\n" + 
				" base_object_type FROM sys.all_triggers WHERE table_owner =  ?  AND table_name = ? ");
		getTables.setString(1, schema);
		getTables.setString(2, table);
		Logger.log("------------");
		Logger.log("Triggers");
		ResultSet resultSet = getTables.executeQuery();
		List<Trigger> triggers = new ArrayList<Trigger>();
		while (resultSet.next()) {
			
			Trigger	trigger=new Trigger();
			InputStream input=resultSet.getBinaryStream("trigger_body");
			InputStreamReader reader=new InputStreamReader(input);
			BufferedReader br = new BufferedReader(reader);
			String read = br.readLine();
			StringBuilder builder=new StringBuilder();
			while(read != null) {
			   
				builder.append(read);
				builder.append("\r\n");
			    read =br.readLine();

			}
			trigger.setBody(builder.toString());
			trigger.setDescription(resultSet.getString("description"));
			trigger.setName(resultSet.getString("trigger_name"));
			trigger.setType(resultSet.getString("action_type"));
			trigger.setTable(resultSet.getString("table_name"));
			trigger.setReference(resultSet.getString("referencing_names"));
			trigger.setClause(resultSet.getString("when_clause"));
			Logger.log(trigger.getName());
			triggers.add(trigger);
			
		}
		getTables.close();
		resultSet.close();
		
		
		return triggers;
	}
	public  List<Constraint> getConstraints(String schema,String table)
	throws SQLException {

			
			PreparedStatement getTables = connection
					.prepareStatement("SELECT owner,table_name,constraint_type,constraint_name,r_owner,r_constraint_name,\r\n" + 
							"delete_rule,status,search_condition,generated,deferrable,deferred,validated,rely,view_related FROM sys.all_constraints \r\n" + 
							"WHERE owner = ? AND table_name = ? \r\n" + 
							"UNION ALL\r\n" + 
							"SELECT  v2.owner,v2.table_name,v2.constraint_type,v2.constraint_name,v2.r_owner,v2.r_constraint_name,v2.delete_rule,\r\n" + 
							"v2.status,v2.search_condition,v2.generated,v2.deferrable,v2.deferred,v2.validated,v2.rely,v2.view_related \r\n" + 
							"FROM sys.all_constraints v1, sys.all_constraints v2 WHERE v2.owner = v1.r_owner AND v2.constraint_name = v1.r_constraint_name AND\r\n" + 
							"v1.owner = ? AND v1.table_name =?");
			getTables.setString(1, schema);
			getTables.setString(2, table);
			getTables.setString(3, schema);
			getTables.setString(4, table);
			ResultSet resultSet = getTables.executeQuery();
			//
			Logger.log("------------");
			Logger.log("Constraints-"+table);
			List<Constraint> constraints = new ArrayList<Constraint>();
			HashMap<String,Constraint> map=new HashMap<String, Constraint>();
			
			
			
			while (resultSet.next()) {
				Constraint constraint=new Constraint();
				constraint.setCondition(resultSet.getString("search_condition"));
				String name = resultSet.getString("constraint_name");
				
				
				constraint.setOwner( resultSet.getString("owner"));
				constraint.setTable( resultSet.getString("table_name"));
				constraint.setName(name);
				constraint.setType(resultSet.getString("constraint_type"));
				
				constraint.setROwner(resultSet.getString("r_owner"));
				constraint.setRName(resultSet.getString("r_constraint_name"));
				Logger.log(constraint.getName());
				if(constraint.getType().equalsIgnoreCase("P"))
				{
					PrimaryKey key=new PrimaryKey();
					key.setName(name);
					key.setType(constraint.getType());
					key.setOwner( resultSet.getString("owner"));
					key.setTable( resultSet.getString("table_name"));
					constraints.add(key);
					map.put(constraint.getName(), key);
				}
				else
				{
				   constraints.add(constraint);
					map.put(constraint.getName(), constraint);
				}
			}
			getTables.close();
			resultSet.close();
			
			PreparedStatement getColumns = connection
			.prepareStatement("SELECT /*+RULE*/  v1.owner,v1.constraint_name,v1.column_name,v1.position FROM sys.all_cons_columns v1, sys.all_constraints v2 \r\n" + 
					"WHERE v1.owner = ? AND v1.table_name = ?  AND v1.owner = v2.owner AND\r\n" + 
					" v1.constraint_name = v2.constraint_name  \r\n" + 
					" UNION ALL\r\n" + 
					"SELECT /*+RULE*/ v3.owner,v3.constraint_name,v3.column_name,v3.position FROM sys.all_constraints v1, sys.all_constraints v2,\r\n" + 
					" sys.all_cons_columns v3 WHERE v2.owner = v1.r_owner AND v2.constraint_name = v1.r_constraint_name AND v3.owner = v2.owner \r\n" + 
					" AND v3.table_name = v2.table_name AND v3.constraint_name = v2.constraint_name AND \r\n" + 
					" v1.owner =  ? AND v1.table_name =  ? \r\n" + 
					"   ORDER BY constraint_name,position \r\n"  );
			getColumns.setString(1, schema);
			getColumns.setString(2, table);
			getColumns.setString(3, schema);
			getColumns.setString(4, table);
			ResultSet resultSet2 = getColumns.executeQuery();
			
			while (resultSet2.next()) {
				map.get(resultSet2.getString("constraint_name")).addCloumn(resultSet2.getString("column_name"));
			}
			getColumns.close();
			resultSet2.close();
			
			for(Constraint constraint:constraints)
			{
				if(constraint.getType().equalsIgnoreCase("P"))
				{
					PreparedStatement getFunctions = connection
					.prepareStatement(" SELECT /*+RULE*/ 1,owner,index_name,index_type,table_owner,table_name,table_type,uniqueness,tablespace_name,initial_extent,next_extent,\r\n" + 
							"			   min_extents,max_extents,pct_increase,Nvl(freelists, 1),Nvl(freelist_groups, 1),pct_free,ini_trans,max_trans,temporary,generated,logging,\r\n" + 
							"			   compression,prefix_length,\r\n" + 
							"			   ityp_owner,ityp_name,parameters,partitioned,buffer_pool,degree,instances FROM sys.all_indexes WHERE table_owner = ? AND \r\n" + 
							"			   table_name = ? AND index_name=? "  );
					getFunctions.setString(1, schema);
					getFunctions.setString(2, table);
					getFunctions.setString(3, constraint.getName());
					ResultSet resultSet3 = getFunctions.executeQuery();
					
					while (resultSet3.next()) {
						PrimaryKey key=(PrimaryKey)map.get(constraint.getName());
						key.setTableSpace(resultSet3.getString("tablespace_name"));
						key.setNextExtend(resultSet3.getInt("next_extent"));
					}
					getFunctions.close();
					resultSet3.close();
					break;
				}
			}
			
			

			
			// con.close();
			Logger.log("------------");
			
			return constraints;
			
	}
	
	
	
	public  Table getTable(String schema,String table)
	throws SQLException {

			
			PreparedStatement getTables = connection
					.prepareStatement("SELECT /*+RULE*/ owner,table_name,Decode(iot_type, NULL,(Decode(TEMPORARY, 'Y', 'TEMPORARY', 'REGULAR')), 'INDEX') table_type,"
							+ "tablespace_name,initial_extent,next_extent,pct_increase,max_extents,pct_free,pct_used,cluster_name FROM sys.all_tables t "
							+ "WHERE owner = ? AND table_name=? and iot_name IS NULL");
			getTables.setString(1, schema);
			getTables.setString(2, table);
			ResultSet resultSet = getTables.executeQuery();
			//
			Logger.log("------------");
			Logger.log("Schema"+schema);
			Logger.log("Tables");
			Table tab = null;
			while (resultSet.next()) {
				String name = resultSet.getString("table_name");
				 tab = new Table();
				tab.setName(name);
				tab.setSchema(schema);
				tab.setTableSpace(resultSet.getString("tablespace_name"));
				tab.setInitialExtent(resultSet.getInt("initial_extent"));
				tab.setNextExtent(resultSet.getInt("next_extent"));
				Logger.log(name);
			}
			getTables.close();
			resultSet.close();
			// con.close();
			Logger.log("------------");
			
			return tab;
			
	}
	
	private void tables(Schema schema, OracleDatabaseMetaData dbmd)
			throws SQLException {

		Connection con = dbmd.getConnection();
		PreparedStatement getTables = con
				.prepareStatement("SELECT /*+RULE*/ owner,table_name,Decode(iot_type, NULL,(Decode(TEMPORARY, 'Y', 'TEMPORARY', 'REGULAR')), 'INDEX') table_type,"
						+ "tablespace_name,initial_extent,next_extent,pct_increase,max_extents,pct_free,pct_used,cluster_name FROM sys.all_tables t "
						+ "WHERE owner = ? AND iot_name IS NULL");
		getTables.setString(1, schema.getName());
		ResultSet resultSet = getTables.executeQuery();
		//
		Logger.log("------------");
		Logger.log("Tables");
		List<Table> tables = new ArrayList<Table>();
		while (resultSet.next()) {
			String name = resultSet.getString("table_name");
			Table tab = new Table();
			tab.setName(name);
			tab.setSchema(schema.getName());
			tab.setTableSpace(resultSet.getString("tablespace_name"));
			tab.setInitialExtent(resultSet.getInt("initial_extent"));
			tab.setNextExtent(resultSet.getInt("next_extent"));
			tables.add(tab);
		}
		getTables.close();
		resultSet.close();
		// con.close();
		Logger.log("------------");

		for (Table tab : tables) {
			Logger.log(tab.getName());
			columns(schema.getName(), tab, dbmd);
		}
		schema.setTables(tables);

		Logger.log("------------");
	}
	
	public  List<View> getViews(String schema)
	throws SQLException {

			
			PreparedStatement getTables = connection
					.prepareStatement("SELECT /*+RULE*/ v.owner,v.view_name,v.text_length,o.created,o.last_ddl_time,o.status FROM sys.all_objects o," + 
							"  sys.all_views v WHERE o.owner = ? AND v.owner = ? AND o.owner = v.owner AND object_name = view_name AND " + 
							"  object_type = 'VIEW'");
			getTables.setString(1, schema);
			getTables.setString(2, schema);
			ResultSet resultSet = getTables.executeQuery();
			//
			Logger.log("------------");
			Logger.log("Views");
			List<View> views = new ArrayList<View>();
			while (resultSet.next()) {
				String name = resultSet.getString("view_name");
				View tab = new View();
				tab.setName(name);
				tab.setSchema(schema);
				
				views.add(tab);
			}
			getTables.close();
			resultSet.close();
			// con.close();
			Logger.log("------------");
			
			return views;
			
	}
	
	public  View getView(String schema,String view)
	throws SQLException {

			
		View tab = null;
		
			PreparedStatement getTables = connection
			.prepareStatement("SELECT  v1.owner,v1.view_name,v1.text,v2.comments FROM sys.all_views v1, sys.all_tab_comments v2 " + 
					"WHERE v1.view_name =?  AND v1.owner = ? AND v1.owner = v2.owner AND v1.view_name = v2.table_name");
			
				getTables.setString(2, schema);
				getTables.setString(1, view);
				ResultSet resultSet1 = getTables.executeQuery();
				//
				Logger.log("------------");
				Logger.log("View");
			
				while (resultSet1.next()) {
					String name = resultSet1.getString("view_name");
					 tab = new View();
					tab.setName(name);
					tab.setSchema(schema);
					tab.setText(resultSet1.getString("text"));
				}
				getTables.close();
				resultSet1.close();
			
			
				PreparedStatement getColumns = connection
				.prepareStatement("SELECT  v1.owner,v1.table_name,v1.column_name,v1.column_id,v2.comments FROM sys.all_tab_columns v1, sys.all_col_comments v2," + 
						"sys.all_views v3 WHERE v1.owner =  ? AND v1.table_name =? AND v1.owner = v2.owner AND v1.table_name = v2.table_name" + 
						" AND v1.column_name = v2.column_name AND v1.owner = v3.owner AND v1.table_name = v3.view_name order by column_id");
				
				getColumns.setString(1, schema);
				getColumns.setString(2, view);
				ResultSet resultSet2 = getColumns.executeQuery();
				
				List<Column>  columns=new ArrayList<Column>();
				while (resultSet2.next()) {
					Column col = new Column();
					col.setName(resultSet2.getString("column_name"));
					col.setColumnId(resultSet2.getInt("column_id"));
					columns.add(col);
					Logger.log(col.toString());
				}
				if(null!=tab)
				tab.setColumns(columns);
				getColumns.close();
				resultSet2.close();
				
			// con.close();
			Logger.log("------------");
			
			return tab;
			
	}

	private void views(Schema schema, OracleDatabaseMetaData dbmd)
			throws SQLException {

		String[] types = { "VIEW" };
		ResultSet resultSet = dbmd
				.getTables(null, schema.getName(), "%", types);

		Logger.log("------------");
		Logger.log("Views");
		List<View> tables = new ArrayList<View>();
		while (resultSet.next()) {
			String name = resultSet.getString(3);
			View tab = new View();
			tab.setName(name);
			tables.add(tab);

		}
		
		resultSet.close();
		for (View tab : tables) {
			Logger.log(tab.getName());
			columns(schema.getName(), tab, dbmd);
		}
		schema.setViews(tables);
		Logger.log("------------");
	}

	private void alias(String schema, OracleDatabaseMetaData dbmd)
			throws SQLException {

		String[] types = { "ALIAS" };
		ResultSet resultSet = dbmd.getTables(null, schema, "%", types);

		Logger.log("------------");
		Logger.log("Alias");
		while (resultSet.next()) {
			String name = resultSet.getString(3);

			Logger.log(name);

		}
		resultSet.close();
		Logger.log("------------");
	}

	public List<Synonym> getSynonyms(String schema,String table)
	throws SQLException {

			Connection con = connection;
			PreparedStatement getSynonyms = con
					.prepareStatement("WITH valid AS(SELECT s.owner,s.synonym_name,s.table_owner,s.table_name,s.db_link,'VALID' status FROM sys.all_synonyms s \r\n" + 
							"  WHERE s.table_owner =? AND (db_link IS NOT NULL OR EXISTS (SELECT 1 FROM sys.all_objects o WHERE s.table_name  = o.object_name AND s.table_owner = o.owner )) and s.table_name=? )\r\n" + 
							"   SELECT * FROM valid UNION ALL\r\n" + 
							"    SELECT s.owner,s.synonym_name,s.table_owner,s.table_name,s.db_link,'INVALID' status FROM sys.all_synonyms s WHERE s.table_owner =? and s.table_name=?  \r\n" + 
							"   AND (s.owner , s.synonym_name) NOT IN (SELECT owner, synonym_name FROM valid)");
			getSynonyms.setString(1, schema);
			getSynonyms.setString(2, table);
			getSynonyms.setString(3, schema);
			getSynonyms.setString(4, table);
			ResultSet resultSet = getSynonyms.executeQuery();
			//
			Logger.log("------------");
			Logger.log("Synonym");
			List<Synonym> tables = new ArrayList<Synonym>();
			while (resultSet.next()) {
				String name = resultSet.getString("synonym_name");
				Synonym synonym = new Synonym();
				synonym.setName(name);
				synonym.setSchema(resultSet.getString("owner"));
				synonym.setTable( resultSet.getString("table_name"));
				synonym.setTableSchema(resultSet.getString("table_owner"));
				tables.add(synonym);
			}
			getSynonyms.close();
			resultSet.close();
			Logger.log("------------");
			// con.close();
			return tables;
			
	}
	
	private void synonym(Schema schema, OracleDatabaseMetaData dbmd)
			throws SQLException {

		Connection con = dbmd.getConnection();
		PreparedStatement getSynonyms = con
				.prepareStatement("select OWNER,SYNONYM_NAME,TABLE_OWNER ,TABLE_NAME from ALL_SYNONYMS where OWNER =?");
		getSynonyms.setString(1, schema.getName());
		ResultSet resultSet = getSynonyms.executeQuery();
		//
		Logger.log("------------");
		Logger.log("Synonym");
		while (resultSet.next()) {
			String name = resultSet.getString(2);
			Synonym synonym = new Synonym();
			synonym.setName(name);
			synonym.setSchema(schema.getName());
			Logger.log(name);
			Logger.log(resultSet.getString(3));
			Logger.log(resultSet.getString(4));
		}
		getSynonyms.close();
		resultSet.close();
		// con.close();
		Logger.log("------------");
	}
	
	public List<Column>  getColumns(String schema, String table)
	throws SQLException {

		
		PreparedStatement getColumns = connection
				.prepareStatement("SELECT  v1.owner,v1.table_name,v1.column_id,v1.column_name,v1.data_type,v1.data_precision,v1.data_scale,v1.data_length,\r\n"
						+ "		v1.char_length,v1.nullable,v1.data_default,v2.comments,v1.char_used FROM sys.all_tab_columns v1, sys.all_col_comments v2,\r\n"
						+ "		 sys.all_tables v3 WHERE v1.owner = ? "
						+ "		  AND v1.table_name = ? AND "
						+ "		   v1.owner = v2.owner AND v1.table_name = v2.table_name AND v1.owner = v3.owner AND "
						+ "		   v1.table_name = v3.table_name AND v1.column_name = v2.column_name order by v1.column_id");
		getColumns.setString(1, schema);
		getColumns.setString(2, table);
		ResultSet resultSet = getColumns.executeQuery();
		//
		Logger.log("------------");
		Logger.log("Columns");
		List<Column>  columns=new ArrayList<Column>();
		while (resultSet.next()) {
			Column col = new Column();
			col.setName(resultSet.getString("column_name"));
			col.setType(resultSet.getString("data_type"));
			col.setColumnId(resultSet.getInt("column_id"));
			col.setPrecision(resultSet.getInt("data_precision"));
			col.setDataScale(resultSet.getInt("data_scale"));
			col.setDataLength(resultSet.getInt("data_length"));
			col.setCharLength(resultSet.getInt("char_length"));
			col.setDataDefault(resultSet.getString("data_default"));
			col.setCharUsed(resultSet.getString("char_used"));
			col.setNullable(resultSet.getString("nullable").equalsIgnoreCase(
					"Y"));
			columns.add(col);
			//Logger.log(col);
		}
		getColumns.close();
		resultSet.close();
		
		Logger.log("------------");
		return columns;
	}

	private void columns(String schema, Table table, OracleDatabaseMetaData dbmd)
			throws SQLException {

		Connection con = dbmd.getConnection();
		PreparedStatement getColumns = con
				.prepareStatement("SELECT  v1.owner,v1.table_name,v1.column_id,v1.column_name,v1.data_type,v1.data_precision,v1.data_scale,v1.data_length,\r\n"
						+ "		v1.char_length,v1.nullable,v1.data_default,v2.comments,v1.char_used FROM sys.all_tab_columns v1, sys.all_col_comments v2,\r\n"
						+ "		 sys.all_tables v3 WHERE v1.owner = ? "
						+ "		  AND v1.table_name = ? AND "
						+ "		   v1.owner = v2.owner AND v1.table_name = v2.table_name AND v1.owner = v3.owner AND "
						+ "		   v1.table_name = v3.table_name AND v1.column_name = v2.column_name order by v1.column_id");
		getColumns.setString(1, schema);
		getColumns.setString(2, table.getName());
		ResultSet resultSet = getColumns.executeQuery();
		//
		Logger.log("------------");
		Logger.log("Columns");
		while (resultSet.next()) {
			Column col = new Column();
			col.setName(resultSet.getString("column_name"));
			col.setType(resultSet.getString("data_type"));
			col.setColumnId(resultSet.getInt("column_id"));
			col.setPrecision(resultSet.getInt("data_precision"));
			col.setDataScale(resultSet.getInt("data_scale"));
			col.setDataLength(resultSet.getInt("data_length"));
			col.setCharLength(resultSet.getInt("char_length"));
			col.setDataDefault(resultSet.getString("data_default"));
			col.setCharUsed(resultSet.getString("char_used"));
			col.setNullable(resultSet.getString("nullable").equalsIgnoreCase(
					"Y"));
			table.addCloumn(col);
			Logger.log(col.toString());
		}
		getColumns.close();
		resultSet.close();

		Logger.log("------------");

	}
}
