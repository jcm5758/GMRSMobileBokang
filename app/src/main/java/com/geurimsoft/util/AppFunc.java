package com.geurimsoft.util;

import java.util.ArrayList;

import android.util.Log;

import com.geurimsoft.data.Customer;
import com.geurimsoft.data.Product;
import com.geurimsoft.data.Vehicle;

public class AppFunc 
{

	public static String StringReplace(String str)
	{       
		
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
	    str = str.replaceAll(match, " ");
	      
	    return str;	    
	  
	}
	
	public static ArrayList<String> getStringArrayFromCustomer(ArrayList<Customer> list)
	{
	
		ArrayList<String> resultList = new ArrayList<String>();
		
		for(int idx=0; idx < list.size() ; idx++)
		{
			Customer thisCustomer = list.get(idx);
			resultList.add(thisCustomer.getName());
		}
		
		return resultList;
		
	}
	
	public static ArrayList<String> getStringArrayFromProduct(ArrayList<Product> list)
	{
	
		ArrayList<String> resultList = new ArrayList<String>();
		
		for(int idx=0; idx < list.size() ; idx++){
			Product thisProduct = list.get(idx);
			resultList.add(thisProduct.getName());
		}
		
		return resultList;
		
	}
	
	public static ArrayList<String> getStringArrayFromVehicle(ArrayList<Vehicle> list)
	{
	
		ArrayList<String> resultList = new ArrayList<String>();
		
		for(int idx=0; idx < list.size() ; idx++)
		{
			Vehicle thisProduct = list.get(idx);
			Log.e("aa","name-"+thisProduct.getVehiclenum());
			resultList.add(thisProduct.getVehiclenum());
		}
		
		return resultList;
		
	}
	
}
