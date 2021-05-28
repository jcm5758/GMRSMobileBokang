package com.geurimsoft.util;

import com.geurimsoft.conf.AppConfig;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DownloadFile 
{
	
 	private static final String TAG = "File"; 
 
	public static void getXMLData(Context currContext, String url,  ItemXmlParser itemXmlParser)
	{
		NetworkDownloadTask ndl = new NetworkDownloadTask(currContext, url,itemXmlParser);
		ndl.execute(100);
	}
	
	public static class NetworkDownloadTask extends AsyncTask<Integer, Integer, Integer>
	{ 
	
		private Context currContext;
		private String network_url = "";
		private String saveSTR = "";
		ProgressDialog fileDownloadProgressDialog  ;
		private Integer mValue = 10;
		ItemXmlParser itemXmlParser ;
		int xmlType ; 
	
		public NetworkDownloadTask(Context context, String url , ItemXmlParser itemXmlParser)
		{
		
			this.currContext = context;
			this.network_url = url;
 			
			fileDownloadProgressDialog = new ProgressDialog(currContext);
			fileDownloadProgressDialog.setCancelable(true);
			fileDownloadProgressDialog.setMessage("Downloading....");
			fileDownloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
 
			this.itemXmlParser = itemXmlParser;
			
		}
		
    	@Override
		protected void onPreExecute()
    	{
    		fileDownloadProgressDialog.show();
    	}
    	
    	@Override
		protected Integer doInBackground(Integer... arg0) 
    	{
    		/*SocketClient sc = new SocketClient(this.network_url);
    		saveSTR = sc.connect(); 		
    		return mValue;*/
    		return null;
    	}
    	
    	@Override
		protected void onProgressUpdate(Integer... progress)
    	{
    		fileDownloadProgressDialog.incrementProgressBy(progress[0]);
    	}
    	
    	@Override
		protected void onPostExecute(Integer result)
    	{
    	
    		fileDownloadProgressDialog.dismiss();
    		
    		if( this.itemXmlParser.URL_TYPE == AppConfig.URL_TYPE_SEARCH)
    		{
    			this.itemXmlParser.getResultList(currContext, saveSTR);
    		}
    		else if( this.itemXmlParser.URL_TYPE == AppConfig.URL_TYPE_CUSTOMER)
    		{
    			this.itemXmlParser.getCustomerList( currContext, saveSTR);
    		}
    		else if( this.itemXmlParser.URL_TYPE == AppConfig.URL_TYPE_PRODUCT)
    		{
    			this.itemXmlParser.getProductList( currContext, saveSTR);
    		}
    		else if( this.itemXmlParser.URL_TYPE == AppConfig.URL_TYPE_VEHICLE)
    		{
    			this.itemXmlParser.getVehicleList(  currContext, saveSTR);
    		}
    		else if( this.itemXmlParser.URL_TYPE == AppConfig.URL_TYPE_USER)
    		{    			
    		}
    		
    	}
    	
    	@Override
		protected void onCancelled()
    	{
    		fileDownloadProgressDialog.dismiss();
    	}
    	
	 }  
	
}
