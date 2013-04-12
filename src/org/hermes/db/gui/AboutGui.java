package org.hermes.db.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutGui extends  JPanel {
	
	public AboutGui()
	{
		 this.setLayout(null);
		displayItems();
	}
	
	private void displayItems()
	{
		JLabel hermesLable=new JLabel("Hermes - Its a Oracle Database Schema Merger. It checks & creates the consolidated SQLs based on two different Databases");
		
		JLabel creatorsHeaderLable=new JLabel("Creators :");
		
		JLabel creatorsLable=new JLabel("Selvaganapathi R (selvaganapathi@intuit.com)");
		
		JLabel creatorsLable2=new JLabel("A.C, Suresh (Suresh_A.C@intuit.com)");
		
		hermesLable.setBounds(20, 20, 800, 50);
		
		creatorsHeaderLable.setBounds(20, 100, 100, 50);
		
		creatorsLable.setBounds(120, 130, 500, 50);
		
		creatorsLable2.setBounds(120, 150, 500, 50);
		
		add(hermesLable);
		add(creatorsHeaderLable);
		add(creatorsLable);
		add(creatorsLable2);
		
	}

}
