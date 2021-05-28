package com.geurimsoft.bokangnew.view.yearfragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.view.etc.StatsHeaderAndFooterView;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.StAdapter;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.data.GSMonthInOut;
import com.geurimsoft.bokangnew.client.SocketClient;

public class JoomyungYearPriceFragment extends Fragment
{

	private ListView yi_year_price_listview;
	private TextView yi_year_price_date;
	private LinearLayout yi_year_price_header_container, yi_year_price_loading_indicator, yi_year_price_loading_fail;

	private YearPriceTask yearPriceTask;

	public JoomyungYearPriceFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.yi_year_price, container,false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();

		View view = this.getView();
	
		this.yi_year_price_listview = (ListView)view.findViewById(R.id.yi_year_price_listview);
		this.yi_year_price_date = (TextView)view.findViewById(R.id.yi_year_price_date);
				
		this.yi_year_price_header_container = (LinearLayout)view.findViewById(R.id.yi_year_price_header_container);
		
		this.yi_year_price_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_year_price_loading_indicator);
		this.yi_year_price_loading_fail = (LinearLayout)view.findViewById(R.id.yi_year_price_loading_fail);
		
		this.yi_year_price_listview.setDividerHeight(0);
		
		makeYearPriceData(GSConfig.DAY_STATS_YEAR);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		yearPriceTask.cancel(true);
	}
	
	private void makeYearPriceData(int _year)
	{

		String dateStr = _year + "년  입출고 현황(단위:천원)";
		yi_year_price_date.setText(dateStr);
		
		String queryDate = String.valueOf(_year);
		
		yearPriceTask = new YearPriceTask(queryDate);
		yearPriceTask.execute();
		
	}
	
	private void setDisplay(GSMonthInOut data)
	{
		
		yi_year_price_header_container.removeAllViews();

		StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(getActivity(), data, AppConfig.STATE_PRICE);
		statsHeaderAndFooterView.makeHeaderView(yi_year_price_header_container);

		StAdapter adapter = new StAdapter(getActivity(), data, AppConfig.STATE_PRICE);

		View foot = View.inflate(getActivity(), R.layout.stats_foot, null);
		LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);

		statsHeaderAndFooterView.makeFooterView(footer_layout);
		yi_year_price_listview.addFooterView(foot);

		yi_year_price_listview.setAdapter(adapter);

	}
	
	public class YearPriceTask extends AsyncTask<String, String, GSMonthInOut>
	{

		private String queryDate;
		private String responseMessage;

		public YearPriceTask(String _queryDate)
		{
			this.queryDate = _queryDate;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			yi_year_price_loading_indicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected GSMonthInOut doInBackground(String... params)
		{

			String department = AppConfig.SITE_JOOMYUNG + ",";
			String message =  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>YEAR_MONEY</GCType><GCQuery>" + department + queryDate + "</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

			try
			{

				SocketClient sc = new SocketClient(AppConfig.SERVER_IP, AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);

				sc.start();
				sc.join();

				responseMessage = sc.getReturnString();

			}
			catch (Exception ex)
			{
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + ex.toString());
				return null;
			}

			if (responseMessage == null || responseMessage.equals("Fail"))
			{
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : doInBackground() : Returend XML is null.");
				return null;
			}

			GSMonthInOut data = XmlConverter.parseMonth(responseMessage);

			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
			}

			return data;

		}

		@Override
		protected void onPostExecute(GSMonthInOut result)
		{

			super.onPostExecute(result);
			
			if(result == null)
			{
				yi_year_price_loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				yi_year_price_loading_fail.setVisibility(View.GONE);
				setDisplay(result);
			}
			
			yi_year_price_loading_indicator.setVisibility(View.GONE);

		}

	}
}
