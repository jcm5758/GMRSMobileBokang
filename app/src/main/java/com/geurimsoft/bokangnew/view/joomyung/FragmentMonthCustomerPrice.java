package com.geurimsoft.bokangnew.view.joomyung;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.view.etc.EnterpriseMonthStatsView;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOut;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutGroup;
import com.geurimsoft.bokangnew.client.SocketClient;

public class FragmentMonthCustomerPrice extends Fragment
{

	private LinearLayout yi_month_enterprise_price_income_empty_layout, yi_month_enterprise_price_release_empty_layout, yi_month_enterprise_price_petosa_empty_layout;
	private LinearLayout yi_month_enterprise_price_loading_indicator, yi_month_enterprise_price_loading_fail;

	private TextView yi_month_enterprise_price_date, yi_month_enterprise_price_income_title, yi_month_enterprise_price_release_title, yi_month_enterprise_price_petosa_title;

	private MonthEnterprisePriceTask monthEnterprisePriceTask;

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
		monthEnterprisePriceTask.cancel(true);
	}
	
	private void makeMonthEnterprisepriceData(int _year, int _monthOfYear)
	{

		String dateStr = _year + "년 " + _monthOfYear + "월  입출고 현황";
		this.yi_month_enterprise_price_date.setText(dateStr);

		String queryDate = _year + ",";

		if (_monthOfYear < 10)
			queryDate += "0" + _monthOfYear;
		else
			queryDate += _monthOfYear;
		
		monthEnterprisePriceTask = new MonthEnterprisePriceTask(queryDate, dateStr);
		monthEnterprisePriceTask.execute();
		
	}
	
	private void setDisplay(GSDailyInOut data, String _date)
	{

		if (data == null)
			return;

		GSDailyInOutGroup inputGroup = data.findByServiceType(AppConfig.MODE_NAMES[AppConfig.MODE_STOCK]);
		GSDailyInOutGroup outputGroup = data.findByServiceType(AppConfig.MODE_NAMES[AppConfig.MODE_RELEASE]);
		GSDailyInOutGroup slugeGroup = data.findByServiceType(AppConfig.MODE_NAMES[AppConfig.MODE_PETOSA]);

		String unit = getString(R.string.unit_won);

		yi_month_enterprise_price_income_empty_layout.removeAllViews();
		yi_month_enterprise_price_release_empty_layout.removeAllViews();
		yi_month_enterprise_price_petosa_empty_layout.removeAllViews();

		EnterpriseMonthStatsView statsView = new EnterpriseMonthStatsView(getActivity(), AppConfig.SITE_JOOMYUNG, AppConfig.STATE_PRICE, _date);

		statsView.makeStatsView(yi_month_enterprise_price_income_empty_layout, inputGroup, AppConfig.MODE_STOCK, AppConfig.STATE_PRICE);
		yi_month_enterprise_price_income_title.setText(AppConfig.MODE_NAMES[AppConfig.MODE_STOCK] + "(" + GSConfig.changeToCommanString(inputGroup.totalUnit) + unit + ")");

		statsView.makeStatsView(yi_month_enterprise_price_release_empty_layout, outputGroup, AppConfig.MODE_RELEASE, AppConfig.STATE_PRICE);
		yi_month_enterprise_price_release_title.setText(AppConfig.MODE_NAMES[AppConfig.MODE_RELEASE] + "(" + GSConfig.changeToCommanString(outputGroup.totalUnit) + unit + ")");

		statsView.makeStatsView(yi_month_enterprise_price_petosa_empty_layout, slugeGroup, AppConfig.MODE_PETOSA, AppConfig.STATE_PRICE);
		yi_month_enterprise_price_petosa_title.setText(AppConfig.MODE_NAMES[AppConfig.MODE_PETOSA] + "(" + GSConfig.changeToCommanString(slugeGroup.totalUnit) + unit + ")");

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

			String branchID = AppConfig.SITE_JOOMYUNG + ",";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>MONTH_CUSTOMER_MONEY</GCType><GCQuery>" + branchID + queryDate + "</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

			try
			{

				SocketClient sc = new SocketClient(AppConfig.SERVER_IP, AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);

				sc.start();
				sc.join();

				responseMessage = sc.getReturnString();

			}
			catch (Exception e)
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
				return null;
			}

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

		@Override
		protected void onPostExecute(GSDailyInOut result)
		{

			super.onPostExecute(result);
			
			if(result == null)
			{
				yi_month_enterprise_price_loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				yi_month_enterprise_price_loading_fail.setVisibility(View.GONE);
				setDisplay(result, queryDate);
			}
			
			yi_month_enterprise_price_loading_indicator.setVisibility(View.GONE);

		}

	}

}
