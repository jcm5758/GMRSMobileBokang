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
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.StatsListData;
import com.geurimsoft.data.XmlConverter;
import com.geurimsoft.socket.client.SocketClient;

public class TotalYearAmountFragment extends Fragment{

	private ListView tt_year_amount_listview;
	private TextView tt_year_amount_date;
	private String dateStr;
	private LinearLayout tt_year_amount_header_container, tt_year_amount_loading_indicator, tt_year_amount_loading_fail;
	
	private YearAmountTask yearAmountTask;
	
	public TotalYearAmountFragment() {
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tt_year_amount, container,false);

		return v;
	}
	
	@Override
	public void onResume() {

		super.onResume();

		View view = this.getView();
	
		this.tt_year_amount_listview = (ListView)view.findViewById(R.id.tt_year_amount_listview);
		this.tt_year_amount_date = (TextView)view.findViewById(R.id.tt_year_amount_date);
				
		this.tt_year_amount_header_container = (LinearLayout)view.findViewById(R.id.tt_year_amount_header_container);
		
		this.tt_year_amount_loading_indicator = (LinearLayout)view.findViewById(R.id.tt_year_amount_loading_indicator);
		this.tt_year_amount_loading_fail = (LinearLayout)view.findViewById(R.id.tt_year_amount_loading_fail);
		
		this.tt_year_amount_listview.setDividerHeight(0);
		
		makeYearAmountData(AppConfig.DAY_STATS_YEAR);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		yearAmountTask.cancel(true);
	}
	
	private void makeYearAmountData(int _year) {

		String dateStr = _year + "년  입출고 현황(단위:루베)";
		tt_year_amount_date.setText(dateStr);
		String queryDate = String.valueOf(_year);
		
		yearAmountTask = new YearAmountTask(queryDate, dateStr);
		yearAmountTask.execute();
		
	}
	
private void setDisplay(StatsListData statsListData) {
		
//		tt_year_amount_header_container.removeAllViews();
//
//		StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(getActivity(), statsListData);
//		statsHeaderAndFooterView.makeHeaderView(tt_year_amount_header_container);
//
//		StAdapter adapter = new StAdapter(getActivity(), statsListData);
//
//		View foot = View.inflate(getActivity(), R.layout.stats_foot, null);
//		LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);
//
//		statsHeaderAndFooterView.makeFooterView(footer_layout);
//		tt_year_amount_listview.addFooterView(foot);
//
//		tt_year_amount_listview.setAdapter(adapter);
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
			tt_year_amount_loading_indicator.setVisibility(View.VISIBLE);
			
		}
		
		@Override
		protected StatsListData doInBackground(String... params) {
			// TODO Auto-generated method stub
			String department = "2,";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>statistic_year</GCType><GCQuery>"+department+queryDate+"</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

			try {
				SocketClient sc = new SocketClient(AppConfig.SERVER_IP,
						AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);
				sc.start();
				sc.join();

				responseMessage = sc.getReturnString();

			} catch (Exception e) {
				Log.e(AppConfig.TAG, "StatViewDetailDaily.makeDailyData() : " + e.toString());
				return null;
			}

			StatsListData data = null;
			
			if (responseMessage == null || responseMessage.equals("Fail")) {
				Log.d(AppConfig.TAG, "Returned xml is null.");
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
				tt_year_amount_loading_fail.setVisibility(View.VISIBLE);
			} else {
				tt_year_amount_loading_fail.setVisibility(View.GONE);
				setDisplay(result);
			}
			
			tt_year_amount_loading_indicator.setVisibility(View.GONE);
		}
	}
}
