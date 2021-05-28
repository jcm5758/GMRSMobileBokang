package com.geurimsoft.bokangnew.view.monthfragment;

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

public class JoomyungMonthPriceFragment extends Fragment
{

	private ListView yi_month_price_listview;
	private TextView yi_month_price_date;
	private String dateStr;
	private LinearLayout yi_month_price_header_container, yi_month_price_loading_indicator, yi_month_price_loading_fail;

	private MonthPriceTask monthpriceTask;

	public JoomyungMonthPriceFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.yi_month_price, container,false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();

		View view = this.getView();
	
		this.yi_month_price_listview = (ListView)view.findViewById(R.id.yi_month_price_listview);
		this.yi_month_price_date = (TextView)view.findViewById(R.id.yi_month_price_date);
				
		this.yi_month_price_header_container = (LinearLayout)view.findViewById(R.id.yi_month_price_header_container);
		
		this.yi_month_price_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_month_price_loading_indicator);
		this.yi_month_price_loading_fail = (LinearLayout)view.findViewById(R.id.yi_month_price_loading_fail);
		
		this.yi_month_price_listview.setDividerHeight(0);
		
		makeMonthpriceData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		monthpriceTask.cancel(true);
	}

	/**
	 * 데이터 질의문 생성 및 요청
	 * @param _year
	 * @param _monthOfYear
	 */
	private void makeMonthpriceData(int _year, int _monthOfYear)
	{

		dateStr = _year + "년 " + _monthOfYear + "월  입출고 현황(단위:천원)";
		yi_month_price_date.setText(dateStr);

		String queryDate = _year + ",";

		if (_monthOfYear < 10)
			queryDate += "0" + _monthOfYear;
		else
			queryDate += _monthOfYear;
		
		monthpriceTask = new MonthPriceTask(queryDate);
		monthpriceTask.execute();
		
	}

	/**
	 * 조회 결과 데이터의 뷰 설정
	 * @param data
	 */
	private void setDisplay(GSMonthInOut data)
	{
		
		yi_month_price_header_container.removeAllViews();

		StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(getActivity(), data, AppConfig.STATE_PRICE);
		statsHeaderAndFooterView.makeHeaderView(yi_month_price_header_container);

		StAdapter adapter = new StAdapter(getActivity(), data, AppConfig.STATE_PRICE);

		View foot = View.inflate(getActivity(), R.layout.stats_foot, null);
		LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);

		statsHeaderAndFooterView.makeFooterView(footer_layout);
		yi_month_price_listview.addFooterView(foot);
		yi_month_price_listview.setAdapter(adapter);

	}

	/**
	 * 데이터 요청 TASK
	 */
	public class MonthPriceTask extends AsyncTask<String, String, GSMonthInOut>
	{

		private String queryDate;
		private String responseMessage;
		
		public MonthPriceTask(String _queryDate)
		{
			this.queryDate = _queryDate;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			yi_month_price_loading_indicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected GSMonthInOut doInBackground(String... params)
		{

			String department = "3,";
			String message ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>MONTH_MONEY</GCType><GCQuery>" + department + queryDate + "</GCQuery></GEURIMSOFT>\n";
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

			if (responseMessage == null || responseMessage.equals("Fail"))
			{
				Log.d(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : Returned xml is null.");
				return null;
			}

			GSMonthInOut data = XmlConverter.parseMonth(responseMessage);
			
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
			}

			return data;

		}

		@Override
		protected void onPostExecute(GSMonthInOut result)
		{

			super.onPostExecute(result);
			
			if(result == null)
			{
				yi_month_price_loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				yi_month_price_loading_fail.setVisibility(View.GONE);
				setDisplay(result);
			}
			
			yi_month_price_loading_indicator.setVisibility(View.GONE);

		}

	}

}
