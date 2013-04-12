package org.hermes.db.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	private static final String name="hermes.log";
	
	private static final FileWriter writer=getWriter();
	
	private static FileWriter getWriter() 
	{
		File file=new File(name);
		
		FileWriter writer=null;
		try {
			writer = new FileWriter(file,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return writer;
	}
	
	public static void shutdown()
	{
		if(null!=writer)
		  {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	}
	
	public static void log(String log)
	{
	  if(null!=writer)
	  {
		  try {
		   System.out.println(log);
			writer.write(log);
			writer.write("\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	  }
		  
	}
}
