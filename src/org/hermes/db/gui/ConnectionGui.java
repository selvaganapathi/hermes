package org.hermes.db.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.hermes.db.Connection;
import org.hermes.db.DBMerger;
import org.hermes.db.util.ConnectionsXmlManager;
import org.hermes.db.util.Logger;


public class ConnectionGui  extends  JPanel {

	JTextField sourceURL, destinationURL,sourceUser,destinationUser;
	JPasswordField sourcePass,destinationPass;
	JComboBox srcConnectionsCombo,destConnectionsCombo;
	 JTable table;
	private JTabbedPane tab;
	DBMerger merger;
	 SchemaGUI schemaGUI;
	 
	 JLabel urllable=new JLabel("URL : ");
		JTextField url=new JTextField(40);
		JLabel userlable=new JLabel("User Name : ");
		JTextField user=new JTextField();
		JLabel passlable=new JLabel("Password: ");
		JPasswordField pass=new JPasswordField();
		JLabel nameLable=new JLabel("Name: ");
		JTextField name=new JTextField();
		 JComponent listTable;
		 JButton addButton = new JButton("Add");
		 JButton deleteButton = new JButton("Delete");
		 JButton saveButton = new JButton("Save");
		 JLabel connectingLable=new JLabel("Connecting... ");
		 
		 ConnectionsXmlManager xmlManager=new ConnectionsXmlManager();
	public SchemaGUI getSchemaGUI() {
		return schemaGUI;
	}
	public void setSchemaGUI(SchemaGUI schemaGUI) {
		this.schemaGUI = schemaGUI;
	}
	public DBMerger getMerger() {
		return merger;
	}
	public void setMerger(DBMerger merger) {
		this.merger = merger;
	}
	public JTabbedPane getTab() {
		return tab;
	}
	public void setTab(JTabbedPane tab) {
		this.tab = tab;
	}
	public ConnectionGui()
	{
		 GridBagLayout gridbag = new GridBagLayout();
         GridBagConstraints c = new GridBagConstraints();
       
        this.setLayout(null);
        try {
			xmlManager.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        displayItems(gridbag,c);
        

	}
	
	
	public void displayItems(GridBagLayout layout,GridBagConstraints c )
	{
		  
		JLabel sourceConnectionlable=new JLabel("Source Connection  ");
		
		Font newLabelFont=new Font(sourceConnectionlable.getFont().getName(),Font.BOLD,sourceConnectionlable.getFont().getSize());  
		sourceConnectionlable.setFont(newLabelFont);
		JLabel destinationConnectionlable=new JLabel("Destination Connection  ");
		destinationConnectionlable.setFont(newLabelFont);
		JButton fetchButton = new JButton("Connect");
		fetchButton. setHorizontalAlignment(SwingConstants.CENTER);
		fetchButton.addActionListener(new ClickListener());
		
		JLabel dummy1=new JLabel(" -");
		JLabel dummy2=new JLabel(" - ");
		
		JLabel sourceURLlable=new JLabel("URL : ");
		sourceURL=new JTextField(40);
		JLabel destURLlable=new JLabel("URL : ");
		destinationURL=new JTextField(40);
		
		JLabel sourceUserlable=new JLabel("User Name : ");
		sourceUser=new JTextField();
		JLabel destUserlable=new JLabel("User Name  : ");
		destinationUser=new JTextField();
		
		
		
		JLabel sourcePasslable=new JLabel("Password: ");
		sourcePass=new JPasswordField();
		JLabel destPasslable=new JLabel("Password  : ");
		destinationPass=new JPasswordField();
		
		sourceConnectionlable.setBounds(20, 10, 200, 50);
		add( sourceConnectionlable);
		destinationConnectionlable.setBounds(700, 10, 200, 50);
		add( destinationConnectionlable);
		
		sourceURLlable.setBounds(20, 90, 200, 50);
		add( sourceURLlable);
		sourceURL.setBounds(100, 100, 500, 30);
		add( sourceURL);
		
		destURLlable.setBounds(700, 90, 200, 50);
		add( destURLlable);
		destinationURL.setBounds(800, 100, 500, 30);
		add( destinationURL);
		
		sourceUserlable.setBounds(20, 130, 200, 50);
		add( sourceUserlable);
		sourceUser.setBounds(100, 140, 500, 30);
		add( sourceUser);
		
		destUserlable.setBounds(700, 130, 200, 50);
		add( destUserlable);
		destinationUser.setBounds(800, 140, 500, 30);
		add( destinationUser);
		
		sourcePasslable.setBounds(20, 170, 200, 50);
		add( sourcePasslable);
		sourcePass.setBounds(100, 180, 500, 30);
		add( sourcePass);
		
		destPasslable.setBounds(700, 170, 200, 50);
		add( destPasslable);
		destinationPass.setBounds(800, 180, 500, 30);
		add( destinationPass);
		
		fetchButton.setBounds(800, 220, 100, 30);
		add( fetchButton);
		
		connectingLable.setBounds(920, 220, 100, 30);
		connectingLable.setVisible(false);
		add( connectingLable);
		
		String[] connections ={};
		
		destConnectionsCombo = new JComboBox(connections);
		destConnectionsCombo.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		destConnectionsCombo.setBounds(800, 60, 500, 30);
		srcConnectionsCombo = new JComboBox(connections);
		srcConnectionsCombo.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		srcConnectionsCombo.setBounds(100, 60, 500, 30);
		add( destConnectionsCombo);
		add( srcConnectionsCombo); 
		srcConnectionsCombo.addItemListener(new ItemChangeListener("src"));
		destConnectionsCombo.addItemListener(new ItemChangeListener("dest"));
		loadConnections();
		
		
		  listTable=getList();
	     listTable.setBounds(200, 400, 900, 400);
	     add( listTable);
	     
	     
	     addButton. setHorizontalAlignment(SwingConstants.CENTER);
	     addButton.addActionListener(new AddListener());
	     addButton.setBounds(1200, 400, 100, 30);
			add( addButton);
			
			deleteButton. setHorizontalAlignment(SwingConstants.CENTER);
			deleteButton.addActionListener(new DeleteListener());
			deleteButton.setBounds(1200, 500, 100, 30);
				add( deleteButton);
	    
	     saveButton. setHorizontalAlignment(SwingConstants.CENTER);
	     saveButton.addActionListener(new SaveListener());
	     saveButton.setBounds(1200, 700, 100, 30);
	     saveButton.setVisible(false);
			add( saveButton);
			
			
			
			nameLable.setBounds(300, 400, 200, 50);
			name.setBounds(380, 410, 500, 30);
			urllable.setBounds(300, 470, 200, 50);
			url.setBounds(380, 480, 500, 30);
			userlable.setBounds(300, 520, 200, 50);
			user.setBounds(380, 530, 500, 30);
			passlable.setBounds(300, 580, 200, 50);
			pass.setBounds(380, 590, 500, 30);
			hideEntries();
			add( urllable);
			add( url);
			add( userlable);
			add( user);
			add( passlable);
			add( pass);
			add( nameLable);
			add( name);
		
		/* destinationURL.setText("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=orac5n2-vip.oasis.dev.wdh.intuit.com)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=orac5n2-vip.oasis.dev.wdh.intuit.com)(PORT=1521)))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=oasisdev1_b)))");
    	 destinationUser.setText("OASIS_APP");
	    	destinationPass.setText("m44ptnn4_oa");
			
	    	sourceURL.setText("jdbc:oracle:thin:@oracle:1522:OASIS1");
	    	sourceUser.setText("oasis_owner");
	    	sourcePass.setText("o3ngin33r");*/
	    	refreshTable();
	    	ConnectionGui.this.validate();
        	ConnectionGui.this.repaint();
		
	}
	
	private  void hideEntries()
	{
		urllable.setVisible(false);
		url.setVisible(false);
		userlable.setVisible(false);
		user.setVisible(false);
		passlable.setVisible(false);
		pass.setVisible(false);
		nameLable.setVisible(false);
		name.setVisible(false);
	}
	
	class ItemChangeListener implements ItemListener{
	    String name;
		public ItemChangeListener( String name)
		{
			this.name=name;
		}
	    public void itemStateChanged(ItemEvent event) {
	       if (event.getStateChange() == ItemEvent.SELECTED) {
	          Object item = event.getItem();
	        
	          if(name=="src")
	          {
	        	
	        	  Connection con=xmlManager.getConnection(srcConnectionsCombo.getSelectedItem().toString());
	        	  sourceUser.setText(con.getUsername());
	        	  sourcePass.setText(con.getPassword());
	        	  sourceURL.setText(con.getUrl());
	          }
	          else
	          {
	        	  
	        	  Connection con=xmlManager.getConnection(destConnectionsCombo.getSelectedItem().toString());
	        	  destinationUser.setText(con.getUsername());
	        	  destinationPass.setText(con.getPassword());
	        	  destinationURL.setText(con.getUrl());
	          }
	       }
	    }       
	}
	
	private  void showEntries()
	{
		urllable.setVisible(true);
		url.setVisible(true);
		userlable.setVisible(true);
		user.setVisible(true);
		passlable.setVisible(true);
		pass.setVisible(true);
		nameLable.setVisible(true);
		name.setVisible(true);
		
		this.validate();
		this.repaint();
	}
	
	private JComponent getList()
	{
		 Object[] columnNames = {"Name","Url","UserName","Password"};
	        Object[][] data = {	        };
	        DefaultTableModel model = new DefaultTableModel(data, columnNames);
	         table  = new JTable(model) {

	            private static final long serialVersionUID = 1L;

	            /*@Override
	            public Class getColumnClass(int column) {
	            return getValueAt(0, column).getClass();
	            }*/
	           
	            
	            public boolean isCellEditable(int row,int column){  
	                return true;  
	              }  
	            
	           @Override
	        public TableCellEditor getCellEditor(int row, int column) {
	        	// TODO Auto-generated method stub
	        	   if(column==3)
	        	   {
	        		   JPasswordField pwf = new JPasswordField();
	       	        DefaultCellEditor editor = new DefaultCellEditor(pwf);
	        		   return editor;
	        	   }
	        	   else
	        	return super.getCellEditor(row, column);
	        }
	           
	           @Override
	        public TableCellRenderer getCellRenderer(int row, int column) {
	        	// TODO Auto-generated method stub
	        	   if(column==3)
	        	   {
	        		  return  new PasswordRenderer();
	        	   }
	        	   else
	        	return super.getCellRenderer(row, column);
	        }
	            
	        };
	        table.setPreferredScrollableViewportSize(table.getPreferredSize());
	        JScrollPane scrollPane = new JScrollPane(table);
	        table.getColumnModel().getColumn(0).setPreferredWidth(100);
	        table.getColumnModel().getColumn(0).setWidth(100);
	        table.getColumnModel().getColumn(1).setPreferredWidth(300);
	        table.getColumnModel().getColumn(1).setWidth(300);
	        
	        table.getColumnModel().getColumn(2).setPreferredWidth(100);
	        table.getColumnModel().getColumn(2).setWidth(100);
	        table.getColumnModel().getColumn(3).setPreferredWidth(100);
	        table.getColumnModel().getColumn(3).setWidth(100);
	        
	        JPasswordField pwf = new JPasswordField();
	        DefaultCellEditor editor = new DefaultCellEditor(pwf);
	        table.getColumnModel().getColumn(3).setCellRenderer(new PasswordRenderer());
	        table.getColumnModel().getColumn(3).setCellEditor(editor);
	        table.addPropertyChangeListener(new ChangeListenerImpl());
	        
	       
	        
	       return scrollPane;
	}
	
	class PasswordRenderer extends JPasswordField implements TableCellRenderer 
	{

		public PasswordRenderer() {
		setOpaque(true); 
		}
	
		public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, 
		Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			
		setText( (value ==null) ? "" : value.toString() ); 
		return this;
		} 
		
		
	}
	
	private class ChangeListenerImpl implements PropertyChangeListener 
	{

		public void propertyChange(PropertyChangeEvent evt) {
			
	    	int row=table.getSelectedRow();
	    	
	    	if(row>=0 && !table.isEditing())
	    	{
	    		Connection con=new Connection();
	        	con.setName(table.getValueAt(row, 0).toString());
	        	con.setUsername(table.getValueAt(row, 2).toString());
	        	con.setPassword(table.getValueAt(row, 3).toString());
	        	con.setUrl(table.getValueAt(row, 1).toString());
	        	
	        	xmlManager.updateConnection(con);
	        	refreshTable();
	    	}
	    	
			
		}
		
	}
	
	private class ClickListener implements ActionListener {
		 
        public void actionPerformed(ActionEvent e)
        {
        	
        	
        	tab.setEnabledAt(1, false);
        	connectingLable.setVisible(true);
        	
        	String srcURL=sourceURL.getText();
        	String srcPass=new String(sourcePass.getPassword());
        	String srcUser=sourceUser.getText();
        	String destURL=destinationURL.getText();
        	String destPass=new String(destinationPass.getPassword());
        	String destUser=destinationUser.getText();
        	 merger=new DBMerger();
        	
        	merger.setDestinationUrl(destURL);
    		merger.setDestinationUsername(destUser);
    		merger.setDestinationPassword(destPass);
    		
    		merger.setSourceUrl(srcURL);
    		merger.setSourceUsername(srcUser);
    		merger.setSourcePassword(srcPass);
    		
    		ConnectionGui.this.revalidate();
        	ConnectionGui.this.repaint();
    		
    		
    		new Thread ()
    		{
    			@Override
    			public void run() {
    				
    				try {
    					merger.init();
    					schemaGUI.setMerger(merger);
    					schemaGUI.loadSchemas();
    					connectingLable.setVisible(false);
    					tab.setEnabledAt(1, true);
    					tab.setSelectedIndex(1);
    				} catch (ClassNotFoundException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				} catch (SQLException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    				catch (RuntimeException e1) {
    					if(e1.getCause() instanceof SQLException)
    					{
    					  JOptionPane.showMessageDialog(null,e1.getMessage()+"\n"+ e1.getCause().getMessage());
    					  
    					}
    					else
    					{
    						JOptionPane.showMessageDialog(null, e1.getMessage());
    					}
    					return;
    				}
    			}
    		}.start();
    		
    		
			Runtime.getRuntime().addShutdownHook(new Thread() {
	    		 
	    		 public void run() {
	    			 try {
	    				 Logger.log("Closing All connection");
	    				 Logger.shutdown();
						merger.shutdown();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		    }
	    		
	    	 });
    		
        	Logger.log("srcURL"+srcURL+"-"+srcPass);
        	
        	Logger.log("You clicked the button");
        }
    }
	
	private void loadConnections()
	{
		srcConnectionsCombo.removeAllItems();
		destConnectionsCombo.removeAllItems();
		List<Connection> list=xmlManager.getConnections();
		for(Connection con : list)
		{
			srcConnectionsCombo.addItem(con.getName());
			destConnectionsCombo.addItem(con.getName());
		}
	}
	
	private class AddListener implements ActionListener {
		 
        public void actionPerformed(ActionEvent e)
        {
        	url.setText("");
        	user.setText("");
        	pass.setText("");
        	name.setText("");
        	listTable.setVisible(false);
        	saveButton.setVisible(true);
        	deleteButton.setVisible(false);
        	addButton.setVisible(false);
        	showEntries();
        	loadConnections();
        	
        	
        }
    }
	
	private class DeleteListener implements ActionListener {
		 
        public void actionPerformed(ActionEvent e)
        {
        	String name=table.getValueAt(table.getSelectedRow(), 0).toString() ;
        	xmlManager.deleteConnection(name);
        	refreshTable();
        	loadConnections();
        	
        }
    }
	
	private void refreshTable()
	{
		Object[] columnNames = {"Name","Url","UserName","Password"};
		List<Connection> connections=xmlManager.getConnections();
		
		if(null!=connections)
		{
			
			Object[][] data =new Object[connections.size()][4];
			int size=connections.size();
			for(int i=0;i<size;i++)
			{
				Connection con=connections.get(i);
				data[i][0]=con.getName();
				data[i][1]=con.getUrl();
				data[i][2]=con.getUsername();
				data[i][3]=con.getPassword();
				
				
			}
			DefaultTableModel model = new DefaultTableModel(data, columnNames);
			 table.setModel( model ); 
			 table.repaint();
		}
		
	}
	
	private class SaveListener implements ActionListener {
		 
        public void actionPerformed(ActionEvent e)
        {
        	saveButton.setVisible(false);
        	deleteButton.setVisible(true);
        	addButton.setVisible(true);
        	Connection con=new Connection();
        	con.setName(name.getText());
        	con.setUsername(user.getText());
        	con.setPassword(new String(pass.getPassword()));
        	con.setUrl(url.getText());
        	xmlManager.addConnection(con);
        	hideEntries();
        	listTable.setVisible(true);
        	
        	refreshTable();
        	loadConnections();
        	ConnectionGui.this.validate();
        	ConnectionGui.this.repaint();
        }
    }
}
