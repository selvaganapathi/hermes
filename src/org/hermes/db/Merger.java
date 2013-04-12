package org.hermes.db;

import java.io.IOException;
import java.sql.SQLException;

public interface  Merger {

	public void createDDL() throws SQLException, IOException;
	
}
