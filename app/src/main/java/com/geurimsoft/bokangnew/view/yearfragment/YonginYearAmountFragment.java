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
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.data.StatsListData;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.client.SocketClient;

public class YonginYearAmountFragment extends Fragment{

	private ListView yi_year_amount_listview;
	private TextView yi_year_amount_date;
	private String dateStr;
	private LinearLayout yi_year_amount_header_container, yi_year_amount_loading_indicator, yi_year_amount_loading_fail;
	
	private YearAmountTask yearAmountTask;
	
	public YonginYearAmountFragment() {
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.yi_year_amount, container,false);

		return v;
	}
	
	@Override
	public void onResume() {

		super.onResume();

		View view = this.getView();
	
		this.yi_year_amount_listview = (ListView)view.findViewById(R.id.yi_year_amount_listview);
		this.yi_year_amount_date = (TextView)view.findViewById(R.id.yi_year_amount_date);
				
		this.yi_year_amount_header_container = (LinearLayout)view.findViewById(R.id.yi_year_amount_header_container);
		
		this.yi_year_amount_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_year_amount_loading_indicator);
		this.yi_year_amount_loading_fail = (LinearLayout)view.findViewById(R.id.yi_year_amount_loading_fail);
		
		this.yi_year_amount_listview.setDividerHeight(0);
		
		makeYearAmountData(GSConfig.DAY_STATS_YEAR);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		yearAmountTask.cancel(true);
	}
	
	private void makeYearAmountData(int _year) {

		String dateStr = _year + "년  입출고 현황(단위:루베)";
		yi_year_amount_date.setText(dateStr);
		
		String queryDate = String.valueOf(_year);
		
		yearAmountTask = new YearAmountTask(queryDate, dateStr);
		yearAmountTask.execute();
		
	}
	
	private void setDisplay(StatsListData statsListData) {
		
//		yi_year_amount_header_container.removeAllViews();
//
//		StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(getActivity(), statsListData);
//		statsHeaderAndFooterView.makeHeaderView(yi_year_amount_header_container);
//
//		StAdapter adapter = new StAdapter(getActivity(), statsListData);
//
//		View foot = View.inflate(getActivity(), R.layout.stats_foot, null);
//		LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);
//
//		statsHeaderAndFooterView.makeFooterView(footer_layout);
//		yi_year_amount_listview.addFooterView(foot);
//
//		yi_year_amount_listview.setAdapter(adapter);
	}

	public class YearAmountTask extends AsyncTask<String, String, StatsListData> {

		private String queryDate;
		private String responseMessage;
		private String dateStr;
		
		public YearAmountTask(String _queryDate, String _dateStr) {
			this.queryDate = _queryDate;
			this.dateStr = _dateStr;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			yi_year_amount_loading_indicator.setVisibility(View.VISIBLE);
			
		}
		
		@Override
		protected StatsListData doInBackground(String... params) {
			// TODO Auto-generated method stub
			String department = "0,";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>statistic_year</GCType><GCQuery>"+department+queryDate+"</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

			try {
				SocketClient sc = new SocketClient(GSConfig.API_SERVER_ADDR,
						AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);
				sc.start();
				sc.join();

				responseMessage = sc.getReturnString();

			} catch (Exception e) {
				Log.e(GSConfig.APP_DEBUG, "StatViewDetailDaily.doInBackground() : " + e.toString());
				return null;
			}

			StatsListData data = null;
			
			if (responseMessage == null || responseMessage.equals("Fail")) {
				Log.d(GSConfig.APP_DEBUG, "Returned xml is null.");
				return data;
			}
			
			data = XmlConverter.parseStatsListInfo(responseMessage);
			
			try {
				Thread.sleep(10);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return data;
		}

		@Override
		protected void onPostExecute(StatsListData result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			
			if(result == null) {
				yi_year_amount_loading_fail.setVisibility(View.VISIBLE);
			} else {
				yi_year_amount_loading_fail.setVisibility(View.GONE);
				setDisplay(result);
			}
			
			yi_year_amount_loading_indicator.setVisibility(View.GONE);
		}
	}
}
