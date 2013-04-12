package org.hermes.db;

public class Utils {

	private static final String[] Q = new String[]{"", "K", "M", "G", "T", "P", "E"};

	public static String getAsString(long bytes)
	{
	    for (int i = 6; i > 0; i--)
	    {
	        double step = Math.pow(1024, i);
	        if (bytes > step) return String.format("%3.0f %s", bytes / step, Q[i]);
	    }
	    return Long.toString(bytes);
	}
}
