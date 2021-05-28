package com.geurimsoft.util;
import android.util.Log;

public class AppLog 
{

	public static void writeLog(String title, String logdata)
	{
		Log.i(title, logdata);
	}
	
	public static void writeError(String title, String logdata)
	{
	 	Log.e(title, logdata);
	}
	
}
