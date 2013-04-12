package org.hermes.db;

public class Column {

	String name;
	String schema;
	String type;
	int precision;
	int dataLength;
	int charLength;
	String dataDefault;
	String charUsed;
	int dataScale;
	int columnId;
	int size;
	String table;
	String defination;
	boolean nullable;
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public String getDefination() {
		return defination;
	}
	public void setDefination(String defination) {
		this.defination = defination;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCharLength() {
		return charLength;
	}
	public void setCharLength(int charLength) {
		this.charLength = charLength;
	}
	public String getCharUsed() {
		return charUsed;
	}
	public void setCharUsed(String charUsed) {
		this.charUsed = charUsed;
	}
	public int getColumnId() {
		return columnId;
	}
	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}
	public String getDataDefault() {
		return dataDefault;
	}
	public void setDataDefault(String dataDefault) {
		this.dataDefault = dataDefault;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	public int getDataScale() {
		return dataScale;
	}
	public void setDataScale(int dataScale) {
		this.dataScale = dataScale;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+"-"+type+ "-"+dataLength+ "-"+nullable ;
	}
}
