package org.hermes.db.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hermes.db.Connection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ConnectionsXmlManager {

	File connectionsXmlFile;
	public void init() throws IOException
	{
		createBaseXml();
	}
	
	private void createBaseXml() throws IOException
	{
		File dir=new File(System.getProperty("user.home")+"/hermes");
		
		if(!dir.exists())
         dir.mkdir();
		
		 connectionsXmlFile=new File(dir,"connections.xml");
		 
		if(!connectionsXmlFile.exists())
		{
			connectionsXmlFile.createNewFile();
			FileWriter writer=new FileWriter(connectionsXmlFile);
			writer.write(getBaseXmlContent());
			writer.flush();
			writer.close();
		}
		
	}
	private String getBaseXmlContent() throws IOException
	{
		StringBuilder builder=new StringBuilder();
		builder.append("<?xml version=\"1.0\"?>");
		builder.append("\n");
		
		builder.append("<connections>");
		
		builder.append("\n");
		builder.append("</connections>");
		
		return builder.toString();
	}
	
	public List<Connection> getConnections() 
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			if(null==connectionsXmlFile)
				return null;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(connectionsXmlFile);
			NodeList nList = doc.getElementsByTagName("connection");
			List<Connection> connections=new ArrayList<Connection>();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Connection con=new Connection();
					con.setName(eElement.getAttribute("name"));
					con.setUsername(eElement.getAttribute("username"));
					con.setPassword(eElement.getAttribute("password"));
					con.setUrl( eElement.getTextContent());
					connections.add(con);
				}
			}
			
			return connections;
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void addConnection(Connection con)
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(connectionsXmlFile);
			
			Node root= doc.getElementsByTagName("connections").item(0);
			Element connection = doc.createElement("connection");
			connection.setAttribute("name", con.getName());
			connection.setAttribute("username", con.getUsername());
			connection.setAttribute("password", con.getPassword());
			connection.setTextContent(con.getUrl());
			root.appendChild(connection);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(connectionsXmlFile);
	 
			
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Connection getConnection(String name)
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(connectionsXmlFile);
			
			
			NodeList nList = doc.getElementsByTagName("connection");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					if(name.equalsIgnoreCase(eElement.getAttribute("name")))
					{
						Connection con=new Connection();
						con.setUsername(eElement.getAttribute("username"));
						con.setUrl(eElement.getTextContent());
						con.setName(name);
						con.setPassword(eElement.getAttribute("password"));
						
						return con;
					}
				}
			}
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public void deleteConnection(String name)
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(connectionsXmlFile);
			
			NodeList nList = doc.getElementsByTagName("connection");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Connection con=new Connection();
					con.setName(eElement.getAttribute("name"));
					
					if(name.equalsIgnoreCase(con.getName()))
					{
						Node parent=eElement.getParentNode();
						parent.removeChild(eElement);
					}
				}
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(connectionsXmlFile);
	 
			
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updateConnection(Connection con)
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(connectionsXmlFile);
			
			
			NodeList nList = doc.getElementsByTagName("connection");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					if(con.getName().equalsIgnoreCase(eElement.getAttribute("name")))
					{
						eElement.setAttribute("username", con.getUsername());
						eElement.setAttribute("password", con.getPassword());
						eElement.setTextContent(con.getUrl());
					}
				}
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(connectionsXmlFile);
	 
			
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
