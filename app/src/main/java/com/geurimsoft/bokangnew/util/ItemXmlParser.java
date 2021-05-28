package com.geurimsoft.bokangnew.util;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.geurimsoft.bokangnew.data.CCTV;
import com.geurimsoft.bokangnew.data.Customer;
import com.geurimsoft.bokangnew.data.DownloadInterface;
import com.geurimsoft.bokangnew.data.Place;
import com.geurimsoft.bokangnew.data.Product;
import com.geurimsoft.bokangnew.data.SearchResult;
import com.geurimsoft.bokangnew.data.Vehicle;
 
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
 
public class ItemXmlParser {

	public DownloadInterface downloadInterface;
	public int URL_TYPE ;
	
	
	public static void getSearchResultList(Context context, DownloadInterface downloadInterface, String contentUrl, int URL_TYPE)
	{

		ItemXmlParser itemXmlParser = new ItemXmlParser();
 		itemXmlParser.downloadInterface = downloadInterface;
 		itemXmlParser.URL_TYPE = URL_TYPE;

 		Log.i("XML","in checkTourCategory  =  ");

 		DownloadFile.getXMLData(context, contentUrl, itemXmlParser);

	}

	public void getCustomerList(Context context, String resultStr)
	{

		Customer thisCustomer = new Customer();
		ArrayList<Customer>  sResultList = new ArrayList<Customer>();
		sResultList.add(new Customer());
		
		/**
		 *  <RESPONSE>
<ID>5</ID>
<NAME>�׸�����</NAME>
</RESPONSE>
		 */
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser ps = factory.newPullParser();
			ps.setInput(new StringReader (resultStr));
				 
			//	ps.setInput(is, "UTF-8");
				
			int et 			= ps.getEventType();
			String tag  	= "";
				
			boolean inReponse = false;
			boolean inId = false;
			boolean inName = false;
 
				
			if(ps == null){
				AppLog.writeLog("xml"," before ps is null");				
			}else{
				AppLog.writeLog("xml"," before ps.getName()" + ps.getName());
			}
			while (et != XmlPullParser.END_DOCUMENT) {
				tag = ps.getName();		
				AppLog.writeLog("xml"," while -ps.getName()" + ps.getName());
				AppLog.writeLog("xml"," while -ps.getText()" + ps.getText());
				if(tag == null){
					tag="";
				}
				if (et == XmlPullParser.START_TAG) {
					if(tag.equalsIgnoreCase("RESPONSE")){
						inReponse = true;
						thisCustomer = new Customer();
					}
					if(inReponse &&tag.equalsIgnoreCase("ID")){
						inId = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("Name")){
						inName = true;
					}
					 
						 			
				}else if(et == XmlPullParser.END_TAG) {
					if(tag.equalsIgnoreCase("RESPONSE")){
						inReponse = false;
						sResultList.add(thisCustomer);
					}
					if(inReponse &&tag.equalsIgnoreCase("ID")){
						inId = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("Name")){
						inName = false;
					} 

				}else if(et == XmlPullParser.TEXT) {
	 				if(inId){
	 					thisCustomer.setId(ps.getText());
	 				}
	 				if(inName){
	 					thisCustomer.setName(ps.getText());
	 				} 
				}	
				et = ps.next();
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}
		
		downloadInterface.downloadComplete(true, resultStr, sResultList, this.URL_TYPE);
		
		
	}
	public void getProductList(Context context, String resultStr){
		Product thisProduct = new Product();
		ArrayList<Product>  sResultList = new ArrayList<Product>();
		sResultList.add(new Product());

		/**
		 *  <RESPONSE>
<ID>5</ID>
<NAME>�׸�����</NAME>
</RESPONSE>
		 */
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser ps = factory.newPullParser();
			ps.setInput(new StringReader (resultStr));
				 
			//	ps.setInput(is, "UTF-8");
				
			int et 			= ps.getEventType();
			String tag  	= "";
				
			boolean inReponse = false;
			boolean inId = false;
			boolean inName = false;
 
				
			if(ps == null){
				AppLog.writeLog("xml"," before ps is null");				
			}else{
				AppLog.writeLog("xml"," before ps.getName()" + ps.getName());
			}
			while (et != XmlPullParser.END_DOCUMENT) {
				tag = ps.getName();		
				AppLog.writeLog("xml"," while -ps.getName()" + ps.getName());
				AppLog.writeLog("xml"," while -ps.getText()" + ps.getText());
				if(tag == null){
					tag="";
				}
				if (et == XmlPullParser.START_TAG) {
					if(tag.equalsIgnoreCase("RESPONSE")){
						inReponse = true;
						thisProduct = new Product();
					}
					if(inReponse &&tag.equalsIgnoreCase("ID")){
						inId = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("Name")){
						inName = true;
					}
					 
						 			
				}else if(et == XmlPullParser.END_TAG) {
					if(tag.equalsIgnoreCase("RESPONSE")){
						inReponse = false;
						sResultList.add(thisProduct);
					}
					if(inReponse &&tag.equalsIgnoreCase("ID")){
						inId = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("Name")){
						inName = false;
					} 

				}else if(et == XmlPullParser.TEXT) {
	 				if(inId){
	 					thisProduct.setId(ps.getText());
	 				}
	 				if(inName){
	 					thisProduct.setName(ps.getText());
	 				} 
				}	
				et = ps.next();
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}
		
		downloadInterface.downloadComplete(true, resultStr, sResultList, this.URL_TYPE);
		
		
	}
	
	public void getVehicleList(Context context, String resultStr){
		Vehicle thisProduct = new Vehicle();
		ArrayList<Vehicle>  sResultList = new ArrayList<Vehicle>();
		sResultList.add(new Vehicle());

		/**
		 *  <RESPONSE>
<ID>5</ID>
<NAME>�׸�����</NAME>
</RESPONSE>
		 */
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser ps = factory.newPullParser();
			ps.setInput(new StringReader (resultStr));
				 
			//	ps.setInput(is, "UTF-8");
				
			int et 			= ps.getEventType();
			String tag  	= "";
				
			boolean inReponse = false;
			boolean inId = false;
			boolean inName = false;
			boolean invehiclenum =false;
				
			if(ps == null){
				AppLog.writeLog("xml"," before ps is null");				
			}else{
				AppLog.writeLog("xml"," before ps.getName()" + ps.getName());
			}
			while (et != XmlPullParser.END_DOCUMENT) {
				tag = ps.getName();		
				AppLog.writeLog("xml"," while -ps.getName()" + ps.getName());
				AppLog.writeLog("xml"," while -ps.getText()" + ps.getText());
				if(tag == null){
					tag="";
				}
				if (et == XmlPullParser.START_TAG) {
					if(tag.equalsIgnoreCase("RESPONSE")){
						inReponse = true;
						thisProduct = new Vehicle();
					}
					if(inReponse &&tag.equalsIgnoreCase("ID")){
						inId = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("Name")){
						inName = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("vehiclenum")){
						invehiclenum = true;
					}
						 			
				}else if(et == XmlPullParser.END_TAG) {
					if(tag.equalsIgnoreCase("RESPONSE")){
						inReponse = false;
						sResultList.add(thisProduct);
					}
					if(inReponse &&tag.equalsIgnoreCase("ID")){
						inId = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("Name")){
						inName = false;
					} 
					if(inReponse &&tag.equalsIgnoreCase("vehiclenum")){
						invehiclenum = false;
					}

				}else if(et == XmlPullParser.TEXT) {
	 				if(inId){
	 					thisProduct.setId(ps.getText());
	 				}
	 				if(inName){
	 					thisProduct.setName(ps.getText());
	 				} 
	 				if(invehiclenum){
	 					thisProduct.setVehiclenum(ps.getText());
	 				} 
				}	
				et = ps.next();
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}
		
		downloadInterface.downloadComplete(true, resultStr, sResultList, this.URL_TYPE);
		
		
	}
	public void getResultList(Context context, String resultStr){
	//	Toast.makeText(context, resultStr, Toast.LENGTH_SHORT).show();		
		Log.i("RESULT","in checkTourCategory  =  "+resultStr);
		
		SearchResult sResult = new SearchResult();
		ArrayList<SearchResult>  sResultList = new ArrayList<SearchResult>();
		/*
		<RESPONSE>
			<ID>65</ID>
			<Type>0</Type>
			<VehicleNum>02�� 5881</VehicleNum>
			<Product>40mm</Product>
			<Customer>�׸�����</Customer>
			<Unit>15.0</Unit>
			<ServiceDate>20140218</ServiceDate>
			<ServiceHour>163756</ServiceHour>
			</RESPONSE>
 
	*/
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser ps = factory.newPullParser();
			ps.setInput(new StringReader (resultStr));
				 
			//	ps.setInput(is, "UTF-8");
				
			int et 			= ps.getEventType();
			String tag  	= "";
				
			boolean inReponse = false;
			boolean inId = false;
			boolean inType = false;
			boolean inVehicleNum = false; 
			boolean inProduct = false; 
			boolean inCustomer = false; 
			boolean inUnit = false; 
			boolean inServiceDate = false; 
			boolean inServiceHour = false; 
				
			if(ps == null){
				AppLog.writeLog("xml"," before ps is null");				
			}else{
				AppLog.writeLog("xml"," before ps.getName()" + ps.getName());
			}
			while (et != XmlPullParser.END_DOCUMENT) {
				tag = ps.getName();		
				AppLog.writeLog("xml"," while -ps.getName()" + ps.getName());
				AppLog.writeLog("xml"," while -ps.getText()" + ps.getText());
				if(tag == null){
					tag="";
				}
				if (et == XmlPullParser.START_TAG) {
					if(tag.equalsIgnoreCase("RESPONSE")){
						inReponse = true;
						sResult = new SearchResult();
					}
					if(inReponse &&tag.equalsIgnoreCase("ID")){
						inId = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("Type")){
						inType = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("VehicleNum")){
						inVehicleNum = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("Product")){
						inProduct = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("Customer")){
						inCustomer = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("Unit")){
						inUnit = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("ServiceDate")){
						inServiceDate = true;
					}
					if(inReponse &&tag.equalsIgnoreCase("ServiceHour")){
						inServiceHour = true;
					}
						 			
				}else if(et == XmlPullParser.END_TAG) {
					if(tag.equalsIgnoreCase("RESPONSE")){
						inReponse = false;
						sResultList.add(sResult);
					}
					if(inReponse &&tag.equalsIgnoreCase("ID")){
						inId = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("Type")){
						inType = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("VehicleNum")){
						inVehicleNum = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("Product")){
						inProduct = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("Customer")){
						inCustomer = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("Unit")){
						inUnit = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("ServiceDate")){
						inServiceDate = false;
					}
					if(inReponse &&tag.equalsIgnoreCase("ServiceHour")){
						inServiceHour = false;
					}

				}else if(et == XmlPullParser.TEXT) {
	 				if(inId){
	 					sResult.setId(ps.getText());
	 				}
	 				if(inType){
	 					sResult.setType(ps.getText());
	 				}
	 				if(inVehicleNum){
	 					sResult.setVehicleNum(ps.getText());
	 				}
	 				if(inProduct){
	 					sResult.setProduct(ps.getText());
	 				}
	 				if(inCustomer){
	 					sResult.setCustomer(ps.getText());
	 				}
	 				if(inUnit){
	 					sResult.setUnit(ps.getText());
	 				}
	 				if(inServiceDate){
	 					sResult.setServiceDate(ps.getText());
	 					
	 				}
	 				if(inServiceHour){
	 					sResult.setServiceHour(ps.getText());
	 				}
	 				
				}	
				et = ps.next();
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}
		
		downloadInterface.downloadComplete(true, resultStr, sResultList, this.URL_TYPE);
	}
	
	private static AssetManager assetManager ;
	 
	public static ArrayList<CCTV> getCCTVList(Context context, String cctvXmlListFile){
		assetManager=context.getAssets();
		CCTV thisCCTV = new CCTV();
		ArrayList<CCTV>  cctvList = new ArrayList<CCTV>();

		InputStream is = null;
		try{
			is = assetManager.open(cctvXmlListFile);			
		}catch(Exception e){
			AppLog.writeLog("error","e2 :"+e);				
		}	
		/**
		 *     
	    <cctv>
			<name><![CDATA[1번카메라]]></name>        
        	<cctvurl>rtsp://116.124.245.42:1935/live/ipcamera01.stream</cctvurl>
    	</cctv>
		 */
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser ps = factory.newPullParser();
			ps.setInput(is, "UTF-8");
			
			int et 			= ps.getEventType();
			String tag  	= "";
			
			boolean inCCTV = false;
			boolean inName = false;
			boolean inCctvUrl = false; 
			
			if(ps == null){
				AppLog.writeLog("xml"," before ps is null");				
			}else{
				AppLog.writeLog("xml"," before ps.getName()" + ps.getName());
			}
			while (et != XmlPullParser.END_DOCUMENT) {
				tag = ps.getName();		
				AppLog.writeLog("xml"," while -ps.getName()" + ps.getName());
				AppLog.writeLog("xml"," while -ps.getText()" + ps.getText());
				if(tag == null){
					tag="";
				}
				if (et == XmlPullParser.START_TAG) {
					if(tag.equals("cctv")){
						inCCTV = true;
						thisCCTV = new CCTV();
					}
					if(inCCTV &&tag.equals("name")){
						inName = true;
					}
					if(inCCTV &&tag.equals("cctvurl")){
						inCctvUrl = true;
					}
					 
					 			
				}else if(et == XmlPullParser.END_TAG) {
					if(tag.equals("cctv")){
						inCCTV = false;
						cctvList.add(thisCCTV);
					}
					if(inCCTV && tag.equals("name")){
						inName = false;
					}
					if(inCCTV &&tag.equals("cctvurl")){
						inCctvUrl = false;
					}
				}else if(et == XmlPullParser.TEXT) {
 					if(inName){ 
 						thisCCTV.setCctvName(ps.getText());			 
					}
 					if(inCctvUrl){ 		
 						thisCCTV.setCctvUrl(ps.getText());
					}
				 
				}	
				et = ps.next();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cctvList;
		
	}
	public static ArrayList<Place> getPlaceList(Context context, String placeListFile){
		assetManager=context.getAssets();
		Place thisPlace = new Place();
		ArrayList<Place>  placeList = new ArrayList<Place>();
		
		
		InputStream is = null;
		try{
			is = assetManager.open(placeListFile);			
		}catch(Exception e){
			AppLog.writeLog("error","e2 :"+e);				
		}	
		
		
		/**
		 *     
	<place>
		<name><![CDATA[A현장]]></name>        
        <cctvlistfile>cctvlist.xml</cctvlistfile>
    </place>
		 */
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser ps = factory.newPullParser();
			ps.setInput(is, "UTF-8");
			
			int et 			= ps.getEventType();
			String tag  	= "";
			
			boolean inPlace = false;
			boolean inName = false;
			boolean inCCTV = false; 
			
			if(ps == null){
				AppLog.writeLog("xml"," before ps is null");				
			}else{
				AppLog.writeLog("xml"," before ps.getName()" + ps.getName());
			}
			while (et != XmlPullParser.END_DOCUMENT) {
				tag = ps.getName();		
				AppLog.writeLog("xml"," while -ps.getName()" + ps.getName());
				AppLog.writeLog("xml"," while -ps.getText()" + ps.getText());
				if(tag == null){
					tag="";
				}
				if (et == XmlPullParser.START_TAG) {
					if(tag.equals("place")){
						inPlace = true;
						thisPlace = new Place();
					}
					if(inPlace &&tag.equals("name")){
						inName = true;
					}
					if(inPlace &&tag.equals("cctvlistfile")){
						inCCTV = true;
					}
					 
					 			
				}else if(et == XmlPullParser.END_TAG) {
					if(tag.equals("place")){
						inPlace = false;
						placeList.add(thisPlace);
					}
					if(inPlace && tag.equals("name")){
						inName = false;
					}
					if(inPlace &&tag.equals("cctvlistfile")){
						inCCTV = false;
					}
				}else if(et == XmlPullParser.TEXT) {
 					if(inName){ 
 						thisPlace.setPlaceName(ps.getText());			 
					}
 					if(inCCTV){ 		
 						thisPlace.setCctvListFile(ps.getText());
					}
				 
				}	
				et = ps.next();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return placeList;
	
	}

	 
	
}
