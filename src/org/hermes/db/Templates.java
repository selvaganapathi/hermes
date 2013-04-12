package org.hermes.db;

public class Templates {
	public static final String RELATED_OWNER="r_owner";
	public static final String TABLE_NAME="table_name";
	public static final String RELATED_TABLE_NAME="r_table";
	public static final String VIEW_NAME="view_name";
	public static final String COLUMN_NAME="column_name";
	public static final String INDEX_NAME="index_name";
	public static final String DATA_TYPE="data_type";
	public static final String CONSTRAINT_NAME="constraint_name";
	public static final String DATA_LENGTH="data_length";
	public static final String NULL_VALUE="null_value";
	public static final String NEXT_EXTENT="nextextent";
	public static final String NULL="NULL";
	public static final String NOT_NULL="NOT NULL";
	public static final String COLUMNS="columns";
	public static final String RELATED_COLUMNS="r_col";
	public static final String COLUMN="column";
	public static final String TABLE_SPACE="table_space";
	public static final String INDEX_TYPE="index_type";
	public static final String PARAMETERS="parameters";
	public static final String TABLE_SPACE_NAME="table_space";
	public static final String SELECT_CRITERIA="select_condtion";
	public static final String TABLE_SPACE_DEFINATION="TABLESPACE table_space_name \r\n";
	public static final String TRIGGER_NAME="trigger_name";
	public static final String TRIGGER_BODY="trigger_body";
	public static final String TRIGGER_DESCRIPTION="trigger_desc";
	public static final String PRIVILEGES="privileges";
	public static final String GRANTEE="grantee";
	public static final String PROCEDURE_NAME="procedure_name";
	public static final String PROCEDURE_DETAILS="procedure_detail";
	public static final String OWNER="owner";

	public static final String PROMPT_CREATE_TABLE_ADD_COLUMN_TEMPLATE="PROMPT CREATE TABLE table_name \r\n";
	public static final String CREATE_TABLE_TEMPLATE="PROMPT CREATE TABLE table_name \r\n" +
			"CREATE TABLE table_name (\r\n" + 
			"columns"+
			")\r\n" + 
			"table_space"+
			"  STORAGE (\r\n" + 
			"    NEXT       nextextent \r\n" + 
			"  )\r\n" + 
			"/\r\n";
	
	public static final String COLUMN_DEFINATION="column_name  data_type  data_length null_value";
	
	public static final String PROMPT_ALTER_TABLE_ADD_COLUMN_TEMPLATE="PROMPT ALTER TABLE table_name \r\n";
	
	public static final String CREATE_VIEW_TEMPLATE="PROMPT VIEW view_name \r\n" +
			"CREATE OR REPLACE VIEW view_name (" +
			"columns \r\n"+
			") AS\r\n" +
			"select_condtion"+
			"\r\n" + 
			"/\r\n";
	
	public static final String ALTER_TABLE_ADD_COLUMN_TEMPLATE="ALTER TABLE table_name \r\n" +
			"ADD column ;\r\n" ;
	public static final String MODIFY_TABLE_ADD_COLUMN_TEMPLATE="ALTER TABLE table_name \r\n" + 
			"  MODIFY column ;\r\n";
	
	public static final String DROP_INDEX_TEMPLATE="PROMPT DROP INDEX index_name \r\n" +
												 "DROP INDEX index_name ;";
	
	public static final String CREATE_INDEX_TABLE_SPACE_TEMPLATE="TABLESPACE table_space \r\n";
	
	public static final String CREATE_INDEX_TEMPLATE="PROMPT CREATE INDEX index_name \r\n"+
														"CREATE INDEX index_name \r\n" + 
														"  ON table_name (\r\n" + 
														"  columns \r\n" + 
														"  )\r\n" + 
														"table_space" + 
														"  STORAGE (\r\n" + 
														"    NEXT       nextextent \r\n" + 
														"  )\r\n" + 
														"/\r\n" ;
	
	public static final String CREATE_INDEX_DOMAIN_TEMPLATE="PROMPT CREATE INDEX index_name \r\n"+
							"CREATE INDEX index_name \r\n" + 
							"  ON table_name (\r\n" + 
							"  columns \r\n" + 
							"  )\r\n" + 
							" INDEXTYPE IS index_type \r\n" + 
							" PARAMETERS(' parameters ') \r\n" + 
							"/\r\n" ;
	
	public static final String DROP_CONSTRAINT_TEMPLATE="PROMPT DROP CONSTRAINT constraint_name \r\n"+
								"ALTER TABLE table_name\r\n"+
								"drop CONSTRAINT constraint_name; \r\n";
	
	public static final String CREATE_UNIQUE_CONSTRAINT_TEMPLATE="PROMPT ALTER TABLE table_name ADD CONSTRAINT constraint_name UNIQUE\r\n" + 
			"ALTER TABLE table_name\r\n" + 
			"  ADD CONSTRAINT constraint_name UNIQUE (\r\n" + 
			"  columns \r\n" + 
			"  )\r\n" + 
			"/\r\n" + 
			"";

	public static final String CREATE_PRIMARY_CONSTRAINT_TEMPLATE="ALTER TABLE table_name \r\n" + 
			"  ADD PRIMARY KEY (\r\n" + 
			"  columns \r\n" + 
			"  )\r\n" + 
			"  USING INDEX\r\n" + 
			"    STORAGE (\r\n" + 
			"      NEXT      nextextent \r\n" + 
			"    )\r\n" + 
			"/\r\n" + 
			"";
	
	public static final String CREATE_FOREIGN_CONSTRAINT_TEMPLATE="PROMPT ALTER TABLE table_name ADD CONSTRAINT constraint_name FOREIGN KEY\r\n" + 
			"ALTER TABLE table_name\r\n" + 
			"  ADD CONSTRAINT constraint_name FOREIGN KEY (\r\n" + 
			"    columns \r\n" + 
			"  ) REFERENCES r_owner.r_table (\r\n" + 
			"    r_col \r\n" + 
			"  )\r\n" + 
			"/\r\n" + 
			"";
	
	public static final String CREATE_TRIGGER_TEMPLATE="PROMPT CREATE OR REPLACE TRIGGER trigger_name\r\n" + 
			"CREATE OR REPLACE TRIGGER trigger_name \r\n" +
			"trigger_desc \r\n"+
			"trigger_body \r\n"+
			"/\r\n" + 
			"";
	
	public static final String GRANT_TEMPLATE="GRANT privileges ON table_name TO grantee;  \r\n"; 
	
	public static final String SYNONYM_TEMPLATE="PROMPT RUN AS  owner " +
			"\r\n create or replace  synonym owner.table_name for r_owner.r_table \r\n";
	
	public static final String PROCEDURE_TEMPLATE="PROMPT CREATE OR REPLACE PROCEDURE procedure_name \r\n" + 
			"CREATE OR REPLACE procedure_detail \r\n";
	
		
	 

}
