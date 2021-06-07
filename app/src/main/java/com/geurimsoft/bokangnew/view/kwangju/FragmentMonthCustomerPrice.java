package com.geurimsoft.bokangnew.view.kwangju;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOut;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentMonthCustomerPrice extends Fragment
{

	private LinearLayout yi_month_enterprise_price_income_empty_layout, yi_month_enterprise_price_release_empty_layout, yi_month_enterprise_price_petosa_empty_layout;
	private LinearLayout yi_month_enterprise_price_loading_indicator, yi_month_enterprise_price_loading_fail;

	private TextView yi_month_enterprise_price_date, yi_month_enterprise_price_income_title, yi_month_enterprise_price_release_title, yi_month_enterprise_price_petosa_title;

	private int iYear, iMonth;

	public FragmentMonthCustomerPrice() { }
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.yi_month_enterprise_price, container, false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();
		
		View view = this.getView();
		
		this.yi_month_enterprise_price_income_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_income_empty_layout);
		this.yi_month_enterprise_price_release_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_release_empty_layout);
		this.yi_month_enterprise_price_petosa_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_petosa_empty_layout);
		
		this.yi_month_enterprise_price_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_loading_indicator); 
		this.yi_month_enterprise_price_loading_fail = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_loading_fail);
		
		this.yi_month_enterprise_price_date = (TextView)view.findViewById(R.id.yi_month_enterprise_price_date); 
		this.yi_month_enterprise_price_income_title = (TextView)view.findViewById(R.id.yi_month_enterprise_price_income_title); 
		this.yi_month_enterprise_price_release_title = (TextView)view.findViewById(R.id.yi_month_enterprise_price_release_title); 
		this.yi_month_enterprise_price_petosa_title = (TextView)view.findViewById(R.id.yi_month_enterprise_price_petosa_title);
		
		makeMonthEnterprisepriceData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	private void makeMonthEnterprisepriceData(int _year, int _monthOfYear)
	{

		String functionName = "makeMonthAmountData()";

		try
		{

			String dateStr = _year + "년 " + _monthOfYear + "월  입출고 현황";
//			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + _year + "년 " + _monthOfYear + "월");

			yi_month_enterprise_price_date.setText(dateStr);

			String qryContent = "TotalPrice";

			this.getData(_year, _monthOfYear, qryContent);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}
		
	}

	private void getData(int searchYear, int searchMonth, String qryContent)
	{

		String functionName = "getData()";

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "searchDate : " + searchDate + ", qryContent : " + qryContent);

		iYear = searchYear;
		iMonth = searchMonth;

		String url = GSConfig.API_SERVER_ADDR + "API";
		RequestQueue requestQueue = Volley.newRequestQueue(GSConfig.context);

		StringRequest request = new StringRequest(
				Request.Method.POST,
				url,
				//응답을 잘 받았을 때 이 메소드가 자동으로 호출
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
//						Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "응답 -> " + response);
						parseData(response);
					}
				},
				//에러 발생시 호출될 리스너 객체
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "에러 -> " + error.getMessage());
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String,String>();
				params.put("GSType", "MONTH_CUSTOMER");
				params.put("GSQuery", "{ \"branchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID() + ", \"searchYear\": " + searchYear + ", \"searchMonth\": " + searchMonth + ", \"qryContent\" : \"" + qryContent + "\" }");
				return params;
			}
		};

		request.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
		requestQueue.add(request);

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "요청 보냄.");

	}

	public void parseData(String msg)
	{

		String functionName = "parseData()";
//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + msg);

		try
		{

			GSDailyInOut dio = new GSDailyInOut();

			Gson gson = new Gson();
			GSDailyInOutGroup[] diog = gson.fromJson(msg, GSDailyInOutGroup[].class);

			dio.list = new ArrayList<>(Arrays.asList(diog));

			this.setDisplayData(dio);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}
	
	private void setDisplayData(GSDailyInOut data)
	{

		if (data == null)
			return;

		GSDailyInOutGroup inputGroup = data.findByServiceType(GSConfig.MODE_NAMES[GSConfig.MODE_STOCK]);
		GSDailyInOutGroup outputGroup = data.findByServiceType(GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE]);
		GSDailyInOutGroup slugeGroup = data.findByServiceType(GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA]);

		String unit = getString(R.string.unit_won);

		yi_month_enterprise_price_income_empty_layout.removeAllViews();
		yi_month_enterprise_price_release_empty_layout.removeAllViews();
		yi_month_enterprise_price_petosa_empty_layout.removeAllViews();

		EnterpriseMonthStatsView statsView = new EnterpriseMonthStatsView(getActivity(), GSConfig.CURRENT_BRANCH.getBranchID(), GSConfig.STATE_PRICE, iYear, iMonth);

		statsView.makeStatsView(yi_month_enterprise_price_income_empty_layout, inputGroup, GSConfig.MODE_STOCK, GSConfig.STATE_PRICE);
		yi_month_enterprise_price_income_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_STOCK] + "(" + GSConfig.changeToCommanString(inputGroup.totalUnit) + unit + ")");

		statsView.makeStatsView(yi_month_enterprise_price_release_empty_layout, outputGroup, GSConfig.MODE_RELEASE, GSConfig.STATE_PRICE);
		yi_month_enterprise_price_release_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE] + "(" + GSConfig.changeToCommanString(outputGroup.totalUnit) + unit + ")");

		statsView.makeStatsView(yi_month_enterprise_price_petosa_empty_layout, slugeGroup, GSConfig.MODE_PETOSA, GSConfig.STATE_PRICE);
		yi_month_enterprise_price_petosa_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA] + "(" + GSConfig.changeToCommanString(slugeGroup.totalUnit) + unit + ")");

	}
	
	
	public class MonthEnterprisePriceTask extends AsyncTask<String, String, GSDailyInOut>
	{

		private String queryDate;
		private String responseMessage;
		private String dateStr;
		
		public MonthEnterprisePriceTask(String _queryDate, String _dateStr)
		{
			this.queryDate = _queryDate;
			this.dateStr = _dateStr;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			yi_month_enterprise_price_loading_indicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected GSDailyInOut doInBackground(String... params)
		{

			String branchID = GSConfig.CURRENT_BRANCH.getBranchID() + ",";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>MONTH_CUSTOMER_MONEY</GCType><GCQuery>" + branchID + queryDate + "</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

//			try
//			{
//
//				SocketClient sc = new SocketClient(GSConfig.API_SERVER_ADDR, AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);
//
//				sc.start();
//				sc.join();
//
//				responseMessage = sc.getReturnString();
//
//			}
//			catch (Exception e)
//			{
//				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
//				return null;
//			}

			if (responseMessage == null || responseMessage.equals("") || responseMessage.equals("Fail"))
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : RETURNED XML is null.");
				return null;
			}

			GSDailyInOut data = XmlConverter.parseDaily(responseMessage);
			
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
				return null;
			}

			return data;

		}

//		@Override
//		protected void onPostExecute(GSDailyInOut result)
//		{
//
//			super.onPostExecute(result);
//
//			if(result == null)
//			{
//				yi_month_enterprise_price_loading_fail.setVisibility(View.VISIBLE);
//			}
//			else
//			{
//				yi_month_enterprise_price_loading_fail.setVisibility(View.GONE);
//				setDisplay(result, queryDate);
//			}
//
//			yi_month_enterprise_price_loading_indicator.setVisibility(View.GONE);
//
//		}

	}

}
