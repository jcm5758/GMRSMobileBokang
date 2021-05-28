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
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.StatsListData;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.client.SocketClient;

public class GwangjuMonthAmountFragment extends Fragment {
	
	private ListView gj_month_amount_listview;
	private TextView gj_month_amount_date;
	private String dateStr;
	private LinearLayout gj_month_amount_header_container, gj_month_amount_loading_indicator, gj_month_amount_loading_fail;
	
	private MonthAmountTask monthAmountTask;
	
	public GwangjuMonthAmountFragment() {
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.gj_month_amount, container,false);

		return v;
	}
	
	@Override
	public void onResume() {

		super.onResume();

		View view = this.getView();
	
		
		this.gj_month_amount_listview = (ListView)view.findViewById(R.id.gj_month_amount_listview);
		this.gj_month_amount_date = (TextView)view.findViewById(R.id.gj_month_amount_date);
				
		this.gj_month_amount_header_container = (LinearLayout)view.findViewById(R.id.gj_month_amount_header_container);
		
		this.gj_month_amount_loading_indicator = (LinearLayout)view.findViewById(R.id.gj_month_amount_loading_indicator);
		this.gj_month_amount_loading_fail = (LinearLayout)view.findViewById(R.id.gj_month_amount_loading_fail);
		
		this.gj_month_amount_listview.setDividerHeight(0);
		
		
		makeMonthAmountData(AppConfig.DAY_STATS_YEAR, AppConfig.DAY_STATS_MONTH);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		monthAmountTask.cancel(true);
	}
	
	private void makeMonthAmountData(int _year, int _monthOfYear) {

		dateStr = _year+"년 "+ _monthOfYear+"월  입출고 현황(단위:루베)";
		gj_month_amount_date.setText(dateStr);
		
		String queryDate = _year+","+_monthOfYear;
		
		monthAmountTask = new MonthAmountTask(queryDate);
		monthAmountTask.execute();
	}
	
	private void setDisplay(StatsListData statsListData) {
		
//		gj_month_amount_header_container.removeAllViews();
//
//		StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(getActivity(), statsListData);
//		statsHeaderAndFooterView.makeHeaderView(gj_month_amount_header_container);
//
//		StAdapter adapter = new StAdapter(getActivity(), statsListData);
//
//		View foot = View.inflate(getActivity(), R.layout.stats_foot, null);
//		LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);
//
//		statsHeaderAndFooterView.makeFooterView(footer_layout);
//		gj_month_amount_listview.addFooterView(foot);
//
//		gj_month_amount_listview.setAdapter(adapter);
	}
	public class MonthAmountTask extends AsyncTask<String, String, StatsListData> {

		private String queryDate;
		private String responseMessage;
		
		public MonthAmountTask(String _queryDate) {
			this.queryDate = _queryDate;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			gj_month_amount_loading_indicator.setVisibility(View.VISIBLE);
			
		}
		
		@Override
		protected StatsListData doInBackground(String... params) {
			// TODO Auto-generated method stub
			String department = "1,";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>statistic_month</GCType><GCQuery>"+department+queryDate+"</GCQuery></GEURIMSOFT>\n";
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
				gj_month_amount_loading_fail.setVisibility(View.VISIBLE);
			} else {
				gj_month_amount_loading_fail.setVisibility(View.GONE);
				setDisplay(result);
			}
			
			gj_month_amount_loading_indicator.setVisibility(View.GONE);
		}
	}
}
