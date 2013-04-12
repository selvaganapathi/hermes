package org.hermes.db.gui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GridGUI  extends  JPanel {
	
	public GridGUI()
	{
		add(getList());
	}
	
	private JComponent getList()
	{
		 Object[] columnNames = {"Select","Name",};
	        Object[][] data = {
	            {false,"Table 1"},
	            {true,"Table 1"},
	            {true,"Table 1"},
	            {false,"Table 1"}
	        };
	        DefaultTableModel model = new DefaultTableModel(data, columnNames);
	        JTable table  = new JTable(model) {

	            private static final long serialVersionUID = 1L;

	            /*@Override
	            public Class getColumnClass(int column) {
	            return getValueAt(0, column).getClass();
	            }*/
	            @Override
	            public Class getColumnClass(int column) {
	                switch (column) {
	                    case 0:
	                    	 return Boolean.class;
	                    case 1:
	                        return String.class;
	                    default:
	                        return Boolean.class;
	                }
	            }
	            
	            public boolean isCellEditable(int row,int column){  
	                if(column == 1) return false;//the 4th column is not editable  
	                return true;  
	              }  
	        };
	        table.setPreferredScrollableViewportSize(table.getPreferredSize());
	        JScrollPane scrollPane = new JScrollPane(table);
	       return scrollPane;
	}

}
