package com.geurimsoft.bokangnew.yearfragment;

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
import com.geurimsoft.bokangnew.view.StatsHeaderAndFooterView;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.StAdapter;
import com.geurimsoft.data.XmlConverter;
import com.geurimsoft.grms.data.GSMonthInOut;
import com.geurimsoft.socket.client.SocketClient;

public class JoomyungYearAmountFragment extends Fragment
{

	private ListView yi_year_amount_listview;
	private TextView yi_year_amount_date;
	private LinearLayout yi_year_amount_header_container, yi_year_amount_loading_indicator, yi_year_amount_loading_fail;

	private YearAmountTask yearAmountTask;

	public JoomyungYearAmountFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.yi_year_amount, container,false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();

		View view = this.getView();
	
		this.yi_year_amount_listview = (ListView)view.findViewById(R.id.yi_year_amount_listview);
		this.yi_year_amount_date = (TextView)view.findViewById(R.id.yi_year_amount_date);
				
		this.yi_year_amount_header_container = (LinearLayout)view.findViewById(R.id.yi_year_amount_header_container);
		
		this.yi_year_amount_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_year_amount_loading_indicator);
		this.yi_year_amount_loading_fail = (LinearLayout)view.findViewById(R.id.yi_year_amount_loading_fail);
		
		this.yi_year_amount_listview.setDividerHeight(0);
		
		makeYearAmountData(AppConfig.DAY_STATS_YEAR);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		yearAmountTask.cancel(true);
	}
	
	private void makeYearAmountData(int _year)
	{

		String dateStr = _year + "년  입출고 현황(단위:루베)";
		yi_year_amount_date.setText(dateStr);
		
		String queryDate = String.valueOf(_year);
		
		yearAmountTask = new YearAmountTask(queryDate);
		yearAmountTask.execute();
		
	}
	
	private void setDisplay(GSMonthInOut data)
	{

		try
		{

			yi_year_amount_header_container.removeAllViews();

			StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(getActivity(), data, AppConfig.STATE_AMOUNT);
			statsHeaderAndFooterView.makeHeaderView(yi_year_amount_header_container);

			StAdapter adapter = new StAdapter(getActivity(), data, AppConfig.STATE_AMOUNT);

			View foot = View.inflate(getActivity(), R.layout.stats_foot, null);
			LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);

			statsHeaderAndFooterView.makeFooterView(footer_layout);
			yi_year_amount_listview.addFooterView(foot);
			yi_year_amount_listview.setAdapter(adapter);

		}
		catch(Exception ex)
		{
			Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : setDisplay() : " + ex.toString());
			return;
		}

	}

	public class YearAmountTask extends AsyncTask<String, String, GSMonthInOut>
	{

		private String queryDate;
		private String responseMessage;
		
		public YearAmountTask(String _queryDate)
		{
			this.queryDate = _queryDate;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			yi_year_amount_loading_indicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected GSMonthInOut doInBackground(String... params)
		{

			String department = AppConfig.SITE_JOOMYUNG + ",";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>YEAR_UNIT</GCType><GCQuery>" + department + queryDate + "</GCQuery></GEURIMSOFT>\n";
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
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
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
				yi_year_amount_loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				yi_year_amount_loading_fail.setVisibility(View.GONE);
				setDisplay(result);
			}
			
			yi_year_amount_loading_indicator.setVisibility(View.GONE);

		}

	}

}
