package org.hermes.db.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.hermes.db.DBMerger;


public class GUI  extends  JPanel{
	
	private DBMerger merger;
	
	public GUI()
	{
		super(new GridLayout(1, 1));
        
        JTabbedPane tabbedPane = new JTabbedPane();
       // ImageIcon icon = createImageIcon("images/middle.gif");
        
        ConnectionGui connectionGui= new ConnectionGui();
        
        SchemaGUI schemaGUI= new SchemaGUI();
        connectionGui.setSchemaGUI(schemaGUI);
    
        
        tabbedPane.addTab("Connection Details", null, connectionGui,
                "Source & Destination connection details");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
       
        tabbedPane.addTab("Schema Detail", null, schemaGUI,
                "Complete set of details  ");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        tabbedPane.addTab("About", null, new AboutGui(),
        "About the product");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_2);
       
        
        tabbedPane.setEnabledAt(1, false);
        connectionGui.setTab(tabbedPane);
        
        add(tabbedPane);
        
	}

	public void start()
	{
		JTabbedPane tabbedPane = new JTabbedPane();
		

		JComponent connectionsTextComp = makeTextPanel("Connections Details");
		tabbedPane.addTab("Connections Details", null, connectionsTextComp,
		                  "Source & destination Connections Details");
	}
	
	 protected JComponent makeTextPanel(String text) {
	        JPanel panel = new JPanel(false);
	        JLabel filler = new JLabel(text);
	        filler.setHorizontalAlignment(JLabel.CENTER);
	        panel.setLayout(new GridLayout(1, 1));
	        panel.add(filler);
	        return panel;
	    }
	     
	    /** Returns an ImageIcon, or null if the path was invalid. */
	    protected static ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = GUI.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + path);
	            return null;
	        }
	    }
	    
	    /**
	     * 
	     */
	    /**
	     * 
	     */
	    private static void createAndShowGUI() {
	        //Create and set up the window.
	        JFrame frame = new JFrame("Hermes - The ultimate DB Merger");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        
	        //Add content to the window.
	        frame.add(new GUI(), BorderLayout.CENTER);
	        
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	    }
	    
	    public static void main(String[] args) throws InterruptedException {
	    	 SwingUtilities.invokeLater(new Runnable() {
	             public void run() {
	                 //Turn off metal's use of bold fonts
			 		UIManager.put("swing.boldMetal", Boolean.FALSE);
			 		
			 		createAndShowGUI();
	             }
	         });
	    	
	    	 
		}
}
