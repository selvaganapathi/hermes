package org.hermes.db.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.hermes.db.DBMerger;
import org.hermes.db.Procedure;
import org.hermes.db.Schema;
import org.hermes.db.Table;
import org.hermes.db.View;
import org.hermes.db.util.Logger;


public class SchemaGUI  extends  JPanel {
	
	DBMerger merger;
	 JComboBox schemaCombo;
	 JRadioButton tablebutton;
     JRadioButton viewbutton ;
     JRadioButton procedurebutton ;
     JTable table;
     JLabel fetchinglabel;
	JLabel merginglabel;
	JCheckBox selectAll;
	
	 ExecutorService  service =Executors.newFixedThreadPool(5);
	 List<Future<Boolean>> results=new ArrayList<Future<Boolean>>();
		
	public DBMerger getMerger() {
		return merger;
	}

	public void setMerger(DBMerger merger) {
		this.merger = merger;
	}

	public SchemaGUI()
	{
		
       /* GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        displayItems(layout);*/
		this.setLayout(null);
		 displayItems();
      

	}
	
	public void loadSchemas()
	{
		try {
			List<Schema> schemas=	merger.getSchemas();
			schemaCombo.removeAllItems();
			schemaCombo.addItem("--Select--");
			for(Schema schema : schemas)
			{
				schemaCombo.addItem(schema.getName());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void displayItems()
	{
		 fetchinglabel=new JLabel("");
		 merginglabel=new JLabel("");
		
		JLabel listlabel=new JLabel("Schema's List");
		Font newLabelFont=new Font(listlabel.getFont().getName(),Font.BOLD,listlabel.getFont().getSize());  
		listlabel.setFont(newLabelFont);
		
		String[] schemas = {"--Select--", "OASIS_OWNER", "BRE", "IMPORT_OWNER", "OASIS_APP" };
         schemaCombo = new JComboBox(schemas);
       // schemaCombo.setPreferredSize(new Dimension(200,50));
        schemaCombo.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXX");
         tablebutton = new JRadioButton("Tables");
         viewbutton = new JRadioButton("Views");
         procedurebutton = new JRadioButton("Procedures");
        JButton fetchButton = new JButton("Fetch");
        fetchButton.addActionListener(new FetchClickListener());
		//fetchButton. setHorizontalAlignment(SwingConstants.RIGHT);
		JButton submitButton = new JButton("Merge");
		submitButton.addActionListener(new MergeClickListener());
		 selectAll=new JCheckBox("All");
		
		
		JLabel fetchListlabel=new JLabel("List");
		fetchListlabel.setFont(newLabelFont);
        
        ButtonGroup group = new ButtonGroup();
        group.add(tablebutton);
        group.add(viewbutton);
        group.add(procedurebutton);
        
        tablebutton.setSelected(true);
        
        JComponent listTable=getList();
        fetchListlabel.setBounds(20, 100, 100, 50);
        listTable.setBounds(120, 100, 500, 300);
        
        listlabel.setBounds(20, 20, 200, 50);
        schemaCombo.setBounds(120, 20, 200, 40);
        
        tablebutton.setBounds(350, 20, 100, 50);
        viewbutton.setBounds(500, 20, 100, 50);
        procedurebutton.setBounds(600, 20, 100, 50);
        fetchButton.setBounds(700, 30, 100, 30);
        
        fetchinglabel.setBounds(850, 30, 100, 20);
      
        
        submitButton.setBounds(700, 80, 100, 30);
        
        merginglabel.setBounds(850, 80, 100, 20);
      
        
        selectAll.setBounds(620, 100, 20, 20);
        selectAll.addItemListener(new ItemListenerImpl());
       
		 add(fetchListlabel);
		 add(schemaCombo) ;
		 add(tablebutton) ;
		 add(viewbutton) ;
		 add(procedurebutton) ;
		 add(fetchButton) ;
		 add(listlabel);
		 add(listTable);
		 add(submitButton);
		 add(selectAll);
		 add(fetchinglabel);
		 add(merginglabel);
		
	}
	
	private class ItemListenerImpl implements ItemListener
	{

		public void itemStateChanged(ItemEvent e) {
			
			int rows=table.getRowCount();
			
			if(selectAll.isSelected())
			{
				
	        		for(int i=0;i<rows;i++)
	        		{
	        			table.setValueAt((Boolean)true, i,0);
	        			
	        		}
			}
			else
			{
				for(int i=0;i<rows;i++)
        		{
        			table.setValueAt((Boolean)false, i,0);
        			
        		}
			}
		}
		
	}
	
	private JComponent getList()
	{
		 Object[] columnNames = {"Select","Name",};
	        Object[][] data = {	        };
	        DefaultTableModel model = new DefaultTableModel(data, columnNames);
	         table  = new JTable(model) {

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
	
	private class FetchClickListener implements ActionListener {
		 
        public void actionPerformed(ActionEvent e)
        {
        
        	fetchinglabel.setText("( Fetching...)");
        	fetchinglabel.revalidate();
        	fetchinglabel.repaint();
        	
        	Thread action =new Thread()
        	{
        		@Override
        		public void run() {
        			
        			 Object[] columnNames = {"Select","Name",};
                	 DefaultTableModel model =null;
                	try {
        	        	if(tablebutton.isSelected())
        	        	{
        	        		
        	        		 
        					List<Table> tables=	merger.getTables(schemaCombo.getSelectedItem().toString());
        					
        					Object[][] data =new Object[tables.size()][2];
        					int size=tables.size();
        					for(int i=0;i<size;i++)
        					{
        						Table table=tables.get(i);
        						data[i][0]=false;
        						data[i][1]=table.getName();
        						Logger.log(table.getName());
        					}
        					
        					 
        					  model = new DefaultTableModel(data, columnNames);
        					 
        					
        					
        	        	}
        	        	else if(viewbutton.isSelected())
        	        	{
        	        		List<View> tables=	merger.getViewes(schemaCombo.getSelectedItem().toString());
        					
        					Object[][] data =new Object[tables.size()][2];
        					int size=tables.size();
        					for(int i=0;i<size;i++)
        					{
        						Table table=tables.get(i);
        						data[i][0]=false;
        						data[i][1]=table.getName();
        						System.out.println(table.getName());
        					}
        					
        					 
        					  model = new DefaultTableModel(data, columnNames);
        	        	}
        	        	else if(procedurebutton.isSelected())
        	        	{
        	        		List<Procedure> tables=	merger.getProcedures(schemaCombo.getSelectedItem().toString());
        					
        					Object[][] data =new Object[tables.size()][2];
        					int size=tables.size();
        					for(int i=0;i<size;i++)
        					{
        						Procedure table=tables.get(i);
        						data[i][0]=false;
        						data[i][1]=table.getName();
        						System.out.println(table.getName());
        					}
        					
        					 
        					  model = new DefaultTableModel(data, columnNames);
        	        	}
        	        	table.setModel( model ); 
        				table.repaint();
                	} catch (SQLException e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        			} catch (IOException e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        			}
        			finally
        			{
        				fetchinglabel.setText("");
        	        	fetchinglabel.revalidate();
        	        	fetchinglabel.repaint();
        			}
        		}
        	};
        
        	
        	action.start();
        	
        		
        }
	}
	
	private class MergeClickListener implements ActionListener {
		 
        public void actionPerformed(ActionEvent e)
        {
        	
        	merginglabel.setText(" ( Merging...)");
        	merginglabel.revalidate();
        	merginglabel.repaint();
        	
        	Thread action =new Thread()
        	{
        		
        		@Override
        		public void run() {
        			
        			final List<String> list=new ArrayList<String>();
               	 try {
       	        	if(tablebutton.isSelected())
       	        	{
       	        		int rows=table.getRowCount();
       	        		for(int i=0;i<rows;i++)
       	        		{
       	        			boolean selected=(Boolean)table.getValueAt(i, 0);
       	        			if(selected)
       	        			{
       	        				System.out.println("selected"+i+"--"+table.getValueAt(i, 1));
       	        				list.add(table.getValueAt(i, 1).toString());
       	        			}
       	        			
       	        		}
       	        	
       	        	 for(final String name: list)
	                	  {
	       	        		results.add(service.submit(new Callable<Boolean>() {
	       	                 public Boolean call() {
	       	                    
		       	                  try {
		       	                	 
		       	                		merger.mergeTable(schemaCombo.getSelectedItem().toString(), name);
		       	                	  
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								return true;
	       	                	 
	       	                 }}));
       	        		
	                	  }
       	                 
       				
       					
       	        	}
       	        	else if(viewbutton.isSelected())
       	        	{
       	        		int rows=table.getRowCount();
       	        		for(int i=0;i<rows;i++)
       	        		{
       	        			boolean selected=(Boolean)table.getValueAt(i, 0);
       	        			if(selected)
       	        			{
       	        				System.out.println("selected"+i+"--"+table.getValueAt(i, 1));
       	        				list.add(table.getValueAt(i, 1).toString());
       	        			}
       	        			
       	        		}
       	        		
       	        	 for(final String name: list)
	               	  {
	      	        		
	       	        		results.add(service.submit(new Callable<Boolean>() {
	          	                 public Boolean call() {
	          	                    
	   	       	                  try {
	   	       	                merger.mergeView(schemaCombo.getSelectedItem().toString(), name);
	   							} catch (SQLException e) {
	   								// TODO Auto-generated catch block
	   								e.printStackTrace();
	   							} catch (IOException e) {
	   								// TODO Auto-generated catch block
	   								e.printStackTrace();
	   							}
	   							
	   							return true;
	          	                	 
	          	                 }}));
	               	  }
       	        		
       	        		 
       	        	}
       	        	else if(procedurebutton.isSelected())
       	        	{
       	        		int rows=table.getRowCount();
       	        		for(int i=0;i<rows;i++)
       	        		{
       	        			boolean selected=(Boolean)table.getValueAt(i, 0);
       	        			if(selected)
       	        			{
       	        				System.out.println("selected"+i+"--"+table.getValueAt(i, 1));
       	        				list.add(table.getValueAt(i, 1).toString());
       	        			}
       	        			
       	        		}
       	        		
       	        		for(final String name: list)
  	               	  {
       	        		results.add(service.submit(new Callable<Boolean>() {
         	                 public Boolean call() {
         	                    
  	       	                  try {
  	       	                	merger.mergeProcedure(schemaCombo.getSelectedItem().toString(), name);
  							} catch (SQLException e) {
  								// TODO Auto-generated catch block
  								e.printStackTrace();
  							} catch (IOException e) {
  								// TODO Auto-generated catch block
  								e.printStackTrace();
  							}
  							
  							return true;
         	                	 
         	                 }}));
  	               	  }
       	        		
       	        		
       	        	}
               	 } 
       				finally
       				{
       					try {
							if(checkForCompletion(schemaCombo.getSelectedItem().toString()))
							{
							merginglabel.setText("");
							merginglabel.revalidate();
							merginglabel.repaint();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
       				}
        		}
        	};
        	
        	action.start();
        	
	        	
        	
        }
	}
	
	private boolean checkForCompletion(String schema) throws IOException
	{
		boolean completed =false;
		
		while(!completed)
		{
			completed=true;
			for(Future<Boolean> future:results)
			{
				try {
					future.get(1000, TimeUnit.MILLISECONDS);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					completed=false;
					
					//Thread.sleep(500);
					break;
				} 
			}
			
			
		}
		
		results.clear();
		System.out.println("Completed merging");
		merger.mergeFiles(schema);
		File file=new File(".");
		JOptionPane.showMessageDialog(null,"Generated Files are available under "+file.getAbsolutePath());
		
		return completed;
		
	}

}
