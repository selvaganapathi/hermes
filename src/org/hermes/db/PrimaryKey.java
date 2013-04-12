package org.hermes.db;


public class PrimaryKey extends Constraint {

	
	String tableSpace;
	boolean indexEnabled;
	long nextExtend;
	
	public boolean isIndexEnabled() {
		return indexEnabled;
	}
	public void setIndexEnabled(boolean indexEnabled) {
		this.indexEnabled = indexEnabled;
	}
	public long getNextExtend() {
		return nextExtend;
	}
	public void setNextExtend(long nextExtend) {
		this.nextExtend = nextExtend;
	}
	public String getTableSpace() {
		return tableSpace;
	}
	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}

}
