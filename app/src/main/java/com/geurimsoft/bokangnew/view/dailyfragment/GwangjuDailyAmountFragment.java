package com.geurimsoft.bokangnew.view.dailyfragment;

import java.text.DecimalFormat;
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
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.data.StatsData;
import com.geurimsoft.bokangnew.client.SocketClient;

public class GwangjuDailyAmountFragment extends Fragment
{
	
	private LinearLayout gwangju_income_empty_layout, gwangju_release_empty_layout, gwangju_petosa_empty_layout;
	private TextView gwangju_stats_daily_date, gwangju_daily_income_title, gwangju_daily_release_title, gwangju_daily_petosa_title;
	
	private int year, month, day;
	
	private DecimalFormat df = new DecimalFormat("#,###");
	
	private LinearLayout gwangju_loading_indicator, gwangju_loading_fail;
	
	private DailyAmountTask dailyAmountTask;
	
	public GwangjuDailyAmountFragment() {

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.gwangjuviewdetaildaily, container, false);
		
		return v;
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		View view = this.getView();
		if(view == null)
			Log.e("BOKANG", "StatsDailyAmountFragment onResume View is null");
		
		this.gwangju_income_empty_layout = (LinearLayout)view.findViewById(R.id.gwangju_income_empty_layout);
		this.gwangju_release_empty_layout = (LinearLayout)view.findViewById(R.id.gwangju_release_empty_layout);
		this.gwangju_petosa_empty_layout = (LinearLayout)view.findViewById(R.id.gwangju_petosa_empty_layout);
		
		this.gwangju_loading_indicator = (LinearLayout)view.findViewById(R.id.gwangju_loading_indicator);
		this.gwangju_loading_fail = (LinearLayout)view.findViewById(R.id.gwangju_loading_fail);
		
		this.gwangju_stats_daily_date = (TextView)view.findViewById(R.id.gwangju_stats_daily_date);
		
		this.gwangju_daily_income_title = (TextView) view.findViewById(R.id.gwangju_daily_income_title);
		this.gwangju_daily_release_title = (TextView) view.findViewById(R.id.gwangju_daily_release_title);
		this.gwangju_daily_petosa_title = (TextView) view.findViewById(R.id.gwangju_daily_petosa_title);
		
		makeDailyAmountData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH,GSConfig.DAY_STATS_DAY);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		dailyAmountTask.cancel(true);
	}
	
	public void makeDailyAmountData(int _year, int _monthOfYear, int _dayOfMonth) {
		String str = _year+"년 "+ _monthOfYear+"월 "+ _dayOfMonth+"일 입출고 현황";
		
		if(gwangju_stats_daily_date == null)
			Log.e("BOKANG", "StatsDailyAmountFragment makeDailyAmountData stats_daily_date is null");
		
		this.gwangju_stats_daily_date.setText(str);
		
		String queryDate = _year+","+_monthOfYear+","+ _dayOfMonth;
		
		dailyAmountTask = new DailyAmountTask(queryDate);
		dailyAmountTask.execute();
	}
	
	private void setDisplayData(StatsData statsData) {
		
		if(getActivity() == null)
			return;
		
		gwangju_income_empty_layout.removeAllViews();
		gwangju_release_empty_layout.removeAllViews();
		gwangju_petosa_empty_layout.removeAllViews();
		
//		StatsView statsView = new StatsView(getActivity(), statsData, null);
//
//		statsView.makeStockStatsView(gwangju_income_empty_layout);
//		gwangju_daily_income_title.setText("입고"+"("+statsData.getStock_several()+"대 : "+statsData.getStock_total()+"루베"+")");
//
//		statsView.makeReleaseStatsView(gwangju_release_empty_layout);
//		gwangju_daily_release_title.setText("출고"+"("+statsData.getRelease_several()+"대 : "+statsData.getRelease_total()+"루베"+")");
//
//		statsView.makePetosaStatsView(gwangju_petosa_empty_layout);
//		gwangju_daily_petosa_title.setText("폐토사"+"("+statsData.getPetosa_several()+"대 : "+statsData.getPetosa_total()+"루베"+")");
		
	}
	
	public class DailyAmountTask extends AsyncTask<String, String, StatsData> {

		private String queryDate;
		
		public DailyAmountTask(String _queryDate) {
			this.queryDate = _queryDate;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			gwangju_loading_indicator.setVisibility(View.VISIBLE);
		
		}
		@Override
		protected StatsData doInBackground(String... params) {
			// TODO Auto-generated method stub
			String department ="1,";
			String messages =  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>statistic_day</GCType><GCQuery>"+department+queryDate+"</GCQuery></GEURIMSOFT>\n";

			SocketClient sc = new SocketClient(AppConfig.SERVER_IP, AppConfig.SERVER_PORT, messages, AppConfig.SOCKET_KEY);
			try {
				sc.start();
				sc.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			StatsData statsData = null;
			String mss = sc.getReturnString();
			
			
			if(mss.equals("Fail")) {
				return statsData;
			}
			
//			statsData = XmlConverter.parseStatsInfo(mss);
			
			return statsData;
		}

		@Override
		protected void onPostExecute(StatsData result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			
			if(result == null) {
				gwangju_loading_fail.setVisibility(View.VISIBLE);
			} else {
				gwangju_loading_fail.setVisibility(View.GONE);
				setDisplayData(result);
			}
			
			gwangju_loading_indicator.setVisibility(View.GONE);
		}
	}
}
