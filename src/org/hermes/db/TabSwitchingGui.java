/**
 * Tab Container Switching With Alternate Buttons
 * 
 * 
 * 
 */
package org.hermes.db;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;




@SuppressWarnings("serial")

public class TabSwitchingGui extends JFrame implements ActionListener {	
	public TabSwitchingGui() {
		super("Switching Tabs");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500,200);
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
		initComponents();
	}
	static Object[] schemaList ;
	/**
	 * Initialise the containers, layout and components.
	 */
	private void initComponents() {
		tabbedPane = new JTabbedPane();
		buttonPanel = new JPanel();
		moveRight = new JButton("Fetch Details");
		movefirst = new JButton(" Cancel");		
				
		// add actionListeners to the buttons // 
		movefirst.addActionListener(this);
		moveRight.addActionListener(this);		
		
		buttonPanel.setLayout( new GridLayout(1,2));
		buttonPanel.add(moveRight);
		buttonPanel.add(movefirst);
		
		addTestPanes();	
		//disable-all
		// add both containers to the JFrame //
		add(tabbedPane, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);		
		
	}
	
	/**
	 * Method just adds some tabs to the panel with
	 * automatically generated names and labels.
	 */
	private void addTestPanes() {
	
			JPanel urlpanel = new JPanel(false);
			JLabel srcurllbl = new JLabel("Source URL ");
			JTextField sourceDBUrl = new JTextField();
			sourceDBUrl.setText("Source URL");
			sourceDBUrl.setSize(10, 3);
			JLabel desturllbl = new JLabel("\n Destination URL ");
			JTextField destDBUrl = new JTextField();
			destDBUrl.setText("Target URL");
			destDBUrl.setSize(10, 3);
			urlpanel.doLayout(); // any layout would do
			urlpanel.setSize(50, 70);
			urlpanel.add(srcurllbl);
			urlpanel.add(sourceDBUrl); 
			urlpanel.setAlignmentX(1);
			urlpanel.add(desturllbl);
			urlpanel.add(destDBUrl); 
			urlpanel.setAlignmentX(2);
			
			tabbedPane.addTab("URL Input " , urlpanel);
			tabbedPane.setSize(100, 100);
			// Second Tab Start
			 final JPanel schemapanel = new JPanel(false);
			 schemapanel.setMaximumSize(new Dimension(200,300));
			JLabel schemalbl = new JLabel("Schema List ");
			schemapanel.setLayout(new FlowLayout()); // any layout would do
			
			//schemaList = new UrlReceiver().fetchSourceSchemaList(sourceDBUrl.getText());
			for(int i = 0; i < schemaList.length; i++)
				   comboBox.addItem(schemaList[i].toString());
			 comboBox.addActionListener(new ActionListener() { 
				   public void actionPerformed(ActionEvent e) {
					 //  schemaList= new UrlReceiver().fetchSourceSchemaList("");
					   for (int i = 0; i < schemaList.length; i++) {
						   JCheckBox chk = new JCheckBox(schemaList[i].toString());
						   schemapanel.add(chk);
					}
				   }
				 });

				schemapanel.add(comboBox);
			
			tabbedPane.addTab("Schema List" , schemapanel);
			// Second Tab end ...
			JPanel metadatapanel = new JPanel(false);
			JLabel metadatalbl = new JLabel("Meta Data List ");
			metadatapanel.setLayout(new FlowLayout()); // any layout would do
			metadatapanel.add(metadatalbl);
			
			tabbedPane.addTab("Meta Data List" , metadatapanel);
	}
	
	
	/**
	 * Implements the desired actions to perform on an event,
	 * in this case. Change the selected tab on a
	 * JTabbedPane.
	 */
	public void actionPerformed(ActionEvent evt) {		
		// check there is more than zero tabs
		if (tabbedPane.getTabCount() == 0) {
			System.err.println("No Tabs In Pane");
			return;
		}
		if (evt.getSource() == movefirst) {
				tabbedPane.setSelectedIndex(0);
		}
		if (evt.getSource() == moveRight) {			
			if(tabbedPane.getSelectedIndex() == tabbedPane.getTabCount()-1)
				tabbedPane.setSelectedIndex(0);
			else 
				tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()+1);	
		}
	}
	
	/*
	 * instantiate an instance of the GUI and show it to the world.
	 */
	public static void main(String[] args) {
		
		new TabSwitchingGui().setVisible(true);
	}
	
	private Container c;
	private JTabbedPane tabbedPane;
	private JPanel buttonPanel;
	private JButton movefirst, moveRight;
	private JComboBox comboBox = new JComboBox();
	
}
